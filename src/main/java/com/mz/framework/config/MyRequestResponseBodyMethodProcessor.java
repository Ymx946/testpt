//package com.mz.framework.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.core.MethodParameter;
//import org.springframework.core.annotation.AnnotatedElementUtils;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
//
//import java.util.List;
//
//@Slf4j
//public class MyRequestResponseBodyMethodProcessor extends RequestResponseBodyMethodProcessor {
//
//    public MyRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters) {
//        super(converters);
//    }
//
//    @Override
//    public boolean supportsParameter(MethodParameter parameter) {
//        log.info("进入自定义============MyRequestResponseBodyMethodProcessor");
//        if (AnnotatedElementUtils.hasAnnotation(parameter.getContainingClass(), FeignClient.class)) {
//            return true;
//        }
//        return super.supportsParameter(parameter);
//    }
//
//}
