package com.mz.framework.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//@Setter
//@Getter
//@ToString
//@Component
//@ConfigurationProperties(prefix = "url.api.prefix")
public class ApiPrefix {
    //     @Value("${url.api.prefix.activiti}")
    private String activiti;

    //     @Value("${url.api.prefix.brand}")
    private String brand;


    //     @Value("${url.api.prefix.datacenter}")
    private String datacenter;

    //     @Value("${url.api.prefix.govern}")
    private String govern;

    //     @Value("${url.api.prefix.server}")
    private String server;


}