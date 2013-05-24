/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extension.pipoarena.room;

import com.smartfoxserver.v2.entities.User;
import log.Debug;

/**
 *
 * @author Vnshine
 */
public class PipoPlayer {

    public static final int NOT_READY = 1;
    public static final int READY = 2;
    public static final int ASWERING = 3;
    public static final int ASWERED = 4;
    public static final int VIEW = 5;
    public static final int CAN_NOT_READY = 6;
    private String name;
    private int seat;
    private User user;
    private int money;
    private int experience;
    public int betTable;
    private int state;
    private int timeAnswer;
    private int ansnumber;
    private boolean isAswerRight;
    private boolean isAnswered;
    private int points;
    private int numberAnswerRights;
    private String avatar;
    private String nickname;

    public PipoPlayer(String name,String nickname,String avatar, int moneyJoin, int exp, int seat, User user) {
        this.name = name;
        this.seat = seat;
        this.user = user;
        this.money = moneyJoin;
        this.experience = exp;
        this.nickname = nickname;
        this.avatar = avatar;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    public String getNickName() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the seat
     */
    public int getSeat() {
        return seat;
    }

    /**
     * @param seat the seat to set
     */
    public void setSeat(int seat) {
        this.seat = seat;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setTimeAnswer(int timeAnswer) {
        this.timeAnswer = timeAnswer;
    }

    public int getTimeAnswer() {
        return timeAnswer;
    }
    public void resetTimeAnswer(){
    this.timeAnswer = 20000;
    }
    public void setAnsNumber(int ansnumber) {
        this.ansnumber = ansnumber;
    }

    public int getAnsNumber() {
        return ansnumber;
    }

    public void setIsAswerRight(boolean isAswerRight) {
        this.isAswerRight = isAswerRight;
    }

    public void setNewPlayerMoney(int money) {
        this.money = money;
    }

    public boolean isIsAswerRight() {
        return isAswerRight;
    }

    public void resetAnswerResult() {
        timeAnswer = 0;
        isAswerRight = false;
    }

    void changeReadyState() {
        state = 3 - state;
    }

    public void ResetState() {
        state = 1;
    }

    public void setIsAnswered(boolean isAnswered) {
        this.isAnswered = isAnswered;
    }

    public boolean isIsAnswered() {
        return isAnswered;
    }

    public int getPoints() {
        return points;
    }

    public void resetPoint() {
        this.points = 0;
    }

    public void updatePoint(int delta) {
        this.points += delta;
        Debug.d(name + ":point=" + points);
    }

    public void setNumberAnswerRights(int numberAnswerRights) {
        this.numberAnswerRights = numberAnswerRights;
    }

    public int getNumberAnswerRights() {
        return numberAnswerRights;
    }

    public void incrementAnswerRight() {
        this.numberAnswerRights++;
    }

    public int getMoney() {
        return money;
    }

    public void setBetTable(int bettable) {
        this.betTable = bettable;
    }

    public int getBetTable() {
        return betTable;
    }

    void updateMoney(long delta) {
        money += delta;
    }

    public int getExperience() {
        return experience;
    }

    public void updateExp(long exp) {
        experience += exp;
    }
}
