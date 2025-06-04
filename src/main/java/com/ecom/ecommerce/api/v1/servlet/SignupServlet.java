package com.ecom.ecommerce.api.v1.servlet;

import com.ecom.ecommerce.api.v1.dao.UserDAO;
import com.ecom.ecommerce.api.v1.model.User;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SignupServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(SignupServlet.class.getName());
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setRole("user");

            int userId = userDAO.addUser(user);
            if (userId > 0) {
                resp.sendRedirect("jsp/home.jsp");
            } else {
                req.setAttribute("error", "Signup failed!");
                req.getRequestDispatcher("jsp/signup.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception during signup for email: {0} {1}", new Object[]{ email, e});
            req.setAttribute("error", "Error: " + e.getMessage());
            req.getRequestDispatcher("jsp/signup.jsp").forward(req, resp);
        }
    }
}
