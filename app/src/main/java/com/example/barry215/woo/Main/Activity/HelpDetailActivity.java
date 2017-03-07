package com.example.barry215.woo.Main.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.barry215.woo.Adapter.CommentAdapter;
import com.example.barry215.woo.Base.BaseSwipeBackActivity;
import com.example.barry215.woo.Bean.HelpInfoBean;
import com.example.barry215.woo.Bean.RemarkInfoBean;
import com.example.barry215.woo.Main.Application.App;
import com.example.barry215.woo.Network.RetrofitManager;
import com.example.barry215.woo.R;
import com.example.barry215.woo.Utils.DateUtil;
import com.example.barry215.woo.ui.FullyLinearLayoutManager;
import com.example.barry215.woo.ui.GlideCircleTransform;
import com.example.barry215.woo.ui.MyScrollview;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by barry215 on 2016/4/11.
 */
public class HelpDetailActivity extends BaseSwipeBackActivity {

    @Bind(R.id.toolbar_helper) Toolbar toolbar;
    @Bind(R.id.recycler_view_com) RecyclerView recyclerView;
    @Bind(R.id.fab_help) com.melnykov.fab.FloatingActionButton fab;
    @Bind(R.id.myScrollview) MyScrollview srcollView;
    @Bind(R.id.ptr_com) PullToRefreshView mPtrNewsList;
    @Bind(R.id.detail_head) ImageView detail_head;
    @Bind(R.id.detail_name) TextView detail_name;
    @Bind(R.id.detail_school) TextView detail_school;
    @Bind(R.id.detail_helper) TextView detail_helper;
    @Bind(R.id.detail_time) TextView detail_time;
    @Bind(R.id.detail_state) TextView detail_state;
    @Bind(R.id.detail_body) TextView detail_body;
    @Bind(R.id.detail_iv_1) ImageView detail_iv_1;
    @Bind(R.id.detail_iv_2) ImageView detail_iv_2;
    @Bind(R.id.detail_iv_3) ImageView detail_iv_3;
    @Bind(R.id.detail_deadTime) TextView detail_deadTime;
    @Bind(R.id.detail_reward) TextView detail_reward;
    @Bind(R.id.detail_comment) ImageView detail_comment;
    @Bind(R.id.detail_sex) ImageView detail_sex;
    @Bind(R.id.detail_state_color) RelativeLayout detail_state_color;


    private static final int REFRESH_DELAY = 500;
    private String HELP_ID;
    private String PLAY_ID;
    private String HELP_USER_ID;
    private CommentAdapter adapter;
    private boolean isFirst = true;
    private List<RemarkInfoBean> remarkInfoBeanList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_helpdetail;
    }

    @Override
    protected void afterCreate() {
        initToolbar();
        initViewPager();
        initClick();
        initPageListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirst){
            updateView();
        }
        isFirst = false;
    }

    private void initPageListener() {
        mPtrNewsList.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPtrNewsList.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateView();
                    }
                }, REFRESH_DELAY);
            }
        });
    }

    private void initClick() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpDetailActivity.this,HelperListActivity.class);
                intent.putExtra("helpId",HELP_ID);
                startActivity(intent);
            }
        });

        fab.attachToScrollView(srcollView);

        detail_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpDetailActivity.this, CommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("help_id", HELP_ID);
                bundle.putString("commented_username", detail_name.getText().toString());
                bundle.putString("parentId", "0");
                intent.putExtra("bundle", bundle);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void initView(){

        RetrofitManager.builder().getHelpInfo(HELP_ID,PLAY_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<HelpInfoBean>() {
                    @Override
                    public void call(HelpInfoBean helpInfoBean) {
                        HELP_USER_ID = helpInfoBean.getUserid();
                        detail_time.setText(DateUtil.parseTime(helpInfoBean.getCreatetime()));
                        detail_name.setText(helpInfoBean.getUsername());
                        detail_school.setText(helpInfoBean.getUniversity());
                        if (helpInfoBean.getState().equals("0")) {
                            detail_state.setText("未解决");
                            detail_state_color.setBackgroundColor(getResources().getColor(R.color.RED));
                        } else if (helpInfoBean.getState().equals("1")) {
                            detail_state.setText("正在解决");
                            detail_state_color.setBackgroundColor(getResources().getColor(R.color.YELLOW));
                        } else {
                            detail_state_color.setBackgroundColor(getResources().getColor(R.color.GREEN));
                            detail_state.setText("已解决");
                        }
                        detail_body.setText(helpInfoBean.getContent());
                        detail_helper.setText(helpInfoBean.getHelpnames());
                        detail_deadTime.setText(DateUtil.lastDate(helpInfoBean.getEndtime()));
                        detail_reward.setText(helpInfoBean.getReward() + "元");
                        if (helpInfoBean.getSex().equals("1")) {
                            detail_sex.setImageDrawable(ContextCompat.getDrawable(HelpDetailActivity.this, R.drawable.item_female));
                        } else {
                            detail_sex.setImageDrawable(ContextCompat.getDrawable(HelpDetailActivity.this, R.drawable.item_male));
                        }

                        if (TextUtils.isEmpty(helpInfoBean.getPic1())){
                            detail_iv_1.setVisibility(View.GONE);
                            detail_iv_2.setVisibility(View.GONE);
                            detail_iv_3.setVisibility(View.GONE);
                        }else if (helpInfoBean.getPic1().equals("000")) {
                            detail_iv_1.setVisibility(View.GONE);
                            detail_iv_2.setVisibility(View.GONE);
                            detail_iv_3.setVisibility(View.GONE);
                        } else if (!helpInfoBean.getPic1().equals("000") && helpInfoBean.getPic2().equals("000")) {
                            Glide.with(HelpDetailActivity.this).load(App.LOADIMGADDRESS + helpInfoBean.getPic1()).placeholder(R.drawable.sample_pic).into(detail_iv_1);
                            detail_iv_1.setVisibility(View.VISIBLE);
                            detail_iv_2.setVisibility(View.GONE);
                            detail_iv_3.setVisibility(View.GONE);
                        } else if (!helpInfoBean.getPic1().equals("000")
                                && !helpInfoBean.getPic2().equals("000")
                                && helpInfoBean.getPic3().equals("000")) {
                            Glide.with(HelpDetailActivity.this).load(App.LOADIMGADDRESS + helpInfoBean.getPic1()).placeholder(R.drawable.sample_pic).into(detail_iv_1);
                            Glide.with(HelpDetailActivity.this).load(App.LOADIMGADDRESS + helpInfoBean.getPic2()).placeholder(R.drawable.sample_pic).into(detail_iv_2);
                            detail_iv_1.setVisibility(View.VISIBLE);
                            detail_iv_2.setVisibility(View.VISIBLE);
                            detail_iv_3.setVisibility(View.GONE);
                        } else if (!helpInfoBean.getPic1().equals("000")
                                && !helpInfoBean.getPic2().equals("000")
                                && !helpInfoBean.getPic3().equals("000")) {
                            Glide.with(HelpDetailActivity.this).load(App.LOADIMGADDRESS + helpInfoBean.getPic1()).placeholder(R.drawable.sample_pic).into(detail_iv_1);
                            Glide.with(HelpDetailActivity.this).load(App.LOADIMGADDRESS + helpInfoBean.getPic2()).placeholder(R.drawable.sample_pic).into(detail_iv_2);
                            Glide.with(HelpDetailActivity.this).load(App.LOADIMGADDRESS + helpInfoBean.getPic3()).placeholder(R.drawable.sample_pic).into(detail_iv_3);
                            detail_iv_1.setVisibility(View.VISIBLE);
                            detail_iv_2.setVisibility(View.VISIBLE);
                            detail_iv_3.setVisibility(View.VISIBLE);
                        }
                        Glide.with(HelpDetailActivity.this).load(App.LOADHEADADDRESS + helpInfoBean.getUserhead())
                                .transform(new GlideCircleTransform(HelpDetailActivity.this))
                                .placeholder(R.drawable.person_default_icon).into(detail_head);

                        remarkInfoBeanList = helpInfoBean.getRemark();
                        adapter.updateComment(getNewCommentList());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(HelpDetailActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void updateView(){

        RetrofitManager.builder().getHelpInfo(HELP_ID,PLAY_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<HelpInfoBean>() {
                    @Override
                    public void call(HelpInfoBean helpInfoBean) {
                        HELP_USER_ID = helpInfoBean.getUserid();
                        detail_time.setText(DateUtil.parseTime(helpInfoBean.getCreatetime()));
                        detail_name.setText(helpInfoBean.getUsername());
                        detail_school.setText(helpInfoBean.getUniversity());
                        if (helpInfoBean.getState().equals("0")) {
                            detail_state.setText("未解决");
                            detail_state_color.setBackgroundColor(getResources().getColor(R.color.RED));
                        } else if (helpInfoBean.getState().equals("1")) {
                            detail_state.setText("正在解决");
                            detail_state_color.setBackgroundColor(getResources().getColor(R.color.YELLOW));
                        } else {
                            detail_state_color.setBackgroundColor(getResources().getColor(R.color.GREEN));
                            detail_state.setText("已解决");
                        }
                        detail_body.setText(helpInfoBean.getContent());
                        detail_helper.setText(helpInfoBean.getHelpnames());
                        detail_deadTime.setText(DateUtil.lastDate(helpInfoBean.getEndtime()));
                        detail_reward.setText(helpInfoBean.getReward() + "元");
                        if(helpInfoBean.getSex().equals("1")){
                            detail_sex.setImageDrawable(ContextCompat.getDrawable(HelpDetailActivity.this, R.drawable.item_female));
                        }else {
                            detail_sex.setImageDrawable(ContextCompat.getDrawable(HelpDetailActivity.this, R.drawable.item_male));
                        }

                        if (TextUtils.isEmpty(helpInfoBean.getPic1())){
                            detail_iv_1.setVisibility(View.GONE);
                            detail_iv_2.setVisibility(View.GONE);
                            detail_iv_3.setVisibility(View.GONE);
                        }else if (helpInfoBean.getPic1().equals("000")){
                            detail_iv_1.setVisibility(View.GONE);
                            detail_iv_2.setVisibility(View.GONE);
                            detail_iv_3.setVisibility(View.GONE);
                        }else if (!helpInfoBean.getPic1().equals("000") && helpInfoBean.getPic2().equals("000")){
                            Glide.with(HelpDetailActivity.this).load(App.LOADIMGADDRESS + helpInfoBean.getPic1()).placeholder(R.drawable.sample_pic).into(detail_iv_1);
                            detail_iv_1.setVisibility(View.VISIBLE);
                            detail_iv_2.setVisibility(View.GONE);
                            detail_iv_3.setVisibility(View.GONE);
                        }else if (!helpInfoBean.getPic1().equals("000")
                                && !helpInfoBean.getPic2().equals("000")
                                && helpInfoBean.getPic3().equals("000")){
                            Glide.with(HelpDetailActivity.this).load(App.LOADIMGADDRESS + helpInfoBean.getPic1()).placeholder(R.drawable.sample_pic).into(detail_iv_1);
                            Glide.with(HelpDetailActivity.this).load(App.LOADIMGADDRESS + helpInfoBean.getPic2()).placeholder(R.drawable.sample_pic).into(detail_iv_2);
                            detail_iv_1.setVisibility(View.VISIBLE);
                            detail_iv_2.setVisibility(View.VISIBLE);
                            detail_iv_3.setVisibility(View.GONE);
                        }else if (!helpInfoBean.getPic1().equals("000")
                                && !helpInfoBean.getPic2().equals("000")
                                && !helpInfoBean.getPic3().equals("000")){
                            Glide.with(HelpDetailActivity.this).load(App.LOADIMGADDRESS + helpInfoBean.getPic1()).placeholder(R.drawable.sample_pic).into(detail_iv_1);
                            Glide.with(HelpDetailActivity.this).load(App.LOADIMGADDRESS + helpInfoBean.getPic2()).placeholder(R.drawable.sample_pic).into(detail_iv_2);
                            Glide.with(HelpDetailActivity.this).load(App.LOADIMGADDRESS + helpInfoBean.getPic3()).placeholder(R.drawable.sample_pic).into(detail_iv_3);
                            detail_iv_1.setVisibility(View.VISIBLE);
                            detail_iv_2.setVisibility(View.VISIBLE);
                            detail_iv_3.setVisibility(View.VISIBLE);
                        }
                        Glide.with(HelpDetailActivity.this).load(App.LOADHEADADDRESS + helpInfoBean.getUserhead())
                                .transform(new GlideCircleTransform(HelpDetailActivity.this))
                                .placeholder(R.drawable.person_default_icon).into(detail_head);

                        remarkInfoBeanList = helpInfoBean.getRemark();
                        adapter.updateComment(getNewCommentList());
                        mPtrNewsList.setRefreshing(false);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(HelpDetailActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private List<RemarkInfoBean> getNewCommentList(){
        List<RemarkInfoBean> remarkInfoBeans = new ArrayList<>();

        for(int i = 0; i < remarkInfoBeanList.size(); i++){
            RemarkInfoBean remarkInfoBean_1 = remarkInfoBeanList.get(i);

            if(remarkInfoBean_1.getReplyid().equals("0")){
                remarkInfoBeans.add(remarkInfoBean_1);

                for(int j = i+1;j<remarkInfoBeanList.size();j++){
                    RemarkInfoBean remarkInfoBean_2 = remarkInfoBeanList.get(j);

                    if(!remarkInfoBean_2.getReplyid().equals("0")
                            && remarkInfoBean_2.getReplyid().equals(remarkInfoBean_1.getId())){
                        remarkInfoBeans.add(remarkInfoBean_2);
                    }

                }
            }
        }
        return remarkInfoBeans;
    }

    private void initViewPager() {
        HELP_ID = getIntent().getStringExtra("helpId");
        PLAY_ID = App.getInstance().getPlayId();
        recyclerView.setLayoutManager(new FullyLinearLayoutManager(recyclerView.getContext()));
        adapter = new CommentAdapter(this,HELP_ID);
        recyclerView.setAdapter(adapter);
        srcollView.smoothScrollTo(0, 20);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_helper, menu);
        return true;
    }

    private void initToolbar(){
        if (toolbar == null) {
            toolbar = (Toolbar) findViewById(R.id.toolbar_newsList);
        }else {
            setTitle("");
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.helper_menu){
                    if (!detail_state.getText().toString().equals("正在解决")){
                        Intent intent = new Intent(HelpDetailActivity.this,HelpEditActivity.class);
                        intent.putExtra("helpId",HELP_ID);
                        startActivity(intent);
                    }else {
                        Toast.makeText(HelpDetailActivity.this,"帮助时不能修改",Toast.LENGTH_SHORT).show();
                    }

                }
                return true;
            }
        });
    }

}
