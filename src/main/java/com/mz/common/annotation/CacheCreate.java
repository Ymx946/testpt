package com.mz.framework.annotation;

import com.mz.common.ConstantsCacheUtil;
import com.mz.common.annotation.enums.CacheType;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author yangh
 * @date 2024/06/20 15:19
 * @desc 查询缓存注解
 * @link <a href="https://www.cnblogs.com/badaoliumangqizhi/p/17544973.html">...</a>
 */
@Documented
// 注解类型为方法注解
@Target({ElementType.METHOD})
// 在运行是执行
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheCreate {
    /**
     * 指定缓存的名称，如果没有指定，则 使用类名 +方法名
     */
    String key() default "";

    /**
     * 过期时间默认10分钟
     */
    long expired() default ConstantsCacheUtil.REDIS_MINUTE_TEN_SECOND;

    /**
     * 指定expire的单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 缓存的类型，包括CacheType.REMOTE、CacheType.LOCAL、CacheType.BOTH。
     * 如果定义为BOTH，会使用LOCAL和REMOTE组合成两级缓存
     */
    CacheType cacheType() default CacheType.REMOTE;

    /**
     * 是否 request 请求
     *
     * @return
     */
    boolean isReq() default true;

}
