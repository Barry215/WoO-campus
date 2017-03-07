package com.example.barry215.woo.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.barry215.woo.Bean.RemarkInfoBean;
import com.example.barry215.woo.Main.Activity.CommentActivity;
import com.example.barry215.woo.Main.Activity.PersonActivity;
import com.example.barry215.woo.Main.Activity.SelfActivity;
import com.example.barry215.woo.Main.Application.App;
import com.example.barry215.woo.R;
import com.example.barry215.woo.Utils.DateUtil;
import com.example.barry215.woo.ui.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by barry215 on 2016/4/13.
 */
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private List<RemarkInfoBean> remarkInfoBeanList = new ArrayList<>();
    private String HELP_ID;

    public CommentAdapter(Activity activity, String helpId){
        this.activity = activity;
        HELP_ID = helpId;
    }

    public void updateComment(List<RemarkInfoBean> remarkInfoBeanList){
        this.remarkInfoBeanList = remarkInfoBeanList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == 0){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
            return new CommentViewHolder_1(view);
        }else if(viewType == 1){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_sample, parent, false);
            return new CommentViewHolder_2(view);
        }else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final RemarkInfoBean remarkInfoBean = remarkInfoBeanList.get(position);

        if(holder instanceof CommentViewHolder_1){
            Glide.with(activity).load(App.LOADHEADADDRESS + remarkInfoBean.getUserhead())
                    .transform(new GlideCircleTransform(activity))
                    .placeholder(R.drawable.person_default_icon).into(((CommentViewHolder_1) holder).commenter_head);
            ((CommentViewHolder_1) holder).commenter_name.setText(remarkInfoBean.getUsername());

            ((CommentViewHolder_1) holder).comment_time.setText(DateUtil.parseTime(remarkInfoBean.getTime()));
            if(remarkInfoBean.getUsersex().equals("1")){
                Glide.with(activity).load(R.drawable.item_female).placeholder(R.drawable.sample_pic).into(((CommentViewHolder_1) holder).commenter_sex);
            }else if(remarkInfoBean.getUsersex().equals("0")){
                Glide.with(activity).load(R.drawable.item_male).placeholder(R.drawable.sample_pic).into(((CommentViewHolder_1) holder).commenter_sex);
            }
            ((CommentViewHolder_1) holder).comment_body.setText(remarkInfoBean.getContent());

            ((CommentViewHolder_1) holder).commenter_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (remarkInfoBean.getUserid().equals(App.getInstance().getUserId())) {
                        Intent intent = new Intent(activity, SelfActivity.class);
                        activity.startActivity(intent);
                    } else {
                        Intent intent = new Intent(activity, PersonActivity.class);
                        intent.putExtra("userId", remarkInfoBean.getUserid());
                        activity.startActivity(intent);
                    }
                }
            });

            ((CommentViewHolder_1) holder).comment_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(App.getInstance().getIsLogin()){
                        Intent intent = new Intent(activity, CommentActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("help_id", HELP_ID);
                        bundle.putString("commented_username", remarkInfoBean.getUsername());
                        bundle.putString("parentId", remarkInfoBean.getId());
                        intent.putExtra("bundle", bundle);
                        activity.startActivity(intent);
                    }else {
                        Toast.makeText(activity, "请您登陆后再进行评论", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else if(holder instanceof CommentViewHolder_2){
            ((CommentViewHolder_2) holder).commenter_name_simple.setText(remarkInfoBean.getUsername());
            ((CommentViewHolder_2) holder).commented_name_simple.setText(remarkInfoBean.getReplyname());
            ((CommentViewHolder_2) holder).comment_body_simple.setText(remarkInfoBean.getContent());

            ((CommentViewHolder_2) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (App.getInstance().getIsLogin()) {
                        Intent intent = new Intent(activity, CommentActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("help_id", HELP_ID);
                        bundle.putString("commented_username", remarkInfoBean.getUsername());
                        bundle.putString("parentId", remarkInfoBean.getReplyid());
                        intent.putExtra("bundle", bundle);
                        activity.startActivity(intent);
                    } else {
                        Toast.makeText(activity, "请您登陆后再进行评论", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return remarkInfoBeanList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (remarkInfoBeanList.get(position).getReplyid().equals("0")){
            return 0;
        }else {
            return 1;
        }
    }

    public static class CommentViewHolder_1 extends RecyclerView.ViewHolder {

        @Bind(R.id.commenter_head) ImageView commenter_head;
        @Bind(R.id.commenter_name) TextView commenter_name;
        @Bind(R.id.comment_time) TextView comment_time;
        @Bind(R.id.comment_body) TextView comment_body;
        @Bind(R.id.commenter_sex) ImageView commenter_sex;
        @Bind(R.id.comment_back) TextView comment_back;


        public CommentViewHolder_1(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class CommentViewHolder_2 extends RecyclerView.ViewHolder {

        @Bind(R.id.commenter_name_simple) TextView commenter_name_simple;
        @Bind(R.id.commented_name_simple) TextView commented_name_simple;
        @Bind(R.id.comment_body_simple) TextView comment_body_simple;

        public CommentViewHolder_2(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
