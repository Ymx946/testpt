package com.mz.common.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 获取几个工作日后的日期工具类
 */

public class HolidayUtil {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) throws ParseException {
        //查询数据库中holiday,遍历存入list（表中每条记录存放的是假期的起止日期,遍历每条结果,并将其中的每一天都存入holiday的list中），以下为模拟假期
        List holidayList = new ArrayList();
        holidayList.add("2022-04-03");
        holidayList.add("2022-04-04");
        holidayList.add("2022-04-05");
        holidayList.add("2022-04-30");
        holidayList.add("2022-05-01");
        holidayList.add("2022-05-02");
        holidayList.add("2022-05-03");
        holidayList.add("2022-05-04");
        holidayList.add("2022-08-30");
        holidayList.add("2022-10-01");
        holidayList.add("2022-10-02");
        holidayList.add("2022-10-03");
        holidayList.add("2022-10-04");
        holidayList.add("2022-10-05");
        holidayList.add("2022-10-06");
        holidayList.add("2022-10-07");
        List workList = new ArrayList();
        workList.add("2022-10-08");
        workList.add("2022-10-09");

        //获取计划激活日期
        String scheduleActiveDate = getScheduleActiveDate(holidayList, workList);
//        System.out.println("2个工作日后,即计划激活日期为::" + scheduleActiveDate);
    }

    //获取计划激活日期
    public static String getScheduleActiveDate(List<String> list, List<String> workList) throws ParseException {
//        测试日期使用
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String dateStr = "2022-03-17 10:59:27";
//        Date today = simpleDateFormat.parse(dateStr);
        Date today = new Date();
        Date tomorrow = null;
        int delay = 1;
        int num = 2;//根据需要设置,这个值就是业务需要的n个工作日
        while (delay <= num) {
            tomorrow = getTomorrow(today);
            //当前日期+1即tomorrow,判断是否是节假日,同时要判断是否是周末,都不是则将scheduleActiveDate日期+1,直到循环num次即可
            if (!isWeekend(sdf.format(tomorrow)) && !isHoliday(sdf.format(tomorrow), list)) {
                delay++;
                today = tomorrow;
            } else if (isWeekend(sdf.format(tomorrow))) {
//                tomorrow = getTomorrow(tomorrow);
                if (workList.contains(sdf.format(tomorrow))) {
                    delay++;
                    today = tomorrow;
                } else {
                    today = tomorrow;
//                    System.out.println(sdf.format(tomorrow) + "::是周末");
                }
            } else if (isHoliday(sdf.format(tomorrow), list)) {
//                tomorrow = getTomorrow(tomorrow);
                today = tomorrow;
//                System.out.println(sdf.format(tomorrow) + "::是节假日");
            }
        }
//        System.out.println("2个工作日后,即计划激活日期为::" + sdf.format(today));
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DATE, 1);  // number of days to add
        return sdf.format(c.getTime());
    }

    /**
     * 获取明天的日期
     *
     * @param date
     * @return
     */
    public static Date getTomorrow(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +1);
        date = calendar.getTime();
        return date;
    }

    /**
     * 判断是否是weekend（注意需要排除节假日调休周末上班的情况）
     *
     * @param sdate
     * @return
     * @throws ParseException
     */
    public static boolean isWeekend(String sdate) throws ParseException {
        Date date = sdf.parse(sdate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;

    }

    /**
     * 判断是否是holiday
     *
     * @param sdate
     * @param list
     * @return
     * @throws ParseException
     */
    public static boolean isHoliday(String sdate, List<String> list) throws ParseException {
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (sdate.equals(list.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }


}
