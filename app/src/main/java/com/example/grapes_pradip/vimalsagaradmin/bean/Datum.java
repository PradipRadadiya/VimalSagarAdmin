
package com.example.grapes_pradip.vimalsagaradmin.bean;

import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("ID")
    @Expose
    private String iD;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("View")
    @Expose
    private String view;
    @SerializedName("Photo")
    @Expose
    private String photo;
    @SerializedName("viewed_user")
    @Expose
    private String viewedUser;

    private boolean isSelected = false;



    public String getID() {
        return iD;
    }

    public void setID(String iD) {
        this.iD = iD;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getViewedUser() {
        return viewedUser;
    }

    public void setViewedUser(String viewedUser) {
        this.viewedUser = viewedUser;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


}
