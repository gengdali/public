package com.tc.personal.common.anotation;

import java.lang.annotation.*;

/**
 * @author: wh
 * @create: 2022-10-28 17:08
 * @description shp常用注解
 **/
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Shp {

    /**
     * 字段名称
     *
     * @return
     */
    String fieldName();


}
