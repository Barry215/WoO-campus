package com.example.barry215.woo.Main.Activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.barry215.woo.Base.BaseSwipeBackActivity;
import com.example.barry215.woo.Bean.CommonBean;
import com.example.barry215.woo.Main.Application.App;
import com.example.barry215.woo.Network.RetrofitManager;
import com.example.barry215.woo.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by barry215 on 2016/4/28.
 */
public class CommentActivity extends BaseSwipeBackActivity {
    @Bind(R.id.ed_comment) MaterialEditText ed_comment;
    @Bind(R.id.toolbar_comment) Toolbar toolbar;

    private String commented_username;
    private String replyId;
    private String helpId;
    private MenuItem item;

    @Override
    public int getLayoutId() {
        return R.layout.activity_comment;
    }

    @Override
    protected void afterCreate() {
        initPageView();
        initToolbar();
        initPageClick();
    }

    private void initPageClick() {
        ed_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String content = ed_comment.getText().toString().replaceAll("\\s*|\t|\r|\n", "");
                if(!TextUtils.isEmpty(content)){
                    item.setVisible(true);
                }else {
                    item.setVisible(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initPageView() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        commented_username = bundle.getString("commented_username");
        replyId = bundle.getString("parentId");
        helpId = bundle.getString("help_id");
        ed_comment.setHint(" @" + commented_username);
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
                if (item.getItemId() == R.id.send_menu) {
                    if (TextUtils.isEmpty(ed_comment.getText().toString())) {
                        Toast.makeText(CommentActivity.this, "您的评论为空", Toast.LENGTH_SHORT).show();
                    } else {

                        RetrofitManager.builder().createRemark(helpId,App.getInstance().getPlayId(),replyId,ed_comment.getText().toString())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<CommonBean>() {
                                    @Override
                                    public void call(CommonBean commonBean) {
                                        if (commonBean.getRes().equals("0")){
                                            Toast.makeText(CommentActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }
                                }, new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        Toast.makeText(CommentActivity.this, throwable.toString(), Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        item = menu.getItem(0);
        return true;
    }
}
