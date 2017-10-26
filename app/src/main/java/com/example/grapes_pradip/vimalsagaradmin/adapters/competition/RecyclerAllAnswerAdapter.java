package com.example.grapes_pradip.vimalsagaradmin.adapters.competition;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.model.competition.AnswerItem;

import java.util.ArrayList;

/**
 * Created by Pradip on 1/2/17.
 */
@SuppressWarnings("ALL")
public class RecyclerAllAnswerAdapter extends RecyclerView.Adapter<RecyclerAllAnswerAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<AnswerItem> itemArrayList;

    public RecyclerAllAnswerAdapter(Activity activity, ArrayList<AnswerItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.answer_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {

        final AnswerItem answerItem = itemArrayList.get(i);
        holder.txt_name.setText(answerItem.getName());
        holder.txt_answer.setText(answerItem.getAnswer());


    }

    @Override
    public int getItemCount() {

        return itemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        final TextView txt_name;
        final TextView txt_answer;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_answer = (TextView) itemView.findViewById(R.id.txt_answer);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }

        @Override
        public boolean onLongClick(View v) {
//            Toast.makeText(activity, "long Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            return false;
        }


    }

}
