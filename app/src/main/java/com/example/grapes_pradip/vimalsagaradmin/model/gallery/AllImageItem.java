package com.example.grapes_pradip.vimalsagaradmin.model.gallery;

@SuppressWarnings("ALL")
public class AllImageItem {

    private String iD;
    private String cID;
    private String photo;
    private String date;
    private boolean isSelected=false;


    public AllImageItem(String iD, String cID, String photo, String date, boolean isSelected) {
        this.iD = iD;
        this.cID = cID;
        this.photo = photo;
        this.date = date;
        this.isSelected = isSelected;
    }



    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
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

    public void setCID(String cID) {
        this.cID = cID;
    }

    public String getCID() {
        return cID;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
