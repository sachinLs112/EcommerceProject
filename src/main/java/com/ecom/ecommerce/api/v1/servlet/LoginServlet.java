package com.ecom.ecommerce.api.v1.servlet;

import com.ecom.ecommerce.api.v1.dao.UserDAO;
import com.ecom.ecommerce.api.v1.model.User;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            User user = userDAO.getUserByEmail(username);

            if (user != null && user.getPassword().equals(password)) {
                HttpSession session = req.getSession();
                session.setAttribute("user", user);

                resp.sendRedirect("jsp/home.jsp");
            } else {
                req.setAttribute("error", "Invalid email or password.");
                req.getRequestDispatcher("jsp/login.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during login to the email: {0}, {1}", new Object[]{username, e});
            req.setAttribute("error", "Login error: " + e.getMessage());
            req.getRequestDispatcher("jsp/login.jsp").forward(req, resp);
        }
    }
}
