package com.example.barry215.woo.Main.Fragment;

import android.app.ProgressDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.barry215.woo.Adapter.SearchPersonListAdapter;
import com.example.barry215.woo.Base.BaseFragment;
import com.example.barry215.woo.Bean.UserInfoBean;
import com.example.barry215.woo.Bean.UserListBean;
import com.example.barry215.woo.Main.Activity.SearchActivity;
import com.example.barry215.woo.Network.RetrofitManager;
import com.example.barry215.woo.R;
import com.example.barry215.woo.ui.DividerLine;

import java.util.List;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by barry215 on 2016/4/13.
 */
public class SearchPersonFragment extends BaseFragment {
    @Bind(R.id.recycler_view_search) RecyclerView recyclerView;

    private ProgressDialog dialog;
    private SearchPersonListAdapter adapter;

    @Override
    protected int getPageLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initPageListener() {
        ((SearchActivity) getActivity()).setOnPersonSearchListener(new SearchActivity.OnPersonSearchListener() {
            @Override
            public void OnPersonSearch(final String newText) {
                dialog = ProgressDialog.show(getActivity(), null, "搜索中，请稍后..");

                RetrofitManager.builder().searchUser("100", "1", newText)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<UserListBean>() {
                            @Override
                            public void call(UserListBean userListBean) {
                                List<UserInfoBean> userInfoBeanList = userListBean.getRes();
                                adapter.updateData(userInfoBeanList, newText);
                                dialog.dismiss();
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Toast.makeText(getActivity(), throwable.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    @Override
    protected void initPageView(View rootView) {
        adapter = new SearchPersonListAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
        DividerLine dividerLine = new DividerLine(DividerLine.VERTICAL);
        dividerLine.setSize(1);
        dividerLine.setColor(getResources().getColor(R.color.SUB_GRAY));
        recyclerView.addItemDecoration(dividerLine);
    }

}
