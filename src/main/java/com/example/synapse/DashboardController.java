package com.example.synapse;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class DashboardController {
    @FXML
    private TextField searchField;

    @FXML
    public void initialize() {
        // Initialize components or add listeners
        searchField.setOnAction(event -> {
            System.out.println("Search Query: " + searchField.getText());
        });
    }

}
