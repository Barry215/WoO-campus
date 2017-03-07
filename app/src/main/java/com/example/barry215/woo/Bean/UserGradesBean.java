package com.example.barry215.woo.Bean;

/**
 * Created by barry215 on 2016/5/30.
 */
public class UserGradesBean {
    private String follow;
    private String fans;
    private String thanks;
    private String help;
    private String helped;

    public String getFollow() {
        return follow;
    }

    public String getHelped() {
        return helped;
    }

    public String getHelp() {
        return help;
    }

    public String getThanks() {
        return thanks;
    }

    public String getFans() {
        return fans;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public void setHelped(String helped) {
        this.helped = helped;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public void setThanks(String thanks) {
        this.thanks = thanks;
    }

    public void setFans(String fans) {
        this.fans = fans;
    }
}
