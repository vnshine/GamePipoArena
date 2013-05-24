/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extension.pipoarena.zone;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

/**
 *
 * @author Vnshine
 */
public class OnJoinLobbyHandler extends BaseServerEventHandler{

    @Override
    public void handleServerEvent(ISFSEvent params) throws SFSException {
    User user = (User) params.getParameter(SFSEventParam.USER);
    Room room = (Room) params.getParameter(SFSEventParam.ROOM);
        if (room.getName().equals("The Lobby")) {
            user.setProperty("user_location", "lobby");
        }
    }
    
}
