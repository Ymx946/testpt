package com.mz.framework.config.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域请求配置
 */
//@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        /*是否允许请求带有验证信息, 允许cookies跨域 */
        corsConfiguration.setAllowCredentials(true);
        /*允许访问的客户端域名，允许向该服务器提交请求的URI，*表示全部允许，在SpringMVC中，如果设成*，会自动转成当前请求头中的Origin*/
        corsConfiguration.addAllowedOrigin("*");
        /*允许服务端访问的客户端请求头, 允许访问的头信息,*表示全部*/
        corsConfiguration.addAllowedHeader("*");
        /*允许访问的方法名,GET POST，允许提交请求的方法，*表示全部允许等*/
        corsConfiguration.addAllowedMethod("*");
        /* 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了*/
        corsConfiguration.setMaxAge(18000L);
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //放行哪些原始域
                .allowedOrigins("*")
                .allowedMethods("GET", "POST")
                .exposedHeaders("access-control-allow-headers",
                        "access-control-allow-methods",
                        "access-control-allow-origin",
                        "access-control-max-age",
                        "X-Frame-Options")
                .allowCredentials(false)
                .maxAge(3600)
                .allowedHeaders("*");
    }
}
