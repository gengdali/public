package com.tc.personal.modules.ums.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * @PROJECT_NAME: west-city-assessment-java
 * @DESCRIPTION:是否枚举
 * @AUTHOR: 12615
 * @DATE: 2023/3/20 17:20
 */
@ToString
@AllArgsConstructor
@Getter
public enum YesOrNotEnum {
    YES("1", "是"),
    NO("0", "否");

    private String value;

    private String text;

    public static String getTextByValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        String valueKey = value;
        for (YesOrNotEnum enums : YesOrNotEnum.values()) {
            if (enums.getValue().equals(valueKey)) {
                return enums.getText();
            }
        }
        return null;
    }
}
