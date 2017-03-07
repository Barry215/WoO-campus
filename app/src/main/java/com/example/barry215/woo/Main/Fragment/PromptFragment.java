package com.example.barry215.woo.Main.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.example.barry215.woo.Main.Activity.LoginActivity;
import com.example.barry215.woo.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by barry215 on 2016/4/12.
 */
public class PromptFragment extends DialogFragment {

    @Bind(R.id.tv_dialog_lookFirst) TextView tv_1;
    @Bind(R.id.tv_dialog_login) TextView tv_2;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_prompt, container);
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
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
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
