/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extension.pipoarena.room;

import com.smartfoxserver.v2.core.SFSEventType;
import log.Debug;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.extensions.SFSExtension;
import extension.pipoarena.DataBase.Database;
import java.util.concurrent.ScheduledFuture;

/**
 *
 * @author Vnshine
 */
public class PipoarenaRoomExtension extends SFSExtension {

    ScheduledFuture<?> taskHandle;
//    protected Database database;
    private PipoGame game;
    private RoomResponse roomResponse;
    Database database;

    @Override
    public void init() {
        Debug.setExt(this);
        database = new Database(getParentZone().getDBManager());
        roomResponse = new RoomResponse(this);
        // REGISTER REQUEST HANDLERS
        Room room = getParentRoom();
        if (room.getName().equals("The Lobby")) {
            addRequestHandler("users_info", OnUserInfoHandler.class);
            addRequestHandler("invite", OnUserInviteHandler.class);
            addRequestHandler("play_now", OnUserQuickPlayHandler.class);
        } else {
            int roomId = Integer.parseInt(room.getName().substring(1));
            initGame(roomId);
            // REGISTER EVENT HANDLERS
            addRequestHandler("kick_user", OnUserKickHandler.class);
            addRequestHandler("ready", ReadyHandler.class);
            addRequestHandler("question_request", QuestionHandler.class);
            addRequestHandler("Ans_Request", AnswerHandler.class);
            addRequestHandler("bet_request", OnUserBetHandler.class);
            addEventHandler(SFSEventType.USER_JOIN_ROOM, OnJoinRoomHandler.class);
            addEventHandler(SFSEventType.USER_LEAVE_ROOM, OnUserGoneHandler.class);
            addEventHandler(SFSEventType.USER_DISCONNECT, OnUserGoneHandler.class);
            addEventHandler(SFSEventType.USER_LOGOUT, OnUserGoneHandler.class);
        }
        Debug.d("room addres:" + this.toString());
        if (Debug.isDebug) {
        }

    }

    public RoomResponse getRoomResponse() {
        return roomResponse;
    }

    private void initGame(int roomId) {
        game = new PipoGame(roomId, this);
        game.start();
    }

    public PipoGame getGame() {
        return game;
    }

    public Database getDatabase() {
        return database;
    }

    public ScheduledFuture<?> getTaskHandle() {
        return taskHandle;
    }
}
