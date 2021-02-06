package sample;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static javafx.application.Platform.setImplicitExit;

public class MainController implements Initializable
{

    public Button newTaskButton;

    public Label welcomeLabel;
    public TableView taskTable;

    private Connection dbConncection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setImplicitExit(false);

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
            System.out.println("Adding");

            // First make sure AWT toolkit is set up
            java.awt.Toolkit.getDefaultToolkit();

            // Secondly Check if System Tray is supported
            if (!SystemTray.isSupported()) {
                System.out.println("WAIT, WHAT IN THE WORLD ARE YOU RUNNING THIS ON?");
                throw new UnsupportedOperationException("GET A PC");
            }

            // Set up a system tray icon
            SystemTray systemTray = SystemTray.getSystemTray();
            java.awt.Image awtImage = Toolkit.getDefaultToolkit().getImage("src/sample/LogoCheck.png");
            TrayIcon trayIcon = new TrayIcon(awtImage, "Tasker");

            // If the user double-clicks tray icon, show the main stage
            trayIcon.addActionListener(event -> Platform.runLater(this::showStage));

            // If the user selects open then show the main stage
            java.awt.MenuItem openItem = new java.awt.MenuItem("Open");
            openItem.addActionListener(event -> Platform.runLater(this::showStage));

            java.awt.MenuItem exitItem = new java.awt.MenuItem("Exit");
            exitItem.addActionListener(event -> {
                Platform.exit();
                systemTray.remove(trayIcon);
            });

            final PopupMenu trayPopupMenu = new PopupMenu();
            trayPopupMenu.add(openItem);
            trayPopupMenu.addSeparator();
            trayPopupMenu.add(exitItem);
            trayIcon.setPopupMenu(trayPopupMenu);

            systemTray.add(trayIcon);
    /*
            MenuItem open = new MenuItem("Open");
            open.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Stage win = (Stage) welcomeLabel.getScene().getWindow();
                    win.show();
                }

            });
            trayPopupMenu.add(open);

            MenuItem action = new MenuItem("Add Task!");
            action.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                }
            });
            trayPopupMenu.add(action);

            MenuItem close = new MenuItem("Close");
            close.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            trayPopupMenu.add(close);


            trayIcon.setImageAutoSize(true);

            try {
                systemTray.add(trayIcon);
            } catch (AWTException awtException) {
                awtException.printStackTrace();
            }
            */
        }
        catch(java.awt.AWTException e)
        {
            System.out.println("unable to init system tray");
            e.printStackTrace();
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

    public void newTaskButton()
    {
        System.out.println("Getting new task");
        Task newTask = Task.newTaskPopup(newTaskButton.getScene().getWindow(), this);
        taskTable.getItems().add(newTask);
        System.out.println(newTask);
        try
        {
            Connection dbConnection = DriverManager.getConnection("jdbc:mysql://sql5.freesqldatabase.com:3306/sql5390450", "sql5390450", "y64muxBbiV");
            PreparedStatement pstmt = dbConnection.prepareStatement("INSERT INTO tasks (taskid, name, details, duedate) VALUES (?, ?, ?, ?)");
            pstmt.setInt(1, UserData.getid());
            pstmt.setString(2, newTask.getTaskName());
            pstmt.setString(3, newTask.getTaskDetails());
            java.sql.Date sqlDate = new java.sql.Date(newTask.getDueDateDate().getYear(), newTask.getDueDateDate().getMonth(), newTask.getDueDateDate().getDate());
            pstmt.setDate(4, sqlDate);
            pstmt.executeUpdate();
            //dbConncection.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void submit() {


    }

    private void loadTasks(int userid, TableView taskView)
    {
        try {
            Connection dbConnection = DriverManager.getConnection("jdbc:mysql://sql5.freesqldatabase.com:3306/sql5390450", "sql5390450", "y64muxBbiV");
            PreparedStatement pstmt = dbConnection.prepareStatement("SELECT name, details, duedate FROM tasks WHERE userid = ?");
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
