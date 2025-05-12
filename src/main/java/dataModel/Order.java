package dataModel;

import java.time.LocalDateTime;

/**
 * Represents an order placed by a client.
 */

public class Order {

    private int id;
    private int clientId;
    private int productId;
    private int quantity;
    private LocalDateTime orderDate;

    /**
     * Default constructor.
     */

    public Order() {

    }

    /**
     * Constructs an Order with all fields including the ID.
     *
     * @param id the order ID
     * @param clientId the ID of the client placing the order
     * @param productId the ID of the product being ordered
     * @param quantity the quantity ordered
     * @param orderDate the date and time when the order was placed
     */

    public Order(int id, int clientId, int productId, int quantity, LocalDateTime orderDate) {
        this.id = id;
        this.clientId = clientId;
        this.productId = productId;
        this.quantity = quantity;
        this.orderDate = orderDate;
    }

    /**
     * Constructs an Order without an ID (typically used before persistence).
     *
     * @param clientId the ID of the client placing the order
     * @param productId the ID of the product being ordered
     * @param quantity the quantity ordered
     * @param orderDate the date and time when the order was placed
     */

    public Order(int clientId, int productId, int quantity, LocalDateTime orderDate) {
        this.clientId = clientId;
        this.productId = productId;
        this.quantity = quantity;
        this.orderDate = orderDate;
    }

    /**
     * Gets the unique identifier of the order.
     *
     * @return the order ID
     */

    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the order.
     *
     * @param id the order ID
     */

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the ID of the client placing the order.
     *
     * @return the client ID
     */

    public int getClientId() {
        return clientId;
    }

    /**
     * Gets the ID of the client placing the order.
     *
     * @param clientId the client ID
     */

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * Gets the ID of the ordered product.
     *
     * @return the product ID
     */

    public int getProductId() {
        return productId;
    }

    /**
     * Sets the ID of the ordered product.
     *
     * @param productId the product ID
     */

    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     * Gets the quantity ordered.
     *
     * @return the ordered quantity
     */

    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the ordered quantity.
     *
     * @param quantity the quantity to order
     */

    public void setQuantity(int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        this.quantity = quantity;
    }

    /**
     * Gets the order date and time.
     *
     * @return the order date
     */

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    /**
     * Sets the order date and time.
     *
     * @param orderDate the order date
     */

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * Returns a string representation of the order.
     *
     * @return formatted string with order details
     */

    @Override
    public String toString() {
        return String.format("Order[id=%d, client ID=%d, product ID=%d, quantity=%d]\n", id, clientId, productId, quantity);
    }
}
