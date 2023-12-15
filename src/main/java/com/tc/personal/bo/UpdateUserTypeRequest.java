package com.tc.personal.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wuzhenzhong
 */
@Data
@ApiModel(value = "修改用户分类参数")
public class UpdateUserTypeRequest {

    @ApiModelProperty("用户id的集合")
    private List<Long> userIds;

    @ApiModelProperty("用户分类;0:普通;1:企业员工")
    private Integer userType;
}
