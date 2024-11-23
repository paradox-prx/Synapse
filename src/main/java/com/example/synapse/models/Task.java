package com.example.synapse.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Task {
    private int taskID;
    private int boardID;
    private String title;
    private String description;
    private LocalDate deadline;
    private List<SubTask> subTasks; // Composition
    private List<String> comments;
    private User assignedUser; // Aggregation
    private String priority;
    private boolean isComplete;

    // Constructor
    public Task(int taskID,int boardID, String title, String description, LocalDate deadline, User assignedUser, String priority) {
        this.taskID = taskID;
        this.boardID=boardID;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.assignedUser = assignedUser;
        this.priority = priority;
        this.isComplete = false;
    }
    public Task(){
        this.isComplete = false;
    }

    // Methods
    public void addSubTask(SubTask subTask) {
        this.subTasks.add(subTask);
    }

    public void addComment(String comment) {
        this.comments.add(comment);
    }

    public void markComplete() {
        this.isComplete = true;
    }

    // Getters and Setters
    public int getTaskID() { return taskID; }
    public void setTaskID(int taskID) { this.taskID = taskID; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public List<SubTask> getSubTasks() { return subTasks; }
    public void setSubTasks(List<SubTask> subTasks) { this.subTasks = subTasks; }

    public List<String> getComments() { return comments; }
    public void setComments(List<String> comments) { this.comments = comments; }

    public User getAssignedUser() { return assignedUser; }
    public void setAssignedUser(User assignedUser) { this.assignedUser = assignedUser; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public boolean isComplete() { return isComplete; }
    public void setComplete(boolean complete) { isComplete = complete; }
}
