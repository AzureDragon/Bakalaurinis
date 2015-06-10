/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package directions;

import org.zu.ardulink.Link;
import org.zu.ardulink.protocol.IProtocol;

/**
 *
 * @author AzureDragon
 */
public class Right extends Direction implements Directions {

    private Link link;

    public Right(DirectionMap path, Link link) {
        super(path, link);
        this.link = link;
    }

        @Override
    public void moveTo(Integer pin) {
        System.out.println(path+" voltage to "+pin+" protocolHIGH");
        try {
        link.sendPowerPinSwitch(pin, IProtocol.HIGH); // 5
        } catch( Exception ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void stopTo(Integer pin) {
        System.out.println(path+" voltage to "+pin+" protocolLOW");
                try {
        link.sendPowerPinSwitch(pin, IProtocol.LOW); // 5
        } catch( Exception ex) {
            System.out.println(ex);
        }
    }
}
