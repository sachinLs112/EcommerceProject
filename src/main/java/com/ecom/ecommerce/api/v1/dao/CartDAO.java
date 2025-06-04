package com.ecom.ecommerce.api.v1.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ecom.ecommerce.api.v1.model.CartItem;
import com.ecom.ecommerce.connect.Connect;

/**
 * DAO class for managing cart-related database operations.
 */
public class CartDAO {
    private static final Logger LOGGER = Logger.getLogger(CartDAO.class.getName());
    private final Connect conn = new Connect();

    /**
     * Adds a cart item to the database or updates the quantity and total price if it already exists.
     *
     * @param cartItem The cart item to be added or updated.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean addToCart(CartItem cartItem) {
        String insertCartSql = "INSERT INTO cart_items (user_id, product_id, quantity, total_price, created_at) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP) " +
                "ON CONFLICT (user_id, product_id) DO UPDATE SET quantity = cart_items.quantity + EXCLUDED.quantity, total_price = cart_items.total_price + EXCLUDED.total_price";

        String updateStockSql = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";

        try (Connection connection = conn.getconnect()) {
            connection.setAutoCommit(false);

            try (PreparedStatement psCart = connection.prepareStatement(insertCartSql)) {
                psCart.setLong(1, cartItem.getUserId());
                psCart.setLong(2, cartItem.getProductId());
                psCart.setInt(3, cartItem.getQuantity());
                psCart.setLong(4, cartItem.getTotalPrice());
                int cartRows = psCart.executeUpdate();

                if (cartRows == 0) {
                    connection.rollback();
                    return false;
                }
            }

            try (PreparedStatement psStock = connection.prepareStatement(updateStockSql)) {
                psStock.setInt(1, cartItem.getQuantity());
                psStock.setLong(2, cartItem.getProductId());
                psStock.setInt(3, cartItem.getQuantity());
                int stockRows = psStock.executeUpdate();

                if (stockRows == 0) {
                    connection.rollback();
                    return false;
                }
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while adding item to cart and updating stock", e);
            return false;
        }
    }


    /**
     * Updates the quantity and recalculates the total price of a specific cart item.
     *
     * @param cartItemId ID of the cart item to update.
     * @param newQuantity   New quantity to be set.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateCartItemQuantity(long cartItemId, int newQuantity) {
        String selectSql = "SELECT quantity, total_price, product_id FROM cart_items WHERE id = ?";
        String updateCartSql = "UPDATE cart_items SET quantity = ?, total_price = ? WHERE id = ?";
        String deleteCartSql = "DELETE FROM cart_items WHERE id = ?";
        String updateProductStockSql = "UPDATE products SET stock = stock + ? WHERE id = ?";

        try (Connection connection = conn.getconnect()) {
            connection.setAutoCommit(false);

            try (PreparedStatement selectPs = connection.prepareStatement(selectSql)) {
                selectPs.setLong(1, cartItemId);
                ResultSet rs = selectPs.executeQuery();

                if (!rs.next()) {
                    connection.rollback();
                    return false;
                }

                int oldQuantity = rs.getInt("quantity");
                long oldTotalPrice = rs.getLong("total_price");
                int productId = rs.getInt("product_id");

                if (oldQuantity == 0) {
                    connection.rollback();
                    return false;
                }

                long pricePerUnit = oldTotalPrice / oldQuantity;

                int quantityDifference = newQuantity - oldQuantity;

                if (newQuantity > 0) {
                    long newTotalPrice = pricePerUnit * newQuantity;

                    try (PreparedStatement updateCartPs = connection.prepareStatement(updateCartSql)) {
                        updateCartPs.setInt(1, newQuantity);
                        updateCartPs.setLong(2, newTotalPrice);
                        updateCartPs.setLong(3, cartItemId);
                        int updatedRows = updateCartPs.executeUpdate();
                        if (updatedRows == 0) {
                            connection.rollback();
                            return false;
                        }
                    }
                } else {
                    try (PreparedStatement deleteCartPs = connection.prepareStatement(deleteCartSql)) {
                        deleteCartPs.setLong(1, cartItemId);
                        int deletedRows = deleteCartPs.executeUpdate();
                        if (deletedRows == 0) {
                            connection.rollback();
                            return false;
                        }
                    }
                }

                try (PreparedStatement updateStockPs = connection.prepareStatement(updateProductStockSql)) {
                    updateStockPs.setInt(1, -quantityDifference);
                    updateStockPs.setInt(2, productId);
                    int updatedStockRows = updateStockPs.executeUpdate();
                    if (updatedStockRows == 0) {
                        connection.rollback();
                        return false;
                    }
                }

                connection.commit();
                return true;

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while updating cart item and product stock", e);
            return false;
        }
    }


    /**
     * Deletes a cart item from the database by its ID.
     *
     * @param cartItemId ID of the cart item to delete.
     * @return true if the deletion was successful, false otherwise.
     */
    public boolean deleteCartItem(long cartItemId) {
        String sql = "DELETE FROM cart_items WHERE id = ?";
        try (Connection connection = conn.getconnect();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, cartItemId);
            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while deleting cart item", e);
            return false;
        }
    }

    /**
     * Retrieves all cart items associated with a specific user.
     *
     * @param userId ID of the user.
     * @return List of cart items for the specified user.
     */
    public List<CartItem> getCartItemsByUser(long userId) {
        List<CartItem> cartItems = new ArrayList<>();
        String sql = "SELECT * FROM cart_items WHERE user_id = ?";

        try (Connection connection = conn.getconnect();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CartItem item = new CartItem();
                item.setUserId(rs.getInt("user_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setTotalPrice(rs.getLong("total_price"));
                cartItems.add(item);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while retrieving cart items by user", e);
        }
        return cartItems;
    }

    /**
     * Calculates the total amount of all cart items for a specific user.
     *
     * @param userId ID of the user.
     * @return The total amount for the user's cart.
     */
    public long getTotalAmount(long userId) {
        String sql = "SELECT COALESCE(SUM(total_price), 0) AS total_amount FROM cart_items WHERE user_id = ?";
        long totalAmount = 0;

        try (Connection connection = conn.getconnect();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalAmount = rs.getLong("total_amount");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while calculating total amount", e);
        }
        return totalAmount;
    }
}
