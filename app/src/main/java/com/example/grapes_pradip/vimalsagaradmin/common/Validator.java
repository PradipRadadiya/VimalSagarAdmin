package com.example.grapes_pradip.vimalsagaradmin.common;

import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SuppressWarnings("ALL")
class Validator {
    public static boolean checkEmpty(EditText edt) {
        if (edt.getText().toString().isEmpty()) {
            edt.setError("field required");
            edt.setFocusable(true);
            edt.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean checkLimit(EditText edt) {
        if (edt.getText().toString().length() > 50) {
            edt.setError("field required");
            edt.setFocusable(true);
            edt.requestFocus();

            return false;
        }
        return true;
    }

    public static boolean checkDescLimit(EditText edt) {
        if (edt.getText().toString().length() > 300) {
            edt.setError("field required");
            edt.setFocusable(true);
            edt.requestFocus();

            return false;
        }
        return true;
    }

    public static boolean checkPhnno(EditText edt) {

        String PHONE_PATTERN = "\\d{4}([- ]*)\\d{6}";
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(edt.getText().toString());
        boolean flg = matcher.matches();
        if (!flg) {
            edt.setFocusable(true);
            edt.requestFocus();
            edt.setError("Phone Number is not valid");
            return false;
        }
        return true;

    }

    public static boolean checkEmail(EditText edt) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(edt.getText().toString());

        boolean flg = matcher.matches();
        if (!flg) {
            edt.setFocusable(true);
            edt.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean checkAlphaNumeric(EditText edt) {
        String PATTERN = "[-\\p{Alnum}]+";
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(edt.getText().toString());
        boolean flg = matcher.matches();
        if (!flg) {
            edt.setFocusable(true);
            edt.requestFocus();
            return false;
        }

        return true;
    }

    public static boolean checkOnlyAlpha(EditText edt) {
        String PATTERN = "[a-zA-z\\s]*";
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(edt.getText().toString());
        boolean flg = matcher.matches();
        if (!flg) {
            edt.setFocusable(true);
            edt.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean checkPhn(EditText edt) {
        String PATTERN = "^\\(?([0-9]{3})\\)?[-.\\\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$";
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(edt.getText().toString());
        boolean flg = matcher.matches();
        Log.e("---", "-" + matcher.matches());
        if (!flg) {
            edt.setFocusable(true);
            edt.requestFocus();
            return false;
        }
        return true;
    }


    public static boolean checkPasswordLength(EditText reg_password) {
        String value = reg_password.getText().toString();
        int password_length = value.length();
        if (password_length < 8) {
            reg_password.setFocusable(true);
            reg_password.requestFocus();
            return false;
        }
        if (password_length > 25) {
            reg_password.setFocusable(true);
            reg_password.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean checkPassword(EditText edt) {
        String PATTERN = "^(?=.*\\d)(?=.*[a-zA-Z]).{8,25}$";
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(edt.getText().toString());
        boolean flg = matcher.matches();
        if (!flg) {
            edt.setFocusable(true);
            edt.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean checkBitmapNullOrNot(ImageView imageView) {
        //noinspection RedundantIfStatement
        if (imageView.getDrawable() == null) {
            return false;
        }
        return true;
    }

    public static boolean checkAdult(int year, int month, int day) {
        Calendar userAge = new GregorianCalendar(year, month, day);
        Calendar minAdultAge = new GregorianCalendar();
        minAdultAge.add(Calendar.YEAR, -18);
        //noinspection RedundantIfStatement
        if (minAdultAge.before(userAge)) {
            return false;
        }
        return true;
    }

    public static boolean isValidEmail(String email) {
        boolean isValidEmail = false;

        String emailExpression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(emailExpression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValidEmail = true;
        }
        return isValidEmail;
    }

}
