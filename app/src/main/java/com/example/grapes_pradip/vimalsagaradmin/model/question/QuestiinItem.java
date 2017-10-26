package com.example.grapes_pradip.vimalsagaradmin.model.question;

@SuppressWarnings("ALL")
public class QuestiinItem {
    private String answer;
    private String isApproved;
    private String userID;
    private String question;
    private String iD;
    private String date;
    private String name;
    private String view;
    private boolean isSelected=false;


    public QuestiinItem(String answer, String isApproved, String userID, String question, String iD, String date, String name, String view, boolean isSelected) {
        this.answer = answer;
        this.isApproved = isApproved;
        this.userID = userID;
        this.question = question;
        this.iD = iD;
        this.date = date;
        this.name = name;
        this.view = view;
        this.isSelected = isSelected;
    }


    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setID(String iD) {
        this.iD = iD;
    }

    public String getID() {
        return iD;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
