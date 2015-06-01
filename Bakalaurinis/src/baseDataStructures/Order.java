/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseDataStructures;

import directions.Directions.DirectionMap;
import java.util.ArrayList;

/**
 *
 * @author AzureDragon
 */
public class Order {

    private ArrayList<DirectionMap> moveDirections;
    private Integer duration;
    private Boolean active;

    public Order(ArrayList<DirectionMap> moveDirections, Integer duration, Boolean active) {
        this.moveDirections = moveDirections;
        this.duration = duration;
        this.active = active;
    }

    public Order() {
        this.moveDirections = new ArrayList();
        this.duration = 0;
        this.active = Boolean.FALSE;
    }

    public ArrayList<DirectionMap> getMoveDirections() {
        return moveDirections;
    }

    public void setMoveDirections(ArrayList<DirectionMap> moveDirections) {
        this.moveDirections = moveDirections;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        if(duration >= 0)
            this.duration = duration;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
