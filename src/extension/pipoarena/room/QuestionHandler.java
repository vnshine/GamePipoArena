/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extension.pipoarena.room;

import com.smartfoxserver.v2.db.IDBManager;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

/**
 *
 * @author Vnshine
 */
public class QuestionHandler extends BaseClientRequestHandler{
    @Override
    public void handleClientRequest(User user, ISFSObject params) {
        IDBManager dBManager = getParentExtension().getParentZone().getDBManager();
        String sql = "SELECT FROM code_question limit 10";
        ISFSObject resObj = new SFSObject();
        try {
            ISFSArray res = dBManager.executeQuery(sql);
        } catch (Exception e) {
        }
    }
    
}
