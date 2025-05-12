package dataAccess;

import dataModel.Client;

import java.sql.*;
import java.util.List;

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

    /**
     * Finds a client in the database by their name.
     *
     * @param name the name of the client to be found
     * @return the {@link Client} object if found; otherwise {@code null}
     * @throws SQLException if a database access error occurs
     */

    public Client findByName(String name) throws SQLException{
        String query = "SELECT * FROM client WHERE name = ?";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            try(ResultSet rs = stmt.executeQuery()) {
                List<Client> results = createObjects(rs);
                return results.isEmpty() ? null : results.get(0);
            }
        }
    }
}
