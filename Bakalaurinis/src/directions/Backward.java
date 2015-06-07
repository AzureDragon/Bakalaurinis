/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package directions;

import java.util.List;
import org.zu.ardulink.Link;
import org.zu.ardulink.protocol.IProtocol;

/**
 *
 * @author AzureDragon
 */
public class Backward extends Direction implements Directions {
    
    public Backward(DirectionMap path, Link link) {
        super(path, link);
    }
    
    @Override
    public void moveTo() {
       // controllerLink.
  
    }
}
