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

    // new board
    ProjectBoard newBoard = new ProjectBoard();

    @FXML
    public void handleCreateBoardAction(ActionEvent event) {
        String boardName = boardNameInput.getText().trim();
        String description = boardDescriptionInput.getText().trim();
        String createdBy = Main.user.getUsername();

        // Input validation
        if (boardName.isEmpty()) {
            showAlert("Validation Error", "Board name is required.");
            return;
        }

        if (boardName.length() < 3 || boardName.length() > 50) {
            showAlert("Validation Error", "Board name must be between 3 and 50 characters.");
            return;
        }

        if (description.isEmpty()) {
            showAlert("Validation Error", "Board description is required.");
            return;
        }

        if (description.length() > 200) {
            showAlert("Validation Error", "Board description cannot exceed 200 characters.");
            return;
        }

        // Proceed with board creation
        enterBoardDetails(boardName, description, createdBy, Main.dashboard);
    }

    @FXML
    public void enterBoardDetails(String boardName, String description, String createdBy, Dashboard dashboard)  {

//        Create the project board
        Boolean check = newBoard.createBoard(boardName, description, Main.user);
        if (check) {
            newBoard.addUsers(selectedMembers);
            dashboard.addProjectBoard(newBoard);

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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
