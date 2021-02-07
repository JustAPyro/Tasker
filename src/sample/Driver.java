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
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.getIcons().add(new Image("file:LogoCheck.png"));
        primaryStage.setTitle("Tasker - Log in");
        primaryStage.setScene(new Scene(root, 600, 450));

        primaryStage.show();

    }

    @Override
    public void stop() throws Exception {
        UserData.getActiveStage().hide();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
