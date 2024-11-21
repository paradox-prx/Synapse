package com.example.synapse;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpController {

    // Define FXML variables to bind to the text fields
    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField confirmPasswordField;

    // Handle Sign-Up logic when the button is clicked
    @FXML
    private void handleSignUp() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Check if any fields are empty
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(AlertType.ERROR, "Invalid Input", "Please fill out all fields.");
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            showAlert(AlertType.ERROR, "Password Mismatch", "Passwords do not match.");
            return;
        }

        // Simulate registration logic (e.g., save user data to a database)
        showAlert(AlertType.INFORMATION, "Registration Successful", "You have successfully signed up.");
    }

    // Method to display alerts
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleLogin(ActionEvent event) {
        try {
            // Load the Sign-Up FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/synapse/login.fxml"));
            Scene loginScene = new Scene(loader.load());

            // Get the stage from the hyperlink click event
            Hyperlink source = (Hyperlink) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow(); // Get the window (stage) from the scene
            stage.setWidth(1200);  // Set the width of the window
            stage.setHeight(700); // Set the height of the window
            // Set the new scene
            stage.setScene(loginScene);
            stage.setTitle("Login Page");
            stage.show(); // Display the new scene
        } catch (IOException e) {
            e.printStackTrace(); // Handle IOException (if FXML fails to load)
        }
    }
}
