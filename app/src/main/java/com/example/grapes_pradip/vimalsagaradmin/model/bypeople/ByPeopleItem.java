package com.example.grapes_pradip.vimalsagaradmin.model.bypeople;

@SuppressWarnings("ALL")
public class ByPeopleItem {
    private String post;
    private String title;
    private String videoImage;
    private String photo;
    private String date;
    private String name;
    private String audioImage;
    private String videoLink;
    private String isApproved;
    private String video;
    private String userID;
    private String iD;
    private String audio;
    private String view;

    public ByPeopleItem(String post, String title, String videoImage, String photo, String date, String name, String audioImage, String videoLink, String isApproved, String video, String userID, String iD, String audio,String view) {
        this.post = post;
        this.title = title;
        this.videoImage = videoImage;
        this.photo = photo;
        this.date = date;
        this.name = name;
        this.audioImage = audioImage;
        this.videoLink = videoLink;
        this.isApproved = isApproved;
        this.video = video;
        this.userID = userID;
        this.iD = iD;
        this.audio = audio;
        this.view=view;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getPost() {
        return post;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setVideoImage(String videoImage) {
        this.videoImage = videoImage;
    }

    public String getVideoImage() {
        return videoImage;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
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

    public void setAudioImage(String audioImage) {
        this.audioImage = audioImage;
    }

    public String getAudioImage() {
        return audioImage;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideo() {
        return video;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setID(String iD) {
        this.iD = iD;
    }

    public String getID() {
        return iD;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getAudio() {
        return audio;
    }

    public String getiD() {
        return iD;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }
}
