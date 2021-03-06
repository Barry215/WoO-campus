package com.example.barry215.woo.Main.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barry215.woo.Bean.CommonBean;
import com.example.barry215.woo.Main.Activity.HelperListActivity;
import com.example.barry215.woo.Network.RetrofitManager;
import com.example.barry215.woo.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by barry215 on 2016/5/3.
 */
public class FinishFragment extends DialogFragment {

    @Bind(R.id.tv_dialog_back) TextView tv_1;
    @Bind(R.id.tv_dialog_confirm) TextView tv_2;

    private String chooserId = "";
    private String helpId = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_finish, container);
        ButterKnife.bind(this, view);
        tv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RetrofitManager.builder().finishHelp(helpId,chooserId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<CommonBean>() {
                            @Override
                            public void call(CommonBean commonBean) {
                                if (commonBean.getRes().equals("0")) {
                                    ((HelperListActivity) getActivity()).helpFinish(chooserId);
                                    dismiss();
                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Toast.makeText(getActivity(), throwable.toString(), Toast.LENGTH_LONG).show();
                            }
                        });

            }
        });
        return view;
    }

    public void setChooserId(String chooserId){
        this.chooserId = chooserId;
    }

    public void setHelpId(String helpId) {
        this.helpId = helpId;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
