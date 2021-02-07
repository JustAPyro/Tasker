package sample;

import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.ResourceBundle;

public class RegisterController implements Initializable
{
    //////////////////////////////////////////////////////
    // - Declaring all my fx:id identified components - //
    //////////////////////////////////////////////////////

    public TextField usernameField;
    public PasswordField passwordField;
    public PasswordField passwordConfirmField;

    public TextField firstNameField;
    public TextField lastNameField;

    public TextField emailField;
    public TextField phoneField;

    public CheckBox checkAgree;

    public Button registerButton;

    private Connection dbConnection; // Connection to the SQL server

    // FXML Init function is called on object creation
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Try to establish connectivity
        try {


            // Attempt to establish connection with database
            dbConnection = DriverManager.getConnection("jdbc:mysql://sql5.freesqldatabase.com:3306/sql5390450", "sql5390450", "y64muxBbiV");


        }
        catch (SQLException sqle)
        {
            // If there's an SQL error let the user know and print stack trace
            invalidEntryPopup("Connection Error", "Warning: Couldn't establish connection to SQL server");
            sqle.printStackTrace(); // TODO: Set this to print to an error file
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    // - Primary purpose of this class; Validates and then registers user with database - //
    ////////////////////////////////////////////////////////////////////////////////////////

    public void registerNewUser()
    {
        // Check to make sure the username is an appropriate length
        if (usernameField.getText().length() > 20 || usernameField.getText().length() < 3)
        {
            invalidEntryPopup("Invalid Username", "Please enter a username between 3 and 20 characters");
            return;
        }

        // Check if both password boxes match
        if (passwordField.getText().equals(passwordConfirmField.getText()) == false)
        {
            invalidEntryPopup("Password Mismatch", "The two passwords entered do not match");
            System.out.println(passwordField.getText() + "<- Regular");
            System.out.println(passwordConfirmField.getText() + "<- Confirm");
            return;
        }

        // If they didn't enter a password tell them they have to
        if (passwordField.getText() == null || passwordField.getText().length() == 0)
        {
            invalidEntryPopup("Invalid Password", "You must enter a password");
            return;
        }

        // Checks to make sure the password is at least 8 characters long
        if (passwordField.getText().length() < 8)
        {
            invalidEntryPopup("Invalid Password", "Your password must be at least 8 characters long");
            return;
        }

        // The below block checks to make sure the password has a number, lowercase, and uppercase in it
        Boolean num = false, lowLetter = false, upperLetter = false; // Start assuming false
        for (int i = 0; i < passwordField.getText().length(); i++) // For each letter of the password
        {
            if (Character.isDigit(passwordField.getText().charAt(i)))
                num = true; // If it's a number, set that flag to true

            if (Character.isLowerCase(passwordField.getText().charAt(i)))
                lowLetter = true; // If it's a lowercase letter set that flag to true

            if (Character.isUpperCase(passwordField.getText().charAt(i)))
                upperLetter = true; // If it's a uppercase letter set that flag to true
        }
        // If any of the flags are still false, let the user know this password won't work
        if (num == false || lowLetter == false || upperLetter == false) {
            invalidEntryPopup("Invalid Password", "Your password must have a upper and lowercase letter, and a number");
            return;
        }

        // Checks against the SQL database to make sure the username isn't taken
        try {

            // Prepare a statement to pull all usernames
            PreparedStatement getUsernames = dbConnection.prepareStatement("SELECT username FROM users");

            // Get the results of username query
            ResultSet currentUsers = getUsernames.executeQuery();

            // Iterate through each of the entries in our results
            while (currentUsers.next()) {   // For each username check to see if this equals the new username (Ignoring case)
                if (currentUsers.getString("username").equalsIgnoreCase(usernameField.getText())) {   // If the two are equal, inform the user they made an invalid entry
                    invalidEntryPopup("Username Taken", "Sorry, this username is already in use! Please pick another one.");
                    return; // Return
                }
            }

            // Now that we've checked all of them we can close the results
            getUsernames.close();

        } catch (SQLException sqle) {
            invalidEntryPopup("Connection Error", "Warning: Couldn't establish connection to SQL server");
            sqle.printStackTrace(); // TODO: Set this to print to an error file
            return;
        }


        /////////////////////////////////////////////////////////////////////////////
        // - Done validating input, below is where the actual user is registered - //
        /////////////////////////////////////////////////////////////////////////////


        SecureRandom sr; // Used to generate random salt
        MessageDigest md; // Used to securely hash password

        try // Now try to get the instances of both SecureRandom and MessageDigest
        {
            sr = SecureRandom.getInstance("SHA1PRNG");
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            invalidEntryPopup("Missing Algorithm", "Warning: Couldn't get security algorithms, aborting.");
            e.printStackTrace(); // TODO: Set this to print to an error file
            return;
        }

        // Create a byte array to store our salt in
        byte[] saltShaker = new byte[32];

        // Add random bytes to salt shaker
        sr.nextBytes(saltShaker);

        // Salt our password hash
        md.update(saltShaker);

        // Hash password
        byte[] hashedPass = md.digest(passwordField.getText().getBytes());

        try {
            PreparedStatement insertNewUser = dbConnection.prepareStatement // Create SQL statement to insert new values
                    ("INSERT INTO users (id, username, firstname, lastname, email, phone, password, salt) VALUES (0, ?, ?, ?, ?, ?, ?, ?)");
            insertNewUser.setString(1, usernameField.getText()); // Sets the username
            insertNewUser.setString(2, firstNameField.getText()); // sets the first name
            insertNewUser.setString(3, lastNameField.getText()); // sets the last name
            insertNewUser.setString(4, emailField.getText()); // Sets the email
            insertNewUser.setString(5, phoneField.getText()); // Sets the phone number field
            insertNewUser.setBytes(6, hashedPass); // Set our newly created hashed password
            insertNewUser.setBytes(7, saltShaker); // Store the players salt as well
            insertNewUser.executeUpdate();
            insertNewUser.close();

        } catch (SQLException sqle) {
            invalidEntryPopup("Connection Error", "Warning: Couldn't establish connection to SQL server");
            sqle.printStackTrace(); // TODO: Set this to print to an error file
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Account Created");
        alert.setHeaderText(null);
        alert.setContentText("Account creation successful! You may now sign in!");
        alert.showAndWait();
        registerButton.getScene().getWindow().hide();
    }

    // Simple pop-up for errors that fail validations
    public void invalidEntryPopup(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Entry");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }



}
