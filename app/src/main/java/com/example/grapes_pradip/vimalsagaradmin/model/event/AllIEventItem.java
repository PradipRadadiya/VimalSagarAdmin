package com.example.grapes_pradip.vimalsagaradmin.model.event;

/**
 * Created by Grapes-Pradip on 2/15/2017.
 */

@SuppressWarnings("ALL")
public class AllIEventItem {

    private String id;
    private String title;
    private String description;
    private String address;
    private String date;
    private String audio;
    private String audioimage;
    private String videolink;
    private String video;
    private String videoimage;
    private String photo;
    private String view;
    private boolean isSelected = false;


    public AllIEventItem(String id, String title, String description, String address, String date, String audio, String audioimage, String videolink, String video, String videoimage, String photo, String view, boolean isSelected) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.address = address;
        this.date = date;
        this.audio = audio;
        this.audioimage = audioimage;
        this.videolink = videolink;
        this.video = video;
        this.videoimage = videoimage;
        this.photo = photo;
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

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getAudioimage() {
        return audioimage;
    }

    public void setAudioimage(String audioimage) {
        this.audioimage = audioimage;
    }

    public String getVideolink() {
        return videolink;
    }

    public void setVideolink(String videolink) {
        this.videolink = videolink;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideoimage() {
        return videoimage;
    }

    public void setVideoimage(String videoimage) {
        this.videoimage = videoimage;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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
