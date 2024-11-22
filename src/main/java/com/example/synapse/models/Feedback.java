package com.example.synapse.models;

public class Feedback {
    private int feedbackID;
    private String username;
    private String category;
    private String details;
    private String status; // e.g., New, In Progress, Resolved

    // Constructor
    public Feedback(int feedbackID, String username, String category, String details, String status) {
        this.feedbackID = feedbackID;
        this.username = username;
        this.category = category;
        this.details = details;
        this.status = status;
    }

    // Methods
    public void fetchFeedback() {
        // Logic to retrieve feedback from the database
    }

    public void giveFeedback(String category, String details) {
        // Logic to save user feedback
    }

    public void filterFeedback(String filterBy) {
        // Logic to filter feedback based on criteria
    }

    public void getFeedbackDetails(int feedbackID) {
        // Logic to retrieve details for specific feedback
    }

    public void updateFeedback(int feedbackID, String status) {
        // Logic to update feedback status
    }

    // Getters and Setters
    public int getFeedbackID() { return feedbackID; }
    public void setFeedbackID(int feedbackID) { this.feedbackID = feedbackID; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
