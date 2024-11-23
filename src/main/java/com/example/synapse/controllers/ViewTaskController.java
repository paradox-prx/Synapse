package com.example.synapse.controllers;

import com.example.synapse.database.DatabaseUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

import  java.util.List;

public class ViewTaskController {

    @FXML
    private Label descriptionText;
    @FXML
    private VBox todoSection;
    @FXML
    private VBox commentsSection;
    @FXML
    private TextField commentInput;

    private DatabaseUtils db = new DatabaseUtils();

    @FXML
    public void initialize() {
        // Initialize description
        String description = fetchTaskDescription();
        setDescription(description);

        // Load subtasks
        loadSubtasks();

        // Load comments
        loadComments();
    }

    public void setDescription(String description) {
        if (description != null && !description.isEmpty()) {
            descriptionText.setText(description);
        } else {
            descriptionText.setText("No description available.");
        }
    }

    private String fetchTaskDescription() {
        // Fetch task description from DB
        return db.getTaskDescription();
    }

    private void loadSubtasks() {
        // Fetch subtasks from DB
        List<String> subtasks = db.getSubtasks();
        for (String subtask : subtasks) {
            CheckBox checkBox = new CheckBox(subtask);
            checkBox.setOnAction(event -> handleSubtaskCompletion(checkBox, subtask));
            todoSection.getChildren().add(checkBox);
        }
    }

    private void loadComments() {
        // Fetch comments from DB
        List<String> comments = db.getComments();
        for (String comment : comments) {
            addCommentToUI(comment);
        }
    }

    private void handleSubtaskCompletion(CheckBox checkBox, String subtask) {
        if (checkBox.isSelected()) {
            // Mark subtask as complete in DB
            db.markSubtaskComplete(subtask);

            // Remove it from the UI
            todoSection.getChildren().remove(checkBox);
        }
    }

    @FXML
    public void handleSubmitComment(ActionEvent event) {
        String newComment = commentInput.getText().trim();
        if (!newComment.isEmpty()) {
            // Add the new comment to the database
            db.addComment(newComment);

            // Clear the input field
            commentInput.clear();

            // Display the new comment in the UI
            addCommentToUI(newComment);
        }
    }

    private void addCommentToUI(String comment) {
        Label commentLabel = new Label(comment);
        commentLabel.setStyle("-fx-background-color: #EFEFEF; -fx-padding: 5; -fx-border-color: #CCCCCC; -fx-border-radius: 3;");
        commentsSection.getChildren().add(commentLabel);
    }

    @FXML
    public void handleMarkAsDone(ActionEvent event) {
        // Logic for marking the entire task as complete
        db.markTaskAsComplete();
    }
}
