package sample;

import java.sql.Date;
import java.util.LinkedList;

public class UserData {

   private static int id;
   private static String username;

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

    public static void addTask(Task task) {
        allTasks.add(task);
    }


}
