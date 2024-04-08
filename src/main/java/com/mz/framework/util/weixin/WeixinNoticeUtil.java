package com.mz.framework.util.weixin;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mz.framework.util.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 微信服务号、小程序模板消息工具类
 */
@Slf4j
@Component
public class WeixinNoticeUtil {
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 公众号通知
     */
    public void sendMsg(String wxOpenId, String appId, String appIdWx, String appSecretWx, String templateId, String pagePath, JSONObject data) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("touser", wxOpenId);
            jsonObject.put("template_id", templateId);
            jsonObject.put("url", null);
            jsonObject.put("data", data);

            JSONObject miniprogram = new JSONObject();
            miniprogram.put("appid", appId);
            miniprogram.put("pagepath", pagePath);
            jsonObject.put("miniprogram", miniprogram);

            String accessToken = redisUtil.getAccessToken(appIdWx, appSecretWx);
            String postUrl = Constants.TEMPLATE_MSG_WX.replace("AccessToken", accessToken);
            JSONObject result = JSON.parseObject(HttpUtil.post(postUrl, jsonObject.toJSONString()));
            int errcode = result.getIntValue("errcode");
            if (errcode == 0) {
                log.info("微信公众号通知成功===userOpenId===" + wxOpenId);
            } else {
                log.error("微信公众号通知失败===result===" + result);
                String errmsg = result.getString("errmsg");
                if (errmsg.contains("access_token")) {
                    accessToken = redisUtil.getAccessToken(appIdWx, appSecretWx);
                    postUrl = Constants.TEMPLATE_MSG_WX.replace("AccessToken", accessToken);
                    result = JSON.parseObject(HttpUtil.post(postUrl, jsonObject.toJSONString()));
                    errcode = result.getIntValue("errcode");
                    if (errcode == 0) {
                        log.info("微信公众号二次通知成功===userOpenId===" + wxOpenId);
                    } else {
                        log.error("微信公众号二次通知失败===result===" + result);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 小程序通知
     */
    public void sendAppletsMsg(String appletOpenId, String appId, String appSecret, String auditTemplateId, String pagepath, JSONObject data) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("touser", appletOpenId);
            jsonObject.put("template_id", auditTemplateId);
            jsonObject.put("url", null);

            jsonObject.put("data", data);
            jsonObject.put("page", pagepath);
            // 跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
            jsonObject.put("miniprogram_state", "formal");

            String accessToken = redisUtil.getAccessToken(appId, appSecret);
            String postUrl = Constants.TEMPLATE_MSG_APPLET.replace("AccessToken", accessToken);
            JSONObject result = JSON.parseObject(HttpUtil.post(postUrl, jsonObject.toJSONString()));
            int errcode = result.getIntValue("errcode");
            if (errcode == 0) {
                log.info("微信小程序通知成功===userOpenId===" + appletOpenId + "===" + data);
            } else {
                log.error("微信小程序通知失败===result===" + result);
                String errmsg = result.getString("errmsg");
                if (errmsg.contains("access_token")) {
                    accessToken = redisUtil.getAccessToken(appId, appSecret);
                    postUrl = Constants.TEMPLATE_MSG_APPLET.replace("AccessToken", accessToken);
                    result = JSON.parseObject(HttpUtil.post(postUrl, jsonObject.toJSONString()));
                    errcode = result.getIntValue("errcode");
                    if (errcode == 0) {
                        log.info("微信小程序二次通知成功===userOpenId===" + appletOpenId);
                    } else {
                        log.error("微信小程序二次通知失败===result===" + result);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
