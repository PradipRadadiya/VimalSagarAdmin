package com.example.grapes_pradip.vimalsagaradmin.model.event;

import android.graphics.Bitmap;

/**
 * Created by Grapes-Pradip on 17-Oct-17.
 */

public class EventImage {

    String ID, EventID, Photo;
    Bitmap bitmap;
    private boolean isSelected = false;

    public EventImage(String ID, String eventID, String photo, Bitmap bitmap, boolean isSelected) {
        this.ID = ID;
        EventID = eventID;
        Photo = photo;
        this.bitmap = bitmap;
        this.isSelected = isSelected;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getEventID() {
        return EventID;
    }

    public void setEventID(String eventID) {
        EventID = eventID;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
