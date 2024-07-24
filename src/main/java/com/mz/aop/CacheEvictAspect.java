package com.mz.framework.aop;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.util.CommonUtil;
import com.mz.common.util.MessageUtil;
import com.mz.common.util.PasswordUtil;
import com.mz.common.util.StringUtils;
import com.mz.framework.annotation.CacheEvict;
import com.mz.framework.util.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

/**
 * @author yangh
 * @date 2024/06/20 15:19
 * @desc 清理缓存数据
 * @link <a href="https://blog.csdn.net/qq_19604451/article/details/134314127">...</a>
 */
@Aspect
@Component
@Slf4j
public class CacheEvictAspect {

    @Lazy
    @Resource
    private RedisUtil redisUtil;

    public CacheEvictAspect() {
    }

    @Pointcut("@annotation(com.mz.framework.annotation.CacheEvict)")
    public void cacheEvict() {
    }

    /**
     * 环绕通知
     */
    @Around(value = "cacheEvict()")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
            CacheEvict cacheEvict = signature.getMethod().getAnnotation(CacheEvict.class);
            if (ObjectUtil.isNotEmpty(cacheEvict)) {
                // 接收到请求，记录请求内容
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                assert attributes != null;
                HttpServletRequest request = attributes.getRequest();
                Map<String, String> headerParamMap = MessageUtil.getHeaderParams(request);
                Map<String, String> reqParamMap = MessageUtil.getReqParams(request);
                String cacheKeyPrefix = CommonUtil.getCachePrefix(headerParamMap);

                // 根据指定的缓存key释缓存
                String[] cacheEvictKey = cacheEvict.key();
                if (StringUtils.isNotEmpty(cacheEvictKey)) {
                    for (String cacheKey : cacheEvictKey) {
                        if (StringUtils.isNotEmpty(cacheKey)) {
                            String delCacheKey = cacheKeyPrefix + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + cacheKey;
                            redisUtil.delete(delCacheKey);
                            log.info("删除指定的缓存:{}", delCacheKey);
                        }
                    }
                } else {
                    // 是否需要释放所有缓存
                    if (cacheEvict.releaseAll()) {
                        Set<String> keys = redisUtil.keys(cacheKeyPrefix + ConstantsCacheUtil.DELETE_ALL);
                        if (CollectionUtils.isNotEmpty(keys)) {
                            redisUtil.delete(keys);
                        }
                    } else {
                        Class<?>[] classes = cacheEvict.type();
                        String[] methods = cacheEvict.method();
                        if (classes.length > 0) {
                            for (int i = 0; i < classes.length; i++) {
                                Class<?> classItem = classes[i];
                                String classPath = classItem.getName();

                                if (methods.length == 0) {
                                    // 释放指定类中所有方法的缓存
                                    releaseClassCache(proceedingJoinPoint, cacheKeyPrefix);
                                } else {
                                    String classMethod = methods[i];
                                    // 如果指定了方法名则删除指定方法的缓存
                                    if (StringUtils.isNotEmpty(classMethod)) {
                                        String headerParam = "", reqParam = "";
                                        if (CollectionUtil.isNotEmpty(headerParamMap)) {
                                            headerParam = JSON.toJSONString(headerParamMap);
                                        }
                                        if (CollectionUtil.isNotEmpty(reqParamMap)) {
                                            reqParam = JSON.toJSONString(reqParamMap);
                                        }

                                        String[] methodNameList = org.springframework.util.StringUtils.delimitedListToStringArray(classMethod, ", ;\t");
                                        for (String methodName : methodNameList) {
                                            String cacheRawKey = classPath + "." + methodName + headerParam + reqParam;
                                            String cacheKey = cacheKeyPrefix + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + PasswordUtil.md5(classPath) + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + PasswordUtil.md5(cacheRawKey);
                                            Set<String> keys = redisUtil.keys(cacheKey);
                                            if (CollectionUtils.isNotEmpty(keys)) {
                                                redisUtil.delete(keys);
                                            }
                                            log.info("删除指定类中指定方法的缓存:{}", cacheKey);
                                        }
                                    }
                                }
                            }
                        } else {
                            // 释放指定类中所有方法的缓存
                            releaseClassCache(proceedingJoinPoint, cacheKeyPrefix);
                        }
                    }
                }
            }
        } catch (Throwable ex) {
            log.error("<====== EvictCache 执行异常: {} ======>", ex);
        }
        return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
    }

    /**
     * 释放指定类中所有方法的缓存
     *
     * @param proceedingJoinPoint
     * @param cacheKeyPrefix
     */
    private void releaseClassCache(ProceedingJoinPoint proceedingJoinPoint, String cacheKeyPrefix) {
        String classPath = proceedingJoinPoint.getTarget().getClass().getName();
        String classMethods = cacheKeyPrefix + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + PasswordUtil.md5(classPath) + ConstantsCacheUtil.DELETE_ALL;
        // 获取模糊匹配的Key
        Set<String> keys = redisUtil.keys(classMethods);
        if (CollectionUtils.isNotEmpty(keys)) {
            redisUtil.delete(keys);
        }
        log.info("释放指定类中所有方法的缓存:{}", classMethods);
    }

}
