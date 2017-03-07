package com.example.barry215.woo.Network;

import com.example.barry215.woo.Bean.CommonBean;
import com.example.barry215.woo.Bean.HelpInfoBean;
import com.example.barry215.woo.Bean.HelpListBean;
import com.example.barry215.woo.Bean.IInfoBean;
import com.example.barry215.woo.Bean.MessageListBean;
import com.example.barry215.woo.Bean.PeopleShipBean;
import com.example.barry215.woo.Bean.RegisterText;
import com.example.barry215.woo.Bean.UserGradesBean;
import com.example.barry215.woo.Bean.UserInfoBean;
import com.example.barry215.woo.Bean.UserListBean;
import com.example.barry215.woo.Main.Application.App;
import com.example.barry215.woo.Utils.NetUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by barry215 on 2016/4/20.
 */
public class RetrofitManager {

    public static final String BASE_URL = "http://www.ulas.top/";
    private static final int DEFAULT_TIMEOUT = 10;
    //短缓存有效期为1分钟
    public static final int CACHE_STALE_SHORT = 60;
    //长缓存有效期为7天
    public static final int CACHE_STALE_LONG = 60 * 60 * 24 * 7;

    public static final String CACHE_CONTROL_AGE = "Cache-Control: public, max-age=";

    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    public static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_LONG;
    //查询网络的Cache-Control设置，头部Cache-Control设为max-age=0时则不会使用缓存而请求服务器
    public static final String CACHE_CONTROL_NETWORK = "max-age=0";
    private static OkHttpClient mOkHttpClient;

    private RetrofitService retrofitService;

    public static RetrofitManager builder(){
        return new RetrofitManager();
    }

    private RetrofitManager(){
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        initOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        retrofitService = retrofit.create(RetrofitService.class);
    }

    // 云端响应头拦截器，用来配置缓存策略
    private Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetUtil.isNetworkConnected(App.getContext())) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            }
            Response originalResponse = chain.proceed(request);
            if (NetUtil.isNetworkConnected(App.getContext())) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder().header("Cache-Control", cacheControl)
                        .removeHeader("Pragma").build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_LONG)
                        .removeHeader("Pragma").build();
            }
        }
    };

    private void initOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (mOkHttpClient == null) {
            synchronized (RetrofitManager.class) {
                if (mOkHttpClient == null) {

                    // 指定缓存路径,缓存大小100Mb
                    Cache cache = new Cache(new File(App.getContext().getCacheDir(), "HttpCache"),
                            1024 * 1024 * 100);

                    mOkHttpClient = new OkHttpClient.Builder()
                            .cache(cache)
                            .addInterceptor(mRewriteCacheControlInterceptor)
                            .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                            .addInterceptor(interceptor)
                            .retryOnConnectionFailure(true)
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }

    public Observable<CommonBean> getLoginInfo(String userPhone,String userPwd){
        return retrofitService.getLoginInfo(userPhone, userPwd);
    }

    public Observable<CommonBean> getVerifyCode(String userPhone){
        return retrofitService.getVerifyCode(userPhone);
    }

    public Observable<RegisterText> getRegResult(String userName, String userPhone,String userPwd,String verifyCode){
        return retrofitService.getRegResult(userName, userPhone, userPwd, verifyCode);
    }

    public Observable<CommonBean> getCheckName(String userName){
        return retrofitService.getCheckName(userName);
    }

    public Observable<CommonBean> modifyUserName(String playid, String userName){
        return retrofitService.modifyUserName(playid, userName);
    }

    public Observable<CommonBean> uploadImage(String playid, RequestBody file){
        return retrofitService.uploadImage(playid, file);
    }

    public Observable<CommonBean> exit(String playid){
        return retrofitService.exit(playid);
    }

    public Observable<CommonBean> modifyUniversity(String playid,String university){
        return retrofitService.modifyUniversity(playid, university);
    }

    public Observable<CommonBean> modifyCollege(String playid,String college){
        return retrofitService.modifyCollege(playid, college);
    }

    public Observable<CommonBean> modifyUserSign(String playid,String userSign){
        return retrofitService.modifyUserSign(playid, userSign);
    }

    public Observable<CommonBean> modifyEmail(String playid,String Email){
        return retrofitService.modifyEmail(playid, Email);
    }

    public Observable<IInfoBean> getIInfo(String playid){
        return retrofitService.getIInfo(playid);
    }

    public Observable<UserInfoBean> getUserInfo(String userid){
        return retrofitService.getUserInfo(userid);
    }

    public Observable<CommonBean> modifySex(String playid, String sex){
        return retrofitService.modifySex(playid, sex);
    }

    public Observable<CommonBean> modifyState(String playid, String state){
        return retrofitService.modifyState(playid, state);
    }

    public Observable<CommonBean> uploadHelpImg(String playid, RequestBody file){
        return retrofitService.uploadHelpImg(playid, file);
    }

    public Observable<CommonBean> createHelp(String playid
                                            ,String type
                                            ,String classs
                                            ,String reward
                                            ,String recommended
                                            ,String content
                                            ,String endtime
                                            ,String pic1
                                            ,String pic2
                                            ,String pic3){
        return retrofitService.createHelp(playid, type, classs, reward, recommended, content, endtime, pic1, pic2, pic3);
    }

    public Observable<HelpListBean> getHelpList(String mode, String pageVl, String pageNum){
        return retrofitService.getHelpList(mode, pageVl, pageNum);
    }

    public Observable<HelpInfoBean> getHelpInfo(String helpid,String playid){
        return retrofitService.getHelpInfo(helpid, playid);
    }

    public Observable<CommonBean> createRemark(String helpid,String playid, String replyid, String content){
        return retrofitService.createRemark(helpid, playid, replyid, content);
    }

    public Observable<CommonBean> changeHelpShip(String helpid,String playid){
        return retrofitService.changeHelpShip(helpid, playid);
    }

    public Observable<CommonBean> sendMessage(String helpid,String playid, String receiverid, String msg){
        return retrofitService.sendMessage(helpid, playid, receiverid, msg);
    }

    public Observable<HelpListBean> searchHelp(String pagevl,String pagenum,String content){
        return retrofitService.searchHelp(pagevl, pagenum, content);
    }

    public Observable<UserListBean> searchUser(String pagevl,String pagenum,String content){
        return retrofitService.searchUser(pagevl, pagenum, content);
    }

    public Observable<CommonBean> changeThankShip(String userid,String playid){
        return retrofitService.changeThankShip(userid, playid);
    }

    public Observable<CommonBean> changeFollowShip(String userid,String playid){
        return retrofitService.changeFollowShip(userid, playid);
    }

    public Observable<UserListBean> getUserFollowList(String playid,String pagevl,String pagenum){
        return retrofitService.getUserFollowList(playid,pagevl,pagenum);
    }

    public Observable<UserListBean> getUserFansList(String playid,String pagevl,String pagenum){
        return retrofitService.getUserFansList(playid,pagevl,pagenum);
    }

    public Observable<UserGradesBean> getUserGrades(String userid){
        return retrofitService.getUserGrades(userid);
    }

    public Observable<HelpListBean> getHelpListByType(String type,String pagevl,String pagenum){
        return retrofitService.getHelpListByType(type, pagevl, pagenum);
    }

    public Observable<HelpListBean> getMyHelpList(String playid,String pagevl,String pagenum){
        return retrofitService.getMyHelpList(playid, pagevl, pagenum);
    }

    public Observable<HelpListBean> getSendHelpList(String playid,String pagevl,String pagenum){
        return retrofitService.getSendHelpList(playid, pagevl, pagenum);
    }

    public Observable<CommonBean> modifyHelp(String playid,String helpid,String reward,String endtime,String content,String type){
        return retrofitService.modifyHelp(playid, helpid, reward, endtime, content, type);
    }

    public Observable<UserListBean> getHelpPeopleList(String helpid,String pagevl,String pagenum){
        return retrofitService.getHelpPeopleList(helpid, pagevl, pagenum);
    }

    public Observable<CommonBean> chooseHelper(String helpid,String userid){
        return retrofitService.chooseHelper(helpid, userid);
    }

    public Observable<CommonBean> fireHelper(String helpid,String userid){
        return retrofitService.fireHelper(helpid, userid);
    }

    public Observable<CommonBean> finishHelp(String helpid,String userid){
        return retrofitService.finishHelp(helpid, userid);
    }

    public Observable<CommonBean> markHelper(String helpid,String mark){
        return retrofitService.markHelper(helpid, mark);
    }

    public Observable<MessageListBean> messageList(String playid,String pagevl,String pagenum){
        return retrofitService.messageList(playid, pagevl, pagenum);
    }

    public Observable<CommonBean> deleteMsg(String msgid,String playid){
        return retrofitService.deleteMsg(msgid, playid);
    }

    public Observable<CommonBean> hasNewMsg(String playid){
        return retrofitService.hasNewMsg(playid);
    }

    public Observable<CommonBean> getBackPsd_SendMsg(String phone){
        return retrofitService.getBackPsd_SendMsg(phone);
    }

    public Observable<CommonBean> getBackPsd(String phone,String vcode,String newpassword){
        return retrofitService.getBackPsd(phone, vcode, newpassword);
    }

    public Observable<UserInfoBean> getChooseUser(String helpid){
        return retrofitService.getChooseUser(helpid);
    }

    public Observable<CommonBean> changeMsgRead(String msgid){
        return retrofitService.changeMsgRead(msgid);
    }

    public Observable<PeopleShipBean> getPeopleShip(String userid,String otherid){
        return retrofitService.getPeopleShip(userid, otherid);
    }

    public Observable<CommonBean> deleteHelp(String helpid,String playid){
        return retrofitService.deleteHelp(helpid, playid);
    }
}
