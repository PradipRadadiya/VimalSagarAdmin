package com.example.grapes_pradip.vimalsagaradmin.fcm;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.grapes_pradip.vimalsagaradmin.activities.MainActivity;
import com.example.grapes_pradip.vimalsagaradmin.activities.admin.UserActivity;
import com.example.grapes_pradip.vimalsagaradmin.activities.audio.CommentListAudio;
import com.example.grapes_pradip.vimalsagaradmin.activities.bypeople.ByPeopleActivity;
import com.example.grapes_pradip.vimalsagaradmin.activities.bypeople.CommentListByPeople;
import com.example.grapes_pradip.vimalsagaradmin.activities.competition.AllQuestionAnswerActivity;
import com.example.grapes_pradip.vimalsagaradmin.activities.event.CommentListEvent;
import com.example.grapes_pradip.vimalsagaradmin.activities.information.CommentListInformation;
import com.example.grapes_pradip.vimalsagaradmin.activities.opinionpoll.OpinionPollActivity;
import com.example.grapes_pradip.vimalsagaradmin.activities.question.QuestionAnswerActivity;
import com.example.grapes_pradip.vimalsagaradmin.activities.thought.CommentListThought;
import com.example.grapes_pradip.vimalsagaradmin.activities.video.CommentListVideo;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.SharedPreferencesClass;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;
    SharedPreferencesClass sharedPreferencesClass;
    public static String questionid;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        sharedPreferencesClass = new SharedPreferencesClass(MyFirebaseMessagingService.this);
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message Data Body: " + remoteMessage.getData().toString());
            JSONObject json = new JSONObject(remoteMessage.getData());
            handleDataMessage(json);
//            handleMessage(json);
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }
    }

    private void handleMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());
        try {
            Log.e(TAG, "get parameter: " + json.getString("Nick"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());
        try {
            JSONObject data = json;
            String imageUrl = "";
//            String imageUrl = data.getString("image");
            String title = CommonMethod.decodeEmoji(data.getString("title"));
            String click_action = CommonMethod.decodeEmoji(data.getString("click_action"));
            String message = CommonMethod.decodeEmoji(data.getString("message"));
            String categoty_id = CommonMethod.decodeEmoji(data.getString("categoty_id"));
            String category_title = CommonMethod.decodeEmoji(data.getString("category_title"));

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "click_action: " + click_action);
            Log.e(TAG, "categoty_id: " + categoty_id);
            Log.e(TAG, "category_title: " + category_title);


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
//                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
//                pushNotification.putExtra("message", message);
//                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
//                // play notification sound
//                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
//                notificationUtils.playNotificationSound();


                if (click_action.equalsIgnoreCase("information_click")) {
                    Log.e("click", "--------------information_click");
                    Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                    resultIntent.putExtra("message", message);


                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Information-" + title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Information -" + title, message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("question_click")) {
                    sharedPreferencesClass.saveActionClick("question_click");
                    Intent resultIntent = new Intent(getApplicationContext(), QuestionAnswerActivity.class);

                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Question & Answer", message + "-user ask question", "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Question & Answer", message + "-user ask question", "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("thought_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                    resultIntent.putExtra("message", message);
                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Thought-" + title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Thought -" + title, message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("competition_item_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), AllQuestionAnswerActivity.class);

                    questionid = categoty_id;
                    resultIntent.putExtra("QID", categoty_id);
                    resultIntent.putExtra("Question", message);
                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {
                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Competition", message + "user answered", "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Competition", message + "user answered", "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("bypeople_click")) {
                    sharedPreferencesClass.saveActionClick("bypeople_click");
                    Intent resultIntent = new Intent(getApplicationContext(), ByPeopleActivity.class);

                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "By People", title + "-New post add by user", "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "By People", title + "-New post add by user", "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("opinionPoll_click")) {
                    sharedPreferencesClass.saveActionClick("opinionPoll_click");
                    Intent resultIntent = new Intent(getApplicationContext(), OpinionPollActivity.class);
                    resultIntent.putExtra("click_action", "opinionPoll_click");
                    resultIntent.putExtra("message", message);
                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {
                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Opinion Poll", "User Polling", "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Opinion Poll", category_title, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("gallery_item_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                    resultIntent.putExtra("cid", categoty_id);
                    resultIntent.putExtra("catname", category_title);
                    resultIntent.putExtra("message", message);
                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {
                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Gallery", category_title, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Gallery", category_title, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("user_click")) {
                    sharedPreferencesClass.saveActionClick("user_click");
                    Intent resultIntent = new Intent(getApplicationContext(), UserActivity.class);
                    resultIntent.putExtra("click_action", "user_click");
                    resultIntent.putExtra("message", message);
                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), message, "New User Registerd.", "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), message, "New User Registerd.", "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("information_comment_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), CommentListInformation.class);
                    resultIntent.putExtra("click_action", "information_comment_click");
                    resultIntent.putExtra("info_id", categoty_id);
                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Information comment-" + title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Information Comment", message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("event_comment_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), CommentListEvent.class);
                    resultIntent.putExtra("click_action", "event_comment_click");
                    resultIntent.putExtra("event_id", categoty_id);
                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Event comment-" + title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Event Comment", message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("audio_comment_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), CommentListAudio.class);
                    resultIntent.putExtra("click_action", "audio_comment_click");
                    resultIntent.putExtra("aid", categoty_id);
                    resultIntent.putExtra("categoryname", category_title);
                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Audio comment-" + title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Audio Comment", message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("video_comment_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), CommentListVideo.class);
                    resultIntent.putExtra("click_action", "video_comment_click");
                    resultIntent.putExtra("vid", categoty_id);
                    resultIntent.putExtra("categoryname", category_title);
                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Video comment-" + title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Video Comment", message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("thought_comment_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), CommentListThought.class);
                    resultIntent.putExtra("click_action", "thought_comment_click");
                    resultIntent.putExtra("tid", categoty_id);
                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Thought comment-" + title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Thought Comment", message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("bypeople_comment_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), CommentListByPeople.class);
                    resultIntent.putExtra("click_action", "bypeople_comment_click");
                    resultIntent.putExtra("ID", categoty_id);
                    resultIntent.putExtra("Name", category_title);
                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {
                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "By People comment-" + title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "By People Comment", message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else {
                    sharedPreferencesClass.saveActionClick("main_click");
                    Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                    resultIntent.putExtra("message", message);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);
                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), title, message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                }

            } else {
                // app is in background, show the notification in notification tray

                if (click_action.equalsIgnoreCase("information_click")) {
                    Log.e("click", "--------------information_click");
                    Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                    resultIntent.putExtra("message", message);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);
                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Information-" + title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Information -" + title, message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("question_click")) {
                    sharedPreferencesClass.saveActionClick("question_click");
                    Intent resultIntent = new Intent(getApplicationContext(), QuestionAnswerActivity.class);
                    resultIntent.putExtra("click_action", "question_click");
                    resultIntent.putExtra("message", message);

                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Question & Answer", message + "-user ask question", "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Question & Answer", message + "-user ask question", "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("thought_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                    resultIntent.putExtra("message", message);
                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Thought-" + title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Thought -" + title, message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("competition_item_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), AllQuestionAnswerActivity.class);
                    questionid = categoty_id;

                    resultIntent.putExtra("QID", categoty_id);
                    resultIntent.putExtra("Question", message);

                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Competition", message + "-user answered.", "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Competition", message + "-user answered.", "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("bypeople_click")) {
                    sharedPreferencesClass.saveActionClick("bypeople_click");
                    Intent resultIntent = new Intent(getApplicationContext(), ByPeopleActivity.class);
                    resultIntent.putExtra("click_action", "bypeople_click");
                    resultIntent.putExtra("message", message);
                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "By People", title + "-New post add by user", "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "By People", title + "-New post add by user", "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("opinionPoll_click")) {
                    sharedPreferencesClass.saveActionClick("opinionPoll_click");
                    Intent resultIntent = new Intent(getApplicationContext(), OpinionPollActivity.class);
                    resultIntent.putExtra("click_action", "opinionPoll_click");
                    resultIntent.putExtra("message", message);
                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Opinion Poll", "Polling", "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Opinion Poll", "polling", "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("gallery_item_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                    resultIntent.putExtra("cid", categoty_id);
                    resultIntent.putExtra("catname", category_title);
                    resultIntent.putExtra("message", message);
                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Gallery", category_title, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Gallery", category_title, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("user_click")) {
                    sharedPreferencesClass.saveActionClick("user_click");
                    Intent resultIntent = new Intent(getApplicationContext(), UserActivity.class);
                    resultIntent.putExtra("click_action", "user_click");
                    resultIntent.putExtra("message", message);
                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), message, "New User Registerd.", "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Gallery", category_title, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("information_comment_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), CommentListInformation.class);
                    resultIntent.putExtra("click_action", "information_comment_click");
                    resultIntent.putExtra("info_id", categoty_id);
                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Information comment-" + title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Information Comment", message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("event_comment_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), CommentListEvent.class);
                    resultIntent.putExtra("click_action", "event_comment_click");
                    resultIntent.putExtra("event_id", categoty_id);
                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Event comment-" + title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Event Comment", message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("audio_comment_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), CommentListAudio.class);
                    resultIntent.putExtra("click_action", "audio_comment_click");
                    resultIntent.putExtra("aid", categoty_id);
                    resultIntent.putExtra("categoryname", category_title);
                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Audio comment-" + title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Audio Comment", message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("video_comment_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), CommentListVideo.class);
                    resultIntent.putExtra("click_action", "video_comment_click");
                    resultIntent.putExtra("vid", categoty_id);
                    resultIntent.putExtra("categoryname", category_title);
                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Video comment-" + title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Video Comment", message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("thought_comment_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), CommentListThought.class);
                    resultIntent.putExtra("click_action", "thought_comment_click");
                    resultIntent.putExtra("tid", categoty_id);
                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "Thought comment-" + title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "Thought Comment", message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else if (click_action.equalsIgnoreCase("bypeople_comment_click")) {
                    Intent resultIntent = new Intent(getApplicationContext(), CommentListByPeople.class);
                    resultIntent.putExtra("click_action", "bypeople_comment_click");
                    resultIntent.putExtra("ID", categoty_id);
                    resultIntent.putExtra("Name", category_title);
                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), "By People comment-" + title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), "By People Comment", message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                } else {
                    sharedPreferencesClass.saveActionClick("main_click");
                    Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                    resultIntent.putExtra("click_action", "main_click");
                    resultIntent.putExtra("message", message);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);
                    if (sharedPreferencesClass.getPushNotification().equalsIgnoreCase("pushon")) {
                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), title, message, "false", resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), title, message, "false", resultIntent, imageUrl);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Push notification off", Toast.LENGTH_LONG).show();
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

}
