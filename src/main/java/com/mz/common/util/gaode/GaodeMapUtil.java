package com.mz.common.util.gaode;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mz.common.ConstantsUtil;
import com.mz.common.util.GPSUtil;
import com.mz.common.util.map.gdmap.MapUtils;
import com.mz.common.util.wxaes.HttpKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 高德地图工具类 <br />
 * https://lbs.amap.com/api/webservice/guide/api/direction
 *
 * @Description:
 * @author: yangh
 * @date: 2019年11月1日 下午12:35:26
 */
@Slf4j
public class GaodeMapUtil {
//    private static final String KEY = "44049791a9db88b4bcdfd84b418227f8";//--20230630修改wf:引用 com.mz.common.util.map.gdmap.MapUtils.KEY
    /**
     * 下方策略仅返回一条路径规划结果
     * <p>
     * 0，速度优先，不考虑当时路况，此路线不一定距离最短
     * <p>
     * 1，费用优先，不走收费路段，且耗时最少的路线
     * <p>
     * 2，距离优先，不考虑路况，仅走距离最短的路线，但是可能存在穿越小路/小区的情况
     * <p>
     * 3，速度优先，不走快速路，例如京通快速路（因为策略迭代，建议使用13）
     * <p>
     * 4，躲避拥堵，但是可能会存在绕路的情况，耗时可能较长
     * <p>
     * 5，多策略（同时使用速度优先、费用优先、距离优先三个策略计算路径）。
     * <p>
     * 其中必须说明，就算使用三个策略算路，会根据路况不固定的返回一~三条路径规划信息。
     * <p>
     * 6，速度优先，不走高速，但是不排除走其余收费路段
     * <p>
     * 7，费用优先，不走高速且避免所有收费路段
     * <p>
     * 8，躲避拥堵和收费，可能存在走高速的情况，并且考虑路况不走拥堵路线，但有可能存在绕路和时间较长
     * <p>
     * 9，躲避拥堵和收费，不走高速
     */
    // API前缀
    private static final String BASE_PATH = "https://restapi.amap.com/v3";
    /**
     * 下方策略返回多条路径规划结果
     * <p>
     * 10，返回结果会躲避拥堵，路程较短，尽量缩短时间，与高德地图的默认策略也就是不进行任何勾选一致
     * <p>
     * 11，返回三个结果包含：时间最短；距离最短；躲避拥堵 （由于有更优秀的算法，建议用10代替）
     * <p>
     * 12，返回的结果考虑路况，尽量躲避拥堵而规划路径，与高德地图的“躲避拥堵”策略一致
     * <p>
     * 13，返回的结果不走高速，与高德地图“不走高速”策略一致
     * <p>
     * 14，返回的结果尽可能规划收费较低甚至免费的路径，与高德地图“避免收费”策略一致
     * <p>
     * 15，返回的结果考虑路况，尽量躲避拥堵而规划路径，并且不走高速，与高德地图的“躲避拥堵&不走高速”策略一致
     * <p>
     * 16，返回的结果尽量不走高速，并且尽量规划收费较低甚至免费的路径结果，与高德地图的“避免收费&不走高速”策略一致
     * <p>
     * 17，返回路径规划结果会尽量的躲避拥堵，并且规划收费较低甚至免费的路径结果，与高德地图的“躲避拥堵&避免收费”策略一致
     * <p>
     * 18，返回的结果尽量躲避拥堵，规划收费较低甚至免费的路径结果，并且尽量不走高速路，与高德地图的“避免拥堵&避免收费&不走高速”策略一致
     * <p>
     * 19，返回的结果会优先选择高速路，与高德地图的“高速优先”策略一致
     * <p>
     * 20，返回的结果会优先考虑高速路，并且会考虑路况躲避拥堵，与高德地图的“躲避拥堵&高速优先”策略一致
     */
    public static int STRATEGY = 2;
    /**
     * 分隔LIST的因子
     */
    public static int LIST_DIVISOR = 20;

    /**
     * String lng = "113.97405", lat = "22.69368"; <br />
     * 经度和纬度用","分割，经度在前，纬度在后，经纬度小数点后不得超过6位。<br />
     *
     * @param lnglat -- "113.97405,22.69368"
     * @param type   -- "city"
     * @return
     */
    public static String lnglat2code(String lnglat, String type) {
        String adcode = "";
        String address = "";
        try {
            String url = BASE_PATH + "/geocode/regeo?key=" + MapUtils.KEY + "&output=JSON&location=" + lnglat;
            String retJson = getHttpResponse(url);
            if (StringUtils.isNoneBlank(retJson)) {
                JSONObject jsonObject = JSONObject.parseObject(retJson);
                if ("1".equals(jsonObject.getString("status"))) {
                    JSONObject regeocode = jsonObject.getJSONObject("regeocode");
                    JSONObject addressComponent = regeocode.getJSONObject("addressComponent");
                    adcode = addressComponent.getString("adcode");
                    if (adcode.length() <= 5) {
                        log.error("地理位置编码解析错误，原因：" + jsonObject);
                        return null;
                    }
                    JSONObject neighborhood = addressComponent.getJSONObject("neighborhood");
                    address = neighborhood.getString("name");
                    if ("[]".equals(address)) {
                        address = addressComponent.getJSONObject("streetNumber").getString("street");
                        if ("[]".equals(address)) {
                            address = regeocode.getString("formatted_address");
                            if (address.indexOf("镇") > -1) {
                                address = address.split("镇")[1];
                            } else if (address.indexOf("县") > -1) {
                                address = address.split("县")[1];
                            } else if (address.indexOf("市") > -1) {
                                address = address.split("市")[1];
                            }
                        }
                    }
                } else {
                    log.info("地理位置编码解析失败，原因：" + jsonObject.getString("info"));
                    return null;
                }
                if ("city".equals(type)) {
                    adcode = adcode.substring(0, 4) + "00";
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return adcode + "," + address;
    }

    public static String positionBylnglat(String lnglat) {
        String address = "";
        try {
            String url = BASE_PATH + "/geocode/regeo?key=" + MapUtils.KEY + "&output=JSON&location=" + lnglat;
            String retJson = getHttpResponse(url);
            if (StringUtils.isNoneBlank(retJson)) {
                JSONObject jsonObject = JSONObject.parseObject(retJson);
                if ("1".equals(jsonObject.getString("status"))) {
                    JSONObject regeocode = jsonObject.getJSONObject("regeocode");
                    address = regeocode.getString("formatted_address");
                } else {
                    log.info("地理位置编码解析失败，原因：" + jsonObject.getString("info"));
                    return null;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return address;
    }

    /**
     * 高德地图WebAPI : 驾车路径规划 计算两地之间行驶的距离(米) <br />
     *
     * @param origins:起始坐标
     * @param destination:终点坐标
     */
    public static String distance(String origins, String destination) {
        String url = BASE_PATH + "/direction/driving?" + "origin=" + origins + "&destination=" + destination + "&strategy=" + STRATEGY + "&extensions=base&key=" + MapUtils.KEY;
        try {
            String retJson = getHttpResponse(url);
            if (StringUtils.isNoneBlank(retJson)) {
                JSONObject jsonObject = JSONObject.parseObject(retJson);
                JSONArray pathArray = jsonObject.getJSONObject("route").getJSONArray("paths");
                return pathArray.getJSONObject(0).getString("distance");
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    /**
     * 高德地图WebAPI : 驾车路径规划 计算两地之间行驶的距离(米) <br />
     *
     * @param @param         origins:起始坐标
     * @param destinationMap key：supplierId, val:终点坐标
     * @return
     */
    public static Map<String, String> distanceBatch(String origin, Map<String, String> destinationMap) {
        Map<String, String> retMap = new HashMap<String, String>();
        Map<String, String> retDistanceMap = new HashMap<String, String>();
        try {
            List<SubGaoDeMapParam> subMapParamList = new ArrayList<SubGaoDeMapParam>();
            destinationMap.forEach((key, val) -> {
                SubGaoDeMapParam subMapParam = new SubGaoDeMapParam();
                subMapParam.setUrl("/v3/direction/driving?" + "origin=" + origin + "&destination=" + val + "&strategy=" + STRATEGY + "&supplierId=1234&extensions=base&key=" + MapUtils.KEY);
                subMapParamList.add(subMapParam);
            });

            int allCount = subMapParamList.size();
            int allPage = (int) (Math.ceil(allCount / (LIST_DIVISOR + 0.0d)));
            for (int page = 1; page <= allPage; page++) {
                List<SubGaoDeMapParam> subDestinationList = new ArrayList<SubGaoDeMapParam>();
                if (page == allPage) {
                    subDestinationList = subMapParamList.subList((page - 1) * LIST_DIVISOR, allCount);
                } else {
                    subDestinationList = subMapParamList.subList((page - 1) * LIST_DIVISOR, page * LIST_DIVISOR);
                }
                retDistanceMap.putAll(getDistanceBatchMap(origin, subDestinationList));
            }

            destinationMap.forEach((key, val) -> {
                retDistanceMap.forEach((subKey, subVal) -> {
                    if (val.equals(subKey.split("_")[1])) {
                        retMap.put(key, subVal);
                        ConstantsUtil.DISTANCE_MAP.put(key + "@" + subKey, subVal);
                    }
                });
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return retMap;
    }

    public static Map<String, String> lnglat2code(String lnglat) {
        String adcode = "";
        String address = "";
        Map<String, String> resultMap = new HashMap<>();
        try {
            String url = BASE_PATH + "/geocode/regeo?key=" + MapUtils.KEY + "&output=JSON&location=" + lnglat;
            String retJson = getHttpResponse(url);
            if (StringUtils.isNoneBlank(retJson)) {
                JSONObject jsonObject = JSONObject.parseObject(retJson);
                if ("1".equals(jsonObject.getString("status"))) {
                    JSONObject regeocode = jsonObject.getJSONObject("regeocode");
                    JSONObject addressComponent = regeocode.getJSONObject("addressComponent");
                    adcode = addressComponent.getString("adcode");
                    String towncode = addressComponent.getString("towncode");
                    if (adcode.length() <= 5) {
                        log.error("地理位置编码解析错误，原因：" + jsonObject);
                        return null;
                    }
                    address = regeocode.getString("formatted_address");
                    resultMap.put("detailAddress", address);

                    String provinceId = adcode.substring(0, 2) + "0000";
                    String cityId = adcode.substring(0, 4) + "00";
                    resultMap.put("province", provinceId);
                    resultMap.put("city", cityId);
                    resultMap.put("county", adcode);
                    // 共9位--街道
                    resultMap.put("street", towncode.substring(0, 9));
                } else {
                    log.info("地理位置编码解析失败，原因：" + jsonObject.getString("info"));
                    return null;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return resultMap;
    }

    /**
     * 根据经纬度获取省市区
     *
     * @param lng
     * @param lat
     * @return
     */
    public static String lnglatConvertcode(String lng, String lat) {
        String url = BASE_PATH + "/geocode/regeo?output=JSON&location=" + lng + "," + lat + "&key=" + MapUtils.KEY + "&radius=1000&extensions=all";
        String retJson = getHttpResponse(url);
        if (StringUtils.isNoneBlank(retJson)) {
            JSONObject jsonObject = JSONObject.parseObject(retJson);
            String status = jsonObject.get("status").toString();
            String info = jsonObject.get("info").toString();
            if (status.equals("1")) {
                JSONObject regeocode = jsonObject.getJSONObject("regeocode");
                JSONObject addressComponent = regeocode.getJSONObject("addressComponent");
                return addressComponent.getString("adcode");
            }
        }
        return "";
    }

    /**
     * 获取距离的map
     *
     * @param origin
     * @param subMapParamList
     * @return
     */
    private static Map<String, String> getDistanceBatchMap(String origin, List<SubGaoDeMapParam> subMapParamList) {
        Map<String, String> retDistanceMap = new HashMap<String, String>();
        GaoDeMapParam mapParam = new GaoDeMapParam();
        mapParam.setOps(subMapParamList);
        try {
            String jsonStr = JSON.toJSONString(mapParam);
            String postHtmlforPara = HttpKit.post(BASE_PATH + "/batch?key=" + MapUtils.KEY, jsonStr);
            if (!postHtmlforPara.startsWith("[")) {
                JSONObject pJsonObject = JSONObject.parseObject(postHtmlforPara);
                if (0 == pJsonObject.getInteger("status")) {
                    log.error(pJsonObject.getString("info"));
                    return retDistanceMap;
                }
            }

            postHtmlforPara = "{" + "result:" + postHtmlforPara + "}";
            JSONObject jsonObject = JSONObject.parseObject(postHtmlforPara);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject subjsonObject = jsonArray.getJSONObject(i);
                if (HttpServletResponse.SC_OK == subjsonObject.getInteger("status")) {
                    JSONObject bodyJsonObject = subjsonObject.getJSONObject("body");
                    JSONObject sonjsonObject = bodyJsonObject.getJSONObject("route");
                    String retOrigin = sonjsonObject.getString("origin");
                    String destination = sonjsonObject.getString("destination");
                    JSONArray pathArray = sonjsonObject.getJSONArray("paths");
                    retDistanceMap.put(retOrigin + "_" + destination, pathArray.getJSONObject(0).getString("distance"));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return retDistanceMap;
    }

    /**
     * 高德地图WebAPI : 地址转化为高德坐标 <br />
     *
     * @param address：高德地图地址
     */
    public static String coordinate(String address) {
        try {
            address = URLEncoder.encode(address, "utf-8");
            String url = BASE_PATH + "/geocode/geo?address=" + address + "&output=json&city=331102&key=" + MapUtils.KEY;
            String retJson = getHttpResponse(url);
            if (StringUtils.isNoneBlank(retJson)) {
                JSONObject jsonObject = JSONObject.parseObject(retJson);
                String coordinateString = "";
                if (jsonObject != null && !jsonObject.isEmpty() && jsonObject.containsKey("geocodes")) {
                    JSONArray pathArray = jsonObject.getJSONArray("geocodes");
                    if (pathArray != null && !pathArray.isEmpty()) {
                        coordinateString = pathArray.getJSONObject(0).getString("location");
                        coordinateString += "," + pathArray.getJSONObject(0).getString("formatted_address");
                    }
                }
                return coordinateString;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static List<Map<String, String>> listAddress(String address) {
        try {
            List<Map<String, String>> resultMap = new ArrayList<>();
            address = URLEncoder.encode(address, "utf-8");
            String url = BASE_PATH + "/place/text?keywords=" + address + "&output=json&city=331102&citylimit=true&offset=5&key=" + MapUtils.KEY;
            String retJson = getHttpResponse(url);
            if (StringUtils.isNoneBlank(retJson)) {
                JSONObject jsonObject = JSONObject.parseObject(retJson);
                String status = jsonObject.getString("status");
                if ("1".equals(status)) {
                    JSONArray pois = jsonObject.getJSONArray("pois");
                    for (int i = 0; i < pois.size(); i++) {
                        JSONObject subJsonObject = pois.getJSONObject(i);
                        Map<String, String> addMap = new HashMap<>();
                        addMap.put("name", subJsonObject.getString("name"));
                        addMap.put("address", subJsonObject.getString("address"));
                        String location = subJsonObject.getString("location");
                        String[] split = location.split(",");
                        // 高德转天地图
                        double[] latLon = GPSUtil.gcj02_To_Gps84(Double.parseDouble(split[1]), Double.parseDouble(split[0]));
                        addMap.put("lat", latLon[0] + "");
                        addMap.put("lng", latLon[1] + "");
                        resultMap.add(addMap);
                    }
                }
            }
            return resultMap;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 高德地图WebAPI : gps坐标转化为高德坐标<br />
     *
     * @param coordsys：高德地图坐标
     */
    public static String convert(String coordsys) {
        try {
            coordsys = URLEncoder.encode(coordsys, "utf-8");
            String url = BASE_PATH + "/assistant/coordinate/convert?locations=" + coordsys + "&coordsys=gps&output=json&key=" + MapUtils.KEY;
            String retJson = getHttpResponse(url);
            if (StringUtils.isNoneBlank(retJson)) {
                JSONObject jsonObject = JSONObject.parseObject(retJson);
                String coordinateString = jsonObject.getString("locations");
                return coordinateString;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getHttpResponse(String allConfigUrl) {
        BufferedReader in = null;
        StringBuffer result = null;
        try {
            // url请求中如果有中文，要在接收方用相应字符转码
            URI uri = new URI(allConfigUrl);
            URL url = uri.toURL();
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Content-type", "text/html");
            connection.setRequestProperty("Accept-Charset", "utf-8");
            connection.setRequestProperty("contentType", "utf-8");
            connection.connect();
            result = new StringBuffer();
            // 读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        // String adcode = "370784";
        // System.out.println(adcode.substring(0, 4) + "00");

		 /*String lnglat2code = positionBylnglat("113.97405,22.69368");
		System.out.println(lnglat2code);*/
        // String lnglat2code = lnglat2code("120.210402,30.300611", "city");
        // String lnglat2code = lnglat2code("120.078245,28.602481", "city");
//		String lnglat2code = lnglat2code("119.912758,28.472051", "");
		/*Map<String, String> map = lnglat2code("119.912758,28.472051");
		System.out.println(map);*/
        //
        // Map<String, String> destinationMap = new HashMap<String, String>();
        // destinationMap.put("1", "120.20523,30.25727");
        // destinationMap.put("2", "120.162887,30.263779");
        //
        // Map<String, String> distanceBatchMap = distanceBatch("120.16289,30.25727",
        // destinationMap);
        // System.out.println(distanceBatchMap);

        // 1.计算两个经纬度之间的距离
        // String origin = "104.043390" + "," + "30.641982"; // 格式:经度,纬度;注意：高德最多取小数点后六位
        // String target = "104.655347" + "," + "30.786691";
        // String distance = distance(origin, target);
        // System.out.println("原坐标:{" + origin + "}，目标坐标:{" + target +
        // "}--------->计算后距离：" + distance);
        //
        // // 2.地址转换高德坐标
//		 String address = "杭州市江干区百合路与采荷路交汇处芙蓉公园1";
//		String coordinate = coordinate(address);
//		System.out.println(coordinate);
		/*List<Map<String, String>> list = listAddress(address);
		System.out.println(list);*/

        //
        // // 3.gps坐标转化为高德坐标
        // String coordsys = "121.43687,31.18826";
        // String moordsys = convert(coordsys);
        // System.out.println("转换前的经纬度：" + coordsys + "-------->转变后的经纬度:" + moordsys);

        String lng = "120.183818";
        String lat = "30.401946";
//        System.out.println(lnglatConvertcode(lng, lat));
        System.out.println(lnglat2code(lng + "," + lat).get("detailAddress"));
    }
}
