package sample;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
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
            dbConnection = DriverManager.getConnection(UserData.dbAddress, UserData.dbUser, UserData.dbPassword);
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


            PreparedStatement pstmt = dbConnection.prepareStatement("SELECT id, username, firstname, password, salt FROM users WHERE username=?");
            pstmt.setString(1, username);
            ResultSet results = pstmt.executeQuery();

            if (!results.next())
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Unknown User");
                alert.setHeaderText(null);
                alert.setContentText("There are no users found with that username. Please consider using the register button if this is your first time, or contacting support if you are having trouble accessing your account.");
                alert.showAndWait();
                return;
            }

            byte[] salt = results.getBytes("salt");

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(results.getBytes("salt"));

            // Save the first name of the user into UserData


            if (Arrays.equals(results.getBytes("password"), (md.digest(passwordInput.getText().getBytes()))))
            {
                // Save the ID for userdata so we don't have to pull it over and over
                UserData.setid(results.getInt("id"));

                // While we're here we might as well also save the user first name
                UserData.setFirstName(results.getString("firstname"));

                FXMLLoader loader = new FXMLLoader(this.getClass().getResource("Main.fxml"));
                Scene newScene;
                newScene = new Scene(loader.load());
                Stage mainStage = new Stage();
                UserData.setActiveStage(mainStage);
                mainStage.setScene(newScene);
                mainStage.setTitle("Tasker");
                mainStage.show();
                loginButton.getScene().getWindow().hide();

            }



        }
        catch (SQLException | NoSuchAlgorithmException | IOException sqle)
        {
            sqle.printStackTrace();
        }


        /*
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("main.fxml"));
        Scene newScene;
        try {
            newScene = new Scene(loader.load());
            Stage inputStage = new Stage();
            inputStage.setScene(newScene);
        } catch (IOException ex) {
            // TODO: Handle Error
        }*/


    } // Handles signing in users

    public void register()
    {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("RegisterScreen.fxml"));
            Scene newScene;
            newScene = new Scene(loader.load());
            Stage mainStage = new Stage();
            UserData.setActiveStage(mainStage);
            mainStage.setScene(newScene);
            mainStage.setTitle("Tasker - Register");
            mainStage.show();
        } catch (IOException e) {
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

