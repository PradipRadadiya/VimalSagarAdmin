package com.example.grapes_pradip.vimalsagaradmin.model.competition;

/**
 * Created by Grapes-Pradip on 3/6/2017.
 */

@SuppressWarnings("ALL")
public class AnswerItem {

    private String id;
    private String qid;
    private String cid;
    private String userid;
    private String answer;
    private String name;

    public AnswerItem(String id, String qid, String cid, String userid, String answer, String name) {
        this.id = id;
        this.qid = qid;
        this.cid = cid;
        this.userid = userid;
        this.answer = answer;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
