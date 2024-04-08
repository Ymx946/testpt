package com.mz.common.util;


import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.*;


/**
 * 萤石云视频
 *
 * @author john
 */

@Component
public class YsUtil {
    private static final Logger log = LogManager.getLogger(YsUtil.class);
    private static final String appKey = "6921bd29b5594a3fbdd744b4abdf2079";
    private static final String secret = "5e0777d93591713d805cb25e00a35464";
    private static final String accessToken = "at.7p9lpyoiamn2iu5l18immfuj5lt3lduh-5fj9n5mn5s-0m2lys5-morbuutok";//测试使用

    /**
     * 获取accessToken
     *
     * @return
     */
    public static String getAccessToken() {
        String requestUrl = "https://open.ys7.com/api/lapp/token/get";
        Map<String, Object> requestUrlParam = new HashMap<String, Object>();
        requestUrlParam.put("appKey", appKey);
        requestUrlParam.put("appSecret", secret);
        //发送post请求读取调用萤石云   https://open.ys7.com/api/lapp/token/get
        JSONObject jsonObject = JSONObject.parseObject(RequestUtil.sendPost(requestUrl, maptoString(requestUrlParam)));
        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
        String ysToken = jsonObjectData.get("accessToken") != null ? (String) jsonObjectData.get("accessToken") : "";
        return ysToken;
    }

    /**
     * 获取摄像头列表
     *
     * @return
     */
    public static JSONObject getCameraList(String accessToken) {
        String requestUrl = "https://open.ys7.com/api/lapp/camera/list";
        Map<String, Object> requestUrlParam = new HashMap<String, Object>();
        requestUrlParam.put("accessToken", accessToken);
        //发送post请求读取调用萤石云
        JSONObject jsonObject = JSONObject.parseObject(RequestUtil.sendPost(requestUrl, maptoString(requestUrlParam)));
        System.out.println(jsonObject);
        return jsonObject;
    }

    /**
     * 添加设备
     *
     * @return
     */
    public static JSONObject addDevice(String accessToken, String deviceSerial, String validateCode) {
        String requestUrl = "https://open.ys7.com/api/lapp/device/add";
        Map<String, Object> requestUrlParam = new HashMap<String, Object>();
        requestUrlParam.put("accessToken", accessToken);
        requestUrlParam.put("deviceSerial", deviceSerial);
        requestUrlParam.put("validateCode", validateCode);
        //发送post请求读取调用萤石云
        JSONObject jsonObject = JSONObject.parseObject(RequestUtil.sendPost(requestUrl, maptoString(requestUrlParam)));
        System.out.println(jsonObject);
        return jsonObject;
    }

    /**
     * 获取用户下直播视频列表
     *
     * @return
     */
    public static JSONObject videoList(String accessToken, String pageStart, String pageSize) {
        String requestUrl = "https://open.ys7.com/api/lapp/live/video/list";
        Map<String, Object> requestUrlParam = new HashMap<String, Object>();
        requestUrlParam.put("accessToken", accessToken);
        requestUrlParam.put("pageStart", pageStart);
        requestUrlParam.put("pageSize", pageSize);
        //发送post请求读取调用萤石云
        JSONObject jsonObject = JSONObject.parseObject(RequestUtil.sendPost(requestUrl, maptoString(requestUrlParam)));
        System.out.println(jsonObject);
        return jsonObject;
    }

    /**
     * 批量开通直播功能开通直播功能
     *
     * @return
     */
    public static JSONObject openVideo(String accessToken, String source) {
        String requestUrl = "https://open.ys7.com/api/lapp/live/video/open";
        Map<String, Object> requestUrlParam = new HashMap<String, Object>();
        requestUrlParam.put("accessToken", accessToken);
        requestUrlParam.put("source", source);//直播源，[设备序列号]:[通道号],[设备序列号]:[通道号]的形式，例如427734222:1,423344555:3，均采用英文符号，限制50个
        //发送post请求读取调用萤石云
        JSONObject jsonObject = JSONObject.parseObject(RequestUtil.sendPost(requestUrl, maptoString(requestUrlParam)));
        System.out.println(jsonObject);
        return jsonObject;
    }

    /**
     * 获取直播地址
     *
     * @return
     */
    public static JSONObject getAddress(String accessToken, String source) {
        String requestUrl = "https://open.ys7.com/api/lapp/live/address/get";
        Map<String, Object> requestUrlParam = new HashMap<String, Object>();
        requestUrlParam.put("accessToken", accessToken);
        requestUrlParam.put("source", source);//直播源，[设备序列号]:[通道号],[设备序列号]:[通道号]的形式，例如427734222:1,423344555:3，均采用英文符号，限制50个
        //发送post请求读取调用萤石云
        JSONObject jsonObject = JSONObject.parseObject(RequestUtil.sendPost(requestUrl, maptoString(requestUrlParam)));
        System.out.println(jsonObject);
        return jsonObject;
    }

    /**
     * 根据设备序列号查询设备能力集
     *
     * @return
     */
    public static JSONObject deviceCapacity(String accessToken, String deviceSerial) {
        String requestUrl = "https://open.ys7.com/api/lapp/device/capacity";
        Map<String, Object> requestUrlParam = new HashMap<String, Object>();
        requestUrlParam.put("accessToken", accessToken);
        requestUrlParam.put("deviceSerial", deviceSerial);//设备序列号,存在英文字母的设备序列号，字母需为大写
        //发送post请求读取调用萤石云
        JSONObject jsonObject = JSONObject.parseObject(RequestUtil.sendPost(requestUrl, maptoString(requestUrlParam)));
        System.out.println(jsonObject);
        return jsonObject;
    }


    /**
     * 开始云控制
     *
     * @return
     */
    public static JSONObject startDevice(String accessToken, String deviceSerial, String channelNo, String direction, String speed) {
        String requestUrl = "https://open.ys7.com/api/lapp/device/ptz/start";
        Map<String, Object> requestUrlParam = new HashMap<String, Object>();
        requestUrlParam.put("accessToken", accessToken);
        requestUrlParam.put("deviceSerial", deviceSerial);//设备序列号,存在英文字母的设备序列号，字母需为大写
        requestUrlParam.put("channelNo", channelNo);//通道号
        requestUrlParam.put("direction", direction);//操作命令：0-上，1-下，2-左，3-右，4-左上，5-左下，6-右上，7-右下，8-放大，9-缩小，10-近焦距，11-远焦距
        requestUrlParam.put("speed", speed);
        //发送post请求读取调用萤石云
        JSONObject jsonObject = JSONObject.parseObject(RequestUtil.sendPost(requestUrl, maptoString(requestUrlParam)));
        System.out.println(jsonObject);
        return jsonObject;
    }

    /**
     * 停止云控制
     *
     * @return
     */
    public static JSONObject stopDevice(String accessToken, String deviceSerial, String channelNo) {
        String requestUrl = "https://open.ys7.com/api/lapp/device/ptz/stop";
        Map<String, Object> requestUrlParam = new HashMap<String, Object>();
        requestUrlParam.put("accessToken", accessToken);
        requestUrlParam.put("deviceSerial", deviceSerial);//设备序列号,存在英文字母的设备序列号，字母需为大写
        requestUrlParam.put("channelNo", channelNo);//通道号
        //发送post请求读取调用萤石云
        JSONObject jsonObject = JSONObject.parseObject(RequestUtil.sendPost(requestUrl, maptoString(requestUrlParam)));
        System.out.println(jsonObject);
        return jsonObject;
    }


    /**
     * 获取数据格式转换
     *
     * @return
     */
    public static String maptoString(Map<String, Object> params) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = params.get(key);
            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    /**
     * 测试
     *
     * @return
     */
    public static void main(String[] args) {
//        addDevice(accessToken,"F73091236","NOXDJI");
//        addDevice(accessToken,"F76328878","GCAOCE");
//        addDevice(accessToken,"F73091236","123456ABC");
//        addDevice(accessToken,"F76328878","123456ABC");
//        deviceCapacity(accessToken,"F73091236");
        startDevice(accessToken, "F76328878", "1", "8", "1");
//        startDevice(accessToken,"F76328878","1","9","1");
//        stopDevice(accessToken,"F76328878","1");
//        videoList(accessToken,"1","10");

//        addDevice(accessToken,"F73091236","NOXDJI");//添加设备
//        openVideo(accessToken,"F73091236:1,F76328878:1");//开通直播
//        getAddress(accessToken,"F73091236:1,F76328878:1");//获取直播地址hls、rtmp
    }
}