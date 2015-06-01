/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalaurinis;

import baseDataStructures.Camera;
import baseDataStructures.Order;
import baseDataStructures.Robot;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionDetector;
import directions.Directions;
import directions.Directions.DirectionMap;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.swing.JOptionPane;
import jssc.SerialPortList;
import org.zu.ardulink.Link;

/**
 * FXML Controller class
 *
 * @author AzureDragon
 */
public class GUIController implements Initializable {

    @FXML
    Button connect;
    @FXML
    Button disconnect;
    @FXML
    Button start;
    @FXML
    Button stop;
    @FXML
    Button reset;
    @FXML
    CheckBox confirmRepeat;
    @FXML
    TextField numberOfRepeats;
    @FXML
    ChoiceBox chooseOrderList;
    @FXML
    Button addOrderList;
    @FXML
    Button removeOrderList;
    @FXML
    Button removeRobot;
    @FXML
    Button removeCamera;
    @FXML
    ChoiceBox chooseActiveRobot;
    @FXML
    ChoiceBox robotChoice;
    @FXML
    ChoiceBox cameraChoice;
    @FXML
    TextArea log;
    @FXML
    ListView orderList;
    @FXML
    ImageView forward;
    @FXML
    ImageView backward;
    @FXML
    ImageView right;
    @FXML
    ImageView left;
    @FXML
    ChoiceBox port;
    @FXML
    ChoiceBox boudRate;
    @FXML
    ImageView camera;
    @FXML
    Button robotAdd;
    @FXML
    Button cameraAdd;
    ObservableList<String> connectedDeviceList = FXCollections.observableArrayList();
    ObservableList<String> boudRateList = FXCollections.observableArrayList();
    private ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(10);
    private String DEFAULT_CHOOSEN_DEVICE = "DEVICE";
    private Link connectionLink = Link.getDefaultInstance();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static String leftOn = "images/left_on.png";
    private static String leftOff = "images/left.png";
    private static String rightOn = "images/right_on.png";
    private static String rightOff = "images/right.png";
    private static String forwardOn = "images/forward_on.png";
    private static String forwardOff = "images/forward.png";
    private static String backwardOn = "images/backward_on.png";
    private static String backwardOff = "images/backward.png";
    private static String accept = "images/accept.png";
    private static String disable = "images/disable.png";
    private static String remove = "images/remove.png";
    private static String add = "images/add.png";
    private static List<List<Order>> orderListMap = new ArrayList<>();
    private Stage stage;
    private static List<Robot> robotList = new ArrayList<>();
    private static List<Camera> cameraList = new ArrayList<>();
    private RunnableDemo R2;

    /**
     * Initializes the controller class.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
        //  stage.addEventHandler(KeyEvent.ANY, keyListener);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        connectedDeviceList = getConnectedDeviceList();
        boudRateList = getBoudRateList();
        port.setItems(connectedDeviceList);
        boudRate.setItems(boudRateList);

        if (connectedDeviceList.size() > 0) {
            port.setValue(connectedDeviceList.get(0));
        }

        boudRate.setValue(boudRateList.get(4));

        scheduledPool.scheduleAtFixedRate(new InformationRefreshChecker(), 1, 1, TimeUnit.SECONDS);
        camera.setImage(new Image("images/black.bmp"));
        // scheduledPool.scheduleWithFixedDelay(new webcamCapture(), 100, 100, TimeUnit.MILLISECONDS);


        ObservableList<String> deviceList = FXCollections.observableArrayList();
        String[] portNames = SerialPortList.getPortNames();
        if (!deviceList.contains(DEFAULT_CHOOSEN_DEVICE)) {
            deviceList.add(DEFAULT_CHOOSEN_DEVICE);
        }

        orderListMap = new ArrayList<>();


//        WebcamMotionDetector detector = new WebcamMotionDetector(Webcam.getDefault());
//        detector.setInterval(100); // one check per 100 ms
//           //    BufferedImage image = ConverterFactory.convertToType(detector.getWebcam().getImage(), BufferedImage.TYPE_3BYTE_BGR);
//   //     camera.setImage(detector.getWebcam().getImage());
        ObservableList list = FXCollections.observableArrayList();
        list.add("camera1");
        list.add("camera2");
        cameraChoice.setItems(list);

        ObservableList list3 = FXCollections.observableArrayList();
        list3.add("orderMap1");
        list3.add("orderMap1");
        chooseOrderList.setItems(list3);

        removeRobot.setGraphic(reformatPictureSize(remove));
        removeCamera.setGraphic(reformatPictureSize(remove));

        ObservableList<String> data = FXCollections.observableArrayList(
                "chocolate", "salmon", "gold", "coral", "darkorchid",
                "darkgoldenrod", "lightsalmon", "black", "rosybrown", "blue",
                "blueviolet", "brown");
        orderList.setItems(data);

        orderList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> list) {
                orderListMap.add(createNewOrderList());
                for(int i = 0; i < orderListMap.size(); i++)
                    new ColorRectCell(orderListMap.get(orderListMap.size() - 1).get(i));
                return;
            }
        });
    }

    @FXML
    private void connectDevice(MouseEvent event) {

        String connectionPort = port.getValue().toString();
        int baudRate = Integer.parseInt(boudRate.getValue().toString());

        if (!connectionPort.equals(DEFAULT_CHOOSEN_DEVICE)) {
            boolean connected = connectionLink.connect(connectionPort, baudRate);

            if (connected) {
                log.setText(log.getText() + "[" + sdf.format(Calendar.getInstance().getTime()) + "] Arduino connected. Connection Status: " + JOptionPane.INFORMATION_MESSAGE + "\n");
            } else {

                log.setText(log.getText() + "[" + sdf.format(Calendar.getInstance().getTime()) + "] Arduino NOT connected. Connection Status: " + JOptionPane.ERROR_MESSAGE + "\n");
            }
        } else {
            log.setText(log.getText() + "[" + sdf.format(Calendar.getInstance().getTime()) + "] Arduino connection can't be established, no device was found!\n");
        }
    }

    @FXML
    private void resetOrder(MouseEvent event) {
    }

    @FXML
    private void stopOrder(MouseEvent event) {
        R2.kill();
    }

    @FXML
    private void startOrder(MouseEvent event) {

        log.setText(log.getText() + "[" + sdf.format(Calendar.getInstance().getTime()) + "] Order procceding starting on " + chooseOrderList.getSelectionModel().getSelectedItem().toString() + "\n");
        R2 = new RunnableDemo();
        R2.start();
    }

    void addRobot(Robot robot) {
        robotList.add(robot);
        ObservableList robotListObservable = FXCollections.observableArrayList();
        for (int i = 0; i < robotList.size(); i++) {
            robotListObservable.add(robotList.get(i).getName());
        }
        robotChoice.setItems(robotListObservable);
        robotChoice.setValue(robotList.get(robotListObservable.size() - 1).getName());
        chooseActiveRobot.setItems(robotListObservable);
        chooseActiveRobot.setValue(robotList.get(robotListObservable.size() - 1).getName());
    }

    private List<Order> createNewOrderList() {
        List<Order> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add(new Order());
        }
        return list;
    }

    class RunnableDemo implements Runnable {

        private Thread t;

        RunnableDemo() {
        }

        @Override
        public void run() {
            System.out.println("i am alive");
            try {
                for (int i = 0; i < moveList.size(); i++) {
                    if (moveList.get(i).getActive()) {
                        for (int j = 0; j < moveList.get(i).getDuration(); j++) {
                            scheduledPool.execute(new moveRobot(moveList.get(i).getMoveDirections()));
                            Thread.sleep(1000);
                        }
                    }
                }
            } catch (InterruptedException e) {
            }
        }

        public void start() {
            if (t == null) {
                t = new Thread(this, "Thread");
                t.start();
            }
        }

        public void kill() {
            if (t != null) {
                t.stop();
            }
        }
    }

    @FXML
    private void addOrderMap(MouseEvent event) {
    }

    @FXML
    private void removeOrderMap(MouseEvent event) {
    }

    @FXML
    private void addRobotAgent(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("robotForm.fxml"));
        Stage stage = new Stage();
        robotAddController controller = new robotAddController(this, stage);
        loader.setController(controller);
        Parent root = (Parent) loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("New Robot Form");
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    @FXML
    private void addCameraAgent(MouseEvent event) throws IOException {
        stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("cameraForm.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("New camera Form");
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    @FXML
    private void removeRobotAgent(MouseEvent event) {
    }

    @FXML
    private void removeCameraAgent(MouseEvent event) {
    }

    @FXML
    private void disconnectDevice(MouseEvent event) {
        connectionLink.disconnect();
    }

    private ObservableList<String> getConnectedDeviceList() {
        ObservableList<String> deviceList = FXCollections.observableArrayList();
        String[] portNames = SerialPortList.getPortNames();
        if (!deviceList.contains(DEFAULT_CHOOSEN_DEVICE)) {
            deviceList.add(DEFAULT_CHOOSEN_DEVICE);
        }
        removeAllDevices(deviceList);
        for (int i = 0; i < portNames.length; i++) {
            deviceList.add(portNames[i]);
        }
        return deviceList;
    }

    private ObservableList<String> getBoudRateList() {
        ObservableList<String> boudRates = FXCollections.observableArrayList();
        boudRates.addAll("300", "1200", "2400", "4800", "9600", "14400", "19200", "28800", "38400", "57600", "115200", "230400");
        return boudRates;
    }

    private void removeAllDevices(ObservableList<String> deviceList) {
        for (int i = 1; i < deviceList.size(); i++) {
            deviceList.remove(i);
        }
    }

    private class moveRobot implements Runnable {

        ArrayList<DirectionMap> map = null;

        public moveRobot(ArrayList<DirectionMap> moveDirections) {
            map = moveDirections;
        }

        @Override
        public void run() {
            forward.setImage(new Image(map.contains(DirectionMap.FORWARD) ? forwardOn : forwardOff));
            backward.setImage(new Image(map.contains(DirectionMap.BACKWARD) ? backwardOn : backwardOff));
            right.setImage(new Image(map.contains(DirectionMap.RIGHT) ? rightOn : rightOff));
            left.setImage(new Image(map.contains(DirectionMap.LEFT) ? leftOn : leftOff));
            if (map.size() > 0) {
                log.setText(log.getText() + "[" + sdf.format(Calendar.getInstance().getTime()) + "] Robot is moving to " + printMapOrders(map) + "\n");
            } else {
                log.setText(log.getText() + "[" + sdf.format(Calendar.getInstance().getTime()) + "] No Directions was given to Robot!\n");
            }
        }

        private String printMapOrders(ArrayList<DirectionMap> map) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < map.size(); i++) {
                builder.append(map.get(i).name()).append(" ");
            }
            return builder.toString();
        }
    }

    private class InformationRefreshChecker implements Runnable {

        public InformationRefreshChecker() {
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
    public final EventHandler<WorkerStateEvent> eventListUploadHandler = new EventHandler<WorkerStateEvent>() {
        @Override
        public void handle(WorkerStateEvent t) {
            ObservableList list = (ObservableList) t.getSource().getValue();
            connectedDeviceList = list;
            port.setItems(connectedDeviceList);
            if (connectedDeviceList.size() > 0) {
                port.setValue(connectedDeviceList.get(0));
            }
        }
    };

    private boolean isNotNull(Object object) {
        return object != null;
    }

    static class ColorRectCell extends ListCell<String> {

        private Order order;

        public ColorRectCell(Order controller) {
            this.order = controller;
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                Pane pane = new Pane();
                HBox box = new HBox();
                VBox vBox = new VBox();
                final Button leftButton = new Button();
                final Button forwardButton = new Button();
                final Button backwardButton = new Button();
                final Button rightButton = new Button();
                final Button increaseButton = new Button("^");
                final Button decreaseButton = new Button("˅");
                final Button upButton = new Button("^");
                final Button downButton = new Button("˅");
                final TextField estimatedTime = new TextField(Integer.toString(order.getDuration()));
                final Button enableButton = new Button();
                final Button removeButton = new Button();


                leftButton.setGraphic(reformatPictureSize(leftOff));
                forwardButton.setGraphic(reformatPictureSize(forwardOff));
                backwardButton.setGraphic(reformatPictureSize(backwardOff));
                rightButton.setGraphic(reformatPictureSize(rightOff));
                enableButton.setGraphic(reformatPictureSize(disable));
                removeButton.setGraphic(reformatPictureSize(remove));
                estimatedTime.setPrefColumnCount(4);

                upButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        order.setDuration(order.getDuration() + 1);
                        estimatedTime.setText(Integer.toString(order.getDuration()));
                    }
                });
                downButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        order.setDuration(order.getDuration() - 1);
                        estimatedTime.setText(Integer.toString(order.getDuration()));
                    }
                });

                leftButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (order.getMoveDirections().contains(DirectionMap.LEFT)) {
                            leftButton.setGraphic(reformatPictureSize(leftOff));
                            order.getMoveDirections().remove(DirectionMap.LEFT);
                        } else {
                            leftButton.setGraphic(reformatPictureSize(leftOn));
                            order.getMoveDirections().add(DirectionMap.LEFT);
                        }
                    }
                });
                forwardButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (order.getMoveDirections().contains(DirectionMap.FORWARD)) {
                            forwardButton.setGraphic(reformatPictureSize(forwardOff));
                            order.getMoveDirections().remove(DirectionMap.FORWARD);
                        } else {
                            forwardButton.setGraphic(reformatPictureSize(forwardOn));
                            order.getMoveDirections().add(DirectionMap.FORWARD);
                        }
                    }
                });
                backwardButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (order.getMoveDirections().contains(DirectionMap.BACKWARD)) {
                            backwardButton.setGraphic(reformatPictureSize(backwardOff));
                            order.getMoveDirections().remove(DirectionMap.BACKWARD);
                        } else {
                            backwardButton.setGraphic(reformatPictureSize(backwardOn));
                            order.getMoveDirections().add(DirectionMap.BACKWARD);
                        }
                    }
                });
                rightButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (order.getMoveDirections().contains(DirectionMap.RIGHT)) {
                            rightButton.setGraphic(reformatPictureSize(rightOff));
                            order.getMoveDirections().remove(DirectionMap.RIGHT);
                        } else {
                            rightButton.setGraphic(reformatPictureSize(rightOn));
                            order.getMoveDirections().add(DirectionMap.RIGHT);
                        }
                    }
                });
                enableButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (order.getActive()) {
                            enableButton.setGraphic(reformatPictureSize(disable));
                            order.setActive(Boolean.FALSE);
                        } else {
                            enableButton.setGraphic(reformatPictureSize(accept));
                            order.setActive(Boolean.TRUE);
                        }
                    }
                });
                increaseButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        order.setDuration(order.getDuration() + 1);
                        estimatedTime.setText(Integer.toString(order.getDuration()));
                    }
                });
                decreaseButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        order.setDuration(order.getDuration() - 1);
                        estimatedTime.setText(Integer.toString(order.getDuration()));
                    }
                });

                estimatedTime.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                        order.setDuration(Integer.parseInt(estimatedTime.getText()));
                    }
                });
                removeButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        moveList.remove(order);

                    }
                });

                vBox.getChildren().addAll(increaseButton, decreaseButton);
                leftButton.setPrefSize(50, 50);
                forwardButton.setPrefSize(50, 50);
                backwardButton.setPrefSize(50, 50);
                rightButton.setPrefSize(50, 50);
                VBox controller = new VBox();
                controller.getChildren().addAll(upButton, downButton);
                box.getChildren().addAll(controller, leftButton, forwardButton, backwardButton, rightButton, vBox, estimatedTime, enableButton, removeButton);
                box.setSpacing(5);
                pane.getChildren().add(box);
                setGraphic(pane);
            }
        }
    }

    private static ImageView reformatPictureSize(String image) {
        ImageView iv2 = new ImageView();
        iv2.setImage(new Image(image));
        iv2.setFitWidth(25);
        iv2.setPreserveRatio(true);
        iv2.setSmooth(true);
        iv2.setCache(true);
        return iv2;
    }
    public EventHandler<KeyEvent> keyListener = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            System.out.println(event.getEventType());
            // event.getEventType().equals(KeyEvent.KEY_RELEASED);
            if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN
                    || event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT) {
                try {
                    System.out.println(event.getEventType());
                    switch (event.getCode()) {
                        case UP:
                            forward.setImage(new Image(forwardOn));
                            break;
                        case LEFT:
                            left.setImage(new Image(leftOn));
                            break;
                        case DOWN:
                            backward.setImage(new Image(backwardOn));
                            break;
                        case RIGHT:
                            right.setImage(new Image(rightOn));
                            break;
                    }
                    //event.consume();
                    System.out.println(event.getCode());
                    //your code for moving the ship
                } catch (Exception e) {
                    e.printStackTrace();
                }
                event.consume();
            } else if (event.getCode() == KeyCode.SPACE) {
                try {
                    System.out.println(event.getCode());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                event.consume();
            } else {
                System.out.println(event.getCode());
            }
        }
    };
    Task<Integer> task = new Task<Integer>() {
        @Override
        protected Integer call() throws Exception {
            int iterations;
            for (iterations = 0; iterations < 100000; iterations++) {
                if (isCancelled()) {
                    break;
                }
                System.out.println("Iteration " + iterations);
            }
            return iterations;
        }
    };

    private class webcamCapture implements Runnable {

        @Override
        public void run() {

            WebcamMotionDetector detector = new WebcamMotionDetector(Webcam.getDefault());
            BufferedImage bf = detector.getWebcam().getImage();


            WritableImage wr = null;
            if (bf != null) {
                wr = new WritableImage(bf.getWidth(), bf.getHeight());
                PixelWriter pw = wr.getPixelWriter();
                for (int x = 0; x < bf.getWidth(); x++) {
                    for (int y = 0; y < bf.getHeight(); y++) {
                        pw.setArgb(x, y, bf.getRGB(x, y));
                    }
                }
            }

            camera.setImage(wr);
        }
    }
}
