package com.ecom.ecommerce.api.v1.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ecom.ecommerce.api.v1.dao.UserDAO;
import com.ecom.ecommerce.api.v1.model.User;

import jakarta.ws.rs.core.Response;

/**
 * Service layer for handling user-related business logic and coordinating with DAO.
 */
public class UserService {
    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
    private final UserDAO userDAO = new UserDAO();

    /**
     * Adds a new user
     *
     * @param userDetails Map containing user information.
     * @return
     */
    public Response addUser(Map<String, Object> userDetails) {
        try {
            User user = new User(userDetails);
            int userId = userDAO.addUser(user);
            if (userId > 0) {
                Map<String, Object> response = new HashMap<>();
                response.put("userId", userId);
                response.put("message", "User added successfully");
                return Response.ok(response).build();
            } else {
                LOGGER.warning("User creation failed");
                return Response.status(Response.Status.BAD_REQUEST).entity("User not created").build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error while adding user", e);
            return Response.serverError().entity("Internal Server Error").build();
        }
    }

    /**
     * Retrieves user details by user ID.
     *
     * @param id User ID to fetch.
     * @return
     */
    public Response getUser(int id) {
        try {
            User user = userDAO.getUser(id);
            if (user != null) {
                return Response.ok(user).build();
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("errorMsg", "Invalid user id");
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(response)
                        .build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error while retrieving user", e);
            return Response.serverError().build();
        }
    }

    /**
     * Retrieves all users from the db.
     *
     * @return returns the all the users.
     */
    public Response getAllUsers() {
        try {
            List<User> users = userDAO.getAllUsers();
            return Response.ok(users).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error while fetching all users", e);
            return Response.serverError().build();
        }
    }

    /**
     * Updates user information by ID.
     *
     * @param id          User ID to update.
     * @param userDetails Map containing updated user data.
     * @return
     */
    public Response updateUser(int id, Map<String, Object> userDetails) {
        try {
            User user = new User(userDetails);
            user.setId(id);
            boolean updated = userDAO.updateUser(user);
            if (updated) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "User updated successfully");
                return Response.ok(response).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error while updating user", e);
            return Response.serverError().build();
        }
    }

    /**
     * Deletes a user by ID.
     *
     * @param id User ID to delete.
     * @return
     */
    public Response deleteUser(int id) {
        try {
            boolean deleted = userDAO.deleteUser(id);
            if (deleted) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "User deleted successfully");
                return Response.ok(response).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error while deleting user", e);
            return Response.serverError().build();
        }
    }
}
