package dataAccess;

import dataModel.Order;

import java.sql.*;
import java.util.List;

/**
 * Data Access Object for the {@link Order} entity.
 * Inherits basic CRUD operations from {@link AbstractDAO} and adds a custom query.
 */

public class OrderDAO extends AbstractDAO<Order> {

    /**
     * Constructs an OrderDAO with the given database connection.
     *
     * @param connection the database connection
     */

    public OrderDAO(Connection connection) {
        super(connection);
    }

    /**
     * Retrieves all orders associated with a given client ID.
     *
     * @param clientId the ID of the client
     * @return a list of orders made by the specified client
     * @throws SQLException if a database access error occurs
     */

    public List<Order> findByClientId(int clientId) throws SQLException {
        String query = "SELECT * FROM order WHERE client_id = ?";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, clientId);
            try(ResultSet rs = stmt.executeQuery()) {
                return createObjects(rs);
            }
        }
    }
}
