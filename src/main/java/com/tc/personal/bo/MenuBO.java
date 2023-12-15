package com.tc.personal.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MenuBO extends PageBO {

    @ApiModelProperty(value = "隐藏状态，0显示，1隐藏")
    private Integer hidden;

    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    @ApiModelProperty(value = "菜单父id")
    private Long menuPid;


}
