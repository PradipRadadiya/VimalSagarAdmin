package com.example.grapes_pradip.vimalsagaradmin.activities.competition;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.activities.bypeople.ByPeopleDetailActivity;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.SharedPreferencesClass;
import com.example.grapes_pradip.vimalsagaradmin.retrofit.APIClient;
import com.example.grapes_pradip.vimalsagaradmin.retrofit.ApiInterface;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompetitionFinalResultHint extends AppCompatActivity {

    private EditText edit_title,edit_description,edit_start_time,edit_end_time,edit_attend_competition;
    private Button btn_submit;
    private ToggleButton toggleButton;
    String flag="0";
    SharedPreferencesClass sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_final_result_hint);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sharedPreferences=new SharedPreferencesClass(CompetitionFinalResultHint.this);
        bindID();
        displayCompetitionFinalResultNote();
    }

    private void bindID() {
        edit_title=findViewById(R.id.edit_title);
        edit_description=findViewById(R.id.edit_description);
        edit_start_time=findViewById(R.id.edit_start_time);
        edit_end_time=findViewById(R.id.edit_end_time);
        edit_attend_competition=findViewById(R.id.edit_attend_competitiion);
        btn_submit=findViewById(R.id.btn_submit);
        toggleButton=findViewById(R.id.toggleButton);


        if (sharedPreferences.getAlert().equalsIgnoreCase("1")){
            toggleButton.setChecked(true);
        }else{
            toggleButton.setChecked(false);
        }

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sharedPreferences.saveAlert("1");
                    saveCompetitionFinalResultNote(CommonMethod.decodeEmoji(edit_title.getText().toString()),CommonMethod.decodeEmoji(edit_description.getText().toString()),edit_start_time.getText().toString(),edit_end_time.getText().toString(),edit_attend_competition.getText().toString(),"1");
                }else{
                    sharedPreferences.saveAlert("0");
                    saveCompetitionFinalResultNote(CommonMethod.decodeEmoji(edit_title.getText().toString()),CommonMethod.decodeEmoji(edit_description.getText().toString()),edit_start_time.getText().toString(),edit_end_time.getText().toString(),edit_attend_competition.getText().toString(),"0");
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(edit_title.getText().toString())){
                    edit_title.setError("Please enter title.");
                    edit_title.requestFocus();
                }else if(TextUtils.isEmpty(edit_description.getText().toString())){
                    edit_description.setError("Please enter description");
                    edit_description.requestFocus();
                }else if(TextUtils.isEmpty(edit_start_time.getText().toString())){
                    edit_start_time.setError("Please enter start time.");
                    edit_start_time.requestFocus();
                }else if(TextUtils.isEmpty(edit_end_time.getText().toString())){
                    edit_end_time.setError("Please enter end time.");
                    edit_end_time.requestFocus();
                }else if(TextUtils.isEmpty(edit_attend_competition.getText().toString())){
                    edit_attend_competition.setError("Please entre attend competition.");
                    edit_attend_competition.requestFocus();
                }else{
                    saveCompetitionFinalResultNote(CommonMethod.decodeEmoji(edit_title.getText().toString()),CommonMethod.decodeEmoji(edit_description.getText().toString()),edit_start_time.getText().toString(),edit_end_time.getText().toString(),edit_attend_competition.getText().toString(),flag);
                }

            }
        });
    }


    private void saveCompetitionFinalResultNote(String title,String description,String start_time,String end_time,String attend_competition,String status){

        final ProgressDialog progressDialog = new ProgressDialog(CompetitionFinalResultHint.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);
        Call<JsonObject> callApi = apiInterface.saveCompetitionResultNote(title, description,start_time,end_time,attend_competition,status);
        callApi.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                progressDialog.dismiss();
                Log.e("reponse", "-----------------" + response.body());
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                            Toast.makeText(CompetitionFinalResultHint.this, "Competition result note updated successfully.", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CompetitionFinalResultHint.this, R.string.reopen, Toast.LENGTH_SHORT).show();
            }

        });

    }


    private void displayCompetitionFinalResultNote(){

        final ProgressDialog progressDialog = new ProgressDialog(CompetitionFinalResultHint.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);
        Call<JsonObject> callApi = apiInterface.getCompetitionResultNote();
        callApi.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                progressDialog.dismiss();
                Log.e("reponse", "-----------------" + response.body());
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            JSONObject object=jsonArray.getJSONObject(0);

                            edit_title.setText(object.getString("title"));
                            edit_description.setText(object.getString("description"));
                            edit_start_time.setText(object.getString("start_date"));
                            edit_end_time.setText(object.getString("end_date"));
                            edit_attend_competition.setText(object.getString("attend_comp"));
                            flag=object.getString("is_visible");

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CompetitionFinalResultHint.this, R.string.reopen, Toast.LENGTH_SHORT).show();
            }

        });

    }

}
