package com.example.grapes_pradip.vimalsagaradmin.adapters.competition;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexandrius.accordionswipelayout.library.SwipeLayout;
import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.activities.competition.AllCompetionQuestionActivity;
import com.example.grapes_pradip.vimalsagaradmin.activities.competition.EditCompetitionCategoryActivity;
import com.example.grapes_pradip.vimalsagaradmin.activities.competition.ViewResultActivity;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.competition.CompetitionItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

/**
 * Created by Pradip on 1/2/17.
 */
@SuppressWarnings("ALL")
public class RecyclerCompetitionCategoryAdapter extends RecyclerView.Adapter<RecyclerCompetitionCategoryAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<CompetitionItem> itemArrayList;
    private String id;
    public static final ArrayList<String> compcatid = new ArrayList<>();
    CountDownTimer countDownTimer;          // built in android class CountDownTimer
    long totalTimeCountInMilliseconds;      // total count down time in milliseconds
    long timeBlinkInMilliseconds;

    public RecyclerCompetitionCategoryAdapter(Activity activity, ArrayList<CompetitionItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_competition, viewGroup, false);
        totalTimeCountInMilliseconds = 60 * 1000;      // time count for 3 minutes = 180 seconds
        timeBlinkInMilliseconds = 30 * 1000;
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {


        final CompetitionItem competitionItem = itemArrayList.get(i);
        holder.txt_title.setText(CommonMethod.decodeEmoji(competitionItem.getTitle()));
        holder.txt_timer.setText(CommonMethod.decodeEmoji(competitionItem.getTotal_minute() + " minute"));
        holder.txt_last_date.setText(CommonMethod.decodeEmoji(competitionItem.getDate()));
        holder.txt_time.setText(CommonMethod.decodeEmoji(competitionItem.getTime()));
        holder.txt_total_question.setText(CommonMethod.decodeEmoji(competitionItem.getTotal_question() + " Question"));
        holder.check_delete.setChecked(competitionItem.isSelected() ? true : false);
        holder.txt_total_participate.setText(CommonMethod.decodeEmoji("Participated User : " + competitionItem.getParticipated_users()));


        if (competitionItem.getStatus().equalsIgnoreCase("2")) {
            holder.txt_publish.setVisibility(View.GONE);
//            holder.txt_result.setVisibility(View.VISIBLE);
        } else {

            holder.txt_publish.setVisibility(View.VISIBLE);
//            holder.txt_result.setVisibility(View.GONE);

            if (competitionItem.isSelected()) {
                holder.txt_publish.setVisibility(View.GONE);
//                holder.txt_result.setVisibility(View.VISIBLE);
            } else {
                holder.txt_publish.setVisibility(View.VISIBLE);
            }
        }

        holder.txt_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(activity, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(activity);
                }
                builder.setTitle("Alert")
                        .setMessage("Are you sure you want to publish result?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                dialog.dismiss();
                                id = itemArrayList.get(holder.getAdapterPosition()).getId();
                                competitionItem.setSelected(true);
                                notifyDataSetChanged();
                                new PublishCompetitionResult().execute();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


            }
        });

        holder.txt_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ViewResultActivity.class);
                intent.putExtra("competitioncategory_id", itemArrayList.get(holder.getAdapterPosition()).getId());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

        holder.txt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, EditCompetitionCategoryActivity.class);
                intent.putExtra("competitioncategory_id", itemArrayList.get(holder.getAdapterPosition()).getId());
                intent.putExtra("title", itemArrayList.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("rules", itemArrayList.get(holder.getAdapterPosition()).getRules());
                intent.putExtra("date", itemArrayList.get(holder.getAdapterPosition()).getDate());
                intent.putExtra("time", itemArrayList.get(holder.getAdapterPosition()).getTime());
                intent.putExtra("total_question", itemArrayList.get(holder.getAdapterPosition()).getTotal_question());
                intent.putExtra("total_minute", itemArrayList.get(holder.getAdapterPosition()).getTotal_minute());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

        holder.txt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                // Setting Dialog Title
                alertDialog.setTitle("Confirm Delete...");

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want to delete?.");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.ic_warning);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        id = itemArrayList.get(holder.getAdapterPosition()).getId();
                        Log.e("id", "-----------------------" + id);
                        new DeleteCompetitionCategory().execute();
                        int pos = holder.getAdapterPosition();
                        itemArrayList.remove(pos);
                        notifyItemRemoved(pos);
                        // Write your code here to invoke YES event
//                            Toast.makeText(activity, "You clicked on YES", Toast.LENGTH_SHORT).show();
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
//                            Toast.makeText(activity, "You clicked on NO", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        notifyDataSetChanged();
                    }
                });

                alertDialog.show();

            }
        });


    }

    @Override
    public int getItemCount() {

        return itemArrayList.size();
    }

    @SuppressWarnings("unused")
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        final ImageView img_audio_category;
        final TextView txt_title;
        final TextView txt_result;
        final TextView txt_total_participate;
        final TextView txt_publish;
        final TextView txt_timer;
        final TextView txt_time;
        final TextView txt_last_date;
        final TextView txt_total_question;
        final TextView txt_edit;
        final TextView txt_delete;
        final SwipeLayout swipeLayout;
        final CheckBox check_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_result = (TextView) itemView.findViewById(R.id.txt_result);
            txt_publish = (TextView) itemView.findViewById(R.id.txt_publish);
            txt_total_participate = (TextView) itemView.findViewById(R.id.txt_total_participate);
            txt_timer = (TextView) itemView.findViewById(R.id.txt_timer);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            txt_last_date = (TextView) itemView.findViewById(R.id.txt_last_date);
            txt_total_question = (TextView) itemView.findViewById(R.id.txt_total_question);
            img_audio_category = (ImageView) itemView.findViewById(R.id.img_audio_category);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            txt_edit = (TextView) itemView.findViewById(R.id.txt_edit);
            txt_delete = (TextView) itemView.findViewById(R.id.txt_delete);
            check_delete = (CheckBox) itemView.findViewById(R.id.check_delete);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {

//            Toast.makeText(activity, "on Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(v.getContext(), AllCompetionQuestionActivity.class);
            intent.putExtra("cid", itemArrayList.get(getAdapterPosition()).getId());
            v.getContext().startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        @Override
        public boolean onLongClick(View v) {
//            Toast.makeText(activity, "long Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    private class DeleteCompetitionCategory extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("cid", id));

            responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + CommonAPI_Name.deleteCompetition, nameValuePairs, activity);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                } else {
                    Toast.makeText(activity, "Competition category not delete.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class PublishCompetitionResult extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("cid", id));

            responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + CommonAPI_Name.publishCompetition, nameValuePairs, activity);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                } else {
                    Toast.makeText(activity, "Competition result publish.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}
