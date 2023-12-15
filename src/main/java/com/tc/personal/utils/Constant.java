package com.tc.personal.utils;

import java.util.Date;

/**
 * @author : LuoYuHong
 * @date : 2022/4/27 16:45
 */
public class Constant {

    public static final String WEEK_START_DATE = DateUtil.DateToString(DateUtil.beginOfWeek(new Date()), 0);
    public static final String WEEK_END_DATE = DateUtil.DateToString(DateUtil.endOfWeek(new Date()), 0);
    public static final String LAST_WEEK_START_DATE = DateUtil.DateToString(DateUtil.offsetWeek(DateUtil.beginOfWeek(new Date()), -1), 0);
    public static final String LAST_WEEK_END_DATE = DateUtil.DateToString(DateUtil.offsetWeek(DateUtil.endOfWeek(new Date()), -1), 0);
    public static final String MONTH_START_DATE = DateUtil.DateToString(DateUtil.beginOfMonth(new Date()), 0);
    public static final String MONTH_END_DATE = DateUtil.DateToString(DateUtil.endOfMonth(new Date()), 0);
    public static final String LAST_MONTH_START_DATE = DateUtil.DateToString(DateUtil.offsetMonth(DateUtil.beginOfMonth(new Date()), -1), 0);
    public static final String LAST_MONTH_END_DATE = DateUtil.DateToString(DateUtil.offsetMonth(DateUtil.endOfMonth(new Date()), -1), 0);

//    public static final String FILE_SHOW_URL = "D:\\IIS\\AppPic\\";
    //public static final String FILE_SHOW_URL = "F:\\IIS\\AppPic\\";
//    public static final String SERVER_IP_TEN = "http://192.168.0.39:8888";

    //本地文件存放地址
    public static final String SERVER_IP_FILE_URL = "http://192.168.3.116:8844/";

}
