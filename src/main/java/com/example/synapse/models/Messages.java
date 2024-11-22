package com.example.synapse.models;

import java.time.LocalDateTime;

public class Messages {
    private int messageID;
    private int boardID;
    private String username;
    private String messageText;
    private LocalDateTime sentAt;

    // Constructor
    public Messages(int messageID, int boardID, String username, String messageText, LocalDateTime sentAt) {
        this.messageID = messageID;
        this.boardID = boardID;
        this.username = username;
        this.messageText = messageText;
        this.sentAt = sentAt;
    }

    // Getters and Setters
    public int getMessageID() { return messageID; }
    public void setMessageID(int messageID) { this.messageID = messageID; }

    public int getBoardID() { return boardID; }
    public void setBoardID(int boardID) { this.boardID = boardID; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getMessageText() { return messageText; }
    public void setMessageText(String messageText) { this.messageText = messageText; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
}
