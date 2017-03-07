package com.example.barry215.woo.Main.Activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.barry215.greendao.LoginCache;
import com.example.barry215.greendao.LoginCacheDao;
import com.example.barry215.woo.Base.BaseSwipeBackActivity;
import com.example.barry215.woo.Bean.CommonBean;
import com.example.barry215.woo.DB.DaoHelper;
import com.example.barry215.woo.Main.Application.App;
import com.example.barry215.woo.Main.Fragment.SettingFragment;
import com.example.barry215.woo.Network.RetrofitManager;
import com.example.barry215.woo.R;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by barry215 on 2016/4/15.
 */
public class SettingActivity extends BaseSwipeBackActivity {

    @Bind(R.id.toolbar_setting) Toolbar toolbar;
    @Bind(R.id.btn_quit) Button btn_quit;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void afterCreate() {
        initToolbar();
        initViewPager();
        initClick();
    }

    private void initClick() {
        if (App.getInstance().getIsLogin()){
            btn_quit.setVisibility(View.VISIBLE);
            btn_quit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginCacheDao loginCacheDao = DaoHelper.getDaoSession(SettingActivity.this).getLoginCacheDao();
                    LoginCache loginCache = loginCacheDao.load((long) 1);
                    loginCache.setIsLogin(false);
                    loginCache.setPlayId("");
                    loginCache.setUserId("");
                    loginCacheDao.update(loginCache);

//                    DaoHelper.getDaoSession(SettingActivity.this).getUserCacheDao().deleteAll();

                    App.getInstance().setIsLogin(false);

                    RetrofitManager.builder().exit(App.getInstance().getPlayId())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<CommonBean>() {
                                @Override
                                public void call(CommonBean commonBean) {
                                    App.getInstance().setPlayId("");
                                    App.getInstance().setUserId("");
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    App.getInstance().setPlayId("");
                                    App.getInstance().setUserId("");
                                }
                            });

                    Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }else {
            btn_quit.setVisibility(View.GONE);
        }
    }

    private void initViewPager() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new SettingFragment())
                .commit();
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
