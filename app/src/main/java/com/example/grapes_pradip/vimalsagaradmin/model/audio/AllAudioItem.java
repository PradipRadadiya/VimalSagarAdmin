package com.example.grapes_pradip.vimalsagaradmin.model.audio;

/**
 * Created by Grapes-Pradip on 2/18/2017.
 */

@SuppressWarnings("ALL")
public class AllAudioItem {

    private String id;
    private String audioname;
    private String categoryid;
    private String audio;
    private String photo;
    private String duration;
    private String date;
    private String categoryname;
    private String view;
    private boolean isSelected=false;


    public AllAudioItem(String id, String audioname, String categoryid, String audio, String photo, String duration, String date, String categoryname, String view, boolean isSelected) {
        this.id = id;
        this.audioname = audioname;
        this.categoryid = categoryid;
        this.audio = audio;
        this.photo = photo;
        this.duration = duration;
        this.date = date;
        this.categoryname = categoryname;
        this.view = view;
        this.isSelected = isSelected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAudioname() {
        return audioname;
    }

    public void setAudioname(String audioname) {
        this.audioname = audioname;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
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
