package com.mz.framework.config.nacos;


import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import com.alibaba.cloud.nacos.registry.NacosRegistration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.Query;
import java.lang.management.ManagementFactory;
import java.util.Iterator;
import java.util.Set;

@Component
@Slf4j
public class NacosConfig implements ApplicationRunner {

    @Autowired
    private NacosRegistration registration;

    @Autowired
    private NacosAutoServiceRegistration nacosAutoServiceRegistration;

    @Value("${server.port}")
    Integer port;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (registration != null && port != null) {
            Integer tomcatPort = port;
            try {
                String tomPort = getTomcatPort();
                if (StringUtils.isNoneBlank(tomPort)) {
                    tomcatPort = new Integer(tomPort);
                }
            } catch (Exception e) {
                log.error("获取外部Tomcat端口异常：{}", e);
            }
            registration.setPort(tomcatPort);
            nacosAutoServiceRegistration.start();
        }
    }

    /**
     * 获取外部tomcat端口
     */
    public String getTomcatPort() throws Exception {
        MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectName> objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"), Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
        Iterator<ObjectName> iterator = objectNames.iterator();
        if (iterator.hasNext()) {
            return iterator.next().getKeyProperty("port");
        }
        return null;
    }
}
