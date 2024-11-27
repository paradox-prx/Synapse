package com.example.synapse.services;

import com.example.synapse.Main;
import com.example.synapse.database.DatabaseUtils;
import com.example.synapse.models.Activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ActivityFactory {
    List<Activity> logEntries;
    public DatabaseUtils db = new DatabaseUtils();

    public ActivityFactory() {
        logEntries = new ArrayList<Activity>();
    }


    public List<String> getUsers(int currentBoard) throws SQLException {
        return db.getUsers(currentBoard);
    }


    public List<Activity> getAllActivityLog(int currentBoard) {
        return db.getAllActivityLog(currentBoard);
    }

    public List<String> getTasks(String user, int currentBoard) {
        return db.getTasks(user, currentBoard);
    }

    public List<Activity> getFilteredActivityLog(String user, String task, String start, String end, int currentBoard) throws SQLException {
        return db.getFilteredActivityLog(user, task, start, end, currentBoard);
    }
}
