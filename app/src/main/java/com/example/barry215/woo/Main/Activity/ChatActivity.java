package com.example.barry215.woo.Main.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barry215.greendao.ChatInfoCache;
import com.example.barry215.greendao.ChatInfoCacheDao;
import com.example.barry215.woo.Adapter.ChatListAdapter;
import com.example.barry215.woo.Base.BaseSwipeBackActivity;
import com.example.barry215.woo.DB.DaoHelper;
import com.example.barry215.woo.Main.Application.App;
import com.example.barry215.woo.R;
import com.yalantis.phoenix.PullToRefreshView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by barry215 on 2016/5/11.
 */
public class ChatActivity extends BaseSwipeBackActivity {
    @Bind(R.id.toolbar_chat) Toolbar toolbar;
    @Bind(R.id.ed_input) EditText ed_input;
    @Bind(R.id.iv_send) ImageView iv_send;
    @Bind(R.id.recycler_view_chat) RecyclerView recyclerView;
    @Bind(R.id.input_tv_name) TextView input_tv_name;
    @Bind(R.id.ptr_news_list) PullToRefreshView mPtrNewsList;

    private Socket socket = null;
    private BufferedReader bufferedReader = null;
    private BufferedWriter bufferedWriter = null;
    private Observable<String> connectObservable;
    private String userName;
    private String userId;
    private String receiverHead;
    private String senderHead;
    private ChatListAdapter adapter;
    private boolean isConnect = false;
    private static final int REFRESH_DELAY = 1000;
    private boolean isFirstDrag = true;
    private boolean isOk = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void afterCreate() {
        initToolbar();
        initPageView();
        initConnect();
        initClick();
        initPageListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startChat();
    }

    private void initPageListener() {
        mPtrNewsList.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPtrNewsList.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isFirstDrag){
                            updateView();
                        }else {
                            Toast.makeText(ChatActivity.this, "没有更多的数据了", Toast.LENGTH_SHORT).show();
                            mPtrNewsList.setRefreshing(false);
                        }
                    }
                }, REFRESH_DELAY);
            }
        });
    }

    private void updateView() {
        List<ChatInfoCache> chatInfoCaches = getBefNews();
        if (chatInfoCaches.size() <= 2){
            Toast.makeText(ChatActivity.this, "没有更多的数据了", Toast.LENGTH_SHORT).show();
        }else {
            adapter.updateView(chatInfoCaches);
            recyclerView.smoothScrollToPosition(adapter.getItemCount() - 3);
        }
        mPtrNewsList.setRefreshing(false);
        isFirstDrag = false;
    }

    private void initConnect() {
        connectObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(connect());
                String line;
                try {
                    while((line = bufferedReader.readLine())!= null){
                        subscriber.onNext(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                subscriber.onCompleted();
            }
        });
    }


    private void initClick() {
        iv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnect && isOk) {
                    sendChatMsg();
                } else {
                    Toast.makeText(ChatActivity.this, "您还未连接服务器！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private List<ChatInfoCache> getBefNews(){
        ChatInfoCacheDao chatInfoCacheDao = DaoHelper.getDaoSession(this).getChatInfoCacheDao();
        return chatInfoCacheDao.queryBuilder()
                .where(chatInfoCacheDao.queryBuilder()
                        .or(ChatInfoCacheDao.Properties.SenderId.eq(App.getInstance().getUserId())
                                , ChatInfoCacheDao.Properties.SenderId.eq(userId))
                        , chatInfoCacheDao.queryBuilder()
                        .or(ChatInfoCacheDao.Properties.ReceiverId.eq(App.getInstance().getUserId())
                                , ChatInfoCacheDao.Properties.ReceiverId.eq(userId))).list();

    }

    private void initPageView() {
        Bundle bundle = getIntent().getBundleExtra("bundle");

        userId = bundle.getString("userId");
        userName = bundle.getString("userName");
        receiverHead = bundle.getString("receiverHead");
        senderHead = bundle.getString("senderHead");

        input_tv_name.setText(userName);

        List<ChatInfoCache> chatInfoCaches = getBefNews();

        List<ChatInfoCache> chatInfoCacheList = new ArrayList<>();
        if (chatInfoCaches.size() > 2){
            Collections.reverse(chatInfoCaches);
            chatInfoCacheList.add(chatInfoCaches.get(1));
            chatInfoCacheList.add(chatInfoCaches.get(0));
        }else {
            chatInfoCacheList.addAll(chatInfoCaches);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ChatListAdapter(ChatActivity.this,chatInfoCacheList,senderHead,receiverHead);
        recyclerView.setAdapter(adapter);
    }

    private void sendChatMsg() {
        if (TextUtils.isEmpty(ed_input.getText().toString().trim())){
            Toast.makeText(ChatActivity.this, "不能发送空消息！", Toast.LENGTH_SHORT).show();
        }else {
            ChatInfoCacheDao chatInfoCacheDao = DaoHelper.getDaoSession(this).getChatInfoCacheDao();
            ChatInfoCache chatInfoCache = new ChatInfoCache();
            chatInfoCache.setSenderId(App.getInstance().getUserId());
            chatInfoCache.setReceiverId(userId);
            chatInfoCache.setMsg(ed_input.getText().toString());
            chatInfoCache.setTime(new Date());
            chatInfoCacheDao.insert(chatInfoCache);

            adapter.addView(chatInfoCache);
            try {
                bufferedWriter.write(ed_input.getText().toString() + "\n");
                bufferedWriter.flush();
                ed_input.setText("");
                hideKeyboard();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void receiveChatMsg(String msg) {
        ChatInfoCacheDao chatInfoCacheDao = DaoHelper.getDaoSession(this).getChatInfoCacheDao();
        ChatInfoCache chatInfoCache = new ChatInfoCache();
        chatInfoCache.setSenderId(userId);
        chatInfoCache.setReceiverId(App.getInstance().getUserId());
        chatInfoCache.setMsg(msg);
        chatInfoCache.setTime(new Date());
        chatInfoCacheDao.insert(chatInfoCache);

        adapter.addView(chatInfoCache);
    }

    private void startChat() {
        connectObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ChatActivity.this, "错误:" + e.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(String s) {
                        if (s.equals("success")) {
                            isConnect = true;
                        } else if (s.equals("connected")) {
                            isOk = true;
                        } else {
                            receiveChatMsg(s);
                        }
                    }
                });
    }

    private String connect() {
        try {
            socket = new Socket("139.129.28.137",14321);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
            return "success";
        } catch (IOException e) {
            isConnect = false;
        }
        return null;
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

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isConnect){
            try {
                bufferedWriter.write("exit" + "\n");
                bufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                socket.close();
                bufferedWriter.close();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
