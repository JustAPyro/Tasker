package sample;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.sql.*;
import java.util.LinkedList;

public class UserData {

   private static int id;
   private static String username;
   private static Stage activeStage;

   private static LinkedList<Task> allTasks;

    public static void setid(int newid, String newusername)
    {
        id = newid;
        username = newusername;
    }

    public static int getid()
    {
        return id;
    }

    public static String getUsername()
    {
        return username;
    }

    public static void setActiveStage(Stage stage)
    {
        activeStage = stage;
    }

    public static Stage getActiveStage()
    {
        return activeStage;
    }

    public static void addTask(Task task) {
        allTasks.add(task);
    }

    public static void refreshTable(TableView table) throws SQLException {

        

        Connection dbConnection = DriverManager.getConnection("jdbc:mysql://sql5.freesqldatabase.com:3306/sql5390450", "sql5390450", "y64muxBbiV");
        PreparedStatement pstmt = dbConnection.prepareStatement("SELECT taskid FROM tasks WHERE userid = ?");
        pstmt.setInt(1, UserData.getid());
        ResultSet taskIDResults = pstmt.executeQuery();

        while (taskIDResults.next())
        {
            taskIDResults.getInt("taskid");
        }

        ObservableList<Task> tableList = table.getItems();
        tableList.forEach(task -> {

        });
    }



}
