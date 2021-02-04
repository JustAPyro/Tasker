package sample;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Window;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Controller implements Initializable
{

    public Button newTaskButton;

    public Label welcomeLabel;

    private Connection dbConncection;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

        createConnection();

        //Window win = welcomeLabel.getScene().getWindow();

        //int rs = (int) win.getUserData();
        /*
        try {
            welcomeLabel.setText("Welcome " + rs.getString("username") + "!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        */

        System.out.println("Setting textbox to username");
        welcomeLabel.setText("Welcome " + UserData.getUsername() + "!");

        createConnection();
    }

    private void createConnection()
    {



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
