package com.tc.personal.common.log.annotation;

import java.lang.annotation.*;

/**
 * @author 阿鸿
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiLog {
    /**
     * 模块 写菜单名称
     */
    String title() default "";

    /**
     * 功能 固定写 新增，删除，修改，导入，导出
     */
    String action() default "";

    /**
     * 操作实体 写实体名称
     */
    Class<?> value();
}
