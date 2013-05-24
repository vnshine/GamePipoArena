/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extension.pipoarena.room;

import com.smartfoxserver.v2.entities.data.ISFSObject;

/**
 *
 * @author Vnshine
 */
public class Question {

    private String content;
    private int id;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private int trueanswer;
    private int idquestion;

    public Question(String content, int id) {
        this.content = content;
        this.id = id;
    }

    Question(ISFSObject object) {
        this.content = object.getUtfString("QUESTION_CONTENT");
        this.idquestion = object.getInt("ID_QUESTION");
        this.answer1 = object.getUtfString("QUESTION_1");
        this.answer2 = object.getUtfString("QUESTION_2");
        this.answer3 = object.getUtfString("QUESTION_3");
        this.answer4 = object.getUtfString("QUESTION_4");
        this.trueanswer = object.getInt("ANSWER_CONTENT");
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer1 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public int getTrueAnswer() {
        return trueanswer;
    }

    public void resetTrueAnsewr() {
        trueanswer = 4;
    }

    public int getIdQuestion() {
        return idquestion;
    }

    public void setIdQuestion(int idquestion) {
        this.idquestion = idquestion;
    }

    public void setTrueAnser(int trueanswer) {
        this.trueanswer = trueanswer;
    }
}
