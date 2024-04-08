package com.mz.framework.util.weixin;

import com.alibaba.fastjson.JSON;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

public class CheckMediaTask extends RecursiveTask<WxComonCheckResult> {

    private String url;

    /**
     * 要检测的图片或音频的url，支持图片格式包括 jpg , jepg, png, bmp, gif（取首帧），
     * 支持的音频格式包括mp3, aac, ac3, wma, flac, vorbis, opus, wav
     */
    private String media_url;
    /**
     * 1:音频;2:图片
     */
    private Integer media_type;

    /**
     * 用户的openid（用户需在近两小时访问过小程序）
     */
    private String openid;
    /**
     * 场景枚举值（1 资料；2 评论；3 论坛；4 社交日志）
     */
    private Integer scene;

    /**
     * 需检测的文本内容，文本字数的上限为2500字，需使用UTF-8编码
     */
    private String content;

    public CheckMediaTask() {
    }


    public CheckMediaTask(String url, String media_url, Integer media_type, String openid, Integer scene) {
        this.url = url;
        this.media_url = media_url;
        this.media_type = media_type;
        this.openid = openid;
        this.scene = scene;
    }

    @Override
    protected WxComonCheckResult compute() {
        WxComonCheckResult result = new WxComonCheckResult();
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("version", 2);
            param.put("openid", openid);
            param.put("scene", scene);
            param.put("media_url", media_url);
            param.put("media_type", media_type);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(param, headers);

            RestTemplate rest = new RestTemplate();
            ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class, new Object[0]);
            String json = new String(entity.getBody(), "utf-8");
            result = JSON.parseObject(json, WxComonCheckResult.class);
        } catch (Exception e) {
            result.setErrcode("500");
            result.setErrmsg("system错误");
        }
        return result;
    }
}
