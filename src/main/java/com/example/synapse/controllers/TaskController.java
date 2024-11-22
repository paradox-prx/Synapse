package com.example.synapse.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskController {

    @FXML
    private VBox todoSection; // Reference to the to-do section VBox

    @FXML
    private Button addTodoButton; // Reference to the "Add Item" button

    @FXML
    private VBox commentsSection; // Reference to the comments section VBox

    @FXML
    private TextField commentInput; // Reference to the input field for comments

    @FXML
    private Button submitCommentButton; // Reference to the "Submit Comment" button

    public void handleSetDeadlines(ActionEvent event) {
        System.out.println("Set Deadlines clicked");
        // Add functionality to open calendar and set deadlines
    }

    public void handleAssignTasks(ActionEvent event) {
        System.out.println("Assign Tasks clicked");
        // Add functionality to assign team members
    }

    public void handleSetPriorities(ActionEvent event) {
        System.out.println("Set Priorities clicked");
        // Add functionality to set task priorities
    }

    public void handleSendNotifications(ActionEvent event) {
        System.out.println("Send Deadline Notification clicked");
        // Add functionality to send notifications via email
    }

    public void handleEditDescription(ActionEvent event) {
        System.out.println("Edit Description clicked");
        // Add functionality to edit task description
    }

    public void handleAddTodoItem(ActionEvent event) {
        System.out.println("Add To-Do Item clicked");

        // Create a new HBox for the to-do item
        HBox newTodo = new HBox();
        newTodo.getStyleClass().add("todo-item"); // Apply the CSS class
        newTodo.setSpacing(10); // Add spacing between elements

        // Create a new CheckBox
        CheckBox checkBox = new CheckBox();
        checkBox.getStyleClass().add("todo-checkbox");

        // Create a new TextField for editable text
        TextField textField = new TextField();
        textField.setPromptText("New to-do item"); // Set placeholder text
        textField.getStyleClass().add("todo-textfield"); // Apply the CSS class
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                // Handle saving or processing when focus is lost
                System.out.println("To-do item updated: " + textField.getText());
            }
        });

        // Add the CheckBox and TextField to the HBox
        newTodo.getChildren().addAll(checkBox, textField);

        // Insert the new HBox above the "Add Item" button
        int buttonIndex = todoSection.getChildren().indexOf(addTodoButton);
        todoSection.getChildren().add(buttonIndex, newTodo);

        // Limit the size of the ScrollPane content dynamically if needed
        if (todoSection.getChildren().size() > 15) { // Arbitrary limit
            todoSection.setPrefHeight(200); // Set a fixed height to trigger scroll
        }
    }



    public void handleAddComment(ActionEvent event) {
        String commentText = commentInput.getText();

        // Validate that the input is not empty
        if (commentText == null || commentText.trim().isEmpty()) {
            System.out.println("Empty comment not allowed!");
            return;
        }

        // Create a new HBox for the comment
        HBox commentBox = new HBox();
        commentBox.setSpacing(10);
        commentBox.getStyleClass().add("comment-box"); // Apply CSS class for styling

        // Create a label for the comment text
        Label commentLabel = new Label(commentText);
        commentLabel.getStyleClass().add("comment-label");

        // Create a label for the timestamp
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a, dd MMM yyyy");
        Label timeLabel = new Label(now.format(formatter));
        timeLabel.getStyleClass().add("comment-time");

        // Add the comment and timestamp to the HBox
        commentBox.getChildren().addAll(commentLabel, timeLabel);

        // Add the HBox to the comments section
        commentsSection.getChildren().add(commentBox);

        // Clear the comment input field
        commentInput.clear();

        // Limit the size of the ScrollPane content dynamically if needed
        if (commentsSection.getChildren().size() > 15) { // Arbitrary limit
            commentsSection.setPrefHeight(400); // Set a fixed height to trigger scroll
        }
    }

}
