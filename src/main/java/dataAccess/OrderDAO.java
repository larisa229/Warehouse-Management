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
}
