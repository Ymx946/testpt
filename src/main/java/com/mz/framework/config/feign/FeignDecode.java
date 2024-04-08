//package com.mz.framework.config.feign;
//
//import com.google.gson.Gson;
//import com.mz.common.util.Result;
//import com.mz.common.util.ResultTrash;
//import feign.FeignException;
//import feign.Response;
//import feign.codec.Decoder;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.ObjectFactory;
//import org.springframework.beans.factory.SmartInitializingSingleton;
//import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
//import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
//import org.springframework.cloud.openfeign.support.SpringDecoder;
//
//import java.io.IOException;
//import java.lang.reflect.Type;
//
//
//@Slf4j
//public class FeignDecode implements Decoder, SmartInitializingSingleton {
//    private Gson gson = new Gson();
//
//    private ResponseEntityDecoder responseEntityDecoder;
//
//    @Override
//    public Object decode(final Response response, Type type) throws IOException, FeignException {
//        if (type == Result.class.getGenericSuperclass()) {
//            return Result.class;
//        }
//        if (type == ResultTrash.class.getGenericSuperclass()) {
//            return Result.class;
//        }
//        Object result = responseEntityDecoder.decode(response, type);
//        log.error("远程调用失败:{}");
//        return result;
//    }
//
//    @Override
//    public void afterSingletonsInstantiated() {
//        //初始化spring提供的解析器
////        responseEntityDecoder = new ResponseEntityDecoder(new SpringDecoder(this.messageConverters));
//        responseEntityDecoder = new ResponseEntityDecoder(new SpringDecoder(feignHttpMessageConverter()));
//    }
//
//    public ObjectFactory<HttpMessageConverters> feignHttpMessageConverter() {
//        final HttpMessageConverters httpMessageConverters = new HttpMessageConverters(new FeignConfig.GateWayMappingJackson2HttpMessageConverter());
//        return () -> httpMessageConverters;
//    }
//}