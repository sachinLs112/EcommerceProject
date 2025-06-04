package com.ecom.ecommerce.api.v1.controller;

import com.ecom.ecommerce.api.v1.service.ProductService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

/**
 * Controller for handling products-related API endpoints.
 */
@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductController {

    private final ProductService productService = new ProductService();

    /**
     * Retrieves all products available in the database.
     *
     * @return Response containing a list of all products.
     */
    @GET
    public Response getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * Retrieves all products under a specific category.
     *
     * @param categoryId ID of the category to filter products.
     * @return Response containing a list of products in the specified category.
     */
    @GET
    @Path("/category/{categoryId}")
    public Response getProductsByCategory(@PathParam("categoryId") int categoryId) {
        return productService.getProductsByCategory(categoryId);
    }

    /**
     * Adds a new product to the database.
     *
     * @param product Map containing product details like name, price, category, etc.
     * @return Response indicating success or failure of the operation.
     */
    @POST
    public Response addProduct(Map<String, Object> product) {
        return productService.addProduct(product);
    }

    /**
     * Updates the details of an existing product.
     *
     * @param productId      ID of the product to update.
     * @param productDetails Map containing updated product details.
     * @return Response indicating success or failure of the update.
     */
    @PUT
    @Path("/{productId}")
    public Response updateProduct(@PathParam("productId") int productId, Map<String, Object> productDetails) {
        return productService.updateProduct(productId, productDetails);
    }

    /**
     * Deletes a product from the system.
     *
     * @param productId ID of the product to delete.
     * @return Response indicating success or failure of the deletion.
     */
    @DELETE
    @Path("/{productId}")
    public Response deleteProduct(@PathParam("productId") int productId) {
        return productService.deleteProduct(productId);
    }

}
