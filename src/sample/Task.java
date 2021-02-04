package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Date;

public class Task
{

    String name;
    String details;
    Date dueDate;



    public Task()
    {



    }


    public static Task newTaskPopup(Window window, MainController controller)
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

        Task returnTask;
        returnTask = loader.<InputController>getController().getTask();
        return returnTask;
    }

    // Setters
    public void setName(String name)
    {
        this.name = name;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
