package com.ecom.ecommerce.api.v1.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ecom.ecommerce.connect.Connect;
import com.ecom.ecommerce.api.v1.model.Product;

/**
 * DAO for handling CRUD operations on the products table.
 */
public class ProductDAO {

    private static final Logger LOGGER = Logger.getLogger(ProductDAO.class.getName());
    private final Connect connect = new Connect();

    /**
     * Adds a new product to the database.
     *
     * @param product Product object to be added.
     * @return ID of the newly created product, or -1 if insertion fails.
     * @throws SQLException in case of database errors.
     */
    public int addProduct(Product product) throws SQLException {
        String sql = "INSERT INTO products (name, description, image_url, price, stock, category_id) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = connect.getconnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setString(3, product.getImageUrl());
            ps.setLong(4, product.getPrice());
            ps.setLong(5, product.getStock());
            ps.setInt(6, product.getCategoryId());

            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt("id") : -1;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to add product: {0}, Error: {1}", new Object[]{product.getName(), e.getMessage()});
            throw e;
        }
    }

    /**
     * Fetches all products under a specific category.
     *
     * @param categoryId Category ID.
     * @return List of products.
     * @throws SQLException in case of database errors.
     */
    public List<Product> getProductsByCategory(int categoryId) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE category_id = ?";

        try (Connection conn = connect.getconnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("image_url"),
                        rs.getLong("price"),
                        rs.getInt("category_id")
                ));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to fetch products by category ID: {0}, Error: {1}",
                    new Object[]{categoryId, e.getMessage()});
            throw e;
        }

        return products;
    }

    /**
     * Retrieves all products from the database.
     *
     * @return List of all products.
     * @throws SQLException in case of database errors.
     */
    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Connection conn = connect.getconnect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("image_url"),
                        rs.getLong("price"),
                        rs.getInt("category_id")
                ));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to retrieve all products. Error: {0}", new Object[]{e.getMessage()});
            throw e;
        }

        return products;
    }

    /**
     * Updates the given product details by ID.
     *
     * @param product Product object with updated values.
     * @return true if update was successful, false otherwise.
     * @throws SQLException in case of database errors.
     */
    public boolean updateProduct(Product product) throws SQLException {
        StringBuilder sql = new StringBuilder("UPDATE products SET ");
        List<Object> params = new ArrayList<>();

        if (product.getName() != null) {
            sql.append("name = ?, ");
            params.add(product.getName());
        }
        if (product.getDescription() != null) {
            sql.append("description = ?, ");
            params.add(product.getDescription());
        }
        if (product.getImageUrl() != null) {
            sql.append("image_url = ?, ");
            params.add(product.getImageUrl());
        }
        if (product.getPrice() != -1L) {
            sql.append("price = ?, ");
            params.add(product.getPrice());
        }
        if (product.getCategoryId() != -1L) {
            sql.append("category_id = ?, ");
            params.add(product.getCategoryId());
        }

        // Remove trailing comma
        if (params.isEmpty()) {
            LOGGER.warning("No fields provided to update for product ID: " + product.getId());
            return false;
        }
        sql.setLength(sql.length() - 2);
        sql.append(" WHERE id = ?");
        params.add(product.getId());

        try (Connection conn = connect.getconnect();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            boolean updated = ps.executeUpdate() > 0;
            if (updated) {
                LOGGER.log(Level.INFO, "Product updated dynamically with ID: {0}", product.getId());
            } else {
                LOGGER.log(Level.WARNING, "No product found to update with ID: {0}", product.getId());
            }
            return updated;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to update product with ID: " + product.getId(), e);
            throw e;
        }
    }


    /**
     * Deletes a product by its ID.
     *
     * @param id Product ID.
     * @return true if deletion was successful, false otherwise.
     * @throws SQLException in case of database errors.
     */
    public boolean deleteProduct(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";

        try (Connection conn = connect.getconnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to delete product with ID: {0}, Error: {1}",
                    new Object[]{id, e.getMessage()});
            throw e;
        }
    }
}
