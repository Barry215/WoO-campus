package com.example.barry215.woo.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.barry215.woo.Bean.CommonBean;
import com.example.barry215.woo.Bean.MessageInfoBean;
import com.example.barry215.woo.Main.Activity.MessageActivity;
import com.example.barry215.woo.Main.Activity.MessageReadActivity;
import com.example.barry215.woo.Main.Activity.PersonActivity;
import com.example.barry215.woo.Main.Application.App;
import com.example.barry215.woo.Network.RetrofitManager;
import com.example.barry215.woo.R;
import com.example.barry215.woo.Utils.DateUtil;
import com.example.barry215.woo.ui.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by barry215 on 2016/4/12.
 */
public class MsgListAdapter extends RecyclerView.Adapter<MsgListAdapter.MsgViewHolder> {

    private Activity activity;
    private List<MessageInfoBean> messageInfoBeanList = new ArrayList<>();

    public MsgListAdapter(Activity activity) {
        this.activity = activity;
    }

    public void updateData(List<MessageInfoBean> messageInfoBeanList){
        this.messageInfoBeanList = messageInfoBeanList;
        notifyDataSetChanged();
    }

    @Override
    public MsgListAdapter.MsgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg, parent, false);
        return new MsgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MsgListAdapter.MsgViewHolder holder, int position) {
        final MessageInfoBean messageInfoBean = messageInfoBeanList.get(position);

        Glide.with(activity).load(App.LOADHEADADDRESS + messageInfoBean.getUserhead())
                .transform(new GlideCircleTransform(activity))
                .placeholder(R.drawable.person_default_icon).into(holder.iv_msg_senderHead);
        holder.tv_msg_time.setText(DateUtil.parseTime(messageInfoBean.getCreatetime()));
        holder.tv_msg_senderName.setText(messageInfoBean.getUsername());

        if(messageInfoBean.getState().equals("1")){
            holder.tv_msg_body.setText(messageInfoBean.getContent());
        }else {
            holder.tv_msg_body.setText("未读");
        }

        holder.iv_msg_senderHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, PersonActivity.class);
                intent.putExtra("userId", messageInfoBean.getUserid());
                activity.startActivity(intent);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitManager.builder().changeMsgRead(messageInfoBean.getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<CommonBean>() {
                            @Override
                            public void call(CommonBean commonBean) {
                                if (commonBean.getRes().equals("0")) {
                                    Intent intent = new Intent(activity, MessageReadActivity.class);
                                    intent.putExtra("helpId", messageInfoBean.getHelpid());
                                    intent.putExtra("userId", messageInfoBean.getUserid());
                                    intent.putExtra("userHead", messageInfoBean.getUserhead());
                                    intent.putExtra("userName", messageInfoBean.getUsername());
                                    intent.putExtra("content", messageInfoBean.getContent());
                                    intent.putExtra("createTime", messageInfoBean.getCreatetime());
                                    activity.startActivity(intent);
                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Toast.makeText(activity, throwable.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((MessageActivity) activity).showDeleteMsg(messageInfoBean.getId());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageInfoBeanList.size();
    }

    public static class MsgViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_msg_senderHead) ImageView iv_msg_senderHead;
        @Bind(R.id.tv_msg_senderName) TextView tv_msg_senderName;
        @Bind(R.id.tv_msg_body) TextView tv_msg_body;
        @Bind(R.id.tv_msg_time) TextView tv_msg_time;

        public MsgViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
