package com.example.synapse.models;

import java.time.LocalDateTime;

public class Activity {
    private int activityID;
    private String description;
    private LocalDateTime timestamp;
    private String username;
    private String entity; // e.g., Task, Board, User

    // Constructor
    public Activity(int activityID, String description, LocalDateTime timestamp, String username, String entity) {
        this.activityID = activityID;
        this.description = description;
        this.timestamp = timestamp;
        this.username = username;
        this.entity = entity;
    }

    // Methods
    public void applyFilter(String filterType) {
        // Apply filtering logic based on filterType
    }

    public void sortLogs(String sortBy) {
        // Apply sorting logic based on the sortBy parameter
    }

    // Getters and Setters
    public int getActivityID() { return activityID; }
    public void setActivityID(int activityID) { this.activityID = activityID; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEntity() { return entity; }
    public void setEntity(String entity) { this.entity = entity; }
}
