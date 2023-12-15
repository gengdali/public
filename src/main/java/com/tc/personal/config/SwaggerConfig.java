package com.tc.personal.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.tc.personal.common.domain.SwaggerProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger API文档相关配置
 * Created by macro on 2018/4/26.
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.tc.personal.modules")
                .title("市规自委西城分局年度考核管理系统PC")
                .description("市规自委西城分局年度考核管理系统PC端")
                .contactName("同创数字")
                .version("1.0")
                .enableSecurity(true)
                .build();
    }
}
