module com.example.synapse {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // Add this line


    opens com.example.synapse to javafx.fxml;
    exports com.example.synapse;
    exports com.example.synapse.controllers;
    opens com.example.synapse.controllers to javafx.fxml;
    exports com.example.synapse.database;
    opens com.example.synapse.database to javafx.fxml;
}
