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
public class AnswerHandler extends BaseClientRequestHandler {

    @Override
    public void handleClientRequest(User user, ISFSObject params) {
        int playerAnswer = params.getInt("number_Answer");
        int timeAnswer = params.getInt("time_left");
        PipoarenaRoomExtension ext = (PipoarenaRoomExtension) getParentExtension();
        PipoGame game = ext.getGame();
        game.doPlayerAnswer(user, timeAnswer, playerAnswer);
    }
}
