package com.mz;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
//@EnableCaching // 开启缓存
@EnableAsync // 开启异步调用
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableConfigurationProperties
@MapperScan(basePackages = {"com.mz.mapper"})
public class DataCenterApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(DataCenterApplication.class, args);
        log.info("====================蓄电池数智运维平台启动成功==================");
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        // 错误页面有容器来处理，而不是SpringBoot
        this.setRegisterErrorPageFilter(false);
        return application.sources(DataCenterApplication.class);
    }

}

