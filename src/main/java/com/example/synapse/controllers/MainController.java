package com.example.synapse.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class MainController {

    @FXML
    private ListView<String> boardListView;

    @FXML
    private ListView<String> newTasksListView;

    @FXML
    private ListView<String> abdullahWipListView;

    @FXML
    private ListView<String> digitalMarketingListView;

    @FXML
    public void initialize() {
        // Populate the boards list
        ObservableList<String> boards = FXCollections.observableArrayList("Skyline Academic");
        boardListView.setItems(boards);

        // Populate New Tasks list
        ObservableList<String> newTasks = FXCollections.observableArrayList(
                "Alternative video for Assignment feedback",
                "Use the same script for real characters"
        );
        newTasksListView.setItems(newTasks);

        // Populate Abdullah's WIP list
        ObservableList<String> abdullahWipTasks = FXCollections.observableArrayList(
                "Skyline website checklist",
                "Create a video for Plagiarism/AI workflow",
                "Things Needed from Jawwad"
        );
        abdullahWipListView.setItems(abdullahWipTasks);

        // Populate Digital Marketing list
        ObservableList<String> digitalMarketingTasks = FXCollections.observableArrayList(
                "Skyline LinkedIn Revamp",
                "Skyline Instagram Marketing"
        );
        digitalMarketingListView.setItems(digitalMarketingTasks);
    }
}
