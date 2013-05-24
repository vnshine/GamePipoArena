/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extension.pipoarena.room;

import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.Zone;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import java.util.List;
import log.Debug;

/**
 *
 * @author Vnshine
 */
public class OnUserQuickPlayHandler extends BaseClientRequestHandler {

    @Override
    public void handleClientRequest(User user, ISFSObject params) {
        PipoarenaRoomExtension ext = (PipoarenaRoomExtension) getParentExtension();
        List<Room> listRoom = ext.getParentZone().getRoomList();
        String listRoomName = null;
        Zone zone = ext.getParentZone();
        Room lobbyRoom = zone.getRoomByName("The Lobby");
        listRoom.remove(lobbyRoom);
        for (Room room : listRoom) {
            listRoomName = room.getName();
            if (!listRoomName.equals("The Lobby")) {
                Debug.d("quick join level :" + room.getGroupId() + " name :" + listRoomName);
                user.setProperty("group_names", room.getGroupId());
            }
            break;
        }
        ISFSObject res = new SFSObject();
        res.putUtfString("list_room", listRoomName);
        ext.send("send_quick_room", res, user);
    }
}
