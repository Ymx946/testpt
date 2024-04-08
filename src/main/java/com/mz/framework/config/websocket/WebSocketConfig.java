package com.mz.framework.config.websocket;

import com.mz.framework.websocket.MyEndpointConfigure;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 开启WebSocket支持
 *
 * @Description:
 * @author: yangh
 * @date: 2020年3月25日 下午10:31:58
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig {

    @Bean
    @ConditionalOnBean(TomcatServletWebServerFactory.class)
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean
    public MyEndpointConfigure newConfigure() {
        return new MyEndpointConfigure();
    }

}