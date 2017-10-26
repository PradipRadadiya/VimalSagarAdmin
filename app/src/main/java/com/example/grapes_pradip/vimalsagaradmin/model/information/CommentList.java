package com.example.grapes_pradip.vimalsagaradmin.model.information;

@SuppressWarnings("ALL")
public class CommentList {
    private String comment;
    private String isApproved;
    private String userID;
    private String informationID;
    private String iD;
    private String date;
    private String name;

    public CommentList(String comment, String isApproved, String userID, String informationID, String iD, String date, String name) {
        this.comment = comment;
        this.isApproved = isApproved;
        this.userID = userID;
        this.informationID = informationID;
        this.iD = iD;
        this.date = date;
        this.name = name;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
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

    public void setInformationID(String informationID) {
        this.informationID = informationID;
    }

    public String getInformationID() {
        return informationID;
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
}
