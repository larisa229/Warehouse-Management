package businessLogic;

/**
 * Custom exception thrown when a product does not have enough stock to fulfill an order.
 */

public class UnderStockException extends Exception {
    /**
     * Constructs an UnderStockException with a default error message.
     */
    public UnderStockException() {
        super("Insufficient product stock");
    }
    /**
     * Constructs an UnderStockException with a custom error message.
     *
     * @param message the custom error message
     */
    public UnderStockException(String message) {
        super(message);
    }
}
