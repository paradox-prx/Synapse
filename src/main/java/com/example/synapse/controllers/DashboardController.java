package com.example.synapse.controllers;

import com.example.synapse.database.DatabaseUtils;
import com.example.synapse.models.ListContainer;
import com.example.synapse.models.ProjectBoard;
import com.example.synapse.models.Task;
import com.example.synapse.models.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.synapse.Main;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardController {
    User user;
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
    private Button addListButton;
    @FXML
    private VBox sidePanel; // Reference to the sidePanel VBox in FXML
    @FXML
    private Label yourBoardsLabel; // Label for "Your Boards"
    private final DatabaseUtils dbUtils = new DatabaseUtils(); // Database Utility Instance




    public void refreshUI() {
        Platform.runLater(() -> {
            System.out.println("Refreshing the UI...");
            displayUserBoards();
        });
    }

    public void refreshProjectBoardUI() {
        Platform.runLater(() -> {
            System.out.println("Refreshing the UI...");
            loadProjectBoard(Main.dashboard.currentBoard);
        });
    }


    public void refreshUIForTasks() {
        Platform.runLater(() -> {
            System.out.println("Refreshing Task UI...");
            refreshSpecificList(Main.dashboard.currentListID);
        });
    }


    @FXML
    // Function to create board
    private void createProjectBoard(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/synapse/fxml/create_project_board.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.setWidth(600);
        stage.setHeight(670);
        stage.setTitle("Create Project Board");

        // Add an event handler to refresh UI after the window is closed
        stage.setOnHidden(e -> {
            System.out.println("Create Board window closed.");
            refreshUI();
        });

        // Show the new window
        stage.show();
        System.out.println("Board creation window opened.");
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
        System.out.println("JANJUA");
    }

    // Function to add Manage Users and User Feedback buttons when an Admin logs in
    public void showAdminButtons() {
        String check = Main.user.getRole();
        if (check.equals("Admin")) {
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
    private void handleCard(ActionEvent event,int boardID, int listID) throws Exception {
        Main.dashboard.currentBoard=boardID;
        Main.dashboard.currentListID=listID;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/synapse/fxml/task.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));

        // Add an event handler to refresh UI after the window is closed
        stage.setOnHidden(e -> {

            System.out.println("Create Board window closed.");
            refreshUIForTasks();
        });

        stage.show();
    }



    // This function would be called to check if the user is an admin (replace with real logic)
    private boolean checkAdminStatus() {
        // Placeholder logic for admin check
        return true; // Assuming the user is an admin for now
    }

    @FXML
    private void initialize() {
        user=Main.user;
        System.out.println("done");
        System.out.println(user.getUsername());
        Main.dashboard.populateDashboard();
        showAdminButtons();
    }

    public void setUsername(String uname) {
        username=uname;
        displayUserBoards();
    }


    private void loadProjectBoard(int boardID) {
        System.out.println("Loading project board with ID: " + boardID);

        // Clear previous content
        taskListsContainer.getChildren().clear();

        // Retrieve the project board object from Main.dashboard
        ProjectBoard projectBoard = Main.dashboard.findBoardByID(boardID);

        Main.dashboard.currentBoard = boardID;
        if (projectBoard == null) {
            System.out.println("Project board not found in dashboard.");
            return;
        }

        // Get the list containers for the project board
        List<ListContainer> boardLists = projectBoard.getLists();

        // Populate the taskListsContainer with lists and tasks
        int listIndex = 2;
        for (ListContainer listContainer : boardLists) {
            listIndex = listContainer.getListID();
            // Create ScrollPane for the list
            ScrollPane listScrollPane = new ScrollPane();
            listScrollPane.setFitToHeight(true);
            listScrollPane.setFitToWidth(true);
            listScrollPane.getStyleClass().add("scroll-pane-list");

            // Create VBox for the list container
            VBox listContainerBox = new VBox();
            listContainerBox.setId("taskList" + listIndex);
            listContainerBox.getStyleClass().add("list-container");
            listContainerBox.setSpacing(10);

            // Add list title
            Label listTitle = new Label(listContainer.getName());
            listTitle.setId("taskList" + listIndex + "Title");
            listTitle.getStyleClass().add("task-list-title");
            listContainerBox.getChildren().add(listTitle);

            // Create VBox for tasks
            VBox tasksContainer = new VBox();
            tasksContainer.setId("taskList" + listIndex + "Cards");
            tasksContainer.getStyleClass().add("list-container");
            tasksContainer.setSpacing(20);

            // Add tasks to the tasks container
            List<Task> tasks = listContainer.getTasks();
            if (tasks != null) {
                int taskIndex = listContainer.getTasks().size();
                for (Task task : tasks) {

                    // Create the StackPane for the task card
                    StackPane taskCard = new StackPane();
                    taskCard.setId("task" + taskIndex + "Card");
                    taskCard.getStyleClass().add("task-card");

                    // Create the HBox for holding the label and image
                    HBox taskContent = new HBox();
                    taskContent.setSpacing(10); // Add spacing between image and label
                    taskContent.setAlignment(Pos.CENTER_LEFT); // Align content within HBox
                    taskContent.getStyleClass().add("task-content");

                    // Create the Label for the task title
                    Label taskLabel = new Label(task.getTitle());
                    taskLabel.setId("task" + taskIndex + "Label");
                    taskLabel.getStyleClass().add("task-labels");

                    // Determine the image path based on the task conditions
                    String imagePath = null;
                    if (!task.isComplete()) {
                        LocalDateTime now = LocalDateTime.now();
                        LocalDateTime taskDeadline = task.getDeadline().atTime(23, 59);
                        Duration duration = Duration.between(now, taskDeadline);
                        long hoursLeft = duration.toHours();

                        if (hoursLeft < 24) {
                            imagePath = "/com/example/synapse/img/alarm.png";
                        } else if ("Urgent".equalsIgnoreCase(task.getPriority())) {
                            imagePath = "/com/example/synapse/img/alert.png";
                        }
                    } else {
                        imagePath = "/com/example/synapse/img/check.png";
                    }

                    // Load the image
                    try {
                        if (imagePath != null) {
                            ImageView imageView = new ImageView(new Image(getClass().getResource(imagePath).toExternalForm()));
                            imageView.setFitWidth(20); // Set image width
                            imageView.setFitHeight(20); // Set image height

                            // Add the ImageView and Label to the HBox
                            taskContent.getChildren().addAll(imageView, taskLabel);
                        } else {
                            // Add the label only if the image path is null
                            taskContent.getChildren().add(taskLabel);
                        }
                    } catch (Exception e) {
                        System.err.println("Error loading image: " + e.getMessage());
                    }

                    // Add the HBox to the StackPane
                    taskCard.getChildren().add(taskContent);

                    // Add the StackPane to the tasks container
                    tasksContainer.getChildren().add(taskCard);

                    // Add click event to open the task view
                    taskCard.setOnMouseClicked(event -> openViewTaskScreen(task.getTitle(), task.getTaskID()));

                    taskIndex++;
                }
            }


            // Add tasks container to the list container
            listContainerBox.getChildren().add(tasksContainer);

            // Add an "Add Card" button
            Button addCardButton = new Button("+ Add a card");
            addCardButton.setId("addCardButton" + listIndex);
            addCardButton.getStyleClass().add("add-card-button");
            addCardButton.setOnAction(event -> {
                try {
                    handleCard(event,projectBoard.getBoardID(), listContainer.getListID()); // Call the handleCard method
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            listContainerBox.getChildren().add(addCardButton);

            listContainerBox.setPrefWidth(300); // Set the width to 200
            listContainerBox.setMinWidth(300);  // Prevent shrinking below 200
            listContainerBox.setMaxWidth(300);  // Prevent expanding beyond 200

            // Add the list container to the ScrollPane
            listScrollPane.setContent(listContainerBox);

            // Add the ScrollPane to the taskListsContainer
            taskListsContainer.getChildren().add(listScrollPane);

            listIndex++;
        }
    }


    private void handleAddTask(String listName) {
        System.out.println("Adding a task to list: " + listName);
        // Logic to handle adding a task dynamically
    }




    public void displayUserBoards() {
        // Clear any existing content in the side panel
        sidePanel.getChildren().clear();

        // Get all project boards from the dashboard
        List<ProjectBoard> projectBoards = Main.dashboard.getProjectBoards();

        if (projectBoards.isEmpty()) {
            yourBoardsLabel.setText("No Boards Found");
            System.out.println("No boards found for user: " + Main.user.getUsername());
        } else {
            yourBoardsLabel.setText("Your Boards");
            System.out.println("Boards found for user " + Main.user.getUsername() + ":");

            for (ProjectBoard board : projectBoards) {
                int boardID = board.getBoardID();
                String boardName = board.getName();

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

    // for viewing task
    private void openViewTaskScreen(String taskName,int taskID) {
        Main.dashboard.currentTaskID = taskID;
        try {
            // Load the View Task FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/synapse/fxml/viewTask.fxml"));
            Stage stage = new Stage();
            Main.dashboard.currentTaskID = taskID;
            stage.setScene(new Scene(loader.load()));

            // Pass the task data to the controller
            ViewTaskController controller = loader.getController();
//            controller.setTaskDetails(taskName);

            // Show the View Task screen
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // for adding new lists
    @FXML
    private void addList() {
        // Create a new window (Stage)
        Stage addListStage = new Stage();
        addListStage.setTitle("Add New List");

        // Create a VBox layout to hold the input form
        VBox vbox = new VBox(15); // Spacing between elements
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #F5F5F5; -fx-background-radius: 10px;");

        // Create a label and text field for list name input
        Label nameLabel = new Label("Enter the list name:");
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #40444B;");

        TextField listNameField = new TextField();
        listNameField.setPromptText("List Name");
        listNameField.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-background-color: #FFFFFF; -fx-border-radius: 5px;");


        // Create an "Add" button to confirm input
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            String listName = listNameField.getText();
            if (listName != null && !listName.trim().isEmpty()) {
                // Get the current board ID from Dashboard
                int currentBoardID = Main.dashboard.currentBoard;
                ProjectBoard board = Main.dashboard.findBoardByID(currentBoardID);
                if (currentBoardID != -1) {
                    boolean isAdded=board.addList(listName);
                    //boolean isAdded = dbUtils.addListToBoard(currentBoardID, listName);
                    if (isAdded) {
                        addListStage.close();
                        refreshProjectBoardUI(); // Refresh the UI to include the new list
                    }
                } else {
                    System.err.println("No board is currently open.");
                }


            } else {
                // Show an error if the name is empty
                Alert alert = new Alert(Alert.AlertType.ERROR, "List name cannot be empty.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        // Style the button
        addButton.setStyle("-fx-background-color: linear-gradient(to top, #007bff, #5865F2); -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-border-radius: 5px;");
        addButton.setPrefWidth(100);

        // Add components to the VBox layout
        vbox.getChildren().addAll(nameLabel, listNameField, addButton);

        // Set up the scene and stage
        Scene scene = new Scene(vbox, 300, 200);
        addListStage.setScene(scene);

        // Show the popup window
        addListStage.show();
    }


    // refresh method of tasks UI
    private void refreshSpecificList(int listID) {
        System.out.println("Refreshing the list with ID: " + listID);

        // Find the list container by ID
        ProjectBoard currentBoard = Main.dashboard.findBoardByID(Main.dashboard.currentBoard);
        if (currentBoard == null) {
            System.out.println("Current board not found.");
            return;
        }

        ListContainer listContainer = currentBoard.findListByID(listID);
        if (listContainer == null) {
            System.out.println("List not found in the current board.");
            return;
        }

        // Find the UI node by ID
        javafx.scene.Node node = taskListsContainer.lookup("#taskList" + listID);
        if (node == null) {
            System.out.println("UI node not found for list ID: " + listID);
            return;
        }

        VBox listContainerBox;
        if (node instanceof ScrollPane) {
            ScrollPane scrollPane = (ScrollPane) node;
            listContainerBox = (VBox) scrollPane.getContent();
        } else if (node instanceof VBox) {
            listContainerBox = (VBox) node;
        } else {
            System.out.println("Unexpected node type found for list ID: " + listID);
            return;
        }

        VBox tasksContainer = (VBox) listContainerBox.lookup("#taskList" + listID + "Cards");
        if (tasksContainer == null) {
            System.out.println("Tasks container not found for list ID: " + listID);
            return;
        }

        // Update tasks for this list
        tasksContainer.getChildren().clear(); // Clear old tasks
        int taskIndex = 1;
        for (Task task : listContainer.getTasks()) {
            StackPane taskCard = new StackPane();
            taskCard.setId("task" + taskIndex + "Card");
            taskCard.getStyleClass().add("task-card");

            Label taskLabel = new Label(task.getTitle());
            taskLabel.setId("task" + taskIndex + "Label");
            taskLabel.getStyleClass().add("task-labels");

            taskCard.getChildren().add(taskLabel);
            tasksContainer.getChildren().add(taskCard);
            taskCard.setOnMouseClicked(event -> openViewTaskScreen(task.getTitle(),task.getTaskID()));




            taskIndex++;
        }

        System.out.println("List with ID: " + listID + " successfully refreshed.");
    }


    public void giveFeedback(ActionEvent actionEvent) {
        Stage feedbackStage = new Stage();
        feedbackStage.setTitle("User Feedback");

        // Create a VBox layout to hold the form
                VBox vbox = new VBox(15); // Spacing between elements
                vbox.setStyle("""
            -fx-padding: 20;
            -fx-alignment: center;
            -fx-background-color: #F5F5F5;
            -fx-background-radius: 10px;
            -fx-border-color: #D0D0D0;
            -fx-border-width: 1px;
        """);

        // Create a label for the feedback header
                Label feedbackLabel = new Label("Submit Your Feedback");
                feedbackLabel.setStyle("""
            -fx-font-size: 18px;
            -fx-font-weight: bold;
            -fx-text-fill: #2C3E50;
        """);

        // Text area for user feedback input
                TextArea feedbackTextArea = new TextArea();
                feedbackTextArea.setPromptText("Enter your feedback here...");
                feedbackTextArea.setStyle("""
            -fx-font-size: 14px;
            -fx-padding: 10px;
            -fx-background-color: #FFFFFF;
            -fx-border-radius: 5px;
            -fx-border-color: #D0D0D0;
        """);

        // Dropdown (ComboBox) for feedback categories
                Label categoryLabel = new Label("Select Category:");
                categoryLabel.setStyle("""
            -fx-font-size: 14px;
            -fx-text-fill: #2C3E50;
        """);

                ComboBox<String> categoryDropdown = new ComboBox<>();
                categoryDropdown.getItems().addAll("Feature Request", "Bug Report", "General Feedback");
                categoryDropdown.setPromptText("Select category...");
                categoryDropdown.setStyle("""
            -fx-font-size: 14px;
            -fx-padding: 5px;
            -fx-background-color: #FFFFFF;
            -fx-border-radius: 5px;
            -fx-border-color: #D0D0D0;
        """);

        // Submit and cancel buttons
                Button submitButton = new Button("Submit");
                submitButton.setStyle("""
            -fx-font-size: 14px;
            -fx-background-color: linear-gradient( to top , #007bff, #5865F2);
            -fx-text-fill: white;
            -fx-padding: 10px;
            -fx-background-radius: 5px;
        """);
                submitButton.setOnAction(event -> {
                    String feedbackText = feedbackTextArea.getText().trim();
                    String category = categoryDropdown.getValue();

                    if (feedbackText.isEmpty() || category == null) {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Please complete all fields before submitting.");
                        alert.showAndWait();
                    } else {
                        // Handle feedback submission logic here
                        dbUtils.storeFeedback(Main.user.getUsername(), category, feedbackText);
                        System.out.println("Feedback submitted: " + feedbackText);
                        feedbackStage.close();
                    }
                });

                Button cancelButton = new Button("Cancel");
                cancelButton.setStyle("""
            -fx-font-size: 14px;
            -fx-background-color: linear-gradient( to top , #007bff, #5865F2);
            -fx-text-fill: white;
            -fx-padding: 10px;
            -fx-background-radius: 5px;
        """);
                cancelButton.setOnAction(event -> feedbackStage.close());

        // Layout for buttons
                HBox buttonBox = new HBox(10, submitButton, cancelButton);
                buttonBox.setStyle("""
            -fx-alignment: center;
        """);

        // Add elements to the VBox
                vbox.getChildren().addAll(feedbackLabel, feedbackTextArea, categoryLabel, categoryDropdown, buttonBox);

        // Create a Scene and display the stage
                Scene scene = new Scene(vbox, 400, 300);
                feedbackStage.setScene(scene);
                feedbackStage.show();


    }



}
