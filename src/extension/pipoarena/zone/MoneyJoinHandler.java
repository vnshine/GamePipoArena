/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extension.pipoarena.zone;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import log.Debug;

/**
 *
 * @author Vnshine
 */
public class MoneyJoinHandler extends BaseClientRequestHandler{

    @Override
    public void handleClientRequest(User user, ISFSObject params) {
        PipoarenaZoneExtension ext = (PipoarenaZoneExtension) getParentExtension();
        if(!Debug.isDebug){
        long moneyJoin = 0;
            try {
                moneyJoin = params.getLong("money_join");
            } catch (Exception e) {
                moneyJoin = params.getInt("money_join");
            }
        }
    }
}
