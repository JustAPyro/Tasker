package sample;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class loginController implements Initializable
{
    public Button loginButton;
    public Button registerButton;

    public TextField usernameInput;
    public PasswordField passwordInput;

    public Connection dbConnection;

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
    }

    public void login()
    {


        System.out.println("Created connection");

    }

    private Boolean checkPassword(String desiredPassword)
    { // Simple method to check the validy of a password
        if (desiredPassword == null || desiredPassword.length() == 0)
        { // If they didn't enter a password tell them they have to
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INVALID PASSWORD");
            alert.setHeaderText(null);
            alert.setContentText("You must enter a password.");
            alert.showAndWait();
            return false;
        }
        if (desiredPassword.length() < 8)
        { // If the desired password is fewer than 8 characters
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INVALID PASSWORD");
            alert.setHeaderText(null);
            alert.setContentText("Your password must be at least 8 characters long.");
            alert.showAndWait();
            return false;
        }
        Boolean num = false, letter = false; // Flags for a letter or number
        for (int i = 0; i < desiredPassword.length(); i++)
        { // Checks each character of the desired password
            if (Character.isDigit(desiredPassword.charAt(i)))
                num = true;
            if (Character.isLetter(desiredPassword.charAt(i)))
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

    }

    public void register()
    {
        String desiredUser = usernameInput.getText();
        String desiredPassword = passwordInput.getText();

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

            Boolean passwordIsValid = checkPassword(desiredPassword);
            if (passwordIsValid == false)
            { // If the password isn't valid
                return; // Break the loop and wait for them to give us better values
            }


            PreparedStatement pstmt = dbConnection.prepareStatement("INSERT INTO users (id, username, password) VALUES (0, ?, ?)");
            pstmt.setString(1, desiredUser);
            pstmt.setString(2, desiredPassword);
            pstmt.executeUpdate();

            System.out.println("Yay, you have a new user!");



        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }
}