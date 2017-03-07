package com.example.barry215.woo.Main.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.example.barry215.woo.Main.Activity.MessageActivity;
import com.example.barry215.woo.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by barry215 on 2016/5/10.
 */
public class DelMsgFragment extends DialogFragment {
    @Bind(R.id.tv_dialog_back) TextView tv_1;
    @Bind(R.id.tv_dialog_confirm) TextView tv_2;

    private String msgId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_delete, container);
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
                ((MessageActivity) getActivity()).clearMsg(msgId);
                dismiss();
            }
        });
        return view;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
