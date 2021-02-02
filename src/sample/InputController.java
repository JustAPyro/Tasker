package sample;

import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.ZoneId;
import java.util.Date;

public class InputController
{

    public Button submit;
    public TextField taskName;
    public TextField taskDetails;
    public DatePicker taskDue;

    public Task getTask() {
        Task returnTask = new Task();
        returnTask.setName(taskName.getText());
        returnTask.setDetails(taskDetails.getText());
        try {
            returnTask.setDueDate(Date.from(taskDue.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        } catch (NullPointerException e) {

        }
        return returnTask;

    }

    public void submitButton() {

        Stage inputWindow = (Stage) submit.getScene().getWindow();
        inputWindow.close();

    }



}
