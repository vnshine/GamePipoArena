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

/**
 *
 * @author Vnshine
 */
public class JoinTableHandler extends BaseClientRequestHandler{

    protected Room room;
    public int numberuserperroom = 0;
    public int userpos;
    @Override
    public void handleClientRequest(User user, ISFSObject params) {
        PipoarenaZoneExtension ext = (PipoarenaZoneExtension) getParentExtension();
//        PipoarenaRoomExtension ext = (PipoarenaRoomExtension) getParentExtension();
        numberuserperroom = ext.getParentZone().getUserCount();
//        numberuserperroom =ext.getParentRoom().getUserList().size();
        ISFSObject resobj = new SFSObject();
        
        if (numberuserperroom<= 2) {
            resobj.putInt("can_join_table", 1);       

            resobj.putInt("user_pos", userpos);            
        }
        else{
        resobj.putInt("can_join_table", 0);
        }
        ext.send("join_table", resobj, user);
    }
}
