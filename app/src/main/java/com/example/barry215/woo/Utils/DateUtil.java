package com.example.barry215.woo.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by barry215 on 2016/5/10.
 */
public class DateUtil {

    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String ONE_SECOND_LATER = "秒后";
    private static final String ONE_MINUTE_LATER = "分钟后";
    private static final String ONE_HOUR_LATER = "小时后";
    private static final String ONE_DAY_LATER = "天后";
    private static final String ONE_MONTH_LATER = "月后";
    private static final String ONE_YEAR_LATER = "年后";

    public static Boolean isDeadLine(String startDate,String deadDate){
        String startTime = startDate.substring(0,18);
        String endTime = deadDate.substring(0,18);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        long lastTime;
        long totalTime;
        try {
            Date endDate = format.parse(endTime);
            Date createDate = format.parse(startTime);
            lastTime = endDate.getTime() - new Date().getTime();
            totalTime = endDate.getTime() - createDate.getTime();
            return lastTime <= totalTime*0.2;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean isPast(String deadTime){
        String endTime = deadTime.substring(0,18);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date date = format.parse(endTime);
            return date.compareTo(new Date()) <= 0;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String parseTime(String createTime){
        try {
            String ret;
            String startTime = createTime.substring(0,18);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = dateFormat.parse(startTime);
            SimpleDateFormat dateFormat_1 = new SimpleDateFormat("HH:mm", Locale.getDefault());
            SimpleDateFormat dateFormat_2 = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
            String hourTime = dateFormat_1.format(date);
            String dayTime = dateFormat_2.format(date);
            long create = date.getTime();
            Calendar now = Calendar.getInstance();
            long ms  = 1000*(now.get(Calendar.HOUR_OF_DAY)*3600+now.get(Calendar.MINUTE)*60+now.get(Calendar.SECOND));//毫秒数
            long ms_now = now.getTimeInMillis();
            if(ms_now-create<ms){
                ret = "今天 " + hourTime;
            }else if(ms_now-create<(ms+24*3600*1000)){
                ret = "昨天 " + hourTime;
            }else if(ms_now-create<(ms+24*3600*1000*2)){
                ret = "前天 " + hourTime;
            }else{
                ret= dayTime;
            }
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parseDate(Date createDate){
        try {
            String ret;
            SimpleDateFormat dateFormat_1 = new SimpleDateFormat("HH:mm", Locale.getDefault());
            SimpleDateFormat dateFormat_2 = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
            String hourTime = dateFormat_1.format(createDate);
            String dayTime = dateFormat_2.format(createDate);
            long create = createDate.getTime();
            Calendar now = Calendar.getInstance();
            long ms  = 1000*(now.get(Calendar.HOUR_OF_DAY)*3600+now.get(Calendar.MINUTE)*60+now.get(Calendar.SECOND));//毫秒数
            long ms_now = now.getTimeInMillis();
            if(ms_now-create<ms){
                ret = "今天 " + hourTime;
            }else if(ms_now-create<(ms+24*3600*1000)){
                ret = "昨天 " + hourTime;
            }else if(ms_now-create<(ms+24*3600*1000*2)){
                ret = "前天 " + hourTime;
            }else{
                ret= dayTime;
            }
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String lastDate(String deadTime){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String lastTime = deadTime.substring(0,18);
            Date endDate = dateFormat.parse(lastTime);
            String end;
            SimpleDateFormat dateFormat_1 = new SimpleDateFormat("HH:mm", Locale.getDefault());
            SimpleDateFormat dateFormat_2 = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
            String hourTime = dateFormat_1.format(endDate);
            String dayTime = dateFormat_2.format(endDate);
            long endTime = endDate.getTime();
            Calendar nowTime = Calendar.getInstance();
            long todayTime  = 1000*(nowTime.get(Calendar.HOUR_OF_DAY)*3600 + nowTime.get(Calendar.MINUTE)*60 + nowTime.get(Calendar.SECOND));//毫秒数
            long beforeTime = nowTime.getTimeInMillis();
            long totalYearTime = getMaxYear()*ONE_DAY;
            long toYearTime = getBeforeDay()*ONE_DAY;
            if (endTime < beforeTime){
                return " (已过期)";
            }
            if(endTime - beforeTime + todayTime < ONE_DAY){
                end = "今天 " + hourTime;
            }else if(endTime - beforeTime + todayTime < 2*ONE_DAY){
                end = "明天 " + hourTime;
            }else if(endTime - beforeTime + todayTime < 3*ONE_DAY){
                end = "后天 " + hourTime;
            }else{
                end= "今年 " + dayTime;
            }
            if(endTime - beforeTime + toYearTime > totalYearTime){
                end= "明年 " + dayTime;
            }
            return end;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getBeforeDay(){
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.DAY_OF_YEAR);//获得一年内今天之前的天数
    }

    //获得本年有多少天
    public static int getMaxYear(){
        Calendar cd = Calendar.getInstance();
        cd.set(Calendar.DAY_OF_YEAR,1);//把日期设为当年第一天
        cd.roll(Calendar.DAY_OF_YEAR,-1);//把日期回滚一天，就是12月31号。
        return cd.get(Calendar.DAY_OF_YEAR);//获得一年内12月31号之前的天数
    }

    public static String strToTime(String deadTime){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        Date date;
        String resTime = null;
        try {
            date = format.parse(deadTime);
            resTime = dateToTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resTime;
    }

    public static String dateToTime(Date deadTime) {
        long delta = deadTime.getTime() - new Date().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String detailTime = dateFormat.format(deadTime);
        if (delta < 1L * ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_LATER;
        }
        if (delta < 60L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_LATER;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_LATER;
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_LATER + detailTime;
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + ONE_MONTH_LATER;
        } else {
            long years = toYears(delta);
            return (years <= 0 ? 1 : years) + ONE_YEAR_LATER;
        }
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }

}
