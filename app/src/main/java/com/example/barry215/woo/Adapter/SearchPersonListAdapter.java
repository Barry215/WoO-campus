package com.example.barry215.woo.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.barry215.woo.Bean.UserInfoBean;
import com.example.barry215.woo.Main.Activity.PersonActivity;
import com.example.barry215.woo.R;
import com.example.barry215.woo.ui.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by barry215 on 2016/5/7.
 */
public class SearchPersonListAdapter extends RecyclerView.Adapter<SearchPersonListAdapter.SearchPersonViewHolder> {

    private Activity activity;
    private List<UserInfoBean> userInfoBeanList = new ArrayList<>();
    private String newText;

    public SearchPersonListAdapter(Activity activity) {
        this.activity = activity;
    }

    public void updateData(List<UserInfoBean> userInfoBeanList,String newText){
        this.userInfoBeanList = userInfoBeanList;
        this.newText = newText;
        notifyDataSetChanged();
    }

    @Override
    public SearchPersonListAdapter.SearchPersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_helper, parent, false);
        return new SearchPersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchPersonListAdapter.SearchPersonViewHolder holder, int position) {
        final UserInfoBean userInfoBean = userInfoBeanList.get(position);
        Glide.with(activity).load(userInfoBean.getHead())
                .transform(new GlideCircleTransform(activity))
                .placeholder(R.drawable.person_default_icon).into(holder.iv_helper);

        Pattern p = Pattern.compile(newText);

        SpannableString spannableName = new SpannableString(userInfoBean.getUsername());
        Matcher m_1 = p.matcher(spannableName);
        while (m_1.find()){
            spannableName.setSpan(new BackgroundColorSpan(Color.YELLOW), m_1.start(), m_1.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        holder.tv_helper_name.setText(spannableName);

        SpannableString spannableInfo = new SpannableString(userInfoBean.getUsersign());
        Matcher m_2 = p.matcher(spannableInfo);
        while (m_2.find()){
            spannableInfo.setSpan(new BackgroundColorSpan(Color.YELLOW), m_2.start(), m_2.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        holder.tv_helper_info.setText(spannableInfo);

        holder.iv_helper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, PersonActivity.class);
                intent.putExtra("userId", userInfoBean.getId());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userInfoBeanList.size();
    }

    public static class SearchPersonViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_helper) ImageView iv_helper;
        @Bind(R.id.tv_helper_name) TextView tv_helper_name;
        @Bind(R.id.tv_helper_info) TextView tv_helper_info;

        public SearchPersonViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
