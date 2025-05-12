package presentation;

import businessLogic.BillBLL;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import businessLogic.ClientBLL;
import businessLogic.ProductBLL;
import businessLogic.OrderBLL;
import java.sql.Connection;
import connection.ConnectionFactory;

/**
 * The main application class for the Order Management System.
 * Initializes the GUI with tabs for managing clients, products and orders.
 */

public class MainApp extends Application {

    /**
     * The entry point for the JavaFX application.
     *
     * @param args command-line arguments
     */

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the JavaFX application, setting up the main window with tabs.
     *
     * @param primaryStage the primary stage for the application
     */

    @Override
    public void start(Stage primaryStage) {
        Connection connection = ConnectionFactory.getConnection();
        ClientBLL clientBLL = new ClientBLL(connection);
        ProductBLL productBLL = new ProductBLL(connection);
        OrderBLL orderBLL = new OrderBLL(connection);
        BillBLL billBLL = new BillBLL(connection);

        TabPane tabPane = new TabPane();

        Tab clientTab = new Tab("Clients", new ClientWindow(clientBLL));
        Tab productTab = new Tab("Products", new ProductWindow(productBLL));
        Tab orderTab = new Tab("Orders", new OrderWindow(orderBLL, clientBLL, productBLL, billBLL));

        tabPane.getTabs().addAll(clientTab, productTab, orderTab);

        Scene scene = new Scene(tabPane, 800, 600);
        primaryStage.setTitle("Orders Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
