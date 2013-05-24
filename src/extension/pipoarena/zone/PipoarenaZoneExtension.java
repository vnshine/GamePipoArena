package extension.pipoarena.zone;

import com.smartfoxserver.v2.api.CreateRoomSettings;
import com.smartfoxserver.v2.api.CreateRoomSettings.RoomExtensionSettings;
import com.smartfoxserver.v2.api.ISFSApi;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.exceptions.SFSCreateRoomException;
import com.smartfoxserver.v2.extensions.SFSExtension;
import extension.pipoarena.DataBase.Database;
import log.Debug;

/**
 *
 * @author Vnshine
 */
public class PipoarenaZoneExtension extends SFSExtension {
    public static final String VERSION="23.5.2013.17";
    public static String[] GROUP_LIST = {RoomGroup.Level1, RoomGroup.Level2, RoomGroup.Level3,
        RoomGroup.Level4, RoomGroup.Level5};
    private int numberTables = 25;
    private int numberTablesPerRoom = 5;
    private Response response;
    private final String roomPrefix = "P";
    private Database database;

    @Override
    public void init() {
        Debug.d("============================VERSION:"+VERSION);
        response = new Response(this);
        database = new Database(getParentZone().getDBManager());
        addRequestHandler("list_table", ListTableInfoHandler.class);
        addRequestHandler("player_choice_room", OnPlayerJoinRoomHandler.class);
        addRequestHandler("money_join", MoneyJoinHandler.class);
        addEventHandler(SFSEventType.USER_JOIN_ROOM, OnJoinLobbyHandler.class);
        addEventHandler(SFSEventType.USER_LOGIN, LoginHandler.class);
        addEventHandler(SFSEventType.USER_JOIN_ZONE, OnUserCountHandler.class);
        addEventHandler(SFSEventType.USER_DISCONNECT, OnUserDisconnectHandler.class);
        createTable();
    }

    private void createTable() {
        for (int i = 0; i < numberTables; i++) {
            makeTable(i + 1, GROUP_LIST[i / numberTablesPerRoom]);
        }
    }

    private void makeTable(int tableName, String groupId) {
        CreateRoomSettings settings = new CreateRoomSettings();
        settings.setName(roomPrefix + String.valueOf(tableName));
        settings.setMaxUsers(20);
        settings.setGame(true);
        settings.setGroupId(groupId);
        RoomExtensionSettings extensionSetting = new CreateRoomSettings.RoomExtensionSettings("PipoarenaZone", "extension.pipoarena.room.PipoarenaRoomExtension");
        settings.setExtension(extensionSetting);
        ISFSApi api = getApi();
        try {
            Room room = api.createRoom(getParentZone(), settings, null, false, null, false, true);
        } catch (SFSCreateRoomException ex) {
        }
    }

    public Response getResponse() {
        return response;
    }
    Database getDatabase(){
    return database;
    }
    public String getRoomPrefix() {
        return roomPrefix;
    }
}
