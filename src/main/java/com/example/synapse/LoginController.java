package com.example.synapse;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.Hyperlink;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo1/sign-up.fxml"));
            Scene signUpScene = new Scene(loader.load());

            // Get the stage from the hyperlink click event
            Hyperlink source = (Hyperlink) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow(); // Get the window (stage) from the scene
            stage.setWidth(1200);  // Set the width of the window
            stage.setHeight(700); // Set the height of the window
            // Set the new scene
            stage.setScene(signUpScene);
            stage.setTitle("Sign-Up Page");
            stage.show(); // Display the new scene
        } catch (IOException e) {
            e.printStackTrace(); // Handle IOException (if FXML fails to load)
        }
    }
}
