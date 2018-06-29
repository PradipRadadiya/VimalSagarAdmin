package com.example.grapes_pradip.vimalsagaradmin.model;



@SuppressWarnings("ALL")
public class SlideImageItem {

    private String id,image;

    public SlideImageItem(String id, String image) {
        this.id = id;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
