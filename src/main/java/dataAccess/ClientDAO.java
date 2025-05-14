package dataAccess;

import dataModel.Client;

import java.sql.*;

/**
 * Data Access Object for {@link Client}.
 * Extends {@link AbstractDAO} to provide basic CRUD operations
 * and adds custom queries such as {@code findByName}.
 */

public class ClientDAO extends AbstractDAO<Client> {

    /**
     * Constructs a ClientDAO with a given database connection.
     *
     * @param connection the database connection
     */

    public ClientDAO(Connection connection) {
        super(connection);
    }
}
