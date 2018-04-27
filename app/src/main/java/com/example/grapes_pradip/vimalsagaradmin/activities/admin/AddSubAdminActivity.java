package com.example.grapes_pradip.vimalsagaradmin.activities.admin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

/**
 * Created by Grapes-Pradip on 2/16/2017.
 */

@SuppressWarnings("ALL")
public class AddSubAdminActivity extends AppCompatActivity {

    private EditText entername;
    private EditText eemail;
    private EditText emobile;
    private EditText eusername;
    private EditText epassword;
    private EditText ecpassword;
    private Button register;
    private ProgressDialog progressDialog;
    private TextView txt_header;
    private ImageView img_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_sub_admin);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        findID();

    }

    @SuppressLint("SetTextI18n")
    private void findID() {
        entername = (EditText) findViewById(R.id.entername);
        eemail = (EditText) findViewById(R.id.eemail);
        emobile = (EditText) findViewById(R.id.emobile);
        eusername = (EditText) findViewById(R.id.eusername);
        epassword = (EditText) findViewById(R.id.epassword);
        ecpassword = (EditText) findViewById(R.id.ecpassword);
        register = (Button) findViewById(R.id.register);
        txt_header = (TextView) findViewById(R.id.txt_header);
        img_back = (ImageView) findViewById(R.id.img_back);
        txt_header.setText("Add Admin");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(entername.getText().toString())) {
                    entername.setError("Please enter name.");
                    entername.requestFocus();
                } else if (TextUtils.isEmpty(eemail.getText().toString())) {
                    eemail.setError("Please enter email.");
                } else if (!isValidEmail(eemail.getText().toString().trim())) {
                    eemail.setError("Please enter valid email address.");
                    eemail.requestFocus();
                } else if (TextUtils.isEmpty(emobile.getText().toString())) {
                    emobile.setError("Please enter mobile mo.");
                    emobile.requestFocus();
                } else if (emobile.getText().toString().trim().length() < 10) {
                    emobile.setError("Please enter 10 digit.");
                    emobile.requestFocus();
                } else if (TextUtils.isEmpty(eusername.getText().toString())) {
                    eusername.setError("Please enter username.");
                    eusername.requestFocus();
                } else if (TextUtils.isEmpty(epassword.getText().toString())) {
                    epassword.setError("Please enter password.");
                    epassword.requestFocus();

                } else if (TextUtils.isEmpty(ecpassword.getText().toString())) {
                    ecpassword.setError("Please enter confirm password.");
                    ecpassword.requestFocus();

                }else if (!epassword.getText().toString().equalsIgnoreCase(ecpassword.getText().toString())) {
                    ecpassword.setError("Confirm password not same.");
                    ecpassword.requestFocus();

                } else {
                    if (CommonMethod.isInternetConnected(AddSubAdminActivity.this)) {
                        new ChechAdminUsername().execute(eusername.getText().toString());
                    } else {
                        Toast.makeText(AddSubAdminActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    private class ChechAdminUsername extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AddSubAdminActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("username", params[0]));

                responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + CommonAPI_Name.checkAdminUsername, nameValuePairs, AddSubAdminActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "---------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("error")) {
                    progressDialog.dismiss();
//                    Toast.makeText(AddSubAdminActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    new AddAdmin().execute(entername.getText().toString(), eemail.getText().toString(), eusername.getText().toString(), emobile.getText().toString(), epassword.getText().toString());
                } else {
                    Toast.makeText(AddSubAdminActivity.this, "Username already exists.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }

    private boolean isValidEmail(String email) {
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

    private class AddAdmin extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AddSubAdminActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("role", "sub"));
                nameValuePairs.add(new BasicNameValuePair("name", params[0]));
                nameValuePairs.add(new BasicNameValuePair("email", params[1]));
                nameValuePairs.add(new BasicNameValuePair("username", params[2]));
                nameValuePairs.add(new BasicNameValuePair("mobile", params[3]));
                nameValuePairs.add(new BasicNameValuePair("password", params[4]));
                responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + CommonAPI_Name.addAdmin, nameValuePairs, AddSubAdminActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "---------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    Toast.makeText(AddSubAdminActivity.this, "Admin added successfully.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(AddSubAdminActivity.this, "Success" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddSubAdminActivity.this, "Admin not added.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(AddSubAdminActivity.this, "Success" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

}
