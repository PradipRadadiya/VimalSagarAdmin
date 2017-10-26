package com.example.grapes_pradip.vimalsagaradmin.model.competition;

@SuppressWarnings("ALL")
public class CompetitionItem {
    private String categoryIcon;
    private String iD;
    private String name;
    private boolean isSelected=false;

    public CompetitionItem(String categoryIcon, String iD, String name, boolean isSelected) {
        this.categoryIcon = categoryIcon;
        this.iD = iD;
        this.name = name;
        this.isSelected = isSelected;
    }



    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setID(String iD) {
        this.iD = iD;
    }

    public String getID() {
        return iD;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
