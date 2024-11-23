package com.example.synapse.models;

import java.util.ArrayList;
import java.util.List;

public class ListContainer {
    private int listID;
    private String name;
    private List<Task> tasks; // Composition

    // Constructor
    public ListContainer(int listID, String name) {
        this.listID = listID;
        this.name = name;
        tasks=new ArrayList<>();
    }

    // Methods
    public void addTask(Task task) {
        this.tasks.add(task);
    }

    // Getters and Setters
    public int getListID() { return listID; }
    public void setListID(int listID) { this.listID = listID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Task> getTasks() { return tasks; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }
}
