package com.example.barry215.woo.Main.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;


import com.example.barry215.woo.Main.Activity.NewsDetailActivity;
import com.example.barry215.woo.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by barry215 on 2016/5/10.
 */
public class MsgFragment extends DialogFragment {
    @Bind(R.id.tv_dialog_back) TextView tv_1;
    @Bind(R.id.tv_dialog_confirm) TextView tv_2;
    @Bind(R.id.ed_msg) EditText ed_msg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_msg, container);
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
                ((NewsDetailActivity) getActivity()).sendMsg(ed_msg.getText().toString());
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
