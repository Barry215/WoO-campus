package com.example.barry215.woo.Main.Application;

import android.app.Application;
import android.content.Context;

import com.example.barry215.greendao.LoginCache;
import com.example.barry215.greendao.LoginCacheDao;
import com.example.barry215.woo.DB.DaoHelper;
import com.example.barry215.woo.Network.RetrofitManager;

import java.util.List;

/**
 * Created by barry215 on 2016/4/26.
 */
public class App extends Application {

    private static App instance;
    private boolean isLogin;
    private String playId;
    private String userId;
    private boolean pullInfo;
    private static Context mApplicationContext;

    public static final String LOADIMGADDRESS = RetrofitManager.BASE_URL + "UserData/HelpImg/";
    public static final String LOADHEADADDRESS = RetrofitManager.BASE_URL + "UserData/UserHead/";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mApplicationContext = this;
        setData();
    }

    public static Context getContext() {
        return mApplicationContext;
    }

    public static App getInstance() {
        return instance;
    }

    private void setData() {

        LoginCacheDao loginCacheDao = DaoHelper.getDaoSession(this).getLoginCacheDao();
        List<LoginCache> loginCacheList = loginCacheDao.queryBuilder().where(LoginCacheDao.Properties.Id.eq((long) 1)).list();
        if (loginCacheList.size() == 0){
            LoginCache loginCache = new LoginCache();
            loginCache.setIsLogin(false);
            loginCache.setUserId("");
            loginCache.setPlayId("");
            loginCacheDao.insert(loginCache);
        }else {
            LoginCache loginCache = loginCacheDao.load((long) 1);
            isLogin = loginCache.getIsLogin();
            userId = loginCache.getUserId();
            playId = loginCache.getPlayId();
        }
    }

    public boolean getIsLogin() {
        return isLogin;
    }

    public String getPlayId() {
        return playId;
    }

    public String getUserId() {
        return userId;
    }

    public boolean getPullInfo() {
        return pullInfo;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public void setPlayId(String playId) {
        this.playId = playId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPullInfo(boolean pullInfo) {
        this.pullInfo = pullInfo;
    }
    //是否完善了资料
}
