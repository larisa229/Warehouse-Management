package dataAccess;

import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Abstract Data Access Object (DAO) class to implement basic CRUD operations.
 * This class handles generic database operations.
 *
 * @param <T> the type of entity managed by this DAO
 */

public abstract class AbstractDAO<T> {

    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());
    private final Class<T> type;
    protected final Connection connection;

    /**
     * Constructor for AbstractDAO.
     *
     * @param connection the database connection to use for CRUD operations
     */

    @SuppressWarnings("unchecked")
    public AbstractDAO(Connection connection) {
        this.connection = connection;
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    }

    /**
     * Converts camelCase field name to snake_case to match database column names.
     *
     * @param fieldName the camelCase field name to be converted
     * @return the snake_case field name
     */

    private String toSnakeCase(String fieldName) {
        return fieldName.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    /**
     * Creates a SQL select query to retrieve an entity by field.
     *
     * @param field the field name to filter by
     * @return the SQL select query
     */

    private String createSelectQuery(String field) {
        return "SELECT * FROM " + type.getSimpleName().toLowerCase() + " WHERE " + toSnakeCase(field) + " = ?";
    }

    /**
     * Finds all instances of the entity type from the database.
     *
     * @return a list of all entities
     */

    public List<T> findAll() {
        List<T> list = new ArrayList<>();
        String query = "SELECT * FROM \"" + type.getSimpleName().toLowerCase() + "\"";
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)) {
            list = createObjects(resultSet);
        } catch(SQLException e) {
            LOGGER.log(Level.WARNING, "Failed to findAll for " + type.getSimpleName(), e);
        }
        return list;
    }

    /**
     * Finds a single entity by its ID.
     *
     * @param id the ID of the entity to find
     * @return the found entity, or null if not found
     */

    public T findById(int id) {
        String query = createSelectQuery("id");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<T> results = createObjects(resultSet);
                return results.isEmpty() ? null : results.get(0);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Failed to findById for " + type.getSimpleName(), e);
            return null;
        }
    }

    /**
     * Creates a list of entities from a ResultSet.
     * Uses reflection to instantiate entities and map fields.
     *
     * @param resultSet the ResultSet containing data
     * @return a list of entities
     * @throws SQLException if there is an error accessing the ResultSet
     */

    List<T> createObjects(ResultSet resultSet) throws SQLException {
        List<T> list = new ArrayList<T>();
        try {
            Constructor<T> ctor = type.getDeclaredConstructor();
            ctor.setAccessible(true);
            while (resultSet.next()) {
                T instance = ctor.newInstance();
                Stream.of(type.getDeclaredFields()).forEach(field -> {
                    String fieldName = field.getName();
                    String columnName = toSnakeCase(fieldName);
                    try {
                        Object value = resultSet.getObject(columnName);
                        if(value != null) {
                            if (field.getType() == double.class && value instanceof Number) {
                                value = ((Number) value).doubleValue();
                            } else if (field.getType() == int.class && value instanceof Number) {
                                value = ((Number) value).intValue();
                            } else if (field.getType() == LocalDateTime.class && value instanceof Timestamp) {
                                value = ((Timestamp) value).toLocalDateTime();
                            }
                            PropertyDescriptor pd = new PropertyDescriptor(fieldName, type);
                            pd.getWriteMethod().invoke(instance, value);
                        }
                    } catch(Exception e) {
                        throw new RuntimeException("Reflection failed for field " + fieldName, e);
                    }
                });
                list.add(instance);
            }
        } catch (Exception e) {
            throw new SQLException("Reflection failed for " + type.getSimpleName(), e);
        }
        return list;
    }

    /**
     * Inserts a new entity into the database.
     *
     * @param t the entity to insert
     * @return the inserted entity with generated ID
     * @throws SQLException if there is an error during the insert
     */

    public T insert(T t) throws SQLException {
       StringBuilder columns = new StringBuilder();
       StringBuilder placeHolders = new StringBuilder();
       List<Object> values = new ArrayList<>();
       Stream.of(type.getDeclaredFields()).filter(field -> !field.getName().equals("id")).forEach(field -> {
           columns.append(toSnakeCase(field.getName())).append(",");
           placeHolders.append("?,");
           try {
               field.setAccessible(true);
               values.add(field.get(t));
           } catch(IllegalAccessException e) {
               throw new RuntimeException("Failed to access field: " + field.getName(), e);
           }
       });
        columns.deleteCharAt(columns.length() - 1);
        placeHolders.deleteCharAt(placeHolders.length() - 1);
        String sql = "INSERT INTO \"" + type.getSimpleName().toLowerCase() + "\" (" + columns + ") VALUES (" + placeHolders + ")";
        try(PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
           for(int i = 0; i < values.size(); i++) {
               statement.setObject(i + 1, values.get(i));
           }
           statement.executeUpdate();
           try(ResultSet rs = statement.getGeneratedKeys()) {
               if(rs.next()) {
                   Field idField = type.getDeclaredField("id");
                   idField.setAccessible(true);
                   idField.set(t, rs.getInt(1));
               }
           } catch (NoSuchFieldException | IllegalAccessException e) {
               throw new RuntimeException(e);
           }
            return t;
       }
    }

    /**
     * Updates an existing entity in the database.
     *
     * @param t the entity to update
     * @param id the ID of the entity to update
     * @return the updated entity
     * @throws SQLException if there is an error during the update
     */

    public T update(T t, int id) throws SQLException {
        StringBuilder setClause = new StringBuilder();
        List<Object> values = new ArrayList<>();
        Stream.of(type.getDeclaredFields()).filter(field -> !field.getName().equals("id")).forEach(field -> {
            setClause.append(toSnakeCase(field.getName())).append("=?,");
            try {
                field.setAccessible(true);
                values.add(field.get(t));
            } catch(IllegalAccessException e) {
                throw new RuntimeException("Failed to access field: " + field.getName(), e);
            }
        });
        String sql = "UPDATE \"" + type.getSimpleName().toLowerCase() + "\" SET " + setClause.deleteCharAt(setClause.length()-1) + " WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for(int i = 0; i < values.size(); i++) {
                statement.setObject(i + 1, values.get(i));
            }
            statement.setInt(values.size() + 1, id);
            statement.executeUpdate();
            return t;
        }
    }

    /**
     * Deletes an entity from the database by its ID.
     *
     * @param id the ID of the entity to delete
     */

    public void delete(int id) {
        String sql = "DELETE FROM \"" + type.getSimpleName().toLowerCase() + "\" WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch(SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:delete " + e.getMessage());
        }
    }

    /**
     * Begins a database transaction by disabling auto-commit.
     *
     * @throws SQLException if there is an error starting the transaction
     */

    public void beginTransaction() throws SQLException {
        connection.setAutoCommit(false);
    }

    /**
     * Commits the current database transaction.
     *
     * @throws SQLException if there is an error committing the transaction
     */

    public void commit() throws SQLException {
        connection.commit();
        connection.setAutoCommit(true);
    }

    /**
     * Rolls back the current database transaction.
     */

    public void rollback() {
        try {
            connection.rollback();
        } catch(SQLException e) {
            LOGGER.log(Level.WARNING, "Rollback failed", e);
        }
    }
}

