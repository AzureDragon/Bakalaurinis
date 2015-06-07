/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseDataStructures;

/**
 *
 * @author AzureDragon
 */
public class Camera {
    private String cameraName;
    private String portName;

    public Camera(String cameraName, String portName) {
        this.cameraName = cameraName;
        this.portName = portName;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }
    
}
