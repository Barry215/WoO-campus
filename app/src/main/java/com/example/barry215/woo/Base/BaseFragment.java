package com.example.barry215.woo.Base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by barry215 on 2016/4/11.
 */
public abstract class BaseFragment extends Fragment {
    protected Activity mActivity;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getPageLayoutId(), container, false);
        ButterKnife.bind(this, view);
        mActivity = getActivity();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPageView(view);
        initPageListener();
    }

    protected abstract int getPageLayoutId();

    protected abstract void initPageListener();

    protected abstract void initPageView(View rootView);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
