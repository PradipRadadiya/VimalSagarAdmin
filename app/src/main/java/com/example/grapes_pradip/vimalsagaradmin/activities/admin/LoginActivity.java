package com.example.grapes_pradip.vimalsagaradmin.activities.admin;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.activities.MainActivity;
import com.example.grapes_pradip.vimalsagaradmin.activities.bypeople.ByPeopleDetailActivity;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.common.SharedPreferencesClass;
import com.example.grapes_pradip.vimalsagaradmin.fcm.Config;
import com.example.grapes_pradip.vimalsagaradmin.fcm.NotificationUtils;
import com.example.grapes_pradip.vimalsagaradmin.retrofit.APIClient;
import com.example.grapes_pradip.vimalsagaradmin.retrofit.ApiInterface;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@SuppressWarnings("ALL")
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edit_unm;
    private EditText edit_pwd;
    private Button btn_login;
    private ProgressDialog progressDialog;
    private SharedPreferencesClass sharedPreferencesClass;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sharedPreferencesClass = new SharedPreferencesClass(LoginActivity.this);
        findID();
        idClick();
        //Push Notification
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_SHORT).show();

                    Log.e("message", "---------------" + message);
                }
            }
        };

        displayFirebaseRegId();
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

//        Log.e("Firebase reg id: ", regId);

        if (!TextUtils.isEmpty(regId)) {
//            txtRegId.setText("Firebase Reg Id: " + regId);
            Log.e("Firebase reg id: ", regId);
            token = regId;
        } else {
            Log.e("Firebase Reg Id is not received yet!", "");
        }
    }

    private void findID() {
        edit_unm = (EditText) findViewById(R.id.edit_unm);
        edit_pwd = (EditText) findViewById(R.id.edit_pwd);
        btn_login = (Button) findViewById(R.id.btn_login);
    }

    private void idClick() {
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (TextUtils.isEmpty(edit_unm.getText().toString())) {
                    edit_unm.setError("Please enter username.");
                    edit_unm.requestFocus();
                } else if (TextUtils.isEmpty(edit_pwd.getText().toString())) {
                    edit_pwd.setError("Please enter password.");
                    edit_pwd.requestFocus();
                } else {
                    if (CommonMethod.isInternetConnected(LoginActivity.this)) {
//                        new Login().execute(edit_unm.getText().toString(), edit_pwd.getText().toString(),token);
                        adminLogin(edit_unm.getText().toString(), edit_pwd.getText().toString(),token);
                    } else {
                        Toast.makeText(LoginActivity.this, "Internet not connected.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void adminLogin(String username,String password,String token){
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);
        Call<JsonObject> callApi = apiInterface.login(username, password, token);
        callApi.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                progressDialog.dismiss();
                Log.e("reponse", "-----------------" + response.body());
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                            progressDialog.dismiss();

                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("ID");
                                String name = object.getString("Name");
                                String email = object.getString("Email");
                                String mobile = object.getString("Mobile");
                                String username = object.getString("Username");
                                String password = object.getString("Password");
                                String date = object.getString("LastUpdated");
                                String role = object.getString("Role");
                                sharedPreferencesClass.saveUser_Id(id);
                                sharedPreferencesClass.saveFirst_name(name);
                                sharedPreferencesClass.saveEmail(email);
                                sharedPreferencesClass.saveMobile(mobile);
                                sharedPreferencesClass.saveUsername(username);
                                sharedPreferencesClass.savePassword(password);
                                sharedPreferencesClass.saveDates(date);
                                sharedPreferencesClass.saveRole(role);
                                sharedPreferencesClass.savePushNotification("pushon");
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
//                        new AddToken().execute(token);

                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid username and password.", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Log.e("msg","---------"+t.getMessage());
                Toast.makeText(LoginActivity.this, R.string.reopen, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToken(){

    }

}
