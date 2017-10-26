package com.example.grapes_pradip.vimalsagaradmin.model.competition;

@SuppressWarnings("ALL")
public class CompetitionQuestionItem {
    private String categoryID;
    private String options;
    private String qType;
    private String question;
    private String iD;

    public CompetitionQuestionItem(String categoryID, String options, String qType, String question, String iD) {
        this.categoryID = categoryID;
        this.options = options;
        this.qType = qType;
        this.question = question;
        this.iD = iD;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getOptions() {
        return options;
    }

    public void setQType(String qType) {
        this.qType = qType;
    }

    public String getQType() {
        return qType;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setID(String iD) {
        this.iD = iD;
    }

    public String getID() {
        return iD;
    }
}
