module com.example.synapse {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics; // Add this line


    opens com.example.synapse to javafx.fxml;
    // Add this line to open your models package to javafx.base
    opens com.example.synapse.models to javafx.base;
    exports com.example.synapse;
    exports com.example.synapse.controllers;
    opens com.example.synapse.controllers to javafx.fxml;
    exports com.example.synapse.database;
    opens com.example.synapse.database to javafx.fxml;
}
