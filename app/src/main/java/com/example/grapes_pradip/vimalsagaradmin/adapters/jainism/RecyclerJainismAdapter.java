package com.example.grapes_pradip.vimalsagaradmin.adapters.jainism;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alexandrius.accordionswipelayout.library.SwipeLayout;
import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.activities.jainism.AudioPlayActivity;
import com.example.grapes_pradip.vimalsagaradmin.activities.jainism.CommentListJainism;
import com.example.grapes_pradip.vimalsagaradmin.activities.jainism.EditJainism;
import com.example.grapes_pradip.vimalsagaradmin.adapters.information.RecyclerLikeAdapter;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.JainismItem;
import com.example.grapes_pradip.vimalsagaradmin.model.information.LikeList;
import com.example.jean.jcplayer.JcPlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;


@SuppressWarnings("ALL")
public class RecyclerJainismAdapter extends RecyclerView.Adapter<RecyclerJainismAdapter.ViewHolder> {

    private final Activity activity;
    private final ArrayList<JainismItem> itemArrayList;
    private String id;
    public static final ArrayList<String> infoid = new ArrayList<>();
    private ArrayList<LikeList> likeLists = new ArrayList<>();
    private int page_count = 1;
    private boolean flag_scroll = false;
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private final int visibleThreshold = 0; // The minimum amount of items to have below your current scroll position before loading more.
    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;
    RecyclerView recyclerView_likes;
    RecyclerLikeAdapter recyclerLikeAdapter;
    ImageView img_nodata;

    public RecyclerJainismAdapter(Activity activity, ArrayList<JainismItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_jainism, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {

        final JainismItem orderItem = itemArrayList.get(i);
        holder.txt_title.setText(CommonMethod.decodeEmoji(orderItem.getTitle()));
        holder.txt_description.setText(CommonMethod.decodeEmoji(orderItem.getDescription()));
        holder.txt_date.setText(CommonMethod.decodeEmoji(orderItem.getDate()));
        holder.txt_time.setText(CommonMethod.decodeEmoji(orderItem.getTime()));
        holder.txt_views.setText(orderItem.getViews() + " Views");
        holder.txt_like.setText(CommonMethod.decodeEmoji(orderItem.getTotalLike()));
        holder.txt_comment.setText(CommonMethod.decodeEmoji(orderItem.getTotalComment()));


        if (orderItem.getAudio().equalsIgnoreCase("")) {
            holder.btn_play.setVisibility(View.GONE);
        } else {
            holder.btn_play.setVisibility(View.VISIBLE);
        }

        holder.lin_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeLists = new ArrayList<>();
                page_count = 1;
                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_like_list);
                dialog.show();
                ImageView img_back = (ImageView) dialog.findViewById(R.id.img_back);

                img_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
                recyclerView_likes = (RecyclerView) dialog.findViewById(R.id.recyclerView_likes);
                recyclerView_likes.setLayoutManager(linearLayoutManager);
                img_nodata = (ImageView) dialog.findViewById(R.id.img_nodata);

                if (CommonMethod.isInternetConnected(activity)) {
                    new LikeUserList().execute(orderItem.getId());
                } else {
                    Toast.makeText(activity, R.string.internet, Toast.LENGTH_SHORT).show();
                }
                recyclerView_likes.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        visibleItemCount = recyclerView.getChildCount();
                        totalItemCount = linearLayoutManager.getItemCount();
                        firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                        if (flag_scroll) {
                            Log.e("flag-Scroll", flag_scroll + "");
                        } else {
                            if (loading) {
                                Log.e("flag-Loading", loading + "");
                                if (totalItemCount > previousTotal) {
                                    loading = false;
                                    previousTotal = totalItemCount;
                                    //Log.e("flag-IF", (totalItemCount > previousTotal) + "");
                                }
                            }
                            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                Log.e("flag-Loading_second_if", loading + "");
                                if (CommonMethod.isInternetConnected(activity)) {

                                    Log.e("total count", "--------------------" + page_count);

                                    page_count++;
//                                    new LikeUserList().execute();
                                } else {
                                    Toast.makeText(activity, R.string.internet, Toast.LENGTH_SHORT).show();
                                }
                                loading = true;

                            }
                        }
                    }
                });


                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });

        holder.lin_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CommentListJainism.class);
                intent.putExtra("j_id", itemArrayList.get(holder.getAdapterPosition()).getId());
                intent.putExtra("click_action", itemArrayList.get(holder.getAdapterPosition()).getTitle());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        holder.txt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EditJainism.class);
                intent.putExtra("id", itemArrayList.get(holder.getAdapterPosition()).getId());
                intent.putExtra("title", itemArrayList.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("description", itemArrayList.get(holder.getAdapterPosition()).getDescription());
                intent.putExtra("date", itemArrayList.get(holder.getAdapterPosition()).getDate());
                intent.putExtra("time", itemArrayList.get(holder.getAdapterPosition()).getTime());
                intent.putExtra("audio", itemArrayList.get(holder.getAdapterPosition()).getAudio());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        holder.btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AudioPlayActivity.class);
                intent.putExtra("audio", orderItem.getAudio());
                activity.startActivity(intent);
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
                        new JainismDelete().execute();
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
        final TextView txt_views;
        final TextView txt_time;
        final TextView txt_edit;
        final TextView txt_delete;
        final TextView txt_like;
        final TextView txt_comment;
        final SwipeLayout swipeLayout;
        final LinearLayout lin_delete, lin_comment, lin_like;
        final CheckBox check_delete;
        final Button btn_play;
//        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_like = (TextView) itemView.findViewById(R.id.txt_like);
            txt_comment = (TextView) itemView.findViewById(R.id.txt_comment);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_views = (TextView) itemView.findViewById(R.id.txt_views);
            txt_description = (TextView) itemView.findViewById(R.id.txt_description);
            txt_edit = (TextView) itemView.findViewById(R.id.txt_edit);
            txt_delete = (TextView) itemView.findViewById(R.id.txt_delete);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            lin_delete = (LinearLayout) itemView.findViewById(R.id.lin_delete);
            lin_comment = (LinearLayout) itemView.findViewById(R.id.lin_comment);
            lin_like = (LinearLayout) itemView.findViewById(R.id.lin_like);
            check_delete = (CheckBox) itemView.findViewById(R.id.check_delete);
            btn_play = itemView.findViewById(R.id.btn_play);
//            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(activity, "on Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();

//            Intent intent = new Intent(v.getContext(), NoteDetailActivity.class);
//            intent.putExtra("id", itemArrayList.get(getAdapterPosition()).getId());
//            intent.putExtra("title", itemArrayList.get(getAdapterPosition()).getTitle());
//            intent.putExtra("description", itemArrayList.get(getAdapterPosition()).getDescription());
//            intent.putExtra("date", itemArrayList.get(getAdapterPosition()).getDate());
//            intent.putExtra("time", itemArrayList.get(getAdapterPosition()).getTime());
//            v.getContext().startActivity(intent);
//            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        @Override
        public boolean onLongClick(View v) {
//            Toast.makeText(activity, "long Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();

            return false;
        }

    }

    private class JainismDelete extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("id", id));
            responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + "jainaism/delete", nameValuePairs, activity);
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

    private class LikeUserList extends AsyncTask<String, Void, String> {
        String responseJSON = "";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage(activity.getResources().getString(R.string.progressmsg));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("jid", params[0]));
            nameValuePairs.add(new BasicNameValuePair("page", "1"));
            nameValuePairs.add(new BasicNameValuePair("psize", "1000"));
            responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + "jainaism/getallusernamesforlike", nameValuePairs, activity);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    Log.e("response", "-----------------" + "success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("ID");
                        String informationID = jsonObject1.getString("JainismID");
                        String userID = jsonObject1.getString("UserID");
                        String name = jsonObject1.getString("Name");
                        likeLists.add(new LikeList(id, informationID, userID, name));
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            if (recyclerView_likes != null) {
                recyclerLikeAdapter = new RecyclerLikeAdapter(activity, likeLists);
                Log.e("response", "-----------------" + "object");
                if (recyclerLikeAdapter.getItemCount() != 0) {
                    recyclerView_likes.setVisibility(View.VISIBLE);
                    img_nodata.setVisibility(View.GONE);
                    recyclerView_likes.setAdapter(recyclerLikeAdapter);
                    Log.e("response", "-----------------" + "setlayout");
                } else {
                    recyclerView_likes.setVisibility(View.GONE);
                    img_nodata.setVisibility(View.VISIBLE);
                }
            }
        }
    }

}
