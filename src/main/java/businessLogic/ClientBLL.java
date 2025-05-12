package businessLogic;

import dataAccess.ClientDAO;
import dataModel.Client;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Business Logic Layer for managing clients.
 * This class handles operations related to client management: adding, updating, deleting, and fetching clients.
 */

public class ClientBLL {

    private static final Logger LOGGER = Logger.getLogger(ClientBLL.class.getName());
    private final ClientDAO clientDAO;

    /**
     * Constructs a ClientBLL instance with the given database connection.
     * Initializes the ClientDAO to interact with the database.
     *
     * @param connection the connection to the database
     */

    public ClientBLL(Connection connection) {
        this.clientDAO = new ClientDAO(connection);
    }

    /**
     * Adds a new client to the database after validating the provided client data.
     *
     * @param client the client to be added
     * @return the added client with the generated ID
     * @throws IllegalArgumentException if the provided client data is invalid
     * @throws RuntimeException if there is a database error
     */

    public Client addClient(Client client) throws IllegalArgumentException {
        try {
            validateClient(client);
            return clientDAO.insert(client);
        } catch(SQLException e) {
            LOGGER.log(Level.SEVERE,"Failed to add client: " + e.getMessage());
            throw new RuntimeException("Database error", e);
        }
    }

    /**
     * Updates the information of an existing client in the database.
     *
     * @param client the updated client data
     * @param id the ID of the client to be updated
     * @return the updated client
     * @throws IllegalArgumentException if the provided client data is invalid
     * @throws RuntimeException if there is a database error
     */

    public Client updateClient(Client client, int id) throws IllegalArgumentException {
        try {
            validateClient(client);
            return clientDAO.update(client, id);
        } catch(SQLException e) {
            LOGGER.log(Level.SEVERE,"Failed to update client: " + e.getMessage());
            throw new RuntimeException("Database error", e);
        }
    }

    /**
     * Deletes a client from the database by its ID.
     *
     * @param id the ID of the client to be deleted
     */

    public void deleteClient(int id) {
        clientDAO.delete(id);
    }

    /**
     * Retrieves all clients from the database.
     *
     * @return a list of all clients
     */

    public List<Client> getAllClients() {
        return clientDAO.findAll();
    }

    /**
     * Retrieves a client by its ID.
     *
     * @param id the ID of the client
     * @return the client with the given ID
     */

    public Client findById(int id) {
        return clientDAO.findById(id);
    }

    /**
     * Validates the provided client data to ensure it meets the constraints.
     *
     * @param client the client to be validated
     * @throws IllegalArgumentException if any client field is invalid
     */

    private void validateClient(Client client) throws IllegalArgumentException {
        if(client.getName() == null || client.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Client name cannot be empty");
        }
        if(client.getEmail() == null || !client.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if(client.getAge() <= 0 || client.getAge() > 100) {
            throw new IllegalArgumentException("Age must be between 1 and 100");
        }
    }
}
