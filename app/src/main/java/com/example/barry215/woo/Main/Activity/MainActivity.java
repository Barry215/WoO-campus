package com.example.barry215.woo.Main.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.barry215.woo.Adapter.FragmentAdapter;
import com.example.barry215.woo.Base.BaseActivity;
import com.example.barry215.woo.Bean.CommonBean;
import com.example.barry215.woo.Bean.IInfoBean;
import com.example.barry215.woo.Main.Application.App;
import com.example.barry215.woo.Main.Fragment.HotFragment;
import com.example.barry215.woo.Main.Fragment.MainFragment;
import com.example.barry215.woo.Main.Fragment.PromptFragment;
import com.example.barry215.woo.Main.Fragment.UrgentFragment;
import com.example.barry215.woo.Network.RetrofitManager;
import com.example.barry215.woo.R;
import com.example.barry215.woo.Utils.NetUtil;
import com.example.barry215.woo.ui.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    @Bind(R.id.drawer_main) DrawerLayout drawer;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.nv_main) NavigationView navigationView;
    @Bind(R.id.viewPager_main) ViewPager viewPager;
    @Bind(R.id.tabs) TabLayout tabLayout;
    @Bind(R.id.fab) FloatingActionButton fab;


    private long firstTime;
    private View headerView;
    private MenuItem item;
    private ImageView iv_header;
    private TextView tv_header;
    private TextView state_header;
    private TextView info_header;
    private ImageView header_sex;

    @Override
    protected void afterCreate() {
        initView();
        initClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        testLogin();
    }

    private void testLogin() {
        if (App.getInstance().getIsLogin()){
            updateNavigation();
            updateMenu();
        }else {
            cleanNavigation();
        }
    }

    private void cleanNavigation(){

        header_sex.setVisibility(View.GONE);
        iv_header.setImageDrawable(getResources().getDrawable(R.drawable.person_default_icon));
        tv_header.setText("未命名");
        state_header.setText("");
        info_header.setText("");
    }

    private void updateNavigation() {

        if (!NetUtil.isNetworkConnected(this)){
            Toast.makeText(MainActivity.this,"网络未连接！", Toast.LENGTH_LONG).show();
        }else {
            RetrofitManager.builder().getIInfo(App.getInstance().getPlayId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<IInfoBean>() {
                        @Override
                        public void call(IInfoBean iInfoBean) {
                            if (iInfoBean.getError().equals("0")) {
                                App.getInstance().setUserId(iInfoBean.getId());
                                if (iInfoBean.getHead().equals("default.png")
                                        || iInfoBean.getUniversity().equals("")
                                        || iInfoBean.getCollege().equals("")
                                        || iInfoBean.getSex().equals("2")
                                        || iInfoBean.getUsersign().equals("")
                                        || iInfoBean.getEmail().equals("")) {
                                    App.getInstance().setPullInfo(false);
                                } else {
                                    App.getInstance().setPullInfo(true);//完善了资料
                                }

                                if(iInfoBean.getSex().equals("0")){
                                    header_sex.setImageDrawable(getResources().getDrawable(R.drawable.sex_male));
                                    header_sex.setVisibility(View.VISIBLE);
                                }else if (iInfoBean.getSex().equals("1")){
                                    header_sex.setImageDrawable(getResources().getDrawable(R.drawable.sex_female));
                                    header_sex.setVisibility(View.VISIBLE);
                                }else if(iInfoBean.getSex().equals("2")){
                                    header_sex.setVisibility(View.GONE);
                                }

                                if (!iInfoBean.getHead().equals("default.png") && !iInfoBean.getHead().equals("")){
                                    Glide.with(MainActivity.this).load(App.LOADHEADADDRESS + iInfoBean.getHead())
                                            .transform(new GlideCircleTransform(MainActivity.this))
                                            .placeholder(R.drawable.person_default_icon).into(iv_header);
                                }

                                tv_header.setText(iInfoBean.getUsername());


                                if (iInfoBean.getState().equals("0")){
                                    state_header.setText("空闲");
                                }else if (iInfoBean.getState().equals("1")){
                                    state_header.setText("忙碌");
                                }else {
                                    state_header.setText("失联");
                                }
                                info_header.setText(iInfoBean.getUsersign());

                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Toast.makeText(MainActivity.this, "您已在别处登陆！", Toast.LENGTH_LONG).show();
                        }
                    });
        }


    }

    private void initClick() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.getInstance().getIsLogin()) {
                    if (App.getInstance().getPullInfo()){
                        Intent intent = new Intent(MainActivity.this, EditActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(MainActivity.this, "您还未完善资料", Toast.LENGTH_LONG).show();
                    }
                } else {
                    PromptFragment dialog = new PromptFragment();
                    dialog.show(getSupportFragmentManager(), "dialog");
                }
            }
        });
    }

    private void initView() {
        initToolbar();
        initNavigation();
        initMenu();
        initViewPager();
        initNotice();
    }

    private void initNotice() {
        Intent intent= getIntent();
        Bundle bundle_main = intent.getBundleExtra("bundle");
        if(bundle_main != null){
            String str = bundle_main.getString("msg");
            Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
        }
    }

    private void initViewPager() {
        List<String> titles = new ArrayList<>();
        titles.add("最新");
        titles.add("热门");
        titles.add("加急");
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(2)));
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new MainFragment());
        fragments.add(new HotFragment());
        fragments.add(new UrgentFragment());
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setTabsFromPagerAdapter(fragmentAdapter);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    private void initNavigation() {
        headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        iv_header = (ImageView) headerView.findViewById(R.id.iv_header);
        tv_header = (TextView) headerView.findViewById(R.id.tv_header);
        state_header = (TextView) headerView.findViewById(R.id.state_header);
        info_header = (TextView) headerView.findViewById(R.id.info_header);
        header_sex = (ImageView) headerView.findViewById(R.id.header_sex);

        iv_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(App.getInstance().getIsLogin()){
                    Intent intent = new Intent(MainActivity.this, SelfActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this,"您还未登陆，请您登陆后再操作",Toast.LENGTH_SHORT).show();
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_one:
                        mToolbar.setTitle("喔哦");
                        break;
                    case R.id.menu_two:
                        if (App.getInstance().getIsLogin()) {
                            Intent intent = new Intent(MainActivity.this, FindActivity.class);
                            startActivity(intent);
                        } else {
                            PromptFragment dialog_1 = new PromptFragment();
                            dialog_1.show(getSupportFragmentManager(), "dialog");
                        }
                        break;
                    case R.id.menu_three:
                        if (App.getInstance().getIsLogin()) {
                            if (App.getInstance().getPullInfo()){
                                Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(MainActivity.this, "您还未完善资料", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            PromptFragment dialog_2 = new PromptFragment();
                            dialog_2.show(getSupportFragmentManager(), "dialog");
                        }
                        break;
                    case R.id.menu_four:
                        if (App.getInstance().getIsLogin()) {
                            if (App.getInstance().getPullInfo()){
                                Intent intent = new Intent(MainActivity.this, FollowActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(MainActivity.this, "您还未完善资料", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            PromptFragment dialog_3 = new PromptFragment();
                            dialog_3.show(getSupportFragmentManager(), "dialog");
                        }
                        break;
                    case R.id.menu_five:
                        if (App.getInstance().getIsLogin()) {
                            if (App.getInstance().getPullInfo()){
                                Intent intent = new Intent(MainActivity.this, MessageActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(MainActivity.this, "您还未完善资料", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            PromptFragment dialog_4 = new PromptFragment();
                            dialog_4.show(getSupportFragmentManager(), "dialog");
                        }

                        break;
                    case R.id.menu_six:
                        Intent intent_1 = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent_1);
//                        overridePendingTransition(R.anim.act_switch_fade_in,R.anim.act_switch_fade_out);
                        break;
                    case R.id.menu_seven:
                        Intent intent_2 = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(intent_2);
//                        overridePendingTransition(R.anim.act_switch_fade_in, R.anim.act_switch_fade_out);
                        break;
                }
//                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void initToolbar(){
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
        }else {
            setSupportActionBar(mToolbar);
            mToolbar.setTitle(getResources().getString(R.string.app_name));
        }
    }

    private void initMenu(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.search_menu:
                        Intent intent_1 = new Intent(MainActivity.this,SearchActivity.class);
                        startActivity(intent_1);
                        break;
                    case R.id.reminder_menu:
                        if(App.getInstance().getIsLogin()){
                            item.setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_notifications));
                            Intent intent_2 = new Intent(MainActivity.this, MessageActivity.class);
                            startActivity(intent_2);
                        }else {
                            Toast.makeText(MainActivity.this,"您还未登陆，请您登陆后再操作",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.set_menu:
                        Intent intent_3 = new Intent(MainActivity.this,SettingActivity.class);
                        startActivity(intent_3);
                        break;
                }
                return true;
            }
        });
    }

    private void updateMenu(){

        RetrofitManager.builder().hasNewMsg(App.getInstance().getPlayId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CommonBean>() {
                    @Override
                    public void call(CommonBean commonBean) {
                        if (commonBean.getRes().equals("1")){
                            item.setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_notifications_red));
                        }else {
                            item.setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_notifications));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(MainActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.LEFT)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
            } else {
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        item = menu.findItem(R.id.reminder_menu);
        return true;
    }

}
