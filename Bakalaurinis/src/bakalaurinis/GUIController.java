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
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import directions.Backward;
import directions.Directions;
import directions.Directions.DirectionMap;
import directions.Forward;
import directions.Left;
import directions.Right;
import java.awt.Color;
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
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingFXUtils;
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
import javax.swing.JFrame;
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
    @FXML
    Pane panelCamera;
    ObservableList<String> connectedDeviceList = FXCollections.observableArrayList();
    ObservableList<String> boudRateList = FXCollections.observableArrayList();
    private ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(10);
    private String DEFAULT_CHOOSEN_DEVICE = "DEVICE";
    public Link connectionLink = Link.getDefaultInstance();
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
    private RunnableDemo R2 = null;
    private String cameraListPromptText = "Choose Camera";
    private Webcam webCam = null;
    private boolean stopCamera = false;
    private BufferedImage grabbedImage;
    private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<Image>();
    private Backward backwardC = null;
    private Forward forwardC = null;
    private Left leftC = null;
    private Right rightC = null;
    private Boolean checkBlue = Boolean.TRUE;
    private Boolean checkGreen = Boolean.TRUE;
    Boolean working = Boolean.FALSE;

    /**
     * Initializes the controller class.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    void addCamera(Camera camera) {
        cameraList.add(camera);
        ObservableList<String> cameraObservableList = FXCollections.observableArrayList();
        for (Camera cameraName : cameraList) {
            cameraObservableList.addAll(cameraName.getCameraName());
        }
        cameraChoice.setItems(cameraObservableList);
        cameraChoice.getSelectionModel().select(cameraObservableList.get(cameraObservableList.size() - 1));
    }

    private void generateOrderMap() {
        ObservableList<ColorRectCell> data = FXCollections.observableArrayList();
        orderListMap.add(new ArrayList<Order>());
        orderList.setItems(data);

        for (int i = 0; i < 8; i++) {
            orderListMap.get(chooseOrderList.getSelectionModel().getSelectedIndex()).add(new Order());
            orderList.getItems().add(new ColorRectCell(orderListMap.get(chooseOrderList.getSelectionModel().getSelectedIndex()).get(orderListMap.get(chooseOrderList.getSelectionModel().getSelectedIndex()).size() - 1)).updateItem());
        }
    }

    private void start(int index) {
        if (!working) {
            working = Boolean.TRUE;
            log.setText(log.getText() + "[" + sdf.format(Calendar.getInstance().getTime()) + "] Order procceding starting on " + chooseOrderList.getItems().get(index) + "\n");
            R2 = new RunnableDemo(orderListMap.get(index));

            R2.start();
        }
    }

    private class WebCamInfo {

        private String webCamName;
        private int webCamIndex;

        public String getWebCamName() {
            return webCamName;
        }

        public void setWebCamName(String webCamName) {
            this.webCamName = webCamName;
        }

        public int getWebCamIndex() {
            return webCamIndex;
        }

        public void setWebCamIndex(int webCamIndex) {
            this.webCamIndex = webCamIndex;
        }

        @Override
        public String toString() {
            return webCamName;
        }
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


        ObservableList<String> deviceList = FXCollections.observableArrayList();
        String[] portNames = SerialPortList.getPortNames();
        if (!deviceList.contains(DEFAULT_CHOOSEN_DEVICE)) {
            deviceList.add(DEFAULT_CHOOSEN_DEVICE);
        }

        orderListMap = new ArrayList<>();

        ObservableList list = FXCollections.observableArrayList();
        cameraChoice.setItems(list);
        cameraChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                initializeWebCam(Webcam.getWebcamByName(cameraList.get(cameraChoice.getSelectionModel().getSelectedIndex()).getPortName()));
            }
        });




        robotChoice.setItems(list);

        ObservableList list3 = FXCollections.observableArrayList();
        list3.add("orderMap");
        chooseOrderList.setItems(list3);
        chooseOrderList.getSelectionModel().select(0);

        removeRobot.setGraphic(reformatPictureSize(remove));
        removeCamera.setGraphic(reformatPictureSize(remove));
        generateOrderMap();

//        chooseOrderList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
//                orderList.getItems().removeAll(orderList.getItems());
//                ObservableList<ColorRectCell> data = FXCollections.observableArrayList();
//                orderList.setItems(data);
//
//                for (int i = 0; i < 30; i++) {
//                    orderList.getItems().add(new ColorRectCell(orderListMap.get(chooseOrderList.getSelectionModel().getSelectedIndex()).get(orderListMap.get(chooseOrderList.getSelectionModel().getSelectedIndex()).size() - 1)).updateItem());
//                }
//            }
//        });

    }

    public class ColorRectCell extends ListCell {

        private Order order;

        public ColorRectCell(Order controller) {
            this.order = controller;
            controller.setCell(this);
        }

        public Pane updateItem() {
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


            leftButton.setGraphic(order.getMoveDirections().contains(DirectionMap.LEFT) ? reformatPictureSize(leftOn) : reformatPictureSize(leftOff));
            forwardButton.setGraphic(order.getMoveDirections().contains(DirectionMap.FORWARD) ? reformatPictureSize(forwardOn) : reformatPictureSize(forwardOff));
            backwardButton.setGraphic(order.getMoveDirections().contains(DirectionMap.BACKWARD) ? reformatPictureSize(backwardOn) : reformatPictureSize(backwardOff));
            rightButton.setGraphic(order.getMoveDirections().contains(DirectionMap.RIGHT) ? reformatPictureSize(rightOn) : reformatPictureSize(rightOff));
            enableButton.setGraphic(order.getActive() ? reformatPictureSize(accept) : reformatPictureSize(disable));
            removeButton.setGraphic(reformatPictureSize(remove));
            estimatedTime.setPrefColumnCount(4);
            estimatedTime.setText(Integer.toString(order.getDuration()));

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
                        rightButton.setGraphic(reformatPictureSize(rightOff));
                        order.getMoveDirections().remove(DirectionMap.RIGHT);
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
                        order.getMoveDirections().remove(DirectionMap.BACKWARD);
                        backwardButton.setGraphic(reformatPictureSize(backwardOff));
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
                        forwardButton.setGraphic(reformatPictureSize(forwardOff));
                        order.getMoveDirections().remove(DirectionMap.FORWARD);
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
                        leftButton.setGraphic(reformatPictureSize(leftOff));
                        order.getMoveDirections().remove(DirectionMap.LEFT);
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
                    orderListMap.get(chooseOrderList.getSelectionModel().getSelectedIndex()).remove(order);
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

            return pane;
        }
    }

    @FXML
    private void connectDevice(MouseEvent event) {

        String connectionPort = port.getValue().toString();
        int baudRate = Integer.parseInt(boudRate.getValue().toString());

        if (!connectionPort.equals(DEFAULT_CHOOSEN_DEVICE)) {
            boolean connected = connectionLink.connect(connectionPort, baudRate);

            if (connected) {
                log.setText(log.getText() + "[" + sdf.format(Calendar.getInstance().getTime()) + "] Arduino connected. Connection Status: " + JOptionPane.INFORMATION_MESSAGE + "\n");
                backwardC = new Backward(DirectionMap.BACKWARD, connectionLink);
                forwardC = new Forward(DirectionMap.FORWARD, connectionLink);
                leftC = new Left(DirectionMap.LEFT, connectionLink);
                rightC = new Right(DirectionMap.RIGHT, connectionLink);
            } else {

                log.setText(log.getText() + "[" + sdf.format(Calendar.getInstance().getTime()) + "] Arduino NOT connected. Connection Status: " + JOptionPane.ERROR_MESSAGE + "\n");
            }
        } else {
            log.setText(log.getText() + "[" + sdf.format(Calendar.getInstance().getTime()) + "] Arduino connection can't be established, no device was found!\n");
        }
    }

    @FXML
    private void resetOrder(MouseEvent event) {
        List<Order> order = orderListMap.get(orderList.getSelectionModel().getSelectedIndex());
        orderList.getItems().removeAll(orderList.getItems());
        for (int i = 0; i < order.size(); i++) {
            order.get(i).setDuration(0);
            order.get(i).setActive(Boolean.FALSE);
            order.get(i).setMoveDirections(new ArrayList());

            orderList.getItems().add(new ColorRectCell(orderListMap.get(chooseOrderList.getSelectionModel().getSelectedIndex()).get(orderListMap.get(chooseOrderList.getSelectionModel().getSelectedIndex()).size() - 1)).updateItem());
        }
    }

    @FXML
    private void stopOrder(MouseEvent event) {
        R2.kill();
        R2 = null;
    }

    @FXML
    private void startOrder(MouseEvent event) {
        start(chooseOrderList.getSelectionModel().getSelectedIndex());
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

    class RunnableDemo implements Runnable {

        private Thread t;
        private List<Order> orders;

        RunnableDemo(List<Order> orders) {
            this.orders = orders;
        }

        @Override
        public void run() {
            System.out.println("i am alive");
            try {
                //for(int k = 0; k < orders.getRepeatCounter(); k++)
                for (int i = 0; i < orders.size(); i++) {
                    if (orders.get(i).getActive()) {
                        for (int j = 0; j < orders.get(i).getDuration(); j++) {
                            if (orders.get(i) != null) {
                                scheduledPool.execute(new moveRobot(orders.get(i).getMoveDirections()));
                            }
                            Thread.sleep(1000);
                        }
                        moveRobotRight(Boolean.FALSE);
                        moveRobotLeft(Boolean.FALSE);
                        moveRobotForward(Boolean.FALSE);
                        moveRobotBackward(Boolean.FALSE);
                    }
                }
                R2 = null;
                checkBlue = Boolean.TRUE;
                checkGreen = Boolean.TRUE;
                working = Boolean.FALSE;
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
                R2 = null;
            }
        }
    }

    @FXML
    private void addOrderMap(MouseEvent event) {
        String orderMapName = "orderMap";
        Integer k = 1;
        Boolean t = Boolean.TRUE;
        String tmpName = orderMapName;
        orderListMap.add(new ArrayList<Order>());
        while (t) {
            if (chooseOrderList.getItems().contains(tmpName)) {
                tmpName = orderMapName + k;
                k++;
            } else {
                t = Boolean.FALSE;
                chooseOrderList.getItems().add(tmpName);
                generateOrderMap();
                chooseOrderList.getSelectionModel().selectLast();


            }
        }
    }

    @FXML
    private void removeOrderMap(MouseEvent event) {
        orderListMap.remove(orderListMap.get(chooseOrderList.getSelectionModel().getSelectedIndex()));
        orderList.getItems().removeAll(orderList.getItems());
        chooseOrderList.getItems().remove(chooseOrderList.getSelectionModel().getSelectedIndex());
        chooseOrderList.getSelectionModel().selectLast();
        if (chooseOrderList.getItems().size() > 0) {
            for (int i = 0; i < orderListMap.get(chooseOrderList.getSelectionModel().getSelectedIndex()).size(); i++) {
                orderList.getItems().add(orderListMap.get(chooseOrderList.getSelectionModel().getSelectedIndex()).get(i).getCell());
            }
        }
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
        stage.show();
    }

    @FXML
    private void addCameraAgent(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("cameraForm.fxml"));
        Stage stage = new Stage();
        CameraController controller = new CameraController(this, stage);
        loader.setController(controller);
        Parent root = (Parent) loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("New Camera Form");
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    private void removeRobotAgent(MouseEvent event) {
        robotList.remove(robotChoice.getSelectionModel().getSelectedIndex());
        robotChoice.getItems().remove(robotChoice.getSelectionModel().getSelectedIndex());
        robotChoice.getSelectionModel().selectLast();
    }

    @FXML
    private void removeCameraAgent(MouseEvent event) {
        cameraList.remove(cameraChoice.getSelectionModel().getSelectedIndex());
        cameraChoice.getItems().remove(cameraChoice.getSelectionModel().getSelectedIndex());
        cameraChoice.getSelectionModel().selectLast();
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
        for (int i = deviceList.size() - 1; i >= 0; i--) {
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
            moveRobotForward(map.contains(DirectionMap.FORWARD) ? Boolean.TRUE : Boolean.FALSE);
            moveRobotBackward(map.contains(DirectionMap.BACKWARD) ? Boolean.TRUE : Boolean.FALSE);
            moveRobotRight(map.contains(DirectionMap.RIGHT) ? Boolean.TRUE : Boolean.FALSE);
            moveRobotLeft(map.contains(DirectionMap.LEFT) ? Boolean.TRUE : Boolean.FALSE);

            if (map.size() > 0) {
                log.setText(log.getText() + "[" + sdf.format(Calendar.getInstance().getTime()) + "] Robot " + robotList.get(robotChoice.getSelectionModel().getSelectedIndex()).getName() + " is moving to " + printMapOrders(map) + "\n");
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
            if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN
                    || event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT || (event.getCode() == KeyCode.UP && event.getCode() == KeyCode.RIGHT)) {
                try {
                    switch (event.getCode()) {
                        case UP:
                            if (event.getEventType().equals(KeyEvent.KEY_RELEASED)) {
                                moveRobotForward(Boolean.FALSE);
                            } else {
                                moveRobotForward(Boolean.TRUE);
                            }

                            break;
                        case LEFT:
                            if (event.getEventType().equals(KeyEvent.KEY_RELEASED)) {
                                moveRobotLeft(Boolean.FALSE);
                            } else {
                                moveRobotLeft(Boolean.TRUE);
                            }
                            break;

                        case DOWN:
                            if (event.getEventType().equals(KeyEvent.KEY_RELEASED)) {
                                moveRobotBackward(Boolean.FALSE);
                            } else {
                                moveRobotBackward(Boolean.TRUE);
                            }
                            break;
                        case RIGHT:
                            if (event.getEventType().equals(KeyEvent.KEY_RELEASED)) {
                                moveRobotRight(Boolean.FALSE);
                            } else {
                                moveRobotRight(Boolean.TRUE);
                            }
                            break;
                    }
                    event.consume();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                event.consume();
            }
        }
    };

    public void moveRobotForward(Boolean event) {
        if (!event) {
            forward.setImage(new Image(forwardOff));
            if (robotChoice.getItems().size() > 0) {
                forwardC.stopTo(robotList.get(chooseActiveRobot.getSelectionModel().getSelectedIndex()).getForwardPin());
            }
        } else {
            forward.setImage(new Image(forwardOn));
            if (robotChoice.getItems().size() > 0) {
                forwardC.moveTo(robotList.get(chooseActiveRobot.getSelectionModel().getSelectedIndex()).getForwardPin());
            }
        }
    }

    public void moveRobotLeft(Boolean event) {
        if (!event) {
            left.setImage(new Image(leftOff));
            if (robotChoice.getItems().size() > 0) {
                leftC.stopTo(robotList.get(chooseActiveRobot.getSelectionModel().getSelectedIndex()).getLeftPin());
            }
        } else {
            left.setImage(new Image(leftOn));
            if (robotChoice.getItems().size() > 0) {
                leftC.moveTo(robotList.get(chooseActiveRobot.getSelectionModel().getSelectedIndex()).getLeftPin());
            }
        }
    }

    public void moveRobotBackward(Boolean event) {
        if (!event) {
            backward.setImage(new Image(backwardOff));
            if (robotChoice.getItems().size() > 0) {
                backwardC.stopTo(robotList.get(chooseActiveRobot.getSelectionModel().getSelectedIndex()).getBackwardPin());
            }
        } else {
            backward.setImage(new Image(backwardOn));
            if (robotChoice.getItems().size() > 0) {
                backwardC.moveTo(robotList.get(chooseActiveRobot.getSelectionModel().getSelectedIndex()).getBackwardPin());
            }
        }
    }

    public void moveRobotRight(Boolean event) {
        if (!event) {
            right.setImage(new Image(rightOff));
            if (robotChoice.getItems().size() > 0) {
                rightC.stopTo(robotList.get(chooseActiveRobot.getSelectionModel().getSelectedIndex()).getRightPin());
            }
        } else {
            right.setImage(new Image(rightOn));
            if (robotChoice.getItems().size() > 0) {
                rightC.moveTo(robotList.get(chooseActiveRobot.getSelectionModel().getSelectedIndex()).getRightPin());
            }
        }
    }

    protected void startWebCamStream() {

        stopCamera = false;

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                while (!stopCamera) {
                    try {
                        if ((grabbedImage = webCam.getImage()) != null) {

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    checkGrabbedImage(grabbedImage);
                                    Image mainiamge = SwingFXUtils.toFXImage(grabbedImage, null);
                                    imageProperty.set(mainiamge);
                                }

                                private void checkGrabbedImage(BufferedImage grabbedImage) {
                                    int green = 0;
                                    int blue = 0;
                                    for (int x = 0; x < grabbedImage.getWidth(); x++) {
                                        for (int y = 0; y < grabbedImage.getHeight(); y++) {
                                            int rgb = grabbedImage.getRGB(x, y);
                                            float hsb[] = new float[3];
                                            int r = (rgb >> 16) & 0xFF;
                                            int g = (rgb >> 8) & 0xFF;
                                            int b = (rgb) & 0xFF;
                                            Color.RGBtoHSB(r, g, b, hsb);

                                            if (hsb[1] < 0.1 && hsb[2] > 0.9) {
                                                //  System.out.println("WHITE");
                                            } else if (hsb[2] < 0.1) {
                                                //    System.out.println("BLACK");
                                            } else {
                                                float deg = hsb[0] * 360;
                                                if (deg >= 0 && deg < 30) {
                                                    // System.out.println("RED");
                                                } else if (deg >= 30 && deg < 90) {
                                                    // System.out.println("YELLOW");
                                                } else if (deg >= 90 && deg < 150) {
                                                    green++;
                                                    // System.out.println("GREEN");
                                                } else if (deg >= 150 && deg < 210) {
                                                    //  System.out.println("CYAN");
                                                } else if (deg >= 210 && deg < 270) {
                                                    // System.out.println("BLUE");
                                                    blue++;
                                                } else if (deg >= 270 && deg < 330) {
                                                    // System.out.println("MAGENTA");
                                                } else {
                                                    //  System.out.println("RED");
                                                }
                                            }
                                        }
                                    }
                                    if (((double) green / (grabbedImage.getWidth() * grabbedImage.getHeight())) >= 0.2 && checkGreen) {
                                        System.out.println("total green pixels: " + green + "/" + grabbedImage.getWidth() * grabbedImage.getHeight());
                                        checkGreen = Boolean.FALSE;
                                        start(0);
                                    }
                                    if (((double) blue / (grabbedImage.getWidth() * grabbedImage.getHeight())) >= 0.6 && checkBlue) {
                                        System.out.println("total blue pixels: " + blue + "/" + grabbedImage.getWidth() * grabbedImage.getHeight());
                                        checkBlue = Boolean.FALSE;
                                        start(1);
                                    }
                                }
                            });

                            grabbedImage.flush();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                return null;
            }
        };

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
        camera.imageProperty().bind(imageProperty);

    }

    protected void initializeWebCam(final Webcam cam) {

        Task<Void> webCamTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                if (webCam != null) {
                    disposeWebCamCamera();
                }
                webCam = cam;
                webCam.open();
                startWebCamStream();

                return null;
            }
        };

        Thread webCamThread = new Thread(webCamTask);
        webCamThread.setDaemon(true);
        webCamThread.start();
    }

    protected void disposeWebCamCamera() {
        stopCamera = true;
        webCam.close();
    }

    public static List<List<Order>> getOrderListMap() {
        return orderListMap;
    }

    public static void setOrderListMap(List<List<Order>> orderListMap) {
        GUIController.orderListMap = orderListMap;
    }

    public static List<Robot> getRobotList() {
        return robotList;
    }

    public static void setRobotList(List<Robot> robotList) {
        GUIController.robotList = robotList;
    }

    public static List<Camera> getCameraList() {
        return cameraList;
    }

    public static void setCameraList(List<Camera> cameraList) {
        GUIController.cameraList = cameraList;
    }
}
