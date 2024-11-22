package com.example.synapse.models;

public class SubTask {
    private int subTaskID;
    private String description;
    private boolean isChecked;

    // Constructor
    public SubTask(int subTaskID, String description) {
        this.subTaskID = subTaskID;
        this.description = description;
        this.isChecked = false;
    }

    // Methods
    public void toggleChecked() {
        this.isChecked = !this.isChecked;
    }

    // Getters and Setters
    public int getSubTaskID() { return subTaskID; }
    public void setSubTaskID(int subTaskID) { this.subTaskID = subTaskID; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isChecked() { return isChecked; }
    public void setChecked(boolean checked) { isChecked = checked; }
}
