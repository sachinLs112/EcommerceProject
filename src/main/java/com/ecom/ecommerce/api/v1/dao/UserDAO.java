package com.ecom.ecommerce.api.v1.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.ecom.ecommerce.api.v1.model.User;
import com.ecom.ecommerce.connect.Connect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data Access Object for performing CRUD operations on the users table.
 */
public class UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);
    private final Connect connect = new Connect();

    /**
     * Adds a new user details to the database.
     *
     * @param user The user object to be added.
     * @return The generated user ID, or -1 if the operation fails.
     * @throws SQLException
     */
    public int addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (name, password, email, role) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection conn = connect.getconnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getRole());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                logger.info("User added with ID: {}", id);
                return id;
            }
        } catch (SQLException e) {
            logger.error("Error while adding user: {}", e.getMessage(), e);
            throw e;
        }
        return -1;
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id The ID of the user to fetch.
     * @return The User object if found, otherwise null.
     * @throws SQLException
     */
    public User getUser(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = connect.getconnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                logger.info("User fetched with ID: {}", id);
                return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            logger.error("Error fetching user by ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
        return null;
    }

    /**
     * Retrieves all users from the database.
     *
     * @return A list of User objects.
     * @throws SQLException
     */
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = connect.getconnect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("role")
                ));
            }
            logger.info("Total users fetched: {}", users.size());
        } catch (SQLException e) {
            logger.error("Error fetching all users: {}", e.getMessage(), e);
            throw e;
        }
        return users;
    }

    /**
     * Updates an existing user's information.
     *
     * @param user The user object with updated data.
     * @return true if user update was successful, false otherwise.
     * @throws SQLException
     */
    public boolean updateUser(User user) throws SQLException {
        StringBuilder sql = new StringBuilder("UPDATE users SET ");
        List<Object> params = new ArrayList<>();

        if (user.getName() != null) {
            sql.append("name = ?, ");
            params.add(user.getName());
        }
        if (user.getPassword() != null) {
            sql.append("password = ?, ");
            params.add(user.getPassword());
        }
        if (user.getEmail() != null) {
            sql.append("email = ?, ");
            params.add(user.getEmail());
        }
        if (user.getRole() != null) {
            sql.append("role = ?, ");
            params.add(user.getRole());
        }

        if (params.isEmpty()) {
            logger.warn("No fields provided for update for user ID: {}", user.getId());
            return false;
        }

        sql.setLength(sql.length() - 2); // remove last comma
        sql.append(" WHERE id = ?");
        params.add(user.getId());

        try (Connection conn = connect.getconnect();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            boolean success = ps.executeUpdate() > 0;
            if (success) {
                logger.info("User updated with ID: {}", user.getId());
            } else {
                logger.warn("No user found to update with ID: {}", user.getId());
            }
            return success;

        } catch (SQLException e) {
            logger.error("Error updating user ID {}: {}", user.getId(), e.getMessage(), e);
            throw e;
        }
    }


    /**
     * Deletes a user from the database.
     *
     * @param id The ID of the user to delete.
     * @return true if deletion was successful, false otherwise.
     * @throws SQLException
     */
    public boolean deleteUser(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = connect.getconnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            boolean success = ps.executeUpdate() > 0;
            if (success) {
                logger.info("User deleted with ID: {}", id);
            } else {
                logger.warn("No user found to delete with ID: {}", id);
            }
            return success;
        } catch (SQLException e) {
            logger.error("Error deleting user ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email The email address of the user.
     * @return The User object if found, otherwise null.
     * @throws SQLException
     */
    public User getUserByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = connect.getconnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                logger.info("User fetched with email: {}", email);
                return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            logger.error("Error fetching user by email {}: {}", email, e.getMessage(), e);
            throw e;
        }
        return null;
    }
}
