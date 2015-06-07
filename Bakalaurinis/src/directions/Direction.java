/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package directions;

import directions.Directions.DirectionMap;
import javafx.scene.control.Button;
import org.zu.ardulink.Link;

/**
 *
 * @author AzureDragon
 */
public abstract class Direction implements Directions{
    private DirectionMap path;
    private Boolean active;
    private Button controller;
    protected Link controllerLink;

    public DirectionMap getPath() {
        return path;
    }

    public void setPath(DirectionMap path) {
        this.path = path;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Direction(DirectionMap path, Link link) {
        this.path = path;
    }

    @Override
    public void moveTo() {
        
    }
    
    
}
