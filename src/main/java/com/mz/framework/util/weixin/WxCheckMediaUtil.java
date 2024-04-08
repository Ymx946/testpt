package com.mz.framework.util.weixin;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

@Slf4j
public class WxCheckMediaUtil {
    /**
     * 消息推送配置 - Token(令牌) 必须为英文或数字，长度为3-32字符
     */
    public static final String TOKEN = "70ad729b7ef909e4bfc37e38c53141ec";
    /**
     * EncodingAESKey 消息加密密钥由43位字符组成，字符范围为A-Z,a-z,0-9
     */
    public static final String ENCODINGAESKEY = "kRbGxRUBkRdp9KLwKNYuOH9ts6cP4wBllVr7afEwcNH";

    /**
     * 微信图片检测接口
     */
    public static final String MEDIA_CHECK = "https://api.weixin.qq.com/wxa/media_check_async?access_token=";

    /**
     * 文件
     */
    public static final String[] FILE_EXT = {"mp3", "aac", "ac3", "wma", "flac", "vorbis", "opus", "wav"};
    /**
     * 图片
     */
    public static final String[] IMG_EXT = {"jpg", "jepg", "png", "bmp", "gif"};

    /**
     * 本地存放检测结果
     */
    public static ConcurrentHashMap<String, String> resultMap = new ConcurrentHashMap<>();

    /**
     * 校验图片/音频
     *
     * @param media_url
     * @param media_type
     * @param openid
     * @param scene
     * @param token
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static WxComonCheckResult checkImg(String media_url, Integer media_type, String openid, Integer scene, String token) throws ExecutionException, InterruptedException {
        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask<WxComonCheckResult> imgResult;
        if (StringUtils.isNotEmpty(media_url)) {
            CheckMediaTask contentTask = new CheckMediaTask(MEDIA_CHECK + token, media_url, media_type, openid, scene);
            imgResult = pool.submit(contentTask);
            WxComonCheckResult wxComonCheckResult = imgResult.get();
            log.info("图片检测结果===" + JSON.toJSONString(wxComonCheckResult));
            return wxComonCheckResult;
        }
        return null;
    }

}
