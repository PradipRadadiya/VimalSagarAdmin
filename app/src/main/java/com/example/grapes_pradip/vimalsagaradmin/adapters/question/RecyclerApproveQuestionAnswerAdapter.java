package com.example.grapes_pradip.vimalsagaradmin.adapters.question;

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
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.alexandrius.accordionswipelayout.library.SwipeLayout;
import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.activities.question.QuestionDetailActivity;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.question.QuestiinItem;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("ALL")
public class RecyclerApproveQuestionAnswerAdapter extends RecyclerView.Adapter<RecyclerApproveQuestionAnswerAdapter.ViewHolder> {


    private final Activity activity;
    private List<QuestiinItem> list;
    private final List<QuestiinItem> itemArrayList;
    private String id;
    public static final ArrayList<String> questionid = new ArrayList<>();

    public RecyclerApproveQuestionAnswerAdapter(Activity activity, List<QuestiinItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_question, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {

        final QuestiinItem questiinItem = itemArrayList.get(i);

        if (questiinItem.getIsApproved().equalsIgnoreCase("1")) {
            holder.txt_title.setText(String.valueOf(itemArrayList.size()-i)+") "+CommonMethod.decodeEmoji(questiinItem.getQuestion()));
            holder.txt_date.setText(CommonMethod.decodeEmoji(questiinItem.getDate()));
            holder.txt_name.setText(CommonMethod.decodeEmoji(questiinItem.getName()));
            holder.txt_views.setText(CommonMethod.decodeEmoji(questiinItem.getView())+" Views");
            ((SwipeLayout) holder.itemView).setItemState(SwipeLayout.ITEM_STATE_COLLAPSED, true);
            holder.checkbox_question.setChecked(questiinItem.isSelected() ? true : false);
        }else{
            itemArrayList.remove(i);
            notifyItemRemoved(i);
        }

        holder.txt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, QuestionDetailActivity.class);
                intent.putExtra("qid", itemArrayList.get(i).getID());
                intent.putExtra("question", itemArrayList.get(i).getQuestion());
                intent.putExtra("answer", itemArrayList.get(i).getAnswer());
                intent.putExtra("date", itemArrayList.get(i).getDate());
                intent.putExtra("isApprove", itemArrayList.get(i).getIsApproved());
                intent.putExtra("userid", itemArrayList.get(i).getUserID());
                intent.putExtra("name", itemArrayList.get(i).getName());
                intent.putExtra("view", itemArrayList.get(i).getView());
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
                        new DeleteQuestion().execute();
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
        holder.checkbox_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questiinItem.setSelected(!questiinItem.isSelected());
                holder.checkbox_question.setChecked(questiinItem.isSelected() ? true : false);

                if (questiinItem.isSelected()) {
                    questionid.add(questiinItem.getID());
                    Log.e("array", "------------" + questionid);
                } else {
                    questionid.remove(questiinItem.getID());
                    Log.e("array", "------------" + questionid);
                }
            }
        });

       /* holder.checkbox_question.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    //checked
                    questionid.add(questiinItem.getID());
                    Log.e("array", "------------" + questionid);
                    holder.checkbox_question.setChecked(true);
                } else {
                    //not checked

                    questionid.remove(questiinItem.getID());
                    Log.e("array", "------------" + questionid);
                    holder.checkbox_question.setChecked(false);
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {

        return itemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, SwipeLayout.OnSwipeItemClickListener {

        final TextView txt_title;
        final TextView txt_views;
        final TextView txt_date;
        final TextView txt_name;
        final TextView txt_edit;
        final TextView txt_delete;
        final CheckBox checkbox_question;

        public ViewHolder(View itemView) {
            super(itemView);
//            this.setIsRecyclable(false);
            txt_views = (TextView) itemView.findViewById(R.id.txt_views);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_edit = (TextView) itemView.findViewById(R.id.txt_edit);
            txt_delete = (TextView) itemView.findViewById(R.id.txt_delete);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            checkbox_question = (CheckBox) itemView.findViewById(R.id.checkbox_question);
            ((SwipeLayout) itemView).setOnSwipeItemClickListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
//            Toast.makeText(activity, "long Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();

            return false;
        }

        @Override
        public void onClick(View v) {

//            Toast.makeText(activity, "on Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(v.getContext(), QuestionDetailActivity.class);
            intent.putExtra("qid", itemArrayList.get(getAdapterPosition()).getID());
            intent.putExtra("question", itemArrayList.get(getAdapterPosition()).getQuestion());
            intent.putExtra("answer", itemArrayList.get(getAdapterPosition()).getAnswer());
            intent.putExtra("date", itemArrayList.get(getAdapterPosition()).getDate());
            intent.putExtra("isApprove", itemArrayList.get(getAdapterPosition()).getIsApproved());
            intent.putExtra("userid", itemArrayList.get(getAdapterPosition()).getUserID());
            intent.putExtra("name", itemArrayList.get(getAdapterPosition()).getName());
            intent.putExtra("view", itemArrayList.get(getAdapterPosition()).getView());
            v.getContext().startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }


        @Override
        public void onSwipeItemClick(boolean left, final int index) {
            if (index == 0) {
                Intent intent = new Intent(activity, QuestionDetailActivity.class);
                intent.putExtra("qid", itemArrayList.get(getAdapterPosition()).getID());
                intent.putExtra("question", itemArrayList.get(getAdapterPosition()).getQuestion());
                intent.putExtra("answer", itemArrayList.get(getAdapterPosition()).getAnswer());
                intent.putExtra("date", itemArrayList.get(getAdapterPosition()).getDate());
                intent.putExtra("isApprove", itemArrayList.get(getAdapterPosition()).getIsApproved());
                intent.putExtra("userid", itemArrayList.get(getAdapterPosition()).getUserID());
                intent.putExtra("name", itemArrayList.get(getAdapterPosition()).getName());
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
                        new DeleteQuestion().execute();
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


//                Toast.makeText(itemView.getContext(), "Trash" + id, Toast.LENGTH_SHORT).show();
            }

        }

    }

    private class DeleteQuestion extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.deletequestionbyadmin + "?qid=" + id);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------" + s);
        }
    }
}
