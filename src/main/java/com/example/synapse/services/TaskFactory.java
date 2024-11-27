package com.example.synapse.services;

import com.example.synapse.Main;
import com.example.synapse.database.DatabaseUtils;
import com.example.synapse.models.Task;

import java.time.LocalDate;
import java.util.ArrayList;

public class TaskFactory {
    private DatabaseUtils dbUtils;
    public TaskFactory() {
        this.dbUtils = new DatabaseUtils();
    }

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
