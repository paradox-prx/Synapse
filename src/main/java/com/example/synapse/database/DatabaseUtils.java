package com.example.synapse.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class DatabaseUtils {

    private static final String DATABASE_URL = "jdbc:sqlite:synapse.db";
    private Connection conn;

    public DatabaseUtils() {
        conn = connect();
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


    public List<String> getListsByBoardID(int boardID) {
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



}
