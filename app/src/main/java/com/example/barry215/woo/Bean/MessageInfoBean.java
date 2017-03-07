package com.example.barry215.woo.Bean;

/**
 * Created by barry215 on 2016/5/31.
 */
public class MessageInfoBean {
    private String id;
    private String state;
    private String content;
    private String username;
    private String userhead;
    private String createtime;
    private String userid;
    private String helpid;

    public String getId() {
        return id;
    }

    public String getUserid() {
        return userid;
    }

    public String getCreatetime() {
        return createtime;
    }

    public String getUserhead() {
        return userhead;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public String getState() {
        return state;
    }

    public String getHelpid() {
        return helpid;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setUserhead(String userhead) {
        this.userhead = userhead;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setHelpid(String helpid) {
        this.helpid = helpid;
    }
}
