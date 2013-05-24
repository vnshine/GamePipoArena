/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extension.pipoarena.zone;

import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import extension.pipoarena.DataBase.Database;
import extension.pipoarena.DataBase.UserData;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Vnshine
 */
public class ListTableInfoHandler extends BaseClientRequestHandler {

    @Override
    public void handleClientRequest(User user, ISFSObject param) {
        String groupName = param.getUtfString("group_name");
        PipoarenaZoneExtension ext = (PipoarenaZoneExtension) getParentExtension();
        Response response = ext.getResponse();
        List<Room> listroom = ext.getParentZone().getRoomListFromGroup(groupName);
        List<Integer> numberplayerinroom = new LinkedList<Integer>();
        List<String> listRoomName = new LinkedList<String>();
        Database database = ext.getDatabase();
        UserData userData = database.getUserInfo(user.getName());
        user.setProperty("group_names", groupName);
        for (Room r : listroom) {
            listRoomName.add(r.getName());
            Integer numberPlayers = r.getUserList().size();
            if (numberPlayers == null) {
                numberplayerinroom.add(0);
            } else {
                numberplayerinroom.add(numberPlayers);
            }
        }
        response.sendListTable(numberplayerinroom, listRoomName, groupName,userData.getMoney(),userData.getLevel(),user);
    }
}
