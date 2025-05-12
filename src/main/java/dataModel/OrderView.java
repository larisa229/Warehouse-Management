package dataModel;

import java.time.LocalDateTime;

/**
 * A model class representing an order with extensive details.
 * Contains details such as order ID, client name, product name, quantity and order date.
 */

public class OrderView {

    private final int id;
    private final String clientName;
    private final String productName;
    private final int quantity;
    private final LocalDateTime orderDate;

    /**
     * Constructs an OrderView instance with the specified order details.
     *
     * @param id the unique identifier of the order
     * @param clientName the name of the client who placed the order
     * @param productName the name of the product ordered
     * @param quantity the quantity of the product ordered
     * @param orderDate the date and time when the order was placed
     */

    public OrderView(int id, String clientName, String productName, int quantity, LocalDateTime orderDate) {
        this.id = id;
        this.clientName = clientName;
        this.productName = productName;
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
     * Gets the name of the client who ordered.
     *
     * @return the client name
     */

    public String getClientName() {
        return clientName;
    }

    /**
     * Gets the name of the ordered product.
     *
     * @return the product name
     */

    public String getProductName() {
        return productName;
    }

    /**
     * Gets the quantity that was ordered.
     *
     * @return the quantity ordered
     */

    public int getQuantity() {
        return quantity;
    }

    /**
     * Gets the date and time of the order.
     *
     * @return the order date
     */

    public LocalDateTime getOrderDate() {
        return orderDate;
    }
}
