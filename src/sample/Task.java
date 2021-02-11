package sample;

import com.sun.org.apache.xml.internal.security.signature.ReferenceNotInitializedException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class Task
{

    int taskid; // Unique ID associated with the task
    int userid; // UserID for who the task belongs to
    int parentid; // ID of the tasks user/parent if applicable (otherwise 0)
    boolean isabstract; // If true, this is just a category header
    String name; // Name of task
    String details; // Details of task
    String location; // URL or file location of task
    ZonedDateTime dueDate; // Date task should be done
    ZonedDateTime createDate; // Date task was created
    int recurring; // Represents how many days task should recur (0 means never)

    public Task()
    {

    }


    /**
     *
     * @param taskID: fds
     * @param userID
     * @param parentID
     * @param isabstract
     * @param name
     * @param details
     * @param location
     * @param duedate
     * @param createDate
     * @param recurring
     */ // Constructor for all parameters. NOTE: Since taskID is passed in, we know that this has coming FROM a database, therefor we do not have to upload it.
    public Task(int taskID, int userID, int parentID, boolean isabstract, String name, String details, String location, ZonedDateTime duedate, ZonedDateTime createDate, int recurring)
    {
        this.taskid = taskID;
        this.userid = userID;
        this.parentid = parentID;
        this.isabstract = isabstract;
        this.name = name;
        this.details = details;
        this.location = location;
        this.dueDate = duedate;
        this.createDate = createDate;
        this.recurring = recurring;
    }

    // Simplest constructor, just requires name and details (Note: This pushes to server)
    public Task(String name, String details)  throws ReferenceNotInitializedException, SQLException {

        taskid = 0; // Just setting this to 0 here for sake of completion, it gets overwritten later
        userid = UserData.getid(); // Set the userid to current static user id
        parentid = 0; // since not specified, we'll assume this task doesn't have a parent
        isabstract = false; // Since not specified, we'll assume this isn't an abstracted task

        this.name = name; // Set name equal to the passed parameter
        this.details = details; // Set details equal to the passed parameter

        location = null; // Since not specified, assume there's no given location
        dueDate = null; // Since not specified, assume there's no given duedate
        createDate = ZonedDateTime.now(); // Set create date to NOW NOTE: Uses the system clock for timezone
        recurring = 0; // Since not specified, assume it's not a recurring task


        Connection dbConnection = DriverManager.getConnection(UserData.dbAddress, UserData.dbUser, UserData.dbPassword);

        // Create a prepared statement inserting the values we have
        PreparedStatement insertion = dbConnection.prepareStatement(
                "INSERT INTO tasks (userid, name, details, createdate) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        insertion.setInt(1, userid); // Set userid
        insertion.setString(2, name); // Set name
        insertion.setString(3, details); // set details
        insertion.setTimestamp(4, new Timestamp(createDate.toInstant().toEpochMilli())); // Convert now to timestamp and submit
        insertion.executeUpdate(); // Execute SQL update

        ResultSet rs = insertion.getGeneratedKeys(); // Get the generated values
        if (rs.next())
            taskid = rs.getInt(1); // Set taskid to the generated taskID

        UserData.addTask(this);

    }

    // Constructor passing an abstraction (Category)
    public Task(int parentid, boolean isabstract, String name, String details) throws SQLException {
        taskid = 0; // Temporary holder
        userid = UserData.getid(); // Set the userid for the task
        this.parentid = parentid;
        this.isabstract = isabstract;

        this.name = name;
        this.details = details;

        location = null;
        dueDate = null;
        createDate = ZonedDateTime.now();
        recurring = 0;

        Connection dbConnection = DriverManager.getConnection(UserData.dbAddress, UserData.dbUser, UserData.dbPassword);
        PreparedStatement insertion = dbConnection.prepareStatement("INSERT INTO tasks (userid, parentid, abstract, name, details, createdate) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        insertion.setInt(1, userid);
        insertion.setInt(2, parentid);
        insertion.setBoolean(3, isabstract);
        insertion.setString(4, name);
        insertion.setString(5, details);
        insertion.setTimestamp(6, new Timestamp(createDate.toInstant().toEpochMilli()));
        insertion.executeUpdate();

        ResultSet rs = insertion.getGeneratedKeys();
        if (rs.next())
            taskid = rs.getInt(1);

        UserData.addTask(this);

    }


    // Simple constructor that takes name, details, and due date (Note this pushes to server)
    public Task(String name, String details, ZonedDateTime duedate)  throws ReferenceNotInitializedException, SQLException {
        taskid = 0; // Just setting this to 0 here for sake of completion, it gets overwritten later
        userid = UserData.getid(); // Set the userid to current static user id
        parentid = 0; // since not specified, we'll assume this task doesn't have a parent
        isabstract = false; // Since not specified, we'll assume this isn't an abstracted task

        this.name = name; // Set name equal to the passed parameter
        this.details = details; // Set details equal to the passed parameter

        location = null; // Since not specified, assume there's no given location
        this.dueDate = duedate; // add the duedate
        createDate = ZonedDateTime.now(); // Set create date to NOW NOTE: Uses the system clock for timezone
        recurring = 0; // Since not specified, assume it's not a recurring task


        Connection dbConnection = DriverManager.getConnection(UserData.dbAddress, UserData.dbUser, UserData.dbPassword);

        // Create a prepared statement inserting the values we have
        PreparedStatement insertion = dbConnection.prepareStatement(
                "INSERT INTO tasks (userid, name, details, createdate, duedate) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        insertion.setInt(1, userid); // Set userid
        insertion.setString(2, name); // Set name
        insertion.setString(3, details); // set details
        insertion.setTimestamp(4, new Timestamp(createDate.toInstant().toEpochMilli())); // Convert now to timestamp and submit
        insertion.setTimestamp(5, new Timestamp(duedate.toInstant().toEpochMilli()));
        insertion.executeUpdate(); // Execute SQL update

        ResultSet rs = insertion.getGeneratedKeys(); // Get the generated values
        if (rs.next())
            taskid = rs.getInt(1); // Set taskid to the generated taskID

        UserData.addTask(this);
    }

    // Simple constructor that takes name, details, and due date (Note this pushes to server)
    public Task(String name, String details, ZonedDateTime duedate, String location)  throws ReferenceNotInitializedException, SQLException {
        // WORKING ON MAKING THIS USE DUEDATE
        taskid = 0; // Just setting this to 0 here for sake of completion, it gets overwritten later
        userid = UserData.getid(); // Set the userid to current static user id
        parentid = 0; // since not specified, we'll assume this task doesn't have a parent
        isabstract = false; // Since not specified, we'll assume this isn't an abstracted task

        this.name = name; // Set name equal to the passed parameter
        this.details = details; // Set details equal to the passed parameter

        this.location = location; // Since not specified, assume there's no given location
        this.dueDate = duedate; // add the duedate
        createDate = ZonedDateTime.now(); // Set create date to NOW NOTE: Uses the system clock for timezone
        recurring = 0; // Since not specified, assume it's not a recurring task


        Connection dbConnection = DriverManager.getConnection(UserData.dbAddress, UserData.dbUser, UserData.dbPassword);

        // Create a prepared statement inserting the values we have
        PreparedStatement insertion = dbConnection.prepareStatement(
                "INSERT INTO tasks (userid, name, details, location, createdate, duedate) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        insertion.setInt(1, userid); // Set userid
        insertion.setString(2, name); // Set name
        insertion.setString(3, details); // set details
        insertion.setString(4, location);
        insertion.setTimestamp(5, new Timestamp(createDate.toInstant().toEpochMilli())); // Convert now to timestamp and submit
        insertion.setTimestamp(6, new Timestamp(duedate.toInstant().toEpochMilli()));
        insertion.executeUpdate(); // Execute SQL update

        ResultSet rs = insertion.getGeneratedKeys(); // Get the generated values
        if (rs.next())
            taskid = rs.getInt(1); // Set taskid to the generated taskID

        UserData.addTask(this);
    }

    public Task(String name, Task parent, String details, ZonedDateTime duedate) throws ReferenceNotInitializedException, SQLException
    {
        taskid = 0; // Just setting this to 0 here for sake of completion, it gets overwritten later
        userid = UserData.getid(); // Set the userid to current static user id
        parentid = parent.getTaskID(); // since not specified, we'll assume this task doesn't have a parent
        isabstract = false; // Since not specified, we'll assume this isn't an abstracted task

        this.name = name; // Set name equal to the passed parameter
        this.details = details; // Set details equal to the passed parameter

        location = null; // Since not specified, assume there's no given location
        this.dueDate = duedate; // add the duedate
        createDate = ZonedDateTime.now(); // Set create date to NOW NOTE: Uses the system clock for timezone
        recurring = 0; // Since not specified, assume it's not a recurring task


        Connection dbConnection = DriverManager.getConnection(UserData.dbAddress, UserData.dbUser, UserData.dbPassword);

        // Create a prepared statement inserting the values we have
        PreparedStatement insertion = dbConnection.prepareStatement(
                "INSERT INTO tasks (userid, name, parentid, details, createdate, duedate) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        insertion.setInt(1, userid); // Set userid
        insertion.setString(2, name); // Set name
        insertion.setInt(3, parentid); //set parentid
        insertion.setString(4, details); // set details
        insertion.setTimestamp(5, new Timestamp(createDate.toInstant().toEpochMilli())); // Convert now to timestamp and submit
        insertion.setTimestamp(6, new Timestamp(duedate.toInstant().toEpochMilli()));
        insertion.executeUpdate(); // Execute SQL update

        ResultSet rs = insertion.getGeneratedKeys(); // Get the generated values
        if (rs.next())
            taskid = rs.getInt(1); // Set taskid to the generated taskID

        UserData.addTask(this);
    }

    // Loads and returns a task from the current set line of a ResultSet
    public static Task loadTask(ResultSet taskSet) throws SQLException {
        if (taskSet.getMetaData().getColumnCount() != 10)
            throw new RuntimeException("ResultSet is missing data. Please ensure you SELECT all task features");

        int taskID = taskSet.getInt("taskid");
        int userID = taskSet.getInt("userid");
        int parentID = taskSet.getInt("parentid");

        boolean isabstract = taskSet.getBoolean("abstract");

        String name = taskSet.getString("name");
        String details = taskSet.getString("details");
        String location = taskSet.getString("location");

        ZoneId timezone = ZoneId.of("America/New_York");
        ZonedDateTime duedate;
        try {
            Timestamp dueTS = taskSet.getTimestamp("duedate");
            duedate = ZonedDateTime.ofInstant(dueTS.toInstant(), timezone);
        }
        catch (NullPointerException e)
        {
            duedate = null;
        }

        Timestamp createTS = taskSet.getTimestamp("createdate");
        ZonedDateTime createdate = ZonedDateTime.ofInstant(createTS.toInstant(), timezone);

        int recurring = taskSet.getInt("recurring");

        Task returnTask = new Task(taskID, userID, parentID, isabstract, name, details, location, duedate, createdate, recurring);
        UserData.addTask(returnTask);

        return returnTask;


    }

    public void removeTask() throws SQLException {
        Connection dbConnection = DriverManager.getConnection(UserData.dbAddress, UserData.dbUser, UserData.dbPassword);
        PreparedStatement deletion = dbConnection.prepareStatement("DELETE FROM tasks WHERE taskid=?");
        deletion.setInt(1, taskid);
        deletion.executeUpdate();
    }


    public Task(String name)
    {

    }


    public static Task newTaskPopup(Window window, MainController controller)
    {

        FXMLLoader loader = new FXMLLoader(controller.getClass().getResource("input.fxml"));
        Scene newScene;
        try {
            newScene = new Scene(loader.load());
        } catch (IOException ex) {
            // TODO: Handle Error
            return null;
        }

        Stage inputStage = new Stage();
        inputStage.initOwner(window);
        inputStage.setScene(newScene);
        inputStage.showAndWait();

        Task returnTask;
        returnTask = loader.<InputController>getController().getTask();
        return returnTask;
    }

    // Setters
    public void setName(String name)
    {
        this.name = name;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setDueDate(ZonedDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public int getTaskID()
    {
        return taskid;
    }

    public String getTaskName()
    {
        return name;
    }

    // This is the method that's called to get the cell values
    public String getTaskCategory()
    {
        // If parentID = 0, this isn't meant to have a cateogry
        if (parentid == 0)
        {
            // So return empty string
            return "";
        }
        else // Otherwise
        {
            // Try and get the name of the parent task and return it
            return UserData.getTask(parentid).getTaskName();
        }
    }

    public String getTaskDetails()
    {
        return details;
    }

    public String getDueDate()
    {
        if (dueDate == null)
            return "None";
        return dueDate.toString();
    }

    public ZonedDateTime getDueDateDate()
    {
        return dueDate;
    }


    @Override
    public String toString()
    {
        return name;
    }
}
