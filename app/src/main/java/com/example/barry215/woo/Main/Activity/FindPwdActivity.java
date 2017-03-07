package com.example.barry215.woo.Main.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.barry215.woo.Base.BaseSwipeBackActivity;
import com.example.barry215.woo.Bean.CommonBean;
import com.example.barry215.woo.Network.RetrofitManager;
import com.example.barry215.woo.R;
import com.example.barry215.woo.Utils.TimeCountUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by barry215 on 2016/4/14.
 */
public class FindPwdActivity extends BaseSwipeBackActivity {
    @Bind(R.id.toolbar_findPwd) Toolbar toolbar;
    @Bind(R.id.btn_validate) Button btn_validate;
    @Bind(R.id.btn_getCode_find) Button btn_getCode_find;
    @Bind(R.id.phoneWrapper_find) TextInputLayout phoneWrapper;
    @Bind(R.id.codeWrapper) TextInputLayout codeWrapper;
    @Bind(R.id.pwdWrapper_1) TextInputLayout pwdWrapper_1;
    @Bind(R.id.pwdWrapper_2) TextInputLayout pwdWrapper_2;
    @Bind(R.id.ed_phone_find) EditText ed_phone_find;
    @Bind(R.id.ed_code) EditText ed_code;
    @Bind(R.id.ed_pwd_1) EditText ed_pwd_1;
    @Bind(R.id.ed_pwd_2) EditText ed_pwd_2;

    private static final String NUMBER_PATTERN = "[1][3578]\\d{9}";
    private Pattern pattern = Pattern.compile(NUMBER_PATTERN);
    private Matcher matcher;
    private boolean isPass_2 = false;
    private boolean isPass_3 = false;
    private boolean isPass_4 = false;
    private boolean isPass_5 = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_findpwd;
    }

    @Override
    protected void afterCreate() {
        initToolbar();
        initClick();
    }

    private void initClick() {
        btn_getCode_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = ed_phone_find.getText().toString();
                if (!validatePhone(phoneNum)) {
                    phoneWrapper.setErrorEnabled(true);
                    phoneWrapper.setError("手机号好像写错了哟~");
                } else {
                    phoneWrapper.setErrorEnabled(false);
                    RetrofitManager.builder().getBackPsd_SendMsg(phoneNum)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<CommonBean>() {
                                @Override
                                public void call(CommonBean commonBean) {
                                    if (commonBean.getRes().equals("0")) {
                                        Toast.makeText(FindPwdActivity.this, "短信已发送，请接收！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    Toast.makeText(FindPwdActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                                }
                            });
                    TimeCountUtil timeCountUtil = new TimeCountUtil(FindPwdActivity.this, 60000, 1000, btn_getCode_find);
                    timeCountUtil.start();
                }
            }
        });

        btn_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                String str_phone = ed_phone_find.getText().toString();
                String str_code = ed_code.getText().toString();
                String str_pwd_1 = ed_pwd_1.getText().toString();
                String str_pwd_2 = ed_pwd_2.getText().toString();

                if (!validatePhone(str_phone)) {
                    phoneWrapper.setErrorEnabled(true);
                    phoneWrapper.setError("手机号好像写错了哟~");
                    isPass_2 = false;
                } else {
                    phoneWrapper.setErrorEnabled(false);
                    isPass_2 = true;
                }
                if(!validateVerify(str_code)){
                    codeWrapper.setEnabled(true);
                    codeWrapper.setError("验证码是6位!");
                    isPass_3 = false;
                }else {
                    codeWrapper.setEnabled(false);
                    isPass_3 = true;
                }

                if (!validatePassword_1(str_pwd_1)) {
                    pwdWrapper_1.setErrorEnabled(true);
                    pwdWrapper_1.setError("密码不能小于6位哟~");
                    isPass_4 = false;
                } else {
                    pwdWrapper_1.setErrorEnabled(false);
                    isPass_4 = true;
                }

                if (!validatePassword_2(str_pwd_1, str_pwd_2)) {
                    pwdWrapper_2.setErrorEnabled(true);
                    pwdWrapper_2.setError("请您再次确认密码！");
                    isPass_5 = false;
                } else {
                    pwdWrapper_2.setErrorEnabled(false);
                    isPass_5 = true;
                }
                if (isPass_2 && isPass_3 && isPass_4 && isPass_5) {
                    ed_phone_find.setFocusable(false);
                    doValidate(str_phone,str_code,str_pwd_1);
                }
            }
        });
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
    }

    private void doValidate(String phone, String code,String newPwd){
        RetrofitManager.builder().getBackPsd(phone,code,newPwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CommonBean>() {
                    @Override
                    public void call(CommonBean commonBean) {
                        if (commonBean.getRes().equals("0")) {
                            Toast.makeText(FindPwdActivity.this, "密码修改成功！", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(FindPwdActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(FindPwdActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    //隐藏键盘
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public boolean validatePhone(String num){
        matcher = pattern.matcher(num);
        return matcher.matches();
    }

    public boolean validateVerify(String verifyCode) {
        return verifyCode.length() == 6;
    }

    public boolean validatePassword_1(String password) {
        return password.length() >= 6;
    }

    public boolean validatePassword_2(String pwd_1,String pwd_2) {
        return pwd_1.equals(pwd_2);
    }

}
