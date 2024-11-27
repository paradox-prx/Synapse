package com.example.synapse.models;

import com.example.synapse.Main;
import com.example.synapse.database.DatabaseUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private DatabaseUtils dbUtils;


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
        this.dbUtils = new DatabaseUtils();

    }
    public Task(){
        this.isComplete = false;
        this.dbUtils = new DatabaseUtils();


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

    public Task makeTask(String title, String description, LocalDate deadline, String assignedUser,
                         String priority, ArrayList<String> subTasks) throws Exception {
        try {
            // Debugging output
            System.out.println("Making task with values:");
            System.out.println("List ID: " + Main.dashboard.currentListID);
            System.out.println("Title: " + title);
            System.out.println("Description: " + description);
            System.out.println("Deadline: " + deadline);
            System.out.println("Assigned User: " + assignedUser);
            System.out.println("Priority: " + priority);

            // Save task to database and get TaskID
            int taskID = dbUtils.insertTask(Main.dashboard.currentListID, title, description, deadline, assignedUser, priority);

            // Debugging TaskID
            System.out.println("Task ID returned: " + taskID);

            dbUtils.assignTask(taskID, assignedUser);
            dbUtils.assignTask(taskID, Main.user.getUsername());

            // Save subtasks to the database
            for (String subTask : subTasks) {
                dbUtils.insertSubTask(taskID, subTask);
            }

            // Add the task to the project and list container
            return dbUtils.getTaskById(taskID);

        } catch (Exception e) {
            throw new Exception("Failed to create task: " + e.getMessage());
        }
    }
    }
