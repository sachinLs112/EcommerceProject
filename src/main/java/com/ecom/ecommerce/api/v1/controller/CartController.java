package com.ecom.ecommerce.api.v1.controller;


import com.ecom.ecommerce.api.v1.service.CartService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

/**
 * Controller for handling cart-related API endpoints.
 */
@Path("/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartController {

    private final CartService cartService = new CartService();

    /**
     * Adds an item to the user's cart.
     *
     * @param cartMap A map containing userId, productId, and quantity.
     * @return Response
     */
    @POST
    @Path("/add")
    public Response addToCart(Map<String, Object> cartMap) {
        return cartService.addToCart(cartMap);
    }

    /**
     * Retrieves all cart items for a given user.
     *
     * @param userId The ID of the user.
     * @return List of cart items for the specified user.
     */
    @GET
    @Path("/{userId}")
    public Response getCartItemsByUser(@PathParam("userId") long userId) {
        return cartService.getCartItemsByUser(userId);
    }

    /**
     * Updates the quantity of a specific item in the cart.
     *
     * @param cartItemId The ID of the cart item.
     * @param item       The CartItem object containing the new quantity.
     * @return
     */
    @PUT
    @Path("/{cartItemId}")
    public Response updateCartItem(@PathParam("cartItemId") long cartItemId, Map<String, Object> item) {
        boolean updated = cartService.updateCartItemQuantity(cartItemId, item);
        if (updated) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     * Deletes a specific item from the cart.
     *
     * @param cartItemId The ID of the cart item to be deleted.
     * @return HTTP 204 No Content if deleted, 404 if item not found.
     */
    @DELETE
    @Path("/{cartItemId}")
    public Response deleteCartItem(@PathParam("cartItemId") long cartItemId) {
        boolean deleted = cartService.deleteCartItem(cartItemId);
        if (deleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     * Calculates the total amount for all items in the user's cart.
     *
     * @param userId The ID of the user.
     * @return The total cart amount as a JSON response.
     */
    @GET
    @Path("/{userId}/total")
    public Response getTotalAmount(@PathParam("userId") long userId) {
        return cartService.getTotalAmount(userId);
    }
}
