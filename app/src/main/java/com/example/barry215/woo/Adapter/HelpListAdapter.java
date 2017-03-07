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
import com.example.barry215.woo.Bean.UserListBean;
import com.example.barry215.woo.Main.Activity.HelpDetailActivity;
import com.example.barry215.woo.Main.Application.App;
import com.example.barry215.woo.Network.RetrofitManager;
import com.example.barry215.woo.R;
import com.example.barry215.woo.Utils.DateUtil;
import com.example.barry215.woo.ui.GlideCircleTransform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by barry215 on 2016/4/11.
 */
public class HelpListAdapter extends RecyclerView.Adapter<HelpListAdapter.HelpViewHolder> {

    private Activity activity;
    private List<HelpDetailBean> helpDetailBeanList = new ArrayList<>();
    private OnLongItemClickListener mOnLongItemClickListener;

    public HelpListAdapter(Activity activity) {
        this.activity = activity;
    }

    public void initView(List<HelpDetailBean> helpDetailBeanList){
        this.helpDetailBeanList = helpDetailBeanList;
        Collections.reverse(this.helpDetailBeanList);
        notifyDataSetChanged();
    }

    public void addView(List<HelpDetailBean> helpDetailBeanList){
        if (this.helpDetailBeanList.size() == 0){
            initView(helpDetailBeanList);
        }else {
            Collections.reverse(this.helpDetailBeanList);
            this.helpDetailBeanList.addAll(helpDetailBeanList);
            Collections.reverse(this.helpDetailBeanList);
        }
        notifyDataSetChanged();
    }

    public interface OnLongItemClickListener {
        void onLongItemClick(View view, String helpId);
    }

    public void setOnLongItemClickListener(OnLongItemClickListener mOnLongItemClickListener) {
        this.mOnLongItemClickListener = mOnLongItemClickListener;
    }

    @Override
    public HelpListAdapter.HelpViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_help, parent, false);
        return new HelpViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final HelpListAdapter.HelpViewHolder holder, final int position) {
        final HelpDetailBean helpDetailBean = helpDetailBeanList.get(position);

        holder.item_name.setText(helpDetailBean.getUsername());
        holder.item_school.setText(helpDetailBean.getUniversity());
        holder.item_time.setText(DateUtil.parseTime(helpDetailBean.getStarttime()));

        if(helpDetailBean.getState().equals("1")){
            holder.item_state_color.setBackgroundColor(activity.getResources().getColor(R.color.YELLOW));
            holder.item_state.setText("正在解决");
        }else if (helpDetailBean.getState().equals("0")){
            holder.item_state_color.setBackgroundColor(activity.getResources().getColor(R.color.RED));
            holder.item_state.setText("未解决");
        }else {
            holder.item_state_color.setBackgroundColor(activity.getResources().getColor(R.color.GREEN));
            holder.item_state.setText("已解决");
        }

        holder.item_body.setText(helpDetailBean.getContent());
        holder.item_deadTime.setText(DateUtil.lastDate(helpDetailBean.getEndtime()));
        holder.item_reward.setText(helpDetailBean.getReward() + "元");
        if (!DateUtil.isPast(helpDetailBean.getEndtime())
                && DateUtil.isDeadLine(helpDetailBean.getStarttime(),helpDetailBean.getEndtime())){
            holder.item_notice.setText("您的求助快要过期了，请注意时间！");
            holder.item_notice.setVisibility(View.VISIBLE);
        }
        if (DateUtil.isPast(helpDetailBean.getEndtime())){
            holder.item_notice.setText("您的求助已经过期");
            holder.item_notice.setVisibility(View.VISIBLE);
        }

        RetrofitManager.builder().getHelpPeopleList(helpDetailBean.getId(),"100","1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserListBean>() {
                    @Override
                    public void call(UserListBean userListBean) {
                        holder.item_sum.setText(userListBean.getRes().size()+"");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(activity, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });


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
                Intent intent = new Intent(activity, HelpDetailActivity.class);
                intent.putExtra("helpId", helpDetailBean.getId());
                activity.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (helpDetailBean.getState().equals("1")){
                    Toast.makeText(activity,"您不能删除正在帮助的求助",Toast.LENGTH_SHORT).show();
                }else {
                    mOnLongItemClickListener.onLongItemClick(holder.itemView, helpDetailBean.getId());
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return helpDetailBeanList.size();
    }

    public static class HelpViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_head) ImageView item_head;
        @Bind(R.id.item_name) TextView item_name;
        @Bind(R.id.item_school) TextView item_school;
        @Bind(R.id.item_time) TextView item_time;
        @Bind(R.id.item_state) TextView item_state;
        @Bind(R.id.item_body) TextView item_body;
        @Bind(R.id.item_deadTime) TextView item_deadTime;
        @Bind(R.id.item_reward) TextView item_reward;
        @Bind(R.id.item_sum) TextView item_sum;
        @Bind(R.id.item_notice) TextView item_notice;
        @Bind(R.id.item_iv_1) ImageView item_iv_1;
        @Bind(R.id.item_iv_2) ImageView item_iv_2;
        @Bind(R.id.item_iv_3) ImageView item_iv_3;
        @Bind(R.id.item_sex) ImageView item_sex;
        @Bind(R.id.item_state_color) RelativeLayout item_state_color;

        public HelpViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
