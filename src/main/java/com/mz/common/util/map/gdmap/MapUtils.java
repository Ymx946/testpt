package com.mz.common.util.map.gdmap;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mz.common.util.RequestUtil;
import com.mz.common.util.baiduai.HttpUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取高德地图经纬度
 *
 * @author lixin.saho
 */
@Slf4j
public class MapUtils {

    //    private static final String KEY = "7d7e9768485f0ba4e6327d5a1ae2468b";//20230629之前老KEY

    //20230629之后新KEY
    // MapUtils:成功
    // DistanceUtil:成功
    // AmapController:成功
    // GaodeMapUtil:成功
    public static final String KEY = "ea7a0898b3eff26bd7883361b0013de5";//20230629之后新KEY

    private static final Pattern pattern = Pattern.compile("\"location\":\"(\\d+\\.\\d+),(\\d+\\.\\d+)\"");
    private static String API = "https://restapi.amap.com/v3/geocode/geo?key=<key>&address=<address>";

    static {
        init();
    }

    private static void init() {
//        System.out.println("高德地图工具类初始化");
//        System.out.println("api: {}"+API);
//        System.out.println("key: {}"+KEY);
        API = API.replaceAll("<key>", KEY);
    }

    public static double[] getLatAndLonByAddress(String address) {
        try {
            String requestUrl = API.replaceAll("<address>", URLEncoder.encode(address, "UTF-8"));
            RequestResult requestResult = RequestUtil.getJsonText(requestUrl, null);
            System.out.println(requestResult.getBody());
            if (200 != requestResult.getCode()) {
                return null;
            }
            requestUrl = requestResult.getBody();
//            log.info(requestUrl);
            if (requestUrl != null) {
                Matcher matcher = pattern.matcher(requestUrl);
                if (matcher.find() && matcher.groupCount() == 2) {
                    double[] gps = new double[2];
                    gps[0] = Double.valueOf(matcher.group(1));
                    gps[1] = Double.valueOf(matcher.group(2));
                    return gps;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getlocalByAddress(String address) {
        String coordinate = "";
        try {
            String requestUrl = API.replaceAll("<address>", URLEncoder.encode(address, "UTF-8"));
//        log.info("requestUrl： " + requestUrl);
//        RequestResult requestResult = RequestUtil.getJsonText(requestUrl, null);
//        log.info("requestResult： " + JSON.toJSONString(requestResult));
//        if (200 != requestResult.getCode()) {
//            return null;
//        }
//        requestUrl = requestResult.getBody();
//        log.info("requestUrl： " + requestUrl);
            String addrResult = HttpUtil.getConnByGet(requestUrl);
//        log.info("retResult： " + addrResult);
//        {"status":"1","info":"OK","infocode":"10000","count":"1","geocodes":[{"formatted_address":"广东省深圳市福田区天安数码城","country":"中国","province":"广东省","citycode":"0755","city":"深圳市","district":"福田区","township":[],"neighborhood":{"name":[],"type":[]},"building":{"name":[],"type":[]},"adcode":"440304","street":[],"number":[],"location":"114.031944,22.531093","level":"兴趣点"}]}
            JSONObject jsonObject = JSONObject.parseObject(addrResult);
            String status = jsonObject.getString("status");
            if (status.equals("1")) {
                JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("geocodes"));
                if (CollectionUtil.isNotEmpty(jsonArray)) {
                    JSONObject jsonObject2 = (JSONObject) jsonArray.stream().findFirst().get();
                    coordinate = jsonObject2.getString("location");
                }
            }
        } catch (Exception e) {
            log.error("根据地址获取信息失败", e);
        }
        return coordinate;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
//        double[] aaa = MapUtils.getLatAndLonByAddress("广东省深圳市福田区天安数码城创业科技大厦一期");
        String address = "山东省淄博市博山区白塔镇大海眼村1号楼1单元102号";
        double[] aaa = MapUtils.getLatAndLonByAddress(address);
        for (double cccc : aaa) {
            log.info(String.valueOf(cccc));
        }

        try {
            String lnglat = MapUtils.getlocalByAddress(address);
            System.out.println(lnglat);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}