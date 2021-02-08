package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

public class Task
{

    int taskid; // Unique ID associated with the task
    int userid; // UserID for who the task belongs to
    int parentid; // ID of the tasks user/parent if applicable (otherwise 0)
    boolean isabstract; // If true, this is just a category header
    String name; // Name of task
    String details; // Details of task
    String location; // URL or file location of task
    LocalDateTime dueDate; // Date task should be done
    LocalDateTime createDate; // Date task was created
    int recurring; // Represents how many days task should recur (0 means never)

    public Task()
    {

    }


    // Simplest constructor, just requires name and details
    public Task(String name, String details)
    {
        taskid = 0;
        userid = 0;
        parentid = 0;
        isabstract = false;

        this.name = name;
        this.details = details;

        location = null;
        dueDate = null;
       // createDate =


    }

    public Task(String name, String details, LocalDateTime dueDate)
    {
        this.name = name;
        this.details = details;
        this.dueDate = dueDate;
    }

    public Task(String name)
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

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public String getTaskName()
    {
        return name;
    }

    public String getTaskDetails()
    {
        return details;
    }

    public String getDueDate()
    {
        return dueDate.toString();
    }

    public LocalDateTime getDueDateDate()
    {
        return dueDate;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
