//package com.mz.framework.config;
//
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.databind.*;
//import com.fasterxml.jackson.databind.module.SimpleModule;
//import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.converter.ByteArrayHttpMessageConverter;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.StringHttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
//import org.springframework.http.converter.xml.SourceHttpMessageConverter;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//
//import java.io.IOException;
//import java.lang.reflect.Field;
//import java.math.BigInteger;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//@Configuration
//public class MsgMvcConfig extends WebMvcConfigurationSupport {
//    @Value("${spring.jackson.date-format}")
//    private String dateFormatPattern;
//    @Value("${spring.jackson.time-zone}")
//    private String timeZone;
//
//    @Override
//    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
//        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
//        stringHttpMessageConverter.setWriteAcceptCharset(false);
//
//        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>(5);
//        messageConverters.add(new ByteArrayHttpMessageConverter());
//        messageConverters.add(stringHttpMessageConverter);
//        messageConverters.add(new SourceHttpMessageConverter<>());
//        messageConverters.add(new AllEncompassingFormHttpMessageConverter());
//
//        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
//        ObjectMapper objectMapper = jackson2HttpMessageConverter.getObjectMapper();
//        SimpleModule simpleModule = new SimpleModule();
//        simpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
//        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
//        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
//        objectMapper.registerModule(simpleModule);
//
//        // 时间格式化
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        objectMapper.setDateFormat(new SimpleDateFormat(dateFormatPattern));
//        objectMapper.setTimeZone(TimeZone.getTimeZone(timeZone));
//        objectMapper.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
////        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
////        下划线转驼峰
////        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
//        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
//            @Override
//            public void serialize(Object value, JsonGenerator paramJsonGenerator, SerializerProvider provider) throws IOException {
//                String fieldName = paramJsonGenerator.getOutputContext().getCurrentName();
//                try {
//                    Field field = paramJsonGenerator.getCurrentValue().getClass().getDeclaredField(fieldName);
//                    if (Objects.equals(field.getType(), String.class)) {
//                        paramJsonGenerator.writeString("");
//                    } else if (Objects.equals(field.getType(), List.class)) {
//                        paramJsonGenerator.writeStartArray();
//                        paramJsonGenerator.writeEndArray();
//                    } else if (Objects.equals(field.getType(), Map.class)) {
//                        paramJsonGenerator.writeStartObject();
//                        paramJsonGenerator.writeEndObject();
//                    } else {
//                        paramJsonGenerator.writeNull();
//                    }
//                } catch (Exception e) {
//                    paramJsonGenerator.writeNull();
//                }
//            }
//        });
//        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
//        messageConverters.add(jackson2HttpMessageConverter);
//
//        MyRequestResponseBodyMethodProcessor resolver = new MyRequestResponseBodyMethodProcessor(messageConverters);
//        argumentResolvers.add(resolver);
//    }
//}
