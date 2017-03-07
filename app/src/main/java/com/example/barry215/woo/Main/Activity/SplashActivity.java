package com.example.barry215.woo.Main.Activity;

import android.content.Intent;
import android.os.Handler;

import com.example.barry215.woo.Base.BaseActivity;
import com.example.barry215.woo.R;

/**
 * Created by barry215 on 2016/4/15.
 */
public class SplashActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void afterCreate() {
        init();
    }

    private void init() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                finish();
            }
        }, 1500);
    }
}
