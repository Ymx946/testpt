//package com.mz.framework.config.feign;
//
//
//import com.mz.framework.config.hierarchical.HierarchicalContract;
//import feign.*;
//import feign.codec.Decoder;
//import feign.codec.Encoder;
//import feign.form.spring.SpringFormEncoder;
//import lombok.extern.slf4j.Slf4j;
//import okhttp3.OkHttpClient;
//import org.apache.http.conn.ssl.NoopHostnameVerifier;
//import org.springframework.beans.factory.ObjectFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.AutoConfigureBefore;
//import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
//import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
//import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
//import org.springframework.cloud.openfeign.support.SpringDecoder;
//import org.springframework.cloud.openfeign.support.SpringEncoder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.net.ssl.*;
//import javax.servlet.http.HttpServletRequest;
//import java.util.ArrayList;
//import java.util.Enumeration;
//import java.util.List;
//
//@Slf4j
//@Configuration
//@AutoConfigureBefore(JacksonAutoConfiguration.class)
//public class FeignConfigBak implements RequestInterceptor {
//    public static SSLSocketFactory feignTrustingSSLSocketFactory = null;
//    @Autowired
//    private ObjectFactory<HttpMessageConverters> messageConverters;
//
//    /**
//     * description 忽略https证书验证
//     */
//    private static HostnameVerifier getHostnameVerifier() {
//        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
//            @Override
//            public boolean verify(String s, SSLSession sslSession) {
//                return true;
//            }
//        };
//        return hostnameVerifier;
//    }
//
//    public static SSLSocketFactory getFeignTrustingSSLSocketFactory() throws Exception {
//        TrustManager[] trustAllCerts = new TrustManager[1];
//        TrustManager tm = new miTM();
//        trustAllCerts[0] = tm;
//        SSLContext sc = SSLContext.getInstance("SSL");
//        sc.init(null, trustAllCerts, null);
//        return sc.getSocketFactory();
//    }
//
//    @Bean
//    public Encoder feignFormEncoder() {
//        return new SpringFormEncoder(new SpringEncoder(messageConverters));
//    }
//
//    @Bean
//    public Decoder feignDecoder() {
//        return new ResponseEntityDecoder(new SpringDecoder(feignHttpMessageConverter()));
//    }
//
//    public ObjectFactory<HttpMessageConverters> feignHttpMessageConverter() {
//        final HttpMessageConverters httpMessageConverters = new HttpMessageConverters(new GateWayMappingJackson2HttpMessageConverter());
//        return () -> httpMessageConverters;
//    }
//
//    @Override
//    public void apply(RequestTemplate template) {
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        if (null != attributes) {
//            HttpServletRequest request = attributes.getRequest();
//            if (null != request) {
//                // 遍历设置 也可从request取出整个Header 写到RequestTemplate 中
//                Enumeration<String> headerNames = request.getHeaderNames();
//                if (headerNames != null) {
//                    while (headerNames.hasMoreElements()) {
//                        String name = headerNames.nextElement();
//                        String values = request.getHeader(name);
//                        if ("content-type".equalsIgnoreCase(name)) {
//                            continue;
//                        }
//                        template.header(name, values);
//                    }
//                }
////                Enumeration<String> bodyNames = request.getParameterNames();
////                StringBuffer body = new StringBuffer();
////                if (bodyNames != null) {
////                    while (bodyNames.hasMoreElements()) {
////                        String name = bodyNames.nextElement();
////                        String values = request.getParameter(name);
////                        body.append(name).append("=").append(values).append("&");
////                    }
////                }
////                if (body.length() != 0) {
////                    body.deleteCharAt(body.length() - 1);
////                    template.body(body.toString());
////                    log.info("feign interceptor body:{}", body);
////                }
//            }
//        }
//    }
//
//    @Bean
//    public Contract feignContract() {
//        return new HierarchicalContract();
//    }
//
//    @Bean
//    public Feign.Builder feignBuilder() {
////        final Client trustSSLSockets = client();
////        return Feign.builder().client(trustSSLSockets);
//        OkHttpClient okHttpClient = okHttpClient();
//        return Feign.builder().client((Client) okHttpClient);
//    }
//
//    @Bean
//    public Client client() {
//        if (feignTrustingSSLSocketFactory == null) {
//            try {
//                feignTrustingSSLSocketFactory = getFeignTrustingSSLSocketFactory();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        Client client = new Client.Default(feignTrustingSSLSocketFactory, new NoopHostnameVerifier());
//        return client;
//    }
//
//    @Bean
//    public OkHttpClient okHttpClient() {
//        if (feignTrustingSSLSocketFactory == null) {
//            try {
//                feignTrustingSSLSocketFactory = getFeignTrustingSSLSocketFactory();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        OkHttpClient client = null;
//        client = new OkHttpClient.Builder()
//                .hostnameVerifier(getHostnameVerifier())
////                    .sslSocketFactory(new SSLContextBuilder().build().getSocketFactory())
//                .sslSocketFactory(feignTrustingSSLSocketFactory)
//                .build();
//        return client;
//    }
//
//    @Bean
//    public feign.okhttp.OkHttpClient feignOkHttpClient() {
//        return new feign.okhttp.OkHttpClient(okHttpClient());
//    }
//
//    public static class GateWayMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
//        GateWayMappingJackson2HttpMessageConverter() {
//            List<MediaType> mediaTypes = new ArrayList<>();
//            mediaTypes.add(MediaType.valueOf(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"));
//            mediaTypes.add(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"));
//            setSupportedMediaTypes(mediaTypes);
//        }
//    }
//
//    static class miTM implements TrustManager, X509TrustManager {
//        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//            return null;
//        }
//
//        public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
//            return true;
//        }
//
//        public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
//            return true;
//        }
//
//        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
//                throws java.security.cert.CertificateException {
//        }
//
//        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
//                throws java.security.cert.CertificateException {
//        }
//    }
//
//}
