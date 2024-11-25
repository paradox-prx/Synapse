package com.example.synapse.controllers;

import com.example.synapse.models.Activity;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

import com.example.synapse.database.DatabaseUtils;

public class ActivityLog {

    public DatabaseUtils db = new DatabaseUtils();

    @FXML
    private ComboBox<String> userComboBox;

    @FXML
    private ComboBox<String> taskComboBox;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TableView<Activity> activityLogTable;

    @FXML
    private TableColumn<Activity, String> taskNameColumn;

    @FXML
    private TableColumn<Activity, String> userColumn;

    @FXML
    private TableColumn<Activity, String> actionColumn;

    @FXML
    private TableColumn<Activity, String> timestampColumn;

    @FXML
    private Label errorMessageLabel;

    @FXML
    public void initialize() {
        // Initialize table columns
        taskNameColumn.setCellValueFactory(new PropertyValueFactory<>("TaskOrSubTaskName"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("RelevantUser"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("Action"));
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("Timestamp"));

        // Populate filters
        populateFilters();

        // Load initial activity log data
        loadLogTable();
    }

    private void loadLogTable() {
        try {
            // Clear any previous error message
            errorMessageLabel.setText("");

            // Fetch all activity log data (without filters)
            List<Activity> logEntries = db.getAllActivityLog();

            // Populate the table view with the data
            activityLogTable.setItems(FXCollections.observableArrayList(logEntries));
        } catch (Exception e) {
            errorMessageLabel.setText("Error loading activity log: " + e.getMessage());
        }
    }


    private void populateFilters() {
        try {
            // Populate userComboBox
            List<String> users = db.getUsers();
            userComboBox.setItems(FXCollections.observableArrayList(users));

            // Populate taskComboBox
            List<String> tasks = db.getTasks();
            taskComboBox.setItems(FXCollections.observableArrayList(tasks));

        } catch (Exception e) {
            errorMessageLabel.setText("Error populating filters: " + e.getMessage());
        }
    }

    private void loadActivityLog(String user, String task, String startDate, String endDate) {
        try {
            // Convert LocalDate to formatted String (if not null)
            String start = (startDate != null) ? startDate + " 00:00:00" : null;
            String end = (endDate != null) ? endDate + " 23:59:59" : null;

            // Fetch activity log data
            List<Activity> logEntries = DatabaseUtils.getFilteredActivityLog(user, task, start, end);

            // Set data to the table view
            activityLogTable.setItems(FXCollections.observableArrayList(logEntries));
        } catch (Exception e) {
            errorMessageLabel.setText("Error loading activity log: " + e.getMessage());
        }
    }


    @FXML
    private void onSearch() {
        String selectedUser = userComboBox.getValue();
        String selectedTask = taskComboBox.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        String sDate = String.valueOf(startDate);
        String eDate = String.valueOf(endDate);

        loadActivityLog(selectedUser, selectedTask, sDate, eDate);
    }
}
