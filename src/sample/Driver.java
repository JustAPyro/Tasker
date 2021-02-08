package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Driver extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("login.fxml")); // Load the login page first
        UserData.setActiveStage(primaryStage); // Set the active stage to user data
        primaryStage.getIcons().add(new Image("file:LogoCheck.png")); // Add the icon
        primaryStage.setTitle("Tasker - Log in"); // Set the title
        primaryStage.setScene(new Scene(root, 600, 450)); // Create scene at 600/450 size
        primaryStage.show(); // set to showing

    }

    @Override // Gets called on stage close, we want to hide
    public void stop() throws Exception {
        UserData.getActiveStage().hide();
    }

    // Main Method
    public static void main(String[] args) {
        launch(args);
    }
}
