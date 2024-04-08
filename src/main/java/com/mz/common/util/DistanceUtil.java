package com.mz.common.util;

import com.alibaba.fastjson.JSONObject;
import com.mz.common.util.map.gdmap.MapUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DistanceUtil {
    public static final double MEETING_DISTANCE = 100.0;//会议签到距离100米
    private static final double EARTH_RADIUS = 6378137;
//    private static final String KEY = "44049791a9db88b4bcdfd84b418227f8";//--20230630修改wf:引用 com.mz.common.util.map.gdmap.MapUtils.KEY

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
     *
     * @param lng1
     * @param lat1
     * @param lng2
     * @param lat2
     * @return
     */
    public static double GetDistance(double lng1, double lat1, double lng2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return s;
    }


    /**
     * @param args
     */
    public static void main(String[] args) throws IOException {
        // TODO 自动生成方法存根

//        120.087132,30.287188
//        120.089899,30.290369
//        120.080409 30.281294
//        120.080411 30.281296
//        double distance = GetDistance(120.087506,30.281294,120.080411,30.281296);
//        System.out.println("Distance is:"+distance);
        String lng = "120.08066243489583";
        String lat = "30.28123806423611";
        System.out.println(getAdd(lng, lat));

//        try {
//            String text = FileUtils.readFileToString(new File("F:\\eclipse-workspace-mangzhong\\future-rural-server\\src\\main\\resources\\mapper\\zhejiangBfhunt\\SysOrganizationMapper.xml"), "UTF-8");
//            String xml = xml2JSON(text);
//            System.out.println(xml);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

    }


    /**
     * 根据经纬度获取省市区
     *
     * @param lng
     * @param lat
     * @return
     */
    public static String getAdd(String lng, String lat) throws IOException {

        String urlString = "https://restapi.amap.com/v3/geocode/regeo?output=JSON&location=" + lng + "," + lat + "&key=" + MapUtils.KEY + "&radius=1000&extensions=all";
        URL url = new URL(urlString);    // 把字符串转换为URL请求地址
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 打开连接
        connection.connect();// 连接会话
        // 获取输入流
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {// 循环读取流
            sb.append(line);
        }
        br.close();// 关闭流
        connection.disconnect();// 断开连接
        String areaCode = "";
        JSONObject responseobj = JSONObject.parseObject(sb.toString());
        String status = responseobj.get("status").toString();
        String info = responseobj.get("info").toString();
        System.out.println("--------------经纬度解析行政区域-------------------" + status + info);
        if (status.equals("1")) {
            JSONObject regeocode = responseobj.getJSONObject("regeocode");
            JSONObject addressComponent = regeocode.getJSONObject("addressComponent");
            areaCode = addressComponent.getString("adcode");
        }
        return areaCode;
    }

    /**
     * 转换一个xml格式的字符串到json格式
     *
     * @param xml xml格式的字符串
     * @return 成功返回json 格式的字符串;失败反回null
     */
    @SuppressWarnings("unchecked")
    public static String xml2JSON(String xml) {
        JSONObject obj = new JSONObject();
        try {
            InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
            SAXBuilder sb = new SAXBuilder();
            Document doc = sb.build(is);
            Element root = doc.getRootElement();
            obj.put(root.getName(), iterateElement(root));
            return obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 一个迭代方法
     *
     * @param element : org.jdom.Element
     * @return java.util.Map 实例
     */
    @SuppressWarnings("unchecked")
    private static Map iterateElement(Element element) {
        List jiedian = element.getChildren();
        Element et = null;
        Map obj = new HashMap();
        List list = null;
        for (int i = 0; i < jiedian.size(); i++) {
            list = new LinkedList();
            et = (Element) jiedian.get(i);
            if (et.getTextTrim().equals("")) {
                if (et.getChildren().size() == 0) continue;
                if (obj.containsKey(et.getName())) {
                    list = (List) obj.get(et.getName());
                }
                list.add(iterateElement(et));
                obj.put(et.getName(), list);
            } else {
                if (obj.containsKey(et.getName())) {
                    list = (List) obj.get(et.getName());
                }
                list.add(et.getTextTrim());
                obj.put(et.getName(), list);
            }
        }
        return obj;
    }

}
