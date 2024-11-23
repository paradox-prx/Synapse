package com.example.synapse;

//import com.example.synapse.database.DatabaseUtils;
import com.example.synapse.database.DatabaseUtils;
import com.example.synapse.models.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import com.example.synapse.models.Dashboard;
public class Main extends Application {
    public static User user;
    public static Dashboard dashboard;
    @Override
    public void start(Stage stage) throws IOException {
        // Load the task.fxml file
        user = new User("", "", "", "", true);
         dashboard = new Dashboard(Main.user, new DatabaseUtils());
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/example/synapse/fxml/login.fxml"));
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
