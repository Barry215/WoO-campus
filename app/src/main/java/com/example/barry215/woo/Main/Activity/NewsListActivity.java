package com.example.barry215.woo.Main.Activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barry215.woo.Adapter.AutoLoadOnScrollListener;
import com.example.barry215.woo.Adapter.NewsListAdapter;
import com.example.barry215.woo.Base.BaseSwipeBackActivity;
import com.example.barry215.woo.Bean.HelpDetailBean;
import com.example.barry215.woo.Bean.HelpListBean;
import com.example.barry215.woo.Network.RetrofitManager;
import com.example.barry215.woo.R;
import com.example.barry215.woo.ui.DividerLine;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by barry215 on 2016/5/2.
 */
public class NewsListActivity extends BaseSwipeBackActivity{
    @Bind(R.id.recycler_view_newsList) RecyclerView recyclerView;
    @Bind(R.id.toolbar_newsList) Toolbar toolbar;
    @Bind(R.id.tv_title) TextView tv_title;
    @Bind(R.id.ptr_news_list) PullToRefreshView mPtrNewsList;

    private NewsListAdapter adapter;
    private String tabName;
    private static final int REFRESH_DELAY = 500;
    private boolean isFirst = true;
    private int page;
    private int position;
    private AutoLoadOnScrollListener mAutoLoadListener;

    @Override
    public int getLayoutId() {
        return R.layout.activity_newslist;
    }

    @Override
    protected void afterCreate() {
        initToolbar();
        initViewPager();
        initPageListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirst){
            initView();
        }
        isFirst = false;
    }

    private void initPageListener() {
        mPtrNewsList.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPtrNewsList.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initView();
                    }
                }, REFRESH_DELAY);

            }
        });
    }

    private void initView() {
        RetrofitManager.builder().getHelpListByType(position + "", "00", "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<HelpListBean>() {
                    @Override
                    public void call(HelpListBean helpListBean) {
                        if (helpListBean.getHelp().size() < 100) {
                            mAutoLoadListener.setLoading(true);
                        } else {
                            mAutoLoadListener.setLoading(false);
                        }
                        List<HelpDetailBean> helpDetailBeanList = helpListBean.getHelp();
                        Collections.reverse(helpDetailBeanList);
                        adapter.initView(helpDetailBeanList);
                        mPtrNewsList.setRefreshing(false);
                        page = 2;
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(NewsListActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void updateView() {

        RetrofitManager.builder().getHelpListByType(position + "", "100", page + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<HelpListBean>() {
                    @Override
                    public void call(HelpListBean helpListBean) {
                        mAutoLoadListener.setLoading(false);
                        adapter.addView(helpListBean.getHelp());
                        mPtrNewsList.setRefreshing(false);
                        page++;
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(NewsListActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
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
        position = getIntent().getIntExtra("position",0);
        tabName = getIntent().getStringExtra("tabName");
        tv_title.setText(tabName);
    }

    private void initViewPager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new NewsListAdapter(NewsListActivity.this);
        recyclerView.setAdapter(adapter);
        DividerLine dividerLine = new DividerLine(DividerLine.VERTICAL);
        dividerLine.setSize(1);
        dividerLine.setColor(getResources().getColor(R.color.SUB_GRAY));
        recyclerView.addItemDecoration(dividerLine);

        mAutoLoadListener = new AutoLoadOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                updateView();
            }
        };
        recyclerView.addOnScrollListener(mAutoLoadListener);

        initView();
    }
}
