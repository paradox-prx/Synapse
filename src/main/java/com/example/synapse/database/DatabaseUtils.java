package com.example.synapse.database;

import com.example.synapse.models.ListContainer;
import com.example.synapse.models.ProjectBoard;
import com.example.synapse.models.Task;
import com.example.synapse.models.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class DatabaseUtils {

    private static final String DATABASE_URL = "jdbc:sqlite:synapse.db";
    private Connection conn;

    public DatabaseUtils() {

        conn = connect();
        enableWALMode();
    }
    public List<String> getAllUsernames() {
        List<String> usernames = new ArrayList<>();
        String sql = "SELECT Username FROM Users";

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
        String sql = "SELECT Username, Email FROM Users WHERE Role = 'Team Member' AND IsActive = 1";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("Username");
                String email = rs.getString("Email");
                teamMembers.add(username + " - " + email); // Add formatted username and email
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

    public int insertTask(int boardID, String title, String description, String deadline, String assignedUser, String priority) {
        String sql = "INSERT INTO Tasks (ListID, TaskName, Description, Priority, Deadline, IsCompleted, CreatedAt) " +
                "VALUES (?, ?, ?, ?, ?, 0, CURRENT_TIMESTAMP)";

        try (PreparedStatement pstmt = connect().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, boardID);
            pstmt.setString(2, title);
            pstmt.setString(3, description);
            pstmt.setString(4, priority);
            pstmt.setString(5, deadline);

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
        String sql = "SELECT t.TaskID, t.TaskName, t.Description, t.Priority, t.Deadline, t.IsCompleted, ta.Username " +
                "FROM Tasks t " +
                "LEFT JOIN TaskAssignments ta ON t.TaskID = ta.TaskID " +
                "WHERE t.ListID = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, listID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int taskID = rs.getInt("TaskID");
                String title = rs.getString("TaskName");
                String description = rs.getString("Description");
                String priority = rs.getString("Priority");
                LocalDate deadline = rs.getDate("Deadline") != null ? rs.getDate("Deadline").toLocalDate() : null;
                boolean isComplete = rs.getBoolean("IsCompleted");

                // Fetch the assigned user, if present
                User assignedUser = null;
                if (rs.getString("Username") != null) {
                    assignedUser = getUserByUsername(rs.getString("Username")); // Assuming this method exists
                }

                // Create a Task object
                Task task = new Task(taskID, listID, title, description, deadline, assignedUser, priority);
                task.setComplete(isComplete); // Set the completion status
                tasks.add(task);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching tasks by list ID: " + e.getMessage());
        }
        return tasks;
    }

    /*public List<String> getListsByBoardID(int boardID) {
        List<String> lists = new ArrayList<>();
        String sql = "SELECT ListName FROM BoardLists WHERE BoardID = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, boardID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                lists.add(rs.getString("ListName"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching lists: " + e.getMessage());
        }
        return lists;
    }

    public List<String> getTasksByListName(String listName) {
        List<String> tasks = new ArrayList<>();
        String sql = "SELECT TaskName FROM Tasks WHERE ListID = (SELECT ListID FROM BoardLists WHERE ListName = ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, listName);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                tasks.add(rs.getString("TaskName"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching tasks: " + e.getMessage());
        }
        return tasks;
    }

    public static List<String> getProjectBoardNamesByIDs(List<Integer> projectIDs) {
        List<String> projectBoardNames = new ArrayList<>();
        if (projectIDs.isEmpty()) {
            return projectBoardNames; // Return empty list if no IDs
        }
        // Create a dynamic IN clause based on the number of IDs
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < projectIDs.size(); i++) {
            placeholders.append("?");
            if (i < projectIDs.size() - 1) {
                placeholders.append(",");
            }
        }
        String sql = "SELECT BoardName FROM ProjectBoards WHERE BoardID IN (" + placeholders + ")";
        try (Connection conn = DatabaseUtils.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < projectIDs.size(); i++) {
                pstmt.setInt(i + 1, projectIDs.get(i));
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                projectBoardNames.add(rs.getString("BoardName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projectBoardNames;
    }

    public static List<Integer> getProjectIDsByUsername(String username) {
        List<Integer> projectIDs = new ArrayList<>();
        String sql = """
            SELECT DISTINCT pb.BoardID 
            FROM ProjectBoards pb
            JOIN BoardMembers bm ON pb.BoardID = bm.BoardID
            WHERE bm.Username = ?;
""";
        try (Connection conn = DatabaseUtils.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                projectIDs.add(rs.getInt("BoardID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projectIDs;
    }

    public List<String> getProjectBoards(String username) {
        String query = """
            SELECT DISTINCT pb.BoardName 
            FROM ProjectBoards pb
            JOIN BoardMembers bm ON pb.BoardID = bm.BoardID
            WHERE bm.Username = ?;
        """;

        List<String> boardNames = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    boardNames.add(rs.getString("BoardName"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching project boards for user: " + e.getMessage());
        }

        return boardNames;
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
*/
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


    // view task methods
    public String getTaskDescription() {
        // Example fetch task description
        return "This is a sample task description.";
    }

    public List<String> getSubtasks() {
        // Example fetch subtasks
        return Arrays.asList("Subtask 1", "Subtask 2", "Subtask 3");
    }

    public List<String> getComments() {
        // Example fetch comments
        return Arrays.asList("Comment 1", "Comment 2", "Comment 3");
    }

    public void addComment(String comment) {
        // Insert the new comment into the database
        System.out.println("New comment added: " + comment);
    }

    public void markSubtaskComplete(String subtask) {
        // Mark subtask as complete in DB
        System.out.println("Subtask marked complete: " + subtask);
    }

    public void markTaskAsComplete() {
        // Mark the entire task as complete
        System.out.println("Task marked as complete.");
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


}

