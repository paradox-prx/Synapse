package com.example.synapse.models;

import com.example.synapse.database.DatabaseUtils;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class ProjectBoard {
    private int boardID;
    private String name;
    private String description;
    private User projectManager; // Aggregation: Project manager is associated with the board
    private List<ListContainer> lists; // Composition: Project board contains multiple lists
    private List<User> boardUsers; // Aggregation: Users associated with the board
    private List<Messages> messages; // Aggregation: Messages associated with the board
    private Report report; // Aggregation: A report object associated with the board
    DatabaseUtils db = new DatabaseUtils();

    // Para Constructor
    public ProjectBoard(int boardID, String name, String description, User projectManager) {
        this.boardID = boardID;
        this.name = name;
        this.description = description;
        this.projectManager = projectManager;
        this.lists = new ArrayList<>();
        this.boardUsers = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    // default
    public ProjectBoard() {
        this.boardID = -1;
        this.name = "";
        this.description = "";
        this.projectManager = null;
        this.lists = new ArrayList<>();
        this.boardUsers = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    // Methods
    // add new list to board
    public void addList(ListContainer list) {
        this.lists.add(list);
    }

    // add new user to board
    public void addUser(User user) {
        this.boardUsers.add(user);
    }


    public void sendMessage(String username, String messageText) {
        Messages message = new Messages(0, boardID, username, messageText, null); // SentAt is handled by DB
        this.messages.add(message);
        System.out.println("Message sent: " + messageText);
    }

    public List<Messages> fetchChat() {
        return messages;
    }


    public void saveBoardData() {
        // Database logic to save board data
    }

    /**
     * Fetches project board data from the database.
     */
    public void fetchBoardData() {
        // Database logic to fetch board data
    }

    // Getters and Setters
    public int getBoardID() {
        return boardID;
    }

    public Boolean createBoard(String boardName, String description, User projectManager) {
        this.name = boardName;
        this.description = description;
        this.projectManager = projectManager;
        this.boardID = db.saveBoardData(name, description, projectManager.getUsername());

        Boolean check = false;
        if (boardID > 0) {
            check = true;
            System.out.println("Project board created with ID: " + this.boardID);
        } else {
            System.out.println("Project board could not be created");
        }

        return check;
    }

    public void addUsers(ObservableList<String> selectedMembers) {
        for (String member : selectedMembers) {
            boolean success = db.addUserToBoard(boardID, member);
            if (success) {
                System.out.println("Added user " + member + " to board ID: " + boardID);
            } else {
                System.out.println("Failed to add user " + member);
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(User projectManager) {
        this.projectManager = projectManager;
    }

    public List<ListContainer> getLists() {
        return lists;
    }

    public void setLists(List<ListContainer> lists) {
        this.lists = lists;
    }

    public List<User> getBoardUsers() {
        return boardUsers;
    }

    public void setBoardUsers(List<User> boardUsers) {
        this.boardUsers = boardUsers;
    }

    public List<Messages> getMessages() {
        return messages;
    }

    public void setMessages(List<Messages> messages) {
        this.messages = messages;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public ListContainer findListByID(int listID) {
        for (ListContainer list : lists) {
            if (list.getListID() == listID) {
                return list;
            }
        }
        return null;
    }

}
