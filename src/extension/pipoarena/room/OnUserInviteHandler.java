/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extension.pipoarena.room;

import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import java.util.List;

/**
 *
 * @author Vnshine
 */
public class OnUserInviteHandler extends BaseClientRequestHandler {

    @Override
    public void handleClientRequest(User user, ISFSObject params) {
        PipoarenaRoomExtension ext = (PipoarenaRoomExtension) getParentExtension();
        List<Room> listRoom = ext.getParentZone().getRoomList();
        String roomName = null;
        String fbinvite = null;
        String fbinvited = null;
        fbinvite = params.getUtfString("fb_id_invite");
        fbinvited = params.getUtfString("fb_id_invited");
        User inviteeUser = null;
        Room roomFound=null;
        for (Room room : listRoom) {
            inviteeUser = room.getUserByName(fbinvited);
            if (inviteeUser != null) {
                roomFound = room;
                break;
            }
        }
        ISFSObject res = new SFSObject();
        user.setProperty("group_names", roomFound.getGroupId());
        res.putUtfString("room_name", roomFound.getName());
        send("play_together", res, user);
    }
}
