package com.example.barry215.woo.Main.Activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.barry215.woo.Adapter.HelperListAdapter;
import com.example.barry215.woo.Base.BaseSwipeBackActivity;
import com.example.barry215.woo.Bean.CommonBean;
import com.example.barry215.woo.Bean.HelpInfoBean;
import com.example.barry215.woo.Bean.UserInfoBean;
import com.example.barry215.woo.Bean.UserListBean;
import com.example.barry215.woo.Main.Application.App;
import com.example.barry215.woo.Main.Fragment.ChooseFragment;
import com.example.barry215.woo.Main.Fragment.CleanChooserFragment;
import com.example.barry215.woo.Main.Fragment.ComplainFragment;
import com.example.barry215.woo.Main.Fragment.FinishFragment;
import com.example.barry215.woo.Main.Fragment.RatingFragment;
import com.example.barry215.woo.Network.RetrofitManager;
import com.example.barry215.woo.R;
import com.example.barry215.woo.ui.DividerLine;
import com.example.barry215.woo.ui.GlideCircleTransform;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by barry215 on 2016/5/2.
 */
public class HelperListActivity extends BaseSwipeBackActivity {
    @Bind(R.id.toolbar_helper) Toolbar toolbar;
    @Bind(R.id.recycler_view_helper) RecyclerView recyclerView;
    @Bind(R.id.iv_choose_helper) ImageView iv_choose_helper;
    @Bind(R.id.tv_choose_name) TextView tv_choose_name;
    @Bind(R.id.tv_choose_info) TextView tv_choose_info;
    @Bind(R.id.btns) LinearLayout btns;
    @Bind(R.id.btn_change_person) Button btn_change_person;
    @Bind(R.id.btn_finish) Button btn_finish;

    private HelperListAdapter adapter;
    private String helpId;
    private Boolean isChoose;
    private Boolean isFinish;
    private String holderId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_helplist;
    }

    @Override
    protected void afterCreate() {
        initToolbar();
        initViewPager();
        initClick();
    }

    private void initClick() {
        btn_change_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFinish) {
                    CleanChooserFragment dialog = new CleanChooserFragment();
                    dialog.setChooserId(holderId);
                    dialog.setHelpId(helpId);
                    dialog.show(getSupportFragmentManager(), "dialog");
                } else {
                    ComplainFragment dialog = new ComplainFragment();
                    dialog.setChooserId(holderId);
                    dialog.setHelpId(helpId);
                    dialog.show(getSupportFragmentManager(), "dialog");
                }
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFinish) {
                    FinishFragment dialog = new FinishFragment();
                    dialog.setChooserId(holderId);
                    dialog.setHelpId(helpId);
                    dialog.show(getSupportFragmentManager(), "dialog");
                } else {
                    RatingFragment dialog = new RatingFragment();
                    dialog.setHelpId(helpId);
                    dialog.show(getSupportFragmentManager(), "dialog");
                }
            }
        });

        adapter.setOnItemChooseListener(new HelperListAdapter.OnItemChooseListener() {
            @Override
            public void onItemChooseClick(View view, String userId) {
                if (!isChoose) {
                    ChooseFragment dialog = new ChooseFragment();
                    dialog.setChooserId(userId);
                    dialog.setHelpId(helpId);
                    dialog.show(getSupportFragmentManager(), "dialog");
                }
            }
        });
    }

    private void updateHelperList() {
        RetrofitManager.builder().getHelpPeopleList(helpId,"100","1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserListBean>() {
                    @Override
                    public void call(UserListBean userListBean) {
                        adapter.updateData(userListBean.getRes());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(HelperListActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void initViewPager() {
        helpId = getIntent().getStringExtra("helpId");

        initChooserView();
        adapter = new HelperListAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
        DividerLine dividerLine = new DividerLine(DividerLine.VERTICAL);
        dividerLine.setSize(1);
        dividerLine.setColor(getResources().getColor(R.color.SUB_GRAY));
        recyclerView.addItemDecoration(dividerLine);

        updateHelperList();
    }

    private void initChooseUser(){
        RetrofitManager.builder().getChooseUser(helpId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserInfoBean>() {
                    @Override
                    public void call(UserInfoBean userInfoBean) {
                        Glide.with(HelperListActivity.this).load(App.LOADHEADADDRESS + userInfoBean.getHead())
                                .transform(new GlideCircleTransform(HelperListActivity.this))
                                .placeholder(R.drawable.person_default_icon).into(iv_choose_helper);

//                      Picasso.with(HelperListActivity.this).load(App.LOADHEADADDRESS + userInfoBean.getHead())
//                              .transform(new PicassoCircleTransform())
//                              .placeholder(R.drawable.person_default_icon).into(iv_choose_helper);
                        tv_choose_name.setText(userInfoBean.getUsername());
                        tv_choose_info.setText(userInfoBean.getUsersign());
                        holderId = userInfoBean.getId();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(HelperListActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void initChooserView(){
        RetrofitManager.builder().getHelpInfo(helpId, App.getInstance().getPlayId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<HelpInfoBean>() {
                    @Override
                    public void call(HelpInfoBean helpInfoBean) {
                        if (helpInfoBean.getState().equals("1")){
                            isChoose = true;
                            isFinish = false;
                            btns.setVisibility(View.VISIBLE);
                            btn_change_person.setText("换人");
                            btn_finish.setText("完成");
                            initChooseUser();

                        }else if (helpInfoBean.getState().equals("2")){
                            isChoose = false;
                            isFinish = true;
                            btns.setVisibility(View.VISIBLE);
                            btn_change_person.setText("投诉");
                            btn_finish.setText("评价");
                            initChooseUser();
                        }else if (helpInfoBean.getState().equals("0")){
                            isChoose = false;
                            isFinish = false;
                            btns.setVisibility(View.GONE);
                            holderId = "";
                            iv_choose_helper.setImageDrawable(getResources().getDrawable(R.drawable.person_default_icon));
                            tv_choose_name.setText("帮手");
                            tv_choose_info.setText("还没有人在帮您");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(HelperListActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });
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

    public void updateChooser(String chooseId){
        updateHelperList();
        isChoose = true;
        isFinish = false;
        btns.setVisibility(View.VISIBLE);
        btn_change_person.setText("换人");
        btn_finish.setText("完成");
        initChooseUser();

        RetrofitManager.builder().sendMessage(helpId,App.getInstance().getPlayId(),chooseId,"您已被选为帮助者")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CommonBean>() {
                    @Override
                    public void call(CommonBean commonBean) {
                        if (commonBean.getRes().equals("0")) {
                            Toast.makeText(HelperListActivity.this, "已通知被选择者！", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(HelperListActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void cleanChooser(){
        iv_choose_helper.setImageDrawable(getResources().getDrawable(R.drawable.person_default_icon));
        tv_choose_name.setText("帮手");
        tv_choose_info.setText("还没有人在帮您");
        btns.setVisibility(View.GONE);
        holderId = "";
        updateHelperList();
        isChoose = false;
    }

    public void helpFinish(String chooseId){
        btn_change_person.setText("投诉");
        btn_finish.setText("评价");
        isFinish = true;

        RetrofitManager.builder().sendMessage(helpId,App.getInstance().getPlayId(),chooseId,"此帮助已解决，感谢您的奉献！")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CommonBean>() {
                    @Override
                    public void call(CommonBean commonBean) {
                        if (commonBean.getRes().equals("0")) {
                            Toast.makeText(HelperListActivity.this, "已通知帮助者！", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(HelperListActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void rating(){
        Toast.makeText(HelperListActivity.this, "评分成功！", Toast.LENGTH_SHORT).show();
    }

    public void complain(String complaint){
        if (!TextUtils.isEmpty(complaint)){
            Toast.makeText(HelperListActivity.this, "投诉成功！", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(HelperListActivity.this, "投诉内容不能为空！", Toast.LENGTH_SHORT).show();
        }
    }
}
