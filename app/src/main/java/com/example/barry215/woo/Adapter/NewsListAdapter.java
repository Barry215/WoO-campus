package com.example.barry215.woo.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.barry215.woo.Bean.HelpDetailBean;
import com.example.barry215.woo.Main.Activity.NewsDetailActivity;
import com.example.barry215.woo.Main.Application.App;
import com.example.barry215.woo.R;
import com.example.barry215.woo.Utils.DateUtil;
import com.example.barry215.woo.ui.GlideCircleTransform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by barry215 on 2016/4/11.
 */
public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsViewHolder> {

    private Activity activity;
    private List<HelpDetailBean> helpInfoCacheList = new ArrayList<>();

    public NewsListAdapter(Activity activity) {
        this.activity = activity;
    }

    public void initView(List<HelpDetailBean> helpInfoCacheList){
        this.helpInfoCacheList = helpInfoCacheList;
        Collections.reverse(this.helpInfoCacheList);
        notifyDataSetChanged();
    }

    public void addView(List<HelpDetailBean> helpInfoCacheList){
        if (this.helpInfoCacheList.size() == 0){
            initView(helpInfoCacheList);
        }else {
            Collections.reverse(this.helpInfoCacheList);
            this.helpInfoCacheList.addAll(helpInfoCacheList);
            Collections.reverse(this.helpInfoCacheList);
        }
        notifyDataSetChanged();
    }

    @Override
    public NewsListAdapter.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsListAdapter.NewsViewHolder holder, final int position) {
        if (helpInfoCacheList.size() != 0){
            final HelpDetailBean helpDetailBean = helpInfoCacheList.get(position);

            holder.item_time.setText(DateUtil.parseTime(helpDetailBean.getStarttime()));
            holder.item_name.setText(helpDetailBean.getUsername());
            holder.item_school.setText(helpDetailBean.getUniversity());
            if(helpDetailBean.getState().equals("1")){
                holder.item_state.setText("正在解决");
                holder.item_state_color.setBackgroundColor(activity.getResources().getColor(R.color.YELLOW));
            }else if(helpDetailBean.getState().equals("0")) {
                holder.item_state.setText("未解决");
                holder.item_state_color.setBackgroundColor(activity.getResources().getColor(R.color.RED));
            }else {
                holder.item_state_color.setBackgroundColor(activity.getResources().getColor(R.color.GREEN));
                holder.item_state.setText("已解决");
            }
            holder.item_body.setText(helpDetailBean.getContent());
            holder.item_deadTime.setText(DateUtil.lastDate(helpDetailBean.getEndtime()));
            holder.item_reward.setText(helpDetailBean.getReward() + "元");
            if(helpDetailBean.getSex().equals("1")){
                holder.item_sex.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.item_female));
            }else {
                holder.item_sex.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.item_male));
            }

            if (TextUtils.isEmpty(helpDetailBean.getPic1())){
                holder.item_iv_1.setVisibility(View.GONE);
                holder.item_iv_2.setVisibility(View.GONE);
                holder.item_iv_3.setVisibility(View.GONE);
            }else if (helpDetailBean.getPic1().equals("000")){
                holder.item_iv_1.setVisibility(View.GONE);
                holder.item_iv_2.setVisibility(View.GONE);
                holder.item_iv_3.setVisibility(View.GONE);
            }else if (!helpDetailBean.getPic1().equals("000") && helpDetailBean.getPic2().equals("000")){
                Glide.with(activity).load(App.LOADIMGADDRESS + helpDetailBean.getPic1()).placeholder(R.drawable.sample_pic).into(holder.item_iv_1);
                holder.item_iv_1.setVisibility(View.VISIBLE);
                holder.item_iv_2.setVisibility(View.GONE);
                holder.item_iv_3.setVisibility(View.GONE);
            }else if (!helpDetailBean.getPic1().equals("000")
                    && !helpDetailBean.getPic2().equals("000")
                    && helpDetailBean.getPic3().equals("000")){
                Glide.with(activity).load(App.LOADIMGADDRESS + helpDetailBean.getPic1()).placeholder(R.drawable.sample_pic).into(holder.item_iv_1);
                Glide.with(activity).load(App.LOADIMGADDRESS + helpDetailBean.getPic2()).placeholder(R.drawable.sample_pic).into(holder.item_iv_2);
                holder.item_iv_1.setVisibility(View.VISIBLE);
                holder.item_iv_2.setVisibility(View.VISIBLE);
                holder.item_iv_3.setVisibility(View.GONE);
            }else if (!helpDetailBean.getPic1().equals("000")
                    && !helpDetailBean.getPic2().equals("000")
                    && !helpDetailBean.getPic3().equals("000")){
                Glide.with(activity).load(App.LOADIMGADDRESS + helpDetailBean.getPic1()).placeholder(R.drawable.sample_pic).into(holder.item_iv_1);
                Glide.with(activity).load(App.LOADIMGADDRESS + helpDetailBean.getPic2()).placeholder(R.drawable.sample_pic).into(holder.item_iv_2);
                Glide.with(activity).load(App.LOADIMGADDRESS + helpDetailBean.getPic3()).placeholder(R.drawable.sample_pic).into(holder.item_iv_3);
                holder.item_iv_1.setVisibility(View.VISIBLE);
                holder.item_iv_2.setVisibility(View.VISIBLE);
                holder.item_iv_3.setVisibility(View.VISIBLE);
            }

            Glide.with(activity).load(App.LOADHEADADDRESS + helpDetailBean.getUserhead())
                    .transform(new GlideCircleTransform(activity))
                    .placeholder(R.drawable.person_default_icon).into(holder.item_head);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(App.getInstance().getIsLogin()){
                        Intent intent = new Intent(activity,NewsDetailActivity.class);
                        intent.putExtra("helpId", helpDetailBean.getId());
                        activity.startActivity(intent);
                    }else {
                        Toast.makeText(activity, "你还未登陆!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return helpInfoCacheList.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_head) ImageView item_head;
        @Bind(R.id.item_name) TextView item_name;
        @Bind(R.id.item_school) TextView item_school;
        @Bind(R.id.item_time) TextView item_time;
        @Bind(R.id.item_state) TextView item_state;
        @Bind(R.id.item_body) TextView item_body;
        @Bind(R.id.item_deadTime) TextView item_deadTime;
        @Bind(R.id.item_reward) TextView item_reward;
        @Bind(R.id.item_iv_1) ImageView item_iv_1;
        @Bind(R.id.item_iv_2) ImageView item_iv_2;
        @Bind(R.id.item_iv_3) ImageView item_iv_3;
        @Bind(R.id.item_sex) ImageView item_sex;
        @Bind(R.id.item_state_color) RelativeLayout item_state_color;

        public NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
