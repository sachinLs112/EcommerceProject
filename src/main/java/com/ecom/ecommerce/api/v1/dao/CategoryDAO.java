package com.ecom.ecommerce.api.v1.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ecom.ecommerce.connect.Connect;
import com.ecom.ecommerce.api.v1.model.Category;

/**
 * Data Access Object for handling CRUD operations related to categories.
 */
public class CategoryDAO {

    private static final Logger LOGGER = Logger.getLogger(CategoryDAO.class.getName());
    private final Connect connect = new Connect();

    /**
     * Retrieves all categories from the database.
     *
     * @return List of all categories.
     * @throws SQLException
     */
    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories";

        try (Connection conn = connect.getconnect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categories.add(new Category(rs.getInt("id"), rs.getString("name")));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while retrieving all categories", e);
            throw e;
        }

        return categories;
    }

    /**
     * Adds a new category to the database and returns the generated ID.
     *
     * @param name Name of the category.
     * @return ID of the newly created category, or -1 if insertion failed.
     * @throws SQLException
     */
    public int addCategory(String name, String description) throws SQLException {
        String sql = "INSERT INTO categories (name) VALUES (?) RETURNING id";

        try (Connection conn = connect.getconnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, description);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt("id") : -1;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while adding category", e);
            throw e;
        }
    }

    /**
     * Updates the name of an existing category.
     *
     * @param id   ID of the category to update.
     * @param name New name of the category.
     * @return true if update was successful, false otherwise.
     * @throws SQLException
     */
    public boolean updateCategory(int id, String name, String description) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder("UPDATE categories SET ");
        List<Object> params = new ArrayList<>();

        if (name != null) {
            sqlBuilder.append("name = ?, ");
            params.add(name);
        }
        if (description != null) {
            sqlBuilder.append("description = ?, ");
            params.add(description);
        }

        if (params.size() > 0) {
            sqlBuilder.setLength(sqlBuilder.length() - 2);
        } else {
            return false;
        }

        sqlBuilder.append(" WHERE id = ?");
        params.add(id);

        String sql = sqlBuilder.toString();

        try (Connection conn = connect.getconnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while updating category: ", e);
            throw e;
        }
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id ID of the category to delete.
     * @return true if deletion was successful, false otherwise.
     * @throws SQLException
     */
    public boolean deleteCategory(int id) throws SQLException {
        String sql = "DELETE FROM categories WHERE id = ?";

        try (Connection conn = connect.getconnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while deleting category : ", e);
            throw e;
        }
    }
}
