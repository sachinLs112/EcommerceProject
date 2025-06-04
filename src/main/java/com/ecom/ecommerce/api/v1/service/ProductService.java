package com.ecom.ecommerce.api.v1.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ecom.ecommerce.api.v1.dao.ProductDAO;
import com.ecom.ecommerce.api.v1.model.Product;

import jakarta.ws.rs.core.Response;

/**
 * Service class for handling business logic related to products.
 */
public class ProductService {

    private static final Logger LOGGER = Logger.getLogger(ProductService.class.getName());
    private final ProductDAO productDAO = new ProductDAO();

    /**
     * Retrieves all products from the database.
     *
     * @return response with list of products.
     */
    public Response getAllProducts() {
        try {
            List<Product> products = productDAO.getAllProducts();
            return Response.ok(products).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching all products: {0}", new Object[]{e.getMessage()});
            return Response.serverError().entity("Internal Server Error").build();
        }
    }

    /**
     * Retrieves all products by category ID.
     *
     * @param categoryId ID of the category to filter products.
     * @return response with list of products or error message.
     */
    public Response getProductsByCategory(int categoryId) {
        try {
            List<Product> products = productDAO.getProductsByCategory(categoryId);
            return Response.ok(products).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching products for category ID: {0}, Error: {1}",
                    new Object[]{categoryId, e.getMessage()});
            return Response.serverError().entity("Internal Server Error").build();
        }
    }

    /**
     * Adds a new product using the provided details.
     *
     * @param productDetails Map containing product data.
     * @return
     */
    public Response addProduct(Map<String, Object> productDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            Product product = new Product(productDetails);
            LOGGER.log(Level.INFO, "Adding product: {0}", new Object[]{productDetails});

            int id = productDAO.addProduct(product);
            if (id > 0) {
                response.put("message", "Product added successfully");
                response.put("productId", id);
                return Response.status(Response.Status.CREATED).entity(response).build();
            } else {
                LOGGER.log(Level.WARNING, "Failed to add product: {0}", new Object[]{productDetails});
                response.put("errorMsg", "Failed to add product");
                return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding product: {0}", new Object[]{e.getMessage()});
            response.put("errorMsg", "Internal Server Error");
            return Response.serverError().entity(response).build();
        }
    }


    /**
     * Updates a product by ID with the provided details.
     *
     * @param id Product ID.
     * @param productDetails Map containing updated product data.
     * @return response indicating update status
     */
    public Response updateProduct(int id, Map<String, Object> productDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            Product product = new Product(productDetails);
            product.setId(id);
            LOGGER.log(Level.INFO, "Updating product with ID: {0}", new Object[]{id});

            boolean updated = productDAO.updateProduct(product);
            if (updated) {
                response.put("message", "Product updated successfully");
                return Response.ok(response).build();
            } else {
                LOGGER.log(Level.WARNING, "Product not found with ID: {0}", new Object[]{id});
                response.put("errorMsg", "Product not found");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating product with ID: {0}, Error: {1}",
                    new Object[]{id, e.getMessage()});
            response.put("errorMsg", "Internal Server Error");
            return Response.serverError().entity(response).build();
        }
    }

    /**
     * Deletes a product by ID.
     *
     * @param id Product ID.
     * @return response indicating deletion status
     */
    public Response deleteProduct(int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            LOGGER.log(Level.INFO, "Deleting product with ID: {0}", new Object[]{id});
            boolean deleted = productDAO.deleteProduct(id);
            if (deleted) {
                response.put("message", "Product deleted successfully");
                return Response.ok(response).build();
            } else {
                LOGGER.log(Level.WARNING, "Product not found with ID: {0}", new Object[]{id});
                response.put("errorMsg", "Product not found");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting product with ID: {0}, Error: {1}",
                    new Object[]{id, e.getMessage()});
            response.put("errorMsg", "Internal Server Error");
            return Response.serverError().entity(response).build();
        }
    }
}
