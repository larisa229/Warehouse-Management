package businessLogic;

import dataAccess.BillDAO;
import dataModel.Bill;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Business Logic Layer for managing bills.
 * This class handles operations related to bills.
 */

public class BillBLL {
    private static final Logger LOGGER = Logger.getLogger(BillBLL.class.getName());
    private final BillDAO billDAO;

    /**
     * Constructs a BillBLL instance with the given database connection.
     * Initializes the BillDAO to interact with the database.
     *
     * @param connection the connection to the database
     */

    public BillBLL(Connection connection) {
        this.billDAO = new BillDAO(connection);
    }

    /**
     * Retrieves a bill from the database by the ID of the order.
     *
     * @param id the ID of the order
     * @return the bill generated for the provided order
     */

    public Bill getBillById(int id) {
        try {
            return billDAO.findByOrderID(id);
        } catch(SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to fetch bill by id: " + e.getMessage());
            throw new RuntimeException("Database error, e");
        }
    }
}
