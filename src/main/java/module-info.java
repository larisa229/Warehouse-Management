module com.example.pt2025_30422_larisa_pasca_assignment_3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires java.naming;
    requires org.postgresql.jdbc;


    opens com.example.pt2025_30422_larisa_pasca_assignment_3 to javafx.fxml;
    exports com.example.pt2025_30422_larisa_pasca_assignment_3;
    exports presentation;
    opens presentation to javafx.graphics;
    opens dataModel to javafx.base;
}