package com.example.grapes_pradip.vimalsagaradmin.model.user;

@SuppressWarnings("ALL")
public class UsersItem {
    private String emailID;
    private String address;
    private String isActive;
    private String deviceID;
    private String deviceToken;
    private String phone;
    private String regDate;
    private String iD;
    private String name;

    public UsersItem(String emailID, String address, String isActive, String deviceID, String deviceToken, String phone, String regDate, String iD, String name) {
        this.emailID = emailID;
        this.address = address;
        this.isActive = isActive;
        this.deviceID = deviceID;
        this.deviceToken = deviceToken;
        this.phone = phone;
        this.regDate = regDate;
        this.iD = iD;
        this.name = name;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getRegDate() {
        return regDate;
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
}
