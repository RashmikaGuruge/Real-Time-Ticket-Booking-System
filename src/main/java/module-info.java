module com.example.realtimeeventticketingsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports com.example.realtimeeventticketingsystem to javafx.graphics;
    exports com.example.realtimeeventticketingsystem.controller to javafx.fxml;
    opens com.example.realtimeeventticketingsystem.controller to javafx.fxml;
}
