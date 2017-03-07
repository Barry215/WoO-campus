package com.example.barry215.woo.Main.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barry215.woo.Base.BaseSwipeBackActivity;
import com.example.barry215.woo.Bean.CommonBean;
import com.example.barry215.woo.Bean.RegisterText;
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
public class SignUpActivity extends BaseSwipeBackActivity {

    @Bind(R.id.btn_sign_up) Button btn_sign_up;
    @Bind(R.id.tv_getCode) Button btn_getCode;
    @Bind(R.id.tv_return) TextView tv_return;
    @Bind(R.id.phoneWrapper) TextInputLayout phoneWrapper;
    @Bind(R.id.userNameWrapper) TextInputLayout userNameWrapper;
    @Bind(R.id.verifyWrapper) TextInputLayout verifyWrapper;
    @Bind(R.id.passwordWrapper_1) TextInputLayout passwordWrapper_1;
    @Bind(R.id.passwordWrapper_2) TextInputLayout passwordWrapper_2;
    @Bind(R.id.ed_phone) EditText ed_phone;
    @Bind(R.id.ed_name) EditText ed_name;
    @Bind(R.id.ed_pwd_reg_1) EditText ed_pwd_1;
    @Bind(R.id.ed_pwd_reg_2) EditText ed_pwd_2;
    @Bind(R.id.ed_verify) EditText ed_verify;


    private static final String NUMBER_PATTERN = "[1][3578]\\d{9}";
    private Pattern pattern = Pattern.compile(NUMBER_PATTERN);
    private Matcher matcher;
    private boolean isPass_1 = false;
    private boolean isPass_2 = false;
    private boolean isPass_3 = false;
    private boolean isPass_4 = false;
    private boolean isPass_5 = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sign_up;
    }

    @Override
    protected void afterCreate() {
        initClick();
    }

    private void initClick() {
        btn_getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = ed_phone.getText().toString();
                if (!validatePhone(phoneNum)) {
                    phoneWrapper.setErrorEnabled(true);
                    phoneWrapper.setError("手机号好像写错了哟~");
                    isPass_1 = false;
                } else {
                    phoneWrapper.setErrorEnabled(false);
                    Toast.makeText(SignUpActivity.this, "短信已发送，请接收！", Toast.LENGTH_SHORT).show();
                    final TimeCountUtil timeCountUtil = new TimeCountUtil(SignUpActivity.this, 60000, 1000, btn_getCode);
                    timeCountUtil.start();
                    RetrofitManager.builder().getVerifyCode(phoneNum)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<CommonBean>() {
                                @Override
                                public void call(CommonBean commonBean) {
                                    String res = commonBean.getRes();
                                    if (res.equals("0")) {
//                                        timeCountUtil.onFinish();
                                        Toast.makeText(SignUpActivity.this, "服务器收到消息！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    Toast.makeText(SignUpActivity.this, throwable.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        tv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                String str_phone = ed_phone.getText().toString();
                String str_name = ed_name.getText().toString();
                String str_pwd_1 = ed_pwd_1.getText().toString();
                String str_pwd_2 = ed_pwd_2.getText().toString();
                String str_verify = ed_verify.getText().toString();

                if (!validateName(str_name)) {
                    userNameWrapper.setErrorEnabled(true);
                    userNameWrapper.setError("用户名未填写！");
                    isPass_1 = false;
                } else {
                    userNameWrapper.setErrorEnabled(false);
                    isPass_1 = true;
                }

                if (!validatePhone(str_phone)) {
                    phoneWrapper.setErrorEnabled(true);
                    phoneWrapper.setError("手机号好像写错了哟~");
                    isPass_2 = false;
                } else {
                    phoneWrapper.setErrorEnabled(false);
                    isPass_2 = true;
                }

                if (!validatePassword_1(str_pwd_1)) {
                    passwordWrapper_1.setErrorEnabled(true);
                    passwordWrapper_1.setError("密码不能小于6位哟~");
                    isPass_3 = false;
                } else {
                    passwordWrapper_1.setErrorEnabled(false);
                    isPass_3 = true;
                }

                if (!validatePassword_2(str_pwd_1, str_pwd_2)) {
                    passwordWrapper_2.setErrorEnabled(true);
                    passwordWrapper_2.setError("请您再次确认密码！");
                    isPass_4 = false;
                } else {
                    passwordWrapper_2.setErrorEnabled(false);
                    isPass_4 = true;
                }

                if (!validateVerify(str_verify)) {
                    verifyWrapper.setErrorEnabled(true);
                    verifyWrapper.setError("验证码输入错误！");
                    isPass_5 = false;
                } else {
                    verifyWrapper.setErrorEnabled(false);
                    isPass_5 = true;
                }

                if (isPass_1 && isPass_2 && isPass_3 && isPass_4 && isPass_5) {

                    doSignUp(str_name,str_phone, str_pwd_1, str_verify);
                }
            }
        });
    }

    private void doSignUp(String name, String phone,String password,String verifyCode) {

        RetrofitManager.builder().getRegResult(name,phone,password,verifyCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RegisterText>() {
                    @Override
                    public void call(RegisterText registerText) {
                        String resultCode = registerText.getRes();
                        if (resultCode.equals("0")) {
                            Toast.makeText(SignUpActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            btn_sign_up.setEnabled(false);
                            Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(SignUpActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
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

    public boolean validateName(String username){
        return !TextUtils.isEmpty(username);
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
