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
import com.example.barry215.woo.Bean.UserInfoBean;
import com.example.barry215.woo.Main.Activity.PersonActivity;
import com.example.barry215.woo.Main.Application.App;
import com.example.barry215.woo.R;
import com.example.barry215.woo.ui.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by barry215 on 2016/5/4.
 */
public class FollowListAdapter extends RecyclerView.Adapter<FollowListAdapter.FollowViewHolder>{

    private Activity activity;
    private List<UserInfoBean> followerList = new ArrayList<>();

    public FollowListAdapter(Activity activity) {
        this.activity = activity;
    }

    public void updateData(List<UserInfoBean> followerList){
        this.followerList = followerList;
        notifyDataSetChanged();
    }

    @Override
    public FollowListAdapter.FollowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_follow, parent, false);
        return new FollowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FollowListAdapter.FollowViewHolder holder, int position) {
        final UserInfoBean userInfoBean = followerList.get(position);

        Glide.with(activity).load(App.LOADHEADADDRESS + userInfoBean.getHead())
                .transform(new GlideCircleTransform(activity))
                .placeholder(R.drawable.person_default_icon).into(holder.iv_follow);
        holder.tv_1.setText(userInfoBean.getUsername());
        holder.tv_2.setText(userInfoBean.getUsersign());

        holder.iv_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,PersonActivity.class);
                intent.putExtra("userId",userInfoBean.getId());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return followerList.size();
    }

    public static class FollowViewHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.iv_follow) ImageView iv_follow;
        @Bind(R.id.tv_follow_name) TextView tv_1;
        @Bind(R.id.tv_follow_have) TextView tv_2;

        public FollowViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
