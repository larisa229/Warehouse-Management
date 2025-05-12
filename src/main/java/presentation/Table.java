package presentation;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.stream.Stream;

/**
 * Utility class for configuring JavaFX TableView dynamically based on the fields of a given data model.
 */

public class Table {

    /**
     * Creates a table to display data from the given list of items.
     * Columns are generated using reflection, based on the fields of the object's class.
     *
     * @param tableView the TableView to configure
     * @param items the list of data items to display in the table
     */

    public static <T> void createTable(TableView<T> tableView, List<T> items) {
        tableView.getItems().clear();
        tableView.getColumns().clear();

        if(items.isEmpty()) return;

        Class<?> clazz = items.get(0).getClass();
        Stream.of(clazz.getDeclaredFields()).map(field -> {
            TableColumn<T, Object> column = new TableColumn<>(field.getName());
            column.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
            return column;
        }).forEach(tableView.getColumns()::add);
        tableView.getItems().addAll(items);
    }
}
