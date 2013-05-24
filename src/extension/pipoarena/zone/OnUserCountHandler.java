/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extension.pipoarena.zone;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

/**
 *
 * @author Vnshine
 */
public class OnUserCountHandler extends BaseServerEventHandler{

    @Override
    public void handleServerEvent(ISFSEvent params) throws SFSException {
        PipoarenaZoneExtension ext = (PipoarenaZoneExtension) getParentExtension();
    }
}
