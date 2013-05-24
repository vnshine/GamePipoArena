/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extension.pipoarena.zone;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import java.util.List;
import log.Debug;

/**
 *
 * @author Vnshine
 */
public class Response {

    PipoarenaZoneExtension ext;

    public Response(PipoarenaZoneExtension ext) {
        this.ext = ext;
    }

    public void sendListTable(List<Integer> numberPlayerInRooms,List<String> listRoomNames, String groupName,int money,long level, User user) {
        ISFSObject resObj = new SFSObject();
        resObj.putUtfString("group_name", groupName);
        resObj.putIntArray("number_player_in_room", numberPlayerInRooms);
        resObj.putUtfStringArray("list_room_name", listRoomNames);
        resObj.putInt("money", money);
        resObj.putLong("level", level);
        ext.send("list_table", resObj, user);
        Debug.d(resObj.getDump());
    }

    void sendTableInfo(String roomname, List<String> listname, List<Integer> listpos, User user) {
        ISFSObject res = new SFSObject();
        res.putUtfString("room_name", roomname);
        if (listname != null) {
            res.putUtfStringArray("room_player_name", listname);
            ext.send("zone", res, user);
            Debug.d(res.getDump());
        }
    }
}
