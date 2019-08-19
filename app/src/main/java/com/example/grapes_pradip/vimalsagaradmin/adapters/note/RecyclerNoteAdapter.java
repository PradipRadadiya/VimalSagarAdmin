package com.example.grapes_pradip.vimalsagaradmin.adapters.note;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexandrius.accordionswipelayout.library.SwipeLayout;
import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.activities.note.EditNote;
import com.example.grapes_pradip.vimalsagaradmin.activities.note.NoteDetailActivity;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.note.NoteItem;

import java.util.ArrayList;


@SuppressWarnings("ALL")
public class RecyclerNoteAdapter extends RecyclerView.Adapter<RecyclerNoteAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<NoteItem> itemArrayList;
    private String id;
    public static final ArrayList<String> infoid = new ArrayList<>();

    public RecyclerNoteAdapter(Activity activity, ArrayList<NoteItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_note, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {

        final NoteItem orderItem = itemArrayList.get(i);
        holder.txt_title.setText(CommonMethod.decodeEmoji(orderItem.getTitle()));
        holder.txt_description.setText(CommonMethod.decodeEmoji(orderItem.getDescription()));
        holder.txt_date.setText(CommonMethod.decodeEmoji(orderItem.getDate()));
        holder.txt_time.setText(CommonMethod.decodeEmoji(orderItem.getTime()));

        holder.txt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EditNote.class);
                intent.putExtra("id", itemArrayList.get(holder.getAdapterPosition()).getId());
                intent.putExtra("title", itemArrayList.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("description", itemArrayList.get(holder.getAdapterPosition()).getDescription());
                intent.putExtra("date", itemArrayList.get(holder.getAdapterPosition()).getDate());
                intent.putExtra("time", itemArrayList.get(holder.getAdapterPosition()).getTime());
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
                        new DeleteNote().execute();
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
//        holder.check_delete.setChecked(orderItem.isSelected() ? true : false);






    }

    @Override
    public int getItemCount() {

        return itemArrayList.size();
    }

    @SuppressWarnings("unused")
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        final TextView txt_title;
        final TextView txt_description;
        final TextView txt_date;
        final TextView txt_time;
        final TextView txt_edit;
        final TextView txt_delete;
        final SwipeLayout swipeLayout;
        final LinearLayout lin_delete;
        final CheckBox check_delete;
//        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_description = (TextView) itemView.findViewById(R.id.txt_description);
            txt_edit = (TextView) itemView.findViewById(R.id.txt_edit);
            txt_delete = (TextView) itemView.findViewById(R.id.txt_delete);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            lin_delete = (LinearLayout) itemView.findViewById(R.id.lin_delete);
            check_delete = (CheckBox) itemView.findViewById(R.id.check_delete);
//            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(activity, "on Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();


            Intent intent = new Intent(v.getContext(), NoteDetailActivity.class);
            intent.putExtra("id", itemArrayList.get(getAdapterPosition()).getId());
            intent.putExtra("title", itemArrayList.get(getAdapterPosition()).getTitle());
            intent.putExtra("description", itemArrayList.get(getAdapterPosition()).getDescription());
            intent.putExtra("date", itemArrayList.get(getAdapterPosition()).getDate());
            intent.putExtra("time", itemArrayList.get(getAdapterPosition()).getTime());
            v.getContext().startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        @Override
        public boolean onLongClick(View v) {
//            Toast.makeText(activity, "long Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();

            return false;
        }

    }

    private class DeleteNote extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + "competition/deletecompetitionnote/"+ "?qid=" + id);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------" + s);
        }
    }

    public ArrayList<String> getDeleteData() {
        return infoid;
    }
}
