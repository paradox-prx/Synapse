package com.example.synapse.controllers;

import com.example.synapse.database.DatabaseUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CreateProjectBoardController {

    @FXML
    private ListView<String> teamMembersList; // ListView to display team members

    private final DatabaseUtils dbUtils = new DatabaseUtils();

    public Button createBoardButton;

    @FXML
    private void handleCreateBoardAction(ActionEvent event) {
        // Logic for creating the project board (save data, etc.)

        // Close the Create Project Board window after creation
        Stage stage = (Stage) createBoardButton.getScene().getWindow();
        stage.close();
    }

    // This method loads active team members from the database
    private void loadTeamMembers() {
        ObservableList<String> teamMembers = FXCollections.observableArrayList();

        // Fetch active team members from the database
        try {
            teamMembers.addAll(dbUtils.getActiveTeamMembers()); // Get team members and add them to the list
            teamMembersList.setItems(teamMembers); // Set the list to ListView
        } catch (Exception e) {
            showError("Error", "Failed to load team members.");
        }
    }

    // Method to show an error alert
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to initialize the controller
    @FXML
    private void initialize() {
        loadTeamMembers(); // Call this method to load team members when the page is initialized
    }
}
