package com.ecom.ecommerce.api.v1.controller;

import com.ecom.ecommerce.api.v1.service.CategoryService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

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
}
