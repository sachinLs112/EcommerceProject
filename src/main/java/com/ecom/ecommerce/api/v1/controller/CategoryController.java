package com.ecom.ecommerce.api.v1.controller;

import com.ecom.ecommerce.api.v1.dao.UserDAO;
import com.ecom.ecommerce.api.v1.model.User;
import com.ecom.ecommerce.api.v1.service.CategoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for handling category-related API endpoints.
 */
@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryController {

    private final CategoryService categoryService = new CategoryService();

    /**
     * Retrieves all available product categories.
     *
     * @return Response containing a list of categories.
     */
    @GET
    public Response getCategories() {
        return categoryService.getAllCategories();
    }

    /**
     * Adds a new category.
     *
     * @param data Map containing category details
     * @return Response indicating success or failure.
     */
    @POST
    public Response addCategory(Map<String, String> data) {
        return categoryService.addCategory(data);
    }

    /**
     * Updates an existing category by its ID.
     *
     * @param categoryId The ID of the category to update.
     * @param data       Map containing updated category details.
     * @return Response indicating the outcome of the update.
     */
    @PUT
    @Path("/{categoryId}")
    public Response updateCategory(@PathParam("categoryId") int categoryId, Map<String, String> data) {
        return categoryService.updateCategory(categoryId, data);
    }

    /**
     * Deletes a category by its ID.
     *
     * @param categoryId The ID of the category to delete.
     * @return Response indicating the result of the deletion.
     */
    @DELETE
    @Path("/{categoryId}")
    public Response deleteCategory(@PathParam("categoryId") int categoryId) {
        return categoryService.deleteCategory(categoryId);
    }

    public static class SignupServlet extends HttpServlet {
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
}
