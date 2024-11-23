package com.example.synapse.controllers;

import com.example.synapse.database.DatabaseUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private String username;
    @FXML
    private HBox taskListsContainer; // Container for all lists and tasks


    @FXML
    private void handleCreateBoard(ActionEvent event) throws Exception {
        // Load the Create Project Board window (CreateProjectBoard.fxml)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/synapse/fxml/create_project_board.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));

        // Set preferred width and height for the Create Project Board window
        stage.setWidth(600.0);
        stage.setHeight(550.0);

        // Optionally, you can set the title of the new window
        stage.setTitle("Create Project Board");

        // Show the new window
        stage.show();
    }

    // Function to navigate to the Activity Log page
    @FXML
    private void handleActivty(ActionEvent event) throws Exception {
        loadPage("/com/example/synapse/fxml/activity_log.fxml");
    }

    // Function to navigate to the Team Chat page
    @FXML
    private void handleChat(ActionEvent event) throws Exception {
        loadPage("/com/example/synapse/fxml/chat.fxml");
    }

    // Function to navigate to the Report page
    @FXML
    private void handleReport(ActionEvent event) throws Exception {
        loadPage("/com/example/synapse/fxml/report.fxml");
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
        loadPage("/com/example/synapse/fxml/manage.fxml");
    }

    // Function to handle User Feedback button click
    @FXML
    private void handleFeedback(ActionEvent event) throws Exception {
        loadPage("/com/example/synapse/fxml/feedback.fxml");
    }

    // Function to handle adding task in the list click
    @FXML
    private void handleCard(ActionEvent event) throws Exception {
        loadPage("/com/example/synapse/fxml/task.fxml");
    }

    // Initialize the controller


    // This function would be called to check if the user is an admin (replace with real logic)
    private boolean checkAdminStatus() {
        // Placeholder logic for admin check
        return true; // Assuming the user is an admin for now
    }

    @FXML
    private VBox sidePanel; // Reference to the sidePanel VBox in FXML

    @FXML
    private Label yourBoardsLabel; // Label for "Your Boards"

    private final DatabaseUtils dbUtils = new DatabaseUtils(); // Database Utility Instance

    @FXML
    private void initialize() {

    }

    public void setUsername(String uname) {
        username=uname;
        displayUserBoards(username);
    }
    private void loadProjectBoard(int boardID) {
        System.out.println("Loading project board with ID: " + boardID);

        // Clear previous content
        taskListsContainer.getChildren().clear();

        // Fetch board details (lists and tasks) from the database
        List<String> boardLists = dbUtils.getListsByBoardID(boardID); // Fetch lists
        Map<String, List<String>> tasksByList = new HashMap<>(); // Tasks for each list

        // Fetch tasks for each list
        for (String listName : boardLists) {
            List<String> tasks = dbUtils.getTasksByListName(listName);
            tasksByList.put(listName, tasks);
        }

        // Populate the taskListsContainer with lists and tasks
        int listIndex = 1;
        for (String listName : boardLists) {
            // Create ScrollPane for the list
            ScrollPane listScrollPane = new ScrollPane();
            listScrollPane.setFitToHeight(true);
            listScrollPane.setFitToWidth(true);
            listScrollPane.getStyleClass().add("scroll-bar");

            // Create VBox for the list container
            VBox listContainer = new VBox();
            listContainer.setId("taskList" + listIndex);
            listContainer.getStyleClass().add("list-container");
            listContainer.setSpacing(10);

            // Add list title
            Label listTitle = new Label(listName);
            listTitle.setId("taskList" + listIndex + "Title");
            listTitle.getStyleClass().add("list-title");
            listContainer.getChildren().add(listTitle);

            // Create VBox for tasks
            VBox tasksContainer = new VBox();
            tasksContainer.setId("taskList" + listIndex + "Cards");
            tasksContainer.setSpacing(10);

            // Add tasks to the tasks container
            List<String> tasks = tasksByList.get(listName);
            if (tasks != null) {
                int taskIndex = 1;
                for (String task : tasks) {
                    StackPane taskCard = new StackPane();
                    taskCard.setId("task" + taskIndex + "Card");
                    taskCard.getStyleClass().add("task-card");

                    Label taskLabel = new Label(task);
                    taskLabel.setId("task" + taskIndex + "Label");
                    taskLabel.getStyleClass().add("task-labels");

                    taskCard.getChildren().add(taskLabel);
                    tasksContainer.getChildren().add(taskCard);

                    taskIndex++;
                }
            }

            // Add tasks container to the list container
            listContainer.getChildren().add(tasksContainer);

            // Add an "Add Card" button
            Button addCardButton = new Button("+ Add a card");
            addCardButton.setId("addCardButton" + listIndex);
            addCardButton.getStyleClass().add("button");
            addCardButton.setOnAction(event -> {
                try {
                    handleCard(event); // Call the handleCard method
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            listContainer.getChildren().add(addCardButton);

            // Add the list container to the ScrollPane
            listScrollPane.setContent(listContainer);

            // Add the ScrollPane to the taskListsContainer
            taskListsContainer.getChildren().add(listScrollPane);

            listIndex++;
        }
    }


    private void handleAddTask(String listName) {
        System.out.println("Adding a task to list: " + listName);
        // Logic to handle adding a task dynamically
    }


    private void displayUserBoards(String username) {
        // Retrieve project IDs and names for the user
        List<Integer> projectIDs = dbUtils.getProjectIDsByUsername(username);
        List<String> projectBoards = dbUtils.getProjectBoardNamesByIDs(projectIDs);

        // Clear any existing content in the side panel
        sidePanel.getChildren().clear();

        if (projectBoards.isEmpty()) {
            yourBoardsLabel.setText("No Boards Found");
            System.out.println("No boards found for user: " + username);
        } else {
            yourBoardsLabel.setText("Your Boards");
            System.out.println("Boards found for user " + username + ":");

            for (int i = 0; i < projectBoards.size(); i++) {
                String boardName = projectBoards.get(i);
                int boardID = projectIDs.get(i);

                System.out.println("- " + boardName + " (ID: " + boardID + ")"); // Print board name and ID

                // Create a StackPane for each board
                StackPane boardPane = new StackPane();
                Label boardLabel = new Label(boardName);
                boardPane.getChildren().add(boardLabel);
                boardPane.getStyleClass().add("board-pane"); // Optional CSS class for styling

                // Add a click listener to load the board by ID
                boardPane.setOnMouseClicked(event -> loadProjectBoard(boardID));

                // Add the board pane to the side panel
                sidePanel.getChildren().add(boardPane);
            }
        }
    }



}
