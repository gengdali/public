package com.tc.personal.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wuzhenzhong
 */
@Data
public class UserIdsRequest {

    @ApiModelProperty("用户id的集合")
    private List<Long> userIds;

    @ApiModelProperty("管理员校验密码")
    private String secretCode;

}
