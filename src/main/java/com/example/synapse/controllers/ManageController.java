package com.example.synapse.controllers;

import com.example.synapse.database.DatabaseUtils;
import com.example.synapse.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ManageController {
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> userNameColumn;
    @FXML private TableColumn<User, String> userEmailColumn;
    @FXML private TableColumn<User, String> userRoleColumn;
    @FXML private TableColumn<User, Boolean> userStatusColumn;

    @FXML private TextField searchTextField;
    @FXML private Button deactivateUserButton;
    @FXML private Button editUserButton;
    @FXML private Button addUserButton;


    @FXML private TextField userNameField;
    @FXML private TextField userEmailField;
    @FXML private TextField passwordField;
    @FXML private ComboBox<String> userRoleComboBox;
    @FXML private CheckBox isActiveCheckBox;
    @FXML private Button submitButton;
    @FXML private Button cancelButton;

    @FXML private VBox deactivationConfirmation;
    @FXML private Button confirmDeactivationButton;
    @FXML private Button cancelDeactivationButton;

    private boolean isAddUserPressed = false;

    private DatabaseUtils dbUtils;
    private ObservableList<User> userList;
    private FilteredList<User> filteredUsers;
    private static final String[] ROLES = {"Team Member", "Project Manager"};

    @FXML
    public void initialize() {
        dbUtils = new DatabaseUtils();
        setupTable();
        setupSearchFunctionality();
        setupButtons();
        setupRoleComboBox();
        loadUsers();

        // Initially disable edit and deactivate buttons
        editUserButton.setDisable(true);
        deactivateUserButton.setDisable(true);

        // Hide deactivation confirmation initially
        deactivationConfirmation.setVisible(false);
    }

    private void setupTable() {

        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        userEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        userRoleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        userStatusColumn.setCellValueFactory(new PropertyValueFactory<>("active"));


        userStatusColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "Active" : "Inactive");
                }
            }
        });

        userTable.setStyle("-fx-text-fill: black;");

        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean hasSelection = newSelection != null;
            editUserButton.setDisable(!hasSelection);
            deactivateUserButton.setDisable(!hasSelection);

            if (hasSelection) {
                deactivateUserButton.setDisable(!newSelection.isActive());
                passwordField.setVisible(false);
                isAddUserPressed = false;
                clearForm();
            }
        });
    }

    private void setupSearchFunctionality() {

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (filteredUsers != null) {
                filteredUsers.setPredicate(user -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();
                    return user.getUsername().toLowerCase().contains(lowerCaseFilter) ||
                            user.getEmail().toLowerCase().contains(lowerCaseFilter);
                });
            }
        });
    }

    private void setupButtons() {
        editUserButton.setOnAction(e -> {
            User selectedUser = userTable.getSelectionModel().getSelectedItem();
            passwordField.setVisible(false);
            userNameField.setDisable(true);
            isAddUserPressed = false;
            if (selectedUser != null) {
                populateEditForm(selectedUser);
            }
        });

        deactivateUserButton.setOnAction(e -> {
            User selectedUser = userTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null && selectedUser.isActive()) {
                showDeactivationPopup(selectedUser);
            }
        });


        submitButton.setOnAction(e -> {
            if(isAddUserPressed){
                addUser();
                loadUsers();
                clearForm();
                passwordField.setVisible(false);
            }
            else{
                submitUserChanges();
                loadUsers();
                clearForm();
                passwordField.setVisible(false);
                showAlert(Alert.AlertType.INFORMATION, "Success", "User updated successfully.");
            }

        });

        cancelButton.setOnAction(e -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });

        addUserButton.setOnAction(e -> {

            userTable.getSelectionModel().clearSelection();
            editUserButton.setDisable(true);
            deactivateUserButton.setDisable(true);

            userNameField.setDisable(false);
            isAddUserPressed = true;
            showAddUserForm();
        });
    }

    private void showAddUserForm() {
        passwordField.setVisible(true);
        userNameField.clear();
        userEmailField.clear();
        userRoleComboBox.setValue(null);
        isActiveCheckBox.setSelected(false);
    }

    private void setupRoleComboBox() {
        userRoleComboBox.setItems(FXCollections.observableArrayList(ROLES));
    }

    private void loadUsers() {
        userList = FXCollections.observableArrayList(dbUtils.getAllUsers());
        filteredUsers = new FilteredList<>(userList, p -> true);
        userTable.setItems(filteredUsers);
    }

    private void populateEditForm(User user) {
        userNameField.setText(user.getUsername());
        userNameField.setDisable(true);
        userEmailField.setText(user.getEmail());
        userRoleComboBox.setValue(user.getRole());
        isActiveCheckBox.setSelected(user.isActive());
    }

    private void deactivateUser(User user) {
        String sql = "UPDATE Users SET IsActive = 0 WHERE Username = ?";
        try (var connection = DatabaseUtils.connect();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getUsername());
            int affected = preparedStatement.executeUpdate();

            if (affected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "User has been deactivated successfully.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "No user was deactivated. Please try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to deactivate user.");
        }
    }

    private boolean submitUserChanges() {
        String username = userNameField.getText();
        String email = userEmailField.getText();
        String role = userRoleComboBox.getValue();
        boolean isActive = isActiveCheckBox.isSelected();

        if (username.isEmpty() || email.isEmpty() || role == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            return false;
        }

        String sql = "UPDATE Users SET Email = ?, Role = ?, IsActive = ? WHERE Username = ?";
        try (var connection = DatabaseUtils.connect();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, role);
            preparedStatement.setBoolean(3, isActive);
            preparedStatement.setString(4, username);

            int affected = preparedStatement.executeUpdate();
            if (affected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "User updated successfully.");
                loadUsers();
                clearForm();
                return true;
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "No user was updated. Please try again.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update user.");
            return false;
        }
    }

    private boolean addUser() {
        String username = userNameField.getText();
        String email = userEmailField.getText();
        String password = passwordField.getText();
        String role = userRoleComboBox.getValue();
        boolean isActive = isActiveCheckBox.isSelected();

        // Validate input
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || role == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            return false;
        }

        // SQL to insert a new user
        String sql = "INSERT INTO Users (Username, Email, Password, Role, IsActive, CreatedAt) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (var connection = DatabaseUtils.connect();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, role);
            preparedStatement.setBoolean(5, isActive);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "User added successfully.");
                return true;
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "No user was added. Please try again.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add user.");
            return false;
        }
    }


    private void clearForm() {
        userNameField.clear();
        userEmailField.clear();
        userRoleComboBox.setValue(null);
        isActiveCheckBox.setSelected(true);
        userNameField.setDisable(false);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showDeactivationPopup(User selectedUser) {
        // Create a new VBox for the deactivation popup layout
        VBox popupLayout = new VBox(20);
        popupLayout.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-padding: 20;");
        popupLayout.setAlignment(Pos.CENTER);

        Label confirmationLabel = new Label("Are you sure you want to deactivate this user?");
        confirmationLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        Button confirmButton = new Button("Deactivate");
        Button cancelButton = new Button("Cancel");


        HBox buttonLayout = new HBox(10);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().addAll(confirmButton, cancelButton);

        popupLayout.getChildren().addAll(confirmationLabel, buttonLayout);

        Stage popupStage = new Stage();
        popupStage.setTitle("Deactivate User");
        popupStage.setScene(new Scene(popupLayout));
        popupStage.initModality(Modality.APPLICATION_MODAL); // Make it modal

        confirmButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5px 15px; -fx-border-radius: 10; -fx-background-radius: 10;");
        cancelButton.setStyle("-fx-font-size: 12px; -fx-padding: 5px 15px; -fx-border-radius: 10; -fx-background-radius: 10;");

        confirmButton.setOnAction(e -> {
            deactivateUser(selectedUser);
            loadUsers();
            popupStage.close();
        });

        cancelButton.setOnAction(e -> popupStage.close()); // Close the popup window without action

        popupStage.show();
    }

}