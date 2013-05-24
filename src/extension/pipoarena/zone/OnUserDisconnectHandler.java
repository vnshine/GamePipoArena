/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extension.pipoarena.zone;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import java.util.List;
import log.Debug;

/**
 *
 * @author Vnshine
 */
public class OnUserDisconnectHandler extends BaseServerEventHandler {

    @Override
    public void handleServerEvent(ISFSEvent event) throws SFSException {
        User user = (User) event.getParameter(SFSEventParam.USER);
        String location = (String) user.getProperty("user_location");
        if (location != null) {
            PipoarenaZoneExtension ext = (PipoarenaZoneExtension) getParentExtension();
            Room lobbyRoom = ext.getParentZone().getRoomByName("The Lobby");
            if (location.equals("lobby")) {
                List<User> listUsers = lobbyRoom.getUserList();
                SFSObject resObj = new SFSObject();
                resObj.putUtfString("fb_id", user.getName());
                resObj.putUtfString("fb_name", (String) user.getProperty("fb_name"));
                resObj.putUtfString("avatar", (String) user.getProperty("avatar_img"));
                send("update_list_offline", resObj, listUsers);
                Debug.d(resObj.getDump());
            }
        }
    }
}