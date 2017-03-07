package com.example.barry215.woo.Main.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.barry215.woo.Base.BaseSwipeBackActivity;
import com.example.barry215.woo.Bean.CommonBean;
import com.example.barry215.woo.Bean.PeopleShipBean;
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
 * Created by barry215 on 2016/4/12.
 */
public class PersonActivity extends BaseSwipeBackActivity {

    @Bind(R.id.iv_person_back) ImageView iv_person_back;
    @Bind(R.id.iv_person_sex) ImageView iv_person_sex;
    @Bind(R.id.action_person_chat) ImageView action_person_chat;
    @Bind(R.id.iv_person_head) ImageView iv_person_head;
    @Bind(R.id.tv_person_name) TextView tv_person_name;
    @Bind(R.id.tv_person_school) TextView tv_person_school;
    @Bind(R.id.tv_person_college) TextView tv_person_college;
    @Bind(R.id.action_person_follow) TextView action_person_follow;
    @Bind(R.id.action_person_thanks) TextView action_person_thanks;
    @Bind(R.id.tv_person_issue) TextView tv_person_issue;
    @Bind(R.id.tv_person_helped) TextView tv_person_helped;
    @Bind(R.id.tv_person_watch) TextView tv_person_watch;
    @Bind(R.id.tv_person_follow) TextView tv_person_follow;
    @Bind(R.id.tv_person_thanks) TextView tv_person_thanks;


    private String userId;
    private boolean isFollow = false;
    private boolean isThanks = false;
    private String receiverHead;

    @Override
    public int getLayoutId() {
        return R.layout.activity_person;
    }

    @Override
    protected void afterCreate() {
        initData();
        initClick();
    }

    private void initClick() {
        iv_person_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        action_person_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitManager.builder().getUserInfo(App.getInstance().getUserId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<UserInfoBean>() {
                            @Override
                            public void call(UserInfoBean userInfoBean) {
                                Intent intent = new Intent(PersonActivity.this, ChatActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("userId", userId);
                                bundle.putString("userName", tv_person_name.getText().toString());
                                bundle.putString("receiverHead", receiverHead);
                                bundle.putString("senderHead",userInfoBean.getHead());
                                intent.putExtra("bundle", bundle);
                                startActivity(intent);
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Toast.makeText(PersonActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        action_person_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFollow) {
                    changeFollowShip();
                    action_person_follow.setText("关注");
                    Toast.makeText(PersonActivity.this, "您已取消关注", Toast.LENGTH_SHORT).show();
                    isFollow = false;
                } else {
                    changeFollowShip();
                    action_person_follow.setText("已关注");
                    Toast.makeText(PersonActivity.this, "您已关注", Toast.LENGTH_SHORT).show();
                    isFollow = true;
                }
            }
        });

        action_person_thanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isThanks) {
                    changeThankShip();
                    action_person_thanks.setText("感谢");
                    Toast.makeText(PersonActivity.this, "您已取消感谢", Toast.LENGTH_SHORT).show();
                    isThanks = false;
                } else {
                    changeThankShip();
                    action_person_thanks.setText("已感谢");
                    Toast.makeText(PersonActivity.this, "您已感谢", Toast.LENGTH_SHORT).show();
                    isThanks = true;
                }
            }
        });


    }

    private void changeFollowShip(){
        RetrofitManager.builder().changeFollowShip(userId,App.getInstance().getPlayId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CommonBean>() {
                    @Override
                    public void call(CommonBean commonBean) {
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(PersonActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void changeThankShip(){
        RetrofitManager.builder().changeThankShip(userId, App.getInstance().getPlayId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CommonBean>() {
                    @Override
                    public void call(CommonBean commonBean) {
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(PersonActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void initData() {
        userId = getIntent().getStringExtra("userId");

        RetrofitManager.builder().getUserInfo(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserInfoBean>() {
                    @Override
                    public void call(UserInfoBean userInfoBean) {
                        if (userInfoBean.getSex().equals("0")) {
                            iv_person_sex.setImageDrawable(getResources().getDrawable(R.drawable.sex_male));
                            iv_person_sex.setVisibility(View.VISIBLE);
                        } else if (userInfoBean.getSex().equals("1")){
                            iv_person_sex.setImageDrawable(getResources().getDrawable(R.drawable.sex_female));
                            iv_person_sex.setVisibility(View.VISIBLE);
                        }else {
                            iv_person_sex.setVisibility(View.GONE);
                        }

                        receiverHead = userInfoBean.getHead();

                        Glide.with(PersonActivity.this).load(App.LOADHEADADDRESS + userInfoBean.getHead())
                                .transform(new GlideCircleTransform(PersonActivity.this))
                                .placeholder(R.drawable.person_default_icon).into(iv_person_head);

                        tv_person_school.setText(userInfoBean.getUniversity());

                        tv_person_college.setText(userInfoBean.getCollege());

                        tv_person_name.setText(userInfoBean.getUsername());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(PersonActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RetrofitManager.builder().getUserGrades(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserGradesBean>() {
                    @Override
                    public void call(UserGradesBean userGradesBean) {
                        tv_person_watch.setText(userGradesBean.getFollow());
                        tv_person_follow.setText(userGradesBean.getFans());
                        tv_person_thanks.setText(userGradesBean.getThanks());
                        tv_person_issue.setText(userGradesBean.getHelp());
                        tv_person_helped.setText(userGradesBean.getHelped());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(PersonActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RetrofitManager.builder().getPeopleShip(App.getInstance().getUserId(), userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<PeopleShipBean>() {
                    @Override
                    public void call(PeopleShipBean peopleShipBean) {
                        if (peopleShipBean.getFollow().equals("0")){
                            action_person_follow.setText("关注");
                            isFollow = false;
                        }else {
                            action_person_follow.setText("已关注");
                            isFollow = true;
                        }
                        if (peopleShipBean.getThanks().equals("0")){
                            action_person_thanks.setText("感谢");
                            isThanks = false;
                        }else {
                            action_person_thanks.setText("已感谢");
                            isThanks = true;
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(PersonActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

}
