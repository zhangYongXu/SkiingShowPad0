package cn.geeksworld.skiingshow.Tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间工具
 * Created by
 */
public class TimeUtils {

    public static String dateFormat_day = "HH:mm";
    public static String dateFormat_month = "MM-dd";

    /**
     * 时间转换成字符串,默认为"yyyy-MM-dd HH:mm:ss"
     *
     * @param time 时间
     */
    public static String dateToString(long time) {
        return dateToString(time, "yyyy.MM.dd HH:mm");
    }

    /**
     * 时间转换成字符串,指定格式
     *
     * @param time   时间
     * @param format 时间格式
     */
    public static String dateToString(long time, String format) {
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * 时间转换成字符串,指定格式
     *
     * @param time   时间
     * @param format 时间格式
     */
    public static String dateToStrGmt(long time, String format) {
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
        return simpleDateFormat.format(date);
    }

    /*转为分秒*/
    public static String getMinute(int time) {
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        return format.format(time);
    }

    /*
   *计算time2减去time1的差值 差值只设置 几天 几个小时 或 几分钟
   * 根据差值返回多长之间前或多长时间后
   * */
    public static String getDistanceTime(long  time1,long time2 ) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long diff ;
        String flag;
        if(time1<time2) {
            diff = time2 - time1;
            flag="前";
        } else {
            diff = time1 - time2;
            flag="后";
        }
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff/1000-day*24*60*60-hour*60*60-min*60);
        if(day!=0)return day+"天"+flag;
        if(hour!=0)return hour+"小时"+flag;
        if(min!=0)return min+"分钟"+flag;
        return "刚刚";
    }
}
