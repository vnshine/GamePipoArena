/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extension.pipoarena.zone;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.Zone;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import com.smartfoxserver.v2.extensions.SFSExtension;

/**
 *
 * @author chaulh
 */
public class LoginHandler extends BaseServerEventHandler {

    @Override
    public void handleServerEvent(ISFSEvent params) throws SFSException {
        String loginName = (String) params.getParameter(SFSEventParam.LOGIN_NAME);
        SFSExtension ext = getParentExtension();
        Zone zone = ext.getParentZone();
        User duplicateUser = zone.getUserByName(loginName);
        if (duplicateUser != null) {
            getApi().kickUser(duplicateUser, null, null, 0);
        }

    }
}
