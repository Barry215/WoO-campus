package com.example.barry215.woo.Main.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barry215.greendao.LoginCache;
import com.example.barry215.greendao.LoginCacheDao;
import com.example.barry215.woo.Base.BaseSwipeBackActivity;
import com.example.barry215.woo.Bean.CommonBean;
import com.example.barry215.woo.DB.DaoHelper;
import com.example.barry215.woo.Main.Application.App;
import com.example.barry215.woo.Network.RetrofitManager;
import com.example.barry215.woo.R;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by barry215 on 2016/4/14.
 */
public class LoginActivity extends BaseSwipeBackActivity {

    @Bind(R.id.usernameWrapper) TextInputLayout usernameWrapper;
    @Bind(R.id.passwordWrapper) TextInputLayout passwordWrapper;
    @Bind(R.id.ed_Login_userName) EditText ed_username;
    @Bind(R.id.ed_Login_password) EditText ed_password;
    @Bind(R.id.btn_login) Button btn_login;
    @Bind(R.id.tv_sign_up) TextView tv_sign_up;
    @Bind(R.id.tv_forgrt) TextView tv_forget;

    private static final String NUMBER_PATTERN = "[1][3578]\\d{9}";
    private Pattern pattern = Pattern.compile(NUMBER_PATTERN);
    private Matcher matcher;
    private boolean isPass_1 = false;
    private boolean isPass_2 = false;
    private ProgressDialog dialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void afterCreate() {
        initClick();
    }

    private void initClick() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                String username = ed_username.getText().toString();
                String password = ed_password.getText().toString();
                if (!validatePhone(username)) {
                    usernameWrapper.setErrorEnabled(true);
                    usernameWrapper.setError("手机号好像写错了哟~");
                    isPass_1 = false;
                } else {
                    usernameWrapper.setErrorEnabled(false);
                    isPass_1 = true;
                }
                if (!validatePassword(password)) {
                    passwordWrapper.setErrorEnabled(true);
                    passwordWrapper.setError("密码不能少于6位哟~");
                    isPass_2 = false;
                } else {
                    passwordWrapper.setErrorEnabled(false);
                    isPass_2 = true;
                }
                if (isPass_1 && isPass_2) {
                    doLogin(username,password);
                }
            }
        });

        tv_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        tv_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FindPwdActivity.class);
                startActivity(intent);
            }
        });
    }

    private void doLogin(String phone, String password) {
        dialog = ProgressDialog.show(this, null, "登陆中..");

        RetrofitManager.builder().getLoginInfo(phone, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CommonBean>() {
                    @Override
                    public void call(CommonBean commonBean) {
                        String playid = commonBean.getRes();
                        if (playid.length() == 10) {
                            Toast.makeText(LoginActivity.this, "登陆成功！", Toast.LENGTH_SHORT).show();
                            LoginCacheDao loginCacheDao = DaoHelper.getDaoSession(LoginActivity.this).getLoginCacheDao();
                            LoginCache loginCache = loginCacheDao.load((long) 1);
                            loginCache.setIsLogin(true);
                            loginCache.setLastLoginTime(new Date());
                            loginCache.setPlayId(playid);
                            loginCacheDao.update(loginCache);

                            App.getInstance().setIsLogin(true);
                            App.getInstance().setPlayId(playid);

                            dialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(LoginActivity.this, "登陆失败！", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(LoginActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
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

    public boolean validatePhone(String num){//因为用户名也支持了
//        matcher = pattern.matcher(num);
//        return matcher.matches();
        return true;
    }

    public boolean validatePassword(String password) {
        return password.length() >= 6;
    }
}
