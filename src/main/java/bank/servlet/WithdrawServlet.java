package bank.servlet;


import bank.dao.TransactionDAO;
import bank.dao.UserDAO;
import bank.model.User;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/withdraw")
public class WithdrawServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        if (!isLoggedIn(req)) { res.sendRedirect("login"); return; }
        req.getRequestDispatcher("withdraw.jsp").forward(req, res);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        if (!isLoggedIn(req)) { res.sendRedirect("login"); return; }

        int userId = (int) req.getSession().getAttribute("userId");
        double amount = Double.parseDouble(req.getParameter("amount"));

        if (amount <= 0) {
            req.setAttribute("error", "Amount must be greater than zero.");
            req.getRequestDispatcher("withdraw.jsp").forward(req, res); return;
        }

        try {
            UserDAO userDAO = new UserDAO();
            User user = userDAO.getUserById(userId);

            if (user.getBalance() < amount) {
                req.setAttribute("error", "Insufficient balance.");
                req.getRequestDispatcher("withdraw.jsp").forward(req, res); return;
            }

            userDAO.updateBalance(userId, user.getBalance() - amount);
            new TransactionDAO().log(userId, "WITHDRAW", amount, null, "Withdrawal");
            req.getSession().setAttribute("toast", "₹" + amount + " withdrawn successfully!");
            res.sendRedirect("dashboard");
        } catch (Exception e) {
            req.setAttribute("error", "Withdrawal failed. Try again.");
            req.getRequestDispatcher("withdraw.jsp").forward(req, res);
        }
    }

    private boolean isLoggedIn(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        return s != null && s.getAttribute("userId") != null;
    }
}