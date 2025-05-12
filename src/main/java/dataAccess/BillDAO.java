package dataAccess;

import dataModel.Bill;

import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Data Access Object for the {@link Bill} entity.
 * Provides methods to interact with the 'log' table in the database.
 */

public class BillDAO {
    private final Connection connection;

    /**
     * Constructs a BillDAO with the given database connection.
     *
     * @param connection the database connection
     */
    public BillDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Inserts a new {@link Bill} record into the database and returns the inserted bill
     * with the generated ID.
     *
     * @param bill the bill to insert
     * @return the inserted bill with generated ID
     * @throws SQLException if a database access error occurs
     */

    public Bill insert(Bill bill) throws SQLException {
        String sql = "INSERT INTO log (order_id, client_name, product_name, quantity, total_price, order_date) VALUES (?, ?, ?, ?, ?, ?) RETURNING ID";
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, bill.orderId());
            ps.setString(2, bill.clientName());
            ps.setString(3, bill.productName());
            ps.setInt(4, bill.quantity());
            ps.setBigDecimal(5, bill.totalPrice());
            ps.setTimestamp(6, Timestamp.valueOf(bill.orderDate()));
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return new Bill(
                            rs.getInt("id"),
                            bill.orderId(),
                            bill.clientName(),
                            bill.productName(),
                            bill.quantity(),
                            bill.totalPrice(),
                            bill.orderDate()
                    );
                }
            }
        }
        throw new SQLException("Failed to insert bill");
    }

    /**
     * Retrieves all {@link Bill} records from the database.
     *
     * @return a list of all bills
     * @throws SQLException if a database access error occurs
     */

    public List<Bill> findAll() throws SQLException {
        String sql = "SELECT * FROM log";
        try(Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            return Stream.generate(() -> {
                try {
                    return rs.next() ? new Bill(
                            rs.getInt("id"),
                            rs.getInt("order_id"),
                            rs.getString("client_name"),
                            rs.getString("product_name"),
                            rs.getInt("quantity"),
                            rs.getBigDecimal("total_price"),
                            rs.getTimestamp("order_date").toLocalDateTime()
                    ) : null;
                } catch(SQLException e) {
                    throw new RuntimeException("Failed to read ResultSet", e);
                }
            }).takeWhile(bill -> bill != null).collect(Collectors.toList());
        }
    }

    /**
     * Finds a {@link Bill} record in the database by the ID of the order for which the bill was generated.
     *
     * @param orderID the ID of the order on the bill
     * @return the bill generated for the given order
     * @throws SQLException if a database access error occurs
     */

    public Bill findByOrderID(int orderID) throws SQLException {
        String sql = "SELECT * FROM log WHERE order_id = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderID);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return new Bill(
                            rs.getInt("id"),
                            rs.getInt("order_id"),
                            rs.getString("client_name"),
                            rs.getString("product_name"),
                            rs.getInt("quantity"),
                            rs.getBigDecimal("total_price"),
                            rs.getTimestamp("order_date").toLocalDateTime()
                    );
                }
            }
        }
        return null;
    }
}
