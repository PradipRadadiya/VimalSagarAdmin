package com.example.grapes_pradip.vimalsagaradmin.model;

@SuppressWarnings("ALL")
public class VicharItem {
    private String iD;
    private String content;
    private String date;

    public VicharItem(String iD, String content, String date) {
        this.iD = iD;
        this.content = content;
        this.date = date;
    }

    public String getiD() {
        return iD;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
