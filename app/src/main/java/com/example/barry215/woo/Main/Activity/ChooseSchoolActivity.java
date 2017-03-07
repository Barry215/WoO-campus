package com.example.barry215.woo.Main.Activity;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
public class ChooseSchoolActivity extends BaseSwipeBackActivity {
    @Bind(R.id.toolbar_userSchool) Toolbar toolbar;
    @Bind(R.id.radio_one) RadioButton radio_one;
    @Bind(R.id.radio_two) RadioButton radio_two;
    @Bind(R.id.radio_group) RadioGroup radio_group;

    private String university;

    @Override
    public int getLayoutId() {
        return R.layout.activity_chooseschool;
    }

    @Override
    protected void afterCreate() {
        initToolbar();
        initClick();
    }

    private void initClick() {
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_one:
                        university = "杭州电子科技大学";
                        break;
                    case R.id.radio_two:
                        university = "浙江理工大学";
                        break;
                }
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

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.changed_menu) {
                    RetrofitManager.builder().modifyUniversity(App.getInstance().getPlayId()
                            ,university)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<CommonBean>() {
                                @Override
                                public void call(CommonBean commonBean) {
                                    switch (commonBean.getRes()) {
                                        case "0":
                                            Toast.makeText(ChooseSchoolActivity.this,"修改成功", Toast.LENGTH_SHORT).show();
                                            finish();
                                            break;
                                        case "1":
                                            Toast.makeText(ChooseSchoolActivity.this,"输入不合法", Toast.LENGTH_SHORT).show();
                                            break;
                                        case "-1":
                                            Toast.makeText(ChooseSchoolActivity.this,"内部错误", Toast.LENGTH_SHORT).show();
                                            break;
                                        case "2":
                                            Toast.makeText(ChooseSchoolActivity.this,"用户冲突", Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    Toast.makeText(ChooseSchoolActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
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
