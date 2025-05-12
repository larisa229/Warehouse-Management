package dataModel;

/**
 * Represents a product with a name, price and available stock.
 */

public class Product {

    private int id;
    private String productName;
    private double price;
    private int currentStock;

    /**
     * Default no-argument constructor for creating an empty Product.
     */

    public Product() {

    }

    /**
     * Constructs a Product with all fields including ID.
     *
     * @param id the product ID
     * @param productName the name of the product
     * @param price the price of the product (must be positive)
     * @param currentStock the current stock level (must be non-negative)
     */

    public Product(int id, String productName, double price, int currentStock) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.currentStock = currentStock;
    }

    /**
     * Constructs a Product without an ID .
     *
     * @param productName the name of the product
     * @param price the price of the product (must be positive)
     * @param currentStock the current stock level (must be non-negative)
     */

    public Product(String productName, double price, int currentStock) {
        this.productName = productName;
        this.price = price;
        this.currentStock = currentStock;
    }

    /**
     * Gets the product ID.
     *
     * @return the ID of the product
     */

    public int getId() {
        return id;
    }

    /**
     * Sets the product ID.
     *
     * @param id the product ID
     */

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the product name.
     *
     * @return the name of the product
     */

    public String getProductName() {
        return productName;
    }

    /**
     * Sets the product name.
     *
     * @param productName the name of the product
     */

    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Gets the price of the product
     *
     * @return the product's price
     */

    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the product.
     *
     * @param price the product price
     * @throws IllegalArgumentException if the price is not positive
     */

    public void setPrice(double price) {
        if (price <= 0) throw new IllegalArgumentException("Price must be positive");
        this.price = price;
    }

    /**
     * Gets the product current stock.
     *
     * @return the current stock of the product
     */

    public int getCurrentStock() {
        return currentStock;
    }

    /**
     * Sets the product's current stock.
     *
     * @param currentStock the current stock level
     */

    public void setCurrentStock(int currentStock) {
        if (currentStock < 0) throw new IllegalArgumentException("Stock cannot be negative");
        this.currentStock = currentStock;
    }

    /**
     * Returns a string representation of the product.
     *
     * @return formatted string with product details
     */

    @Override
    public String toString() {
        return String.format("Product[id=%d, name=%s, price=%.2f, current stock=%d]\n", id, productName, price, currentStock);
    }
}
