/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extension.pipoarena.DataBase;

import com.smartfoxserver.v2.db.IDBManager;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import java.sql.SQLException;
import log.Debug;

/**
 *
 * @author Vnshine
 */
public class Database {

    IDBManager dbManager;

    public Database(IDBManager dbManager) {
        this.dbManager = dbManager;
    }

    public UserData getUserInfo(String name) {
        UserData userData = new UserData();
        try {
            String sql = "SELECT * FROM user_manager WHERE name=?";
            ISFSArray res = dbManager.executeQuery(sql, new Object[]{name});
            if (res.size() > 0) {
                ISFSObject s = res.getSFSObject(0);
                userData.setName(s.getUtfString("name"));
                userData.setLevel(s.getInt("level"));
                userData.setMoney(s.getInt("money"));
                userData.setNickName(s.getUtfString("fb_name"));
                userData.setAvatar(s.getUtfString("avatar"));
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Debug.d(ex.toString());
            return null;
        }
        return userData;
    }

    public void updateUserMoney(String name, int newMoney) {
        try {
            String query = "update user_manager set money=? where name=?";
            dbManager.executeUpdate(query, new Object[]{newMoney, name});
            Debug.d("query :"+query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUserExp(String name, int newExp) {
        try {
            String query = "update user_manager set level=? where name=?";
            dbManager.executeUpdate(query, new Object[]{newExp, name});
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
