package com.example.synapse.controllers;

import com.example.synapse.Main;
import com.example.synapse.database.DatabaseUtils;
import com.example.synapse.models.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ViewTaskController {
    public int taskID;
    public int listID;
    public String title;
    public String priority;
    public String description;
    public User assignedUser;

    @FXML
    private Label titleLabel;

    @FXML
    private Label descriptionText;

    @FXML
    private Label hoursLeft;

    @FXML
    private Circle lowCircle;

    @FXML
    private Circle medCircle;

    @FXML
    private Circle highCircle;

    @FXML
    private Circle urgCircle;

    @FXML
    private VBox todoSection;

    @FXML
    private VBox commentsSection;

    @FXML
    private TextField commentInput;

    @FXML
    private Button markAsCompleted;

    private DatabaseUtils db = new DatabaseUtils();
    private LocalDateTime deadline;

    @FXML
    public void initialize() {
        // Initialize task details
        listID= Main.dashboard.currentListID;
        taskID=Main.dashboard.currentTaskID;
        checkUserAssignment();

        if(db.HasTaskBeenCompleted(taskID)) {

            markAsCompleted.setDisable(true);
        }


        title = fetchTaskTitle();
        setTitle(title);

        description = fetchTaskDescription();
        setDescription(description);

        deadline = fetchTaskDeadline();
        //startCountdown();
        LocalDateTime deadline = db.fetchTaskDeadline(taskID);
        displayDeadlineCountdown(deadline);

        priority = fetchTaskPriority();
        setPriorityIndicator(priority);

        // Load subtasks
        loadSubtasks();

        // Load comments
        loadComments();

    }

    private void checkUserAssignment() {
        String currentUsername = Main.user.getUsername();
        boolean isAssigned = db.isUserAssignedToTask(taskID, currentUsername);

        if (isAssigned) {
            markAsCompleted.setVisible(true); // Show the button
        } else {
            markAsCompleted.setVisible(false); // Hide the button
        }
    }

    public void setTitle(String title) {
        titleLabel.setText(title != null ? title : "No Title Available");
    }

    public void setDescription(String description) {
        descriptionText.setText(description != null ? description : "No description available.");
    }

    private String fetchTaskTitle() {
        return db.getTaskTitle(taskID);
    }

    private String fetchTaskDescription() {
        return db.getTaskDescription(taskID);
    }

    private LocalDateTime fetchTaskDeadline() {
        return db.getTaskDeadline(taskID); // Fetch deadline as LocalDateTime
    }

    private String fetchTaskPriority() {
        return db.getTaskPriority(taskID);
    }



    private void setPriorityIndicator(String priority) {
        // Reset all circles to transparent
        resetPriorityCircles();

        // Set the appropriate circle to blue
        switch (priority.toLowerCase()) {
            case "low":
                lowCircle.setFill(Color.web("#5865f2"));
                break;
            case "normal":
            case "med":
                medCircle.setFill(Color.web("#5865f2"));
                break;
            case "high":
                highCircle.setFill(Color.web("#5865f2"));
                break;
            case "urgent":
            case "urg":
                urgCircle.setFill(Color.web("#5865f2"));
                break;
            default:
                break;
        }
    }

    private void resetPriorityCircles() {
        lowCircle.setFill(Color.TRANSPARENT);
        medCircle.setFill(Color.TRANSPARENT);
        highCircle.setFill(Color.TRANSPARENT);
        urgCircle.setFill(Color.TRANSPARENT);
    }

    private void loadSubtasks() {
        List<String> subtasks = db.getSubtasks(taskID); // Fetch subtasks for the task
        for (String subtask : subtasks) {
            // Create a new HBox for the subtask item
            HBox subtaskBox = new HBox();
            subtaskBox.getStyleClass().add("todo-item"); // Apply CSS class for styling
            subtaskBox.setSpacing(10); // Add spacing between elements

            // Create a new CheckBox for the subtask
            CheckBox checkBox = new CheckBox(subtask);
            checkBox.getStyleClass().add("todo-checkbox"); // Apply CSS class for styling
            checkBox.setOnAction(event -> handleSubtaskCompletion(checkBox, subtask));

            // Add the CheckBox to the HBox
            subtaskBox.getChildren().add(checkBox);

            // Add the styled HBox to the todoSection
            todoSection.getChildren().add(subtaskBox);
        }
    }


    private void loadComments() {
        List<String[]> comments = db.getComments(taskID); // Fetch comments as [Username, CommentText, CreatedAt]
        for (String[] comment : comments) {
            String username = comment[0];
            String commentText = comment[1];
            String createdAt = comment[2];
            addCommentToUI(username, commentText, createdAt); // Add each comment to the UI
        }
    }
    private void addCommentToUI(String username, String commentText, String createdAt) {
        // Create a new HBox for the comment
        HBox commentBox = new HBox();
        commentBox.setSpacing(10);
        commentBox.getStyleClass().add("comment-box"); // Apply CSS class for styling

        // Create a label for the username
        Label usernameLabel = new Label(username + ":");
        usernameLabel.getStyleClass().add("username-label");

        // Create a label for the comment text
        Label commentLabel = new Label(commentText);
        commentLabel.getStyleClass().add("comment-label");

        // Create a label for the timestamp
        Label timeLabel = new Label(createdAt);
        timeLabel.getStyleClass().add("comment-time");

        // Add the username, comment, and timestamp to the HBox
        commentBox.getChildren().addAll(usernameLabel, commentLabel, timeLabel);

        // Add the HBox to the comments section
        commentsSection.getChildren().add(commentBox);

        // Limit the size of the ScrollPane content dynamically if needed
        if (commentsSection.getChildren().size() > 15) { // Arbitrary limit
            commentsSection.setPrefHeight(400); // Set a fixed height to trigger scroll
        }
    }


    private void handleSubtaskCompletion(CheckBox checkBox, String subtask) {
        if (checkBox.isSelected()) {
            db.markSubtaskComplete(taskID, subtask); // Mark the subtask as completed
            checkBox.setDisable(true); // Disable the checkbox
        }
    }

    @FXML
    public void handleSubmitComment(ActionEvent event) {
        String newComment = commentInput.getText().trim();
        if (!newComment.isEmpty()) {
            db.addComment(taskID, newComment); // Add comment to the database
            commentInput.clear(); // Clear input field
            addCommentToUI(newComment); // Add comment to UI
        }
    }

    private void addCommentToUI(String commentText) {
        // Get the username of the current logged-in user
        String username = Main.user.getUsername();

        // Create a new HBox for the comment
        HBox commentBox = new HBox();
        commentBox.setSpacing(10);
        commentBox.getStyleClass().add("comment-box"); // Apply CSS class for styling

        // Create a label for the username
        Label usernameLabel = new Label(username + ":");
        usernameLabel.getStyleClass().add("username-label");

        // Create a label for the comment text
        Label commentLabel = new Label(commentText);
        commentLabel.getStyleClass().add("comment-label");

        // Create a label for the timestamp
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a, dd MMM yyyy");
        Label timeLabel = new Label(now.format(formatter));
        timeLabel.getStyleClass().add("comment-time");

        // Add the username, comment, and timestamp to the HBox
        commentBox.getChildren().addAll(usernameLabel, commentLabel, timeLabel);

        // Add the HBox to the comments section
        commentsSection.getChildren().add(commentBox);

        // Clear the comment input field
        commentInput.clear();

        // Limit the size of the ScrollPane content dynamically if needed
        if (commentsSection.getChildren().size() > 15) { // Arbitrary limit
            commentsSection.setPrefHeight(400); // Set a fixed height to trigger scroll
        }
    }


    private void displayDeadlineCountdown(LocalDateTime deadline) {
        if (deadline != null) {
            new Thread(() -> {
                while (true) {
                    try {
                        LocalDateTime now = LocalDateTime.now();
                        if (now.isAfter(deadline)) {
                            // Deadline has passed
                            javafx.application.Platform.runLater(() -> hoursLeft.setText("Expired"));
                            break;
                        }

                        java.time.Duration duration = java.time.Duration.between(now, deadline);

                        long days = duration.toDays();
                        long hours = duration.toHours() % 24;
                        long minutes = duration.toMinutes() % 60;
                        long seconds = duration.toSeconds() % 60;

                        javafx.application.Platform.runLater(() -> {
                            if (days > 0) {
                                // Show days and hours if more than 24 hours remain
                                hoursLeft.setText(String.format("%d days, %02d hours", days, hours));
                            } else {
                                // Show hours, minutes, and seconds if less than 24 hours remain
                                hoursLeft.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
                            }
                        });

                        Thread.sleep(1000); // Update every second
                    } catch (InterruptedException e) {
                        System.out.println("Countdown thread interrupted: " + e.getMessage());
                        break;
                    }
                }
            }).start();
        } else {
            hoursLeft.setText("No Deadline");
        }
    }


    @FXML
    public void handleMarkAsDone(ActionEvent event) {
        db.markTaskAsComplete(taskID); // Mark task as complete in the database
        markAsCompleted.setDisable(true); // Disable the "Mark As Completed" button
    }
}
