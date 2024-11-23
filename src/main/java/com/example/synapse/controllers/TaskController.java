package com.example.synapse.controllers;

import com.example.synapse.Main;
import com.example.synapse.models.ListContainer;
import com.example.synapse.models.ProjectBoard;
import com.example.synapse.models.Task;
import com.example.synapse.database.DatabaseUtils;

import com.example.synapse.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class TaskController {
    public TextField taskTitle;
    private Task task;


    public TextArea descriptionTextArea;
    public DatePicker deadlinePick;
    public ComboBox assignUser;
    public ComboBox setPriority;
    public Button createTaskButton;

    @FXML
    private VBox todoSection; // Reference to the to-do section VBox

    @FXML
    private Button addTodoButton; // Reference to the "Add Item" button

    @FXML
    private VBox commentsSection; // Reference to the comments section VBox

    @FXML
    private TextField commentInput; // Reference to the input field for comments

    private DatabaseUtils dbUtils;
    private int boardID; // Passed from the board
    private Task createdTask;

    public void setBoardID(int boardID) {
        System.out.println("Board ID set to: " + boardID);
        this.boardID = boardID;
    }

    public Task getCreatedTask() {
        return createdTask;
    }

    public void initialize() {
        dbUtils = new DatabaseUtils();

        // Populate priority dropdown
        setPriority.getItems().addAll("Low", "Normal", "High", "Urgent");
        System.out.println("Priority dropdown populated.");
        // Populate assignUser dropdown (you can replace with actual user data from the board)

        List<String> usernames = dbUtils.getAllUsernames(Main.dashboard.currentBoard);
        assignUser.getItems().addAll(usernames); // Add usernames to dropdown
        };

    @FXML
    public void createTask() {
        // Retrieve field values
        String title = taskTitle.getText();
        String description = descriptionTextArea.getText();
        LocalDate deadline = deadlinePick.getValue();
        String assignedUser = assignUser.getValue().toString();
        String priority = setPriority.getValue().toString();

        // Validation
        if (title == null || title.isEmpty()) {
            showAlert("Error", "Task title is required.");
            return;
        }
        if (description == null || description.isEmpty()) {
            showAlert("Error", "Task description is required.");
            return;
        }
        if (deadline == null || deadline.isBefore(LocalDate.now())) {
            showAlert("Error", "Deadline must be a future date.");
            return;
        }
        if (assignedUser == null || assignedUser.isEmpty()) {
            showAlert("Error", "Please select a user to assign the task.");
            return;
        }
        if (priority == null || priority.isEmpty()) {
            showAlert("Error", "Please select a priority.");
            return;
        }

        try {
            // Save to database and get TaskID
            int taskID = dbUtils.insertTask(Main.dashboard.currentListID, title, description, deadline, assignedUser, priority);
            ProjectBoard projectBoard = Main.dashboard.findBoardByID(Main.dashboard.currentBoard);
            ListContainer listContainer = projectBoard.findListByID(Main.dashboard.currentListID);
            Task t = dbUtils.getTaskById(taskID);
            listContainer.addTask(dbUtils.getTaskById(taskID));

            // Create Task instance
            //User user =dbUtils.getUserbyUsername(assignUser);
            //createdTask = new Task(taskID, boardID, title, description, deadline, user, priority);

            // Close current window and return to board
            Stage stage = (Stage) taskTitle.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            showAlert("Error", "Failed to create task: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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
