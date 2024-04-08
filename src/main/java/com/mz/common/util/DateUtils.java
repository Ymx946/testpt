package com.mz.common.util;


import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;


/**
 * 日期格式
 *
 * @author john
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    private static final Logger log = LogManager.getLogger(DateUtils.class);
    private static final String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};
    public static String YYYY = "yyyy";
    public static String YYYY_MM = "yyyy-MM";
    public static String YYYY_MM_DD = "yyyy-MM-dd";
    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";


    /**
     * 把字符串转化为Date
     *
     * @param dateStr
     * @return
     */
    public static Date parseDate(String formatStr, String dateStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.parse(dateStr);
    }

    /**
     * 把字符串转化为Date
     *
     * @param dateStr
     * @return
     */
    public static Date parseDate(String dateStr) {
        if (dateStr == null || "".equals(dateStr)) {
            return null;
        }

        SimpleDateFormat format = null;
        if (Pattern.matches("\\d{4}-\\d{1,2}-\\d{1,2}", dateStr)) {
            format = new SimpleDateFormat("yyyy-MM-dd");
        } else if (Pattern.matches("\\d{4}\\d{2}\\d{2}", dateStr)) {
            format = new SimpleDateFormat("yyyyMMdd");
        } else if (Pattern.matches("\\d{4}年\\d{2}月\\d{2}日", dateStr)) {
            format = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        } else if (Pattern.matches("\\d{4}年\\d{1,2}月\\d{1,2}日", dateStr)) {
            format = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        } else if (Pattern.matches("\\d{1,2}\\w{3}\\d{4}", dateStr)) {
            format = new SimpleDateFormat("dMMMyyyy", Locale.ENGLISH);
        } else if (Pattern.matches("\\d{1,2}-\\w{3}-\\d{4}", dateStr)) {
            format = new SimpleDateFormat("d-MMM-yyyy", Locale.ENGLISH);
        } else if (dateStr.length() > 20) {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        } else {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }

        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) {
        // 获取相差的天数
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(smdate);
        long timeInMillis1 = calendar.getTimeInMillis();
        calendar.setTime(bdate);
        long timeInMillis2 = calendar.getTimeInMillis();

        long betweenDays = (timeInMillis2 - timeInMillis1) / (1000L * 3600L * 24L);

        return Integer.parseInt(String.valueOf(betweenDays));
    }

    public static void main(String[] args) throws UnsupportedEncodingException, ParseException {
        Date newDate = DateUtil.offsetDay(DateUtil.parseDate(DateUtil.now()), -75);
        System.out.println(newDate);
        int startDays2 = daysBetween(DateUtil.parseDate("2023-11-22"), DateUtil.parseDate(DateUtil.now()));
        int startDays4 = daysBetween(DateUtil.parseDate(DateUtil.now()), DateUtil.parseDate("2023-11-22"));
        System.out.println(startDays2);
        System.out.println(startDays4);
        // int i = 7 / 7;
        // int y = 7 % 7;
        // double dd = startDays4 / Double.valueOf(7);
        // System.out.println(i);
        // System.out.println(y);
        // System.out.println(dd);
    }


    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的字符串
     * @return
     */
    public static Date timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return null;
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return string2Date(sdf.format(new Date(Long.valueOf(seconds + "000"))), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * Construct the Date object according to the string and the specifed format.
     *
     * @param dateValue
     * @param dateFormat
     * @return Date
     */
    public static Date string2Date(String dateValue, String dateFormat) {
        return string2Date(dateValue, dateFormat, null);
    }

    /**
     * Parse string to date according to the specified format,if exception occurs,return the specified default value
     *
     * @param dateValue
     * @param dateFormat
     * @param defaultValue
     * @return date
     */
    public static Date string2Date(String dateValue, String dateFormat, Date defaultValue) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            date = sdf.parse(dateValue);
        } catch (Exception eDate) {
            return defaultValue;
        }
        return date;
    }

    /**
     * 计算日期星期几
     *
     * @param date 日期字符串（yyyy-MM-dd）
     * @return 第几天（星期一作为第一特）
     * @throws ParseException
     */
    public static int dayOfWeekLocal(String date) {
        int weekDay = DateUtil.dayOfWeek(DateUtil.parseDate(date));
        if (weekDay == 1) {
            weekDay = 7;
        } else {
            weekDay = weekDay - 1;
        }
        return weekDay;
    }

    /**
     * 计算日期星期几
     *
     * @param weekDay 第几天
     * @return 星期几
     * @throws ParseException
     */
    public static String dayOfWeekStr(int weekDay) {
        String weekDayStr = "";
        if (weekDay == 1) {
            weekDayStr = "星期一";
        } else if (weekDay == 2) {
            weekDayStr = "星期二";
        } else if (weekDay == 3) {
            weekDayStr = "星期三";
        } else if (weekDay == 4) {
            weekDayStr = "星期四";
        } else if (weekDay == 5) {
            weekDayStr = "星期五";
        } else if (weekDay == 6) {
            weekDayStr = "星期六";
        } else if (weekDay == 7) {
            weekDayStr = "星期日";
        }
        return weekDayStr;
    }

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor) {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor) {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }
}