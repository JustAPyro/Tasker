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

   public static final String dbAddress = "jdbc:mysql://sql473.main-hosting.eu:3306/u375142563_Tasker?autoReconnect=true";
   public static final String dbUser = "u375142563_root";
   public static final String dbPassword = "Tasker2021";

   public static final Boolean testing = true;

   private static int id;
   private static String username;
   private static String firstName;
   private static Stage activeStage;

   private static LinkedList<Task> allTasks = new LinkedList<Task>();

    /**
     * Sets the first name stored in the static UserData class
     *
     * @param newFirstName Represents the users first name.
     */
    public static void setFirstName(String newFirstName)
    {
        firstName = newFirstName;
    }

    /**
     * Returns the first name saved into the static UserData
     *
     * @return The first name of user.
     */
    public static String getFirstName()
    {
        return firstName;
    }

    /**
     * Sets the new id to be saved into user data.
     *
     * @param newid The ID you want saved into user Data in ram.
     */
    public static void setid(int newid)
    {
        // Set the static id to new id
        id = newid;
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
