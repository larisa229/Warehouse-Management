package businessLogic;

import dataAccess.ProductDAO;
import dataModel.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Business Logic Layer for managing product operations.
 * This class handles adding, updating, deleting, and fetching products,
 * as well as checking and decrementing product stock.
 */

public class ProductBLL {

    private static final Logger LOGGER = Logger.getLogger(ProductBLL.class.getName());
    private final ProductDAO productDAO;

    /**
     * Constructs a ProductBLL instance with the provided database connection.
     * Initializes the DAO for interacting with the product table in the database.
     *
     * @param connection the connection to the database
     */

    public ProductBLL(Connection connection) {
        this.productDAO = new ProductDAO(connection);
    }

    /**
     * Adds a new product to the database after validating the product's data.
     *
     * @param product the product to be added
     * @return the added product with the generated ID
     * @throws IllegalArgumentException if the product data is invalid
     */

    public Product addProduct(Product product) throws IllegalArgumentException {
        try {
            validateProduct(product);
            return productDAO.insert(product);
        } catch(SQLException e) {
            LOGGER.log(Level.SEVERE,"Failed to add product: " + e.getMessage());
            throw new RuntimeException("Database error", e);
        }
    }

    /**
     * Updates an existing product in the database after validating the product's data.
     *
     * @param product the product with updated information
     * @param id the ID of the product to be updated
     * @return the updated product
     * @throws IllegalArgumentException if the product data is invalid
     */

    public Product updateProduct(Product product, int id) throws IllegalArgumentException {
        try {
            validateProduct(product);
            return productDAO.update(product, id);
        } catch(SQLException e) {
            LOGGER.log(Level.SEVERE,"Failed to update product: " + e.getMessage());
            throw new RuntimeException("Database error", e);
        }
    }

    /**
     * Deletes a product from the database by its ID.
     *
     * @param id the ID of the product to be deleted
     */

    public void deleteProduct(int id) {
        productDAO.delete(id);
    }

    /**
     * Retrieves all products from the database.
     *
     * @return a list of all products
     */

    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    /**
     * Retrieves a product by its ID from the database.
     *
     * @param id the ID of the product to be retrieved
     * @return the product with the specified ID
     */

    public Product findById(int id) {
        return productDAO.findById(id);
    }

    /**
     * Checks if there is enough stock for a specific product.
     *
     * @param productId the ID of the product to check
     * @param quantity the quantity to check for availability
     * @return true if there is enough stock, false otherwise
     */

    public boolean isStockSufficient(int productId, int quantity) {
        try {
            return productDAO.checkStock(productId, quantity);
        } catch(SQLException e) {
            LOGGER.log(Level.SEVERE,"Failed to check stock: " + e.getMessage());
            throw new RuntimeException("Database error", e);
        }
    }

    /**
     * Decrements the stock of a specific product by a specified quantity.
     *
     * @param productId the ID of the product to decrement stock for
     * @param quantity the quantity to decrement from the stock
     */

    public void decrementStock(int productId, int quantity) {
        try {
            productDAO.decrementStock(productId, quantity);
        } catch(SQLException e) {
            LOGGER.log(Level.SEVERE,"Failed to decrement stock: " + e.getMessage());
            throw new RuntimeException("Database error", e);
        }
    }

    /**
     * Validates the provided product data to ensure it is valid.
     *
     * @param product the product to be validated
     * @throws IllegalArgumentException if any product data is invalid
     */

    private void validateProduct(Product product) throws IllegalArgumentException {
        if(product.getProductName() == null || product.getProductName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if(product.getPrice() <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        if(product.getCurrentStock() < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
    }
}
