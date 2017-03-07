package com.example.barry215.woo.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.barry215.woo.Main.Activity.NewsListActivity;
import com.example.barry215.woo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by barry215 on 2016/5/2.
 */
public class FindListAdapter extends RecyclerView.Adapter<FindListAdapter.FindViewHolder> {

    private Activity activity;
    private List<String> tabNames = new ArrayList<>();
    private List<String> tabInfos = new ArrayList<>();
    private List<Integer> tabImages = new ArrayList<>();

    public FindListAdapter(Activity activity) {
        this.activity = activity;
    }

    public void addAll(List<String> tabNames,List<String> tabInfos,List<Integer> tabImages) {
        this.tabNames = tabNames;
        this.tabInfos = tabInfos;
        this.tabImages = tabImages;

        notifyDataSetChanged();
    }


    @Override
    public FindListAdapter.FindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_find, parent, false);
        return new FindViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FindListAdapter.FindViewHolder holder, final int position) {
        Glide.with(activity).load(tabImages.get(position)).placeholder(R.drawable.sample_pic).into(holder.iv_find_head);
        holder.tv_find_title.setText(tabNames.get(position));
        holder.tv_find_info.setText(tabInfos.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,NewsListActivity.class);
                intent.putExtra("tabName",tabNames.get(position));
                intent.putExtra("position",position);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public static class FindViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_find_head) ImageView iv_find_head;
        @Bind(R.id.tv_find_title) TextView tv_find_title;
        @Bind(R.id.tv_find_info) TextView tv_find_info;

        public FindViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
