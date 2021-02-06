package sample;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
        TableColumn<Task, String> column1 = new TableColumn<>("To do:");
        column1.setCellValueFactory(new PropertyValueFactory<>("taskName"));

        TableColumn<Task, String> detailsColumn = new TableColumn<>("Task Details");
        detailsColumn.setCellValueFactory(new PropertyValueFactory<>("taskDetails"));

        TableColumn<Task, String> dueDateColumn = new TableColumn<>("Due Date");
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        taskTable.getColumns().add(column1);
        taskTable.getColumns().add(detailsColumn);
        taskTable.getColumns().add(dueDateColumn);

        taskTable.getItems().add(new Task("Math", "Do stuff", new Date(2021, 10, 2)));

        createConnection();
        loadTasks(UserData.getid(), taskTable);

        //Window win = welcomeLabel.getScene().getWindow();

        //int rs = (int) win.getUserData();
        /*
        try {
            welcomeLabel.setText("Welcome " + rs.getString("username") + "!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        */

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
        taskTable.getItems().add(newTask);
        System.out.println(newTask);
        try
        {
            Connection dbConnection = DriverManager.getConnection("jdbc:mysql://sql5.freesqldatabase.com:3306/sql5390450", "sql5390450", "y64muxBbiV");
            PreparedStatement pstmt = dbConnection.prepareStatement("INSERT INTO `tasks`(`id`, `name`, `details`) VALUES (31, 'calc', 'reading')");
            //pstmt.setInt(1, UserData.getid());
            //pstmt.setString(2, newTask.getTaskName());
            //pstmt.setString(3, newTask.getTaskDetails());
            //java.sql.Date sqlDate = new java.sql.Date(newTask.getDueDateDate().getYear(), newTask.getDueDateDate().getMonth(), newTask.getDueDateDate().getDate());
            //pstmt.setDate(4, new java.sql.Date(1, 3, 4));
            pstmt.executeUpdate();
            dbConncection.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally
        {

        }
    }

    public void submit() {


    }

    private void loadTasks(int userid, TableView taskView)
    {
        try {
            Connection dbConnection = DriverManager.getConnection("jdbc:mysql://sql5.freesqldatabase.com:3306/sql5390450", "sql5390450", "y64muxBbiV");
            PreparedStatement pstmt = dbConnection.prepareStatement("SELECT name, details, duedate FROM tasks WHERE id = ?");
            pstmt.setInt(1, UserData.getid());
            ResultSet taskResults = pstmt.executeQuery();

            while (taskResults.next())
            {

                String name = taskResults.getString("name");
                String details = taskResults.getString("details");
                Date dueDate = taskResults.getDate("duedate");
                Task retrievedTask = new Task(name, details, dueDate);

                //UserData.addTask(retrievedTask);
                taskView.getItems().add(retrievedTask);

            }



        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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
                String name = taskResults.getString("name");
                String details = taskResults.getString("details");
                Date dueDate = taskResults.getDate("duedate");
                //UserData.addTask(new Task(name, details, dueDate));
            }



        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


}
