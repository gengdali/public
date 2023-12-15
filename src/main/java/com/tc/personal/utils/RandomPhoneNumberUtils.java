package com.tc.personal.utils;

import java.util.Random;

/**
 * @PROJECT_NAME: MeiShi_merchant_java
 * @DESCRIPTION:生产随机手机虚拟号工具类
 * @AUTHOR: 12615
 * @DATE: 2023/3/10 15:46
 */
public class RandomPhoneNumberUtils {
    //中国移动
    public static final String[] CHINA_MOBILE = {
            "134", "135", "136", "137", "138", "139", "150", "151", "152", "157", "158", "159",
            "182", "183", "184", "187", "188", "178", "147", "172", "198"
    };
    //中国联通
    public static final String[] CHINA_UNICOM = {
            "130", "131", "132", "145", "155", "156", "166", "171", "175", "176", "185", "186", "166"
    };
    //中国电信
    public static final String[] CHINA_TELECOME = {
            "133", "149", "153", "173", "177", "180", "181", "189", "199"
    };

    /**
     * 生成手机号
     *
     * @param op 0 移动 1 联通 2 电信
     */
    public static String createMobile(int op) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        String mobile;//手机号前三位
        int temp;
        switch (op) {
            case 0:
                mobile = CHINA_MOBILE[random.nextInt(CHINA_MOBILE.length)];
                break;
            case 1:
                mobile = CHINA_UNICOM[random.nextInt(CHINA_UNICOM.length)];
                break;
            case 2:
                mobile = CHINA_TELECOME[random.nextInt(CHINA_TELECOME.length)];
                break;
            default:
                mobile = "op标志位有误！";
                break;
        }
        if (mobile.length() > 3) {
            return mobile;
        }
        sb.append(mobile);
        //生成手机号后8位
        for (int i = 0; i < 8; i++) {
            temp = random.nextInt(10);
            sb.append(temp);
        }
        return sb.toString();
    }

    /**
     * 生成带星手机号
     *
     * @param
     */
    public static String createVirtualMobile(int op) {
        String mobile = createMobile(op);
        String replace = StringUtils.replace(3, 7, mobile, '*');
        return replace;
    }
}
