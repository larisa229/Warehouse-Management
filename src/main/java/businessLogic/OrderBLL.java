package businessLogic;

import dataAccess.BillDAO;
import dataAccess.ClientDAO;
import dataAccess.OrderDAO;
import dataAccess.ProductDAO;
import dataModel.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Business Logic Layer for managing orders.
 * This class handles the operations related to placing an order, stock validation, and bill generation.
 */

public class OrderBLL {
    private static final Logger LOGGER = Logger.getLogger(OrderBLL.class.getName());
    private final OrderDAO orderDAO;
    private final ClientDAO clientDAO;
    private final ProductDAO productDAO;
    private final BillDAO billDAO;

    /**
     * Constructs an OrderBLL instance with the given database connection.
     * Initializes DAOs for interacting with the database.
     *
     * @param connection the connection to the database
     */

    public OrderBLL(Connection connection) {
        this.orderDAO = new OrderDAO(connection);
        this.clientDAO = new ClientDAO(connection);
        this.productDAO = new ProductDAO(connection);
        this.billDAO = new BillDAO(connection);
    }

    /**
     * Places an order by validating the provided order data, checking stock availability,
     * decrementing stock, and generating the associated bill.
     *
     * @param order the order to be placed
     * @return the saved order with the generated ID
     * @throws UnderStockException if there is not enough stock for the ordered product
     * @throws RuntimeException if there is a database error or an unexpected issue
     */

    public Order placeOrder(Order order) throws UnderStockException {
        try {
            validateOrder(order);
            if(!productDAO.checkStock(order.getProductId(), order.getQuantity())) {
                throw new UnderStockException("Not enough stock for product ID: " + order.getProductId());
            }
            orderDAO.beginTransaction();
            productDAO.decrementStock(order.getProductId(), order.getQuantity());
            Order saveOrder = orderDAO.insert(order);
            Bill bill = generateBill(saveOrder);
            billDAO.insert(bill);
            orderDAO.commit();
            return saveOrder;
        } catch(SQLException e) {
            LOGGER.log(Level.SEVERE,"Order failed: " + e.getMessage());
            orderDAO.rollback();
            throw new RuntimeException("Database error", e);
        }
    }

    /**
     * Generates a bill for the given order by fetching client and product data from the database.
     *
     * @param order the order for which the bill is generated
     * @return the generated bill
     * @throws SQLException if there is an error fetching client or product data
     */

    private Bill generateBill(Order order) throws SQLException{
        Client client = clientDAO.findById(order.getClientId());
        Product product = productDAO.findById(order.getProductId());

        return new Bill(
                0,
                order.getId(),
                client.getName(),
                product.getProductName(),
                order.getQuantity(),
                BigDecimal.valueOf(product.getPrice()).multiply(BigDecimal.valueOf(order.getQuantity())),
                LocalDateTime.now()
        );
    }

    /**
     * Retrieves a list of all the orders from the database and includes client and product names
     * instead of just their IDs.
     *
     * @return a list of {@link OrderView} objects representing the orders
     */

    public List<OrderView> getAllOrders() {
        return orderDAO.findAll().stream().map(order -> {
            Client client = clientDAO.findById(order.getClientId());
            Product product = productDAO.findById(order.getProductId());
            return new OrderView(
                    order.getId(),
                    client.getName(),
                    product.getProductName(),
                    order.getQuantity(),
                    order.getOrderDate()
            );
        }).collect(Collectors.toList());
    }

    /**
     * Validates the provided order data to ensure it meets required constraints.
     *
     * @param order the order to be validated
     * @throws IllegalArgumentException if any order data is invalid
     */

    private void validateOrder(Order order) throws IllegalArgumentException {
        if(order.getClientId() <= 0 || order.getProductId() <= 0) {
            throw new IllegalArgumentException("Invalid client or product ID");
        }
        if(order.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
    }
}
