package com.example.grapes_pradip.vimalsagaradmin.model.information;



@SuppressWarnings("ALL")
public class LikeList {

    private String id;
    private String informationID;
    private String userID;
    private String ame;

    public LikeList(String id, String informationID, String userID, String ame) {
        this.id = id;
        this.informationID = informationID;
        this.userID = userID;
        this.ame = ame;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInformationID() {
        return informationID;
    }

    public void setInformationID(String informationID) {
        this.informationID = informationID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAme() {
        return ame;
    }

    public void setAme(String ame) {
        this.ame = ame;
    }
}
