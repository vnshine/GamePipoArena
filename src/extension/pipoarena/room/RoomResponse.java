/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extension.pipoarena.room;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.ISFSExtension;
import java.util.List;
import log.Debug;

/**
 *
 * @author Vnshine
 */
public class RoomResponse {

    private ISFSExtension ext;

    public RoomResponse(ISFSExtension ext) {
        this.ext = ext;
    }

    void sendTableInfo(List<String> listNames,
            String roomName,
            List<Integer> listPos,
            List<Integer> money,
            List<Integer> exp,
            List<Integer> level,
            List<Integer> viewer,
            long bettable,
            int seat,
            int tableHost,
            boolean isPlay,
            List<Integer> state,
            List<Integer> seatintable,
            List<String> avatar,
            List<String> fbName,
            User user) {
        ISFSObject resObj = new SFSObject();
        resObj.putUtfStringArray("list_names", listNames);
        resObj.putUtfString("room_name", roomName);
        resObj.putIntArray("list_pos", listPos);
        resObj.putIntArray("money", money);
        resObj.putIntArray("experience", exp);
        resObj.putIntArray("level", level);
        resObj.putIntArray("viewer", viewer);
        resObj.putInt("seat", seat);
        resObj.putLong("bet_table", bettable);
        resObj.putInt("table_host", tableHost);
        resObj.putBool("is_play", isPlay);
        resObj.putIntArray("state", state);
        resObj.putIntArray("seat_in_table", seatintable);
        resObj.putUtfStringArray("avatar", avatar);
        resObj.putUtfStringArray("fb_names", fbName);
        ext.send("table_info", resObj, user);
        Debug.d(resObj.getDump());
    }

    void sendTableState(List<String> avatar,
            List<String> listNames,
            List<Integer> nameWinner,
            List<String> fbName,
            List<Integer> listPos,
            List<Integer> timeans,
            List<Integer> numberans,
            List<Integer> totaltrueans,
            String trueAnswer,
            List<Integer> listAnswerRight,
            List<Integer> listAnswerWrong,
            int NumTrueAns,
            int numberWinners,
            long moneyWin,
            List<Integer> moneyLost,
            List<Integer> level,
            List<Integer> viewer,
            List<Integer> mySeat,
            List<Integer> exp,
            long bettable,
            int seat,
            List<Integer> money,
            User listUsers) {
        ISFSObject resObj = new SFSObject();
        resObj.putUtfStringArray("list_names", listNames);
        resObj.putUtfStringArray("fb_names", fbName);
        resObj.putIntArray("name_winner", nameWinner);
        resObj.putIntArray("list_pos", listPos);
        resObj.putIntArray("time_ans", timeans);
        resObj.putIntArray("number_answer", numberans);
        resObj.putIntArray("total_true_ans", totaltrueans);
        resObj.putUtfString("true_answer", trueAnswer);
        resObj.putIntArray("list_answer_right", listAnswerRight);
        resObj.putIntArray("list_answer_wrong", listAnswerWrong);
        resObj.putInt("number_true_ans", NumTrueAns);
        resObj.putInt("number_winners", numberWinners);
        resObj.putLong("money_win", moneyWin);
        resObj.putIntArray("level", level);
        resObj.putIntArray("viewer", viewer);
        resObj.putIntArray("money_lost", moneyLost);
        resObj.putInt("reset_ready", 1);
        resObj.putIntArray("experience", exp);
        resObj.putIntArray("my_seat", mySeat);
        resObj.putUtfStringArray("avatar", avatar);
        resObj.putLong("bet_table", bettable);
        resObj.putInt("seat", seat);
        resObj.putIntArray("money", money);
        ext.send("table_state", resObj, listUsers);
        Debug.d(resObj.getDump());
    }

    void sendTableState(List<String> listNames,
            List<Integer> listPos, int seat, List<User> listUsers) {

        ISFSObject resObj = new SFSObject();
        resObj.putUtfStringArray("list_names", listNames);
        resObj.putIntArray("list_pos", listPos);
        resObj.putInt("seat", seat);
        ext.send("table_state", resObj, listUsers);
        Debug.d(resObj.getDump());
    }

    void sendResult(List<Integer> listPlayerAnswerRight, List<Integer> listPlayerAnswerWrong, List<User> users) {
        ISFSObject resObj = new SFSObject();
        resObj.putIntArray("answer_right", listPlayerAnswerRight);
        resObj.putIntArray("answer_wrong", listPlayerAnswerWrong);
        ext.send("result_right_wrong", resObj, users);
    }

    void SendMoney(long totalWinMoney, long totalLostMoney, List<User> userslist) {
        ISFSObject resObj = new SFSObject();
        resObj.putLong("win_money", totalWinMoney);
        resObj.putLong("lost_money", totalLostMoney);
        ext.send("money_in_each_question", resObj, userslist);
    }

    void sendPlayerEnter(int seatFound, String name, String fbName, String avatar,int money,int exp, List<User> listUser) {
        ISFSObject resObj = new SFSObject();
        resObj.putInt("seat_pos", seatFound);
        resObj.putUtfString("player_name", name);
        resObj.putUtfString("avatar", avatar);
        resObj.putUtfString("fb_names", fbName);
        resObj.putInt("money", money);
        resObj.putInt("experience", exp);
        ext.send("player_enter", resObj, listUser);
    }

    void sendReady(int seat, int state, List<User> listUser) {
        ISFSObject resObj = new SFSObject();
        resObj.putInt("seat", seat);
        resObj.putInt("state", state);
        ext.send("ready", resObj, listUser);
    }

    void sendCanNotReady(User user) {
        ext.send("ready_failed", null, user);
    }

    void sendNewMatch(List<String> avatar,
            List<String> listNames,
            List<String> fbName,
            List<Integer> listPos, 
            long bettable, 
            int tableHost, 
            int seat, 
            List<Integer> money, 
            List<Integer> exp,
            List<Integer> level, 
            User userList) {
        ISFSObject resObj = new SFSObject();
        resObj.putUtfStringArray("avatar", avatar);
        resObj.putUtfStringArray("list_names", listNames);
        resObj.putUtfStringArray("fb_names", fbName);
        resObj.putIntArray("list_pos", listPos);
        resObj.putLong("bet_table", bettable);
        resObj.putInt("table_host", tableHost);
        resObj.putInt("seat", seat);
        resObj.putIntArray("money", money);
        resObj.putIntArray("experience", exp);
        resObj.putIntArray("level", level);
        ext.send("new_match", resObj, userList);
    }

    void sendBetTable(long bettable,List<User> users) {
        ISFSObject res = new SFSObject();
        res.putLong("bet_table", bettable);
        ext.send("bet_info", res, users);
    }
}
