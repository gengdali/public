package com.tc.personal.modules.ums.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wuzhenzhong
 */
@Data
public class SendMsgRequest {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "手机号")
    private String phone;
}
