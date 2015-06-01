/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package directions;

/**
 *
 * @author AzureDragon
 */
public class Left extends Direction implements Directions {
    
    public Left(DirectionMap path) {
        super(path);
    }
    
    @Override
    public void moveTo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
