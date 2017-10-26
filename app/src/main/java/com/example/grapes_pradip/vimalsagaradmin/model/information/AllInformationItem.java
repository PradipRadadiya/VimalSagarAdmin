package com.example.grapes_pradip.vimalsagaradmin.model.information;

/**
 * Created by Grapes-Pradip on 2/15/2017.
 */

@SuppressWarnings("ALL")
public class AllInformationItem {

    private String id;
    private String title;
    private String discription;
    private String address;
    private String date;
    private String view;
    private boolean isSelected = false;



    public AllInformationItem(String id, String title, String discription, String address, String date, String view, boolean isSelected) {
        this.id = id;
        this.title = title;
        this.discription = discription;
        this.address = address;
        this.date = date;
        this.view = view;
        this.isSelected = isSelected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }
}
