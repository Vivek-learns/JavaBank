package bank.servlet;


import bank.dao.TransactionDAO;
import bank.model.Transaction;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.List;

@WebServlet("/transactionHistory")
public class TransactionHistoryServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            res.sendRedirect("login"); return;
        }
        try {
            int userId = (int) session.getAttribute("userId");
            List<Transaction> list = new TransactionDAO().getByUserId(userId);
            req.setAttribute("transactions", list);
            req.getRequestDispatcher("transactionHistory.jsp").forward(req, res);
        } catch (Exception e) {
            req.setAttribute("error", "Could not load transactions.");
            req.getRequestDispatcher("transactionHistory.jsp").forward(req, res);
        }
    }
}