package com.example.barry215.woo.Main.Activity;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.example.barry215.woo.Base.BaseSwipeBackActivity;
import com.example.barry215.woo.Bean.CommonBean;
import com.example.barry215.woo.Bean.HelpInfoBean;
import com.example.barry215.woo.Main.Application.App;
import com.example.barry215.woo.Network.RetrofitManager;
import com.example.barry215.woo.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by barry215 on 2016/5/7.
 */
public class HelpEditActivity extends BaseSwipeBackActivity {
    @Bind(R.id.toolbar_helpEdit) Toolbar toolbar;
    @Bind(R.id.ed_helpBody) MaterialEditText ed_helpBody;
    @Bind(R.id.ed_reward) EditText ed_reward;
    @Bind(R.id.timer) EditText ed_timer;
    @Bind(R.id.iv_add) ImageView iv_add;
    @Bind(R.id.iv_switch) Switch iv_switch;
    @Bind(R.id.tv_remind) TextView tv_remind;
    @Bind(R.id.tv_remind_body) TextView tv_remind_body;

    private TimePickerView pvTime;
    private boolean isCheck;
    private String oldReward;
    private String helpId;
    private boolean loading;

    @Override
    public int getLayoutId() {
        return R.layout.activity_helpedit;
    }

    @Override
    protected void afterCreate() {
        initToolbar();
        initViewPager();
        initPicker();
        initClick();
    }

    private void initPicker() {
        pvTime = new TimePickerView(this, TimePickerView.Type.ALL);
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 1);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                ed_timer.setText(format.format(date));
            }
        });
    }

    private void initClick() {
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newReward = ed_reward.getText().toString();
                try {
                    int reward = Integer.valueOf(newReward) + 1;
                    ed_reward.setText(reward + "");
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Toast.makeText(HelpEditActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        ed_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });

        iv_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!isCheck && isChecked && !loading) {
                    tv_remind.setVisibility(View.VISIBLE);
                    tv_remind_body.setVisibility(View.VISIBLE);
                    tv_remind_body.setText("为了能让加急模块真正的起到作用，加急操作需要另付 5 元！");
                    isCheck = true;
                }
                if (isCheck && !isChecked && !loading) {
                    tv_remind.setVisibility(View.VISIBLE);
                    tv_remind_body.setVisibility(View.VISIBLE);
                    tv_remind_body.setText("加急可以帮助你更快的解决问题，请慎重选择！");
                    isCheck = false;
                }
            }
        });
    }

    private void initViewPager() {
        helpId = getIntent().getStringExtra("helpId");
        loading = true;
        RetrofitManager.builder().getHelpInfo(helpId, App.getInstance().getPlayId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<HelpInfoBean>() {
                    @Override
                    public void call(HelpInfoBean helpInfoBean) {
                        if (helpInfoBean.getType().equals("1")) {
                            iv_switch.setChecked(true);
                            isCheck = true;
                        } else {
                            iv_switch.setChecked(false);
                            isCheck = false;
                        }
                        oldReward = helpInfoBean.getReward();
                        ed_helpBody.setText(helpInfoBean.getContent());
                        ed_reward.setText(helpInfoBean.getReward());
                        ed_timer.setText(helpInfoBean.getEndtime().substring(0,15));
                        loading = false;
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(HelpEditActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
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
                if (item.getItemId() == R.id.changed_menu){

                    int newReward = 0;
                    int befReward = 0;
                    try {
                        newReward = Integer.valueOf(ed_reward.getText().toString());
                        befReward = Integer.valueOf(oldReward);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        Toast.makeText(HelpEditActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                    }

                    if (newReward < befReward){
                        Toast.makeText(HelpEditActivity.this,"酬金不能减少",Toast.LENGTH_SHORT).show();
                    }else{

                        RetrofitManager.builder().modifyHelp(App.getInstance().getPlayId()
                                ,helpId
                                ,ed_reward.getText().toString()
                                ,ed_timer.getText().toString()+":00"
                                ,ed_helpBody.getText().toString()
                                ,isCheck?"1":"0")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<CommonBean>() {
                                    @Override
                                    public void call(CommonBean commonBean) {
                                        if (commonBean.getRes().equals("0")){
                                            Toast.makeText(HelpEditActivity.this, "修改成功!", Toast.LENGTH_LONG).show();
                                            finish();
                                        }else {
                                            Toast.makeText(HelpEditActivity.this, "修改失败!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }, new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        Toast.makeText(HelpEditActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
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
