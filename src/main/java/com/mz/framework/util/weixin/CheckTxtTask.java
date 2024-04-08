package com.mz.framework.util.weixin;

import com.alibaba.fastjson.JSON;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

public class CheckTxtTask extends RecursiveTask<WxTxtCheckResult> {

    private String url;

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
    /**
     * 用户昵称，需使用UTF-8编码
     */
    private String nickname;
    /**
     * 文本标题，需使用UTF-8编码
     */
    private String title;
    /**
     * 个性签名，该参数仅在资料类场景有效(scene=1)，需使用UTF-8编码
     */
    private String signature;

    public CheckTxtTask() {
    }

    public CheckTxtTask(String url, String content, String openid, Integer scene) {
        this.url = url;
        this.content = content;
        this.openid = openid;
        this.scene = scene;
    }

    @Override
    protected WxTxtCheckResult compute() {
        WxTxtCheckResult result = new WxTxtCheckResult();
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("version", 2);
            param.put("openid", openid);
            param.put("scene", scene);
            param.put("content", content);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(param, headers);

            RestTemplate rest = new RestTemplate();
            ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class, new Object[0]);
            String json = new String(entity.getBody(), "utf-8");
            result = JSON.parseObject(json, WxTxtCheckResult.class);
        } catch (Exception e) {
            result.setErrcode("500");
            result.setErrmsg("system错误");
        }
        return result;
    }
}
