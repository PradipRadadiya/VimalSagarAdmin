package com.example.grapes_pradip.vimalsagaradmin.adapters.competition;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.model.competition.CompetitionQuestionCorrectWrongItem;

import java.util.ArrayList;


@SuppressWarnings("ALL")
public class RecyclerCompetitionQuestionCorrectWrongAnswerAdapter extends RecyclerView.Adapter<RecyclerCompetitionQuestionCorrectWrongAnswerAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<CompetitionQuestionCorrectWrongItem> itemArrayList;
    private String id;

    public RecyclerCompetitionQuestionCorrectWrongAnswerAdapter(Activity activity, ArrayList<CompetitionQuestionCorrectWrongItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_particular_user, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {

        final CompetitionQuestionCorrectWrongItem competitionQuestionItem = itemArrayList.get(i);

//        holder.img_option.setVisibility(View.VISIBLE);
        holder.txt_index.setText(CommonMethod.decodeEmoji(i+1+") "));
        holder.txt_title.setText(CommonMethod.decodeEmoji(competitionQuestionItem.getQuestion()));
        holder.txt_type.setText(CommonMethod.decodeEmoji(competitionQuestionItem.getAnswer()));
        holder.txt_answer.setText(CommonMethod.decodeEmoji(competitionQuestionItem.getTrue_answer()));

        if (competitionQuestionItem.getTrue_answer().equalsIgnoreCase("")){
            holder.txt_answer.setVisibility(View.GONE);
        }else{
            holder.txt_answer.setVisibility(View.VISIBLE);
            if (competitionQuestionItem.getAnswer().equalsIgnoreCase("null")) {
                holder.txt_type.setText("--");
            } else {
                holder.txt_type.setText(CommonMethod.decodeEmoji(competitionQuestionItem.getAnswer()));
            }
        }

        String[] options;
        options = null;
        options=competitionQuestionItem.getOptions().split("\\|");



        holder.txt_option.setText("");
        Log.e("option lenght","--------------"+options.length);
        for(int j=0;j<options.length;j++){
            holder.txt_option.append(options[j]+"\n");
//            holder.txt_option.append((j+1)+") "+options[j]+"\n");
        }
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    @SuppressWarnings("unused")
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        final TextView txt_index;
        final TextView txt_title;
        final TextView txt_type;
        final TextView txt_answer;
        final TextView txt_option;
        final TextView txt_edit;
        final TextView txt_delete;
        final ImageView img_option;
        final ImageView img_view;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_index = (TextView) itemView.findViewById(R.id.txt_index_count);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_type = (TextView) itemView.findViewById(R.id.txt_type);
            txt_answer = (TextView) itemView.findViewById(R.id.txt_answer);
            txt_option = (TextView) itemView.findViewById(R.id.txt_option);
            img_option = (ImageView) itemView.findViewById(R.id.img_option);
            img_view = (ImageView) itemView.findViewById(R.id.img_view);
            txt_edit = (TextView) itemView.findViewById(R.id.txt_edit);
            txt_delete = (TextView) itemView.findViewById(R.id.txt_delete);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
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





    }


}
