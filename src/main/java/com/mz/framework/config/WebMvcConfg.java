package com.mz.framework.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mz.framework.config.converter.StringDecoderForHeaderConverter;
import com.mz.framework.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.unit.DataSize;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

/**
 * @Description: springboot 拦截器
 * @author: yangh
 * @date: 2019年9月19日 下午3:11:38
 */
@Configuration
@EnableConfigurationProperties({ServerProperties.class})
public class WebMvcConfg implements WebMvcConfigurer {
    @Value("${spring.jackson.date-format}")
    private String dateFormatPattern;
    @Value("${spring.jackson.time-zone}")
    private String timeZone;
    @Resource
    private AuthInterceptor authInterceptor;
//    @Resource
//    private ApiPrefix apiPrefix;

//    @Override
//    public void configurePathMatch(PathMatchConfigurer configurer) {
//        configurer.addPathPrefix(apiPrefix.getActiviti(), c -> c.isAnnotationPresent(ApiPrefixActivitiRestController.class));
//        configurer.addPathPrefix(apiPrefix.getBrand(), c -> c.isAnnotationPresent(ApiPrefixBrandRestController.class));
//        configurer.addPathPrefix(apiPrefix.getDatacenter(), c -> c.isAnnotationPresent(ApiPrefixDatacenterRestController.class));
//        configurer.addPathPrefix(apiPrefix.getGovern(), c -> c.isAnnotationPresent(ApiPrefixGovernRestController.class));
//        configurer.addPathPrefix(apiPrefix.getServer(), c -> c.isAnnotationPresent(ApiPrefixServerRestController.class));
//    }

    /**
     * @param registry 配置静态资源放行
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    /**
     * @param registry 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加登录处理拦截器，拦截所有请求，登录请求除外
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(authInterceptor);
        // 配置拦截策略
        interceptorRegistration.addPathPatterns("/**");
    }

    @Bean
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);

//         时间格式化
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDateFormat(new SimpleDateFormat(dateFormatPattern));
        objectMapper.setTimeZone(TimeZone.getTimeZone(timeZone));
        objectMapper.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator paramJsonGenerator, SerializerProvider provider) throws IOException {
                String fieldName = paramJsonGenerator.getOutputContext().getCurrentName();
                try {
                    Field field = paramJsonGenerator.getCurrentValue().getClass().getDeclaredField(fieldName);
                    if (Objects.equals(field.getType(), String.class)) {
                        paramJsonGenerator.writeString("");
                    } else if (Objects.equals(field.getType(), List.class)) {
                        paramJsonGenerator.writeStartArray();
                        paramJsonGenerator.writeEndArray();
                    } else if (Objects.equals(field.getType(), Map.class)) {
                        paramJsonGenerator.writeStartObject();
                        paramJsonGenerator.writeEndObject();
                    } else {
                        paramJsonGenerator.writeNull();
                    }
                } catch (Exception e) {
                    paramJsonGenerator.writeNull();
                }
            }
        });

        // 序列化是记录被序列化的类型信息
        //指定序列化输入的类型为非最终类型，除了少数“自然”类型（字符串、布尔值、整数、双精度），它们可以从 JSON 正确推断； 以及所有非最终类型的数组
//        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY)
//                // null 值不序列化
//                .setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return objectMapper;
    }

    /**
     * +对于header中的中文字进行解码
     *
     * @return 转换结果
     */
    @Bean
    public StringDecoderForHeaderConverter stringHeaderConverter(ServerProperties serverProperties) {
        return new StringDecoderForHeaderConverter(serverProperties.getServlet().getEncoding().getCharset());
    }

    /**
     * 1st request: As we discussed above on first request, Response will be 200 and
     * response header contains ETag which is hashcode of response, We do not worry
     * about hashcode value it will be managed by spring boot internally.
     *
     * @return
     */
    @Bean
    public Filter filter() {
        System.setProperty("pagehelper.banner", "false");
        ShallowEtagHeaderFilter filter = new ShallowEtagHeaderFilter();
        return filter;
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(100L));
        factory.setMaxRequestSize(DataSize.ofGigabytes(2L));
        return factory.createMultipartConfig();
    }

    /**
     * 扩展消息转换解析
     *
     * @param converters
     */
//    @Override
//    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//        for (HttpMessageConverter<?> cvt : converters) {
//            if (cvt instanceof MappingJackson2HttpMessageConverter) {
//                MappingJackson2HttpMessageConverter converter = (MappingJackson2HttpMessageConverter) cvt;
//                enhanceConvertor(converter);
//            }
//        }
//    }
//
//    /**
//     * 增强转换器
//     *
//     * @param converter
//     */
//    private void enhanceConvertor(MappingJackson2HttpMessageConverter converter) {
//        converter.setObjectMapper(jacksonObjectMapper(new Jackson2ObjectMapperBuilder()));
//    }

}
