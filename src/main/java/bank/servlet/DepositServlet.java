package bank.servlet;


import bank.dao.TransactionDAO;
import bank.dao.UserDAO;
import bank.model.User;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/deposit")
public class DepositServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        if (!isLoggedIn(req)) { res.sendRedirect("login"); return; }
        req.getRequestDispatcher("deposit.jsp").forward(req, res);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        if (!isLoggedIn(req)) { res.sendRedirect("login"); return; }

        int userId = (int) req.getSession().getAttribute("userId");
        double amount = Double.parseDouble(req.getParameter("amount"));

        if (amount <= 0) {
            req.setAttribute("error", "Amount must be greater than zero.");
            req.getRequestDispatcher("deposit.jsp").forward(req, res); return;
        }

        try {
            UserDAO userDAO = new UserDAO();
            User user = userDAO.getUserById(userId);
            double newBalance = user.getBalance() + amount;
            userDAO.updateBalance(userId, newBalance);
            new TransactionDAO().log(userId, "DEPOSIT", amount, null, "Deposit");
            req.getSession().setAttribute("toast", "₹" + amount + " deposited successfully!");
            res.sendRedirect("dashboard");
        } catch (Exception e) {
            req.setAttribute("error", "Deposit failed. Try again.");
            req.getRequestDispatcher("deposit.jsp").forward(req, res);
        }
    }

    private boolean isLoggedIn(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        return s != null && s.getAttribute("userId") != null;
    }
}
