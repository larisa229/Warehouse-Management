package presentation;

import businessLogic.*;
import dataModel.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * A JavaFX VBox for displaying orders in a TableView, with bill display.
 * Provides the functionality of placing orders.
 */

public class OrderWindow extends VBox {
    private final OrderBLL orderBLL;
    private final ClientBLL clientBLL;
    private final ProductBLL productBLL;
    private final BillBLL billBLL;

    private final ComboBox<Client> clientCombo = new ComboBox<>();
    private final ComboBox<Product> productCombo = new ComboBox<>();
    private final TextField quantityField = new TextField("1");
    private final Button placeOrderButton = new Button("Place Order");
    private final TextArea billTextArea = new TextArea();
    private final TableView<OrderView> orderTable = new TableView<>();

    /**
     * Constructs an OrderWindow with the specified business logic layers.
     *
     * @param orderBLL the BLL for order operations
     * @param clientBLL the BLL for client operations
     * @param productBLL the BLL for product operations
     * @param billBLL the BLL for bill operations
     */

    public OrderWindow(OrderBLL orderBLL, ClientBLL clientBLL, ProductBLL productBLL, BillBLL billBLL) {
        this.orderBLL = orderBLL;
        this.clientBLL = clientBLL;
        this.productBLL = productBLL;
        this.billBLL = billBLL;

        initUI();
        loadData();
    }

    /**
     * Initializes the user interface with input fields, a TableView and bill display.
     */

    private void initUI() {
        setSpacing(10);
        setPadding(new Insets(10));

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> loadData());

        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.add(new Label("Client:"), 0, 0);
        inputGrid.add(clientCombo, 1, 0);
        inputGrid.add(new Label("Product:"), 0, 1);
        inputGrid.add(productCombo, 1, 1);
        inputGrid.add(new Label("Quantity:"), 0, 2);
        inputGrid.add(quantityField, 1, 2);

        TabPane tabPane = new TabPane();

        Tab ordersTab = new Tab("Orders", orderTable);
        ordersTab.setClosable(false);
        tabPane.getTabs().addAll(ordersTab);

        placeOrderButton.setOnAction(e -> placeOrder());
        placeOrderButton.setMaxWidth(Double.MAX_VALUE);

        billTextArea.setEditable(false);
        billTextArea.setPrefHeight(300);
        billTextArea.setPrefWidth(500);
        billTextArea.setStyle("-fx-font-family: monospace;");

        VBox inputBox = new VBox(10, inputGrid, placeOrderButton, refreshButton);
        inputBox.setPadding(new Insets(10));
        VBox billBox = new VBox(5, new Label("Order Bill:"), billTextArea);
        billBox.setPadding(new Insets(10));
        HBox hbox = new HBox(20, inputBox, billBox);
        hbox.setPadding(new Insets(10));

        getChildren().addAll(hbox, tabPane);

        orderTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                OrderView selectedOrder = orderTable.getSelectionModel().getSelectedItem();
                if (selectedOrder != null) {
                    Bill bill = billBLL.getBillById(selectedOrder.getId());
                    displayBill(bill);
                }
            }
        });
    }

    /**
     * Loads clients, products and orders into the UI components.
     */

    private void loadData() {
        try {
            List<Client> clients = clientBLL.getAllClients();
            clientCombo.setItems(FXCollections.observableArrayList(clients));
            if (!clients.isEmpty()) clientCombo.getSelectionModel().selectFirst();

            List<Product> products = productBLL.getAllProducts();
            productCombo.setItems(FXCollections.observableArrayList(products));
            if (!products.isEmpty()) productCombo.getSelectionModel().selectFirst();

            List<OrderView> orders = orderBLL.getAllOrders();
            Table.createTable(orderTable, orders);
        } catch (Exception e) {
            showAlert("Error loading data: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Places an order based on user input, creates a bill, and refreshes the UI.
     */

    private void placeOrder() {
        Client client = clientCombo.getValue();
        Product product = productCombo.getValue();
        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException ex) {
            showAlert("Invalid quantity entered.", Alert.AlertType.ERROR);
            return;
        }
        if (client == null || product == null) {
            showAlert("Please select both client and product", Alert.AlertType.ERROR);
            return;
        }
        try {
            Order order = new Order(0, client.getId(), product.getId(), quantity, LocalDateTime.now());
            Order savedOrder = orderBLL.placeOrder(order);
            BigDecimal total = BigDecimal.valueOf(product.getPrice()).multiply(BigDecimal.valueOf(quantity));
            Bill bill = new Bill(0, savedOrder.getId(), client.getName(), product.getProductName(), quantity, total, LocalDateTime.now());

            displayBill(bill);
            loadData();
            showAlert("Order placed successfully!", Alert.AlertType.INFORMATION);

        } catch (UnderStockException e) {
            showAlert("Under stock: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error placing order: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Displays the bill details in the TextArea.
     *
     * @param bill the bill to display, or null if no bill exists
     */

    private void displayBill(Bill bill) {
        if(bill == null) {
            showAlert("No bill found for this order.", Alert.AlertType.ERROR);
            return;
        }
        String formattedBill = String.format(
                "=== ORDER BILL ===\n" +
                        "Order ID: %d\n" +
                        "Client: %s\n" +
                        "Product: %s\n" +
                        "Quantity: %d\n" +
                        "Unit Price: $%.2f\n" +
                        "Total: $%.2f\n" +
                        "Date: %s\n",
                bill.orderId(),
                bill.clientName(),
                bill.productName(),
                bill.quantity(),
                bill.totalPrice().divide(BigDecimal.valueOf(bill.quantity())),
                bill.totalPrice(),
                bill.orderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
        billTextArea.setText(formattedBill);
    }

    /**
     * Displays an alert with the specified message and type.
     *
     * @param message the message to display
     * @param type the type of alert
     */

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(type.name().toLowerCase());
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}