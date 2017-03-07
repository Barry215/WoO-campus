package com.example.barry215.woo.Main.Activity;

import android.content.Intent;
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
 * Created by barry215 on 2016/5/27.
 */
public class ModifyNameActivity extends BaseSwipeBackActivity {
    @Bind(R.id.toolbar_userName) Toolbar toolbar;
    @Bind(R.id.userNameWrapper) TextInputLayout userNameWrapper;
    @Bind(R.id.ed_username) EditText ed_username;

    private String str_name;

    @Override
    public int getLayoutId() {
        return R.layout.activity_modifyname;
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
                Intent intent = new Intent(ModifyNameActivity.this,UserInfoActivity.class);
                intent.putExtra("name",ed_username.getText().toString());
                setResult(3, intent);
                finish();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.changed_menu){
                    String userId = App.getInstance().getPlayId();
                    str_name  = ed_username.getText().toString();
                    RetrofitManager.builder().modifyUserName(userId,str_name)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<CommonBean>() {
                                @Override
                                public void call(CommonBean commonBean) {
                                    switch (commonBean.getRes()) {
                                        case "0":
                                            Toast.makeText(ModifyNameActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                                            userNameWrapper.setErrorEnabled(false);
                                            finish();
                                            break;
                                        case "1":
                                            userNameWrapper.setErrorEnabled(true);
                                            userNameWrapper.setError("此姓名已存在！");
                                            break;
                                        case "-1":
                                            userNameWrapper.setErrorEnabled(true);
                                            userNameWrapper.setError("输入格式有误！");
                                            break;
                                    }
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    Toast.makeText(ModifyNameActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
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
