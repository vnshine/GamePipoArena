/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extension.pipoarena.zone;

import com.smartfoxserver.v2.db.IDBManager;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import extension.pipoarena.room.PipoarenaRoomExtension;
import log.Debug;

/**
 *
 * @author Vnshine
 */
public class OnUserInfoHandler extends BaseClientRequestHandler {

    @Override
    public void handleClientRequest(User user, ISFSObject params) {
        PipoarenaRoomExtension ext = (PipoarenaRoomExtension) getParentExtension();
        String avatar = null;
        String nickname = null;
        Integer userid;
        avatar = params.getUtfString("avatar_img");
        user.setProperty("avatar", avatar);
        nickname = params.getUtfString("user_name");
        user.setProperty("user_name", nickname);
        userid = params.getInt("user_id");
        user.setProperty("userid", userid);
        Debug.d(params.getDump());
        String location = (String) user.getProperty("user_location");
        if (location.equals("lobby")) {
        IDBManager dbManager = getParentExtension().getParentZone().getDBManager();
        sendPlayerInfo(dbManager,user);
        UpdateUserInfo(dbManager,user);
        }
    }

    private void sendPlayerInfo(IDBManager dBManager, User user) {
        String query = "SELECT money,level FROM user_manager WHERE name=?";
        try {
            ISFSArray resarray = dBManager.executeQuery(query, new Object[]{user.getName()});
            ISFSObject resobj = new SFSObject();
            if (resarray.size() > 0) {
                ISFSObject info = resarray.getSFSObject(0);
                
            }
        } catch (Exception e) {
        }
    }

    private void UpdateUserInfo(IDBManager dBManager, User user) {
        try {
            
        } catch (Exception e) {
        }
    }
}
