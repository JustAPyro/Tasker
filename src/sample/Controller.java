package sample;

import javafx.scene.control.Button;

public class Controller
{

    public Button newTaskButton;

    public void newTaskButton()
    {
        System.out.println("Getting new task");
        Task newTask = Task.newTaskPopup(newTaskButton.getScene().getWindow(), this);
        System.out.println(newTask);
    }

}
