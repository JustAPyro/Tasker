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

    public void register()
    {
        String desiredUser = usernameInput.getText();
        String desiredPassword = passwordInput.getText();

        try {
            Statement stmt = dbConnection.createStatement();
            ResultSet currentUsers = stmt.executeQuery("SELECT username FROM users");
            Boolean validUser = true;
            while (currentUsers.next()) {
                System.out.println(currentUsers.getString(1));
                if (currentUsers.getString(1).equalsIgnoreCase(desiredUser)) {
                    validUser = false;
                }
            }
            // If the name they picked is invalid,
            if (validUser == false) {
                // Create an alert dialog and let them know that this username is invalid and to pick a new one
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("INVALID USERNAME");
                alert.setHeaderText(null);
                alert.setContentText("This username was already taken, please choose a new one");
                alert.showAndWait();
                return;
            }
            else // Otherwise, if it's valid, let's create a new user in our database!
            {
               stmt.executeUpdate("INSERT INTO users(id, username, password) VALUES (0,'" + desiredUser +"','"+ desiredPassword +"')");
            }

            System.out.println("Yay, you have a new user!");



        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }
}