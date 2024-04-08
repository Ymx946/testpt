package com.mz.common.datasysn.util;

import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.util.StringUtil;
import com.mz.common.datasysn.Constants;
import com.mz.common.datasysn.entity.CityDoAppUserInfo;
import com.mz.common.datasysn.entity.CityDoWebUserInfo;
import com.mz.common.util.wxaes.HttpKit;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CityDoUtil {
    public static String citydo_access_token = null;
    public static String citydo_time = null;

    public static void main(String[] args) {
        String accessToken = getAccessToken("TEST", "WLXC_TEST", "u3ou6yww9e4epxd7u315sngn");
        log.info(accessToken);
        log.info(JSON.toJSONString(getWebUserInfo("test", accessToken, "TEST")));
        log.info(JSON.toJSONString(getAppUserInfo("test", accessToken, "TEST")));
    }

    /**
     * 获取token
     *
     * @return
     */
    public static String getAccess_token(String profile, String accessKey, String secretKey) {
        String url = "";
        if ("agri".equalsIgnoreCase(profile)) {//省农业厅
            url = Constants.CITYDO_URL_AGRI + Constants.CITYDO_ISSUEACCESSTOKEN_URL;
        } else {
            url = Constants.CITYDO_URL_TEST + Constants.CITYDO_ISSUEACCESSTOKEN_URL;
        }

        String jsonObjectStr = HttpKit.sendRequest(url, JSON.toJSONString(new HashMap<String, String>() {{
            put("accessKey", accessKey);
            put("secretKey", secretKey);
        }}), null, "POST");

        if (StringUtil.isEmpty(jsonObjectStr) || jsonObjectStr.equals("{}")) {
            log.info("获取token失败, token为空");
            return null;
        }

        Map<String, Object> retMap = (Map<String, Object>) JSON.parse(jsonObjectStr);
        if (retMap.containsKey("code") && (int) retMap.get("code") == 0) {
            Map<String, Object> subMap = (Map<String, Object>) JSON.parse(retMap.get("data") + "");
            if (subMap.containsKey("accessToken")) {
                return subMap.get("accessToken") + "";
            }
        }
        return null;
    }

    public static String getAccessToken(String profile, String accessKey, String secretKey) {
        if (citydo_access_token == null) {
            citydo_access_token = getAccess_token(profile, accessKey, secretKey);
            citydo_time = getTimeStr();
        } else {
            if (!citydo_time.substring(0, 13).equals(getTimeStr().substring(0, 13))) { // 每小时刷新一次
                citydo_access_token = null;
                citydo_access_token = getAccess_token(profile, accessKey, secretKey);
                citydo_time = getTimeStr();
            }
        }
        return citydo_access_token;
    }

    public static CityDoAppUserInfo getAppUserInfo(String profile, String accessToken, String ticket) {
        String url = "", clientId = "";
        if ("agri".equalsIgnoreCase(profile)) {
            url = Constants.CITYDO_URL_AGRI + Constants.CITYDO_APP_USERINFO_URL + "?accessToken=" + accessToken + "&ticket=" + ticket;
        } else {
            url = Constants.CITYDO_URL_AGRI + Constants.CITYDO_APP_USERINFO_URL + "?accessToken=" + accessToken + "&ticket=" + ticket;
        }

        String retObj = null;
        try {
            retObj = HttpKit.get(url);
        } catch (Exception e) {
            log.info("获取用户信息失败，", e);
        }
        if (StringUtil.isEmpty(retObj) || retObj.equals("{}")) {
            log.info("获取用户信息失败, 返回信息为空");
            return null;
        }

        Map<String, Object> retMap = (Map<String, Object>) JSON.parse(retObj);
        if (retMap.containsKey("code") && (int) retMap.get("code") == 0) {
            return com.alibaba.fastjson.JSON.parseObject(retMap.get("data") + "", CityDoAppUserInfo.class);
        }
        return null;
    }

    public static CityDoWebUserInfo getWebUserInfo(String profile, String accessToken, String ticket) {
        String url = "", clientId = "";
        if ("agri".equalsIgnoreCase(profile)) {
            url = Constants.CITYDO_URL_AGRI + Constants.CITYDO_WEB_USERINFO_URL + "?accessToken=" + accessToken + "&ticket=" + ticket;
        } else {
            url = Constants.CITYDO_URL_AGRI + Constants.CITYDO_WEB_USERINFO_URL + "?accessToken=" + accessToken + "&ticket=" + ticket;
        }

        String retObj = null;
        try {
            retObj = HttpKit.get(url);
        } catch (Exception e) {
            log.info("获取用户信息失败，", e);
        }
        if (StringUtil.isEmpty(retObj) || retObj.equals("{}")) {
            log.info("获取用户信息失败, 返回信息为空");
            return null;
        }

        Map<String, Object> retMap = (Map<String, Object>) JSON.parse(retObj);
        if (retMap.containsKey("code") && (int) retMap.get("code") == 0) {
            return com.alibaba.fastjson.JSON.parseObject(retMap.get("data") + "", CityDoWebUserInfo.class);
        }
        return null;
    }

    public synchronized static String getTimeStr() {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(dt);
    }

}
