package sample;

public class UserData {

   private static int id;
   private static String username;

    public static void setid(int newid, String newusername)
    {
        System.out.println("Setting id");
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


}
