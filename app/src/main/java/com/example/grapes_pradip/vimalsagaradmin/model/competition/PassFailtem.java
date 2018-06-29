package com.example.grapes_pradip.vimalsagaradmin.model.competition;

@SuppressWarnings("ALL")
public class PassFailtem {
    private String user_total_mark;
    private String competition_id;
    private String total_mark;
    private String taken_time;
    private String Name;
    private String user_id;


    public PassFailtem(String user_total_mark, String competition_id, String total_mark, String taken_time, String name, String user_id) {
        this.user_total_mark = user_total_mark;
        this.competition_id = competition_id;
        this.total_mark = total_mark;
        this.taken_time = taken_time;
        Name = name;
        this.user_id = user_id;
    }

    public String getUser_total_mark() {
        return user_total_mark;
    }

    public void setUser_total_mark(String user_total_mark) {
        this.user_total_mark = user_total_mark;
    }

    public String getCompetition_id() {
        return competition_id;
    }

    public void setCompetition_id(String competition_id) {
        this.competition_id = competition_id;
    }

    public String getTotal_mark() {
        return total_mark;
    }

    public void setTotal_mark(String total_mark) {
        this.total_mark = total_mark;
    }

    public String getTaken_time() {
        return taken_time;
    }

    public void setTaken_time(String taken_time) {
        this.taken_time = taken_time;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
