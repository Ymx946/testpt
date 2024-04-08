package com.mz.common.datasysn.util;


import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.mz.common.datasysn.Constants;
import com.mz.common.util.PasswordUtil;
import com.mz.common.util.wxaes.HttpKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class OpenDataUtil {

    public static void main(String[] args) throws IOException {
//        log.info(OpenDataUtil.getOrgStructure("3302051103", "prod"));
//        log.info(OpenDataUtil.getOrgStructure("3311070909"));

////        String filepath = "C:\\Users\\yangh\\Desktop\\area.txt";
//        String filepath = "C:\\Users\\yangh\\Desktop\\area111.txt";
//        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filepath)), StandardCharsets.UTF_8)); // read encoding
//        String str = null;
//        while ((str = br.readLine()) != null) {
////            str = br.readLine();
//            String[] split = str.split("\\s+");
////            log.info(split[0] + "@" + split[1]);
////            if (split[1].indexOf("镇") > -1) {
////                log.info("put("split[0] + "\t" + str.split("镇")[1]);
////            } else if (split[1].indexOf("乡") > -1) {
////                log.info(split[0] + "\t" + str.split("乡")[1]);
////            } else if (split[1].indexOf("街道") > -1) {
////                log.info(split[0] + "\t" + str.split("街道")[1]);
////            }
//        }
//        br.close();

//        String str = FileUtil.readUtf8String(filepath);
//        log.info(str);
//        String[] split = str.split("\\s+");
//        if(split[1].indexOf("镇") > -1){
//            log.info(split[0] + "\t" + str.split("镇")[1]);
//        }else if(split[1].indexOf("乡") > -1) {
//            log.info(str.split("乡")[1]);
//        }
//        log.info(getGridMemberData("330521115220"));
    }

    public static String getOrgStructure(String orgId, String profile) {
        String baseUrl = "";
        if ("agri".equalsIgnoreCase(profile)) {
            baseUrl = Constants.DOMAIN_AGRI_URL;
        } else {
            baseUrl = Constants.DOMAIN_PROD_URL;
        }
        String url = baseUrl + Constants.PARTY_ORGANIZATIONAL_STRUCTURE_URL;
        try {
            String retObj = HttpKit.sendRequest(url, JSON.toJSONString(new HashMap<String, String>() {{
                put("area", orgId);
            }}), getHeaderMap(), "GET");
            return retObj;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static String getGridMemberData(String areaCode) {
        String url = com.mz.common.util.zjzw.Constants.YD_BASE_URL + com.mz.common.util.zjzw.Constants.AGRI_YD_WGY_USER;
        try {
            String retObj = HttpKit.post(url, JSON.toJSONString(new HashMap<String, String>() {{
                put("dataId", areaCode);
            }}));
            return retObj;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取token
     *
     * @return
     */
    public static String getToken(String profile) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userName", Constants.USERNAME);
        paramMap.put("userPassword", Constants.USERPASSWORD);
        String baseUrl = "";
        if ("dev".equalsIgnoreCase(profile) || "test".equalsIgnoreCase(profile)) {
            baseUrl = Constants.DOMAIN_DEV_URL;
        } else if ("prod".equalsIgnoreCase(profile)) {
            baseUrl = Constants.DOMAIN_DEV_URL;
        } else {
            baseUrl = Constants.DOMAIN_AGRI_URL;
        }
        String token = HttpUtil.post(baseUrl + Constants.TOKEN_URL, JSON.toJSONString(paramMap));
        Map<String, Object> retMap = (Map<String, Object>) JSON.parse(token);
        if (retMap.containsKey("success") && Boolean.TRUE.equals(retMap.get("success"))) {
            String data = retMap.get("data") + "";
            if (StringUtils.isNoneBlank(data)) {
                Map<String, Object> subMap = (Map<String, Object>) JSON.parse(data);
                if (subMap.containsKey("token")) {
                    return subMap.get("token") + "";
                }
            }
        }
        return token;
    }

    public static Map<String, String> getHeaderMap(String XCaDeptID, String profile) {
        return new HashMap<String, String>() {{
            put("X-CA-DEPT-ID", XCaDeptID);
            put("token", getToken(profile));
        }};
    }

    /**
     * sign=  MD5(appKey =%s&appSecret =%s&timestamp =%s).toLowerCase()
     *
     * @param orgId
     * @return
     */
    public static Map<String, String> getHeaderMap(String orgId) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String sign = PasswordUtil.md5("appKey=" + Constants.APPKEY + "&appSecret=" + Constants.APPSECRET + "&timestamp=" + timestamp).toLowerCase();
        return new LinkedHashMap<String, String>() {{
            put("orgId", orgId);
            put("appKey", Constants.APPKEY);
            put("sign", sign);
            put("timestamp", timestamp);
        }};
    }

    public static Map<String, String> getHeaderMap() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String sign = PasswordUtil.md5("appKey=" + Constants.APPKEYNEW + "&appSecret=" + Constants.APPSECRETNEW + "&timestamp=" + timestamp).toLowerCase();
        return new LinkedHashMap<String, String>() {{
            put("appKey", Constants.APPKEYNEW);
            put("timestamp", timestamp);
            put("sign", sign);
        }};
    }
}