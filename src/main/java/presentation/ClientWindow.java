package presentation;

import businessLogic.ClientBLL;
import dataModel.Client;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.List;
import java.util.Optional;

/**
 * A JavaFX BorderPane for displaying clients in a TableView.
 * Provides functionalities such as adding, editing and deleting clients.
 */

public class ClientWindow extends BorderPane {

    private final ClientBLL clientBLL;
    private final TableView<Client> table = new TableView<>();

    /**
     * Constructs a ClientWindow with the specified business logic layer.
     *
     * @param clientBLL the business logic layer for the client operations
     */

    public ClientWindow(ClientBLL clientBLL) {
        this.clientBLL = clientBLL;
        initUI();
        loadClients();
    }

    /**
     * Initializes the user interface with a table and buttons for client management.
     */

    private void initUI() {
        setCenter(table);

        Button addButton = new Button("Add Client");
        Button editButton = new Button("Edit Client");
        Button deleteButton = new Button("Delete Client");

        HBox buttonBox = new HBox(10, addButton, editButton, deleteButton);
        buttonBox.setPadding(new Insets(10));
        setBottom(buttonBox);

        addButton.setOnAction(e -> showClientDialog(null));
        editButton.setOnAction(e -> {
            Client selected = table.getSelectionModel().getSelectedItem();
            if(selected != null) {
                showClientDialog(selected);
            }
        });
        deleteButton.setOnAction(e -> deleteClient());
    }

    /**
     * Loads all clients from the BLL and populates the table with them.
     */

    private void loadClients() {
        try {
            List<Client> clients = clientBLL.getAllClients();
            Table.createTable(table, clients);
        } catch(Exception e) {
            showAlert("Error loading clients: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Displays a dialog for adding or editing a client.
     *
     * @param client the client to edit, or null in the case of adding a new client
     */

    private void showClientDialog(Client client) {
        Dialog<Client> dialog = new Dialog<>();
        dialog.setTitle(client == null ? "Add New Client" : "Edit Client");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        TextField emailField = new TextField();
        TextField addressField = new TextField();
        Spinner<Integer> ageSpinner = new Spinner<>(1, 120, 30);

        if (client != null) {
            nameField.setText(client.getName());
            emailField.setText(client.getEmail());
            addressField.setText(client.getAddress());
            ageSpinner.getValueFactory().setValue(client.getAge());
        }

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Address:"), 0, 2);
        grid.add(addressField, 1, 2);
        grid.add(new Label("Age:"), 0, 3);
        grid.add(ageSpinner, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    Client newClient = new Client(client != null ? client.getId() : 0, nameField.getText(),
                            addressField.getText(), emailField.getText(), ageSpinner.getValue());
                    return newClient;
                } catch (Exception e) {
                    showAlert("Validation Error: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        Optional<Client> result = dialog.showAndWait();
        result.ifPresent(clientToSave -> {
            try {
                if (client == null) {
                    clientBLL.addClient(clientToSave);
                } else {
                    clientBLL.updateClient(clientToSave, clientToSave.getId());
                }
                loadClients();
            } catch (Exception e) {
                showAlert("Error saving client: " + e.getMessage());
            }
        });
    }

    /**
     * Deletes the selected client from the table.
     */

    private void deleteClient() {
        Client selected = table.getSelectionModel().getSelectedItem();
        if(selected != null) {
            try {
                clientBLL.deleteClient(selected.getId());
                loadClients();
            } catch (Exception e) {
                showAlert("Error deleting client: " + e.getMessage());
            }
        }
    }

    /**
     * Displays an error alert with the specified message.
     *
     * @param message the error message to display
     */

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
