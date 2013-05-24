/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extension.pipoarena.room;

import com.smartfoxserver.v2.db.IDBManager;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import extension.pipoarena.DataBase.Database;
import extension.pipoarena.DataBase.UserData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import log.Debug;

/**
 *
 * @author Vnshine
 */
public class PipoGame implements Runnable {

    public static final int SEND_QUESTION = 1;
    public static final int SEND_ANSWER = 2;
    public static final int WAIT_NEXT_QUESTION = 3;
    public static final int Sending = 4;
    public static final int SEND_FINAL_RESULT = 5;
    public static final int WAIT_NEXT_MATCH = 6;
    private long currentGameStateTime = 0;
    public int TIME_TO_ANSWER = 20000;
    public int TIME_TO_READY = 5000;
    public int TIME_TO_NEXT_MATCH = 30000;
    private int gameState;
    private long question_id;
    protected boolean isRunning;
    private PipoPlayer[] listPlayers;
    private int roomId;
    private int maxPlayers = 8;
    private int numberPlayes;
    private PipoarenaRoomExtension ext;
    private RoomResponse response;
    private List<Question> listQuestions;
    private PipoPlayer player;
    private long sendQuestionTimeAnchor;
    private Question currentQuestion;
    private Room currentRoom;
    private boolean isNotifyTableState;
    private boolean isSendFinalResult;
    private int countQuestion;
    private int numberCanPlays = 1;
    private boolean isSendQuestion;
    private boolean isSendAnswer;
    private int betMoney = 10;
    private int tableHost;
    private boolean cantplay;
    private boolean isPlay;
    private int numberPlayings;
    private int penaltyMoney;
    private int numberReadys;
    private int numberQuestion =0;

    public PipoGame(int roomId, PipoarenaRoomExtension ext) {
        this.roomId = roomId;
        currentRoom = ext.getParentRoom();
        this.ext = ext;
        this.response = ext.getRoomResponse();
        listQuestions = new LinkedList<Question>();
        listPlayers = new PipoPlayer[maxPlayers];
        ext.getParentZone().getDBManager();
    }

    public long getTimeToAnswer() {
        return TIME_TO_ANSWER / 1000;
    }

    public void start() {
        isRunning = true;
        Thread newThread = new Thread(this);
        newThread.start();
    }

    public void onPlayerJoin(User user) {
        Database database = ext.getDatabase();
        UserData userData = database.getUserInfo(user.getName());
        if (numberPlayes < 8 && userData.getMoney() > 18) {
            // tim cho
            int seatFound = -1;
            for (int i = 0; i < maxPlayers; i++) {
                if (listPlayers[i] == null) {
                    seatFound = i;
                    break;
                }
            }

            if (seatFound != -1) {

                PipoPlayer newPlayer = new PipoPlayer(user.getName(), userData.getNickName(), userData.getAvatar(), userData.getMoney(), userData.getLevel(), seatFound, user);
                listPlayers[seatFound] = newPlayer;
                if (!isPlay) {
                    newPlayer.setState(PipoPlayer.NOT_READY);
                    cantplay = false;
                } else {
                    newPlayer.setState(PipoPlayer.VIEW);
                }
                numberPlayes = 0;
                numberPlayings = 0;
                for (int i = 0; i < maxPlayers; i++) {
                    if (listPlayers[i] != null) {
                        numberPlayes++;
                        if (listPlayers[i].getState() != PipoPlayer.VIEW) {
                            numberPlayings++;
                        }
                    }
                }

                if (numberPlayes == 1) {
                    tableHost = seatFound;
                } else if (numberPlayes >= 2) {

                    //bao cho nhung thang con lai
                    List<User> listUser = currentRoom.getUserList();
                    listUser.remove(user);
                    response.sendPlayerEnter(seatFound, user.getName(), userData.getNickName(), userData.getAvatar(), userData.getMoney(),userData.getLevel(), listUser);
                }
                sendTableInfo(newPlayer);
                sendUpdateToLobby(user);
                sendUpdateUserJoinTable(user);
            }

        } else {
            user.setJoining(false);
            SFSObject res = new SFSObject();
            res.putInt("user_in_room", currentRoom.getUserList().size());
            ext.send("full_table", res, user);
        }
    }

    private void sendUpdateToLobby(User user) {
        Room lobbyRoom = ext.getParentZone().getRoomByName("The Lobby");
        List<User> listUsers = lobbyRoom.getUserList();
        List<String> listRoomName = new LinkedList<String>();
        List<Integer> numberplayerinroom = new LinkedList<Integer>();
        String groupName = (String) user.getProperty("group_names");
        List<Room> listroom = ext.getParentZone().getRoomListFromGroup(groupName);
        listroom.remove(lobbyRoom);
        for (Room r : listroom) {
            listRoomName.add(r.getName());
            numberplayerinroom.add(r.getUserList().size());
        }
        ISFSObject res = new SFSObject();
        res.putUtfStringArray("room_name", listRoomName);
        res.putIntArray("number_player_in_room", numberplayerinroom);
        ext.send("update_user_in_room", res, listUsers);
        Debug.d(res.getDump());
    }

    private void sendTableInfo(PipoPlayer player) {
        Database database = ext.getDatabase();
        UserData userData = database.getUserInfo(player.getName());
        List<String> listNames = new LinkedList<String>();
        List<Integer> listPos = new LinkedList<Integer>();
        List<Integer> money = new LinkedList<Integer>();
        List<Integer> experience = new LinkedList<Integer>();
        List<Integer> level = new LinkedList<Integer>();
        List<Integer> viewer = new LinkedList<Integer>();
        List<Integer> state = new LinkedList<Integer>();
        List<Integer> seat = new LinkedList<Integer>();
        List<String> fbName = new LinkedList<String>();
        List<String> avatar = new LinkedList<String>();
        String roomName = null;
        for (int i = 0; i < maxPlayers; i++) {
            if (listPlayers[i] != null) {
                if (listPlayers[i].getState() == PipoPlayer.VIEW) {
                    viewer.add(i);
                }
            }
        }
        for (int i = 0; i < maxPlayers; i++) {
            if (listPlayers[i] != null) {
                state.add(listPlayers[i].getState());
                seat.add(listPlayers[i].getSeat());
                listNames.add(listPlayers[i].getName());
                avatar.add(listPlayers[i].getAvatar());
                fbName.add(listPlayers[i].getNickName());
                listPos.add(1);
                money.add(listPlayers[i].getMoney());
                experience.add(listPlayers[i].getExperience());
                roomName = currentRoom.getName();
                if (listPlayers[i].getExperience() < 100) {
                    level.add(0);
                } else if (listPlayers[i].getExperience() < 1000) {
                    level.add(1);
                } else if (listPlayers[i].getExperience() < 10000) {
                    level.add(2);
                } else if (listPlayers[i].getExperience() < 100000) {
                    level.add(3);
                } else if (listPlayers[i].getExperience() < 500000) {
                    level.add(4);
                } else if (listPlayers[i].getExperience() < 1000000) {
                    level.add(5);
                } else if (listPlayers[i].getExperience() < 2000000) {
                    level.add(6);
                } else if (listPlayers[i].getExperience() < 3000000) {
                    level.add(7);
                } else if (listPlayers[i].getExperience() < 5000000) {
                    level.add(8);
                } else {
                    level.add(9);
                }
            } else {
                listNames.add("0");
                listPos.add(0);
                fbName.add("0");
                avatar.add("0");
                money.add(0);
                experience.add(0);
            }
        }

        response.sendTableInfo(listNames, roomName, listPos, money, experience,
                level, viewer, betMoney, player.getSeat(), tableHost,
                isPlay, state, seat, avatar, fbName, player.getUser());
    }

    void sendQuestion() {
        listQuestions = new LinkedList<Question>();
        IDBManager dBManager = ext.getParentZone().getDBManager();
        String query = "SELECT * FROM code_question ORDER BY RAND() limit 1";
        ISFSArray resultSet = null;
        try {
            resultSet = dBManager.executeQuery(query);
            if (resultSet.size() > 0) {
                ISFSObject object = resultSet.getSFSObject(0);
                currentQuestion = new Question(object);
                for (int i = 0; i < maxPlayers; i++) {
                    if (listPlayers[i] != null) {
                        listPlayers[i].resetTimeAnswer();
                        ISFSObject resObj = new SFSObject();
                        resObj.putUtfString("QUESTION_CONTENT", currentQuestion.getContent());
                        resObj.putUtfString("QUESTION_1", currentQuestion.getAnswer1());
                        resObj.putUtfString("QUESTION_2", currentQuestion.getAnswer2());
                        resObj.putUtfString("QUESTION_3", currentQuestion.getAnswer3());
                        resObj.putUtfString("QUESTION_4", currentQuestion.getAnswer4());
                        resObj.putInt("ID_QUESTION", currentQuestion.getIdQuestion());
                        resObj.putLong("time_to_answer", TIME_TO_ANSWER);
                        resObj.putInt("player_state", listPlayers[i].getState());
                        resObj.putInt("question_numbers", countQuestion);
                        resObj.putLong("money", listPlayers[i].getMoney());
                        resObj.putInt("number_ans_right", listPlayers[i].getNumberAnswerRights());
                        ext.send("question_and_ans", resObj, listPlayers[i].getUser());
                    }
                }
                ++countQuestion;
            }
        } catch (SQLException ex) {
        }
    }

    private void sendTableState() {
        List<String> listNames = new LinkedList<String>();
        List<Integer> listPos = new LinkedList<Integer>();
        List<Integer> level = new LinkedList<Integer>();
        List<Integer> timeans = new LinkedList<Integer>();
        List<Integer> numberans = new LinkedList<Integer>();
        List<Integer> listseat = new LinkedList<Integer>();
        List<Integer> viewer = new LinkedList<Integer>();
        List<String> avatar = new LinkedList<String>();
        List<String> fbName = new LinkedList<String>();
        List<Integer> totaltrueans = new LinkedList<Integer>();
        List<Integer> money = new LinkedList<Integer>();
        List<Integer> experience = new LinkedList<Integer>();
        
        for (int i = 0; i < maxPlayers; i++) {
            if (listPlayers[i] != null) {
                if (listPlayers[i].getState() == PipoPlayer.VIEW) {
                    viewer.add(i);
                }
            }
        }
        // lay list name va list pos
        for (int i = 0; i < maxPlayers; i++) {
            if (listPlayers[i] != null) {
                Database database = ext.getDatabase();
                UserData userData = database.getUserInfo(listPlayers[i].getName());
                avatar.add(userData.getAvatar());
                listNames.add(listPlayers[i].getName());
                listPos.add(1);
                timeans.add(listPlayers[i].getTimeAnswer());
                numberans.add(listPlayers[i].getAnsNumber());
                listseat.add(listPlayers[i].getSeat());
                fbName.add(userData.getNickName());
                totaltrueans.add(listPlayers[i].getNumberAnswerRights());
                experience.add(listPlayers[i].getExperience());
            } else {
                listNames.add("0");
                listPos.add(0);
                fbName.add("0");
                totaltrueans.add(0);
                avatar.add("0");
                experience.add(0);
            }
        }
        //tim noi dung cau tra loi dung
        int trueAnswer = currentQuestion.getTrueAnswer();
        currentQuestion.getTrueAnswer();
        String answerContent = null;
        switch (trueAnswer) {
            case 0:
                answerContent = currentQuestion.getAnswer1();
                break;
            case 1:
                answerContent = currentQuestion.getAnswer2();
                break;
            case 2:
                answerContent = currentQuestion.getAnswer3();
                break;
            case 3:
                answerContent = currentQuestion.getAnswer4();
                break;
        }
// tim danh sach tra loi sai va dung
        int numberWinners = 0;
        List<Integer> listPlayerAnswerWrong = new LinkedList<Integer>();
        List<Integer> listPlayerAnswerRight = new LinkedList<Integer>();
        for (int i = 0; i < maxPlayers; i++) {
            if (listPlayers[i] != null) {
                if (listPlayers[i].getState() != PipoPlayer.VIEW) {

                    if (listPlayers[i].isIsAswerRight()) {
                        listPlayerAnswerRight.add(i);
                    } else {
                        listPlayerAnswerWrong.add(i);
                    }
                }
            }
        }
        if (!listPlayerAnswerRight.isEmpty()) {
            if (listPlayerAnswerRight.size() >= 2) {
                for (int i = 0; i < listPlayerAnswerRight.size() - 1; i++) {
                    for (int j = i + 1; j < listPlayerAnswerRight.size(); j++) {
                        PipoPlayer playeri = listPlayers[i];
                        PipoPlayer playerj = listPlayers[j];
                        if (playeri.getTimeAnswer() > playerj.getTimeAnswer()) {
                            listPlayerAnswerRight.set(i, j);
                            listPlayerAnswerRight.set(j, i);
                        }
                    }
                }
                //tim so thang bang nhau
                int playerFatestPos = listPlayerAnswerRight.get(0);
                for (Integer playerPos : listPlayerAnswerRight) {
                    //diem se giam dan tu thang thu nhat den thang cuoi
                    if (listPlayers[playerFatestPos].getTimeAnswer() == listPlayers[playerPos].getTimeAnswer()) {
                        numberWinners++;
                    }
                }
            } else {
                numberWinners = 1;
            }
            //tinh diem
            int playerFatestPos = listPlayerAnswerRight.get(0);
            int count = 0;
            for (Integer playerPos : listPlayerAnswerRight) {
                Database database = ext.getDatabase();
                UserData userData = database.getUserInfo(listPlayers[playerPos].getName());
                //diem se giam dan tu thang thu nhat den thang cuoi
                if (listPlayers[playerFatestPos].getTimeAnswer() == listPlayers[playerPos].getTimeAnswer()) {
                    listPlayers[playerPos].updatePoint(numberPlayings - count);
                    Debug.d("point" + listPlayers[playerPos].getPoints());
                    listPlayers[playerPos].updateExp(+1);
                    database.updateUserExp(listPlayers[playerPos].getName(), userData.getLevel() + 1);

                } else {
                    listPlayers[playerPos].updatePoint(numberPlayings - count);
                    listPlayers[playerPos].updateExp(+1);
                    database.updateUserExp(listPlayers[playerPos].getName(), userData.getLevel() + 1);
                    Debug.d("point" + listPlayers[playerPos].getPoints());
                    count++;
                }
            }
        }
        //update level
        for (int i = 0; i < maxPlayers; i++) {
            if (listPlayers[i] != null) {
                if (listPlayers[i].getExperience() < 100) {
                    level.add(0);
                } else if (listPlayers[i].getExperience() < 1000) {
                    level.add(1);
                } else if (listPlayers[i].getExperience() < 10000) {
                    level.add(2);
                } else if (listPlayers[i].getExperience() < 100000) {
                    level.add(3);
                } else if (listPlayers[i].getExperience() < 500000) {
                    level.add(4);
                } else if (listPlayers[i].getExperience() < 1000000) {
                    level.add(5);
                } else if (listPlayers[i].getExperience() < 2000000) {
                    level.add(6);
                } else if (listPlayers[i].getExperience() < 3000000) {
                    level.add(7);
                } else if (listPlayers[i].getExperience() < 5000000) {
                    level.add(8);
                } else {
                    level.add(9);
                }
            }
        }

        //tinh tong tien thua
        List<Integer> lostmoney = new LinkedList<Integer>();
        for (int i = 0; i < maxPlayers; i++) {
            if (listPlayers[i] != null) {
                Database database = ext.getDatabase();
                UserData userData = database.getUserInfo(listPlayers[i].getName());
                if (listPlayers[i].getState() == PipoPlayer.VIEW) {
                    lostmoney.add(0);
                } else {
                    if (listPlayers[i].getState() != PipoPlayer.VIEW) {
                        lostmoney.add(-betMoney);
                        database.updateUserMoney(listPlayers[i].getName(), userData.getMoney() - betMoney);
                    }
                }
            }
        }
        int totalLostMoney = 0;
        for (Integer playerPos : listPlayerAnswerWrong) {
            totalLostMoney += betMoney;
            //tru tien thang tra sai
            if (listPlayers[playerPos].getState() != PipoPlayer.VIEW) {
                listPlayers[playerPos].updateMoney(-betMoney);
            }
        }
        int totalWinMoney = 0;
        List<Integer> nameWinner = new LinkedList<Integer>();
        if (numberWinners > 0) {
            totalWinMoney = totalLostMoney / numberWinners;
            for (int i = 0; i < numberWinners; i++) {
                int playerPos = listPlayerAnswerRight.get(i);
                listPlayers[playerPos].updateMoney(totalWinMoney);
                nameWinner.add(listPlayers[playerPos].getSeat());
                Database database = ext.getDatabase();
                database.updateUserMoney(listPlayers[playerPos].getName(), listPlayers[playerPos].getMoney());
            }
        }
        for (int i = 0; i < maxPlayers; i++) {
            if (listPlayers[i] != null) {
                money.add(listPlayers[i].getMoney());
            }else{
            money.add(0);
            }
        }
        for (int i = 0; i < maxPlayers; i++) {
            if (listPlayers[i] != null) {
                response.sendTableState(avatar, listNames, nameWinner, fbName, listPos, timeans, numberans, totaltrueans, answerContent,
                        listPlayerAnswerRight, listPlayerAnswerWrong, trueAnswer,
                        numberWinners, totalWinMoney, lostmoney, level, viewer,
                        listseat,
                        experience,
                        betMoney,
                        listPlayers[i].getSeat(),
                        money,
                        listPlayers[i].getUser());
            }
        }
    }

    private void sendFinalResult() {

        ISFSObject resObj = new SFSObject();
        List<Integer> playerRanks = new LinkedList<Integer>();
        List<String> playerRankName = new LinkedList<String>();
        List<String> fbName = new LinkedList<String>();
        List<Integer> listMoneyResult = new LinkedList<Integer>();
        List<Integer> totalAnswerRights = new LinkedList<Integer>();
        List<Integer> listPoints = new LinkedList<Integer>();
        for (int i = 0; i < maxPlayers; i++) {
            if (listPlayers[i] != null && listPlayers[i].getState() != PipoPlayer.VIEW) {
                playerRanks.add(i);
                listPlayers[i].getBetTable();
            }
        }

        //xep lai thu tu player theo point
        for (int i = 0; i < playerRanks.size() - 1; i++) {
            for (int j = i + 1; j < playerRanks.size(); j++) {
                int posi = playerRanks.get(i);
                int posj = playerRanks.get(j);
                if (listPlayers[posi].getPoints() < listPlayers[posj].getPoints()) {
                    playerRanks.set(posi, posj);
                    playerRanks.set(posj, posi);
                }
            }
        }

        //tinh tien
        int totalMoneyWin = 0;
        for (int i = 1; i < playerRanks.size(); i++) {
            int playerPos = playerRanks.get(i);
            totalMoneyWin += (betMoney * i);
        }
        totalMoneyWin += penaltyMoney;

        //tinh tien thang
        listMoneyResult.add(totalMoneyWin);
        int count = 0;

        for (Integer pos : playerRanks) {
            Database database = ext.getDatabase();
            UserData userData = database.getUserInfo(listPlayers[pos].getName());
            playerRankName.add(listPlayers[pos].getName());
            fbName.add(listPlayers[pos].getNickName());
            listPoints.add(listPlayers[pos].getPoints());
            totalAnswerRights.add(listPlayers[pos].getNumberAnswerRights());
            listPlayers[pos].setNumberAnswerRights(0);
            if (count == 0) {
                listPlayers[pos].updateMoney(+totalMoneyWin);
                database.updateUserMoney(listPlayers[pos].getName(), userData.getMoney() + totalMoneyWin);
                listPlayers[pos].updateExp(+10);
                database.updateUserExp(listPlayers[pos].getName(), userData.getLevel() + 10);
            }
            if (count > 0) {
                listMoneyResult.add(-betMoney * count);
                listPlayers[pos].updateMoney(-betMoney * count);
                database.updateUserMoney(listPlayers[pos].getName(), userData.getMoney() + (-betMoney * count));
            }
            count++;
        }
        for (int i = 0; i < maxPlayers; i++) {
            if (listPlayers[i] != null) {
                resObj.putIntArray("list_money_result", listMoneyResult);
                resObj.putUtfStringArray("player_ranking", playerRankName);
                resObj.putUtfStringArray("fb_names", fbName);
                resObj.putIntArray("total_true_ans", totalAnswerRights);
                resObj.putIntArray("list_point", listPoints);
                resObj.putLong("money", listPlayers[i].getMoney());
                ext.send("final_result", resObj, listPlayers[i].getUser());
                Debug.d(resObj.getDump());
            }
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            if (gameState == 0) {
                countQuestion = 0;
            }
            if (numberPlayes >= 1) {
                switch (gameState) {

                    case SEND_QUESTION:
                        //gui cau hoi
                        if (!isSendQuestion) {
                            sendQuestion();
                            isSendQuestion = true;
                            currentGameStateTime = System.currentTimeMillis();
                        }
                        //khi het thoi gian tra loi gui cau tra loi
                        if (System.currentTimeMillis() - currentGameStateTime > TIME_TO_ANSWER) {
                            gameState = SEND_ANSWER;
                            isSendQuestion = false;
                            isSendAnswer = false;
                        }
                        break;
                    case SEND_ANSWER:
                        if (!isSendAnswer) {
                            sendAnswer();
                            isSendAnswer = true;
                            currentGameStateTime = System.currentTimeMillis();
                            for (int i = 0; i < maxPlayers; i++) {
                                if (listPlayers[i] != null) {
                                    listPlayers[i].setIsAnswered(false);
                                }
                            }
                        }
                        if (System.currentTimeMillis() - currentGameStateTime > 2000) {
                            gameState = WAIT_NEXT_QUESTION;
                            isSendAnswer = false;
                        }
                        break;
                    case WAIT_NEXT_QUESTION:
                        if (!isNotifyTableState) {
                            isNotifyTableState = true;
                            sendTableState();
                            currentGameStateTime = System.currentTimeMillis();
                            for (int i = 0; i < maxPlayers; i++) {
                                if (listPlayers[i] != null) {
                                    listPlayers[i].setIsAswerRight(false);
                                }
                            }
                        }
                        if (System.currentTimeMillis() - currentGameStateTime > 5000) {
                            isNotifyTableState = false;
                            if (countQuestion > numberQuestion) {
                                gameState = SEND_FINAL_RESULT;
                                countQuestion = 0;
                                isPlay = false;
                                isSendFinalResult = false;
                            } else {
                                startSendQuestion();
                            }
                        }
                        break;
                    case SEND_FINAL_RESULT:
                        if (!isSendFinalResult) {
                            sendFinalResult();
                            isSendFinalResult = true;
                            currentGameStateTime = System.currentTimeMillis();
//                            gameState = WAIT_NEXT_MATCH;
                        }
                        long timeWait = System.currentTimeMillis() - currentGameStateTime;
                        if (timeWait > 15000) {
                            isSendFinalResult = false;
                            countQuestion = 0;
                            gameState = WAIT_NEXT_MATCH;
                            startNewMatch();
                        }
                        break;
                    default:
                        break;
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }
        }
    }

    private void startNewMatch() {
        penaltyMoney = 0;
        List<String> listNames = new LinkedList<String>();
        List<Integer> listPos = new LinkedList<Integer>();
        List<Integer> level = new LinkedList<Integer>();
        List<String> avatar = new LinkedList<String>();
        List<String> fbName = new LinkedList<String>();
        List<Integer> money = new LinkedList<Integer>();
        List<Integer> experience = new LinkedList<Integer>();
        
        numberPlayings = 0;
        for (int i = 0; i < maxPlayers; i++) {
            if (listPlayers[i] != null) {
                Database database = ext.getDatabase();
                UserData userData = database.getUserInfo(listPlayers[i].getName());
                avatar.add(userData.getAvatar());
                money.add(userData.getMoney());
                experience.add(userData.getLevel());
                numberPlayings++;
                listPlayers[i].setState(PipoPlayer.NOT_READY);
                listNames.add(listPlayers[i].getName());
                fbName.add(userData.getNickName());
                listPos.add(1);
                if (listPlayers[i].getExperience() < 100) {
                    level.add(0);
                } else if (listPlayers[i].getExperience() < 1000) {
                    level.add(1);
                } else if (listPlayers[i].getExperience() < 10000) {
                    level.add(2);
                } else if (listPlayers[i].getExperience() < 100000) {
                    level.add(3);
                } else if (listPlayers[i].getExperience() < 500000) {
                    level.add(4);
                } else if (listPlayers[i].getExperience() < 1000000) {
                    level.add(5);
                } else if (listPlayers[i].getExperience() < 2000000) {
                    level.add(6);
                } else if (listPlayers[i].getExperience() < 3000000) {
                    level.add(7);
                } else if (listPlayers[i].getExperience() < 5000000) {
                    level.add(8);
                } else {
                    level.add(9);
                }
            } else {
                listNames.add("0");
                listPos.add(0);
                fbName.add("0");
                money.add(0);
                experience.add(0);
            }
        }
        for (int i = 0; i < maxPlayers; i++) {
            if (listPlayers[i] != null) {
                response.sendNewMatch(avatar, listNames, fbName, listPos, betMoney, tableHost, listPlayers[i].getSeat(), money, experience, level, listPlayers[i].getUser());
            }
        }
    }

    public void onUserLeave(User user) {
        int idLeave = -1;
        boolean isPlayerLeavePlaying = false;
        for (int i = 0; i < maxPlayers; i++) {
            if (listPlayers[i] != null && listPlayers[i].getUser().equals(user)) {
                idLeave = listPlayers[i].getSeat();
                if (idLeave == tableHost) {
                    doTableHostExit();
                }
                if (listPlayers[i].getState() != PipoPlayer.VIEW && isPlay) {
                    penaltyMoney = betMoney * numberPlayings;
                    isPlayerLeavePlaying = true;
                    Database database = ext.getDatabase();
                    listPlayers[idLeave].updateMoney(-penaltyMoney);
                    database.updateUserMoney(listPlayers[idLeave].getName(), listPlayers[idLeave].getMoney());
                    Debug.d("Kiem Tra Name: " + user.getName() + "money" + listPlayers[idLeave].getMoney());
                    Debug.d("Kiem Tra Name: " + listPlayers[idLeave].getName() + "money" + listPlayers[idLeave].getMoney());
                }
                listPlayers[i] = null;
            }
        }
        numberPlayes = 0;
        numberPlayings = 0;
        for (int i = 0; i < maxPlayers; i++) {
            if (listPlayers[i] != null) {
                numberPlayes++;
                if (listPlayers[i].getState() != PipoPlayer.VIEW) {
                    numberPlayings++;
                } else {
                    cantplay = true;
                }
            }
        }
        Debug.d("number player3 = " + numberPlayings);
        if (numberPlayings == 1 && isPlay) {
            Database database = ext.getDatabase();
            UserData userData = database.getUserInfo(user.getName());
            database.updateUserMoney(user.getName(), userData.getMoney() + penaltyMoney);
            gameState = SEND_FINAL_RESULT;
            isSendFinalResult = false;
        }
        if (numberPlayes <= 1) {
            isPlay = false;
            if (numberPlayes <= 0) {
                this.betMoney = 10;
                gameState = WAIT_NEXT_MATCH;
                countQuestion = 0;
            }
        }
        if (idLeave != -1) {
            sendUpdateToLobby(user);
            sendUserDisconnectToLobby(user);
            for (int i = 0; i < maxPlayers; i++) {
                if (listPlayers[i] != null) {
                    ISFSObject res = new SFSObject();
                    res.putInt("player_leave", idLeave);
                    res.putInt("table_host", tableHost);
                    res.putInt("seat", listPlayers[i].getSeat());
                    ext.send("user_disconnect", res, currentRoom.getUserList());
                    Debug.d(res.getDump());
                    break;
                }
            }
        }
    }

    public void sendAnswer() {
        //lay dap an roi gui ve
        currentQuestion.getTrueAnswer();
        ISFSObject res = new SFSObject();
        res.putInt("answer_content", currentQuestion.getTrueAnswer());
        ext.send("true_ans", res, currentRoom.getUserList());
    }

    public void setGameState(int gameState) {
        this.gameState = gameState;
    }

    public int getGameState() {
        return gameState;
    }

    private void sendWaitNextMatch() {
        ISFSObject res = new SFSObject();
        ext.send("next_match", res, currentRoom.getUserList());
    }

    void doReady(User user) {
        PipoPlayer playerReady = null;
        for (int i = 0; i < maxPlayers; i++) {
            if (listPlayers[i] != null && listPlayers[i].getUser().equals(user)) {
                playerReady = listPlayers[i];
                break;
            }
        }
        if (playerReady != null) {
            if (playerReady.getSeat() == tableHost) {
                numberReadys = 0;
                for (int i = 0; i < maxPlayers; i++) {
                    if (listPlayers[i] != null && listPlayers[i].getState() == PipoPlayer.READY) {
                        numberReadys++;
                        if (listPlayers[i].getMoney() < betMoney * numberQuestion) {
                            numberReadys = 0;
                        }
                    }
                }
                if (numberPlayes >= 2 && numberReadys == numberPlayings - 1) {
                    //can ready
                    List<User> listUser = currentRoom.getUserList();
                    response.sendReady(playerReady.getSeat(), playerReady.getState(), listUser);
                    isPlay = true;
                    startSendQuestion();
                    //send question
                    for (int i = 0; i < maxPlayers; i++) {
                        if (listPlayers[i] != null && listPlayers[i].getState() == PipoPlayer.READY) {
                            listPlayers[i].changeReadyState();
                        }
                    }
                } else {
                    response.sendCanNotReady(user);
                }
            } else {
                playerReady.changeReadyState();
                List<User> listUser = currentRoom.getUserList();
                if (playerReady.getMoney() < betMoney * (numberQuestion +1)) {
                    playerReady.setState(PipoPlayer.CAN_NOT_READY);
                }
                response.sendReady(playerReady.getSeat(), playerReady.getState(), listUser);
            }
        }
    }

    private void startSendQuestion() {
        gameState = SEND_QUESTION;
        isSendQuestion = false;
    }

    void doPlayerAnswer(User user, int timeAnswer, int playerAnswer) {
        PipoPlayer pFound = null;
        for (int i = 0; i < maxPlayers; i++) {
            if (listPlayers[i] != null && listPlayers[i].getUser().equals(user)) {
                pFound = listPlayers[i];
                break;
            }
        }
        if (pFound != null && pFound.getState() != PipoPlayer.VIEW) {
            pFound.setIsAnswered(true);
            if (currentQuestion.getTrueAnswer() == playerAnswer) {
                pFound.incrementAnswerRight();
                pFound.setIsAswerRight(true);
            }
            pFound.setTimeAnswer(timeAnswer);
            pFound.setAnsNumber(playerAnswer);
        }
        int numberAnswerd = 0;
        for (int i = 0; i < maxPlayers; i++) {
            if (listPlayers[i] != null && listPlayers[i].isIsAnswered()) {
                numberAnswerd++;
            }
        }
        if (numberAnswerd == numberPlayings) {
            gameState = SEND_ANSWER;
        }
    }

    void setBetTable(int betTable) {
        this.betMoney = betTable;
        if (betMoney * numberQuestion > listPlayers[tableHost].getMoney()) {
            ext.send("can_not_bet", null, listPlayers[tableHost].getUser());
            betMoney = 10;
        }

        response.sendBetTable(betMoney, currentRoom.getPlayersList());
        List<Integer> state = new LinkedList<Integer>();
        for (int i = 0; i < maxPlayers; i++) {
            if (listPlayers[i] != null) {
                listPlayers[i].ResetState();
                state.add(listPlayers[i].getState());
            }
        }
        ISFSObject res = new SFSObject();
        res.putIntArray("reset_user_state", state);
        res.putInt("table_host", tableHost);
        ext.send("change_state", res, currentRoom.getPlayersList());

    }

    private void doTableHostExit() {
        for (int i = 0; i < maxPlayers; i++) {
            if (listPlayers[i] != null) {
                if (listPlayers[i].getState() != PipoPlayer.VIEW && i != tableHost) {
                    tableHost = i;
                    listPlayers[tableHost].setState(PipoPlayer.NOT_READY);
                    break;
                }
            }
        }
    }

    private void sendUpdateUserJoinTable(User user) {
        Room lobbyRoom = ext.getParentZone().getRoomByName("The Lobby");
        List<User> listUsers = lobbyRoom.getUserList();
        List<String> listRoomName = new LinkedList<String>();
        List<Integer> numberplayerinroom = new LinkedList<Integer>();
        String groupName = (String) user.getProperty("group_names");
        List<Room> listroom = ext.getParentZone().getRoomListFromGroup(groupName);
        Database database = ext.getDatabase();
        UserData userData = database.getUserInfo(user.getName());
        listroom.remove(lobbyRoom);
        for (Room r : listroom) {
            listRoomName.add(r.getName());
            numberplayerinroom.add(r.getUserList().size());
        }
        ISFSObject res = new SFSObject();
        res.putUtfString("user_id", user.getName());
        res.putUtfString("fb_name", userData.getNickName());
        res.putUtfString("avatar", userData.getAvatar());
        ext.send("update_user_join_table", res, listUsers);
        Debug.d(res.getDump());
    }
//
//    void UpdateListOnline(List<User> listUsers) {
//        for (int i = 0; i < maxPlayers; i++) {
//            if (listPlayers != null) {
//                Database database = ext.getDatabase();
//                UserData userData = database.getUserInfo(listPlayers[i].getName());
//                ISFSObject resObj = new SFSObject();
//                resObj.putUtfString("fb_name", userData.getNickName());
//                resObj.putUtfString("avatar", userData.getAvatar());
//                resObj.putUtfString("fb_id", userData.getName());
//                ext.send("update_list_online", resObj, listUsers);
//            }
//        }
//    }

    private void sendUserDisconnectToLobby(User user) {
        Room lobbyRoom = ext.getParentZone().getRoomByName("The Lobby");
        List<User> listUsers = lobbyRoom.getUserList();
        String groupName = (String) user.getProperty("group_names");
        List<Room> listroom = ext.getParentZone().getRoomListFromGroup(groupName);
        ISFSObject res = new SFSObject();
        res.putUtfString("fb_id_disconect", user.getName());
        ext.send("update_user_disconnect", res, listUsers);
        Debug.d(res.getDump());
    }

    void findInvitedTable(User user) {
        Room lobbyRoom = ext.getParentZone().getRoomByName("The Lobby");
        String fbIdinvite = (String) user.getProperty("fb_id_invite");
        List<User> listUsers = lobbyRoom.getUserList();

        String groupName = (String) user.getProperty("group_names");
        List<Room> listroom = ext.getParentZone().getRoomListFromGroup(groupName);
        String fbIdinvited = (String) user.getProperty("fb_id_invited");
        fbIdinvited = currentRoom.getName();
        ISFSObject res = new SFSObject();
        res.putUtfString("table_name", fbIdinvited);
        ext.send("table_number_invited", res, user);
        Debug.d(res.getDump());
    }

    void sendKickUser(User user) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
