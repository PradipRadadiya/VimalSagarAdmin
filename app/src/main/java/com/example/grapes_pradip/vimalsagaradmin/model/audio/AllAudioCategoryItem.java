package com.example.grapes_pradip.vimalsagaradmin.model.audio;



@SuppressWarnings("ALL")
public class AllAudioCategoryItem {

    private String id;
    private String name;
    private String categoryicon;
    private boolean isSelected=false;


    public AllAudioCategoryItem(String id, String name, String categoryicon, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.categoryicon = categoryicon;
        this.isSelected = isSelected;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryicon() {
        return categoryicon;
    }

    public void setCategoryicon(String categoryicon) {
        this.categoryicon = categoryicon;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

