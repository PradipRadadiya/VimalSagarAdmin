package com.example.grapes_pradip.vimalsagaradmin.model.competition;

@SuppressWarnings("ALL")
public class OptionItem {
    private String categoryID;
    private String qType;
    private String optionValue;
    private String question;
    private String iD;
    private String qID;

    public OptionItem(String categoryID, String qType, String optionValue, String question, String iD, String qID) {
        this.categoryID = categoryID;
        this.qType = qType;
        this.optionValue = optionValue;
        this.question = question;
        this.iD = iD;
        this.qID = qID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setQType(String qType) {
        this.qType = qType;
    }

    public String getQType() {
        return qType;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public String getOptionValue() {
        return optionValue;
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

    public void setQID(String qID) {
        this.qID = qID;
    }

    public String getQID() {
        return qID;
    }

    @Override
    public String toString() {
        return
                "OptionItem{" +
                        "categoryID = '" + categoryID + '\'' +
                        ",qType = '" + qType + '\'' +
                        ",optionValue = '" + optionValue + '\'' +
                        ",question = '" + question + '\'' +
                        ",iD = '" + iD + '\'' +
                        ",qID = '" + qID + '\'' +
                        "}";
    }
}
