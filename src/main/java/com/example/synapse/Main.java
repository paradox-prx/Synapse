package com.example.synapse;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Test the database
        DatabaseUtils.createTable();
        DatabaseUtils.insertSampleData();
        DatabaseUtils.retrieveData();

        // Load the task.fxml file
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("sign-up.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Synapse App");
        stage.setScene(scene);
        stage.setWidth(1200);
        stage.setHeight(700);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
