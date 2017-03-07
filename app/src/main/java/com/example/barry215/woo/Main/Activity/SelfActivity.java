package com.example.barry215.woo.Main.Activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.barry215.woo.Base.BaseSwipeBackActivity;
import com.example.barry215.woo.Bean.UserGradesBean;
import com.example.barry215.woo.Bean.UserInfoBean;
import com.example.barry215.woo.Main.Application.App;
import com.example.barry215.woo.Network.RetrofitManager;
import com.example.barry215.woo.R;
import com.example.barry215.woo.ui.GlideCircleTransform;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by barry215 on 2016/4/22.
 */
public class SelfActivity extends BaseSwipeBackActivity {

    @Bind(R.id.iv_self_back) ImageView iv_self_back;
    @Bind(R.id.iv_self_edit) ImageView iv_self_edit;
    @Bind(R.id.iv_self_head) ImageView iv_self_head;
    @Bind(R.id.tv_self_school) TextView tv_self_school;
    @Bind(R.id.tv_self_college) TextView tv_self_college;
    @Bind(R.id.tv_self_watch) TextView tv_self_watch;
    @Bind(R.id.tv_self_follower) TextView tv_self_follower;
    @Bind(R.id.tv_self_thanks) TextView tv_self_thanks;
    @Bind(R.id.tv_self_issue) TextView tv_self_issue;
    @Bind(R.id.tv_self_helped) TextView tv_self_helped;
    @Bind(R.id.tv_self_name) TextView tv_self_name;
    @Bind(R.id.iv_self_sex) ImageView iv_self_sex;

    @Override
    public int getLayoutId() {
        return R.layout.activity_self;
    }

    @Override
    protected void afterCreate() {
        initClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPageView();
    }

    private void initPageView() {
        RetrofitManager.builder().getUserInfo(App.getInstance().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserInfoBean>() {
                    @Override
                    public void call(UserInfoBean userInfoBean) {
                        if (userInfoBean.getSex().equals("0")) {
                            iv_self_sex.setImageDrawable(getResources().getDrawable(R.drawable.sex_male));
                            iv_self_sex.setVisibility(View.VISIBLE);
                        } else if (userInfoBean.getSex().equals("1")) {
                            iv_self_sex.setImageDrawable(getResources().getDrawable(R.drawable.sex_female));
                            iv_self_sex.setVisibility(View.VISIBLE);
                        } else if (userInfoBean.getSex().equals("2")) {
                            iv_self_sex.setVisibility(View.GONE);
                        }

                        if (!userInfoBean.getHead().equals("default.png")) {
                            Glide.with(SelfActivity.this).load(App.LOADHEADADDRESS + userInfoBean.getHead())
                                    .transform(new GlideCircleTransform(SelfActivity.this))
                                    .placeholder(R.drawable.person_default_icon).into(iv_self_head);
                        }

                        tv_self_name.setText(userInfoBean.getUsername());
                        tv_self_school.setText(userInfoBean.getUniversity());
                        tv_self_college.setText(userInfoBean.getCollege());

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(SelfActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RetrofitManager.builder().getUserGrades(App.getInstance().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserGradesBean>() {
                    @Override
                    public void call(UserGradesBean userGradesBean) {
                        tv_self_watch.setText(userGradesBean.getFollow());
                        tv_self_follower.setText(userGradesBean.getFans());
                        tv_self_thanks.setText(userGradesBean.getThanks());
                        tv_self_issue.setText(userGradesBean.getHelp());
                        tv_self_helped.setText(userGradesBean.getHelped());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(SelfActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void initClick() {
        iv_self_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_self_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelfActivity.this,UserInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}
