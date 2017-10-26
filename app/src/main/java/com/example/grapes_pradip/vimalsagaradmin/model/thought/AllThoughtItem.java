package com.example.grapes_pradip.vimalsagaradmin.model.thought;

/**
 * Created by Grapes-Pradip on 2/20/2017.
 */

@SuppressWarnings("ALL")
public class AllThoughtItem {

    private String id;
    private String title;
    private String description;
    private String date;
    private String view;
    private boolean isSelected=false;

    public AllThoughtItem(String id, String title, String description, String date, String view, boolean isSelected) {
        this.id = id;
        this.title = title;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
