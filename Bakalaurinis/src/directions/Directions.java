/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package directions;

import javafx.scene.control.Button;

/**
 *
 * @author AzureDragon
 */
public interface Directions {

    public enum DirectionMap {

        FORWARD,
        RIGHT,
        BACKWARD,
        LEFT
    }

    public abstract void moveTo();
}
