package com.mz.framework.config.redis;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.google.common.collect.Lists;
import com.mz.framework.config.datasource.AsyncTaskConfig;
import com.mz.framework.service.common.RedisSubscribeService;
import com.mz.framework.util.redis.RedisUtil;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Configuration
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.database}")
    private String database;
    @Resource
    private AsyncTaskConfig asyncTaskConfig;

    @Bean("redisSingleConfig")
    public RedisStandaloneConfiguration redisSingleConfig() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(host, Convert.toInt(port));
        redisConfig.setPassword(password);
        redisConfig.setDatabase(Convert.toInt(database));
        return redisConfig;
    }


    /**
     * 注意传入的对象名和类型RedisStandaloneConfiguration
     *
     * @param config
     * @param redisConfig
     * @return
     */
    @Primary
    @Bean("redisFactorySingle")
    public LettuceConnectionFactory redisFactorySingle(@Qualifier("redisSinglePool") GenericObjectPoolConfig config,
                                                       @Qualifier("redisSingleConfig") RedisStandaloneConfiguration redisConfig) {
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
                // 10000 毫秒
                .poolConfig(config).commandTimeout(Duration.ofMillis(10000)).build();
        return new LettuceConnectionFactory(redisConfig, clientConfiguration);
    }

    /**
     * 单实例redis数据源
     *
     * @param connectionFactory
     * @return
     */
    @Bean("redisSingleTemplate")
    public RedisTemplate<String, Object> redisSingleTemplate(@Qualifier("redisFactorySingle") LettuceConnectionFactory connectionFactory) {
        return redisTemplateSerializer(connectionFactory);
    }

    @Bean
    public RedisUtil redisUtil() {
        return new RedisUtil();
    }

    private RedisTemplate<String, Object> redisTemplateSerializer(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate();
        template.setConnectionFactory(connectionFactory);
        // 序列化工具
        FastJson2JsonRedisSerializer serializer = new FastJson2JsonRedisSerializer(Object.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        serializer.setObjectMapper(mapper);

        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);

        template.setValueSerializer(new GenericFastJsonRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        // Hash的key也采用StringRedisSerializer的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }

    /**
     * redis消息监听器容器
     * 可以添加多个监听不同话题的redis监听器，只需要把消息监听器和相应的消息订阅处理器绑定，该消息监听器
     * 通过反射技术调用消息订阅处理器的相关方法进行一些业务处理
     *
     * @return
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory, RedisSubscribeService redisSubscribeService) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(connectionFactory);
        redisMessageListenerContainer.setTaskExecutor(asyncTaskConfig.getAsyncExecutor());
//        redisMessageListenerContainer.addMessageListener(redisSubscribeService, Lists.newArrayList(
//                ChannelTopic.of("mangzhong"),
//                ChannelTopic.of("mangzhong222")
//        ));
        Map<RedisSubscribeService, Collection<? extends Topic>> map = new HashMap<>();
        map.put(redisSubscribeService, Lists.newArrayList(ChannelTopic.of("mangzhong"), ChannelTopic.of("mangzhong222")));
        redisMessageListenerContainer.setMessageListeners(map);
        return redisMessageListenerContainer;
    }

}
