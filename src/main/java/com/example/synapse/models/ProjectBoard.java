package com.example.synapse.models;

import java.util.ArrayList;
import java.util.List;

public class ProjectBoard {
    private int boardID;
    private String boardName;
    private String description;
    private List<ListContainer> lists; // Composition
    private List<Messages> messages; // Message List (Aggregation)
    private Report report; // Object of Report (Aggregation)

    // Constructor
    public ProjectBoard(int boardID, String boardName, String description) {
        this.boardID = boardID;
        this.boardName = boardName;
        this.description = description;
        this.lists = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    // Methods
    public void sendMessage(String username, String messageText) {
        // Logic to send a message
        Messages message = new Messages(0, boardID, username, messageText, null); // SentAt handled by DB
        messages.add(message);
        System.out.println("Message sent: " + messageText);
    }

    public List<Messages> fetchChat() {
        // Logic to fetch messages for the board
        return messages;
    }

    public void saveBoardData() {
        // Save the board data to the database
    }

    public void fetchBoardData() {
        // Fetch the board data from the database
    }

    // Getters and Setters
    public int getBoardID() { return boardID; }
    public void setBoardID(int boardID) { this.boardID = boardID; }

    public String getBoardName() { return boardName; }
    public void setBoardName(String boardName) { this.boardName = boardName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<ListContainer> getLists() { return lists; }
    public void setLists(List<ListContainer> lists) { this.lists = lists; }

    public List<Messages> getMessages() { return messages; }
    public void setMessages(List<Messages> messages) { this.messages = messages; }

    public Report getReport() { return report; }
    public void setReport(Report report) { this.report = report; }
}
