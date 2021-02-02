package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Date;

public class Task
{

    String taskName;
    String details;
    Date dueDate;

    public Task()
    {



    }


    public static Task newTaskPopup(Window window, Controller controller)
    {

        FXMLLoader loader = new FXMLLoader(controller.getClass().getResource("input.fxml"));
        Scene newScene;
        try {
            newScene = new Scene(loader.load());
        } catch (IOException ex) {
            // TODO: Handle Error
            return null;
        }

        Stage inputStage = new Stage();
        inputStage.initOwner(window);
        inputStage.setScene(newScene);
        inputStage.showAndWait();

        Task returnTask = loader.<InputController>getController().getTask();
        return returnTask;
    }
}
