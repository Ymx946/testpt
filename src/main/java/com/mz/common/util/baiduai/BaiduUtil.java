package com.mz.common.util.baiduai;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class BaiduUtil {

    /**
     * 根据经纬度获取区域信息
     *
     * @param lng 经度
     * @param lat 纬度
     * @return
     * @throws IOException
     */
    public static Map<String, Object> getAreaInfo(Double lat, Double lng) throws IOException {
        StringBuilder resultData = new StringBuilder();
//        String url ="http://api.map.baidu.com/geocoder?location=" + lat + ","+ lng + "&output=json&pois=1";//老方法存在失败概率
//        String url = "http://api.map.baidu.com/reverse_geocoding/v3/?ak=OYjhspV6PoRryvf7H0EEVMb2WMT0rO4V&output=json&coordtype=wgs84ll&location=LOCATION" + lat + "," + lng;
        String url = BaiduConstant.REVERSE_GEOCODING_URL.replace("LOCATION", lat + "," + lng);
        URL myURL = null;
        URLConnection httpsConn = null;

        try {
            myURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        InputStreamReader isr = null;
        BufferedReader br = null;

        try {
            httpsConn = myURL.openConnection();// 不使用代理
            if (httpsConn != null) {
                isr = new InputStreamReader(httpsConn.getInputStream(), StandardCharsets.UTF_8);
                br = new BufferedReader(isr);
                String data = null;
                while ((data = br.readLine()) != null) {
                    resultData.append(data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (isr != null) {
                isr.close();
            }
            if (br != null) {
                br.close();
            }
        }
        //可以根据自己的需求自定义获取
        JSONObject addressComponent = JSON.parseObject(resultData.toString()).getJSONObject("result").getJSONObject("addressComponent");

        Map<String, Object> area = new HashMap<>();
        area.put("province", addressComponent.get("province"));//山东省
        area.put("city", addressComponent.get("city"));//淄博市
        area.put("district", addressComponent.get("district"));//沂源县
        area.put("town", addressComponent.get("town"));//燕崖镇（可能为空）
        area.put("street", addressComponent.get("street"));//燕山路
        area.put("street_number", addressComponent.get("street_number"));
        area.put("town_code", addressComponent.get("town_code"));//燕崖镇代码（可能为空）
        area.put("adcode", addressComponent.get("adcode"));//沂源县代码
        area.put("formatted_address", addressComponent.get("formatted_address"));//山东省淄博市沂源县

        return area;

    }

    public static boolean checkAreaInfo(String coordinate, String areaCode) throws IOException {
        if (!StringUtils.isEmpty(coordinate)) {
            String[] coordinateArr = coordinate.split(",");
            if (coordinateArr != null && coordinateArr.length == 2) {
                Double lng = Double.valueOf(coordinateArr[0].trim());
                Double lat = Double.valueOf(coordinateArr[1].trim());
                Map<String, Object> areaInfo = BaiduUtil.getAreaInfo(lat, lng);
//                String district =  (String)areaInfo .get("district");
                String adcode = (String) areaInfo.get("adcode");
                //不在沂源
                return adcode != null && adcode.equals(areaCode);
            } else {
                return false; //坐标格式不对
            }
        } else {
            return true; //坐标为空
        }

    }

    public static void main(String[] args) throws IOException {
        //演示
//        36.113946,118.202099
//        120.08041666666666,30.28144775390625
//        Map<String, Object> areaInfo = getAreaInfo(30.28144775390625, 120.08041666666666);
//        Map<String, Object> areaInfo = getAreaInfo(36.113946, 118.202099);
        String coordinate = "118.202099,36.113946";
//        String coordinate = "120.08041666666666,30.28144775390625";
        String[] coordinateArr = coordinate.split(",");
        Double lng = Double.valueOf(coordinateArr[0].trim());
        Double lat = Double.valueOf(coordinateArr[1].trim());
        Map<String, Object> areaInfo = BaiduUtil.getAreaInfo(lat, lng);
        System.out.println(areaInfo.get("district"));
        System.out.println(areaInfo.get("adcode"));
    }
}