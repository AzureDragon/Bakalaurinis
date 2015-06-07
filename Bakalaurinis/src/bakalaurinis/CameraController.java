/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalaurinis;

import static bakalaurinis.robotAddController.isNumeric;
import baseDataStructures.Camera;
import com.github.sarxos.webcam.Webcam;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author AzureDragon
 */
class CameraController {

    @FXML
    TextField cameraName;
    @FXML
    ChoiceBox cameraDeviceAdd;
    private GUIController gui;
    private Stage stage;
    private ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(10);
    private List<String> possibleDevice = new ArrayList<>();
    private String DEFAULT_CHOOSEN_DEVICE = "CHOOSE";
    ObservableList<String> connectedDeviceList = FXCollections.observableArrayList();
    private static String badField = "No data Found!";

    @FXML
    private void cameraAddEvent(MouseEvent event) {
        if (checkCameraData()) {
            stage.close();
            scheduledPool.shutdownNow();
            gui.addCamera(new Camera(cameraName.getText(), cameraDeviceAdd.getItems().get(cameraDeviceAdd.getSelectionModel().getSelectedIndex()).toString()));

        }
    }

    CameraController(GUIController gui, Stage stage) {
        this.gui = gui;
        this.stage = stage;
        scheduledPool.scheduleAtFixedRate(new DeviceChecker(), 1, 1, TimeUnit.SECONDS);
    }

    private boolean checkCameraData() {
        Boolean validator = Boolean.TRUE;
        if (!(checkCameraName(cameraName.getText()) && !cameraName.getText().isEmpty())) {
            cameraName.setPromptText(badField);
            validator = Boolean.FALSE;
        }
        if (!(checkCameraDevice(cameraDeviceAdd.getSelectionModel().getSelectedItem().toString()) && cameraDeviceAdd.getSelectionModel().getSelectedItem() != null)) {
            validator = Boolean.FALSE;
        }
        return validator;
    }

    private Boolean checkCameraName(String cameraName) {
        List<Camera> cameraList = gui.getCameraList();
        for (Camera camera : cameraList) {
            if (camera.getCameraName().equals(cameraName)) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    private Boolean checkCameraDevice(String cameraDevice) {
        List<Camera> cameraList = gui.getCameraList();
        for (Camera camera : cameraList) {
            if (camera.getCameraName().equals(cameraName)) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    private class DeviceChecker implements Runnable {

        public DeviceChecker() {
        }

        @Override
        public void run() {
            ObservableList<String> checkList = getConnectedDeviceList();
            if (!checkList.equals(connectedDeviceList)) {

                Task<ObservableList> task = new EventListPrepareTask(checkList);
                task.setOnSucceeded(eventListUploadHandler);
                Thread th = new Thread(task);
                th.setDaemon(Boolean.TRUE);
                th.start();
            }
        }
    }

    private ObservableList<String> getConnectedDeviceList() {
        ObservableList<String> deviceList = FXCollections.observableArrayList();
        List<Webcam> webcam = Webcam.getWebcams();
        removeAllDevices(deviceList);
        for (int i = 0; i < webcam.size(); i++) {
            deviceList.add(webcam.get(i).getName());
        }
        return deviceList;
    }

    private void removeAllDevices(ObservableList<String> deviceList) {
        for (int i = deviceList.size() - 1; i >= 0; i--) {
            deviceList.remove(i);
        }
    }
    public final EventHandler<WorkerStateEvent> eventListUploadHandler = new EventHandler<WorkerStateEvent>() {
        @Override
        public void handle(WorkerStateEvent t) {
            ObservableList list = (ObservableList) t.getSource().getValue();
            connectedDeviceList = list;
            cameraDeviceAdd.setItems(connectedDeviceList);
            if (connectedDeviceList.size() > 0) {
                cameraDeviceAdd.setValue(connectedDeviceList.get(0));
            }
        }
    };

    public class EventListPrepareTask extends Task<ObservableList> {

        ObservableList list = null;

        public EventListPrepareTask(ObservableList list) {
            this.list = list;
        }

        @Override
        protected ObservableList call() throws Exception {
            return isNotNull(list) ? list : null;
        }
    };

    private boolean isNotNull(Object object) {
        return object != null;
    }
}
