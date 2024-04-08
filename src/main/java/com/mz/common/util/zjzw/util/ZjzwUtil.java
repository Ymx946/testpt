package com.mz.common.util.zjzw.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.util.StringUtil;
import com.mz.common.util.wxaes.HttpKit;
import com.mz.common.util.zjzw.Constants;
import com.mz.common.util.zjzw.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class ZjzwUtil {
    public static String zjzw_access_token = null;
    public static String zjzw_time = null;

//    public static void main(String[] args) throws IOException {
////        String token = getAccessToken(Constants.ZNM_CLIENTID, Constants.ZNM_CLIENTSECRET);
////        System.out.println(token);
//        String param = "";
//
////        Map<String, String> headMap = new HashMap<>();
////        headMap.put("Authorization", "bearer " + ZjzwUtil.getAccessToken("", "", ""));
////        String retObj = HttpUtil.post(Constants.ZNM_URL_TEST + Constants.ZNM_UPDATE_URL, JSON.toJSONString());
////        System.out.println(retObj);
//
//        String areaCode = "";
//        String address = "浙江省温州市瑞安市曹村镇许岙村";
//         // String areaCode, String address, String villageName, String colorReason, Integer colorSettingMode, Integer qrCodeColor, Integer qrSize, String profile
////        getZnmQrCodeUrl("330381128219", address, "许岙村", null, null, null, null, "dev");
//
//        String yh = "{\"callbackUrl\":\"https://dev.mzszxc.com/wlxczxH5/\",\"clientId\":\"zheliweilaixiangcunzaixian\",\"extendCodeFileUrl\":\"\",\"extendType\":3,\"resolveUrl\":\"https://dev.mzszxc.com/wlxczxH5/\",\"updateParamsDtoList\":[{\"areaNumber\":\"330381128219\",\"attributeParamDtoList\":[{\"key\":\"address\",\"keyName\":\"地址\",\"value\":\"浙江省温州市瑞安市曹村镇东岙村\"}],\"belong\":\"\",\"belongType\":0,\"extendCode\":\"330381128219\",\"extendCodeType\":4,\"imageUrl\":\"\",\"objectName\":\"东岙村\"}],\"useForCode\":\"040100\"}";
//        String yh = "{\"callbackUrl\":\"https://gt-fe.qthome.com/fe/fe-lyt-h5/dev/index.html#/lyt-app-home/app-map\",\"clientId\":\"zheliweilaixiangcunzaixian\",\"extendCodeFileUrl\":\"https://gt-fe.qthome.com/fe/fe-lyt-h5/dev/index.html#/lyt-app-home/app-map\",\"extendType\":3,\"resolveUrl\":\"https://gt-fe.qthome.com/fe/fe-lyt-h5/dev/index.html#/lyt-app-home/app-map\",\"updateParamsDtoList\":[{\"areaNumber\":\"330825109225\",\"attributeParamDtoList\":[{\"key\":\"address\",\"keyName\":\"地址\",\"value\":\"浙江省衢州市龙游县溪口镇溪口村\"}],\"belong\":\"\",\"belongType\":0,\"extendCode\":\"330825109225\",\"extendCodeType\":4,\"imageUrl\":\"\",\"objectName\":\"溪口村\"}],\"useForCode\":\"040100\"}";
//        updateZnm(yh, "prod");
////
//        getZnmCodeInfo("1077020285001640167-91", "dev");
//        getZnmUserInfo("eb2c5e5bf48b489bbaa9edd2c8d78beb", "prod");

//        String tokenUrl = "https://ibcdsg.zj.gov.cn:8443/restapi/prod/IC33000020220329000007/uc/sso/access_token";
//        String userUrl = "https://ibcdsg.zj.gov.cn:8443/restapi/prod/IC33000020220329000008/uc/sso/getUserInfo";

//        System.out.println(getTokenByTicketId("debug_tk_e4a0dc3fcc8d464ba336b9bcb1ba2072", tokenUrl));

//        System.out.println(getUserInfoByToken("debug_at_3ea0b1705e7d431690b06473205a53681", userUrl));
//    }

    private synchronized static String getAccess_token(String clientId, String clientSecret, String profile) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("clientId", clientId);
        paramMap.put("clientSecret", clientSecret);
        paramMap.put("grantType", "client_credentials");
        String baseUrl = "";
        if ("dev".equalsIgnoreCase(profile) || "test".equalsIgnoreCase(profile)) {
            baseUrl = Constants.ZNM_URL_TEST;
        } else {
            baseUrl = Constants.ZNM_URL_PROD;
        }
        String jsonObjectStr = HttpUtil.post(baseUrl + Constants.ZNM_TOKEN_URL, JSON.toJSONString(paramMap));
        String token = null;
        if (StringUtil.isEmpty(jsonObjectStr) || jsonObjectStr.equals("[]")) {
            log.info("获取token失败, token为空");
            return null;
        }
        Map<String, Object> retMap = (Map<String, Object>) JSON.parse(jsonObjectStr);
        if (retMap.containsKey("state") && HttpStatus.HTTP_OK == (int) retMap.get("state")) {
            String results = retMap.get("results") + "";
            if (StringUtils.isNoneBlank(results)) {
                Map<String, Object> subMap = (Map<String, Object>) JSON.parse(results);
                if (subMap.containsKey("accessToken")) {
                    token = subMap.get("accessToken") + "";
                }
            }
        }
        return token;
    }

    public static String getAccessToken(String clientId, String clientSecret, String profile) {
        if (StringUtils.isEmpty(clientId) || StringUtils.isEmpty(clientSecret)) {
            clientId = Constants.ZNM_CLIENTID_TEST;
            clientSecret = Constants.ZNM_CLIENTSECRET_TEST;
            if (!"dev".equalsIgnoreCase(profile) && !"test".equalsIgnoreCase(profile)) {
                clientId = Constants.ZNM_CLIENTID_PROD;
                clientSecret = Constants.ZNM_CLIENTSECRET_PROD;
            }
        }
        if (zjzw_access_token == null) {
            zjzw_access_token = getAccess_token(clientId, clientSecret, profile);
            zjzw_time = getTimeStr();
        } else {
            if (!zjzw_time.substring(0, 13).equals(getTimeStr().substring(0, 13))) { // 每小时刷新一次
                zjzw_access_token = null;
                zjzw_access_token = getAccess_token(clientId, clientSecret, profile);
                zjzw_time = getTimeStr();
            }
        }
        return zjzw_access_token;
    }

    /**
     * 申请浙江农码
     *
     * @param znmCodeApplyParamsV3Dto
     * @return
     */
    public static boolean znmApply(ZnmCodeParamsV3Dto znmCodeApplyParamsV3Dto, String profile) {
        String authorization = "bearer " + getAccessToken("", "", profile);
        String baseUrl = "";
        if ("dev".equalsIgnoreCase(profile) || "test".equalsIgnoreCase(profile)) {
            baseUrl = Constants.ZNM_URL_TEST;
        } else {
            baseUrl = Constants.ZNM_URL_PROD;
        }
        try {
            String retObj = HttpKit.sendPostHttp(baseUrl + Constants.ZNM_APPLY_URL, authorization, JSON.toJSONString(znmCodeApplyParamsV3Dto));
            log.info("======申请浙江农码：" + retObj);
            Map<String, Object> retMap = (Map<String, Object>) JSON.parse(retObj);
            if (retMap.containsKey("state")) {
                return HttpStatus.HTTP_OK == (int) retMap.get("state");
            }
        } catch (Exception e) {
            log.error("Error Message: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * 更新浙农码
     *
     * @return
     */
    public static boolean updateZnm(String jsonParam, String profile) {
        String authorization = "bearer " + getAccessToken("", "", profile);
        String baseUrl = "";
        if ("dev".equalsIgnoreCase(profile) || "test".equalsIgnoreCase(profile)) {
            baseUrl = Constants.ZNM_URL_TEST;
        } else {
            baseUrl = Constants.ZNM_URL_PROD;
        }
        try {
            String retObj = HttpKit.sendPostHttp(baseUrl + Constants.ZNM_UPDATE_URL, authorization, jsonParam);
            log.info("======更新浙江农码：" + retObj);
            Map<String, Object> retMap = (Map<String, Object>) JSON.parse(retObj);
            if (retMap.containsKey("state")) {
                return HttpStatus.HTTP_OK == (int) retMap.get("state");
            }
        } catch (Exception e) {
            log.error("Error Message: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * 更新浙农码
     *
     * @return
     */
    public static boolean updateZnm(ZnmCodeParamsV3Dto znmCodeApplyParamsV3Dto, String profile) {
        String authorization = "bearer " + getAccessToken("", "", profile);
        String baseUrl = "";
        if ("dev".equalsIgnoreCase(profile) || "test".equalsIgnoreCase(profile)) {
            baseUrl = Constants.ZNM_URL_TEST;
        } else {
            baseUrl = Constants.ZNM_URL_PROD;
        }
        try {
            String retObj = HttpKit.sendPostHttp(baseUrl + Constants.ZNM_UPDATE_URL, authorization, JSON.toJSONString(znmCodeApplyParamsV3Dto));
            log.info("======更新浙江农码：" + retObj);
            Map<String, Object> retMap = (Map<String, Object>) JSON.parse(retObj);
            if (retMap.containsKey("state")) {
                return HttpStatus.HTTP_OK == (int) retMap.get("state");
            }
        } catch (Exception e) {
            log.error("Error Message: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * @param areaCode         - 唯一性标识 eg: areaCode
     * @param address          - 地址
     * @param villageName      - 村名称
     * @param colorReason      - 示码颜色原因
     * @param colorSettingMode - 颜色设置方式1-二维码颜色设置
     * @param qrCodeColor      - 二维码预警赋色 0-绿色 1-红色 3-黄色
     * @param qrSize           - 二维码尺寸
     * @param profile          - 开发、测试、正式环境
     * @return
     */
    public static ByteArrayInputStream getZnmQrCodeUrl(String areaCode, String address, String villageName, String colorReason, Integer colorSettingMode, Integer qrCodeColor, Integer qrSize, String profile) throws IOException {
        log.info("======浙农码before========");
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(colorReason)) {
            colorReason = "获取【" + villageName + "[" + areaCode + "]】浙农码";
        }
        map.put("colorReason", colorReason);
        map.put("colorSettingMode", Optional.ofNullable(colorSettingMode).orElse(1));
        map.put("extendCode", areaCode);
        map.put("qrCodeColor", Optional.ofNullable(qrCodeColor).orElse(0));
        map.put("qrSize", Optional.ofNullable(qrSize).orElse(200));

        try {
            String authorization = "bearer " + ZjzwUtil.getAccessToken("", "", profile);
            String baseUrl = "";
            if ("dev".equalsIgnoreCase(profile) || "test".equalsIgnoreCase(profile)) {
                baseUrl = Constants.ZNM_URL_TEST;
            } else {
                baseUrl = Constants.ZNM_URL_PROD;
            }
            String retObj = HttpKit.sendPostHttp(baseUrl + Constants.ZNM_QRCODELIST_URL, authorization, JSON.toJSONString(new HashMap<String, List<Map<String, Object>>>() {{
                put("list", Collections.singletonList(map));
            }}));
            log.info("======获取浙农码返回信息：" + retObj);
            Map<String, Object> retMap = (Map<String, Object>) JSON.parse(retObj);

            if (retMap.containsKey("state")) {
                if (HttpStatus.HTTP_OK != (int) retMap.get("state")) {
                    if ("扩展码不存在".equals(retMap.get("msg"))) { // 生成浙农码
                        ZnmCodeParamsV3Dto znmCodeParamsV3Dto = new ZnmCodeParamsV3Dto();
                        String callbackUrl = "", resolveUrl = "";
                        if ("prod".equalsIgnoreCase(profile)) {
                            callbackUrl = Constants.RESOLVE_URL_PROD;
                            resolveUrl = Constants.RESOLVE_URL_PROD;
                        } else if ("agri".equalsIgnoreCase(profile)) {
                            callbackUrl = Constants.RESOLVE_URL_AGRI;
                            resolveUrl = Constants.RESOLVE_URL_AGRI;
                        } else {
                            callbackUrl = Constants.RESOLVE_URL_TEST;
                            resolveUrl = Constants.RESOLVE_URL_TEST;
                        }
                        znmCodeParamsV3Dto.setCallbackUrl(callbackUrl);
                        String clientId = "";
                        if ("dev".equalsIgnoreCase(profile) || "test".equalsIgnoreCase(profile)) {
                            clientId = Constants.ZNM_CLIENTID_TEST;
                        } else {
                            clientId = Constants.ZNM_CLIENTID_PROD;
                        }
                        znmCodeParamsV3Dto.setClientId(clientId);
                        znmCodeParamsV3Dto.setExtendType(3);
                        znmCodeParamsV3Dto.setResolveUrl(resolveUrl);

                        List<UploadParamsDto> uploadParamsDtoList = new ArrayList<>();
                        UploadParamsDto uploadParamsDto = new UploadParamsDto();
                        uploadParamsDto.setAreaNumber(areaCode);

                        AttributeParamDto attributeParamDto = new AttributeParamDto();
                        List<AttributeParamDto> attributeParamDtoList = new ArrayList<>();
                        attributeParamDto.setKey("address");
                        attributeParamDto.setKeyName("地址");
                        attributeParamDto.setValue(address);
                        attributeParamDtoList.add(attributeParamDto);
                        uploadParamsDto.setAttributeParamDtoList(attributeParamDtoList);

                        uploadParamsDto.setBelongType(0);
                        uploadParamsDto.setExtendCode(areaCode);
                        uploadParamsDto.setExtendCodeType(Constants.ZNM_EXTENDCODE_TYPE);
                        uploadParamsDto.setObjectName(villageName);
                        uploadParamsDtoList.add(uploadParamsDto);
                        znmCodeParamsV3Dto.setUploadParamsDtoList(uploadParamsDtoList);
                        znmCodeParamsV3Dto.setUseForCode(Constants.ZNM_USEFOR_CODE);

                        if (znmApply(znmCodeParamsV3Dto, profile)) {
                            return getZnmQrCodeUrl(areaCode, address, villageName, colorReason, colorSettingMode, qrCodeColor, qrSize, profile);
                        } else {
                            log.warn("申请【" + villageName + "[" + areaCode + "]】浙农码失败，请联系管理员");
                            return null;
                        }
                    }
                } else {
                    String results = retMap.get("results") + "";
                    if (StringUtils.isNoneBlank(results)) {
                        List<ZnmQrCode> znmQrCodeList = JSON.parseArray(results, ZnmQrCode.class);
                        if (CollectionUtil.isNotEmpty(znmQrCodeList)) {
                            ByteArrayInputStream byteArrayInputStream = null;
                            try {
                                byteArrayInputStream = new ByteArrayInputStream(Base64.decodeBase64(znmQrCodeList.stream().findFirst().get().getQrCodeBase64().getBytes()));
                            } catch (Exception e) {
                                log.error("转换浙农码图片为字节流失败: " + e.getMessage());
                            } finally {
                                byteArrayInputStream.close();
                            }
                            return byteArrayInputStream;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error Message: " + e.getMessage());
        }
        return null;
    }

    /**
     * 获取被扫对象信息
     *
     * @return
     */
    public static Map<String, Object> getZnmCodeInfo(String znmUid, String profile) {
        String baseUrl = "";
        if ("dev".equalsIgnoreCase(profile) || "test".equalsIgnoreCase(profile)) {
            baseUrl = Constants.ZNM_URL_TEST;
        } else {
            baseUrl = Constants.ZNM_URL_PROD;
        }
        String url = baseUrl + Constants.ZNM_CODE_INFO_URL.replace("ZNMUID", znmUid);
        try {
            String retObj = HttpUtil.createGet(url).addHeaders(new HashMap<String, String>() {{
                put("authorization", "bearer " + ZjzwUtil.getAccessToken("", "", profile));
            }}).execute().body();
            log.info("======获取被扫对象信息：" + retObj);
            Map<String, Object> retMap = (Map<String, Object>) JSON.parse(retObj);
            if (retMap.containsKey("state") && HttpStatus.HTTP_OK == (int) retMap.get("state")) {
                return retMap;
            }
        } catch (Exception e) {
            log.error("Error Message: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 获取扫码人信息
     *
     * @return
     */
    public static Map<String, Object> getZnmUserInfo(String znmToken, String profile) {
        String baseUrl = "";
        if ("dev".equalsIgnoreCase(profile) || "test".equalsIgnoreCase(profile)) {
            baseUrl = Constants.ZNM_URL_TEST;
        } else {
            baseUrl = Constants.ZNM_URL_PROD;
        }
        String url = baseUrl + Constants.ZNM_USER_INFO_URL.replace("AUTHORIZATION", ZjzwUtil.getAccessToken("", "", profile)).replace("ZNMTOKEN", znmToken);
        try {
            String retObj = HttpUtil.createGet(url).addHeaders(new HashMap<String, String>() {{
                put("authorization", "bearer " + ZjzwUtil.getAccessToken("", "", profile));
            }}).execute().body();
            log.info("======获取扫码人信息：" + retObj);
            Map<String, Object> retMap = (Map<String, Object>) JSON.parse(retObj);
            if (retMap.containsKey("state") && HttpStatus.HTTP_OK == (int) retMap.get("state")) {
                String results = retMap.get("results") + "";
                if (StringUtils.isNoneBlank(results)) {
                    Map<String, Object> subMap = (Map<String, Object>) JSON.parse(results);
                    if (subMap != null && !subMap.isEmpty()) {
                        Map<String, Object> infoMap = new HashMap<>();
                        if (subMap.containsKey("legalPersonInfo")) {
                            String legalPersonInfoStr = subMap.get("legalPersonInfo") + "";
                            if (StringUtils.isNotEmpty(legalPersonInfoStr)) {
                                infoMap.put("legalPersonInfo", JSON.parseObject(legalPersonInfoStr, LegalPersonInfo.class));
                            }
                        }
                        if (subMap.containsKey("personalInfo")) {
                            String personalInfoStr = subMap.get("personalInfo") + "";
                            if (StringUtils.isNotEmpty(personalInfoStr)) {
                                infoMap.put("personalInfo", JSON.parseObject(personalInfoStr, PersonalInfo.class));
                            }
                        }
                        return infoMap;
                    }
                }
                return null;
            }
        } catch (Exception e) {
            log.error("Error Message: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    public synchronized static String getTokenByTicketId(String ticketId, String url) {
        // 1. 通过ticketId 换取 accessToken
        try {
            String retObj = HttpKit.sendSSLRequest(url, JSON.toJSONString(new HashMap<String, String>() {{
                put("appId", Constants.APP_ID);
                put("ticketId", ticketId);
            }}), getHeadMap(url), "POST");

            if (StringUtils.isNoneBlank(retObj)) {
                return retObj;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error Message: " + e.getMessage());
        }
        return null;
    }

    public synchronized static String getUserInfoByToken(String accessToken, String userUrl) {
        // 1. 通过ticketId 换取 accessToken
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("token", accessToken);
        try {
            String retObj = HttpKit.sendSSLRequest(userUrl, JSON.toJSONString(paramMap), getHeadMap(userUrl), "POST");
            if (StringUtils.isNoneBlank(retObj)) {
                Map<String, Object> objMap = (Map<String, Object>) JSON.parse(retObj);
                if (objMap.containsKey("success") && Boolean.TRUE.equals(objMap.get("success"))) {
                    Map<String, Object> subMap = (Map<String, Object>) JSON.parse(objMap.get("data") + "");
                    if (subMap.containsKey("personInfo")) {
                        return subMap.get("personInfo") + "";
                    }
                } else {
                    log.info("获取浙里办微信用户信息失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error Message: " + e.getMessage());
        }
        return "";
    }

    private synchronized static Map<String, String> getHeadMap(String url) {
        IrsSignRes res = IrsUtils.sign(url, "POST");
        Map<String, String> headMap = new HashMap<>();
        headMap.put(Constants.X_BG_HMAC_ACCESS_KEY, res.getAccessKey());
        headMap.put(Constants.X_BG_HMAC_ALGORITHM, res.getAlgorithm());
        headMap.put(Constants.X_BG_HMAC_SIGNATURE, res.getSignature());
        headMap.put(Constants.X_BG_DATE_TIME, res.getDateTime());
        return headMap;
    }

    // 获取当前系统时间 用来判断access_token是否过期
    public synchronized static String getTimeStr() {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(dt);
    }
}
