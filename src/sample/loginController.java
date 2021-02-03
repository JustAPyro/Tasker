package sample;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Arrays;
import java.util.ResourceBundle;

// Handles the log-in screen
public class loginController implements Initializable
{
    public Button loginButton;
    public Button registerButton;

    public TextField usernameInput;
    public PasswordField passwordInput;

    private Connection dbConnection;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            dbConnection = DriverManager.getConnection("jdbc:mysql://sql5.freesqldatabase.com:3306/sql5390450", "sql5390450", "y64muxBbiV");
            //Statement stmt = con.createStatement();
            //ResultSet rs = stmt.executeQuery("SELECT * FROM users");

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    } // Initializes the window

    public void login()
    {

        String username = usernameInput.getText();

        try
        {


            PreparedStatement pstmt = dbConnection.prepareStatement("SELECT username, password, salt FROM users WHERE username=?");
            pstmt.setString(1, username);
            ResultSet passSalt = pstmt.executeQuery();

            if (!passSalt.next())
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Unknown User");
                alert.setHeaderText(null);
                alert.setContentText("There are no users found with that username. Please consider using the register button if this is your first time, or contacting support if you are having trouble accessing your account.");
                alert.showAndWait();
                return;
            }

            byte[] salt = passSalt.getBytes("salt");
            System.out.println("Bytes fetched:");
            for (int i = 0; i < salt.length; i++) {
                System.out.print(salt[i] + " ");
            }

            System.out.println("Salt fetched: " + salt);

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(passSalt.getBytes("salt"));


            if (Arrays.equals(passSalt.getBytes("password"), (md.digest(passwordInput.getText().getBytes())))) {
                System.out.println("LOGGED IN!");
            }

        }
        catch (SQLException | NoSuchAlgorithmException sqle)
        {
            sqle.printStackTrace();
        }

        System.out.println("Created connection");

    } // Handles signing in users

    public void register()
    {
        String desiredUser = usernameInput.getText();

        try {
            Statement stmt = dbConnection.createStatement();
            ResultSet currentUsers = stmt.executeQuery("SELECT username FROM users");

            while (currentUsers.next())
            { // For each user already in the database
                if (currentUsers.getString(1).equalsIgnoreCase(desiredUser))
                { // Check to see if desired username is repeated, ignoring caps
                    // If so, spawn an alert dialog and return, forcing user to pick a new username.
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("INVALID USERNAME");
                    alert.setHeaderText(null);
                    alert.setContentText("This username was already taken, please choose a new one");
                    alert.showAndWait();
                    return;
                }
            }

            Boolean passwordIsValid = checkPassword();
            if (passwordIsValid == false)
            { // If the password isn't valid
                return; // Break the loop and wait for them to give us better values
            }


            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[32];
            sr.nextBytes(salt);

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] hashedPass = md.digest(passwordInput.getText().getBytes());

            System.out.println("Salt uploaded: ");
            for (int i = 0; i < salt.length; i++) {
                System.out.print(salt[i] + " ");
            }
            System.out.println();

            PreparedStatement pstmt = dbConnection.prepareStatement("INSERT INTO users (id, username, password, salt) VALUES (0, ?, ?, ?)");
            pstmt.setString(1, desiredUser);
            pstmt.setBytes(2, hashedPass);
            pstmt.setBytes(3, salt);
            pstmt.executeUpdate();




            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Account Created");
            alert.setHeaderText(null);
            alert.setContentText("Account creation successful! You may now use the log-in button.");
            alert.showAndWait();

            pstmt.close();
            stmt.close();



        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }


    } // Handles registering new users

    private Boolean checkPassword()
    { // Simple method to check the validy of a password
        if (passwordInput.getText() == null || passwordInput.getText().length() == 0)
        { // If they didn't enter a password tell them they have to
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INVALID PASSWORD");
            alert.setHeaderText(null);
            alert.setContentText("You must enter a password.");
            alert.showAndWait();
            return false;
        }
        if (passwordInput.getText().length() < 8)
        { // If the desired password is fewer than 8 characters
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INVALID PASSWORD");
            alert.setHeaderText(null);
            alert.setContentText("Your password must be at least 8 characters long.");
            alert.showAndWait();
            return false;
        }
        Boolean num = false, letter = false; // Flags for a letter or number
        for (int i = 0; i < passwordInput.getText().length(); i++)
        { // Checks each character of the desired password
            if (Character.isDigit(passwordInput.getText().charAt(i)))
                num = true;
            if (Character.isLetter(passwordInput.getText().charAt(i)))
                letter = true;
        }
        if (num != true || letter != true)
        { // If there's not a number or letter
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INVALID PASSWORD");
            alert.setHeaderText(null);
            alert.setContentText("Your password must have a letter and a number");
            alert.showAndWait();
            return false;
        }

        return true;

    } // Checks a password to see if it's valid

}

