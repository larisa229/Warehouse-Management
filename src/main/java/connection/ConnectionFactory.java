package connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Connection factory class responsible for creating and managing the connection with the database.
 * It only allows one instance of database connection.
 */

public class ConnectionFactory {

    private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
    private static final String DRIVER = "org.postgresql.Driver";

    private static final Properties props = new Properties();

    static {
        try (InputStream input = ConnectionFactory.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            props.load(input);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load database configuration", e);
        }
    }

    private static final String DBURL = props.getProperty("db.url");
    private static final String USER = props.getProperty("db.user");
    private static final String PASS = props.getProperty("db.password");

    private static ConnectionFactory singleInstance = new ConnectionFactory();

    /**
     * Private constructor to initialize the JDBC driver.
     */
    private ConnectionFactory() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Database driver not found", e);
        }
    }

    /**
     * Creates and returns a database connection.
     *
     * @return a Connection object
     */

    private Connection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DBURL, USER, PASS);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "An error occurred while trying to connect to the database");
        }
        return connection;
    }

    /**
     * Returns a single instance of the database connection.
     *
     * @return a Connection object
     */

    public static Connection getConnection() {
        return singleInstance.createConnection();
    }

    /**
     * Closes the given database connection.
     *
     * @param connection the Connection object to close
     */

    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occured while trying to close the connection");
            }
        }
    }

    /**
     * Closes the given Statement.
     *
     * @param statement the Statement object to close
     */

    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occured while trying to close the statement");
            }
        }
    }

    /**
     * Closes the given ResultSet.
     *
     * @param resultSet the ResultSet object to close
     */

    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occured while trying to close the ResultSet");
            }
        }
    }
}
