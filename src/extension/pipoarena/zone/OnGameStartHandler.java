/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extension.pipoarena.zone;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

/**
 *
 * @author Vnshine
 */
public class OnGameStartHandler extends BaseClientRequestHandler{

    @Override
    public void handleClientRequest(User user, ISFSObject params) {
//        PipoarenaRoomExtension pipoarenaRoomExtension = (PipoarenaRoomExtension) getParentExtension();
        PipoarenaZoneExtension pipoarenaZoneExtension = (PipoarenaZoneExtension) getParentExtension();
    }
}
