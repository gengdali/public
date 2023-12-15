package com.tc.personal.utils;

/**
 * @PROJECT_NAME: MeiShi_merchant_java
 * @DESCRIPTION:字符串工具类
 * @AUTHOR: 12615
 * @DATE: 2023/3/11 20:58
 */
public class StringUtils {
    /*替换指定区间内的字符串*/
    public static String replace(int from, int to, String source, char target) {
        if (source == null || to - from < 0)
            return null;
        StringBuffer bf = new StringBuffer("");
        int width = to - from;
        while (width > 0) {
            bf.append(target);
            width--;
        }
        String s = source.substring(0, from) + bf + source.substring(to, source.length());
        return s;
    }


}
