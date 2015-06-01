/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseDataStructures;

/**
 *
 * @author AzureDragon
 */
public class Robot {
    private String name;
    private Integer backwardPin;
    private Integer forwardPin;
    private Integer leftPin;
    private Integer rightPin;

    public Robot(String name, Integer backwardPin, Integer forwardPin, Integer leftPin, Integer rightPin) {
        this.name = name;
        this.backwardPin = backwardPin;
        this.forwardPin = forwardPin;
        this.leftPin = leftPin;
        this.rightPin = rightPin;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBackwardPin() {
        return backwardPin;
    }

    public void setBackwardPin(Integer backwardPin) {
        this.backwardPin = backwardPin;
    }

    public Integer getForwardPin() {
        return forwardPin;
    }

    public void setForwardPin(Integer forwardPin) {
        this.forwardPin = forwardPin;
    }

    public Integer getLeftPin() {
        return leftPin;
    }

    public void setLeftPin(Integer leftPin) {
        this.leftPin = leftPin;
    }

    public Integer getRightPin() {
        return rightPin;
    }

    public void setRightPin(Integer rightPin) {
        this.rightPin = rightPin;
    }
    
    
}
