package com.example.grapes_pradip.vimalsagaradmin.model.gallery;

/**
 * Created by Grapes-Pradip on 3/1/2017.
 */

@SuppressWarnings("ALL")
public class GalleryCategoryItem {
    private String id;
    private String name;
    private String categoryIcon;
    private boolean isSelected=false;
    private String description;


    public GalleryCategoryItem(String id, String name, String categoryIcon, boolean isSelected, String description) {
        this.id = id;
        this.name = name;
        this.categoryIcon = categoryIcon;
        this.isSelected = isSelected;
        this.description = description;
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

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
