package com.example.synapse.database;

import java.sql.*;

public class DatabaseUtils {

    private static final String DATABASE_URL = "jdbc:sqlite:synapse.db";
    private Connection conn;

    public DatabaseUtils() {
        conn = connect();
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
