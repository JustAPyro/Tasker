package sample;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Controller implements Initializable
{

    public Button newTaskButton;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        createConnection();
    }

    private void createConnection() {

    }

    public void newTaskButton()
    {
        System.out.println("Getting new task");
        Task newTask = Task.newTaskPopup(newTaskButton.getScene().getWindow(), this);
        System.out.println(newTask);
    }

    public void submit() {


    }


}
