package com.example.synapse.models;

import javafx.beans.property.*;

import java.sql.Timestamp;

public class Feedback {

    private final IntegerProperty feedbackID;
    private final StringProperty username;
    private final StringProperty category;
    private final StringProperty details;
    private final StringProperty status;
    private final ObjectProperty<Timestamp> submittedAt;
    private final StringProperty actionTaken;

    // Constructor
    public Feedback(int feedbackID, String username, String category, String details, String status, Timestamp submittedAt) {
        this.feedbackID = new SimpleIntegerProperty(feedbackID);
        this.username = new SimpleStringProperty(username);
        this.category = new SimpleStringProperty(category);
        this.details = new SimpleStringProperty(details);
        this.status = new SimpleStringProperty(status);
        this.submittedAt = new SimpleObjectProperty<>(submittedAt);
        this.actionTaken = new SimpleStringProperty(null); // Optional: Can be set later
    }

    // Property methods for TableView
    public IntegerProperty feedbackIDProperty() {
        return feedbackID;
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public StringProperty detailsProperty() {
        return details;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public ObjectProperty<Timestamp> submittedAtProperty() {
        return submittedAt;
    }

    public StringProperty actionTakenProperty() {
        return actionTaken;
    }

    // Standard Getters and Setters (if needed)
    public int getFeedbackID() {
        return feedbackID.get();
    }

    public void setFeedbackID(int feedbackID) {
        this.feedbackID.set(feedbackID);
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public String getDetails() {
        return details.get();
    }

    public void setDetails(String details) {
        this.details.set(details);
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public Timestamp getSubmittedAt() {
        return submittedAt.get();
    }

    public void setSubmittedAt(Timestamp submittedAt) {
        this.submittedAt.set(submittedAt);
    }

    public String getActionTaken() {
        return actionTaken.get();
    }

    public void setActionTaken(String actionTaken) {
        this.actionTaken.set(actionTaken);
    }
}
