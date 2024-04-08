package com.mz.framework.config.mongo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@SuppressWarnings("ConfigurationProperties")
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "mongodb")
public class MongoClientOptionProperties {

    /**
     * 基础连接参数
     */
    @NotEmpty
    private String database; // 要连接的数据库
    private String username; // 用户名
    private String password; // 密码
    @NotEmpty
    private String address; // IP和端口（host:port），例如127.0.0.1:27017。集群模式用,分隔开，例如host1:port1,host2:port2
    private String authenticationDatabase; // 设置认证数据库，如果有的话

    /**
     * 客户端连接池参数
     */
    @NotEmpty
    private String clientName; // 客户端的标识，用于定位请求来源等，一般用程序名
    @Min(value = 1)
    private int connectionTimeoutMs; // TCP（socket）连接超时时间，毫秒
    @Min(value = 1)
    private int maxConnectionIdleTimeMs; // TCP（socket）连接闲置时间，毫秒
    @Min(value = 1)
    private int maxConnectionLifeTimeMs; // TCP（socket）连接最多可以使用多久，毫秒
    @Min(value = 1)
    private int readTimeoutMs; // TCP（socket）读取超时时间，毫秒
    @Min(value = 1)
    private int maxWaitTimeMs; // 当连接池无可用连接时客户端阻塞等待的最大时长，毫秒
    @Min(value = 2000)
    private int heartbeatFrequencyMs; // 心跳检测发送频率，毫秒
    @Min(value = 300)
    private int minHeartbeatFrequencyMs; // 最小的心跳检测发送频率，毫秒
    @Min(value = 200)
    private int heartbeatConnectionTimeoutMs; // 心跳检测连接超时时间，毫秒
    @Min(value = 200)
    private int heartbeatReadTimeoutMs; // 心跳检测读取超时时间，毫秒
    @Min(value = 1)
    private int connectionsPerHost; // 线程池允许的最大连接数
    @Min(value = 1)
    private int minConnectionsPerHost; // 线程池空闲时保持的最小连接数
    @Min(value = 1)
    // 计算允许多少个线程阻塞等待时的乘数，算法：threadsAllowedToBlockForConnectionMultiplier*maxConnectionsPerHost
    private int threadsAllowedToBlockForConnectionMultiplier;
}
