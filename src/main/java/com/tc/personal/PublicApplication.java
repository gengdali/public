package com.tc.personal;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@EnableScheduling
@MapperScan("com.tc.personal.modules.*.mapper")
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
public class PublicApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext application = SpringApplication.run(PublicApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String port = env.getProperty("server.port");
        log.info("\n----------------------------------------------------------\n\t" +
                "Application is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port + "/\n\t" +
                "External: \thttp://" + ip + ":" + port + "/\n\t" +
                "Swagger文档: \thttp://" + ip + ":" + port + "/doc.html\n\t" +
                "Swagger文档: \thttp://" + ip + ":" + port + "/swagger-ui.html\n" +
                "----------------------------------------------------------");
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
