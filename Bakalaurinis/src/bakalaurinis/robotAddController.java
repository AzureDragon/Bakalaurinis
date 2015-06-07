/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalaurinis;

import baseDataStructures.Robot;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author AzureDragon
 */
public class robotAddController {

    @FXML
    TextField backwardPin;
    @FXML
    TextField forwardPin;
    @FXML
    TextField leftPin;
    @FXML
    TextField rightPin;
    @FXML
    TextField robotName;
    private static String badField = "No data Found!";
    private GUIController gui;
    private Stage stage;

    robotAddController(GUIController gui, Stage stage) throws IOException {
        this.gui = gui;
        this.stage = stage;
    }

    @FXML
    private void checkAndAddRobot(MouseEvent event) {
        if (checkRobotData()) {
            gui.addRobot(new Robot(robotName.getText(), Integer.parseInt(backwardPin.getText()), Integer.parseInt(forwardPin.getText()), Integer.parseInt(leftPin.getText()), Integer.parseInt(rightPin.getText())));
            stage.close();
        }
    }

    private boolean checkRobotData() {
        Boolean validator = Boolean.TRUE;
        if (!isNumeric(backwardPin.getText()) || backwardPin.getText().isEmpty()) {
            backwardPin.setPromptText(badField);
            validator = Boolean.FALSE;
        }
        if (!isNumeric(forwardPin.getText()) || forwardPin.getText().isEmpty()) {
            forwardPin.setPromptText(badField);
            validator = Boolean.FALSE;
        }
        if (!isNumeric(leftPin.getText()) || leftPin.getText().isEmpty()) {
            leftPin.setPromptText(badField);
            validator = Boolean.FALSE;
        }
        if (!isNumeric(rightPin.getText()) || rightPin.getText().isEmpty()) {
            rightPin.setPromptText(badField);
            validator = Boolean.FALSE;
        }
        if (robotName.getText().isEmpty()) {
            robotName.setPromptText(badField);
            validator = Boolean.FALSE;
        }
        return validator;
    }

    public static boolean isNumeric(String str) {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }
}
