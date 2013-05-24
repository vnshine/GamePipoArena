package extension.pipoarena.room;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class OnUserGoneHandler extends BaseServerEventHandler{

    @Override
    public void handleServerEvent(ISFSEvent event) throws SFSException {
        PipoarenaRoomExtension ext = (PipoarenaRoomExtension) getParentExtension();
        User user = (User) event.getParameter(SFSEventParam.USER);
        ext.getGame().onUserLeave(user);
    }
}