package com.example.synapse.database;

import com.example.synapse.Main;
import com.example.synapse.models.ListContainer;
import com.example.synapse.models.ProjectBoard;
import com.example.synapse.models.Task;
import com.example.synapse.models.User;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DatabaseUtils {

    private static final String DATABASE_URL = "jdbc:sqlite:synapse.db";
    private Connection conn;

    public DatabaseUtils() {

        conn = connect();
        enableWALMode();
    }

    public void updateNewUser(String username, String email, String role, boolean isActive){
        String sql = "UPDATE Users SET Email = ?, Role = ?, IsActive = ?, UpdatedAt = CURRENT_TIMESTAMP WHERE Username = ?";
        try (var connection = DatabaseUtils.connect();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, role);
            preparedStatement.setBoolean(3, isActive);
            preparedStatement.setString(4, username);

            int affected = preparedStatement.executeUpdate();
            if (affected == 0) {
                showAlert(Alert.AlertType.WARNING, "Warning", "No user was updated. Please try again.");

            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update user.");

        }
    }

    public void insertNewUser(String username, String email, String password, String role, boolean isActive){
        // SQL to insert a new user
        String sql = "INSERT INTO Users (Username, Email, Password, Role, IsActive, CreatedAt) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (var connection = DatabaseUtils.connect();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, role);
            preparedStatement.setBoolean(5, isActive);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                showAlert(Alert.AlertType.WARNING, "Warning", "No user was added. Please try again.");

            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add user.");

        }

    }
    public void deactivateUser(User user) {
        String sql = "UPDATE Users SET IsActive = 0 WHERE Username = ?";
        try (var connection = DatabaseUtils.connect();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getUsername());
            int affected = preparedStatement.executeUpdate();

            if (affected == 0) {
                showAlert(Alert.AlertType.WARNING, "Warning", "No user was deactivated. Please try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to deactivate user.");
        }
    }

    // Get all users
    public List<User> getAllUsers() {
        String sql = "SELECT Username, Email, Role, IsActive FROM Users";
        List<User> users = new ArrayList<>();

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User(
                        rs.getString("Username"),
                        rs.getString("Email"),
                        "", // Password not needed for display
                        rs.getString("Role"),
                        rs.getBoolean("IsActive")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean addListToBoard(int boardID, String listName) {
        String query = "INSERT INTO BoardLists (BoardID, ListName, CreatedAt) VALUES (?, ?, CURRENT_TIMESTAMP)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, boardID);
            pstmt.setString(2, listName);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<String> getAllUsernames(int boardID) {
        List<String> usernames = new ArrayList<>();
        String sql = "SELECT Username FROM BoardMembers WHERE BoardID="+boardID;
        System.out.println("Checking for BoardID: "+boardID);

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                usernames.add(rs.getString("Username"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching usernames: " + e.getMessage());
        }

        return usernames;
    }

    // Method to fetch active Team Members
    public List<String> getActiveTeamMembers() {
        List<String> teamMembers = new ArrayList<>();
        String sql = "SELECT Username FROM Users WHERE Role = 'Team Member' AND IsActive = 1";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("Username");
                teamMembers.add(username);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching team members: " + e.getMessage());
            e.printStackTrace();
        }

        return teamMembers;
    }
    // Method to store User data
    public boolean storeUserData(String userName, String email, String password) {
        String sql = "INSERT INTO Users (Username, Email, Password, Role, isActive, CreatedAt, UpdatedAt) " +
                "VALUES (?, ?, ?, 'Team Member', 1, CURRENT_TIMESTAMP, null)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userName);
            pstmt.setString(2, email);
            pstmt.setString(3, password);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("User data successfully stored.");
                return true;
            } else {
                System.out.println("Failed to store user data.");
            }
        } catch (SQLException e) {
            System.out.println("Error while storing user data: " + e.getMessage());
            e.printStackTrace(); // Add stack trace for debugging
        }
        return false;
    }
    public User getUserByUsername(String username) {
        String sql = "SELECT Username, Email, Password, Role, IsActive FROM Users WHERE Username = ?";
        User user = null;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User(
                        rs.getString("Username"),
                        rs.getString("Email"),
                        rs.getString("Password"), // Provide the password (ensure security in real apps)
                        rs.getString("Role"),
                        rs.getBoolean("IsActive")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user by username: " + e.getMessage());
        }
        return user;
    }


    public List<ListContainer> getListsByBoardID(int boardID) {
        List<ListContainer> lists = new ArrayList<>();
        String sql = "SELECT ListID, ListName, CreatedAt FROM BoardLists WHERE BoardID = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, boardID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ListContainer list = new ListContainer(
                        rs.getInt("ListID"),
                        rs.getString("ListName")
                );
                lists.add(list);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching lists: " + e.getMessage());
        }
        return lists;
    }

    public int insertTask(int boardID, String title, String description, LocalDate deadlineDate, String assignedUser, String priority) {
        String sql = "INSERT INTO Tasks (ListID, TaskName, Description, Priority, Deadline, IsCompleted) " +
                "VALUES (?, ?, ?, ?, ?, 0)";

        try (PreparedStatement pstmt = connect().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Convert LocalDate to java.sql.Timestamp
            Timestamp deadlineTimestamp = convertToSqlTimestamp(deadlineDate);

            pstmt.setInt(1, boardID);
            pstmt.setString(2, title);
            pstmt.setString(3, description);
            pstmt.setString(4, priority);
            pstmt.setTimestamp(5, deadlineTimestamp); // Use java.sql.Timestamp

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                // Retrieve generated TaskID
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Return the TaskID
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Failed to insert task into database.");
    }

    // Helper method to convert LocalDate to java.sql.Timestamp
    private Timestamp convertToSqlTimestamp(LocalDate deadlineDate) {
        // Set time to 23:59:59 (end of the day)
        LocalDateTime deadlineDateTime = deadlineDate.atTime(23, 59, 59);
        return Timestamp.valueOf(deadlineDateTime);
    }
    public List<ProjectBoard> getProjectsByUsername(String username) {
        List<ProjectBoard> projectBoards = new ArrayList<>();
        String sql = "SELECT pb.BoardID, pb.BoardName, pb.Description, pb.CreatedBy, pb.CreatedAt " +
                "FROM ProjectBoards pb " +
                "JOIN BoardMembers bm ON pb.BoardID = bm.BoardID " +
                "WHERE bm.Username = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // Fetch User object for CreatedBy
                String createdByUsername = rs.getString("CreatedBy");
                User projectManager = getUserByUsername(createdByUsername);

                // Construct the ProjectBoard object
                ProjectBoard projectBoard = new ProjectBoard(
                        rs.getInt("BoardID"),
                        rs.getString("BoardName"),
                        rs.getString("Description"),
                        projectManager
                );
                projectBoards.add(projectBoard);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching projects by username: " + e.getMessage());
        }
        return projectBoards;
    }

    public List<Task> getTasksByListID(int listID) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT t.TaskID, t.TaskName, t.Description, t.Priority, t.Deadline, t.IsCompleted " +
                "FROM Tasks t WHERE t.ListID = ?";
//                "LEFT JOIN TaskAssignments ta ON t.TaskID = ta.TaskID " +


        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, listID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int taskID = rs.getInt("TaskID");
                String title = rs.getString("TaskName");
                String description = rs.getString("Description");
                String priority = rs.getString("Priority");
                LocalDate deadline = rs.getDate("Deadline") != null ? rs.getDate("Deadline").toLocalDate() : null;
                boolean isComplete = rs.getBoolean("IsCompleted");


                Task task = new Task(taskID, listID, title, description, deadline, Main.user, priority);
                task.setComplete(isComplete); // Set the completion status
                tasks.add(task);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching tasks by list ID: " + e.getMessage());
        }
        return tasks;
    }

    // Method to check user trying to log in
    public ResultSet checkUserData(String usernameOrEmail, String password) {
        String query = "SELECT * FROM Users WHERE (Username = ? OR Email = ?) AND Password = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, usernameOrEmail);
            pstmt.setString(2, usernameOrEmail);
            pstmt.setString(3, password);

            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace(); // Log exception
            return null; // Return null in case of an error
        }
    }



    // Method to connect to SQLite
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DATABASE_URL);
            System.out.println("Connection to SQLite has been established: " + DATABASE_URL);
        } catch (SQLException e) {
            System.out.println("Error connecting to SQLite: " + e.getMessage());
            e.printStackTrace(); // Add stack trace for debugging
        }
        return conn;
    }



    public static void retrieveData() {

    }




    // Function to fetch subtasks for a specific TaskID
    public List<String> getSubtasks(int taskID) {
        List<String> subtasks = new ArrayList<>();
        String sql = "SELECT SubTaskName FROM SubTasks WHERE TaskID = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taskID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                subtasks.add(rs.getString("SubTaskName"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching subtasks: " + e.getMessage());
        }
        return subtasks;
    }

    // Function to fetch comments for a specific TaskID
    public List<String> getComments(int taskID) {
        List<String> comments = new ArrayList<>();
        String sql = "SELECT CommentText FROM Comments WHERE TaskID = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taskID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                comments.add(rs.getString("CommentText"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching comments: " + e.getMessage());
        }
        return comments;
    }

    // Function to add a new comment to a task
    public void addComment(int taskID, String comment) {
        String sql = "INSERT INTO Comments (TaskID, Username, CommentText) VALUES (?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taskID);
            pstmt.setString(2, Main.user.getUsername()); // Replace with the logged-in username
            pstmt.setString(3, comment);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("New comment added successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding comment: " + e.getMessage());
        }
    }

    // Function to mark a subtask as complete
    public void markSubtaskComplete(int taskID, String subtask) {
        String sql = "UPDATE SubTasks SET IsCompleted = 1 WHERE TaskID = ? AND SubTaskName = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taskID);
            pstmt.setString(2, subtask);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Subtask marked as complete.");
            }
        } catch (SQLException e) {
            System.out.println("Error marking subtask as complete: " + e.getMessage());
        }
    }

    // Function to mark an entire task as complete
    public void markTaskAsComplete(int taskID) {
        String sql = "UPDATE Tasks SET IsCompleted = 1 WHERE TaskID = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taskID);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Task marked as complete.");
            }
        } catch (SQLException e) {
            System.out.println("Error marking task as complete: " + e.getMessage());
        }
    }

    // creating new project board
    public int createProjectBoard(String boardName, String description, String createdBy) {
        String sql = "INSERT INTO ProjectBoards (BoardName, Description, CreatedBy, CreatedAt) " +
                "VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, boardName);
            pstmt.setString(2, description);
            pstmt.setString(3, createdBy);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                // Retrieve the generated BoardID
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    String query = "INSERT INTO BoardMembers(BoardID, Username) VALUES (?, ?)";
                    PreparedStatement pstmt2 = conn.prepareStatement(query);
                    pstmt2.setString(1, String.valueOf(generatedKeys.getInt(1)));
                    pstmt2.setString(2, createdBy);
                    pstmt2.executeUpdate();
                    return generatedKeys.getInt(1); // Return the generated BoardID
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while creating project board: " + e.getMessage());
            e.printStackTrace();
        }
        return -1; // Return -1 if the creation fails
    }

    // mutiple connections
    public void enableWALMode() {
        String sql = "PRAGMA journal_mode=WAL;";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("WAL mode enabled.");
        } catch (SQLException e) {
            System.err.println("Error enabling WAL mode: " + e.getMessage());
        }
    }


    public boolean addUserToBoard(int newBoardID, String member) {
        String query = "INSERT INTO BoardMembers (BoardID, Username) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, String.valueOf(newBoardID));
            pstmt.setString(2, member);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows < 0) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Could not add Users to Board " + e.getMessage());
        }
        return false;
    }

    public String getListNameByListID(int currentListID) {
        String query = "SELECT ListName FROM BoardLists WHERE ListID = ?";
        String listName = null;

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, currentListID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    listName = rs.getString("ListName");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching list name by ListID: " + e.getMessage());
        }

        return listName;
    }

    public int getListIDByListName(String listName) {
        String query = "SELECT ListID FROM BoardLists WHERE ListName = ?";
        int listID = 0;

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, listName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    listID = Integer.parseInt(rs.getString("ListID"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching list name by ListID: " + e.getMessage());
        }

        return listID;
    }


    // tp get tasks using task ID
    public Task getTaskById(int taskId) {
        String query = "SELECT * FROM tasks WHERE TaskID = ?";
        Task task = null;

        try (PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, taskId);  // Set the TaskID parameter in the query

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Extract basic task information from the result set
                    String title = rs.getString("TaskName");
                    String description = rs.getString("Description");
                    LocalDate deadline = rs.getDate("Deadline").toLocalDate();
                    String priority = rs.getString("Priority");

                    // Create Task object with fetched data
                    task = new Task(taskId, Main.dashboard.currentBoard, title, description, deadline, Main.user, priority);

                    // Fetch and set subTasks (assuming SubTask objects exist)
//                    List<SubTask> subTasks = getSubTasksForTask(taskId);
//                    task.setSubTasks(subTasks);
//
//                    // Fetch and set comments (assuming Comments are simple Strings in the database)
//                    List<String> comments = getCommentsForTask(taskId);
//                    task.setComments(comments);

                    // Fetch and set complete status (if any)
                    task.setComplete(rs.getBoolean("IsCompleted"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions properly
        }

        return task;
    }



    public String getTaskTitle(int taskID) {
        String sql = "SELECT TaskName FROM Tasks WHERE TaskID = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taskID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("TaskName");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching task title: " + e.getMessage());
        }
        return null;
    }

    // Function to get the task description by TaskID
    public String getTaskDescription(int taskID) {
        String sql = "SELECT Description FROM Tasks WHERE TaskID = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taskID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Description");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching task description: " + e.getMessage());
        }
        return null;
    }

    public LocalDateTime fetchTaskDeadline(int taskID) {
        String sql = "SELECT Deadline FROM Tasks WHERE TaskID = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taskID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                long timestamp = rs.getLong("Deadline"); // Read as long
                // Convert UNIX timestamp (milliseconds) to LocalDateTime
                return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
            }
        } catch (SQLException e) {
            System.out.println("Error fetching deadline: " + e.getMessage());
        }
        return null; // Return null if no deadline is found
    }
    // Function to get the task deadline by TaskID as LocalDateTime
    public LocalDateTime getTaskDeadline(int taskID) {
        String sql = "SELECT Deadline FROM Tasks WHERE TaskID = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taskID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                long timestamp = rs.getLong("Deadline"); // Retrieve UNIX timestamp in milliseconds
                // Convert to LocalDateTime
                return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
            }
        } catch (SQLException e) {
            System.out.println("Error fetching task deadline: " + e.getMessage());
        }
        return null; // Return null if no deadline is found
    }

    // Function to get the task priority by TaskID
    public String getTaskPriority(int taskID) {
        String sql = "SELECT Priority FROM Tasks WHERE TaskID = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taskID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Priority");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching task priority: " + e.getMessage());
        }
        return null;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

}

