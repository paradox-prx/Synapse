package com.example.synapse.models;

import com.example.synapse.database.DatabaseUtils;
import com.example.synapse.models.ListContainer;
import com.example.synapse.models.ProjectBoard;
import com.example.synapse.models.Task;
import com.example.synapse.models.User;
import com.example.synapse.Main;
import java.util.ArrayList;
import java.util.List;

public class Dashboard {

    private List<ProjectBoard> projectBoards; // List of project boards to display
    private User user;
    private DatabaseUtils dbUtils; // Database utility instance

    // Constructor
    public Dashboard(User user, DatabaseUtils dbUtils) {
        this.user = user;
        this.dbUtils = dbUtils;
        this.projectBoards = new ArrayList<>();
    }

    // Getter for the list of project boards
    public List<ProjectBoard> getProjectBoards() {
        return projectBoards;
    }

    // Method to populate the dashboard with project boards and their lists/tasks
    public void populateDashboard() {
        // Get the project boards the user is a part of
        List<ProjectBoard> userProjectBoards = dbUtils.getProjectsByUsername(user.getUsername());

        for (ProjectBoard projectBoard : userProjectBoards) {
            // For each project board, fetch its list containers
            List<ListContainer> listContainers = dbUtils.getListsByBoardID(projectBoard.getBoardID());

            // For each list container, fetch its tasks
            for (ListContainer listContainer : listContainers) {
                List<Task> tasks = dbUtils.getTasksByListID(listContainer.getListID());
                listContainer.setTasks(tasks); // Set the tasks in the list container
            }

            // Set the list containers in the project board
            projectBoard.setLists(listContainers);

            // Add the project board to the dashboard
            this.projectBoards.add(projectBoard);
        }
    }

    // Utility to find a board by its ID
    public ProjectBoard findBoardByID(int boardID) {
        for (ProjectBoard board : projectBoards) {
            if (board.getBoardID() == boardID) {
                return board;
            }
        }
        return null; // Return null if no board matches the given ID
    }

    // adding project boards into dashboard
    public void addProjectBoard(ProjectBoard projectBoard) {

    }


}
