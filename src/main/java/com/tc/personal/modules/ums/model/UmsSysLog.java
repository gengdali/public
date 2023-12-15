package com.tc.personal.modules.ums.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 系统日志表
 * </p>
 *
 * @author luoyuhong
 * @since 2022-04-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ums_sys_log")
@ApiModel(value = "UmsSysLog对象", description = "系统日志表")
public class UmsSysLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "操作模块")
    private String operation;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "方法名")
    private String method;

    @ApiModelProperty(value = "参数名")
    private String params;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "ip地址")
    private String ip;

    @ApiModelProperty(value = "操作类型")
    private String operationType;

    @ApiModelProperty(value = "请求方式")
    private String requestMethod;

    @ApiModelProperty(value = "操作状态")
    private Integer status;

    @ApiModelProperty(value = "请求地址")
    private String url;

    @ApiModelProperty(value = "人员操作记录")
    private String peopleOperationRecord;


}
