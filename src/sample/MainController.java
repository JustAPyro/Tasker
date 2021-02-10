package sample;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

import static javafx.application.Platform.setImplicitExit;

public class MainController implements Initializable
{

    private static final String logoImageLoc = "src/sample/LogoCheck.png";

    public Button newTaskButton;

    public Label welcomeLabel;
    public TableView taskTable;
    public TreeView taskTree;

    public RadioMenuItem showTreeCheck;

    private Connection dbConncection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setImplicitExit(false);

        TableColumn<Task, String> column1 = new TableColumn<>("To do:");
        column1.setCellValueFactory(new PropertyValueFactory<>("taskName"));

        TableColumn<Task, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("taskCategory"));

        TableColumn<Task, String> detailsColumn = new TableColumn<>("Task Details");
        detailsColumn.setCellValueFactory(new PropertyValueFactory<>("taskDetails"));

        TableColumn<Task, String> dueDateColumn = new TableColumn<>("Due Date");
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        taskTable.getColumns().add(column1);
        taskTable.getColumns().add(categoryColumn);
        taskTable.getColumns().add(detailsColumn);
        taskTable.getColumns().add(dueDateColumn);

        taskTree.setManaged(false);
        taskTree.setVisible(false);

        //taskTable.getItems().add(new Task("Math", "Do stuff", new Date(2021, 10, 2)));

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

        // Sets up the tray icon (using awt code run on swing thread)
        javax.swing.SwingUtilities.invokeLater(this::addAppToTray);


    }

    private void addAppToTray()
    {
        try {

            // First make sure AWT toolkit is set up
            java.awt.Toolkit.getDefaultToolkit();

            // Secondly Check if System Tray is supported
            if (!SystemTray.isSupported()) {
                System.out.println("WAIT, WHAT IN THE WORLD ARE YOU RUNNING THIS ON?");
                throw new UnsupportedOperationException("GET A PC");
            }

            final PopupMenu trayPopupMenu = new PopupMenu();

            // Set up a system tray icon
            SystemTray systemTray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage("src/sample/LogoCheck.png");
            TrayIcon trayIcon = new TrayIcon(image, "Tasker", trayPopupMenu);
            trayIcon.setImageAutoSize(true);

            // If the user double-clicks tray icon, show the main stage
            trayIcon.addActionListener(event -> Platform.runLater(this::showStage));

            // If the user selects open then show the main stage
            java.awt.MenuItem openItem = new java.awt.MenuItem("Open");
            openItem.addActionListener(event -> Platform.runLater(this::showStage));

            java.awt.MenuItem newTask = new java.awt.MenuItem("New Task");
            newTask.addActionListener(event -> Platform.runLater(this::newTaskButton));

            java.awt.MenuItem exitItem = new java.awt.MenuItem("Exit");
            exitItem.addActionListener(event -> {
                Platform.exit();
                systemTray.remove(trayIcon);
            });


            trayPopupMenu.add(openItem);
            trayPopupMenu.add(newTask);
            trayPopupMenu.addSeparator();
            trayPopupMenu.add(exitItem);
            trayIcon.setPopupMenu(trayPopupMenu);

            systemTray.add(trayIcon);

        }
        catch(AWTException e)
        {
            System.out.println("unable to init system tray");
            e.printStackTrace();
        }
    }

    protected static Image createImage(String path, String description)
    {
        URL imageURL = MainController.class.getResource(path);
        if (imageURL == null)
        {
            System.err.println("Resource not found: " + path);
            return null;
        }
        else
        {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }

    private void showStage()
    {
        Stage stg = UserData.getActiveStage();
        if (stg != null)
        {
            stg.show();
            stg.toFront();
        }
    }

    // This gets called when the show task tree is selected
    public void onShowTreeCheck()
    {
        // If it was selected
        if (showTreeCheck.isSelected() == true)
        {
            // Set the task tree to be visible
            taskTree.setVisible(true);

            // Also set it to be managed, so it sizes to the window
            taskTree.setManaged(true);
        }
        else // Otherwise, the task tree should be hidden
        {
            // Set the task tree to be hidden
            taskTree.setVisible(false);

            // Also make it un-managed so it doesn't leave empty space
            taskTree.setManaged(false);
        }
    }

    public void newTaskButton()
    {
        Task newTask = Task.newTaskPopup(newTaskButton.getScene().getWindow(), this);
        taskTable.getItems().add(newTask);
    }

    public void completeTask()
    {
        Task task = (Task) taskTable.getSelectionModel().getSelectedItem();
        try
        {
            task.removeTask(); // Removes it from database

            // Removes it from the database (Later, you may wish to replace this with a update function that also checks the server for updates)
            taskTable.getItems().removeAll(
                    taskTable.getSelectionModel().getSelectedItems()
            );
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }


    public void submit() {


    }

    private void loadTasks(int userid, TableView taskView)
    {
        try {
            Connection dbConnection = DriverManager.getConnection("jdbc:mysql://sql5.freesqldatabase.com:3306/sql5390450", "sql5390450", "y64muxBbiV");
            PreparedStatement pstmt = dbConnection.prepareStatement("SELECT taskid, userid, parentid, abstract, name, details, location, duedate, createdate, recurring FROM tasks WHERE userid = ?");
            pstmt.setInt(1, UserData.getid());
            ResultSet taskResults = pstmt.executeQuery();

            // Iterate through each row of the table
            while (taskResults.next())
            {

                // Loads a instance of Task based on the current row of the table
                Task retrievedTask = Task.loadTask(taskResults);

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
