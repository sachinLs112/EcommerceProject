package com.ecom.ecommerce.api.v1.controller;

import java.util.Map;

import com.ecom.ecommerce.api.v1.service.UserService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Controller for handling user-related API endpoints.
 */
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    private final UserService userService = new UserService();

    /**
     * To add the user.
     *
     * @param userInfo map containing user details
     * @return
     */
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(Map<String, Object> userInfo) {
        return userService.addUser(userInfo);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return Specified user details
     */
    @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") int id) {
        return userService.getUser(id);
    }

    /**
     * Retrieves all users in the db.
     *
     * @return A list of all users.
     */
    @GET
    public Response getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Updates an existing user's information.
     *
     * @param id       The ID of the user to identify.
     * @param userInfo A map containing updated user information.
     * @return
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") int id, Map<String, Object> userInfo) {
        return userService.updateUser(id, userInfo);
    }

    @PUT
    @Path("/test")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response testPut(String body) {
        return Response.ok("Received: " + body).build();
    }

    
    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to identify.
     * @return
     */
    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") int id) {
        return userService.deleteUser(id);
    }
}
