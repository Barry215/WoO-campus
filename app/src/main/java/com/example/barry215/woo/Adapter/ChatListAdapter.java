package com.example.barry215.woo.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.barry215.greendao.ChatInfoCache;
import com.example.barry215.woo.Main.Application.App;
import com.example.barry215.woo.R;
import com.example.barry215.woo.Utils.DateUtil;
import com.example.barry215.woo.ui.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by barry215 on 2016/6/2.
 */
public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private List<ChatInfoCache> chatInfoList = new ArrayList<>();
    private String senderHead;
    private String receiverHead;
    private boolean showYesterday = false;
    private boolean showBefday = false;


    public ChatListAdapter(Activity activity,List<ChatInfoCache> chatInfoList,String senderHead,String receiverHead){
        this.activity = activity;
        this.chatInfoList = chatInfoList;
        this.senderHead = senderHead;
        this.receiverHead = receiverHead;
    }

    public void addView(ChatInfoCache chatInfoCache){
        chatInfoList.add(chatInfoCache);
        notifyDataSetChanged();
    }

    public void updateView(List<ChatInfoCache> chatInfoList){
        this.chatInfoList = chatInfoList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == 0){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
            return new ChatViewHolder(view);
        }else if(viewType == 1){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chatback, parent, false);
            return new ChatBackViewHolder(view);
        }else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatInfoCache chatInfoCache = chatInfoList.get(position);

        if (holder instanceof ChatViewHolder){
            ChatViewHolder chatViewHolder = ((ChatViewHolder) holder);
            if (position == 0){
                chatViewHolder.tv_item_time.setVisibility(View.VISIBLE);
                chatViewHolder.tv_item_time.setText(DateUtil.parseDate(chatInfoCache.getTime()));
            }else if (!showYesterday && DateUtil.parseDate(chatInfoCache.getTime()).substring(0,1).equals("昨天")){
                showYesterday = true;
                chatViewHolder.tv_item_time.setVisibility(View.VISIBLE);
                chatViewHolder.tv_item_time.setText(DateUtil.parseDate(chatInfoCache.getTime()));
            }else if (!showBefday && DateUtil.parseDate(chatInfoCache.getTime()).substring(0,1).equals("前天")){
                showBefday = true;
                chatViewHolder.tv_item_time.setVisibility(View.VISIBLE);
                chatViewHolder.tv_item_time.setText(DateUtil.parseDate(chatInfoCache.getTime()));
            }

            Glide.with(activity).load(App.LOADHEADADDRESS + senderHead)
                    .transform(new GlideCircleTransform(activity))
                    .placeholder(R.drawable.person_default_icon).into(chatViewHolder.iv_item_self);

            chatViewHolder.tv_item_self.setText(chatInfoCache.getMsg());
        }else if (holder instanceof ChatBackViewHolder){
            ChatBackViewHolder chatBackViewHolder = ((ChatBackViewHolder) holder);
            if (position == 0){
                chatBackViewHolder.tv_item_time.setVisibility(View.VISIBLE);
                chatBackViewHolder.tv_item_time.setText(DateUtil.parseDate(chatInfoCache.getTime()));
            }
            Glide.with(activity).load(App.LOADHEADADDRESS + receiverHead)
                    .transform(new GlideCircleTransform(activity))
                    .placeholder(R.drawable.person_default_icon).into(chatBackViewHolder.iv_item_other);

            chatBackViewHolder.tv_item_other.setText(chatInfoCache.getMsg());
        }
    }

    @Override
    public int getItemCount() {
        return chatInfoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatInfoList.get(position).getSenderId().equals(App.getInstance().getUserId())){
            return 0;
        }else {
            return 1;
        }
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_item_self) ImageView iv_item_self;
        @Bind(R.id.tv_item_self) TextView tv_item_self;
        @Bind(R.id.tv_item_time) TextView tv_item_time;

        public ChatViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class ChatBackViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_item_other) ImageView iv_item_other;
        @Bind(R.id.tv_item_other) TextView tv_item_other;
        @Bind(R.id.tv_item_time) TextView tv_item_time;

        public ChatBackViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
