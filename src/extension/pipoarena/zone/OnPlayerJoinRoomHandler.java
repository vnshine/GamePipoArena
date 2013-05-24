/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extension.pipoarena.zone;

import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import extension.pipoarena.DataBase.Database;
import extension.pipoarena.DataBase.UserData;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Vnshine
 */
public class OnPlayerJoinRoomHandler extends BaseClientRequestHandler {

    @Override
    public void handleClientRequest(User user, ISFSObject params) {
        PipoarenaZoneExtension ext = (PipoarenaZoneExtension) getParentExtension();
        int Level = 0;
        int Money;
        List<Room> listroom = new LinkedList<Room>();
        List<String> listrom = new LinkedList<String>();
        Database database = ext.getDatabase();
        UserData userData = database.getUserInfo(user.getName());
        Level = userData.getLevel();
        Money = userData.getMoney();
        for (Room r : listroom) {
            listrom.add(r.getName());
        }
        ISFSObject res = new SFSObject();
        res.putUtfStringArray("list_room", listrom);
        res.putInt("player_level", Level);
        res.putInt("player_money", Money);
        ext.send("check_room", res, user);
    }
}
