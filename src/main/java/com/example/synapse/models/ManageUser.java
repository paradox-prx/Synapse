package com.example.synapse.models;

import com.example.synapse.database.DatabaseUtils;
import com.example.synapse.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class ManageUser {
    private final DatabaseUtils dbUtils;
    private ObservableList<User> userList;

    public ManageUser() {
        dbUtils = new DatabaseUtils();
    }

    public ObservableList<User> getAllUsers() {

        return FXCollections.observableArrayList(dbUtils.getAllUsers());
    }

    public void addUser(String username, String email, String password, String role, boolean isActive) {
        dbUtils.insertNewUser(username, email, password, role, isActive);
    }

    public void updateUser(String username, String email, String role, boolean isActive) {
        dbUtils.updateNewUser(username, email, role, isActive);
    }

    public void deactivateUser(User user) {
        dbUtils.deactivateUser(user);
    }
}
