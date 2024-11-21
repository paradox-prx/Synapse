package com.example.synapse;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DashboardController {

    @FXML
    private TextField searchField;
    // Top Navbar buttons
    @FXML
    private Button activtyButton;
    @FXML
    private Button recentButton;
    @FXML
    private Button teamChat;
    @FXML
    private Button reportButton;
    @FXML
    private Button createButton;
    @FXML
    private Button manageUsersButton;
    @FXML
    private Button userFeedbackButton;
    @FXML
    private Button addCardButton;

    // Function to navigate to the Activity Log page
    @FXML
    private void handleActivty(ActionEvent event) throws Exception {
        loadPage("activity_log.fxml");
    }

    // Function to navigate to the Team Chat page
    @FXML
    private void handleChat(ActionEvent event) throws Exception {
        loadPage("chat.fxml");
    }

    // Function to navigate to the Report page
    @FXML
    private void handleReport(ActionEvent event) throws Exception {
        loadPage("report.fxml");
    }

    // Function to load the corresponding FXML file in a new window
    private void loadPage(String fxmlFile) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    // Function to add Manage Users and User Feedback buttons when an Admin logs in
    public void showAdminButtons(boolean isAdmin) {
        if (isAdmin) {
            manageUsersButton.setVisible(true);
            userFeedbackButton.setVisible(true);
        } else {
            manageUsersButton.setVisible(false);
            userFeedbackButton.setVisible(false);
        }
    }

    // Function to handle Manage Users button click
    @FXML
    private void handleManageUsers(ActionEvent event) throws Exception {
        loadPage("manage.fxml");
    }

    // Function to handle User Feedback button click
    @FXML
    private void handleFeedback(ActionEvent event) throws Exception {
        loadPage("feedback.fxml");
    }

    // Function to handle adding task in the list click
    @FXML
    private void handleCard(ActionEvent event) throws Exception {
        loadPage("task.fxml");
    }

    // Initialize the controller
    @FXML
    private void initialize() {
        // Assuming the admin status is passed from the login or elsewhere
        boolean isAdmin = checkAdminStatus();
        showAdminButtons(isAdmin);
        // Initialize components or add listeners
//        searchField.setOnAction(event -> {
//            System.out.println("Search Query: " + searchField.getText());
//        });
    }

    // This function would be called to check if the user is an admin (replace with real logic)
    private boolean checkAdminStatus() {
        // Placeholder logic for admin check
        return true; // Assuming the user is an admin for now
    }
}
