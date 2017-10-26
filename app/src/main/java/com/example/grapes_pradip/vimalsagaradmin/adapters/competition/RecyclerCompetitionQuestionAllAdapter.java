package com.example.grapes_pradip.vimalsagaradmin.adapters.competition;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexandrius.accordionswipelayout.library.SwipeLayout;
import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.activities.competition.AllQuestionAnswerActivity;
import com.example.grapes_pradip.vimalsagaradmin.activities.competition.AllQuestionOptionActivity;
import com.example.grapes_pradip.vimalsagaradmin.activities.competition.EditCompetitionQuestionActivity;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.competition.CompetitionQuestionItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Pradip on 1/2/17.
 */
@SuppressWarnings("ALL")
public class RecyclerCompetitionQuestionAllAdapter extends RecyclerView.Adapter<RecyclerCompetitionQuestionAllAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<CompetitionQuestionItem> itemArrayList;
    private String id;

    public RecyclerCompetitionQuestionAllAdapter(Activity activity, ArrayList<CompetitionQuestionItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_competition_all, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {

        final CompetitionQuestionItem competitionQuestionItem = itemArrayList.get(i);

        if (competitionQuestionItem.getQType().equalsIgnoreCase("Option")) {
            holder.img_option.setVisibility(View.VISIBLE);
            holder.txt_title.setText(competitionQuestionItem.getQuestion());
            holder.txt_type.setText(competitionQuestionItem.getQType());
            ((SwipeLayout) holder.itemView).setItemState(SwipeLayout.ITEM_STATE_COLLAPSED, true);

            holder.img_option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(activity, AllQuestionOptionActivity.class);
                    intent.putExtra("QID", competitionQuestionItem.getID());
                    intent.putExtra("Question", competitionQuestionItem.getQuestion());
                    intent.putExtra("QType", competitionQuestionItem.getQType());
                    intent.putExtra("CategoryID", competitionQuestionItem.getCategoryID());
                    intent.putExtra("Options", competitionQuestionItem.getOptions());
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
        } else {
            holder.img_option.setVisibility(View.GONE);
            holder.txt_title.setText(competitionQuestionItem.getQuestion());
            holder.txt_type.setText(competitionQuestionItem.getQType());
            ((SwipeLayout) holder.itemView).setItemState(SwipeLayout.ITEM_STATE_COLLAPSED, true);
        }

        holder.img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AllQuestionAnswerActivity.class);
                intent.putExtra("QID", competitionQuestionItem.getID());
                intent.putExtra("Question", competitionQuestionItem.getQuestion());
                activity.startActivity(intent);

            }
        });

        holder.txt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EditCompetitionQuestionActivity.class);
                intent.putExtra("ID", itemArrayList.get(holder.getAdapterPosition()).getID());
                intent.putExtra("Question", itemArrayList.get(holder.getAdapterPosition()).getQuestion());
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
                        id = itemArrayList.get(holder.getAdapterPosition()).getID();
                        Log.e("id", "-----------------------" + id);
                        new DeleteCompetitionQuestion().execute();
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
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, SwipeLayout.OnSwipeItemClickListener {

        final ImageView img_audio_category;
        final TextView txt_title;
        final TextView txt_type;
        final TextView txt_edit;
        final TextView txt_delete;
        final SwipeLayout swipeLayout;
        final ImageView img_option;
        final ImageView img_view;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_type = (TextView) itemView.findViewById(R.id.txt_type);
            img_audio_category = (ImageView) itemView.findViewById(R.id.img_audio_category);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            img_option = (ImageView) itemView.findViewById(R.id.img_option);
            img_view = (ImageView) itemView.findViewById(R.id.img_view);
            txt_edit = (TextView) itemView.findViewById(R.id.txt_edit);
            txt_delete = (TextView) itemView.findViewById(R.id.txt_delete);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            ((SwipeLayout) itemView).setOnSwipeItemClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(activity, "on Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(v.getContext(), AudioDetailActivity.class);
//            intent.putExtra("aid", itemArrayList.get(getAdapterPosition()).getId());
//            intent.putExtra("categoryname", itemArrayList.get(getAdapterPosition()).getCategoryname());
//            v.getContext().startActivity(intent);
//            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        @Override
        public boolean onLongClick(View v) {
//            Toast.makeText(activity, "long Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public void onSwipeItemClick(boolean left, final int index) {
            if (index == 0) {
                Intent intent = new Intent(activity, EditCompetitionQuestionActivity.class);
                intent.putExtra("ID", itemArrayList.get(getAdapterPosition()).getID());
                intent.putExtra("Question", itemArrayList.get(getAdapterPosition()).getQuestion());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (index == 1) {

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
                        id = itemArrayList.get(getAdapterPosition()).getID();
                        Log.e("id", "-----------------------" + id);
                        new DeleteCompetitionQuestion().execute();
                        Log.e("pos", "----------" + index);
                        int pos = getAdapterPosition();
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


//                    Toast.makeText(itemView.getContext(), "Trash" + id, Toast.LENGTH_SHORT).show();
            }

        }

    }

    private class DeleteCompetitionQuestion extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.deletequestioncompetition + "?qid=" + id);
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
                    Toast.makeText(activity, "Competition question not delete." , Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
