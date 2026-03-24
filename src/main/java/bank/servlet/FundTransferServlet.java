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
        String action             = req.getParameter("action");
        String receiverAccNo      = req.getParameter("receiverAccount");
        String message            = req.getParameter("message");
        String amountStr          = req.getParameter("amount");

        if (receiverAccNo != null) receiverAccNo = receiverAccNo.trim();
        if (message != null) message = message.trim();
        
        double amount = 0;
        if (amountStr != null && !amountStr.trim().isEmpty()) {
            amount = Double.parseDouble(amountStr);
        }

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

            if (!"confirm".equals(action)) {
                req.setAttribute("receiverName", receiver.getFullName());
                req.setAttribute("receiverAccount", receiverAccNo);
                req.setAttribute("amount", amountStr);
                req.setAttribute("message", message);
                req.getRequestDispatcher("confirmTransfer.jsp").forward(req, res);
                return;
            }

            // Atomic transaction (Confirmed)
            conn = DBconnection.getConnection();
            conn.setAutoCommit(false);

            TransactionDAO txDAO = new TransactionDAO();
            userDAO.updateBalance(conn, senderId,      sender.getBalance()   - amount);
            userDAO.updateBalance(conn, receiver.getId(), receiver.getBalance() + amount);
            String senderDesc = "Transfer to " + receiver.getFullName() + " (" + receiverAccNo + ")" + (message != null && !message.isEmpty() ? " - " + message : "");
            String receiverDesc = "Transfer from " + sender.getFullName() + " (" + senderAccNo + ")" + (message != null && !message.isEmpty() ? " - " + message : "");

            txDAO.log(conn, senderId,        "TRANSFER_OUT", amount, receiverAccNo, senderDesc);
            txDAO.log(conn, receiver.getId(), "TRANSFER_IN",  amount, senderAccNo,  receiverDesc);

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