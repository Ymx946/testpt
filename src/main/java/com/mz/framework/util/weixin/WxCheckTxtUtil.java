package com.mz.framework.util.weixin;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

@Slf4j
public class WxCheckTxtUtil {
    /**
     * 微信文字检测接口
     */
    public static final String TXT_CHECK = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token=";

    /**
     * 校验文字
     *
     * @param openid
     * @param scene
     * @param content
     * @param token
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static WxTxtCheckResult checkTxt(String openid, Integer scene, String content, String token) throws ExecutionException, InterruptedException {
        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask<WxTxtCheckResult> txtResult;
        if (StringUtils.isNotEmpty(content)) {
            CheckTxtTask contentTask = new CheckTxtTask(TXT_CHECK + token, content, openid, scene);
            txtResult = pool.submit(contentTask);
            WxTxtCheckResult wxTxtCheckResult = txtResult.get();
            log.info("文字检测结果===" + JSON.toJSONString(wxTxtCheckResult));
            return wxTxtCheckResult;
        }
        return null;
    }
}
