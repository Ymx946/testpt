//package com.mz.framework.config.Tinify;
//
//import com.tinify.Tinify;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.context.annotation.Configuration;
//
//@Slf4j
//@Configuration
//public class TinifyConfig implements ApplicationRunner {
//
//    @Value("${tinify.key}")
//    private String tinifyKey;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        log.info("tinifyKey: " + tinifyKey);
//        Tinify.setKey(tinifyKey);
//    }
//}
