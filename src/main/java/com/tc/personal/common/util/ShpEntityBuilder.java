package com.tc.personal.common.util;

import org.opengis.feature.simple.SimpleFeature;

/**
 * @author : LuoYuHong
 * @date : 2023/4/3 17:24
 */
@FunctionalInterface
public interface ShpEntityBuilder<T> {
    /**
     * 根据字段对应关系，生成指定对象
     *
     * @param feature
     * @return
     * @throws Exception
     */
    T createEntity(SimpleFeature feature) throws Exception;
}
