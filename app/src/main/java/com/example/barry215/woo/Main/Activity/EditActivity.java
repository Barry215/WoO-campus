package com.example.barry215.woo.Main.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.example.barry215.woo.Adapter.GalleryAdapter;
import com.example.barry215.woo.Adapter.TagAdapter;
import com.example.barry215.woo.Base.BaseSwipeBackActivity;
import com.example.barry215.woo.Bean.CommonBean;
import com.example.barry215.woo.FlowTagLibrary.FlowTagLayout;
import com.example.barry215.woo.FlowTagLibrary.OnTagSelectListener;
import com.example.barry215.woo.Main.Application.App;
import com.example.barry215.woo.Main.Fragment.RecommendFragment;
import com.example.barry215.woo.Network.RetrofitManager;
import com.example.barry215.woo.R;
import com.example.barry215.woo.Utils.Bimp;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by barry215 on 2016/4/12.
 */
public class EditActivity extends BaseSwipeBackActivity {
    @Bind(R.id.recyclerview_horizontal) RecyclerView recyclerView;
    @Bind(R.id.take_picture) TextView tv1;
    @Bind(R.id.select_local_picture) TextView tv2;
    @Bind(R.id.cancel) TextView tv3;
    @Bind(R.id.edit_photo_fullscreen_layout) RelativeLayout full_layout;
    @Bind(R.id.edit_photo_outer_layout) RelativeLayout out_layout;
    @Bind(R.id.flow_layout) FlowTagLayout flowTagLayout;
    @Bind(R.id.toolbar_edit) Toolbar toolbar;
    @Bind(R.id.timer) EditText ed_timer;
    @Bind(R.id.ed_reward) EditText ed_reward;
    @Bind(R.id.help_body) MaterialEditText help_body;



    public List<Bitmap> bmp = new ArrayList<Bitmap>();
    public List<String> drr = new ArrayList<String>();
    private static final int TAKE_PICTURE = 0;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int CUT_PHOTO_REQUEST_CODE = 2;
    private float dp;
    private Uri photoUri;
    private GalleryAdapter adapter;
    private TagAdapter<String> tagAdapter;
    private TimePickerView pvTime;
    private String tab_select;
    private String tab_choosed;
    public static final String defaultDir
            = Environment.getExternalStorageDirectory().getPath() + "/tempPic/";
    private List<String> ImgAddressList = new ArrayList<>();
    private String chooserId = "0";
    private ProgressDialog dialog;
    private boolean uploading = true;
    private boolean sendMsging = true;

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit;
    }

    @Override
    protected void afterCreate() {
        dp = getResources().getDimension(R.dimen.dp);
        initToolbar();
        galleryInit();
        flowTagInit();
        initTabData();
        initPicker();
        initClick();
    }

    private void initPicker() {
        pvTime = new TimePickerView(this, TimePickerView.Type.ALL);
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 1);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                ed_timer.setText(format.format(date));
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

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.send_menu) {
                    chooserId = "0";
                    if (isEmpty(help_body.getText().toString()
                            , ed_reward.getText().toString()
                            , ed_timer.getText().toString()
                            , tab_select)) {
                        Toast.makeText(EditActivity.this, "您还未完成内容的填写", Toast.LENGTH_SHORT).show();
                    } else {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                        try {
                            Date date = format.parse(ed_timer.getText().toString());
                            if (date.compareTo(new Date()) <= 0) {
                                Toast.makeText(EditActivity.this, "时间输入错误，请换一个合理的时间", Toast.LENGTH_SHORT).show();
                            } else {
                                RecommendFragment dialog = new RecommendFragment();
                                dialog.show(getSupportFragmentManager(), "dialog");
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                }
                return true;
            }
        });
    }

    private boolean isEmpty(String body, String reward, String timer, String tab_select) {
        if(!TextUtils.isEmpty(body) && !TextUtils.isEmpty(reward) && !TextUtils.isEmpty(timer) && !TextUtils.isEmpty(tab_select)){
            return false;
        }else {
            return true;
        }
    }


    private void initTabData(){
        List<String> dataSource = new ArrayList<>();
        dataSource.add("快递");
        dataSource.add("提问");
        dataSource.add("课程");
        dataSource.add("带饭");
        dataSource.add("组队");
        dataSource.add("考试");
        dataSource.add("租借");
        dataSource.add("寻找");
        dataSource.add("电脑");
        dataSource.add("求偶");
        dataSource.add("买卖");
        dataSource.add("运动");
        dataSource.add("跑路");
        dataSource.add("游戏");
        dataSource.add("其他");
        tagAdapter.onlyAddAll(dataSource);
    }

    private void updateGalleryView() {
        adapter.updateData(bmp, drr);
    }

    private void updateUploadView(){
        adapter.updateData(bmp, drr, ImgAddressList);
    }

    private void flowTagInit() {
        tagAdapter = new TagAdapter<>(this);
        flowTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        flowTagLayout.setAdapter(tagAdapter);
        flowTagLayout.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    String choose = "0";
                    for (int i : selectedList) {
                        sb.append(parent.getAdapter().getItem(i));
                        choose = i +"";
                    }
                    tab_select = sb.toString();
                    tab_choosed = choose;
//                    Snackbar.make(parent, "选择的是" + sb.toString(), Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                }
            }
        });
    }

    public void updateImgList(List<String> ImgAddressList){
        this.ImgAddressList = ImgAddressList;
    }

    private void galleryInit() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new GalleryAdapter(EditActivity.this,bmp,drr,ImgAddressList);
        adapter.setOnItemClickLitener(new GalleryAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == bmp.size()) {
                    String sdcardState = Environment.getExternalStorageState();//得到SDK卡的状态
                    if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
                        hideKeyboard();
                        showEditPhotoLayout(view);

                    } else {
                        Toast.makeText(EditActivity.this, "sdcard已拔出，不能选择照片", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent intent = new Intent(EditActivity.this, PhotoActivity.class);
                    intent.putExtra("ID", position);
                    startActivity(intent);
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    //隐藏键盘
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void initClick() {

        tv1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hideEditPhotoLayout(v);
                open_photo();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hideEditPhotoLayout(v);
                Intent i = new Intent(
                        // 相册
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hideEditPhotoLayout(v);
            }
        });

        ed_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (drr.size() < 3 && resultCode == -1) {// 拍照
                    startPhotoZoom(photoUri);
                }
                break;
            case RESULT_LOAD_IMAGE:
                if (drr.size() < 3 && resultCode == RESULT_OK && null != data) {// 相册返回
                    Uri uri = data.getData();
                    if (uri != null) {
                        startPhotoZoom(uri);
                    }
                }
                break;
            case CUT_PHOTO_REQUEST_CODE:
                if (resultCode == RESULT_OK && null != data) {// 裁剪返回
                    Bitmap bitmap = Bimp.getLoacalBitmap(drr.get(drr.size() - 1));
                    PhotoActivity.bitmap.add(bitmap);
                    bitmap = Bimp.createFramedPhoto(480, 480, bitmap,
                            (int) (dp * 1.6f));
                    bmp.add(bitmap);
                    File file = new File(drr.get(drr.size() - 1));
                    uploadImage(file);
                }
                break;
            case 65551:
                if (resultCode == 15 && data != null){
                    chooserId = data.getStringExtra("chooserId");
                }
        }
    }

    private void uploadImage(File file) {
        String userId = App.getInstance().getPlayId();
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);//或者"image/*"，"mimeType"
        RetrofitManager.builder().uploadHelpImg(userId, requestBody)//userId,file.getName(),requestBody
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CommonBean>() {
                    @Override
                    public void call(CommonBean commonBean) {
                        if (commonBean.getRes().length() == 38) {
                            ImgAddressList.add(commonBean.getRes());
                            updateUploadView();
                            Toast.makeText(EditActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        } else {
                            updateGalleryView();
                            Toast.makeText(EditActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(EditActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
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

        drr.add(file.getAbsolutePath());
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

    public void showEditPhotoLayout(View view) {
        full_layout.setVisibility(View.VISIBLE);
        Animation show_up = AnimationUtils.loadAnimation(
                this, R.anim.search_layout_in_from_down);
        out_layout.startAnimation(show_up);
    }

    public void hideEditPhotoLayout(View view) {
        Animation show_out = AnimationUtils.loadAnimation(
                this, R.anim.search_layout_out_from_up);
        out_layout.startAnimation(show_out);
        show_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                full_layout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationStart(Animation arg0) {
            }
        });
    }

    public void SubmitData(Boolean isUrgent, Boolean isSysPut){
        dialog = ProgressDialog.show(this, null, "发布中..");
        String recommended;
        String type;
        String classs = tab_choosed;
        String reward = ed_reward.getText().toString();
        String content = help_body.getText().toString();
        String endtime = ed_timer.getText().toString() + ":00";
        String pic1 = "0";
        String pic2 = "0";
        String pic3 = "0";

        switch (ImgAddressList.size()){
            case 0:
                pic1 = "0";
                pic2 = "0";
                pic3 = "0";
                break;
            case 1:
                pic1 = ImgAddressList.get(0);
                pic2 = "0";
                pic3 = "0";
                break;
            case 2:
                pic1 = ImgAddressList.get(0);
                pic2 = ImgAddressList.get(1);
                pic3 = "0";
                break;
            case 3:
                pic1 = ImgAddressList.get(0);
                pic2 = ImgAddressList.get(1);
                pic3 = ImgAddressList.get(2);
                break;
        }

        if(isUrgent){
            type = "1";
        }else {
            type = "0";
        }
        if (isSysPut){
            recommended = "0";
        }else {
            recommended = "1";
        }

        RetrofitManager.builder().createHelp(App.getInstance().getPlayId(), type, classs, reward, recommended, content, endtime, pic1, pic2, pic3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CommonBean>() {
                    @Override
                    public void call(CommonBean commonBean) {
                        if (commonBean.getRes().equals("0")) {
                            Toast.makeText(EditActivity.this, "发表成功！", Toast.LENGTH_SHORT).show();
                        }
                        uploading = false;
                        if (!sendMsging || chooserId.equals("0")) {
                            dialog.dismiss();
                            Intent intent = new Intent(EditActivity.this, MainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("msg", "信息已成功发布");
                            intent.putExtra("bundle", bundle);
                            startActivity(intent);
                            finish();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(EditActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });


        if (!chooserId.equals("0")){
            RetrofitManager.builder().sendMessage("0", App.getInstance().getPlayId(), chooserId, "您能帮我解决个问题吗？")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<CommonBean>() {
                        @Override
                        public void call(CommonBean commonBean) {
                            if (commonBean.getRes().equals("0")) {
                                Toast.makeText(EditActivity.this, "已给关注的人发送求助消息", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(EditActivity.this, "您选择的人开小差啦~", Toast.LENGTH_LONG).show();
                            }

                            sendMsging = false;
                            if (!uploading) {
                                dialog.dismiss();
                                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("msg", "信息已成功发布");
                                intent.putExtra("bundle", bundle);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Toast.makeText(EditActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        // 清理图片缓存
        for (int i = 0; i < bmp.size(); i++) {
            bmp.get(i).recycle();
        }
        for (int i = 0; i < PhotoActivity.bitmap.size(); i++) {
            PhotoActivity.bitmap.get(i).recycle();
        }
        PhotoActivity.bitmap.clear();
        bmp.clear();
        drr.clear();
        ImgAddressList.clear();
        super.onDestroy();
    }
}
