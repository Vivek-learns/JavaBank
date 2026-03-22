package bank.servlet;


import bank.dao.UserDAO;
import bank.model.User;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.getRequestDispatcher("login.jsp").forward(req, res);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String email    = req.getParameter("email").trim();
        String password = req.getParameter("password").trim();

        try {
            User user = new UserDAO().login(email, password);
            if (user != null) {
                HttpSession session = req.getSession();
                session.setAttribute("userId",        user.getId());
                session.setAttribute("userName",      user.getFullName());
                session.setAttribute("accountNumber", user.getAccountNumber());
                res.sendRedirect("dashboard");
            } else {
                req.setAttribute("error", "Invalid email or password.");
                req.getRequestDispatcher("login.jsp").forward(req, res);
            }
        } catch (Exception e) {
            req.setAttribute("error", "Something went wrong.");
            req.getRequestDispatcher("login.jsp").forward(req, res);
        }
    }
}
