package com.example.barry215.woo.Main.Activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.example.barry215.woo.Base.BaseSwipeBackActivity;
import com.example.barry215.woo.R;

import butterknife.Bind;

/**
 * Created by barry215 on 2016/5/28.
 */
public class ModifyAliPayActivity extends BaseSwipeBackActivity {
    @Bind(R.id.toolbar_AliPay)
    Toolbar toolbar;
    @Bind(R.id.AliPayWrapper)
    TextInputLayout AliPayWrapper;
    @Bind(R.id.ed_AliPay)
    EditText ed_AliPay;

    private String str_name;

    @Override
    public int getLayoutId() {
        return R.layout.activity_modify_alipay;
    }

    @Override
    protected void afterCreate() {
        initToolbar();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        setTitle("");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModifyAliPayActivity.this,UserInfoActivity.class);
                intent.putExtra("AliPay",ed_AliPay.getText().toString());
                setResult(9,intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_changed, menu);
        return true;
    }
}
