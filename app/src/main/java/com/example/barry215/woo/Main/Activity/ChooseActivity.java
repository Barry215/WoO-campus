package com.example.barry215.woo.Main.Activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.barry215.woo.Adapter.ChooseListAdapter;
import com.example.barry215.woo.Base.BaseSwipeBackActivity;
import com.example.barry215.woo.Bean.UserInfoBean;
import com.example.barry215.woo.Bean.UserListBean;
import com.example.barry215.woo.Main.Application.App;
import com.example.barry215.woo.Network.RetrofitManager;
import com.example.barry215.woo.R;
import com.example.barry215.woo.ui.DividerLine;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by barry215 on 2016/4/15.
 */
public class ChooseActivity extends BaseSwipeBackActivity {
    @Bind(R.id.toolbar_choose) Toolbar toolbar;
    @Bind(R.id.recycler_view_choose) RecyclerView recyclerView;

    private ChooseListAdapter adapter;
    private List<UserInfoBean> userInfoBeanList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose;
    }

    @Override
    protected void afterCreate() {
        initToolbar();
        initViewPager();
        initData();
    }

    private void initData() {
        RetrofitManager.builder().getUserFollowList(App.getInstance().getPlayId(),"100","1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserListBean>() {
                    @Override
                    public void call(UserListBean userListBean) {
                        userInfoBeanList = userListBean.getRes();
                        adapter.updateData(userInfoBeanList);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(ChooseActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
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

                if (item.getItemId() == R.id.complete_menu) {

                    List<Boolean> checks = adapter.getCheck();
                    ArrayList<String> positionList = new ArrayList<>();

                    for(int i = 0; i<checks.size() ; i++){
                        if(checks.get(i)){
                            String userId = userInfoBeanList.get(i).getId();
                            positionList.add(userId);
                        }
                    }
                    Intent intent = new Intent();
                    intent.putExtra("chooserId",positionList.get(0));
                    setResult(15, intent);
                    finish();

                }
                return true;
            }
        });
    }

    private void initViewPager() {
        adapter = new ChooseListAdapter(ChooseActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
        DividerLine dividerLine = new DividerLine(DividerLine.VERTICAL);
        dividerLine.setSize(1);
        dividerLine.setColor(getResources().getColor(R.color.GRAY));
        recyclerView.addItemDecoration(dividerLine);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_choose, menu);
        return true;
    }
}
