package com.example.synapse.models;

import com.example.synapse.database.DatabaseUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private String username;
    private String email;
    private String password;
    private String role; // e.g., Admin, Project Manager, Team Member
    private boolean isActive;

    DatabaseUtils db;


    // Constructor
    public User(String username, String email, String password, String role, boolean isActive) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isActive = isActive;

        // new db object
        db = new DatabaseUtils();
    }

    public User() {
        this.setUsername("");
        this.setEmail("");
        this.setPassword("");
        Boolean check = this.storeUserData();
    }

    // To create new user
    public Boolean createUser(String userName, String email, String password) {
        this.setUsername(userName);
        this.setEmail(email);
        this.setPassword(password);
        Boolean check = this.storeUserData();
        return check;
    }
    // To store user data in the database
    public Boolean storeUserData() {
        Boolean check = db.storeUserData(username, email, password);
        return check;
    }

    // Validate login details
    public Boolean validateCredentials(String usernameOrEmail, String password) {
        // Check the user's credentials in the database
        ResultSet resultSet = db.checkUserData(usernameOrEmail, password);

        try {
            if (resultSet != null && resultSet.next()) {
                // Populate User object attributes with data from the database
                this.setUsername(resultSet.getString("Username"));
                this.setEmail(resultSet.getString("Email"));
                this.setPassword(resultSet.getString("Password"));
                this.setRole(resultSet.getString("Role"));
                this.setActive(resultSet.getBoolean("isActive"));

                return true; // User validated successfully
            } else {
                return false; // User not found or invalid credentials
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log exception for debugging
            return false; // Validation failed due to an error
        }
    }


    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
