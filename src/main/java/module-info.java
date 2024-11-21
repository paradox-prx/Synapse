module com.example.synapse {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // Add this line


    opens com.example.synapse to javafx.fxml;
    exports com.example.synapse;
}
