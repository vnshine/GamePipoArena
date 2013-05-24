/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extension.pipoarena.room;

import com.smartfoxserver.v2.db.IDBManager;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.Zone;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import log.Debug;

/**
 *
 * @author Vnshine
 */
public class OnUserInfoHandler extends BaseClientRequestHandler {

    private PipoPlayer[] listPlayers;

    @Override
    public void handleClientRequest(User user, ISFSObject params) {
        PipoarenaRoomExtension ext = (PipoarenaRoomExtension) getParentExtension();
        String avatar = null;
        String fbName = null;
        String fbId = null;
        fbId = params.getUtfString("fb_id");
        avatar = params.getUtfString("avatar_img");
        fbName = params.getUtfString("fb_name");
        Debug.d(params.getDump());
        Zone zone = ext.getParentZone();
        Room lobbyRoom = zone.getRoomByName("The Lobby");
        List<Room> listRooms = zone.getRoomList();
        List<User> listInGame = new LinkedList<User>();
        listRooms.remove(lobbyRoom);

        for (Room room : listRooms) {
            if (room.getUserList().size() > 0) {
                listInGame.addAll(room.getUserList());
            }
        }
        IDBManager dbManager = getParentExtension().getParentZone().getDBManager();
        user.setProperty("fb_name", fbName);
        user.setProperty("avatar_img", avatar);
        //update db
        String query = "CALL update_users(?,?,?)";
        try {
            ext.getParentZone().getDBManager().executeUpdate(query, new Object[]{fbId, avatar, fbName});
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<User> listUsers = lobbyRoom.getUserList();

        listUsers.remove(user);
        sendPlayerInfo(dbManager, user);
        sendListInLobby(listRooms, user, listInGame);
//        sendListInLobby(listRooms, user, listU);
//        UpdateOnlineInLobby(fbName, avatar, user, listUsers);
        sendTopMoney(dbManager, user);
        sendTopExp(dbManager, user);
    }

    private void sendPlayerInfo(IDBManager dBManager, User user) {
        String query = "SELECT money,level FROM user_manager WHERE name=?";
        try {
            ISFSArray res = dBManager.executeQuery(query, new Object[]{user.getName()});
            ISFSObject response = new SFSObject();
            if (res.size() > 0) {
                ISFSObject info = res.getSFSObject(0);
                response.putSFSObject("info", info);
                int playerMoney = info.getInt("money");
                int level = info.getInt("level");
                user.setProperty("money", playerMoney);
                user.setProperty("level", level);
            } else {
                ISFSObject info = new SFSObject();
                info.putInt("money", 100);
                info.putInt("level", 0);
                response.putSFSObject("info", info);
                user.setProperty("level", 0);
                user.setProperty("money", 100);
            }
            Debug.d(response.getDump());
            send("user_info", response, user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sendListInLobby(List<Room> room, User user, List<User> listInGame) {

        List<String> listName = new LinkedList<String>();
        List<String> listFbId = new LinkedList<String>();
        List<String> listAvatars = new LinkedList<String>();
        if (listInGame != null) {
            for (User u : listInGame) {
                listFbId.add(u.getName());
                listName.add((String) u.getProperty("fb_name"));
                listAvatars.add((String) u.getProperty("avatar_img"));
            }
        } else {
            listFbId.add(null);
            listName.add(null);
            listAvatars.add(null);
        }
        //send list in lobby
        SFSObject resObj = new SFSObject();
        resObj.putUtfStringArray("list_fb_ids", listFbId);
        resObj.putUtfStringArray("list_names", listName);
        resObj.putUtfStringArray("list_avatars", listAvatars);
        send("list_online", resObj, user);
        Debug.d(resObj.getDump());
    }

//    private void UpdateOnlineInLobby(String nickName, String avatarImg, User user, List<User> listUsers) {
//        ISFSObject resObj = new SFSObject();
//        resObj.putUtfString("fb_name", nickName);
//        resObj.putUtfString("avatar", avatarImg);
//        resObj.putUtfString("fb_id", user.getName());
//        send("update_list_online", resObj, listUsers);
//    }
    private void sendTopMoney(IDBManager dbManager, User user) {
        String query = "SELECT money,avatar,fb_name FROM user_manager ORDER BY money desc limit 5";
        try {
            ISFSArray res = dbManager.executeQuery(query);
            ISFSObject resObj = new SFSObject();
            resObj.putSFSArray("table", res);
            send("top_money", resObj, user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sendTopExp(IDBManager dbManager, User user) {
        String query = "SELECT level,avatar,fb_name FROM user_manager ORDER BY level desc limit 5";
        try {
            ISFSArray res = dbManager.executeQuery(query);
            ISFSObject resObj = new SFSObject();
            resObj.putSFSArray("table", res);
            send("top_exp", resObj, user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
