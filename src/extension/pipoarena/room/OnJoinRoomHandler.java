/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extension.pipoarena.room;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Vnshine
 */
public class OnJoinRoomHandler extends BaseServerEventHandler {

    @Override
    public void handleServerEvent(ISFSEvent params) throws SFSException {
        PipoarenaRoomExtension ext = (PipoarenaRoomExtension) getParentExtension();
        User user = (User) params.getParameter(SFSEventParam.USER);
        Room room = (Room) params.getParameter(SFSEventParam.ROOM);
        List<User> users = new LinkedList<User>();
        if (!room.getName().equals("The Lobby")) {
            PipoGame game = ext.getGame();
            game.onPlayerJoin(user);
}
}
}
