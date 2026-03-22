package bank.servlet;


import bank.dao.UserDAO;
import bank.model.User;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            res.sendRedirect("login"); return;
        }
        try {
            int userId = (int) session.getAttribute("userId");
            User user = new UserDAO().getUserById(userId);
            req.setAttribute("user", user);
            req.getRequestDispatcher("dashboard.jsp").forward(req, res);
        } catch (Exception e) {
            res.sendRedirect("login");
        }
    }
}