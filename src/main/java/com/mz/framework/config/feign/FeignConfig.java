package com.mz.framework.config.feign;


import com.mz.framework.config.hierarchical.HierarchicalContract;
import feign.*;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
@Configuration
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public class FeignConfig implements RequestInterceptor {
    public static final int CONNECT_TIME_OUT_MILLIS = 60000;
    public static final int READ_TIME_OUT_MILLIS = 60000;

    private final static int READ_TIMEOUT = 1000;

    private final static int CONNECT_TIMEOUT = 600;

    private final static int WRITE_TIMEOUT = 600;

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    /**
     * description 忽略https证书验证
     */
    private static HostnameVerifier getHostnameVerifier() {
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };
        return hostnameVerifier;
    }

    @Bean
    public Request.Options feignOptions() {
        return new Request.Options(CONNECT_TIME_OUT_MILLIS, READ_TIME_OUT_MILLIS);
    }

    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(1, SECONDS.toMillis(10000), 5);
    }

    /**
     * feign 日志记录级别
     * NONE：无日志记录（默认）
     * BASIC：只记录请求方法和 url 以及响应状态代码和执行时间。
     * HEADERS：记录请求和响应头的基本信息。
     * FULL：记录请求和响应的头、正文和元数据。
     *
     * @return Logger.Level
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    @Scope("prototype")
    public Encoder feignFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }

    @Bean
    @Primary
    @Scope("prototype")
    public Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        this.messageConverters = messageConverters;
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }

    @Bean
    public Decoder feignDecoder() {
//        return new ResponseEntityDecoder(new SpringDecoder(feignHttpMessageConverter()));
//        return new ResponseEntityDecoder(new CustomGZIPResponseDecoder(new SpringDecoder(feignHttpMessageConverter())));
        return new CustomGZIPResponseDecoder(new SpringDecoder(feignHttpMessageConverter()));
    }

    public ObjectFactory<HttpMessageConverters> feignHttpMessageConverter() {
        final HttpMessageConverters httpMessageConverters = new HttpMessageConverters(new GateWayMappingJackson2HttpMessageConverter());
        return () -> httpMessageConverters;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        // 对于自动任务，起服务就调用参数的，没有请求信息，不需要传递
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String values = request.getHeader(name);
                // 跳过content-length，不然可能会报too many bites written问题
                if ("content-length".equalsIgnoreCase(name) || "cookie".equalsIgnoreCase(name)) {
                    continue;
                }
                // 此处：把content-type的头带过去
                if ("content-type".equalsIgnoreCase(name)) {
                    continue;
                }
                requestTemplate.header(name, values);
            }
        }
    }

    @Bean
    public Contract feignContract() {
        return new HierarchicalContract();
    }

    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder();
    }

    @Bean
    public OkHttpClient okHttpClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .hostnameVerifier(getHostnameVerifier())
                //设置连接超时
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                //设置读超时
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                //设置写超时
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                //是否自动重连
                .retryOnConnectionFailure(true)
                .connectionPool(new ConnectionPool(100, 5L, TimeUnit.MINUTES))
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getX509TrustManager())
                .addInterceptor(new UnzippingInterceptor())
                .build();
        return client;
    }

    @Bean
    public feign.okhttp.OkHttpClient feignOkHttpClient() {
        return new feign.okhttp.OkHttpClient(okHttpClient());
    }

    public static class GateWayMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
        GateWayMappingJackson2HttpMessageConverter() {
            List<MediaType> mediaTypes = new ArrayList<>();
            mediaTypes.add(MediaType.valueOf(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"));
            mediaTypes.add(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"));
            mediaTypes.add(MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8"));
            mediaTypes.add(MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM_VALUE + ";charset=UTF-8"));
            mediaTypes.add(MediaType.valueOf(MediaType.APPLICATION_PDF_VALUE + ";charset=UTF-8"));
            mediaTypes.add(MediaType.valueOf(MediaType.MULTIPART_FORM_DATA + ";charset=UTF-8"));
            setSupportedMediaTypes(mediaTypes);
        }
    }

}
