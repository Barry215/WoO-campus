package com.example.barry215.woo.Main.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.barry215.greendao.UserCache;
import com.example.barry215.greendao.UserCacheDao;
import com.example.barry215.woo.Base.BaseSwipeBackActivity;
import com.example.barry215.woo.Bean.CommonBean;
import com.example.barry215.woo.Bean.IInfoBean;
import com.example.barry215.woo.Bean.UserInfoBean;
import com.example.barry215.woo.DB.DaoHelper;
import com.example.barry215.woo.Main.Application.App;
import com.example.barry215.woo.Network.RetrofitManager;
import com.example.barry215.woo.R;
import com.example.barry215.woo.ui.GlideCircleTransform;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by barry215 on 2016/5/3.
 */
public class UserInfoActivity extends BaseSwipeBackActivity implements View.OnClickListener{
    @Bind(R.id.toolbar_userInfo) Toolbar toolbar;
    @Bind(R.id.userHead) ImageView userHead;
    @Bind(R.id.userName) TextView userName;
    @Bind(R.id.userSex) TextView userSex;
    @Bind(R.id.userState) TextView userState;
    @Bind(R.id.userSchool) TextView userSchool;
    @Bind(R.id.userCollege) TextView userCollege;
    @Bind(R.id.userInfo) TextView userInfo;
    @Bind(R.id.userPhone) TextView userPhone;
    @Bind(R.id.userEmail) TextView userEmail;
    @Bind(R.id.userAlipay) TextView userAlipay;
    @Bind(R.id.tv_choose_one) TextView tv_choose_one;
    @Bind(R.id.tv_choose_two) TextView tv_choose_two;
    @Bind(R.id.cancel) TextView cancel;
    @Bind(R.id.relative_layout_choose) RelativeLayout relative_layout_choose;
    @Bind(R.id.choose_layout) RelativeLayout choose_layout;

    public static final String defaultDir
            = Environment.getExternalStorageDirectory().getPath() + "/tempPic/";

    private static final int TAKE_PICTURE = 0;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int CUT_PHOTO_REQUEST_CODE = 2;

    private float dp;
    private Uri photoUri;
    private String drr = null;
    private Bitmap bitmap;

    @Override
    public int getLayoutId() {
        return R.layout.activity_userinfo;
    }

    @Override
    protected void afterCreate() {
        dp = getResources().getDimension(R.dimen.dp);
        initToolbar();
        initClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        RetrofitManager.builder().getUserInfo(App.getInstance().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserInfoBean>() {
                    @Override
                    public void call(UserInfoBean userInfoBean) {
                        if (userInfoBean.getSex().equals("0")) {
                            userSex.setText("男生");
                        } else if (userInfoBean.getSex().equals("1")) {
                            userSex.setText("女生");
                        } else {
                            userSex.setText("未知");
                        }

                        if (!userInfoBean.getHead().equals("default.png")) {
                            Glide.with(UserInfoActivity.this).load(App.LOADHEADADDRESS + userInfoBean.getHead())
                                    .transform(new GlideCircleTransform(UserInfoActivity.this))
                                    .placeholder(R.drawable.person_default_icon).into(userHead);
                        }

                        userName.setText(userInfoBean.getUsername());

                        if (userInfoBean.getUniversity().equals("未填写")) {
                            userSchool.setText("某大学");
                        } else {
                            userSchool.setText(userInfoBean.getUniversity());
                        }
                        if (userInfoBean.getCollege().equals("未填写")) {
                            userCollege.setText("某学院");
                        } else {
                            userCollege.setText(userInfoBean.getCollege());
                        }

                        if (userInfoBean.getUsersign().equals("未填写")) {
                            userInfo.setText("快介绍下你自己吧");
                        } else {
                            userInfo.setText(userInfoBean.getUsersign());
                        }

                        if (userInfoBean.getEmail().equals("未填写")){
                            userEmail.setText("请填写邮箱地址");
                        }else {
                            userEmail.setText(userInfoBean.getEmail());
                        }
                        userAlipay.setText("未绑定");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(UserInfoActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RetrofitManager.builder().getIInfo(App.getInstance().getPlayId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<IInfoBean>() {
                    @Override
                    public void call(IInfoBean iInfoBean) {
                        if (iInfoBean.getPhone().equals("")) {
                            userPhone.setText("请填写手机号码");
                        } else {
                            userPhone.setText(iInfoBean.getPhone());
                        }
                        if (iInfoBean.getState().equals("0")){
                            userState.setText("空闲");
                        }else if (iInfoBean.getState().equals("1")){
                            userState.setText("忙碌");
                        }else {
                            userState.setText("失联");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(UserInfoActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_choose_one:
                String text_one = tv_choose_one.getText().toString();
                if (text_one.equals("拍照")){
                    hideChooseLayout(v);
                    open_photo();
                }else if (text_one.equals("男生")){
                    hideChooseLayout(v);
                    modifySex("0");
                    userSex.setText("男生");
                }else if (text_one.equals("空闲")){
                    hideChooseLayout(v);
                    modifyState("0");
                    userState.setText("空闲");
                }
                break;
            case R.id.tv_choose_two:
                String text_two = tv_choose_two.getText().toString();
                if (text_two.equals("相册")){
                    hideChooseLayout(v);
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }else if(text_two.equals("女生")){
                    hideChooseLayout(v);
                    modifySex("1");
                    userSex.setText("女生");
                }else if (text_two.equals("忙碌")){
                    hideChooseLayout(v);
                    modifyState("1");
                    userState.setText("忙碌");
                }
                break;
            case R.id.cancel:
                hideChooseLayout(v);
                break;
            case R.id.userHead:
                String sdcardState = Environment.getExternalStorageState();//得到SDK卡的状态
                if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
                    hideKeyboard();
                    tv_choose_one.setText("拍照");
                    tv_choose_two.setText("相册");
                    showChooseLayout(v);
                } else {
                    Toast.makeText(UserInfoActivity.this, "sdcard已拔出，不能选择照片", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.userName:
                Intent intent1 = new Intent(UserInfoActivity.this,ModifyNameActivity.class);
                startActivity(intent1);
                break;
            case R.id.userSex:
                tv_choose_one.setText("男生");
                tv_choose_two.setText("女生");
                showChooseLayout(v);
                break;
            case R.id.userState:
                tv_choose_one.setText("空闲");
                tv_choose_two.setText("忙碌");
                showChooseLayout(v);
                break;
            case R.id.userSchool:
                Intent intent2 = new Intent(UserInfoActivity.this,ChooseSchoolActivity.class);
                startActivity(intent2);
                break;
            case R.id.userCollege:
                Intent intent3 = new Intent(UserInfoActivity.this,ChooseCollegeActivity.class);
                startActivity(intent3);
                break;
            case R.id.userInfo:
                Intent intent4 = new Intent(UserInfoActivity.this,ModifyUserInfoActivity.class);
                startActivity(intent4);
                break;
            case R.id.userPhone:
                break;
            case R.id.userEmail:
                Intent intent6 = new Intent(UserInfoActivity.this,ModifyEmailActivity.class);
                startActivity(intent6);
                break;
            case R.id.userAlipay:
                break;
        }
    }

    private void modifySex(final String sex){
        RetrofitManager.builder().modifySex(App.getInstance().getPlayId(), sex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CommonBean>() {
                    @Override
                    public void call(CommonBean commonBean) {
                        switch (commonBean.getRes()) {
                            case "0":
                                UserCacheDao userCacheDao = DaoHelper.getDaoSession(UserInfoActivity.this).getUserCacheDao();
                                UserCache userCache = userCacheDao.load(App.getInstance().getPlayId());
                                userCache.setSex(sex);
                                userCacheDao.update(userCache);
                                Toast.makeText(UserInfoActivity.this, "性别修改成功！", Toast.LENGTH_SHORT).show();
                                break;
                            case "1":
                                Toast.makeText(UserInfoActivity.this, "性别修改失败！", Toast.LENGTH_SHORT).show();
                                break;
                            case "2":
                                Toast.makeText(UserInfoActivity.this, "您未登陆！", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(UserInfoActivity.this, throwable.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void modifyState(final String state){
        RetrofitManager.builder().modifyState(App.getInstance().getPlayId(), state)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CommonBean>() {
                    @Override
                    public void call(CommonBean commonBean) {
                        switch (commonBean.getRes()) {
                            case "0":
                                UserCacheDao userCacheDao = DaoHelper.getDaoSession(UserInfoActivity.this).getUserCacheDao();
                                UserCache userCache = userCacheDao.load(App.getInstance().getPlayId());
                                userCache.setUserState(state);
                                userCacheDao.update(userCache);
                                Toast.makeText(UserInfoActivity.this, "状态修改成功！", Toast.LENGTH_SHORT).show();
                                break;
                            case "1":
                                Toast.makeText(UserInfoActivity.this, "状态修改失败！", Toast.LENGTH_SHORT).show();
                                break;
                            case "2":
                                Toast.makeText(UserInfoActivity.this, "您未登陆！", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(UserInfoActivity.this, throwable.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initClick() {
        tv_choose_one.setOnClickListener(this);
        tv_choose_two.setOnClickListener(this);
        cancel.setOnClickListener(this);
        userHead.setOnClickListener(this);
        userName.setOnClickListener(this);
        userSex.setOnClickListener(this);
        userState.setOnClickListener(this);
        userSchool.setOnClickListener(this);
        userCollege.setOnClickListener(this);
        userInfo.setOnClickListener(this);
        userPhone.setOnClickListener(this);
        userEmail.setOnClickListener(this);
        userAlipay.setOnClickListener(this);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == -1) {// 拍照
                    startPhotoZoom(photoUri);
                }
                break;
            case RESULT_LOAD_IMAGE:
                if (resultCode == RESULT_OK && null != data) {// 相册返回
                    Uri uri = data.getData();
                    if (uri != null) {
                        startPhotoZoom(uri);
                    }
                }
                break;
            case CUT_PHOTO_REQUEST_CODE:
                if (resultCode == RESULT_OK && null != data) {// 裁剪返回
//                    bitmap = Bimp.getLoacalBitmap(drr);
//                    bitmap = Bimp.createFramedPhoto(480, 480, bitmap,
//                            (int) (dp * 1.6f));
//                    userHead.setImageBitmap(bitmap);
                    File file = new File(drr);
                    uploadImage(file);
                }
                break;
        }
    }

    private void uploadImage(File file) {
        String userId = App.getInstance().getPlayId();
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);//或者"image/*"，"mimeType"
        RetrofitManager.builder().uploadImage(userId,requestBody)//userId,file.getName(),requestBody
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CommonBean>() {
                    @Override
                    public void call(CommonBean commonBean) {
                        if (commonBean.getRes().length() == 38) {
                            Glide.with(UserInfoActivity.this)
                                    .load(App.LOADHEADADDRESS + commonBean.getRes())
                                    .transform(new GlideCircleTransform(UserInfoActivity.this))
                                    .placeholder(R.drawable.person_default_icon).into(userHead);
                            UserCacheDao userCacheDao = DaoHelper.getDaoSession(UserInfoActivity.this).getUserCacheDao();
                            UserCache userCache = userCacheDao.load(App.getInstance().getPlayId());
                            userCache.setUserHead(commonBean.getRes());
                            userCacheDao.update(userCache);
                            Toast.makeText(UserInfoActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        } else if (commonBean.getRes().equals("1")) {
                            Toast.makeText(UserInfoActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(UserInfoActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(UserInfoActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void startPhotoZoom(Uri uri) {

        // 获取系统时间 然后将裁剪后的图片保存至指定的文件夹
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss", Locale.getDefault());
        String address = sDateFormat.format(new Date());

        File file = new File(defaultDir + address +".png");
        File fileDir = new File(defaultDir);//自已项目 文件夹

        if (!fileDir.exists()) {
            if(fileDir.mkdirs()){//创建文件目录，并且返回是否成功
                Log.d("PATH", " 这是拍照的照片目录" + fileDir.getAbsolutePath());
            }else{
                Log.e("ERROR","Directory not created");
            }
        }else{
            Log.d("PATH","文件夹已存在！" + fileDir.getAbsolutePath());
        }

        drr = file.getAbsolutePath();
        Uri imageUri = Uri.fromFile(file);

        final Intent intent = new Intent("com.android.camera.action.CROP");

        // 照片URL地址
        intent.setDataAndType(uri, "image/*");

        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 480);
        intent.putExtra("outputY", 480);
        // 输出路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        // 输出格式
        intent.putExtra("outputFormat",
                Bitmap.CompressFormat.JPEG.toString());
        // 不启用人脸识别
        intent.putExtra("noFaceDetection", false);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, CUT_PHOTO_REQUEST_CODE);
    }

    public void open_photo() {
        try {
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            String sdcardState = Environment.getExternalStorageState();
            File file = null;
            if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
                // 有sd卡，是否有myImage文件夹
                File fileDir = new File(defaultDir);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();   //按照路径，创建目录
                }
                // 是否有headImg文件
                file = new File(defaultDir + System.currentTimeMillis() //获取当前时间
                        + ".png");//以当前时间为文件名
            }
            if (file != null) {
                photoUri = Uri.fromFile(file);
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(openCameraIntent, TAKE_PICTURE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void showChooseLayout(View view){
        choose_layout.setVisibility(View.VISIBLE);
        Animation show_up = AnimationUtils.loadAnimation(
                this, R.anim.search_layout_in_from_down);
        relative_layout_choose.startAnimation(show_up);
    }

    public void hideChooseLayout(View view){
        Animation show_out = AnimationUtils.loadAnimation(
                this, R.anim.search_layout_out_from_up);
        relative_layout_choose.startAnimation(show_out);
        show_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                choose_layout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationStart(Animation arg0) {
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

    @Override
    protected void onDestroy() {
        // 清理图片缓存
        if(bitmap != null){
            bitmap.recycle();
        }
        super.onDestroy();
    }

}
