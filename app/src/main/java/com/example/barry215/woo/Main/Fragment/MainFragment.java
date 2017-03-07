package com.example.barry215.woo.Main.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.barry215.woo.Adapter.AutoLoadOnScrollListener;
import com.example.barry215.woo.Adapter.NewsListAdapter;
import com.example.barry215.woo.Base.BaseFragment;
import com.example.barry215.woo.Bean.HelpDetailBean;
import com.example.barry215.woo.Bean.HelpListBean;
import com.example.barry215.woo.Network.RetrofitManager;
import com.example.barry215.woo.R;
import com.example.barry215.woo.ui.DividerLine;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.List;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by barry215 on 2016/4/11.
 */
public class MainFragment extends BaseFragment {
    @Bind(R.id.recycler_view) RecyclerView recyclerView;
    @Bind(R.id.ptr_news_list) PullToRefreshView mPtrNewsList;

    private static final int REFRESH_DELAY = 500;
    private NewsListAdapter adapter;
    private boolean isFirst = true;
    private int page;
    private AutoLoadOnScrollListener mAutoLoadListener;

    @Override
    protected int getPageLayoutId() {
        return R.layout.fragment_newslist;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirst){
            initView();
        }
        isFirst = false;
    }

    @Override
    protected void initPageListener() {
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

    private void updateView() {

        RetrofitManager.builder().getHelpList("0","100",page +"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<HelpListBean>() {
                    @Override
                    public void call(HelpListBean helpListBean) {
                        mAutoLoadListener.setLoading(false);
                        adapter.addView(helpListBean.getHelp());
//                        mPtrNewsList.setRefreshing(false);
                        page++;
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(getActivity(), throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void initView() {
        RetrofitManager.builder().getHelpList("0","100","1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<HelpListBean>() {
                    @Override
                    public void call(HelpListBean helpListBean) {
                        if (helpListBean.getHelp().size() < 100){
                            mAutoLoadListener.setLoading(true);
                        }else {
                            mAutoLoadListener.setLoading(false);
                        }
                        List<HelpDetailBean> helpDetailBeanList = helpListBean.getHelp();
                        adapter.initView(helpDetailBeanList);
                        mPtrNewsList.setRefreshing(false);
                        page = 2;
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(getActivity(), throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void initPageView(View rootView) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new NewsListAdapter(getActivity());
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

