package com.example.grapes_pradip.vimalsagaradmin.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.activities.admin.LoginActivity;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.common.SharedPreferencesClass;
import com.example.grapes_pradip.vimalsagaradmin.fcm.Config;
import com.example.grapes_pradip.vimalsagaradmin.fcm.NotificationUtils;
import com.example.grapes_pradip.vimalsagaradmin.fragments.AudioFragment;
import com.example.grapes_pradip.vimalsagaradmin.fragments.ByPeopleFragment;
import com.example.grapes_pradip.vimalsagaradmin.fragments.CompetitionFragment;
import com.example.grapes_pradip.vimalsagaradmin.fragments.DesktopFragment;
import com.example.grapes_pradip.vimalsagaradmin.fragments.EventFragment;
import com.example.grapes_pradip.vimalsagaradmin.fragments.GalleryFragment;
import com.example.grapes_pradip.vimalsagaradmin.fragments.InformationFragment;
import com.example.grapes_pradip.vimalsagaradmin.fragments.OpinionPollFragment;
import com.example.grapes_pradip.vimalsagaradmin.fragments.QuestionAnswerFragment;
import com.example.grapes_pradip.vimalsagaradmin.fragments.SubAdminListFragment;
import com.example.grapes_pradip.vimalsagaradmin.fragments.ThoughtFragment;
import com.example.grapes_pradip.vimalsagaradmin.fragments.UserFragment;
import com.example.grapes_pradip.vimalsagaradmin.fragments.VideoFragment;
import com.example.grapes_pradip.vimalsagaradmin.util.NetworkChangeReceiver;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity implements View.OnClickListener, NetworkChangeReceiver.NetworkChange {
    private View headerLayout;
    private LinearLayout lin_home;
    private LinearLayout lin_info;
    private LinearLayout lin_event;
    private LinearLayout lin_audio;
    private LinearLayout lin_video;
    private LinearLayout lin_thought;
    private LinearLayout lin_gallery;
    private LinearLayout lin_qa;
    private LinearLayout lin_comp;
    private LinearLayout lin_op;
    private LinearLayout lin_bypeople;
    private LinearLayout lin_user;
    private LinearLayout lin_setting;
    private ImageView img_home;
    private ProgressDialog progressDialog;
    private SharedPreferencesClass sharedPreferencesClass;
    private Dialog dialog;
    private boolean doubleBackToExitPressedOnce = false;
    private static Dialog csDialog = null;
    private static Context context = null;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private NotificationUtils notificationUtils;
    ToggleButton pushonoff;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        sharedPreferencesClass = new SharedPreferencesClass(MainActivity.this);
//        Intent intent = getIntent();
//        action = intent.getStringExtra("click_action");
//        intent.getStringArrayExtra("message");
//        Log.e("action", "---------------" + action);
//
//        if (action.equalsIgnoreCase("main_click")) {
//            Log.e("question ignore click", "--------------" + action);
//            openDesktop();
//        } else if (action.equalsIgnoreCase("question_click")) {
//            Log.e("question click", "--------------" + action);
//            openQA();
//        } else if (action.equalsIgnoreCase("bypeople_click")) {
//            openByPeople();
//            Log.e("bypeople click", "--------------" + action);
//        } else if (action.equalsIgnoreCase("user_click")) {
//            openUser();
//            Log.e("user click ", "--------------" + action);
//        } else if (action.equalsIgnoreCase("opinionPoll_click")) {
//            openUser();
//            Log.e("opinionPoll_click ", "--------------" + action);
//        } else {
//            Log.e("null", "--------------" + action);
//            openDesktop();
//        }

        sharedPreferencesClass = new SharedPreferencesClass(MainActivity.this);
        Log.e("action", "---------------" + sharedPreferencesClass.getActionClick());
        if (sharedPreferencesClass.getActionClick().equalsIgnoreCase("main_click")) {
            Log.e("question ignore click", "--------------" + sharedPreferencesClass.getActionClick());
            openDesktop();
        } else if (sharedPreferencesClass.getActionClick().equalsIgnoreCase("question_click")) {
            Log.e("question click", "--------------" + sharedPreferencesClass.getActionClick());
            openQA();
        } else if (sharedPreferencesClass.getActionClick().equalsIgnoreCase("bypeople_click")) {
            openByPeople();
            Log.e("bypeople click", "--------------" + sharedPreferencesClass.getActionClick());
        } else if (sharedPreferencesClass.getActionClick().equalsIgnoreCase("user_click")) {
            openUser();
            Log.e("user click ", "--------------" + sharedPreferencesClass.getActionClick());
        } else if (sharedPreferencesClass.getActionClick().equalsIgnoreCase("opinionPoll_click")) {
            openOP();
            Log.e("opinionPoll_click ", "--------------" + sharedPreferencesClass.getActionClick());
        } else {
            Log.e("null", "--------------" + sharedPreferencesClass.getActionClick());
            openDesktop();
        }


//        FirebaseMessaging.getInstance().subscribeToTopic("news");
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
                    Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);

                    String action1 = intent.getStringExtra("click_action");
                    String message = intent.getStringExtra("message");
//                    showNotificationMessage(MainActivity.this, "Vimalsagarji", message, "", resultIntent);
//                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
//                    if (action.equalsIgnoreCase("main_click")) {
//                        Log.e("question ignore click", "--------------" + action);
//                        openDesktop();
//                    } else if (action.equalsIgnoreCase("question_click")) {
//                        Log.e("question click", "--------------" + action);
//                        openQA();
//                    } else if (action.equalsIgnoreCase("bypeople_click")) {
//                        openByPeople();
//                        Log.e("bypeople click", "--------------" + action);
//                    } else if (action.equalsIgnoreCase("user_click")) {
//                        openUser();
//                        Log.e("user click ", "--------------" + action);
//                    } else if (action.equalsIgnoreCase("opinionPoll_click")) {
//                        openUser();
//                        Log.e("opinionPoll_click ", "--------------" + action);
//                    } else {
//                        Log.e("null", "--------------" + action);
//                        openDesktop();
//                    }

                }
            }
        };
        displayFirebaseRegId();
        MainActivity.context = MainActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerLayout = navigationView.getHeaderView(0);
        findID();
        idClick();


    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e("Firebase reg id: ", regId);

        if (!TextUtils.isEmpty(regId))
            Log.e("Firebase Reg Id: ", "--------------" + regId);
        else
            Log.e("Firebase Reg Id is not received yet!", "");
    }

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    @Override
    public void onNetworkChange(String status) {
        try {
            if (csDialog == null && status.equals("FALSE")) {
                csDialog = new Dialog(context);

                //FrameLayout csMainLay = null;
                Button csTryAgainBtn;

                csDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                csDialog.setContentView(R.layout.dialog_network_connectivity);

                csTryAgainBtn = (Button) csDialog.findViewById(R.id.networkConnectivity_tryagainBtn);
                //csMainLay.setAlpha(0.9f);
                //((TextView)(csDialog.findViewById(R.id.dialog_textView))).setText("Please check your internet connection");
                csDialog.setCancelable(false);
                csDialog.setCanceledOnTouchOutside(false);

                csTryAgainBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new NetworkChangeReceiver();
                    }
                });

                if (status.equals("FALSE")) {
                    csDialog.show();
                    Window window = csDialog.getWindow();
                    window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                }
            } else //noinspection StatementWithEmptyBody
                if (status.equals("TRUE") && csDialog != null && csDialog.isShowing()) {
                    csDialog.dismiss();
                    csDialog = null;
                } else {

                }
        } catch (Exception e) {
            if (csDialog != null && csDialog.isShowing())
                csDialog.dismiss();

            csDialog = null;
        }
    }

    private void findID() {
        img_home = (ImageView) findViewById(R.id.img_home);
        lin_home = (LinearLayout) headerLayout.findViewById(R.id.lin_home);
        lin_info = (LinearLayout) headerLayout.findViewById(R.id.lin_info);
        lin_event = (LinearLayout) headerLayout.findViewById(R.id.lin_event);
        lin_audio = (LinearLayout) headerLayout.findViewById(R.id.lin_audio);
        lin_video = (LinearLayout) headerLayout.findViewById(R.id.lin_video);
        lin_thought = (LinearLayout) headerLayout.findViewById(R.id.lin_thought);
        lin_gallery = (LinearLayout) headerLayout.findViewById(R.id.lin_gallery);
        lin_qa = (LinearLayout) headerLayout.findViewById(R.id.lin_qa);
        lin_comp = (LinearLayout) headerLayout.findViewById(R.id.lin_comp);
        lin_op = (LinearLayout) headerLayout.findViewById(R.id.lin_op);
        lin_bypeople = (LinearLayout) headerLayout.findViewById(R.id.lin_bypeople);
        lin_user = (LinearLayout) headerLayout.findViewById(R.id.lin_user);
        lin_setting = (LinearLayout) headerLayout.findViewById(R.id.lin_setting);
    }

    private void idClick() {
        img_home.setOnClickListener(this);
        lin_home.setOnClickListener(this);
        lin_info.setOnClickListener(this);
        lin_event.setOnClickListener(this);
        lin_audio.setOnClickListener(this);
        lin_video.setOnClickListener(this);
        lin_thought.setOnClickListener(this);
        lin_gallery.setOnClickListener(this);
        lin_qa.setOnClickListener(this);
        lin_comp.setOnClickListener(this);
        lin_op.setOnClickListener(this);
        lin_bypeople.setOnClickListener(this);
        lin_user.setOnClickListener(this);
        lin_setting.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click Back again to exit.", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.viewadmin) {
            openSubAdminList();
            return true;
        }
        if (id == R.id.changepwd) {
//            Toast.makeText(MainActivity.this, "change password", Toast.LENGTH_SHORT).show();
            dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.change_password_activity);
            dialog.show();
            final EditText edit_old_pwd = (EditText) dialog.findViewById(R.id.edit_old_pwd);
            final EditText edit_new_pwd = (EditText) dialog.findViewById(R.id.edit_new_pwd);
            final EditText edit_con_pwd = (EditText) dialog.findViewById(R.id.edit_con_pwd);
            final Button btn_change_password = (Button) dialog.findViewById(R.id.btn_change_password);
            btn_change_password.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(edit_old_pwd.getText().toString())) {
                        edit_old_pwd.setError("Enter old password.");
                        edit_old_pwd.requestFocus();
                    } else if (TextUtils.isEmpty(edit_new_pwd.getText().toString())) {
                        edit_new_pwd.setError("Enter new password.");
                        edit_new_pwd.requestFocus();
                    } else if (TextUtils.isEmpty(edit_con_pwd.getText().toString())) {
                        edit_con_pwd.setError("Enter confirm password.");
                        edit_con_pwd.requestFocus();
                    } else if (!edit_new_pwd.getText().toString().equalsIgnoreCase(edit_con_pwd.getText().toString())) {
                        edit_con_pwd.setError("Confirm password not same.");
                    } else {
                        if (CommonMethod.isInternetConnected(MainActivity.this)) {
                            new ChangePassword().execute(edit_old_pwd.getText().toString(), edit_new_pwd.getText().toString(), edit_con_pwd.getText().toString());
                        } else {
                            Toast.makeText(MainActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            return true;
        }
        if (id == R.id.logout) {
//            Toast.makeText(MainActivity.this, "logout", Toast.LENGTH_SHORT).show();
            dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_logout);
            dialog.show();
            final Button btn_yes = (Button) dialog.findViewById(R.id.yes);
            final Button btn_no = (Button) dialog.findViewById(R.id.no);
            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Logout();
                }
            });
            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            Window window = dialog.getWindow();
            assert window != null;
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void Logout() {
        sharedPreferencesClass.saveUser_Id("");
        sharedPreferencesClass.saveFirst_name("");
        sharedPreferencesClass.saveEmail("");
        sharedPreferencesClass.saveMobile("");
        sharedPreferencesClass.saveUsername("");
        sharedPreferencesClass.savePassword("");
        sharedPreferencesClass.saveDates("");
        sharedPreferencesClass.saveRole("");
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_home:
                openHome();
                break;
            case R.id.lin_home:
                Log.e("lin_home", "------------------" + "click");
                openHome();
                onBackPressed();
                break;
            case R.id.lin_info:
                Log.e("lin_info", "------------------" + "click");
                openInformation();
                onBackPressed();
                break;
            case R.id.lin_event:
                Log.e("information", "------------------" + "click");
                openEvent();
                onBackPressed();
                break;
            case R.id.lin_audio:
                Log.e("lin_event", "------------------" + "click");
                openAudio();
                onBackPressed();
                break;
            case R.id.lin_video:
                Log.e("lin_video", "------------------" + "click");
                openVideo();
                onBackPressed();
                break;
            case R.id.lin_thought:
                Log.e("lin_thought", "------------------" + "click");
                openThought();
                onBackPressed();
                break;
            case R.id.lin_gallery:
                Log.e("lin_gallery", "------------------" + "click");
                openGallery();
                onBackPressed();
                break;
            case R.id.lin_qa:
                Log.e("lin_qa", "------------------" + "click");
                openQA();
                onBackPressed();
                break;
            case R.id.lin_comp:
                Log.e("lin_comp", "------------------" + "click");
                openCompition();
                onBackPressed();
                break;
            case R.id.lin_op:
                Log.e("lin_op", "------------------" + "click");
                openOP();
                onBackPressed();
                break;
            case R.id.lin_bypeople:
                Log.e("lin_bypeople", "------------------" + "click");
                openByPeople();
                onBackPressed();
                break;
            case R.id.lin_user:
                Log.e("lin_bypeople", "------------------" + "click");
                openUser();
                onBackPressed();
                break;
            case R.id.lin_setting:
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                onBackPressed();
                /*dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_setting);
                dialog.show();
                pushonoff = (ToggleButton) dialog.findViewById(R.id.pushonoff);
                ImageView img_back= (ImageView) dialog.findViewById(R.id.img_back);
                img_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {
                    pushonoff.setChecked(true);
                } else {
                    pushonoff.setChecked(false);
                }
                pushonoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isChecked) {
                            // The toggle is enabled
                            sharedPreferencesClass.savePushNotification("pushon");
                            pushonoff.setChecked(true);
                            Toast.makeText(MainActivity.this, "Pnotification on.", Toast.LENGTH_SHORT).show();
                        } else {
                            // The toggle is disabled
                            sharedPreferencesClass.savePushNotification("pushoff");
                            pushonoff.setChecked(false);
                            Toast.makeText(MainActivity.this, "Push notification off.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                WindowManager.LayoutParams attrs = getWindow().getAttributes();
                attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                getWindow().setAttributes(attrs);
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));*/
                break;
        }
    }

    private void openSubAdminList() {
        Fragment fr = null;
        fr = new SubAdminListFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openUser() {
        Fragment fr = null;
        fr = new UserFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openDesktop() {
        Fragment fr = null;
        fr = new DesktopFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openHome() {
        Fragment fr = null;
        fr = new DesktopFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openInformation() {
        Fragment fr = null;
        fr = new InformationFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openEvent() {
        Fragment fr = null;
        fr = new EventFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openAudio() {
        Fragment fr = null;
        fr = new AudioFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openVideo() {
        Fragment fr = null;
        fr = new VideoFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openThought() {
        Fragment fr = null;
        fr = new ThoughtFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openGallery() {
        Fragment fr = null;
        fr = new GalleryFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openQA() {
        Fragment fr = null;
        fr = new QuestionAnswerFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openCompition() {
        Fragment fr = null;
        fr = new CompetitionFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openOP() {
        Fragment fr = null;
        fr = new OpinionPollFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openByPeople() {
        Fragment fr = null;
        fr = new ByPeopleFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private class ChangePassword extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("adminid", sharedPreferencesClass.getUser_Id()));
            nameValuePairs.add(new BasicNameValuePair("oldpassword", params[0]));
            nameValuePairs.add(new BasicNameValuePair("newpassword", params[1]));
            nameValuePairs.add(new BasicNameValuePair("cpassword", params[2]));
            responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + CommonAPI_Name.changepassword, nameValuePairs, MainActivity.this);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "---------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    Toast.makeText(MainActivity.this, "Password changed successfully.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(MainActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, "Password not changed.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(MainActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
    protected void onResume() {
        super.onResume();
        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_setting);
//        dialog.show();
        pushonoff = (ToggleButton) dialog.findViewById(R.id.pushonoff);
        if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {
            pushonoff.setChecked(true);
        } else {
            pushonoff.setChecked(false);
        }
//        String action;
//
//        action=null;
//        Intent intent = getIntent();
//        action = intent.getStringExtra("click_action");
//        intent.getStringArrayExtra("message");
//        Log.e("action", "---------------" + sharedPreferencesClass.getActionClick());
//        if (sharedPreferencesClass.getActionClick().equalsIgnoreCase("main_click")) {
//            Log.e("question ignore click", "--------------" + sharedPreferencesClass.getActionClick());
//            openDesktop();
//        } else if (sharedPreferencesClass.getActionClick().equalsIgnoreCase("question_click")) {
//            Log.e("question click", "--------------" + sharedPreferencesClass.getActionClick());
//            openQA();
//        } else if (sharedPreferencesClass.getActionClick().equalsIgnoreCase("bypeople_click")) {
//            openByPeople();
//            Log.e("bypeople click", "--------------" + sharedPreferencesClass.getActionClick());
//        } else if (sharedPreferencesClass.getActionClick().equalsIgnoreCase("user_click")) {
//            openUser();
//            Log.e("user click ", "--------------" + sharedPreferencesClass.getActionClick());
//        } else if (sharedPreferencesClass.getActionClick().equalsIgnoreCase("opinionPoll_click")) {
//            openOP();
//            Log.e("opinionPoll_click ", "--------------" + sharedPreferencesClass.getActionClick());
//        } else {
//            Log.e("null", "--------------" + sharedPreferencesClass.getActionClick());
//            openDesktop();
//        }

        // register GCM registration complete receiver

    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


}
