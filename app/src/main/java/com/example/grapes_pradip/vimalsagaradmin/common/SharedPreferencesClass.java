package com.example.grapes_pradip.vimalsagaradmin.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by Pradip on 16-Nov-16.
 */


@SuppressWarnings("ALL")
public class SharedPreferencesClass {
    private final SharedPreferences sharedPreferences;
    private final Editor editor;
    private static final String SHARED = "DEMO";
    private static final String User_Id = "Id";
    private static final String First_name = "First_name";
    private static final String Email = "Email";
    private static final String Mobile = "Mobile";
    private static final String Username = "Username";
    private static final String Dates = "Dates";
    private static final String Role = "Role";
    private static final String Password = "Password";
    private static final String PushNotification = "PushNotification";
    private static final String ActionClick = "ActionClick";


    @SuppressLint("CommitPrefEdits")
    public SharedPreferencesClass(Context mContext) {
        sharedPreferences = mContext.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    //Get and Save User Id
    public String getUser_Id() {
        return sharedPreferences.getString(User_Id, "");
    }

    public void saveUser_Id(String id) {
        editor.putString(User_Id, id);
        editor.commit();
    }

    //Get and Save FirstName
    public String getFirst_name() {
        return sharedPreferences.getString(First_name, "");
    }

    public void saveFirst_name(String fnm) {
        editor.putString(First_name, fnm);
        editor.commit();
    }

    //Get and Save Email
    public String getEmail() {
        return sharedPreferences.getString(Email, "");
    }

    public void saveEmail(String em) {
        editor.putString(Email, em);
        editor.commit();
    }

    //Get and Save LastName
    public String getUsername() {
        return sharedPreferences.getString(Username, "");
    }

    public void saveUsername(String unm) {
        editor.putString(Username, unm);
        editor.commit();
    }

    //Get and Save Status
    public String getDates() {
        return sharedPreferences.getString(Dates, "");
    }

    public void saveDates(String dt) {
        editor.putString(Dates, dt);
        editor.commit();
    }

    //Get and Save Photo
    public String getRole() {
        return sharedPreferences.getString(Role, "");
    }

    public void saveRole(String role) {
        editor.putString(Role, role);
        editor.commit();
    }

    //Get and Save Mobile
    public String getMobile() {
        return sharedPreferences.getString(Mobile, "");
    }

    public void saveMobile(String mobile) {
        editor.putString(Mobile, mobile);
        editor.commit();
    }


    //Get and Save Mobile
    public String getPassword() {
        return sharedPreferences.getString(Password, "");
    }

    public void savePassword(String pwd) {
        editor.putString(Password, pwd);
        editor.commit();
    }

    //Get and Save Mobile
    public String getPushNotification() {
        return sharedPreferences.getString(PushNotification, "");
    }

    public void savePushNotification(String notification) {
        editor.putString(PushNotification, notification);
        editor.commit();
    }

    //Get and Save Mobile
    public String getActionClick() {
        return sharedPreferences.getString(ActionClick, "");
    }

    public void saveActionClick(String click) {
        editor.putString(ActionClick, click);
        editor.commit();
    }

}
