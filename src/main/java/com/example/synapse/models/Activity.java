package com.example.synapse.models;

// Inner class representing an ActivityLog entry
public class Activity{
    private String TaskOrSubTaskName;
    private String RelevantUser;
    private String Action;
    private String Timestamp;

    public Activity(String taskName, String user, String action, String timestamp) {
        this.TaskOrSubTaskName = taskName;
        this.RelevantUser = user;
        this.Action = action;
        this.Timestamp = timestamp;
    }

    public String getTaskOrSubTaskName() {
        return TaskOrSubTaskName;
    }

    public void setTaskOrSubTaskName(String taskOrSubTaskName) {
        this.TaskOrSubTaskName = taskOrSubTaskName;
    }

    public String getRelevantUser() {
        return RelevantUser;
    }

    public void setRelevantUser(String relevantUser) {
        RelevantUser = relevantUser;
    }

    public String getAction() {
        return Action;
    }

    public void setAction(String action) {
        Action = action;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }
}