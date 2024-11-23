package com.example.synapse;

//import com.example.synapse.database.DatabaseUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Load the task.fxml file
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/example/synapse/fxml/create_project_board.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Synapse App");
        stage.setScene(scene);
        stage.setHeight(600);
        stage.setWidth(600);

        //stage.setWidth(1200);
        //stage.setHeight(700);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
