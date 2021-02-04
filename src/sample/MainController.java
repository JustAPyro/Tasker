package sample;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Window;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class MainController implements Initializable
{

    public Button newTaskButton;

    public Label welcomeLabel;
    public TableView taskTable;

    private Connection dbConncection;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        taskTable = new TableView<Task>();

        createConnection();
        loadTasks(UserData.getid());

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

    private void loadTasks(int userid) {
        try {
            Connection dbConnection = DriverManager.getConnection("jdbc:mysql://sql5.freesqldatabase.com:3306/sql5390450", "sql5390450", "y64muxBbiV");
            PreparedStatement pstmt = dbConnection.prepareStatement("SELECT name, details, duedate FROM tasks WHERE id = ?");
            pstmt.setInt(1, UserData.getid());
            ResultSet taskResults = pstmt.executeQuery();

            while (taskResults.next())
            {
                System.out.println(taskResults.getString("name") + " : " + taskResults.getString("details"));
            }



        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


}
