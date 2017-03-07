package com.example.barry215.woo.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
 * Created by barry215 on 2016/4/15.
 */
public class ChooseListAdapter extends RecyclerView.Adapter<ChooseListAdapter.ChooseViewHolder> {

    private Activity activity;

    private List<Boolean> checks = new ArrayList<>();

    private List<UserInfoBean> userInfoBeanList = new ArrayList<>();

    public ChooseListAdapter(Activity activity) {
        this.activity = activity;
    }

    public void updateData(List<UserInfoBean> userInfoBeanList){
        this.userInfoBeanList = userInfoBeanList;
        notifyDataSetChanged();
    }

    public List<Boolean> getCheck(){
        return checks;
    }

    @Override
    public ChooseListAdapter.ChooseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_person, parent, false);
        return new ChooseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChooseListAdapter.ChooseViewHolder holder, final int position) {

        final UserInfoBean userInfoBean = userInfoBeanList.get(position);

        holder.checkBox.setChecked(false);

        checks.add(false);

        final String userId = userInfoBean.getId();

        Glide.with(activity).load(App.LOADHEADADDRESS + userInfoBean.getHead())
                .transform(new GlideCircleTransform(activity))
                .placeholder(R.drawable.person_default_icon).into(holder.iv_choose);

        holder.tv_1.setText(userInfoBean.getUsername());

        holder.tv_2.setText(userInfoBean.getUsersign());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holder.checkBox.setChecked(isChecked);
                checks.set(position,isChecked);
            }
        });

        holder.iv_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, PersonActivity.class);
                intent.putExtra("userId",userId);
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userInfoBeanList.size();
    }

    public static class ChooseViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_check) CheckBox checkBox;
        @Bind(R.id.iv_choose) ImageView iv_choose;
        @Bind(R.id.tv_choose_name) TextView tv_1;
        @Bind(R.id.tv_choose_have) TextView tv_2;

        public ChooseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
