package sample;

import com.sun.org.apache.xml.internal.security.signature.ReferenceNotInitializedException;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class UserData {

   public static final String dbAddress = "jdbc:mysql://sql5.freesqldatabase.com:3306/sql5393891";
   public static final String dbUser = "sql5393891";
   public static final String dbPassword = "j9mPmDpmpq";

   private static int id;
   private static String username;
   private static Stage activeStage;

   private static LinkedList<Task> allTasks = new LinkedList<Task>();

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

    public static Task getTask(int taskid)
    {
        // Check each task in our current local RAM list
        for (Task t : allTasks)
        {
            // Check to see if task ID matches the one we're looking for
            if (t.getTaskID() == taskid)
            {
                return t; // If so, return task
            }
        }

        throw new NoSuchElementException("Did not find task in local list");
    }

    public static LinkedList<Task> getAllTasks()
    {
        return allTasks;
    }


    //TODO: Implement this in  O(nlogn) timehg
    public static void refreshTable(TableView table) throws SQLException {

        LinkedList<Integer> databaseIDs = new LinkedList<Integer>();

        Connection dbConnection = DriverManager.getConnection(UserData.dbAddress, UserData.dbUser, UserData.dbPassword);
        PreparedStatement pstmt = dbConnection.prepareStatement("SELECT taskid FROM tasks WHERE userid = ?");
        pstmt.setInt(1, UserData.getid());
        ResultSet taskIDResults = pstmt.executeQuery();

        while (taskIDResults.next())
        {
            databaseIDs.add(taskIDResults.getInt("taskid"));
        }



        //int[] databaseList = (int) (databaseIDs.toArray();
        //int[] tableList = (Task[]) table.getItems().toArray();




    }



}
