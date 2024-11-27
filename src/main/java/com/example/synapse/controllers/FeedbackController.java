package com.example.synapse.controllers;

import com.example.synapse.models.Feedback;
import com.example.synapse.database.DatabaseUtils;
import com.example.synapse.services.FeedbackFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.sql.Timestamp;

public class FeedbackController {

    @FXML
    private TableView<Feedback> feedbackTable;
    @FXML
    private TableColumn<Feedback, Integer> feedbackIdColumn;
    @FXML
    private TableColumn<Feedback, String> userColumn;
    @FXML
    private TableColumn<Feedback, String> categoryColumn;
    @FXML
    private TableColumn<Feedback, String> statusColumn;
    @FXML
    private TableColumn<Feedback, Timestamp> dateSubmittedColumn;

    @FXML
    private TextArea feedbackDetailsTextArea;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private TextArea actionLogTextArea;
    @FXML
    private Button updateFeedbackButton;

    private Feedback selectedFeedback;
    private FeedbackFactory feedbackFactory;

    private DatabaseUtils db = new DatabaseUtils(); // Initialize DatabaseUtils instance

    @FXML
    public void initialize() {
        feedbackFactory = new FeedbackFactory();
        // Initialize columns
        feedbackIdColumn.setCellValueFactory(data -> data.getValue().feedbackIDProperty().asObject());
        userColumn.setCellValueFactory(data -> data.getValue().usernameProperty());
        categoryColumn.setCellValueFactory(data -> data.getValue().categoryProperty());
        statusColumn.setCellValueFactory(data -> data.getValue().statusProperty());
        dateSubmittedColumn.setCellValueFactory(data -> data.getValue().submittedAtProperty());

        // Populate table with feedback data using DatabaseUtils
        loadFeedback();

        // Add selection listener for feedbackTable
        feedbackTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedFeedback = newSelection;
                displayFeedbackDetails(selectedFeedback);
            }
        });
    }

    private void loadFeedback() {
        feedbackTable.setItems(feedbackFactory.getFeedbackList());
    }

    private void displayFeedbackDetails(Feedback feedback) {
        feedbackDetailsTextArea.setText(feedback.getDetails());
        statusComboBox.setValue(feedback.getStatus());
        actionLogTextArea.setText(feedback.getActionTaken() != null ? feedback.getActionTaken() : "");
    }

    @FXML
    private void updateFeedback() {
        if (selectedFeedback == null) {
            showAlert("No Feedback Selected", "Please select a feedback entry to update.");
            return;
        }

        String newStatus = statusComboBox.getValue();
        String actionTaken = actionLogTextArea.getText();

        if (newStatus == null || newStatus.isEmpty()) {
            showAlert("Invalid Status", "Please select a valid status.");
            return;
        }

        boolean isUpdated = feedbackFactory.updateFeedback(selectedFeedback.getFeedbackID(), newStatus, actionTaken); // Call DatabaseUtils method
        if (isUpdated) {
            showAlert("Success", "Feedback updated successfully.");
            loadFeedback(); // Reload feedback data
        } else {
            showAlert("Database Error", "Could not update feedback.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
