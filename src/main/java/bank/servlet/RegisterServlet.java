package bank.servlet;


import bank.dao.UserDAO;
import bank.model.User;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.getRequestDispatcher("register.jsp").forward(req, res);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String fullName = req.getParameter("fullName").trim();
        String email    = req.getParameter("email").trim();
        String password = req.getParameter("password").trim();
        String confirm  = req.getParameter("confirmPassword").trim();

        if (!password.equals(confirm)) {
            req.setAttribute("error", "Passwords do not match.");
            req.getRequestDispatcher("register.jsp").forward(req, res);
            return;
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(password); // hash in production!

        try {
            boolean success = new UserDAO().register(user);
            if (success) {
                res.sendRedirect("login?msg=registered");
            } else {
                req.setAttribute("error", "Registration failed. Try again.");
                req.getRequestDispatcher("register.jsp").forward(req, res);
            }
        } catch (Exception e) {
            req.setAttribute("error", "Email already exists.");
            req.getRequestDispatcher("register.jsp").forward(req, res);
        }
    }
}