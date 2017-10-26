package com.example.grapes_pradip.vimalsagaradmin.model.opinionpoll;

@SuppressWarnings("ALL")
public class OpinionItem {
    private String totalPolls;
    private String ques;
    private String noPolls;
    private String yesPolls;
    private String iD;
    private boolean isSelected=false;


    public OpinionItem(String totalPolls, String ques, String noPolls, String yesPolls, String iD, boolean isSelected) {
        this.totalPolls = totalPolls;
        this.ques = ques;
        this.noPolls = noPolls;
        this.yesPolls = yesPolls;
        this.iD = iD;
        this.isSelected = isSelected;
    }

    public void setTotalPolls(String totalPolls) {
        this.totalPolls = totalPolls;
    }

    public String getTotalPolls() {
        return totalPolls;
    }

    public void setQues(String ques) {
        this.ques = ques;
    }

    public String getQues() {
        return ques;
    }

    public void setNoPolls(String noPolls) {
        this.noPolls = noPolls;
    }

    public String getNoPolls() {
        return noPolls;
    }

    public void setYesPolls(String yesPolls) {
        this.yesPolls = yesPolls;
    }

    public String getYesPolls() {
        return yesPolls;
    }

    public void setID(String iD) {
        this.iD = iD;
    }

    public String getID() {
        return iD;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
