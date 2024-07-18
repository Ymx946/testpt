package com.mz.framework.service.common.impl;

import cn.hutool.core.date.DateUtil;
import com.mz.common.Constants;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.util.PasswordUtil;
import com.mz.framework.service.common.CommonService;
import com.mz.framework.util.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service("CommonServiceImpl")
public class CommonServiceImpl implements CommonService {

    @Autowired
    private RedisUtil redisUtil;

    @Value("${spring.profiles.active}")
    private String profile;

    public Map<String, Object> getOpenUserAccessToken(String clientId) {
        Long tokenExpiresIn = 2 * 60 * 60L; // 2小时
        String curDate = DateUtil.formatDateTime(new Date());
        String accessToken = PasswordUtil.md5(clientId + "accessToken" + curDate);
        redisUtil.setEx(ConstantsCacheUtil.OPENUSER_ACCESS_TOKEN, accessToken, tokenExpiresIn, TimeUnit.SECONDS);

        String refreshToken = "";
        if ("prod".equalsIgnoreCase(profile)) {
            refreshToken = PasswordUtil.md5(com.mz.common.Constants.SERVER_ID_AGRI + "refreshToken" + curDate);
        } else {
            refreshToken = PasswordUtil.md5(Constants.SERVER_ID_TEST + "refreshToken" + curDate);
        }

        Long refreshTokenExpiresIn = 2 * tokenExpiresIn;
        redisUtil.setEx(ConstantsCacheUtil.OPENUSER_REFRESH_TOKEN, refreshToken, refreshTokenExpiresIn, TimeUnit.SECONDS);

        Map<String, Object> retMap = new HashMap<>();
        retMap.put("token_type", "bearer");
        retMap.put("access_token", accessToken);
        retMap.put("expires_in", tokenExpiresIn);
        retMap.put("refresh_token", refreshToken);
        return retMap;
    }

    @Override
    public Map<String, Object> getAccessToken() {

        Long tokenExpiresIn = 2 * 60 * 60L; // 2小时
        String accessToken = "";
        String curDate = DateUtil.formatDateTime(new Date());
        if ("agri".equalsIgnoreCase(profile)) {
            accessToken = PasswordUtil.md5(Constants.SERVER_ID_AGRI + "accessToken" + curDate);
        } else {
            accessToken = PasswordUtil.md5(Constants.SERVER_ID_TEST + "accessToken" + curDate);
        }
        redisUtil.setEx(ConstantsCacheUtil.MZSZ_ACCESS_TOKEN, accessToken, tokenExpiresIn, TimeUnit.SECONDS);

        String refreshToken = "";
        if ("agri".equalsIgnoreCase(profile)) {
            refreshToken = PasswordUtil.md5(Constants.SERVER_ID_AGRI + "refreshToken" + curDate);
        } else {
            refreshToken = PasswordUtil.md5(Constants.SERVER_ID_TEST + "refreshToken" + curDate);
        }
        redisUtil.setEx(ConstantsCacheUtil.MZSZ_REFRESH_TOKEN, refreshToken, 24 * 60 * 60L, TimeUnit.SECONDS);

        Map<String, Object> retMap = new HashMap<>();
        retMap.put("tokenType", "bearer");
        retMap.put("expiresIn", tokenExpiresIn);
        retMap.put("accessToken", accessToken);
        retMap.put("refreshToken", refreshToken);
        return retMap;
    }

    @Override
    public Map<String, Object> getAccessToken(String appId, String appKey) {

        Long tokenExpiresIn = 2 * 60 * 60L; // 2小时
        String accessToken = "";
        String curDate = DateUtil.formatDateTime(new Date());
        if ("prod".equalsIgnoreCase(profile)) {
            accessToken = PasswordUtil.md5(appId + "accessToken" + curDate);
        } else {
            accessToken = PasswordUtil.md5(appId + "accessToken" + curDate);
        }
        redisUtil.setEx(ConstantsCacheUtil.MZSZ_ACCESS_TOKEN, accessToken, tokenExpiresIn, TimeUnit.SECONDS);

        String refreshToken = "";
        if ("prod".equalsIgnoreCase(profile)) {
            refreshToken = PasswordUtil.md5(appId + "refreshToken" + curDate);
        } else {
            refreshToken = PasswordUtil.md5(appId + "refreshToken" + curDate);
        }
        redisUtil.setEx(ConstantsCacheUtil.MZSZ_REFRESH_TOKEN, refreshToken, 24 * 60 * 60L, TimeUnit.SECONDS);

        Map<String, Object> retMap = new HashMap<>();
        retMap.put("tokenType", "bearer");
        retMap.put("expiresIn", tokenExpiresIn);
        retMap.put("accessToken", accessToken);
        retMap.put("refreshToken", refreshToken);
        return retMap;
    }
}
