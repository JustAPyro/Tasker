package sample;

import com.sun.org.apache.xml.internal.security.signature.ReferenceNotInitializedException;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.ResourceBundle;

public class InputController implements Initializable
{

    public Button submit;
    public TextField taskName;
    public TextField taskDetails;
    public DatePicker taskDue;
    public ChoiceBox timedropdown;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        timedropdown.getItems().clear();
        timedropdown.getItems().addAll("1 AM", "2 AM", "3 AM", "4 AM", "5 AM", "6 AM", "7 AM", "8 AM", "9 AM", "10 AM", "11 AM", "12 AM", "1 PM", "2 PM", "3 PM", "4 PM", "5 PM", "6 PM", "7 PM",  "8 PM", "9 PM", "10 PM", "11 PM", "12 PM");
        timedropdown.getSelectionModel().select("12 PM");
    }


    public Task getTask() { /*
        Task returnTask = new Task();
        returnTask.setName(taskName.getText());
        returnTask.setDetails(taskDetails.getText());
        LocalDate date = taskDue.getValue();
        returnTask.setDueDate(LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 00, 00));
        return returnTask; */

        return generateTask();

    }

    private int getDropDownTime()
    {
        if (timedropdown.getValue().equals("12 AM"))
            return 0;
        else if (timedropdown.getValue().equals("1 AM"))
            return 1;
        else if (timedropdown.getValue().equals("2 AM"))
            return 2;
        else if (timedropdown.getValue().equals("3 AM"))
            return 3;
        else if (timedropdown.getValue().equals("4 AM"))
            return 4;
        else if (timedropdown.getValue().equals("5 AM"))
            return 5;
        else if (timedropdown.getValue().equals("6 AM"))
            return 6;
        else if (timedropdown.getValue().equals("7 AM"))
            return 7;
        else if (timedropdown.getValue().equals("8 AM"))
            return 8;
        else if (timedropdown.getValue().equals("9 AM"))
            return 9;
        else if (timedropdown.getValue().equals("10 AM"))
            return 10;
        else if (timedropdown.getValue().equals("11 AM"))
            return 11;
        else if (timedropdown.getValue().equals("12 PM"))
            return 12;
        else if (timedropdown.getValue().equals("1 PM"))
            return 13;
        else if (timedropdown.getValue().equals("2 PM"))
            return 14;
        else if (timedropdown.getValue().equals("3 PM"))
            return 15;
        else if (timedropdown.getValue().equals("4 PM"))
            return 16;
        else if (timedropdown.getValue().equals("5 PM"))
            return 17;
        else if (timedropdown.getValue().equals("6 PM"))
            return 18;
        else if (timedropdown.getValue().equals("7 PM"))
            return 19;
        else if (timedropdown.getValue().equals("8 PM"))
            return 20;
        else if (timedropdown.getValue().equals("9 PM"))
            return 21;
        else if (timedropdown.getValue().equals("10 PM"))
            return 22;
        else if (timedropdown.getValue().equals("11 PM"))
            return 23;
        return 0;
    }

    private Task generateTask()
    {
        try {

            if (taskDue.getValue() != null && taskName.getText() != null && taskDetails.getText() != null)
            {
                LocalDateTime local = taskDue.getValue().atTime(getDropDownTime(), 0);
                ZonedDateTime zonedDueDate = ZonedDateTime.of(local, ZoneId.of("America/New_York"));
                return new Task(taskName.getText(), taskDetails.getText(), zonedDueDate);
            }
            return new Task(taskName.getText(), taskDetails.getText());
        }
        catch (ReferenceNotInitializedException e)
        {

        }
        catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }
        return null;
    }

    public void submitButton() {

        Stage inputWindow = (Stage) submit.getScene().getWindow();
        inputWindow.close();

    }



}
