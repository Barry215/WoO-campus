package com.example.barry215.woo.Main.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.example.barry215.woo.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by barry215 on 2016/5/11.
 */
public class DelHelpFragment extends DialogFragment {
    @Bind(R.id.tv_dialog_back) TextView tv_1;
    @Bind(R.id.tv_dialog_confirm) TextView tv_2;

    private String helpId;
    private OnConfirmClickListener mOnConfirmClickListener;

    public interface OnConfirmClickListener {
        void onConfirmClick(String helpId);
    }

    public void setOnConfirmClickListener(OnConfirmClickListener mOnConfirmClickListener) {
        this.mOnConfirmClickListener = mOnConfirmClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_delhelp, container);
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
                mOnConfirmClickListener.onConfirmClick(helpId);
                dismiss();
            }
        });
        return view;
    }

    public void setHelpId(String helpId){
        this.helpId = helpId;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
