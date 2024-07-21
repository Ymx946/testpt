package com.mz.aop;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.util.StringUtil;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.util.*;
import com.mz.framework.annotation.CacheCreate;
import com.mz.framework.util.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author yangh
 * @date 2024/06/20 15:19
 * @desc 缓存查询到的数据
 * @link <a href="https://www.cnblogs.com/chuhongyun/p/11429025.html">...</a>
 * @link <a href="https://www.cnblogs.com/badaoliumangqizhi/p/17544973.html">...</a>
 */
@Aspect
@Component
@Slf4j
public class CacheCreateAspect {

    @Lazy
    @Resource
    private RedisUtil redisUtil;

    public CacheCreateAspect() {
    }

    @Pointcut("@annotation(com.mz.framework.annotation.CacheCreate)")
    public void cacheCreate() {
    }

    /**
     * 环绕通知
     * 环绕通知非常强大，可以决定目标method是否执行，什么时候执行，执行时是否需要替换method参数，执行完毕是否需要替换返回值。
     * 环绕通知第一个参数必须是org.aspectj.lang.ProceedingJoinPoint类型
     */
    @Around(value = "cacheCreate()")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) {
        try {
            MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
            Object[] args = proceedingJoinPoint.getArgs();
            // 获取CacheCreate注解
            CacheCreate cacheCreate = signature.getMethod().getAnnotation(CacheCreate.class);
            if (ObjectUtil.isNotEmpty(cacheCreate)) {
                String cacheKeyPrefix = ConstantsCacheUtil.QUERY_SYSCODE_NODECODE;
                String cacheKey = "", headerParam = "", reqParam = "";
                String cacheCreateKey = cacheCreate.key();
                if (cacheCreate.isReq()) {
                    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                    if (attributes != null) {
                        HttpServletRequest request = attributes.getRequest();
                        Map<String, String> headerParamMap = MessageUtil.getHeaderParams(request);
                        cacheKeyPrefix = CommonUtil.getCachePrefix(headerParamMap);
                        if (CollectionUtil.isNotEmpty(headerParamMap)) {
                            headerParam = JSON.toJSONString(headerParamMap);
                        }
                        Map<String, String> reqParamMap = MessageUtil.getReqParams(request);
                        if (CollectionUtil.isNotEmpty(reqParamMap)) {
                            reqParam = JSON.toJSONString(reqParamMap);
                        }
                    }
                } else {
                    Object[] arguments = new Object[args.length];
                    for (int i = 0; i < args.length; i++) {
                        if (!(args[i] instanceof ServletRequest || args[i] instanceof ServletResponse || args[i] instanceof MultipartFile)) {
                            arguments[i] = args[i];
                        }
                    }
                    if (ArrayUtil.isNotEmpty(arguments)) {
                        reqParam = JSON.toJSONString(arguments);
                    }
                }

                // 是否指定了缓存的key
                if (StringUtils.isNotEmpty(cacheCreateKey)) {
                    cacheKey = cacheKeyPrefix + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + cacheCreateKey;
                } else {
                    // 类路径 +  方法名 + 请求头参数 + 请求体参数 md5生成 redis key
                    String cachePathMethod = proceedingJoinPoint.getTarget().getClass().getName() + "." + signature.getName();
                    String cacheRawKey = "";
                    if (StringUtils.isNotEmpty(headerParam)) {
                        cacheRawKey += headerParam;
                    }
                    if (StringUtils.isNotEmpty(reqParam)) {
                        cacheRawKey += reqParam;
                    }
                    log.info("cacheRawKey: {}", cacheRawKey);
                    cacheKey = cacheKeyPrefix + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + cachePathMethod;
                    if (StringUtils.isNotEmpty(cacheRawKey)) {
                        cacheKey += ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + PasswordUtil.md5(cacheRawKey);
                    }
                }
                log.info("cacheKey: {}", cacheKey);

                // 查询操作
                String cacheRet = redisUtil.get(cacheKey);
                if (StringUtil.isEmpty(cacheRet)) {
                    // Redis 中不存在，则从数据库中查找，并保存到 Redis
                    log.info("<====== Cache 中不存在该记录，从DB中获取，并且放置缓存 ======>");
                    Object retData = proceedingJoinPoint.proceed(args);
                    if (ObjectUtil.isNotEmpty(retData)) {
                        long expired = cacheCreate.expired();
                        TimeUnit timeUnit = cacheCreate.timeUnit();
                        if (JSONUtil.isTypeJSONObject(retData + "")) {
                            Result result = JSON.to(Result.class, retData);
                            if (ObjectUtil.isNotEmpty(result)) {
                                if (10000 == result.getCode()) {
                                    if (expired > 0) {
                                        redisUtil.setEx(cacheKey, JSON.toJSONString(result), expired, timeUnit);
                                    } else {
                                        redisUtil.set(cacheKey, JSON.toJSONString(result));
                                    }
                                }
                            }
                        } else {
                            if (expired > 0) {
                                redisUtil.setEx(cacheKey, JSON.toJSONString(retData), expired, timeUnit);
                            } else {
                                redisUtil.set(cacheKey, JSON.toJSONString(retData));
                            }
                        }
                    }
                    return retData;
                }

                if (JSONUtil.isTypeJSONObject("{")) {
                    return JSON.to(Result.class, cacheRet);
                } else {
                    return proceedingJoinPoint.proceed(args);
                }
            }
        } catch (Throwable ex) {
            log.error("<====== AopCache 执行异常: {} ======>", ex);
        }
        return null;
    }

}
