package com.example.synapse.controllers;

import com.example.synapse.Main;
import com.example.synapse.models.*;
import com.example.synapse.database.DatabaseUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    public ArrayList<String> subTasks = new ArrayList<>(); // To store to-do items

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
        List<String> usernames = dbUtils.getAllUsernames(Main.dashboard.currentBoard);
        assignUser.getItems().addAll(usernames); // Add usernames to dropdown
    }

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
            // Save task to database and get TaskID
            int taskID = dbUtils.insertTask(Main.dashboard.currentListID, title, description, deadline, assignedUser, priority);
            dbUtils.assignTask(taskID, assignedUser);
            dbUtils.assignTask(taskID, Main.user.getUsername());

            // Save subtasks to the database
            for (String subTask : subTasks) {
                dbUtils.insertSubTask(taskID, subTask);
            }

            // Add the task to the project and list container
            ProjectBoard projectBoard = Main.dashboard.findBoardByID(Main.dashboard.currentBoard);
            ListContainer listContainer = projectBoard.findListByID(Main.dashboard.currentListID);
            listContainer.addTask(dbUtils.getTaskById(taskID));

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

    @FXML
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
                // Add text to the subtasks array
                String todoText = textField.getText().trim();
                if (!todoText.isEmpty()) {
                    subTasks.add(todoText);
                    System.out.println("To-do item added to list: " + todoText);
                }
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
}
