package com.example.grapes_pradip.vimalsagaradmin.model.gallery;

/**
 * Created by Grapes-Pradip on 12/27/2016.
 */

@SuppressWarnings("ALL")
public class ImageItemSplash {
    private String image;
    private String imageUrl;
    private boolean isSelected=false;


    public ImageItemSplash(String image, String imageUrl, boolean isSelected) {
        this.image = image;
        this.imageUrl = imageUrl;
        this.isSelected = isSelected;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
