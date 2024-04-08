package com.mz.framework.web.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mz.common.util.Result;
import com.mz.common.util.ResultTrash;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author yangh
 * @date 2020/5/5 10:06 上午
 */
@ControllerAdvice(basePackages = "com.mz.controller",
        annotations = {Controller.class, RestController.class, ResponseBody.class})
public class GlobalResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        // 若接口返回的类型本身就是ResultWrapper，则无需操作，返回false
        // return !returnType.getParameterType().equals(ResultWrapper.class);
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof String) {
            // 若返回值为String类型，需要包装为String类型返回。否则会报错
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Result result = Result.success(body);
                return objectMapper.writeValueAsString(result);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("序列化String返回类型错误");
            }
        } else if (body instanceof Result) {
            return body;
        } else if (body instanceof ResultTrash) {
            return body;
        }
        return Result.success(body);
    }


}
