package com.example.barry215.woo.Main.Activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.barry215.woo.Adapter.FragmentAdapter;
import com.example.barry215.woo.Base.BaseSwipeBackActivity;
import com.example.barry215.woo.Main.Fragment.HelpFragment;
import com.example.barry215.woo.Main.Fragment.HelperFragment;
import com.example.barry215.woo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by barry215 on 2016/4/18.
 */
public class HelpActivity extends BaseSwipeBackActivity {
    @Bind(R.id.tabs_help) TabLayout tabLayout;
    @Bind(R.id.viewPager_help) ViewPager viewPager;
    @Bind(R.id.toolbar_help) Toolbar toolbar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_help;
    }

    @Override
    protected void afterCreate() {
        initToolbar();
        initViewPager();
    }

    private void initViewPager() {
        List<String> titles = new ArrayList<>();
        titles.add("我的帮助");
        titles.add("请求帮助");
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(1)));
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HelpFragment());
        fragments.add(new HelperFragment());
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
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
