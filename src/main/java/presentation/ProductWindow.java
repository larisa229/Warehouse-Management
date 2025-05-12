package presentation;

import businessLogic.ProductBLL;
import dataModel.Product;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.Optional;

/**
 * JavaFX BorderPane for displaying products in a TableView.
 * Provides functionalities like adding, editing and deleting products.
 */

public class ProductWindow extends BorderPane {
    private final ProductBLL productBLL;
    private TableView<Product> table = new TableView<>();

    /**
     * Constructs a ProductWindow with the specified business logic layer.
     *
     * @param productBLL the business logic layer for product operations
     */

    public ProductWindow(ProductBLL productBLL) {
        this.productBLL = productBLL;
        initUI();
        loadProducts();
    }

    /**
     * Initializes the user interface with a TableView and buttons for product management.
     */

    private void initUI() {
        setCenter(table);

        Button addButton = new Button("Add Product");
        Button editButton = new Button("Edit Product");
        Button deleteButton = new Button("Delete Product");

        HBox buttonBox = new HBox(10, addButton, editButton, deleteButton);
        buttonBox.setPadding(new Insets(10));
        setBottom(buttonBox);

        addButton.setOnAction(e -> showProductDialog(null));
        editButton.setOnAction(e -> {
            Product selected = table.getSelectionModel().getSelectedItem();
            if(selected != null) {
                showProductDialog(selected);
            }
        });
        deleteButton.setOnAction(e -> deleteProduct());
    }

    /**
     * Loads all products from the business layer and populates the TableView.
     */

    private void loadProducts() {
        try {
            List<Product> products = productBLL.getAllProducts();
            Table.createTable(table, products);
        } catch (Exception e) {
            showAlert("Error loading products: " + e.getMessage());
        }
    }

    /**
     * Displays a dialog for adding or editing a product.
     *
     * @param product the product to edit, or null in the case of a new product
     */

    private void showProductDialog(Product product) {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle(product == null ? "Add New Product" : "Edit Product");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        TextField priceField = new TextField();
        Spinner<Integer> stockSpinner = new Spinner<>(0, Integer.MAX_VALUE, 0);

        if (product != null) {
            nameField.setText(product.getProductName());
            priceField.setText(String.valueOf(product.getPrice()));
            stockSpinner.getValueFactory().setValue(product.getCurrentStock());
        }

        grid.add(new Label("Product name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Price:"), 0, 1);
        grid.add(priceField, 1, 1);
        grid.add(new Label("Current stock:"), 0, 2);
        grid.add(stockSpinner, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    String name = nameField.getText();
                    String priceText = priceField.getText();
                    int stock = stockSpinner.getValue();
                    double price;
                    try {
                        price = Double.parseDouble(priceText);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Price must be a valid number");
                    }
                    return new Product(product != null ? product.getId() : 0, name, price, stock);
                } catch (Exception e) {
                    showAlert("Validation Error: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        Optional<Product> result = dialog.showAndWait();
        result.ifPresent(productToSave -> {
            try {
                if (product == null) {
                    productBLL.addProduct(productToSave);
                } else {
                    productBLL.updateProduct(productToSave, productToSave.getId());
                }
                loadProducts();
            } catch (Exception e) {
                showAlert("Error saving product: " + e.getMessage());
            }
        });
    }

    /**
     * Deletes the selected product from the table.
     */

    private void deleteProduct() {
        Product selected = table.getSelectionModel().getSelectedItem();
        if(selected != null) {
            try {
                productBLL.deleteProduct(selected.getId());
                loadProducts();
            } catch (Exception e) {
                showAlert("Error deleting product: " + e.getMessage());
            }
        }
    }

    /**
     * Displays an error alert with the specified message.
     *
     * @param message the error message to display
     */

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
