package com.example.synapse.controllers;

import com.example.synapse.Main;
import com.example.synapse.database.DatabaseUtils;
import com.example.synapse.models.Dashboard;
import com.example.synapse.models.ProjectBoard;
import com.example.synapse.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class CreateProjectBoardController {

    @FXML
    private ListView<String> teamMembersList; // ListView to display team members

    private final DatabaseUtils dbUtils = new DatabaseUtils();
    private final ObservableList<String> selectedMembers = FXCollections.observableArrayList(); // Track selected members

    public Button createBoardButton;
    public TextField boardNameInput;
    public TextArea boardDescriptionInput;

    @FXML
    public void handleCreateBoardAction(ActionEvent event) {
        String boardName = boardNameInput.getText();
        String description = boardDescriptionInput.getText();
        String createdBy = Main.user.getUsername();
        handleCreateProject(boardName, description, createdBy, Main.dashboard);
    }

    @FXML
    public void handleCreateProject(String boardName, String description, String createdBy, Dashboard dashboard)  {
        // Logic for creating the project board
        // Step 1: Create the project board
        int newBoardID = dbUtils.createProjectBoard(boardName, description, createdBy);
        if (newBoardID > 0) {
            System.out.println("Project board created with ID: " + newBoardID);

            // Step 2: Add team members to the board
            for (String member : selectedMembers) {
                boolean success = dbUtils.addUserToBoard(newBoardID, member);
                if (success) {
                    System.out.println("Added user " + member + " to board ID: " + newBoardID);
                } else {
                    System.out.println("Failed to add user " + member);
                }
            }

            // Step 3: Update the dashboard
            ProjectBoard newBoard = new ProjectBoard(newBoardID, boardName, description, new User(createdBy, "", "", "Project Manager", true));
            dashboard.addProjectBoard(newBoard);

            // Refresh the UI
//            dashboard.populateDashboard();
        } else {
            System.out.println("Failed to create the project board.");
        }

        // Close the Create Project Board window after creation
        Stage stage = (Stage) createBoardButton.getScene().getWindow();
        stage.close();
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


    // Load active team members from the database
    private void loadTeamMembers() {
        ObservableList<String> teamMembers = FXCollections.observableArrayList();

        try {
            teamMembers.addAll(dbUtils.getActiveTeamMembers());
            teamMembersList.setItems(teamMembers); // Set items to the ListView

            // Use CheckBoxListCell to add checkboxes to the ListView
            teamMembersList.setCellFactory(CheckBoxListCell.forListView(item -> {
                CheckBox checkBox = new CheckBox();
                checkBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                    if (isSelected) {
                        selectedMembers.add(item); // Add to selected members
                    } else {
                        selectedMembers.remove(item); // Remove from selected members
                    }
                });
                return checkBox.selectedProperty();
            }));
        } catch (Exception e) {
            showError("Error", "Failed to load team members.");
        }
    }
}
