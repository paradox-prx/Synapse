package com.example.synapse.models;

public class Report {
    private int reportID;
    private int boardID;
    private String dateRange;

    // Constructor
    public Report(int reportID, int boardID, String dateRange) {
        this.reportID = reportID;
        this.boardID = boardID;
        this.dateRange = dateRange;
    }

    // Getters and Setters
    public int getReportID() { return reportID; }
    public void setReportID(int reportID) { this.reportID = reportID; }

    public int getBoardID() { return boardID; }
    public void setBoardID(int boardID) { this.boardID = boardID; }

    public String getDateRange() { return dateRange; }
    public void setDateRange(String dateRange) { this.dateRange = dateRange; }
}
