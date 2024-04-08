package com.mz.common.util.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mz.common.ConstantsCacheUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public class LocalCache {
    private static final int MAX_CACHE_SIZE = 50000;

    private static final Cache<String, Object> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(ConstantsCacheUtil.REDIS_MILLISECONDS, TimeUnit.MILLISECONDS)
            .initialCapacity(50)
            .maximumSize(MAX_CACHE_SIZE)
            .build();

    public static Object getKey(String key) {
        return cache.getIfPresent(key);
    }

    public static void setKey(String key, Object value) {
        long size = cache.size();
        if (size >= MAX_CACHE_SIZE) {
            log.warn("local cache overflow (10000.)");
        }
        cache.put(key, value);
    }

    public static void removeKey(String key) {
        cache.invalidate(key);
    }

    public static void clearAll() {
        cache.invalidateAll();
    }

    public static ConcurrentMap<String, Object> queryAll() {
        return cache.asMap();
    }
}
