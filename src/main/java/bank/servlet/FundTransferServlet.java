package bank.servlet;


import bank.dao.TransactionDAO;
import bank.dao.UserDAO;
import bank.db.DBconnection;
import bank.model.User;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/fundTransfer")
public class FundTransferServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        if (!isLoggedIn(req)) { res.sendRedirect("login"); return; }
        req.getRequestDispatcher("fundTransfer.jsp").forward(req, res);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        if (!isLoggedIn(req)) { res.sendRedirect("login"); return; }

        HttpSession session = req.getSession();
        int senderId              = (int) session.getAttribute("userId");
        String senderAccNo        = (String) session.getAttribute("accountNumber");
        String receiverAccNo      = req.getParameter("receiverAccount").trim();
        double amount             = Double.parseDouble(req.getParameter("amount"));

        if (amount <= 0) {
            req.setAttribute("error", "Amount must be greater than zero.");
            req.getRequestDispatcher("fundTransfer.jsp").forward(req, res); return;
        }

        if (senderAccNo.equals(receiverAccNo)) {
            req.setAttribute("error", "Cannot transfer to your own account.");
            req.getRequestDispatcher("fundTransfer.jsp").forward(req, res); return;
        }

        Connection conn = null;
        try {
            UserDAO userDAO = new UserDAO();
            User sender   = userDAO.getUserById(senderId);
            User receiver = userDAO.getUserByAccountNumber(receiverAccNo);

            if (receiver == null) {
                req.setAttribute("error", "Receiver account not found.");
                req.getRequestDispatcher("fundTransfer.jsp").forward(req, res); return;
            }

            if (sender.getBalance() < amount) {
                req.setAttribute("error", "Insufficient balance.");
                req.getRequestDispatcher("fundTransfer.jsp").forward(req, res); return;
            }

            // Atomic transaction
            conn = DBconnection.getConnection();
            conn.setAutoCommit(false);

            TransactionDAO txDAO = new TransactionDAO();
            userDAO.updateBalance(conn, senderId,      sender.getBalance()   - amount);
            userDAO.updateBalance(conn, receiver.getId(), receiver.getBalance() + amount);
            txDAO.log(conn, senderId,        "TRANSFER_OUT", amount, receiverAccNo, "Transfer to "   + receiverAccNo);
            txDAO.log(conn, receiver.getId(), "TRANSFER_IN",  amount, senderAccNo,  "Transfer from " + senderAccNo);

            conn.commit();
            session.setAttribute("toast", "₹" + amount + " transferred to " + receiverAccNo + " successfully!");
            res.sendRedirect("dashboard");

        } catch (Exception e) {
            if (conn != null) try { conn.rollback(); } catch (Exception ignored) {}
            req.setAttribute("error", "Transfer failed. Try again.");
            req.getRequestDispatcher("fundTransfer.jsp").forward(req, res);
        } finally {
            if (conn != null) try { conn.close(); } catch (Exception ignored) {}
        }
    }

    private boolean isLoggedIn(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        return s != null && s.getAttribute("userId") != null;
    }
}