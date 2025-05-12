package dataModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a bill issued for a placed order.
 * Contains client, product, pricing and timestamps.
 *
 * @param id the ID of the bill itself
 * @param orderId the ID of the order
 * @param clientName the name of the client placing the order
 * @param productName the name of the product ordered
 * @param quantity the quantity ordered
 * @param totalPrice the total price of the order
 * @param orderDate the date and time when the order was placed
 */

public record Bill(
        int id,
        int orderId,
        String clientName,
        String productName,
        int quantity,
        BigDecimal totalPrice,
        LocalDateTime orderDate
) {

    /**
     * Returns a formatted invoice string.
     *
     * @return formatted invoice text block
     */

    public String toFormattedString() {
        return String.format("""
            ============= INVOICE =============
            Order ID: %d
            Invoice ID: %d
            Date: %s
            --------------------------------
            Client: %s
            Product: %s
            Quantity: %d
            --------------------------------
            TOTAL: $%.2f
            ==================================
            """,
                orderId,
                id,
                orderDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                clientName,
                productName,
                quantity,
                totalPrice);
    }

    /**
     * Returns a compact summary of the bill.
     *
     * @return a one-line summary of the invoice
     */

    @Override
    public String toString() {
        return String.format("Bill[%d] | Client: %s | Product: %s x%d | Total: ($%.2f)\n",
                id, clientName, productName, quantity, totalPrice);
    }
}
