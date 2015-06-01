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
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author AzureDragon
 */
public class robotAddController implements Initializable {

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    public AnchorPane createNewRobotController() {
//        AnchorPane anchorPane = new AnchorPane();
//        anchorPane.prefWidth(237.0);
//        anchorPane.prefHeight(400.0);
//
//        Pane pane = new Pane();
//        pane.prefWidth(237.0);
//        pane.prefHeight(400.0);
//
//        VBox vBox1 = new VBox();
//        vBox1.prefWidth(237.0);
//        vBox1.prefHeight(400.0);
//        vBox1.setSpacing(10);
//
//        HBox hBox1 = new HBox();
//        hBox1.prefWidth(237.0);
//        hBox1.prefHeight(31.0);
//        hBox1.setSpacing(10);
//
//        Label robotNameLabel = new Label("Robot Name");
//        robotNameLabel.prefWidth(101.0);
//        robotNameLabel.prefHeight(34.0);
//
//        TextField robotNameField = new TextField();
//        robotNameField.prefWidth(126.0);
//        robotNameField.prefHeight(31.0);
//
//        hBox1.getChildren().addAll(robotNameLabel, robotNameField);
//
//        HBox hBox2 = new HBox();
//        hBox2.prefWidth(237.0);
//        hBox2.prefHeight(31.0);
//        hBox2.setSpacing(10);
//
//        Label leftLabel = new Label("Left");
//        leftLabel.prefWidth(101.0);
//        leftLabel.prefHeight(34.0);
//
//        TextField leftField = new TextField();
//        leftField.prefWidth(126.0);
//        leftField.prefHeight(31.0);
//
//        hBox2.getChildren().addAll(leftLabel, leftField);
//
//        HBox hBox3 = new HBox();
//        hBox3.prefWidth(237.0);
//        hBox3.prefHeight(31.0);
//        hBox3.setSpacing(10);
//
//        Label rightLabel = new Label("right");
//        rightLabel.prefWidth(101.0);
//        rightLabel.prefHeight(34.0);
//
//        TextField rightField = new TextField();
//        rightField.prefWidth(126.0);
//        rightField.prefHeight(31.0);
//
//        hBox3.getChildren().addAll(rightLabel, rightField);
//
//        HBox hBox4 = new HBox();
//        hBox4.prefWidth(237.0);
//        hBox4.prefHeight(31.0);
//        hBox4.setSpacing(10);
//
//        Label forwardLabel = new Label("forward");
//        forwardLabel.prefWidth(101.0);
//        forwardLabel.prefHeight(34.0);
//
//        TextField forwardField = new TextField();
//        forwardField.prefWidth(126.0);
//        forwardField.prefHeight(31.0);
//
//        hBox4.getChildren().addAll(forwardLabel, forwardField);
//
//        HBox hBox5 = new HBox();
//        hBox5.prefWidth(237.0);
//        hBox5.prefHeight(31.0);
//        hBox5.setSpacing(10);
//
//        Label backwardLabel = new Label("backward");
//        backwardLabel.prefWidth(101.0);
//        backwardLabel.prefHeight(34.0);
//
//        TextField backwardField = new TextField();
//        backwardField.prefWidth(126.0);
//        backwardField.prefHeight(31.0);
//
//        hBox5.getChildren().addAll(backwardLabel, backwardField);
//
//        Label information = new Label("To add control of new robot, &#10;fill robot write pin numbers &#10;robot is using in controller");
//
//        Button accept = new Button("Accept");
//        accept.prefHeight(81.0);
//        accept.prefWidth(240.0);
//        accept.setOnAction(new EventHandler<ActionEvent>() {
//            public void handle(ActionEvent event) {
//                System.out.println("Checking robot");
//                if (checkRobotData()) {
//                    gui.addRobot(new Robot(robotName.getText(), Integer.parseInt(backwardPin.getText()), Integer.parseInt(forwardPin.getText()), Integer.parseInt(leftPin.getText()), Integer.parseInt(rightPin.getText())));
//                    stage.close();
//                }
//            }
//        });
//        vBox1.getChildren().addAll(hBox1, hBox2, hBox3, hBox4, hBox5, information, accept);
//        //              <Insets bottom="5.0" left="5.0" right="5.0" top="5.0" />
//
//        //  <Insets bottom="10.0" left="10.0" right="10.0" top="10.0" />
//        return anchorPane;
//    }
}
