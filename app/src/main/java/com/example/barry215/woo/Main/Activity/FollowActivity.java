package com.example.barry215.woo.Main.Activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.barry215.woo.Adapter.FollowListAdapter;
import com.example.barry215.woo.Base.BaseSwipeBackActivity;
import com.example.barry215.woo.Bean.UserListBean;
import com.example.barry215.woo.Main.Application.App;
import com.example.barry215.woo.Network.RetrofitManager;
import com.example.barry215.woo.R;
import com.example.barry215.woo.ui.DividerLine;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by barry215 on 2016/5/2.
 */
public class FollowActivity extends BaseSwipeBackActivity {
    @Bind(R.id.recycler_view_follow) RecyclerView recyclerView;
    @Bind(R.id.toolbar_follow) Toolbar toolbar;

    private FollowListAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_follow;
    }

    @Override
    protected void afterCreate() {
        initToolbar();
        initViewPager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        RetrofitManager.builder().getUserFollowList(App.getInstance().getPlayId(),"100","1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserListBean>() {
                    @Override
                    public void call(UserListBean userListBean) {
                        adapter.updateData(userListBean.getRes());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(FollowActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void initViewPager() {
        adapter = new FollowListAdapter(FollowActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
        DividerLine dividerLine = new DividerLine(DividerLine.VERTICAL);
        dividerLine.setSize(1);
        dividerLine.setColor(getResources().getColor(R.color.GRAY));
        recyclerView.addItemDecoration(dividerLine);
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
