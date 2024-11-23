package com.example.synapse.models;

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

    // Constructor
    public ProjectBoard(int boardID, String name, String description, User projectManager) {
        this.boardID = boardID;
        this.name = name;
        this.description = description;
        this.projectManager = projectManager;
        this.lists = new ArrayList<>();
        this.boardUsers = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    // Methods

    /**
     * Add a new list to the project board.
     *
     * @param list The list to be added.
     */
    public void addList(ListContainer list) {
        this.lists.add(list);
    }

    /**
     * Add a new user to the project board.
     *
     * @param user The user to be added.
     */
    public void addUser(User user) {
        this.boardUsers.add(user);
    }

    /**
     * Sends a message and associates it with this project board.
     *
     * @param username    The username of the sender.
     * @param messageText The content of the message.
     */
    public void sendMessage(String username, String messageText) {
        Messages message = new Messages(0, boardID, username, messageText, null); // SentAt is handled by DB
        this.messages.add(message);
        System.out.println("Message sent: " + messageText);
    }

    /**
     * Fetches the chat history (messages) associated with this project board.
     *
     * @return List of messages.
     */
    public List<Messages> fetchChat() {
        return messages;
    }

    /**
     * Saves project board data to the database.
     */
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

    public void setBoardID(int boardID) {
        this.boardID = boardID;
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
}
