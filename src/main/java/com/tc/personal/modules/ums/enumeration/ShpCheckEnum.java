package com.tc.personal.modules.ums.enumeration;


import com.tc.personal.common.exception.ApiException;

import java.util.Arrays;

/**
 * @author: wh
 * @create: 2022-10-24 17:23
 * @description shp文件配置
 **/
public enum ShpCheckEnum {

    ;

    private String code;

    private Class desc;

    ShpCheckEnum(String code, Class desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Class getDesc() {
        return desc;
    }

    public void setDesc(Class desc) {
        this.desc = desc;
    }

    public static Class getDesc(String code) {
        return Arrays.stream(ShpCheckEnum.values())
                .filter(t -> code.equals(t.getCode()))
                .findFirst()
                .orElseThrow(() -> new ApiException("shp类型找不到对应实体")).desc;
    }


}
