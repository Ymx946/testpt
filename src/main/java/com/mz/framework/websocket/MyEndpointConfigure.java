package com.mz.framework.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.websocket.server.ServerEndpointConfig;

/**
 * @Description:以websocketConfig.java注册的bean是由自己管理的，需要使用配置托管给spring管理
 * @author: yangh
 * @date: 2020年3月27日 下午8:52:05
 */
@Slf4j
public class MyEndpointConfigure extends ServerEndpointConfig.Configurator implements ApplicationContextAware {
    private static volatile BeanFactory context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("auto load: " + this.hashCode());
        MyEndpointConfigure.context = applicationContext;
    }

    @Override
    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
        return context.getBean(clazz);
    }
}