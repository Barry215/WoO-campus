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
 * Created by barry215 on 2016/5/2.
 */
public class HelperListAdapter extends RecyclerView.Adapter<HelperListAdapter.HelperViewHolder> {

    private Activity activity;
    private List<UserInfoBean> userInfoBeanList = new ArrayList<>();
    private OnItemChooseListener mOnItemChooseListener;

    public HelperListAdapter(Activity activity) {
        this.activity = activity;
    }

    public interface OnItemChooseListener {
        void onItemChooseClick(View view, String userId);
    }

    public void setOnItemChooseListener(OnItemChooseListener mOnItemChooseListener) {
        this.mOnItemChooseListener = mOnItemChooseListener;
    }

    public void updateData(List<UserInfoBean> userInfoBeanList){
        this.userInfoBeanList = userInfoBeanList;
        notifyDataSetChanged();
    }

    @Override
    public HelperListAdapter.HelperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_helper, parent, false);
        return new HelperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HelperListAdapter.HelperViewHolder holder, final int position) {
        final UserInfoBean userInfoBean = userInfoBeanList.get(position);

        Glide.with(activity).load(App.LOADHEADADDRESS + userInfoBean.getHead())
                .transform(new GlideCircleTransform(activity))
                .placeholder(R.drawable.person_default_icon).into(holder.iv_helper);

        holder.tv_helper_name.setText(userInfoBean.getUsername());
        holder.tv_helper_info.setText(userInfoBean.getUsersign());

        holder.iv_helper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, PersonActivity.class);
                intent.putExtra("userId", userInfoBean.getId());
                activity.startActivity(intent);
            }
        });

        if (mOnItemChooseListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemChooseListener.onItemChooseClick(holder.itemView,userInfoBean.getId());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return userInfoBeanList.size();
    }

    public static class HelperViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_helper) ImageView iv_helper;
        @Bind(R.id.tv_helper_name) TextView tv_helper_name;
        @Bind(R.id.tv_helper_info) TextView tv_helper_info;

        public HelperViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
