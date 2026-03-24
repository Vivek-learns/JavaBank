package bank.servlet;

import bank.dao.TransactionDAO;
import bank.model.Transaction;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin")
public class AdminPanelServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("email") == null) {
            res.sendRedirect("login");
            return;
        }

        String email = (String) session.getAttribute("email");
        if (!"admin@javabank.com".equals(email)) {
            // Not an admin, redirect back to dashboard securely
            res.sendRedirect("dashboard");
            return;
        }

        try {
            TransactionDAO txDAO = new TransactionDAO();
            List<Transaction> allTx = txDAO.getAllTransactions();
            req.setAttribute("transactions", allTx);
            req.getRequestDispatcher("adminPanel.jsp").forward(req, res);
        } catch (SQLException e) {
            req.setAttribute("error", "Failed to load transactions.");
            req.getRequestDispatcher("dashboard").forward(req, res);
        }
    }
}
