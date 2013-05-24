/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extension.pipoarena.room;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

/**
 *
 * @author Vnshine
 */
public class OnUserBetHandler extends BaseClientRequestHandler {

    @Override
    public void handleClientRequest(User user, ISFSObject params) {
//        List<Integer> betTable = (List<Integer>) params.getIntArray("bet_money");
        int betTable = params.getInt("bet_money");
        PipoarenaRoomExtension ext = (PipoarenaRoomExtension) getParentExtension();
        PipoGame game = ext.getGame();
        game.setBetTable(betTable);
//        game.doReady(user);
    }
}
