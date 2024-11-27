package com.example.synapse.services;

import com.example.synapse.database.DatabaseUtils;
import com.example.synapse.models.Feedback;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class FeedbackFactory {
    private ObservableList<Feedback> feedbackList = FXCollections.observableArrayList();
    DatabaseUtils db = new DatabaseUtils();

    public FeedbackFactory() {

    }


    public ObservableList<Feedback> getFeedbackList() {
        feedbackList.clear();
        feedbackList.addAll(db.loadFeedback());
        return feedbackList;
    }


    public boolean updateFeedback(int feedbackID, String newStatus, String actionTaken) {
        return db.updateFeedback(feedbackID, newStatus, actionTaken);
    }
}
