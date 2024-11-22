package com.example.synapse.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;

public class LoginController {

    // Define FXML variables to bind to the text fields
    @FXML
    private TextField usernameOrEmailField;

    @FXML
    private TextField passwordField;

    @FXML
    private ImageView logoImageView; // Bind to the ImageView in FXML

    @FXML
    private void initializeLogoImage() {
        try {
            String resolvedPath = getClass().getResource("/images/synapse-logo.png").toExternalForm();
            if (resolvedPath == null) {
                System.err.println("Image path is incorrect or file not found.");
            } else {
                System.out.println("Resolved Image Path: " + resolvedPath);
                Image logoImage = new Image(resolvedPath); // Load image
                logoImageView.setImage(logoImage); // Set image
                System.out.println("Image successfully loaded.");
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
        }
    }

    // Handle Login logic when the button is clicked
    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String usernameOrEmail = usernameOrEmailField.getText();
        String password = passwordField.getText();

        // Check if any fields are empty
        if (usernameOrEmail.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Invalid Input", "Please fill out all fields.");
            return;
        }

        // Simulate authentication logic (e.g., validate user data)
        if (usernameOrEmail.equals("admin") && password.equals("pass") ||usernameOrEmail.equals("a") && password.equals("a")  ) { // Example credentials
            this.openDashboard(event);

        } else {
            showAlert(AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    // Method to open the dashboard
    private void openDashboard(ActionEvent event) {
        try {
            // Load the dashboard.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/synapse/fxml/dashboard.fxml"));
            Scene dashboardScene = new Scene(loader.load());

            // Get the current stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(dashboardScene);
            stage.setTitle("Dashboard");
            stage.setWidth(1200); // Set the width of the window
            stage.setHeight(700); // Set the height of the window
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Failed to load the dashboard.");
        }
    }

    // Handle Registration logic when the button is clicked
    @FXML
    private void handleRegister() {
        String usernameOrEmail = usernameOrEmailField.getText();
        String password = passwordField.getText();

        // Check if any fields are empty
        if (usernameOrEmail.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Invalid Input", "Please fill out all fields.");
            return;
        }

        // Simulate registration logic (e.g., save user data to a database)
        showAlert(AlertType.INFORMATION, "Registration Successful", "You have successfully registered.");
    }

    // Method to display alerts
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }

    public void handleSignUp(ActionEvent event) {
        try {
            // Load the Sign-Up FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/synapse/fxml/sign-up.fxml"));
            Scene signUpScene = new Scene(loader.load());

            // Get the stage from the hyperlink click event
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Get the window (stage) from the scene
            stage.setScene(signUpScene);
            stage.setTitle("Sign-Up Page");
            stage.setWidth(1200);  // Set the width of the window
            stage.setHeight(700); // Set the height of the window
            stage.show(); // Display the new scene
        } catch (IOException e) {
            e.printStackTrace(); // Handle IOException (if FXML fails to load)
        }
    }
}
