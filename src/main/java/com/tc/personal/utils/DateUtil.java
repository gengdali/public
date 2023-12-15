package com.tc.personal.utils;


import cn.hutool.core.date.DateTime;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author lyh
 */
public class DateUtil {
    private static final String[] PATTERNS = {"yyyy-MM-dd",
            "yyyy-MM-dd HH:mm",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-M-d",
            "yyyyMMdd",
            "yyyyMM",
            "yyyy年M月d日",
            "yyyy.M.d",
            "yyyy.MM.dd",
            "yyyy年M月",
            "yyyyMMddHHmmss",
            "yyyy"
    };

    /**
     * Date to String
     *
     * @param date
     * @param pattern 0-"yyyy-MM-dd"
     *                1-"yyyy-MM-dd HH:mm"
     *                2-"yyyy-MM-dd HH:mm:ss"
     *                3-"yyyy-M-d"
     *                4-"yyyyMMdd"
     *                5-"yyyyMM"
     *                6-"yyyy年M月d日"
     *                7-"yyyy.M.d"
     *                8-"yyyy.MM.dd"
     *                9-"yyyy年M月"
     *                10-"yyyyMMddHHmmss"
     * @return
     */
    public static String DateToString(Date date, int pattern) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat df = new SimpleDateFormat();
        df.applyPattern(PATTERNS[pattern]);
        return df.format(date);
    }

    /**
     * Timestamp to String
     *
     * @param date
     * @param pattern 0-"yyyy-MM-dd"
     *                1-"yyyy-MM-dd HH:mm"
     *                2-"yyyy-MM-dd HH:mm:ss"
     *                3-"yyyy-M-d"
     *                4-"yyyyMMdd"
     *                5-"yyyyMM"
     *                6-"yyyy年M月d日"
     *                7-"yyyy.M.d"
     *                8-"yyyy.MM.dd"
     *                9-"yyyy年M月"
     *                10-"yyyyMMddHHmmss"
     * @return
     */
    public static String TimestampToString(Timestamp date, int pattern) {
        return DateToString(TimestampToDate(date), 1);
    }

    public static Date TimestampToDate(Timestamp date) {
        return new Date(date.getTime());
    }

    /**
     * String to Date
     *
     * @param dateString
     * @param pattern    0-"yyyy-MM-dd"
     *                   1-"yyyy-MM-dd HH:mm"
     *                   2-"yyyy-MM-dd HH:mm:ss"
     *                   3-"yyyy-M-d"
     *                   4-"yyyyMMdd"
     *                   5-"yyyyMM"
     *                   6-"yyyy年M月d日"
     *                   7-"yyyy.M.d"
     *                   8-"yyyy.MM.dd"
     *                   9-"yyyy年M月"
     *                   10-"yyyyMMddHHmmss"
     * @return
     */
    public static Date StringToDate(String dateString, int pattern) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        dateString = dateString.replace('T', ' ');
        SimpleDateFormat df = new SimpleDateFormat();
        df.applyPattern(PATTERNS[pattern]);
        try {
            return df.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * String to Long
     *
     * @param dateString
     * @param pattern    0-"yyyy-MM-dd"
     *                   1-"yyyy-MM-dd HH:mm"
     *                   2-"yyyy-MM-dd HH:mm:ss"
     *                   3-"yyyy-M-d"
     *                   4-"yyyyMMdd"
     *                   5-"yyyyMM"
     *                   6-"yyyy年M月d日"
     *                   7-"yyyy.M.d"
     *                   8-"yyyy.MM.dd"
     *                   9-"yyyy年M月"
     *                   10-"yyyyMMddHHmmss"
     * @return
     */
    public static Long StringToLong(String dateString, int pattern) {
        return DateToLong(StringToDate(dateString, pattern));
    }

    /**
     * 获得时间的秒数
     *
     * @param date null返回0
     * @return long
     */
    public static Long DateToLong(Date date) {
        return date == null ? 0 : date.getTime() / 1000;
    }

    /**
     * 通过秒数获得时间
     *
     * @param l 0返回null
     * @return Date
     */
    public static Date LongToDate(long l) {
        return l == 0 ? null : new Date(l * 1000);
    }

    /**
     * 通过秒数获得时间字符串
     *
     * @param l       0返回空字符串
     * @param pattern 0-"yyyy-MM-dd"
     *                1-"yyyy-MM-dd HH:mm"
     *                2-"yyyy-MM-dd HH:mm:ss"
     *                3-"yyyy-M-d"
     *                4-"yyyyMMdd"
     *                5-"yyyyMM"
     *                6-"yyyy年M月d日"
     *                7-"yyyy.M.d"
     *                8-"yyyy.MM.dd"
     *                9-"yyyy年M月"
     *                10-"yyyyMMddHHmmss"
     * @return
     */
    public static String LongToString(long l, int pattern) {
        return DateToString(LongToDate(l), pattern);
    }

    /**
     * 获得当前时间字符串
     *
     * @param pattern 0-"yyyy-MM-dd"
     *                1-"yyyy-MM-dd HH:mm"
     *                2-"yyyy-MM-dd HH:mm:ss"
     *                3-"yyyy-M-d"
     *                4-"yyyyMMdd"
     *                5-"yyyyMM"
     *                6-"yyyy年M月d日"
     *                7-"yyyy.M.d"
     *                8-"yyyy.MM.dd"
     *                9-"yyyy年M月"
     *                10-"yyyyMMddHHmmss"
     * @return
     */
    public static String getNowString(int pattern) {
        return DateToString(new Date(), pattern);
    }

    /**
     * 获得当前时间秒数
     *
     * @return
     */
    public static Long getNowLong() {
        return DateToLong(new Date());
    }

    /**
     * 通过秒数获得“xx小时xx分钟”字符串
     *
     * @param seconds
     * @return
     */
    public static String getStringBySeconds(long seconds) {
        String s = "";
        if (seconds > 0) {
            double d1 = seconds / 3600.0;

            d1 = d1 / 24;
            seconds = (int) d1;
            if (seconds > 0) {
                s += seconds + "天";
            }
            d1 = (d1 - seconds) * 24;

            seconds = (int) d1;
            if (seconds > 0) {
                s += seconds + "小时";
            }

            d1 = (d1 - seconds) * 60;
            seconds = (int) d1;
            if (seconds > 0) {
                s += seconds + "分钟";
            }
            if (s.isEmpty()) {
                s = "小于1分钟";
            }
        }
        return s;
    }

    /**
     * 获得months个月的时间，负数： 当前时间之前
     *
     * @param months
     * @return
     */
    public static Date getDateByMonths(int months) {
        Date date = new Date();//当前日期
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.setTime(date);//设置当前日期
        calendar.add(Calendar.MONTH, months);//月份减一
        return calendar.getTime();
    }

    /**
     * 获得本周一的当前时间
     *
     * @return
     */
    public static Date getWeekFirstDate() {
        Calendar cal = Calendar.getInstance();
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 2;
        cal.add(Calendar.DATE, -day_of_week);
        return cal.getTime();
    }

    /**
     * 获得本周日的当前时间
     *
     * @return
     */
    public static Date getWeekLastDate() {
        Calendar cal = Calendar.getInstance();
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 2;
        cal.add(Calendar.DATE, -day_of_week);
        cal.add(Calendar.DATE, 6);
        return cal.getTime();
    }

    /**
     * 获得本周一的毫秒数
     *
     * @return
     */
    public static long getWeekFirstDateLong() {
        return StringToLong(DateToString(getWeekFirstDate(), 0), 0);
    }

    /**
     * 获得本周日的毫秒数
     *
     * @return
     */
    public static long getWeekLastDateLong() {
        return StringToLong(DateToString(getWeekLastDate(), 0), 0);
    }

    public static Date addYear(Date d, int v) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.YEAR, v);
        return cal.getTime();
    }

    public static Date addMonth(Date d, int v) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MONTH, v);
        return cal.getTime();
    }

    public static Date addDay(Date d, int v) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DATE, v);
        return cal.getTime();
    }

    public static Date addHour(Date d, int v) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.HOUR, v);
        return cal.getTime();
    }

    /**
     * 判断是否为闰年
     *
     * @param year
     * @return
     */
    public static boolean isLeapYear(int year) {
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 日期转年龄
     *
     * @param bir
     * @return
     */
    public static double birthToAge(Date bir) {
        double age = 0;
        Calendar birth = Calendar.getInstance();
        birth.setTime(bir);
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        Long days = (Long) (new Date().getTime() - bir.getTime()) / (1000 * 3600 * 24);
        if ((DateUtil.isLeapYear(now.get(Calendar.YEAR)) && days <= 366) || days <= 365) {
            if (now.get(Calendar.MONTH) == birth.get(Calendar.MONTH) && now.get(Calendar.YEAR) == birth.get(Calendar.YEAR)) {
                age = (double) days / 1000 + 0.9;
            } else {
                double cha = (int) (now.get(Calendar.MONTH) - birth.get(Calendar.MONTH));
                if (cha < 0) {
                    cha += 12;
                }
                age = cha / 1000 + 0.8;
            }
        } else {
            return now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
        }
        return age;
    }

    /**
     * 获取N天前后的日期
     *
     * @param past
     * @return
     */
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }

    public static DateTime offsetDay(Date date, int offSet) {
        return cn.hutool.core.date.DateUtil.offsetDay(date, offSet);
    }

    public static DateTime beginOfWeek(Date date) {
        return cn.hutool.core.date.DateUtil.beginOfWeek(date);
    }

    public static DateTime endOfWeek(Date date) {
        return cn.hutool.core.date.DateUtil.endOfWeek(date);
    }

    public static DateTime offsetWeek(Date date, int offSet) {
        return cn.hutool.core.date.DateUtil.offsetWeek(date, offSet);
    }

    public static DateTime beginOfMonth(Date date) {
        return cn.hutool.core.date.DateUtil.beginOfMonth(date);
    }

    public static DateTime endOfMonth(Date date) {
        return cn.hutool.core.date.DateUtil.endOfMonth(date);
    }

    public static DateTime offsetMonth(Date date, int offSet) {
        return cn.hutool.core.date.DateUtil.offsetMonth(date, offSet);
    }

    public static DateTime beginOfYear(Date date) {
        return cn.hutool.core.date.DateUtil.beginOfYear(date);
    }

    public static DateTime endOfYear(Date date) {
        return cn.hutool.core.date.DateUtil.endOfYear(date);
    }

    /**
     * 生成指定日期内随机时间
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static Date randomDate(String beginDate, String endDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = format.parse(beginDate);// 构造开始日期
            Date end = format.parse(endDate);// 构造结束日期
            // getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
            if (start.getTime() >= end.getTime()) {
                return null;
            }
            long date = random(start.getTime(), end.getTime());
            return new Date(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long random(long begin, long end) {
        long rtn = begin + (long) (Math.random() * (end - begin));
        // 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
        if (rtn == begin || rtn == end) {
            return random(begin, end);
        }
        return rtn;
    }


}
