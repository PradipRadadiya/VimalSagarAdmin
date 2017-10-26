package com.example.grapes_pradip.vimalsagaradmin.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Pradip on 16-Nov-16.
 */

@SuppressWarnings("ALL")
public class CommonMethod {
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static Date convert_date(String date) {
        Date mDate = null;
        try {
            mDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mDate;
    }

    public static String giveDate(String time) {
        Calendar cal = Calendar.getInstance();
        return sdf.format(cal.getTime());
    }
}
