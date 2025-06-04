package com.ecom.ecommerce.api.v1.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ecom.ecommerce.api.v1.dao.CategoryDAO;
import com.ecom.ecommerce.api.v1.model.Category;

import jakarta.ws.rs.core.Response;

/**
 * Service class to handle business logic related to product categories.
 */
public class CategoryService {
    private static final Logger LOGGER = Logger.getLogger(CategoryService.class.getName());
    private final CategoryDAO categoryDAO = new CategoryDAO();

    /**
     * Retrieves all categories from the database.
     *
     * @return response with list of categories or error message.
     */
    public Response getAllCategories() {
        try {
            List<Category> categories = categoryDAO.getAllCategories();
            return Response.ok(categories).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching all categories: {0}", new Object[]{e.getMessage()});
            return Response.serverError().entity("Internal Server Error").build();
        }
    }

    /**
     * Adds a new category with the provided name.
     *
     * @param category Map containing the category name.
     * @return response with status and message.
     */
    public Response addCategory(Map<String, String> category) {
        LOGGER.log(Level.INFO, "Received request to add category: {0}", new Object[]{category});
        Map<String, Object> response = new HashMap<>();
        try {
            String categoryName = category.get("name");
            String description = category.get("description");
            LOGGER.log(Level.INFO, "Extracted category name: {0}", new Object[]{categoryName});

            if (categoryName == null || categoryName.trim().isEmpty()) {
                LOGGER.warning("Category name is missing or empty.");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Category name is required").build();
            }

            int categoryId = categoryDAO.addCategory(categoryName, description);
            LOGGER.log(Level.INFO, "Category added with ID: {0}", new Object[]{categoryId});

            if (categoryId > 0) {
                response.put("message", "Category added successfully");
                response.put("categoryId", categoryId);
                return Response.status(Response.Status.CREATED)
                        .entity(response).build();
            } else {
                response.put("errorMsg","Failed to add category");
                LOGGER.warning("Failed to add category, DAO returned ID <= 0");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Failed to add category").build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding category: {0}", new Object[]{e.getMessage()});
            return Response.serverError().entity("Internal Server Error").build();
        }
    }

    /**
     * Updates an existing category by ID.
     *
     * @param id   ID of the category to update.
     * @param data Map containing the new name.
     * @return response with status and message.
     */
    public Response updateCategory(int id, Map<String, String> data) {
        Map<String, Object> response = new HashMap<>();
        try {
            String newName = data.get("name");
            String description = data.get("description");

            LOGGER.log(Level.INFO, "Updating category ID: {0} with name: {1}", new Object[]{id, newName});

            boolean updated = categoryDAO.updateCategory(id, newName, description);
            if (updated) {
                response.put("message", "Category updated successfully");
                return Response.ok(response).build();
            } else {
                response.put("errorMsg", "Category not found");
                LOGGER.warning("Category not found with ID: " + id);
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating category ID: {0}, Error: {1}", new Object[]{id, e.getMessage()});
            response.put("errorMsg", "Internal Server Error");
            return Response.serverError().entity(response).build();
        }
    }

    /**
     * Deletes a category by ID.
     *
     * @param id ID of the category to delete.
     * @return response with status and message.
     */
    public Response deleteCategory(int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            LOGGER.info("Deleting category with ID: " + id);
            boolean deleted = categoryDAO.deleteCategory(id);
            if (deleted) {
                response.put("message", "Category deleted successfully");
                return Response.ok(response).build();
            } else {
                response.put("errorMsg", "Category not found");
                LOGGER.warning("Category not found with ID: " + id);
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting category ID: {0}, Error: {1}", new Object[]{id, e.getMessage()});
            response.put("errorMsg", "Internal Server Error");
            return Response.serverError().entity(response).build();
        }
    }
}
