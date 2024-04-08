package com.mz.device;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import com.mz.common.util.json.JSONUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * API: http://101.34.116.221:8000/guide
 */
@Slf4j
public class DeviceUtil {
    public static String device_token = null;
    public static Date time = null;

    public static void main(String[] args) {
//        String deviceToken = getDeviceToken(Constants.ACCOUNT_TEST, Constants.PWD_TEST);
        String deviceToken = getDeviceToken(Constants.ACCOUNT_PROD, Constants.PWD_PROD);

//        System.out.println(getDeviceUserInfo(deviceToken, Constants.ACCOUNT_PROD, ""));

//        System.out.println(getDeviceInfo(deviceToken, "63212021"));

//        System.out.println(getDeviceInfo(deviceToken, "63212021"));
//        System.out.println(getDeviceLastData("63212021"));
        System.out.println(getDevicePestLastData("L06977393"));
//        postDeviceIMEIInfo(deviceToken, "866547052770826");
//        postDeviceIMEIInfo(deviceToken, "60161866");
//        System.out.printf(String.valueOf(postDeviceIMEIInfo(deviceToken, "L06977393")));
//        System.out.printf(String.valueOf(postDeviceIMEIInfoIsOnline(deviceToken, "L06977393")));
//        System.out.println(String.valueOf(postDeviceDataExtendData(deviceToken, "L06977393")));
//        postDeviceIMEIImageList(deviceToken, "60161866", 1, 10, "2021-01-12 00:00:00", "2024-01-11 00:00:00", "DESC");
//        postDeviceIMEIImageDebug(deviceToken, "K09576994", 0, 1, 0);
    }

    /**
     * 获取设备最新数据
     *
     * @return
     */
    public static JSONObject getDeviceLastData(String deviceId) {
        String requestUrl = Constants.DEVICE_LASTDATA_URL.replace("DEVICEID", deviceId);
        String retUrl = HttpUtil.get(requestUrl);
        if (!JSONUtil.isJson(retUrl)) {
            return null;
        }
        return JSONObject.parseObject(retUrl);
    }

    /**
     * 获取设备最新数据
     *
     * @return
     */
    public static JSONObject getDevicePestLastData(String deviceId) {
        String requestUrl = Constants.DEVICE_PEST_LASTDATA_URL.replace("DEVICEID", deviceId);
        String retUrl = HttpUtil.get(requestUrl);
        if (!JSONUtil.isJson(retUrl)) {
            return null;
        }
        return JSONObject.parseObject(retUrl);
    }

    /**
     * 获取token
     *
     * @param username
     * @param password
     * @return
     */

    public static String getDevice_token(String username, String password) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("username", username);
        paramMap.put("password", password);
        String retUrl = HttpUtil.post(Constants.DEVICE_GET_TOKEN, JSONObject.toJSONString(paramMap));
        if (!JSONUtil.isJson(retUrl)) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(retUrl);
        String device_token = null;
        if (null != jsonObject) {
            try {
                device_token = jsonObject.getString("token");
            } catch (JSONException e) {
                // 获取token失败
                log.error("获取token失败 :", e);
            }
        }
        return device_token;
    }

    /**
     * 服务器本地缓存token
     *
     * @param username
     * @param password
     * @return
     */
    public static String getDeviceToken(String username, String password) {
        if (null == device_token) {
            device_token = getDevice_token(username, password);
            time = getTime();
        } else {
            if (getTime().getTime() - time.getTime() >= 3600 * 1000) { // 每小时刷新一次
                device_token = null;
                device_token = getDeviceToken(username, password);
                time = getTime();
            }
        }
        return device_token;
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public static Map<?, ?> getDeviceUserInfo(String token, String userName, String keyword) {
        String requestUrl = Constants.DEVICE_GET_USERINFO.replace("USERNAME", userName);
        String retObj = HttpUtil.createGet(requestUrl).addHeaders(getMap(token)).execute().body();
        if (!JSONUtil.isJson(retObj)) {
            return null;
        }
        return JSONObject.parseObject(retObj);
    }

    /**
     * 获取要素列表
     *
     * @return
     */
    public static JSONArray getDeviceElementInfo(String token) {
        String requestUrl = Constants.DEVICE_GET_ELEMENT;
        String retObj = HttpUtil.createGet(requestUrl).addHeaders(getMap(token)).execute().body();
        if (!JSONUtil.isJson(retObj)) {
            return null;
        }
        return JSONObject.parseArray(retObj);
    }

    /**
     * 获取继电器列表
     *
     * @return
     */
    public static JSONArray getDeviceRelayInfo(String token) {
        String requestUrl = Constants.DEVICE_GET_RELAY;
        String retObj = HttpUtil.createGet(requestUrl).addHeaders(getMap(token)).execute().body();
        if (!JSONUtil.isJson(retObj)) {
            return null;
        }
        return JSONObject.parseArray(retObj);
    }

    /**
     * 获取设备信息
     *
     * @return
     */
    public static String getDeviceInfo(String token, String deviceId) {
        String requestUrl = Constants.DEVICE_GET_DEVICEINFO.replace("DEVICEID", deviceId);
        return HttpUtil.createGet(requestUrl).addHeaders(getMap(token)).execute().body();
    }

    /**
     * 获取设备数据及继电器状态
     *
     * @return
     */
    public static Map<?, ?> getDeviceDeviceState(String token, String deviceId) {
        String requestUrl = Constants.DEVICE_GET_DEVICESTATE.replace("DEVICEID", deviceId);
        String retObj = HttpUtil.createGet(requestUrl).addHeaders(getMap(token)).execute().body();
        if (!JSONUtil.isJson(retObj)) {
            return null;
        }
        return JSONObject.parseObject(retObj);
    }

    /**
     * 继电器控制- 有错误
     *
     * @return
     */
    public static Map<?, ?> postDeviceRelayController(String token, String deviceId, Integer relayNum, Integer relayState) {
        String requestUrl = Constants.DEVICE_GET_RELAY;
        Map<String, String> header = getMap(token);
        header.put("Content-Type", "application/json");

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("deviceId", deviceId);
        paramMap.put("relayNum", relayNum);
        paramMap.put("relayState", relayState);
        String retObj = HttpUtil.createPost(requestUrl).addHeaders(header).form(JSONObject.toJSONString(paramMap)).execute().body();
        if (!JSONUtil.isJson(retObj)) {
            return null;
        }
        return JSONObject.parseObject(retObj);
    }

    /**
     * 获取虫情设备信息
     *
     * @return
     */
    public static String postDeviceIMEIInfo(String token, String imei) {
        String requestUrl = Constants.DEVICE_GET_IMEIINFO.replace("IMEI", imei);
        String retObj = HttpUtil.createGet(requestUrl).addHeaders(getMap(token)).execute().body();
        if (!JSONUtil.isJson(retObj)) {
            return null;
        }
        return retObj;
    }

    /**
     * 获取虫情设备是否在线
     *
     * @return
     */
    public static boolean postDeviceIMEIInfoIsOnline(String token, String imei) {
        String requestUrl = Constants.DEVICE_GET_IMEIINFO_ONLINE.replace("IMEI", imei);
        String retObj = HttpUtil.createGet(requestUrl).addHeaders(getMap(token)).execute().body();
        return Boolean.TRUE.equals(retObj);
    }

    /**
     * 获取虫情设备监测数据
     *
     * @return
     */
    public static String postDeviceDataExtendData(String token, String imei) {
        String requestUrl = Constants.DEVICE_GET_DATAEXTEND_DATA.replace("IMEI", imei);
        String retObj = HttpUtil.createGet(requestUrl).addHeaders(getMap(token)).execute().body();
        if (!JSONUtil.isJson(retObj)) {
            return null;
        }
        return retObj;
    }

    /**
     * 获取最新虫情图片
     *
     * @return
     */
    public static String postDeviceImeiImage(String token, String imei) {
        String requestUrl = Constants.DEVICE_GET_IMEI_IMAGE.replace("IMEI", imei);
        String retObj = HttpUtil.createGet(requestUrl).addHeaders(getMap(token)).execute().body();
        if (!JSONUtil.isJson(retObj)) {
            return null;
        }
        return retObj;
    }

    /**
     * 获取最新虫情图片
     *
     * @return
     */
    public static String postDeviceLastIMEIImage(String token, String imei) {
        String requestUrl = Constants.DEVICE_GET_IMEI_IMAGE.replace("IMEI", imei);
        String retObj = HttpUtil.createGet(requestUrl).addHeaders(getMap(token)).execute().body();
        if (!JSONUtil.isJson(retObj)) {
            return null;
        }
        return retObj;
    }

    /**
     * 虫情设备调试
     *
     * @return
     */
    public static boolean postDeviceIMEIImageDebug(String token, String imei, Integer type, Integer index, Integer value) {
        String requestUrl = Constants.DEVICE_GET_IMEI_DEGUG.replace("IMEI", imei);
        Map<String, String> header = getMap(token);
        header.put("Content-Type", "application/json");

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("type", type);
        paramMap.put("index", index);
        paramMap.put("value", value);
        String retObj = HttpUtil.createPost(requestUrl).addHeaders(header).form(JSONObject.toJSONString(paramMap)).execute().body();
        return Boolean.TRUE.equals(retObj);
    }

    /**
     * 获取虫情图片列表
     *
     * @return
     */
    public static JSONArray postDeviceIMEIImageList(String token, String imei, Integer pageNum, Integer pageSize, String startTime, String endTime, String sort) {
        String requestUrl = Constants.DEVICE_GET_IMEI_IMAGE.replace("IMEI", imei);
        requestUrl = requestUrl + "?endTime=" + endTime + "&pageNum=" + pageNum + "&pageSize=" + pageSize + "&sort=" + sort + "&startTime=" + startTime;
        String retObj = HttpUtil.createGet(requestUrl).addHeaders(getMap(token)).execute().body();
        if (!JSONUtil.isJson(retObj)) {
            return null;
        }
        return JSONObject.parseArray(retObj);
    }

    private static Map<String, String> getMap(String token) {
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "*/*");
        headers.put("token", token);
        return headers;
    }

    // 获取当前系统时间 用来判断access_token是否过期
    public static Date getTime() {
        Date curDate = new Date();
        return curDate;
    }

}
