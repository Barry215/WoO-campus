package com.example.barry215.woo.Bean;

import java.util.List;

/**
 * Created by barry215 on 2016/5/29.
 */
public class HelpListBean {
    private String allpagenum;
    private String thispagenum;
    private List<HelpDetailBean> help;

    public String getAllpagenum() {
        return allpagenum;
    }

    public List<HelpDetailBean> getHelp() {
        return help;
    }

    public String getThispagenum() {
        return thispagenum;
    }

    public void setAllpagenum(String allpagenum) {
        this.allpagenum = allpagenum;
    }

    public void setHelp(List<HelpDetailBean> help) {
        this.help = help;
    }

    public void setThispagenum(String thispagenum) {
        this.thispagenum = thispagenum;
    }
}
