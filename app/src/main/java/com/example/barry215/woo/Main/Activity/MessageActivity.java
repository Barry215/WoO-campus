package com.example.barry215.woo.Main.Activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.barry215.woo.Adapter.MsgListAdapter;
import com.example.barry215.woo.Base.BaseSwipeBackActivity;
import com.example.barry215.woo.Bean.CommonBean;
import com.example.barry215.woo.Bean.MessageListBean;
import com.example.barry215.woo.Main.Application.App;
import com.example.barry215.woo.Main.Fragment.DelMsgFragment;
import com.example.barry215.woo.Network.RetrofitManager;
import com.example.barry215.woo.R;
import com.example.barry215.woo.ui.DividerLine;
import com.yalantis.phoenix.PullToRefreshView;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by barry215 on 2016/4/12.
 */
public class MessageActivity extends BaseSwipeBackActivity {

    @Bind(R.id.toolbar_msg) Toolbar toolbar;
    @Bind(R.id.recycler_view_msg) RecyclerView recyclerView;
    @Bind(R.id.ptr_news_list) PullToRefreshView mPtrNewsList;

    private MsgListAdapter adapter;
    private static final int REFRESH_DELAY = 500;

    @Override
    public int getLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected void afterCreate() {
        initToolbar();
        initViewPager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateView();
    }

    private void updateView(){
        RetrofitManager.builder().messageList(App.getInstance().getPlayId(),"100","1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MessageListBean>() {
                    @Override
                    public void call(MessageListBean messageListBean) {
                        adapter.updateData(messageListBean.getRes());
                        mPtrNewsList.setRefreshing(false);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(MessageActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void initViewPager() {
        adapter = new MsgListAdapter(MessageActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
        DividerLine dividerLine = new DividerLine(DividerLine.VERTICAL);
        dividerLine.setSize(1);
        dividerLine.setColor(getResources().getColor(R.color.SUB_GRAY));
        recyclerView.addItemDecoration(dividerLine);

        mPtrNewsList.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPtrNewsList.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateView();
                    }
                }, REFRESH_DELAY);
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
    }

    public void showDeleteMsg(String msgId){
        DelMsgFragment dialog = new DelMsgFragment();
        dialog.setMsgId(msgId);
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    public void clearMsg(String msgId) {
        RetrofitManager.builder().deleteMsg(msgId,App.getInstance().getPlayId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CommonBean>() {
                    @Override
                    public void call(CommonBean commonBean) {
                        updateView();
                        Toast.makeText(MessageActivity.this, "删除成功！", Toast.LENGTH_LONG).show();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(MessageActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });


    }

}
