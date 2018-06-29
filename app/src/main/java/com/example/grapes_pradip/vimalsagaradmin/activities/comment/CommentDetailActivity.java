package com.example.grapes_pradip.vimalsagaradmin.activities.comment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

@SuppressLint("Registered")
public class CommentDetailActivity extends AppCompatActivity {

    private EditText edit_comment;
    private ProgressDialog progressDialog;
    private String id;
    private String modulename;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_detail_activity);
        ImageView img_back = (ImageView) findViewById(R.id.img_back);
        TextView txt_header = (TextView) findViewById(R.id.txt_header);
        TextView txt_module = (TextView) findViewById(R.id.txt_module);
        TextView txt_title = (TextView) findViewById(R.id.txt_title);
        TextView txt_comment = (TextView) findViewById(R.id.txt_comment);
        TextView txt_unm = (TextView) findViewById(R.id.txt_unm);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txt_header.setText(R.string.comment_detail);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        String title = intent.getStringExtra("Title");
        String comment = intent.getStringExtra("Comment");
        String name = intent.getStringExtra("Name");
        modulename = intent.getStringExtra("module_name");

        txt_title.setText(CommonMethod.decodeEmoji(title));
        txt_comment.setText(CommonMethod.decodeEmoji(comment));
        txt_unm.setText(CommonMethod.decodeEmoji(name));
        txt_module.setText(CommonMethod.decodeEmoji(modulename));

        edit_comment = (EditText) findViewById(R.id.edit_comment);
        Button btn_send = (Button) findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("modulename", "--------------------" + modulename);

                if (TextUtils.isEmpty(edit_comment.getText().toString())) {
                    edit_comment.setError("Please enter comment.");
                    edit_comment.requestFocus();
                } else {

                    switch (modulename) {

                        case "Information":
                            new AdminInformationComment().execute(CommonMethod.encodeEmoji(edit_comment.getText().toString()));
                            break;

                        case "Event":
                            new AdminEventComment().execute(CommonMethod.encodeEmoji(edit_comment.getText().toString()));
                            break;

                        case "Thought":
                            new AdminThoughtComment().execute(CommonMethod.encodeEmoji(edit_comment.getText().toString()));
                            break;

                        case "Audio":
                            new AdminAudioComment().execute(CommonMethod.encodeEmoji(edit_comment.getText().toString()));
                            break;

                        case "Video":
                            new AdminVideoComment().execute(CommonMethod.encodeEmoji(edit_comment.getText().toString()));
                            break;

                        case "ByPeople":
                            new AdminByPeopleComment().execute(CommonMethod.encodeEmoji(edit_comment.getText().toString()));
                            break;

                    }

                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class AdminAudioComment extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CommentDetailActivity.this);
            progressDialog.setMessage("Loading..");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("aid", id));
                nameValuePairs.add(new BasicNameValuePair("Comment", params[0]));
                responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + CommonAPI_Name.admincommentaudio, nameValuePairs, CommentDetailActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    if (CommonMethod.isInternetConnected(CommentDetailActivity.this)) {
                        progressDialog.dismiss();
                        Toast.makeText(CommentDetailActivity.this, "Comment added successfully.", Toast.LENGTH_SHORT).show();
                        edit_comment.setText("");
                        finish();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(CommentDetailActivity.this, "Comment not added.", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AdminByPeopleComment extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CommentDetailActivity.this);
            progressDialog.setMessage("Loading..");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("pid", id));
                nameValuePairs.add(new BasicNameValuePair("Comment", params[0]));
                responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + CommonAPI_Name.admincommentbypeople, nameValuePairs, CommentDetailActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-------------------" + s);
            progressDialog.dismiss();
            edit_comment.setText("");
            Toast.makeText(CommentDetailActivity.this, "Comment added successfully.", Toast.LENGTH_SHORT).show();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    edit_comment.setText("");
                    if (CommonMethod.isInternetConnected(CommentDetailActivity.this)) {
                        Toast.makeText(CommentDetailActivity.this, "Comment added successfully.", Toast.LENGTH_SHORT).show();
                        finish();
//                        dialog.dismiss();
//                        Toast.makeText(ByPeopleDetailActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CommentDetailActivity.this, "Comment not added.", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(ByPeopleDetailActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AdminEventComment extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CommentDetailActivity.this);
            progressDialog.setMessage("Loading..");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("eid", id));
                nameValuePairs.add(new BasicNameValuePair("Comment", params[0]));
                responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + CommonAPI_Name.admincommentevent, nameValuePairs, CommentDetailActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    progressDialog.dismiss();
                    Toast.makeText(CommentDetailActivity.this, "Comment added successfully.", Toast.LENGTH_SHORT).show();
                    if (CommonMethod.isInternetConnected(CommentDetailActivity.this)) {
                        finish();
//                        Toast.makeText(EventDetailActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        edit_comment.setText("");
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(CommentDetailActivity.this, "Comment not added.", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(EventDetailActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AdminInformationComment extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CommentDetailActivity.this);
            progressDialog.setMessage("Loading..");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("infoid", id));
                nameValuePairs.add(new BasicNameValuePair("Comment", params[0]));
                responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + CommonAPI_Name.admincomment, nameValuePairs, CommentDetailActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    if (CommonMethod.isInternetConnected(CommentDetailActivity.this)) {
                        progressDialog.dismiss();
                        Toast.makeText(CommentDetailActivity.this, "Comment added successfully.", Toast.LENGTH_SHORT).show();
                        edit_comment.setText("");
                        finish();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(CommentDetailActivity.this, "Comment not added.", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AdminThoughtComment extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CommentDetailActivity.this);
            progressDialog.setMessage("Loading..");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("tid", id));
                nameValuePairs.add(new BasicNameValuePair("Comment", params[0]));
                responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + CommonAPI_Name.admincommentthought, nameValuePairs, CommentDetailActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    progressDialog.dismiss();
                    if (CommonMethod.isInternetConnected(CommentDetailActivity.this)) {
                        Toast.makeText(CommentDetailActivity.this, "Comment added successfully.", Toast.LENGTH_SHORT).show();
                        edit_comment.setText("");
                        finish();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(CommentDetailActivity.this, "Comment not added.", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AdminVideoComment extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CommentDetailActivity.this);
            progressDialog.setMessage("Loading..");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("vid", id));
                nameValuePairs.add(new BasicNameValuePair("Comment", params[0]));
                responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + CommonAPI_Name.admincommentvideo, nameValuePairs, CommentDetailActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    if (CommonMethod.isInternetConnected(CommentDetailActivity.this)) {
                        progressDialog.dismiss();
                        Toast.makeText(CommentDetailActivity.this, "Comment added successfully.", Toast.LENGTH_SHORT).show();
                        edit_comment.setText("");
                        finish();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(CommentDetailActivity.this, "Comment not added.", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }

}
