package com.example.grapes_pradip.vimalsagaradmin.model;

public class AllCommentItem {

    private String id, Title, Comment, Name, module_name, date,cid;

    public AllCommentItem(String id, String title, String comment, String name, String module_name, String date, String cid) {
        this.id = id;
        Title = title;
        Comment = comment;
        Name = name;
        this.module_name = module_name;
        this.date = date;
        this.cid = cid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getModule_name() {
        return module_name;
    }

    public void setModule_name(String module_name) {
        this.module_name = module_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
