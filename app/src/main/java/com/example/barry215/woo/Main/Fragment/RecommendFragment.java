package com.example.barry215.woo.Main.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barry215.woo.Main.Activity.ChooseActivity;
import com.example.barry215.woo.Main.Activity.EditActivity;
import com.example.barry215.woo.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by barry215 on 2016/4/15.
 */
public class RecommendFragment extends DialogFragment {

    @Bind(R.id.tv_dialog_choose) TextView tv_1;
    @Bind(R.id.tv_dialog_publish) TextView tv_2;
    @Bind(R.id.checkbox_recom) CheckBox check_recom;
    @Bind(R.id.checkbox_follow) CheckBox check_follow;
    @Bind(R.id.checkbox_urgent) CheckBox check_urgent;


    private boolean isCheck_sysput = false;
    private boolean isCheck_follow = false;
    private boolean isCheck_urgent = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_recommend, container);
        ButterKnife.bind(this, view);

        check_recom.setChecked(isCheck_sysput);
        check_follow.setChecked(isCheck_follow);

        check_recom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheck_sysput = isChecked;
                check_recom.setChecked(isChecked);
            }
        });

        check_follow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheck_follow = isChecked;
                check_follow.setChecked(isChecked);
                if (isChecked){
                    tv_1.setVisibility(View.VISIBLE);
                }else {
                    tv_1.setVisibility(View.GONE);
                }
            }
        });

        check_urgent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheck_urgent = isChecked;
                check_urgent.setChecked(isChecked);
            }
        });

        tv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCheck_follow) {
                    Intent intent = new Intent(getActivity(), ChooseActivity.class);
                    startActivityForResult(intent,15);
                }else {
                    Toast.makeText(getActivity(),"您想选择谁呢？",Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditActivity)getActivity()).SubmitData(isCheck_urgent, isCheck_sysput);
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
