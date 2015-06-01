/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalaurinis;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author AzureDragon
 */
public class Bakalaurinis extends Application implements WebcamMotionListener {

    private Stage currentStage;

    public Stage getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    @Override
    public void start(Stage stage) throws Exception {
        currentStage = stage;
        System.setProperty("file.encoding", "UTF-8");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI.fxml"));

        Parent root = (Parent) loader.load();
        GUIController gui = (GUIController) loader.getController();

        gui.setStage(currentStage);
        
        setProgramKillOperators();
        prepareScene(root);
        currentStage.addEventHandler(KeyEvent.ANY, gui.keyListener);
//currentStage.getScene().setOnKeyTyped(gui.keyListener);//.setOnKeyPressed(gui.keyListener);
//        WebcamMotionDetector detector = new WebcamMotionDetector(Webcam.getDefault());
     //   detector.setInterval(100); // one check per 100 ms
     //   detector.addMotionListener(this);
     //   detector.start();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */   
    public static void main(String[] args) {
        launch(args);
    }

    private void prepareScene(Parent root) {
        Scene scene = new Scene(root);
       // scene.getStylesheets().add("lt/dekbera/mobileClient/styleSheets/MobileClient.css");
        currentStage.setScene(scene);
        currentStage.show();
    }

    private void setProgramKillOperators() {
        currentStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    @Override
    public void motionDetected(WebcamMotionEvent wme) {
        System.out.println("Detected motion I, alarm turn on you have");
    }
}
