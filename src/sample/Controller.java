package sample;

import javafx.scene.control.Button;

public class Controller
{

    public Button newTaskButton;

    public void newTaskButton()
    {
        Task newTask = Task.newTaskPopup(newTaskButton.getScene().getWindow(), this);
    }

}
