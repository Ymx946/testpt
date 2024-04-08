package com.mz.common.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DateUtil {
    public final static String START_HOUR = " 00:00:00";
    public final static String END_HOUR = " 23:59:59";
    /**
     * 时间间隔：日
     */
    public final static int DATE_INTERVAL_DAY = 1;
    /**
     * 时间间隔：周
     */
    public final static int DATE_INTERVAL_WEEK = 2;
    /**
     * 时间间隔：月
     */
    public final static int DATE_INTERVAL_MONTH = 3;
    /**
     * 时间间隔：年
     */
    public final static int DATE_INTERVAL_YEAR = 4;
    /**
     * 时间间隔：小时
     */
    public final static int DATE_INTERVAL_HOUR = 5;
    /**
     * 时间间隔：分钟
     */
    public final static int DATE_INTERVAL_MINUTE = 6;
    /**
     * 时间间隔：秒
     */
    public final static int DATE_INTERVAL_SECOND = 7;
    /**
     * 时间格式：年月日
     */
    public final static String DATE_FORMAT_YMD = "yyyy-MM-dd";
    /**
     * 时间格式：年月日时分秒
     */

    public final static String DATE_FORMAT_YEAR = "yyyy";
    public final static String DATE_FORMAT_MONTH = "MM";
    public final static String DATE_FORMAT_DAY = "dd";
    public final static String DATE_FORMAT_YM = "yyyy-MM";
    public final static String DATE_FORMAT_MD = "MM-dd";
    public final static String DATE_FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";
    public final static String DATE_FORMAT_YMDHMS_XG = "yyyy/MM/dd HH:mm:ss";
    public final static String DATE_FORMAT_YMDHMS_TZ = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public final static String DATE_FORMAT_YMDHMS_CN = "yyyy年MM月dd HH:mm:ss";
    public final static String DATE_FORMAT_YMDHM_CN = "yyyy年MM月dd HH:mm";
    public final static String DATE_FORMAT_YMD_CN = "yyyy年MM月dd";
    public final static String DATE_FORMAT_YM_CN = "yyyy年MM月";
    public final static String DATE_FORMAT_MDHM_CN = "MM月dd HH:mm";
    public final static String DATE_FORMAT_YMDHH = "yyyy-MM-dd HH";
    public final static String DATE_FORMAT_HHMM = "HH:mm";
    public final static String DATE_FORMAT_YMDHMS2 = "yyyyMMddHHmmss";
    public final static String DATE_FORMAT_YMD2 = "yyyyMMdd";
    public final static String DATE_FORMAT_YMDHMSsss = "yyyyMMddHHmmssSSS";
    public final static String DATE_FORMAT_YMDHM = "yyyyMMddHHmmss";
    private static final Logger LOG = LoggerFactory.getLogger(DateUtil.class);
    static int[] monthDay = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    /**
     * LocalDateTime 转成Date
     *
     * @param localDateTime
     * @return
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime date2LocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();

        ZonedDateTime zonedDateTime = instant.atZone(zoneId);
        return zonedDateTime.toLocalDateTime();
    }

    /**
     * 获得时间
     *
     * @param date       时间
     * @param dateFormat 时间格式
     * @return 时间
     * @author sunju @creationDate. 2012-7-31 下午03:06:05
     */
    public static Date getDate(Date date, String dateFormat) {
        return dateFormat(dateFormat(date, dateFormat), dateFormat);
    }

    /**
     * 获得当前日期(年月日)
     *
     * @return 当前时间（yyyy-MM-dd）
     * @author sunju @creationDate. 2010-8-27 下午05:11:23
     */
    public static Date getNowDate() {
        return dateFormat(dateFormat(new Date(), DATE_FORMAT_YMD), DATE_FORMAT_YMD);
    }

    /**
     * 获取当前时间字符串(年月日)
     *
     * @return 时间字符串
     * @author sunju @creationDate. 2011-5-4 下午08:22:34
     */
    public static String getNowStringDate() {
        return dateFormat(new Date(), DATE_FORMAT_YMD);
    }

    public static String getNowStringYMDHMSDate() {
        return dateFormat(new Date(), DATE_FORMAT_YMDHMS2);
    }

    public static String getNowStringYMDHMSSDate() {
        return dateFormat(new Date(), DATE_FORMAT_YMDHMSsss);
    }

    /**
     * 获得当前时间(年月日时分秒)
     *
     * @return 当前时间（yyyy-MM-dd HH:mm:ss）
     * @author sunju @creationDate. 2010-8-27 下午05:12:57
     */
    public static Date getNowTime() {
        return dateFormat(dateFormat(new Date(), DATE_FORMAT_YMDHMS), DATE_FORMAT_YMDHMS);
    }

    /**
     * 获取当前时间字符串(年月日时分秒)
     *
     * @return 时间字符串
     * @author sunju @creationDate. 2014-3-10 下午03:16:42
     */
    public static String getNowStringTime() {
        return dateFormat(new Date(), DATE_FORMAT_YMDHMS);
    }

    /**
     * 获得明天的日期字符串(格式年月日)
     *
     * @return 明天的日期
     * @author sunju @creationDate. 2011-5-4 下午08:19:28
     */
    public static String getTomorrowStringDate() {
        return dateFormat(getTomorrowTime(), DATE_FORMAT_YMD);
    }

    /**
     * 获得明天的日期字符串(格式年月日)
     *
     * @return 明天的日期
     * @author sunju @creationDate. 2011-5-4 下午08:19:28
     */
    public static String getAfterTomorrowStringDate() {
        return dateFormat(getAfterTomorrowTime(), DATE_FORMAT_YMD);
    }

    /**
     * 获得明天的日期(年月日)
     *
     * @return 明天的日期
     * @author sunju @creationDate. 2011-5-4 下午08:19:57
     */
    public static Date getTomorrowDate() {
        return dateAdd(DATE_INTERVAL_DAY, getNowDate(), 1);
    }

    /**
     * 获得明天的时间(年月日时分秒)
     *
     * @return 明天的时间
     * @author sunju @creationDate. 2011-5-4 下午08:20:19
     */
    public static Date getTomorrowTime() {
        return dateAdd(DATE_INTERVAL_DAY, getNowTime(), 1);
    }

    /**
     * 获得前几分钟的时间(年月日时分秒)
     */
    public static Date getBeforeMinuteTime(int num) {
        return dateAdd(DATE_INTERVAL_MINUTE, getNowTime(), num);
    }

    /**
     * 后天
     *
     * <ul>
     * <li><b>原因：<br/>
     * <p>
     * [2014-10-31]gaozhanglei<br/>
     *
     * @return TODO
     * </p>
     * </li>
     * </ul>
     */
    public static Date getAfterTomorrowTime() {
        return dateAdd(DATE_INTERVAL_DAY, getNowTime(), 2);
    }

    /**
     * 获得昨天的日期
     *
     * @return 昨天的日期
     * @author sunju @creationDate. 2013-10-22 下午03:54:48
     */
    public static Date getYesterdayDate() {
        return dateAdd(DATE_INTERVAL_DAY, getNowDate(), -1);
    }

    /**
     * 获取当年的第一天
     *
     * @return
     */
    public static Date getCurrYearFirst() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    /**
     * 获取当年的最后一天
     *
     * @return
     */
    public static Date getCurrYearLast() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearLast(currentYear);
    }

    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取某年最后一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearLast(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();

        return currYearLast;
    }

    /**
     * 获取上个月第一天
     *
     * @return 日期
     * @author sunju @creationDate. 2013-10-22 下午03:45:53
     */
    public static Date getPrevMonthFirst() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, -1);
        lastDate.set(Calendar.DAY_OF_MONTH, 1);// 设为当前月的1号
        return getDate(lastDate.getTime(), DATE_FORMAT_YMD);
    }

    /**
     * 获取上个月最后一天
     *
     * @return 日期
     * @author sunju @creationDate. 2013-10-22 下午03:45:53
     */
    public static Date getPrevMonthEnd() {
        Calendar lastDate = Calendar.getInstance();
        //当前日期设置为指定日期
        lastDate.setTime(new Date());
        //指定日期月份减去一
        lastDate.add(Calendar.MONTH, -1);
        //指定日期月份减去一后的 最大天数
        lastDate.set(Calendar.DATE, lastDate.getActualMaximum(Calendar.DATE));
        //获取上给月最后一天的日期
        return getDate(lastDate.getTime(), DATE_FORMAT_YMD);
    }

    /**
     * 获取当月第一天
     *
     * @return 日期
     * @author sunju @creationDate. 2013-10-22 下午03:45:53
     */
    public static Date getMonthFirst() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, 0);
        lastDate.set(Calendar.DAY_OF_MONTH, 1);// 设为当前月的1号
        return getDate(lastDate.getTime(), DATE_FORMAT_YMD);
    }

    /**
     * 获得下个月第一天的日期
     *
     * @return 日期
     * @author sunju @creationDate. 2013-10-22 下午03:52:38
     */
    public static Date getNextMonthFirst() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, 1); // 加一个月
        lastDate.set(Calendar.DATE, 1); // 把日期设置为当月第一天
        return getDate(lastDate.getTime(), DATE_FORMAT_YMD);
    }

    public static Date getNextMonthFirst(Date date) {
        Calendar lastDate = Calendar.getInstance();
        lastDate.setTime(date);
        lastDate.add(Calendar.MONTH, 1); // 加一个月
        lastDate.set(Calendar.DATE, 1); // 把日期设置为当月第一天
        return getDate(lastDate.getTime(), DATE_FORMAT_YMD);
    }

    public static Date getMonthFirst(Date date) {
        Calendar lastDate = Calendar.getInstance();
        lastDate.setTime(date);
        // 加一个月
        lastDate.set(Calendar.DATE, 1); // 把日期设置为当月第一天
        return getDate(lastDate.getTime(), DATE_FORMAT_YMD);
    }

    public static Date getMonthEnd() {
        Calendar calendar = Calendar.getInstance();
        // 设置为当月的第一天
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        // 加一个月
        calendar.add(Calendar.MONTH, 1);
        // 减一天，获取当月的最后一天
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return getDate(calendar.getTime(), DATE_FORMAT_YMD);
    }

    /**
     * 取得当前星期几
     *
     * @param date 时间
     * @return 星期
     * @author sunju @creationDate. 2010-9-20 下午05:34:36
     */
    public static String getWeekOfDate(Date date) {
        if (date == null) {
            return null;
        }
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    /**
     * 或得当前日期是一年中的第几周
     *
     * @param date 时间
     * @return 周
     * @author wxl @creationDate. 2015-10-26 下午05:29:36
     */
    public static int getWeekNumOfDate(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.get(Calendar.WEEK_OF_YEAR);
        int weeknum = cal.get(Calendar.WEEK_OF_YEAR);
        return weeknum;
    }

    /**
     * 获取上一周第一天
     *
     * @return 日期
     * @author sunju @creationDate. 2013-10-22 下午03:45:53
     */
    public static Date getPrevWeekFirst() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.setFirstDayOfWeek(Calendar.MONDAY);
        lastDate.add(Calendar.DATE, -1 * 7);
        lastDate.set(Calendar.HOUR_OF_DAY, 0);
        lastDate.set(Calendar.MINUTE, 0);
        lastDate.set(Calendar.SECOND, 0);
        lastDate.set(Calendar.MILLISECOND, 0);
        lastDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return getDate(lastDate.getTime(), DATE_FORMAT_YMD);
    }

    /**
     * 获取上一周最后一天
     *
     * @return 日期
     * @author sunju @creationDate. 2013-10-22 下午03:45:53
     */
    public static Date getPrevWeekEnd() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.setFirstDayOfWeek(Calendar.MONDAY);
        lastDate.add(Calendar.DATE, -1 * 7);
        lastDate.set(Calendar.HOUR_OF_DAY, 0);
        lastDate.set(Calendar.MINUTE, 0);
        lastDate.set(Calendar.SECOND, 0);
        lastDate.set(Calendar.MILLISECOND, 0);
        lastDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return getDate(lastDate.getTime(), DATE_FORMAT_YMD);
    }

    /**
     * 获取当周第一天
     *
     * @return 日期
     * @author sunju @creationDate. 2013-10-22 下午03:45:53
     */
    public static Date getCurWeekFirst() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.setFirstDayOfWeek(Calendar.MONDAY);
        lastDate.set(Calendar.HOUR_OF_DAY, 0);
        lastDate.set(Calendar.MINUTE, 0);
        lastDate.set(Calendar.SECOND, 0);
        lastDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return getDate(lastDate.getTime(), DATE_FORMAT_YMD);
    }

    /**
     * 获取当周最后一天
     *
     * @return 日期
     * @author sunju @creationDate. 2013-10-22 下午03:45:53
     */
    public static Date getCurWeekEnd() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.setFirstDayOfWeek(Calendar.MONDAY);
        lastDate.set(Calendar.HOUR_OF_DAY, 23);
        lastDate.set(Calendar.MINUTE, 59);
        lastDate.set(Calendar.SECOND, 59);
        lastDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return getDate(lastDate.getTime(), DATE_FORMAT_YMD);
    }

    /**
     * 获取指定周的第一天
     *
     * @return 日期
     * @author sunju @creationDate. 2013-10-22 下午03:45:53
     */
    public static Date getWeekFirst(Date date) {
        Calendar lastDate = Calendar.getInstance();
        lastDate.setTime(date);
        int d = 0;
        if (lastDate.get(Calendar.DAY_OF_WEEK) == 1) {
            d = -6;
        } else {
            d = 2 - lastDate.get(Calendar.DAY_OF_WEEK);
        }
        lastDate.add(Calendar.DAY_OF_WEEK, d);
        return getDate(lastDate.getTime(), DATE_FORMAT_YMD);
    }

    /**
     * 获取指定周的最后一天
     *
     * @return 日期
     * @author sunju @creationDate. 2013-10-22 下午03:45:53
     */
    public static Date getWeekEnd(Date date) {
        Calendar lastDate = Calendar.getInstance();
        lastDate.setTime(date);
        int d = 0;
        if (lastDate.get(Calendar.DAY_OF_WEEK) == 1) {
            d = -6;
        } else {
            d = 2 - lastDate.get(Calendar.DAY_OF_WEEK);
        }
        lastDate.add(Calendar.DAY_OF_WEEK, d);
        lastDate.add(Calendar.DAY_OF_WEEK, 6);
        return getDate(lastDate.getTime(), DATE_FORMAT_YMD);
    }

    /**
     * 时间类型转换返回字符串
     *
     * @param date       时间
     * @param dateFormat 时间格式
     * @return 转换后的时间字符串
     * @author sunju @creationDate. 2010-8-27 下午05:18:37
     */
    public static String dateFormat(Date date, String dateFormat) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        return format.format(date);
    }

    /**
     * 字符串时间类型转换返回Date类型
     *
     * @param date       字符串时间
     * @param dateFormat 时间格式
     * @return 转换后的时间
     * @author sunju @creationDate. 2010-8-27 下午05:23:35
     */
    public static Date dateFormat(String date, String dateFormat) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        try {
            return format.parse(date);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return null;
        }
    }

    /**
     * 加时间
     *
     * @param interval 增加项，可以是天数、月份、年数、时间、分钟、秒
     * @param date     时间
     * @param num      加的数目
     * @return 时间加后的时间
     * @author sunju @creationDate. 2010-8-27 下午05:28:06
     */
    public static Date dateAdd(int interval, Date date, int num) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (interval) {
            case DATE_INTERVAL_DAY:
                calendar.add(Calendar.DATE, num);
                break;
            case DATE_INTERVAL_WEEK:
                calendar.add(Calendar.WEEK_OF_MONTH, num);
                break;
            case DATE_INTERVAL_MONTH:
                calendar.add(Calendar.MONTH, num);
                break;
            case DATE_INTERVAL_YEAR:
                calendar.add(Calendar.YEAR, num);
                break;
            case DATE_INTERVAL_HOUR:
                calendar.add(Calendar.HOUR, num);
                break;
            case DATE_INTERVAL_MINUTE:
                calendar.add(Calendar.MINUTE, num);
                break;
            case DATE_INTERVAL_SECOND:
                calendar.add(Calendar.SECOND, num);
                break;
            default:
        }
        return calendar.getTime();
    }

    /**
     * 两个时间时间差[前面时间和比较时间比,小于比较时间返回负数]
     *
     * @param interval 比较项，可以是天数、月份、年数、时间、分钟、秒
     * @param date     时间
     * @param compare  比较的时间
     * @return 时间差(保留两位小数点, 小数点以后两位四舍五入)
     * @author sunju @creationDate. 2010-8-27 下午05:26:13
     */
    public static double getDateDiff(int interval, Date date, Date compare) {
        if (date == null || compare == null) {
            return 0;
        }
        double result = 0;
        double time = 0;
        Calendar calendar = null;
        switch (interval) {
            case DATE_INTERVAL_DAY:
                time = date.getTime() - compare.getTime();
                result = time / 1000d / 60d / 60d / 24d;
                break;
            case DATE_INTERVAL_HOUR:
                time = date.getTime() - compare.getTime();
                result = time / 1000d / 60d / 60d;
                break;
            case DATE_INTERVAL_MINUTE:
                time = date.getTime() / 1000d / 60d;
                result = time - compare.getTime() / 1000d / 60d;
                break;
            case DATE_INTERVAL_MONTH:
                calendar = Calendar.getInstance();
                calendar.setTime(date);
                time = calendar.get(Calendar.YEAR) * 12d;
                calendar.setTime(compare);
                time -= calendar.get(Calendar.YEAR) * 12d;
                calendar.setTime(date);
                time += calendar.get(Calendar.MONTH);
                calendar.setTime(compare);
                result = time - calendar.get(Calendar.MONTH);
                break;
            case DATE_INTERVAL_SECOND:
                time = date.getTime() - compare.getTime();
                result = time / 1000d;
                break;
            case DATE_INTERVAL_WEEK:
                calendar = Calendar.getInstance();
                calendar.setTime(date);
                time = calendar.get(Calendar.YEAR) * 52d;
                calendar.setTime(compare);
                time -= calendar.get(Calendar.YEAR) * 52d;
                calendar.setTime(date);
                time += calendar.get(Calendar.WEEK_OF_YEAR);
                calendar.setTime(compare);
                result = time - calendar.get(Calendar.WEEK_OF_YEAR);
                break;
            case DATE_INTERVAL_YEAR:
                calendar = Calendar.getInstance();
                calendar.setTime(date);
                time = calendar.get(Calendar.YEAR);
                calendar.setTime(compare);
                result = time - (double) calendar.get(Calendar.YEAR);
                break;
            default:
                break;
        }
        return new BigDecimal(result).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 获取时间差[前面时间和比较时间比,小于比较时间返回负数]
     *
     * @param level   返回时间等级(1:返回天;2:返回天-小时;3:返回天-小时-分4:返回天-小时-分-秒;)
     * @param date    时间
     * @param compare 比较的时间
     * @return 时间差(保留两位小数点, 小数点以后两位四舍五入)
     * @author sunju @creationDate. 2010-9-1 下午04:36:07
     */
    public static String getDateBetween(Integer level, Date date, Date compare) {
        if (date == null || compare == null) {
            return null;
        }
        long s = BigDecimal.valueOf(getDateDiff(DATE_INTERVAL_SECOND, date, compare)).setScale(2, RoundingMode.HALF_UP).longValue();
        int ss = 1;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = s / dd;
        long hour = (s - day * dd) / hh;
        long minute = (s - day * dd - hour * hh) / mi;
        long second = (s - day * dd - hour * hh - minute * mi) / ss;
        String flag = (day < 0 || hour < 0 || minute < 0 || second < 0) ? "-" : "";
        day = Math.abs(day);
        hour = Math.abs(hour);
        minute = Math.abs(minute);
        second = Math.abs(second);
        StringBuilder result = new StringBuilder(flag);
        switch (level) {
            case 1:
                if (day != 0) {
                    result.append(day).append("天");
                }
                break;
            case 2:
                if (day != 0) {
                    result.append(day).append("天");
                }
                if (hour != 0) {
                    result.append(hour).append("小时");
                }
                break;
            case 3:
                if (day != 0) {
                    result.append(day).append("天");
                }
                if (hour != 0) {
                    result.append(hour).append("小时");
                }
                if (minute != 0) {
                    result.append(minute).append("分");
                }
                break;
            case 4:
                if (day != 0) {
                    result.append(day).append("天");
                }
                if (hour != 0) {
                    result.append(hour).append("小时");
                }
                if (minute != 0) {
                    result.append(minute).append("分");
                }
                if (second != 0) {
                    result.append(second).append("秒");
                }
                break;
            default:
                break;
        }
        return result.toString();
    }

    /**
     * 时间是否是今天
     *
     * @param date 时间
     * @return 布尔
     * @author sunju @creationDate. 2011-5-4 下午08:24:58
     */
    public static boolean isToday(Date date) {
        if (date == null) {
            return false;
        }
        return getNowStringDate().equals(dateFormat(date, DATE_FORMAT_YMD));
    }

    /**
     * 时间是否合法
     *
     * @param date       时间
     * @param dateFormat 时间格式
     * @return
     * @author sunju @creationDate. 2012-6-19 下午01:07:59
     */
    public static boolean isValidDate(String date, String dateFormat) {
        try {
            new SimpleDateFormat(dateFormat).parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据年月获取这个月的天数
     */
    public static int getMonthDays(String date) {
        String year = date.split("-")[0];
        String month = date.split("-")[1];
        int y = Integer.parseInt(year);
        int m = Integer.parseInt(month);
        if ((y % 4 == 0 && y % 100 != 0) || y % 400 == 0) {
            if (m == 2) {
                return 29;
            } else {
                return monthDay[m - 1];
            }
        } else {
            return monthDay[m - 1];
        }
    }

    /**
     * Description: 返回当前日期和时间 yyyy-MM-dd <BR>
     */
    public static String getNowDateYMD() {
        return LocalDateTime.now(ZoneOffset.of("+8")).format(DateTimeFormatter.ofPattern(DateUtil.DATE_FORMAT_YMD));
    }

    /**
     * Description: 返回当前日期和时间 yyyyMMdd <BR>
     */
    public static String getNowDateYMD2() {
        return LocalDateTime.now(ZoneOffset.of("+8")).format(DateTimeFormatter.ofPattern(DateUtil.DATE_FORMAT_YMD2));
    }


    /**
     * 字符串转时间
     */
    public static LocalDateTime parseDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMAT_YMDHMS));
    }

    /**
     * 当前时间加减多少天
     */
    public static String getAddOrSubDay(long day) {
        LocalDateTime today = LocalDateTime.now(ZoneOffset.of("+8"));
        if (day > 0) {
            today = today.plusDays(day);
        } else {
            today = today.minusDays(day * -1);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return today.format(formatter);
    }

    /**
     * 根据日期加减多少天
     */
    public static String getAddOrSubDay(LocalDateTime date, long day) {
        if (day > 0) {
            date = date.plusDays(day);
        } else {
            date = date.minusDays(day * -1);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    /**
     * 根据日期加减多少月
     */
    public static String getAddOrSubMonth(LocalDateTime date, int month) {
        if (month > 0) {
            date = date.plusMonths(month);
        } else {
            date = date.minusMonths(month * -1);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }


    /**
     * Date 转String “yyyy-MM-dd”
     */
    public static String getStringDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    /**
     * Date 转String 指定格式
     */
    public static String getStringDate(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    /**
     * Date 转String “MM-dd”
     */
    public static String getStringDateMD(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
        return formatter.format(date);
    }

    /**
     * String 转 Date “yyyy-MM-dd”
     */
    public static Date getDateString(String date) {
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf2.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * String 转 Date “yyyy-MM-dd”
     */
    public static Date getDateString(String date, String format) {
        SimpleDateFormat sdf2 = new SimpleDateFormat(format);
        try {
            return sdf2.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取本周的开始时间
     */
    @SuppressWarnings("unused")
    public static String getBeginDayOfWeek() {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);
        return getStringDate(cal.getTime());
    }

    // 获取本周的结束时间
    public static String getEndDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDateString(getBeginDayOfWeek()));
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getStringDate(weekEndSta);
    }

    /**
     * 获取今年是哪一年
     */
    public static Integer getNowYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(1));
    }

    /**
     * 获取本月是哪一月
     */
    public static int getNowMonth() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(2) + 1;
    }

    /**
     * 获取本月的开始时间
     */
    public static String getBeginDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        return getStringDate(calendar.getTime());
    }

    /**
     * 获取本月的结束时间
     */
    public static String getEndDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 1, day);
        return getStringDate(calendar.getTime());
    }

    /**
     * 获取两天之间的天数
     */
    public static int getDaysBetween(String beginDayOfMonth, String endDayOfMonth) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date smdate = getDateString(beginDayOfMonth);
            Date bdate = getDateString(endDayOfMonth);
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bdate);
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);
            return Integer.parseInt(String.valueOf(between_days));
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取当前季度开始时间
     */
    public static String getBeginDayOfQuarter() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = null;
        try {
            switch (currentMonth) {
                case 1:
                case 2:
                case 3:
                    c.set(Calendar.MONTH, 0);
                    break;
                case 4:
                case 5:
                case 6:
                    c.set(Calendar.MONTH, 3);
                    break;
                case 7:
                case 8:
                case 9:
                    c.set(Calendar.MONTH, 4);
                    break;
                case 10:
                case 11:
                case 12:
                    c.set(Calendar.MONTH, 9);
                    break;
            }
            c.set(Calendar.DATE, 1);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getStringDate(now);
    }

    /**
     * 获取当前季度结束时间
     */
    public static String getEndDayOfQuarter() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDateString(getBeginDayOfQuarter()));
        cal.add(Calendar.MONTH, 3);
        return getStringDate(cal.getTime());
    }

    /*
     * 将时间转换为时间戳 2017-11-27 03:16:03
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_YMDHMS);
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime() / 1000L;
        res = String.valueOf(ts);
        return res;
    }

    /*
     * 将时间转换为时间戳 2017-11-27T03:16:03Z
     */
    public static Date stampTzToDate(String s) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT_YMDHMS_TZ);
        Date date = sf.parse(s);
        return date;
    }

    /*
     * 将时间转换为时间戳 2017-11-27T03:16:03Z
     */
    public static String dateToStampTz(Date date) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT_YMDHMS_TZ);
        return sf.format(date);
    }

    /**
     * 指定时间段内的每一天
     *
     * @param dBegin
     * @param dEnd
     * @return
     */
    public static List<Date> findDates(Date dBegin, Date dEnd) {
        List<Date> dateList = new ArrayList<Date>();
        dateList.add(dBegin);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            dateList.add(calBegin.getTime());
        }
        return dateList;
    }

    public static long getMilli(LocalDateTime memberEndTime, LocalDateTime now) {
        long endMilli = memberEndTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long nowMilli = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return endMilli - nowMilli;
    }

}