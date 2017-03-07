package com.example.barry215.woo.Main.Activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.barry215.woo.Base.BaseSwipeBackActivity;
import com.example.barry215.woo.R;

import butterknife.Bind;

/**
 * Created by barry215 on 2016/4/26.
 */
public class ChangePwdActivity extends BaseSwipeBackActivity {
    @Bind(R.id.toolbar_changePwd)
    Toolbar toolbar;
    @Bind(R.id.btn_change)
    Button btn_change;

    @Override
    public int getLayoutId() {
        return R.layout.activity_findpwd;
    }

    @Override
    protected void afterCreate() {
        initToolbar();
        initClick();
    }

    private void initClick() {
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePwdActivity.this,LoginActivity.class);
                startActivity(intent);
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
}
