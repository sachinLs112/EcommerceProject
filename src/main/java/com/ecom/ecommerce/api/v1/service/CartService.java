package com.ecom.ecommerce.api.v1.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ecom.ecommerce.api.v1.dao.CartDAO;
import com.ecom.ecommerce.api.v1.model.CartItem;

import jakarta.ws.rs.core.Response;
import org.json.JSONObject;

/**
 * Service class responsible for managing cart-related operations.
 */
public class CartService {
    private static final Logger LOGGER = Logger.getLogger(CartService.class.getName());
    private final CartDAO cartDAO = new CartDAO();

    /**
     * Adds an item to the user's cart.
     *
     * @param request Map containing userId, productId, quantity, totalPrice.
     * @return HTTP Response indicating success or failure.
     */
    public Response addToCart(Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (request.get("userId") == null || request.get("productId") == null ||
                    request.get("quantity") == null || request.get("totalPrice") == null) {
                LOGGER.warning("Missing required fields in addToCart request");
                response.put("errorMsg", "Missing required fields");
                return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
            }

            CartItem cartItem = new CartItem(request);

            if (cartItem.getQuantity() <= 0 || cartItem.getTotalPrice() <= 0) {
                LOGGER.warning("Invalid quantity or totalPrice in request");
                response.put("errorMsg", "Quantity and totalPrice must be positive");
                return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
            }

            boolean success = cartDAO.addToCart(cartItem);
            if (success) {
                LOGGER.info("Cart item added successfully for userId: " + cartItem.getUserId());
                response.put("message", "Item added to cart successfully");
                return Response.ok(response).build();
            } else {
                LOGGER.severe("Failed to add item to cart for userId: " + cartItem.getUserId());
                response.put("errorMsg", "Failed to add item to cart");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
            }

        } catch (ClassCastException | NullPointerException e) {
            LOGGER.log(Level.WARNING, "Invalid input data types in addToCart", e);
            response.put("errorMsg", "Invalid input data types");
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while adding to cart", e);
            response.put("errorMsg", "Internal server error");
            return Response.serverError().entity(response).build();
        }
    }

    /**
     * Updates the quantity of a specific cart item.
     *
     * @param cartItemId The ID of the cart item.
     * @param items A map containing the new quantity.
     * @return True if updated successfully, false otherwise.
     */
    public boolean updateCartItemQuantity(long cartItemId, Map<String, Object> items) {
        CartItem cartItem = new CartItem(items);
        if (cartItem.getQuantity() <= 0) {
            LOGGER.warning("Attempted to update cart item with non-positive quantity");
            return false;
        }
        boolean result = cartDAO.updateCartItemQuantity(cartItemId, cartItem.getQuantity());
        LOGGER.info("Cart item quantity update result: " + result + " for ID: " + cartItemId);
        return result;
    }

    /**
     * Deletes a specific item from the cart.
     *
     * @param cartItemId The ID of the cart item.
     * @return True if deleted successfully, false otherwise.
     */
    public boolean deleteCartItem(long cartItemId) {
        boolean result = cartDAO.deleteCartItem(cartItemId);
        LOGGER.info("Cart item deletion result: " + result + " for ID: " + cartItemId);
        return result;
    }

    /**
     * Retrieves all cart items for a given user.
     *
     * @param userId The ID of the user.
     * @return List of cart items for the specified user.
     */
    public Response getCartItemsByUser(long userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<CartItem> items = cartDAO.getCartItemsByUser(userId);
            LOGGER.info("Retrieved " + items.size() + " cart items for userId: " + userId);
            response.put("items", items);
            return Response.ok(response).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving cart items for userId: " + userId, e);
            response.put("errorMsg", "Internal server error");
            return Response.serverError().entity(response).build();
        }
    }

    /**
     * Retrieves the total amount for the user's cart.
     *
     * @param userId The ID of the user.
     * @return Total amount wrapped in a JSON response.
     */
    public Response getTotalAmount(long userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            long totalAmount = cartDAO.getTotalAmount(userId);
            LOGGER.info("Total amount for userId " + userId + ": " + totalAmount);
            response.put("totalAmount", totalAmount);
            return Response.ok(response).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving total amount for userId: " + userId, e);
            response.put("errorMsg", "Internal server error");
            return Response.serverError().entity(response).build();
        }
    }
}
