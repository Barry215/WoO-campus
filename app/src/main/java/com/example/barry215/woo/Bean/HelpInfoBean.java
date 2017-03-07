package com.example.barry215.woo.Bean;

import java.util.List;

/**
 * Created by barry215 on 2016/5/29.
 */
public class HelpInfoBean {
    private String id;
    private String username;
    private String userid;
    private String university;
    private String sex;
    private String userhead;
    private String type;
    private String state;
    private String createtime;
    private String classs;
    private String content;
    private String endtime;
    private String reward;
    private String recommended;
    private String helpnames;
    private String mark;
    private String pic1;
    private String pic2;
    private String pic3;
    private List<RemarkInfoBean> remark;

    public String getId() {
        return id;
    }

    public List<RemarkInfoBean> getRemark() {
        return remark;
    }

    public String getPic3() {
        return pic3;
    }

    public String getPic2() {
        return pic2;
    }

    public String getPic1() {
        return pic1;
    }

    public String getMark() {
        return mark;
    }

    public String getRecommended() {
        return recommended;
    }

    public String getReward() {
        return reward;
    }

    public String getEndtime() {
        return endtime;
    }

    public String getContent() {
        return content;
    }

    public String getClasss() {
        return classs;
    }

    public String getCreatetime() {
        return createtime;
    }

    public String getState() {
        return state;
    }

    public String getType() {
        return type;
    }

    public String getUserhead() {
        return userhead;
    }

    public String getSex() {
        return sex;
    }

    public String getUniversity() {
        return university;
    }

    public String getUsername() {
        return username;
    }

    public String getUserid(){
        return userid;
    }

    public String getHelpnames() {
        return helpnames;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRemark(List<RemarkInfoBean> remark) {
        this.remark = remark;
    }

    public void setPic3(String pic3) {
        this.pic3 = pic3;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }

    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public void setRecommended(String recommended) {
        this.recommended = recommended;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setClasss(String classs) {
        this.classs = classs;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUserhead(String userhead) {
        this.userhead = userhead;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setHelpnames(String helpnames) {
        this.helpnames = helpnames;
    }
}
