//package com.example.synapse.database;
//
//import java.sql.*;
//
//public class DatabaseUtils {
//
//    private static final String DATABASE_URL = "jdbc:sqlite:synapse.db";
//
//    // Method to connect to SQLite
//    public static Connection connect() {
//        Connection conn = null;
//        try {
//            conn = DriverManager.getConnection(DATABASE_URL);
//            System.out.println("Connection to SQLite has been established: " + DATABASE_URL);
//        } catch (SQLException e) {
//            System.out.println("Error connecting to SQLite: " + e.getMessage());
//            e.printStackTrace(); // Add stack trace for debugging
//        }
//        return conn;
//    }
//
//
//    // Method to create a sample table
//    public static void createTable() {
//        /*String createTableSQL = "CREATE TABLE IF NOT EXISTS ahmedfasseh3 ("
//                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + "title TEXT NOT NULL, "
//                + "description TEXT, "
//                + "status TEXT DEFAULT 'Pending');";*/
//
//       /* try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
//            stmt.execute(createTableSQL);
//            System.out.println("Table 'ahmedfasseh3' has been created (if it didn't exist).");
//        } catch (SQLException e) {
//            System.out.println("Error creating table: " + e.getMessage());
//        }*/
//    }
//    public static void retrieveData() {
//
//        /*String query = "SELECT id, title, description, status FROM tasks;";
//
//
//        try (Connection conn = connect();
//             Statement stmt = conn.createStatement();
//             ResultSet resultSet = stmt.executeQuery(query)) {
//
//            // Print column headers
//            System.out.printf("%-5s %-20s %-50s %-10s%n", "ID", "Title", "Description", "Status");
//
//            // Iterate over the result set
//            while (resultSet.next()) {
//                int id = resultSet.getInt("id");
//                String title = resultSet.getString("title");
//                String description = resultSet.getString("description");
//                String status = resultSet.getString("status");
//
//                // Print each row
//                System.out.printf("%-5d %-20s %-50s %-10s%n", id, title, description, status);
//            }
//        } catch (SQLException e) {
//            System.out.println("Error retrieving data: " + e.getMessage());
//        }*/
//    }
//
//
//    // Method to insert sample data
//    public static void insertSampleData() {
//        /*String insertDataSQL = "INSERT INTO ahmedfasseh3 (title, description, status) VALUES "
//                + "('Task 3', 'Description for task 1', 'Pending'), "
//                + "('Task 4', 'Description for task 2', 'Completed');";
//*/
//        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
//            stmt.executeUpdate(insertDataSQL);
//            System.out.println("Sample data has been inserted into 'ahmedfasseh'.");
//        } catch (SQLException e) {
//            System.out.println("Error inserting data: " + e.getMessage());
//        }
//    }
//
//}
