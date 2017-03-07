package com.example.barry215.woo.Main.Activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.barry215.woo.Base.BaseSwipeBackActivity;
import com.example.barry215.woo.Bean.HelpInfoBean;
import com.example.barry215.woo.Main.Application.App;
import com.example.barry215.woo.Network.RetrofitManager;
import com.example.barry215.woo.R;
import com.example.barry215.woo.Utils.DateUtil;
import com.example.barry215.woo.ui.GlideCircleTransform;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by barry215 on 2016/5/4.
 */
public class MessageReadActivity extends BaseSwipeBackActivity {
    @Bind(R.id.toolbar_msgDetail) Toolbar toolbar;
    @Bind(R.id.message_head) ImageView message_head;
    @Bind(R.id.tv_msg_senderName) TextView tv_msg_senderName;
    @Bind(R.id.tv_msg_time) TextView tv_msg_time;
    @Bind(R.id.tv_msg_body) TextView tv_msg_body;
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
    @Bind(R.id.item_card) CardView item_card;

    private String helpId;
    private String userId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_messageread;
    }

    @Override
    protected void afterCreate() {
        initToolbar();
        initPageView();
        initClick();
    }

    private void initClick() {
        message_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageReadActivity.this, PersonActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        item_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageReadActivity.this, NewsDetailActivity.class);
                intent.putExtra("helpId", helpId);
                startActivity(intent);
            }
        });
    }

    private void initPageView() {
        helpId = getIntent().getStringExtra("helpId");
        userId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String userHead = getIntent().getStringExtra("userHead");
        String content = getIntent().getStringExtra("content");
        String createTime = getIntent().getStringExtra("createTime");


        Glide.with(this).load(App.LOADHEADADDRESS + userHead)
                .transform(new GlideCircleTransform(this))
                .placeholder(R.drawable.person_default_icon).into(message_head);

        tv_msg_senderName.setText(userName);
        tv_msg_time.setText(DateUtil.parseTime(createTime));
        tv_msg_body.setText(content);

        if (!helpId.equals("0")){
            item_card.setVisibility(View.VISIBLE);
            RetrofitManager.builder().getHelpInfo(helpId,App.getInstance().getPlayId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<HelpInfoBean>() {
                        @Override
                        public void call(HelpInfoBean helpInfoBean) {

                            item_time.setText(DateUtil.parseTime(helpInfoBean.getCreatetime()));
                            item_name.setText(helpInfoBean.getUsername());
                            item_school.setText(helpInfoBean.getUniversity());
                            if (helpInfoBean.getState().equals("0")) {
                                item_state.setText("未解决");
                                item_state_color.setBackgroundColor(getResources().getColor(R.color.RED));
                            } else if (helpInfoBean.getState().equals("1")) {
                                item_state.setText("正在解决");
                                item_state_color.setBackgroundColor(getResources().getColor(R.color.YELLOW));
                            } else {
                                item_state_color.setBackgroundColor(getResources().getColor(R.color.GREEN));
                                item_state.setText("已解决");
                            }
                            item_body.setText(helpInfoBean.getContent());
                            item_deadTime.setText(DateUtil.lastDate(helpInfoBean.getEndtime()));
                            item_reward.setText(helpInfoBean.getReward() + "元");
                            if (helpInfoBean.getSex().equals("1")) {
                                item_sex.setImageDrawable(ContextCompat.getDrawable(MessageReadActivity.this, R.drawable.item_female));
                            } else {
                                item_sex.setImageDrawable(ContextCompat.getDrawable(MessageReadActivity.this, R.drawable.item_male));
                            }

                            if (TextUtils.isEmpty(helpInfoBean.getPic1())){
                                item_iv_1.setVisibility(View.GONE);
                                item_iv_2.setVisibility(View.GONE);
                                item_iv_3.setVisibility(View.GONE);
                            }else if (helpInfoBean.getPic1().equals("000")) {
                                item_iv_1.setVisibility(View.GONE);
                                item_iv_2.setVisibility(View.GONE);
                                item_iv_3.setVisibility(View.GONE);
                            } else if (!helpInfoBean.getPic1().equals("000") && helpInfoBean.getPic2().equals("000")) {
                                Glide.with(MessageReadActivity.this).load(App.LOADIMGADDRESS + helpInfoBean.getPic1()).placeholder(R.drawable.sample_pic).into(item_iv_1);
                                item_iv_1.setVisibility(View.VISIBLE);
                                item_iv_2.setVisibility(View.GONE);
                                item_iv_3.setVisibility(View.GONE);
                            } else if (!helpInfoBean.getPic1().equals("000")
                                    && !helpInfoBean.getPic2().equals("000")
                                    && helpInfoBean.getPic3().equals("000")) {
                                Glide.with(MessageReadActivity.this).load(App.LOADIMGADDRESS + helpInfoBean.getPic1()).placeholder(R.drawable.sample_pic).into(item_iv_1);
                                Glide.with(MessageReadActivity.this).load(App.LOADIMGADDRESS + helpInfoBean.getPic2()).placeholder(R.drawable.sample_pic).into(item_iv_2);
                                item_iv_1.setVisibility(View.VISIBLE);
                                item_iv_2.setVisibility(View.VISIBLE);
                                item_iv_3.setVisibility(View.GONE);
                            } else if (!helpInfoBean.getPic1().equals("000")
                                    && !helpInfoBean.getPic2().equals("000")
                                    && !helpInfoBean.getPic3().equals("000")) {
                                Glide.with(MessageReadActivity.this).load(App.LOADIMGADDRESS + helpInfoBean.getPic1()).placeholder(R.drawable.sample_pic).into(item_iv_1);
                                Glide.with(MessageReadActivity.this).load(App.LOADIMGADDRESS + helpInfoBean.getPic2()).placeholder(R.drawable.sample_pic).into(item_iv_2);
                                Glide.with(MessageReadActivity.this).load(App.LOADIMGADDRESS + helpInfoBean.getPic3()).placeholder(R.drawable.sample_pic).into(item_iv_3);
                                item_iv_1.setVisibility(View.VISIBLE);
                                item_iv_2.setVisibility(View.VISIBLE);
                                item_iv_3.setVisibility(View.VISIBLE);
                            }
                            Glide.with(MessageReadActivity.this).load(App.LOADHEADADDRESS + helpInfoBean.getUserhead())
                                    .transform(new GlideCircleTransform(MessageReadActivity.this))
                                    .placeholder(R.drawable.person_default_icon).into(item_head);

                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Toast.makeText(MessageReadActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
        }else {
            item_card.setVisibility(View.GONE);
        }
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        setTitle("");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
