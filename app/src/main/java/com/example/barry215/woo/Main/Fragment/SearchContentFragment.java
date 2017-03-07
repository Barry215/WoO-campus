package com.example.barry215.woo.Main.Fragment;

import android.app.ProgressDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.barry215.woo.Adapter.SearchListAdapter;
import com.example.barry215.woo.Base.BaseFragment;
import com.example.barry215.woo.Bean.HelpDetailBean;
import com.example.barry215.woo.Bean.HelpListBean;
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
 * Created by barry215 on 2016/4/12.
 */
public class SearchContentFragment extends BaseFragment {

    @Bind(R.id.recycler_view_search) RecyclerView recyclerView;

    private SearchListAdapter adapter;
    private ProgressDialog dialog;

    @Override
    protected int getPageLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initPageListener() {
        ((SearchActivity) getActivity()).setOnTextSearchListener(new SearchActivity.OnTextSearchListener() {
            @Override
            public void onTextSearch(final String newText) {
                dialog = ProgressDialog.show(getActivity(), null, "搜索中，请稍后..");

                RetrofitManager.builder().searchHelp("100", "1", newText)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<HelpListBean>() {
                            @Override
                            public void call(HelpListBean helpListBean) {
                                List<HelpDetailBean> helpDetailBeanList = helpListBean.getHelp();
                                adapter.updateData(helpDetailBeanList, newText);
                                dialog.dismiss();
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Toast.makeText(getActivity(), throwable.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    protected void initPageView(View rootView) {
        adapter = new SearchListAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
        DividerLine dividerLine = new DividerLine(DividerLine.VERTICAL);
        dividerLine.setSize(1);
        dividerLine.setColor(getResources().getColor(R.color.SUB_GRAY));
        recyclerView.addItemDecoration(dividerLine);
    }


}
