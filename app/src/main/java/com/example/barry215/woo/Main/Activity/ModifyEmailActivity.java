package com.example.barry215.woo.Main.Activity;

import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.barry215.woo.Base.BaseSwipeBackActivity;
import com.example.barry215.woo.Bean.CommonBean;
import com.example.barry215.woo.Main.Application.App;
import com.example.barry215.woo.Network.RetrofitManager;
import com.example.barry215.woo.R;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by barry215 on 2016/5/28.
 */
public class ModifyEmailActivity extends BaseSwipeBackActivity {
    @Bind(R.id.toolbar_Email)
    Toolbar toolbar;
    @Bind(R.id.EmailWrapper)
    TextInputLayout EmailWrapper;
    @Bind(R.id.ed_Email)
    EditText ed_Email;

    @Override
    public int getLayoutId() {
        return R.layout.activity_modify_email;
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
                finish();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.changed_menu) {
                    RetrofitManager.builder().modifyEmail(App.getInstance().getPlayId()
                            , ed_Email.getText().toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<CommonBean>() {
                                @Override
                                public void call(CommonBean commonBean) {
                                    switch (commonBean.getRes()) {
                                        case "0":
                                            Toast.makeText(ModifyEmailActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                            finish();
                                            break;
                                        case "1":
                                            Toast.makeText(ModifyEmailActivity.this, "输入不合法", Toast.LENGTH_SHORT).show();
                                            break;
                                        case "-1":
                                            Toast.makeText(ModifyEmailActivity.this, "内部错误", Toast.LENGTH_SHORT).show();
                                            break;
                                        case "2":
                                            Toast.makeText(ModifyEmailActivity.this, "用户冲突", Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    Toast.makeText(ModifyEmailActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                                }
                            });
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_changed, menu);
        return true;
    }
}
