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

import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by barry215 on 2016/4/20.
 */
public interface RetrofitService {

    @FormUrlEncoded
    @POST("servlet/Login")
    Observable<CommonBean> getLoginInfo(@Field("username") String username
            , @Field("password") String password);

    @FormUrlEncoded
    @POST("servlet/SignIn_SendMsg")
    Observable<CommonBean> getVerifyCode(@Field("phone") String userPhone);

    @FormUrlEncoded
    @POST("servlet/SignIn")
    Observable<RegisterText> getRegResult(@Field("username") String username
            ,@Field("phone") String phone
            , @Field("password") String password
            , @Field("vcode") String vcode);

    @FormUrlEncoded
    @POST("servlet/UserNameCheck")
    Observable<CommonBean> getCheckName(@Field("username") String username);

    @FormUrlEncoded
    @POST("servlet/Exit")
    Observable<CommonBean> exit(@Field("playid") String playid);

    @Multipart
    @POST("servlet/HeadUpload")
    Observable<CommonBean> uploadImage(@Part("playid") String playid,
                                       @Part("image\"; filename=\"image.png") RequestBody file);
    //如果不行，那就把String的都去掉，只留RequestBody
//    @Part("fileName") String description,

    @FormUrlEncoded
    @POST("servlet/ModifyUniversity")
    Observable<CommonBean> modifyUniversity(@Field("playid") String playid
                                            ,@Field("university") String university);

    @FormUrlEncoded
    @POST("servlet/ModifyCollege")
    Observable<CommonBean> modifyCollege(@Field("playid") String playid
                                        ,@Field("college") String college);

    @FormUrlEncoded
    @POST("servlet/ModifyUserSign")
    Observable<CommonBean> modifyUserSign(@Field("playid") String playid
                                         ,@Field("usersign") String usersign);

    @FormUrlEncoded
    @POST("servlet/ModifyEmail")
    Observable<CommonBean> modifyEmail(@Field("playid") String playid
                                         ,@Field("email") String email);

    @FormUrlEncoded
    @POST("servlet/GetUserInfo")
    Observable<UserInfoBean> getUserInfo(@Field("userid") String userid);

    @FormUrlEncoded
    @POST("servlet/I_Info")
    Observable<IInfoBean> getIInfo(@Field("playid") String playid);

    @FormUrlEncoded
    @POST("servlet/ModifyUserName")
    Observable<CommonBean> modifyUserName(@Field("playid") String playid
                                        ,@Field("username") String username);

    @FormUrlEncoded
    @POST("servlet/ModifySex")
    Observable<CommonBean> modifySex(@Field("playid") String playid
                                        ,@Field("sex") String sex);

    @FormUrlEncoded
    @POST("servlet/ModifyState")
    Observable<CommonBean> modifyState(@Field("playid") String playid
            ,@Field("state") String state);

    @Multipart
    @POST("servlet/UploadHelpImg")
    Observable<CommonBean> uploadHelpImg(@Part("playid") String playid,
                                       @Part("image\"; filename=\"image.png") RequestBody file);

    @FormUrlEncoded
    @POST("servlet/CreateHelp")
    Observable<CommonBean> createHelp(@Field("playid") String playid
                                        ,@Field("type") String type
                                        ,@Field("classs") String classs
                                        ,@Field("reward") String reward
                                        ,@Field("recommended") String recommended
                                        ,@Field("content") String content
                                        ,@Field("endtime") String endtime
                                        ,@Field("pic1") String pic1
                                        ,@Field("pic2") String pic2
                                        ,@Field("pic3") String pic3);
    @Headers(RetrofitManager.CACHE_CONTROL_AGE + RetrofitManager.CACHE_STALE_SHORT)
    @FormUrlEncoded
    @POST("servlet/GetHelpList")
    Observable<HelpListBean> getHelpList(@Field("mode") String mode
                                        ,@Field("pagevl") String pagevl
                                        ,@Field("pagenum") String pagenum);

    @FormUrlEncoded
    @POST("servlet/GetHelpInfo")
    Observable<HelpInfoBean> getHelpInfo(@Field("helpid") String helpid
                                        ,@Field("playid") String playid);

    @FormUrlEncoded
    @POST("servlet/CreateRemark")
    Observable<CommonBean> createRemark(@Field("helpid") String helpid
                                        ,@Field("playid") String playid
                                        ,@Field("replyid") String replyid
                                        ,@Field("content") String content);

    @FormUrlEncoded
    @POST("servlet/ChangeHelpShip")
    Observable<CommonBean> changeHelpShip(@Field("helpid") String helpid
                                        ,@Field("playid") String playid);

    @FormUrlEncoded
    @POST("servlet/SendMessage")
    Observable<CommonBean> sendMessage(@Field("helpid") String helpid
                                        ,@Field("playid") String playid
                                        ,@Field("receiverid") String receiverid
                                        ,@Field("msg") String msg);

    @FormUrlEncoded
    @POST("servlet/SearchHelp")
    Observable<HelpListBean> searchHelp(@Field("pagevl") String pagevl
                                    ,@Field("pagenum") String pagenum
                                    ,@Field("content") String content);

    @FormUrlEncoded
    @POST("servlet/SearchUser")
    Observable<UserListBean> searchUser(@Field("pagevl") String pagevl
                                        ,@Field("pagenum") String pagenum
                                        ,@Field("content") String content);

    @FormUrlEncoded
    @POST("servlet/ChangeThankShip")
    Observable<CommonBean> changeThankShip(@Field("userid") String userid
                                        ,@Field("playid") String playid);

    @FormUrlEncoded
    @POST("servlet/ChangeFollowShip")
    Observable<CommonBean> changeFollowShip(@Field("userid") String userid
                                        ,@Field("playid") String playid);

    @FormUrlEncoded
    @POST("servlet/GetUserFollowList")
    Observable<UserListBean> getUserFollowList(@Field("playid") String playid
                                            ,@Field("pagevl") String pagevl
                                            ,@Field("pagenum") String pagenum);

    @FormUrlEncoded
    @POST("servlet/GetUserFansList")
    Observable<UserListBean> getUserFansList(@Field("playid") String playid
                                            ,@Field("pagevl") String pagevl
                                            ,@Field("pagenum") String pagenum);

    @FormUrlEncoded
    @POST("servlet/GetUserGrades")
    Observable<UserGradesBean> getUserGrades(@Field("userid") String userid);

    @Headers(RetrofitManager.CACHE_CONTROL_AGE + RetrofitManager.CACHE_STALE_SHORT)
    @FormUrlEncoded
    @POST("servlet/GetHelpListByType")
    Observable<HelpListBean> getHelpListByType(@Field("type") String type
                                                ,@Field("pagevl") String pagevl
                                                ,@Field("pagenum") String pagenum);

    @FormUrlEncoded
    @POST("servlet/GetMyHelpList")
    Observable<HelpListBean> getMyHelpList(@Field("playid") String playid
                                            ,@Field("pagevl") String pagevl
                                            ,@Field("pagenum") String pagenum);

    @FormUrlEncoded
    @POST("servlet/GetSendHelpList")
    Observable<HelpListBean> getSendHelpList(@Field("playid") String playid
                                            ,@Field("pagevl") String pagevl
                                            ,@Field("pagenum") String pagenum);

    @FormUrlEncoded
    @POST("servlet/ModifyHelp")
    Observable<CommonBean> modifyHelp(@Field("playid") String playid
                                    ,@Field("helpid") String helpid
                                    ,@Field("reward") String reward
                                    ,@Field("endtime") String endtime
                                    ,@Field("content") String content
                                    ,@Field("type") String type);

    @FormUrlEncoded
    @POST("servlet/GetHelpPeopleList")
    Observable<UserListBean> getHelpPeopleList(@Field("helpid") String helpid
                                                ,@Field("pagevl") String pagevl
                                                ,@Field("pagenum") String pagenum);

    @FormUrlEncoded
    @POST("servlet/ChooseHelper")
    Observable<CommonBean> chooseHelper(@Field("helpid") String helpid
                                        ,@Field("userid") String userid);

    @FormUrlEncoded
    @POST("servlet/FireHelper")
    Observable<CommonBean> fireHelper(@Field("helpid") String helpid
                                    ,@Field("userid") String userid);

    @FormUrlEncoded
    @POST("servlet/FinishHelp")
    Observable<CommonBean> finishHelp(@Field("helpid") String helpid
                                        ,@Field("userid") String userid);

    @FormUrlEncoded
    @POST("servlet/MarkHelper")
    Observable<CommonBean> markHelper(@Field("helpid") String helpid
                                    ,@Field("mark") String mark);

    @FormUrlEncoded
    @POST("servlet/MessageList")
    Observable<MessageListBean> messageList(@Field("playid") String playid
                                        ,@Field("pagevl") String pagevl
                                        ,@Field("pagenum") String pagenum);

    @FormUrlEncoded
    @POST("servlet/DeleteMsg")
    Observable<CommonBean> deleteMsg(@Field("msgid") String msgid
                                    ,@Field("playid") String playid);

    @FormUrlEncoded
    @POST("servlet/HasNewMsg")
    Observable<CommonBean> hasNewMsg(@Field("playid") String playid);

    @FormUrlEncoded
    @POST("servlet/GetBackPsd_SendMsg")
    Observable<CommonBean> getBackPsd_SendMsg(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("servlet/GetBackPsd")
    Observable<CommonBean> getBackPsd(@Field("phone") String phone
                                    ,@Field("vcode") String vcode
                                    ,@Field("newpassword") String newpassword);

    @FormUrlEncoded
    @POST("servlet/GetChooseUser")
    Observable<UserInfoBean> getChooseUser(@Field("helpid") String helpid);

    @FormUrlEncoded
    @POST("servlet/ChangeMsgRead")
    Observable<CommonBean> changeMsgRead(@Field("msgid") String msgid);

    @FormUrlEncoded
    @POST("servlet/GetPeopleShip")
    Observable<PeopleShipBean> getPeopleShip(@Field("userid") String userid
                                        ,@Field("otherid") String otherid);

    @FormUrlEncoded
    @POST("servlet/DeleteHelp")
    Observable<CommonBean> deleteHelp(@Field("helpid") String helpid
                                        ,@Field("playid") String playid);
}
