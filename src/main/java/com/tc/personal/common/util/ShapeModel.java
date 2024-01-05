package com.tc.personal.common.util;

/**
 * @author: wh
 * @create: 2022-10-17 19:21
 * @description
 **/

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @program: tdt-commons
 * @description: Shape文件映射
 * @author: Mr.superbeyone
 * @create: 2018-09-19 11:04
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShapeModel {
    private String layer;

    private String projection;

    private String region;

    private String regionCode;

    private Integer level;

    private String time;

    private Date createTime;

    private String clazz;

    private String geoStr;

}

