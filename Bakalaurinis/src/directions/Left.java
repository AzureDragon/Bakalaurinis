/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package directions;

import org.zu.ardulink.Link;

/**
 *
 * @author AzureDragon
 */
public class Left extends Direction implements Directions {
    
    public Left(DirectionMap path, Link link) {
        super(path, link);
    }
    
    @Override
    public void moveTo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
