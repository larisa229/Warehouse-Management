package dataAccess;

import dataModel.Product;

import java.sql.*;

/**
 * Data Access Object for the {@link Product} entity.
 * Extends {@link AbstractDAO} and provides additional methods for stock validation and modification.
 */

public class ProductDAO extends AbstractDAO<Product> {

    /**
     * Constructs a ProductDAO with the given database connection.
     *
     * @param connection the connection to the database
     */

    public ProductDAO(Connection connection) {
        super(connection);
    }

    /**
     * Checks if there is sufficient stock available for a given product.
     *
     * @param productId the ID of the given product
     * @param quantity the desired quantity to check for
     * @return {@code true} if there is enough stock available and {@code false} otherwise
     * @throws SQLException if a database access error occurs
     */

    public boolean checkStock(int productId, int quantity) throws SQLException {
        String query = "SELECT current_stock >= ? FROM product WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getBoolean(1);
            }
        }
    }

    /**
     * Decrements the stock for a given product by the desired quantity.
     *
     * @param productId the ID of the product
     * @param quantity the amount to decrement
     * @throws SQLException if there is insufficient stock or the product ID is invalid
     */

    public void decrementStock(int productId, int quantity) throws SQLException {
        String query = "UPDATE product SET current_stock = current_stock - ? WHERE id = ? AND current_stock >= ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Under-stock or invalid product ID");
            }
        }
    }
}
