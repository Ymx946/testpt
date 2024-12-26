package com.mz.controller.base;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.util.ResponseCode;
import com.mz.common.util.Result;
import com.mz.common.util.UUIDGenerator;
import com.mz.common.util.verifyCode.VerifyCodeResp;
import com.mz.common.util.verifyCode.VerifyCodeUtil;
import com.mz.framework.util.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping(value = {"server/verifyCode", "verifyCode"})
public class VerifyCodeController {

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/verifyCodeImage")
    public Result verifyCodeImage(HttpServletRequest request, HttpServletResponse response) {
        // String captchaKey = VerifyCodeUtil.getSessionId(request, response);
        String captchaKey = UUIDGenerator.generate16();
        // 生成随机字串
        String captchaCode = VerifyCodeUtil.generateVerifyCode(4).toUpperCase();
        // 图片验证码缓存1分钟
        redisUtil.setEx(ConstantsCacheUtil.CACHE_KEY_OF_VERIFY_CODE + ":" + captchaKey, captchaCode, ConstantsCacheUtil.CACHE_IMG_VERIFY_CODE_MINUTES, TimeUnit.MINUTES);

        String base64String = "";
        try {
            // 返回 base64
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            // 生成图片
            VerifyCodeUtil.outputImage(ConstantsCacheUtil.VERIFY_CODE_WIDTH, ConstantsCacheUtil.VERIFY_CODE_HEIGHT, bos, captchaCode);
            byte[] bytes = bos.toByteArray();
            Base64.Encoder encoder = Base64.getEncoder();
            base64String = "data:image/png;base64," + encoder.encodeToString(bytes);
        } catch (Exception e) {
            log.error("生成验证码异常", e);
        }
        VerifyCodeResp verifyCodeResp = new VerifyCodeResp();
        verifyCodeResp.setCaptchaKey(captchaKey);
        verifyCodeResp.setCaptchaImg(base64String);
        return Result.success(verifyCodeResp);
    }

    /**
     * 方法一 ShearCaptcha
     * 图片格式
     * session存储
     * 接口需添加白名单放行
     *
     * @param request HttpServletRequest
     */
    @GetMapping("/captchaCodeImage")
    public Result captchaCodeImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String captchaKey = UUIDGenerator.generate16();
        //定义图形验证码的长、宽、验证码字符数、干扰线宽度
        ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(ConstantsCacheUtil.VERIFY_CODE_WIDTH, ConstantsCacheUtil.VERIFY_CODE_HEIGHT, 4, 4);
        String captchaCode = shearCaptcha.getCode().toUpperCase();
        // 图片验证码缓存1分钟
        redisUtil.setEx(ConstantsCacheUtil.CACHE_KEY_OF_VERIFY_CODE + ":" + captchaKey, captchaCode, ConstantsCacheUtil.CACHE_IMG_VERIFY_CODE_MINUTES, TimeUnit.MINUTES);
        String base64String = "";

        try {
            base64String = "data:image/png;base64," + shearCaptcha.getImageBase64();
        } catch (Exception e) {
            log.error("生成验证码异常", e);
        }
        VerifyCodeResp verifyCodeResp = new VerifyCodeResp();
        verifyCodeResp.setCaptchaKey(captchaKey);
        verifyCodeResp.setCaptchaImg(base64String);
        return Result.success(verifyCodeResp);
    }


    @PostMapping("/checkVerifyCode")
    public Result checkVerifyCode(String captchaKey, String captchaCode) {
        if (StringUtils.isEmpty(captchaKey)) {
            return Result.failed("验证码key不能为空");
        }

        if (StringUtils.isEmpty(captchaCode)) {
            return Result.failed("验证码不能为空");
        }

        String verifyCodeKey = ConstantsCacheUtil.CACHE_KEY_OF_VERIFY_CODE + ":" + captchaKey;
        if (!redisUtil.hasKey(verifyCodeKey)) {
            return new Result(ResponseCode.ERROR_VERIFY_CODE.getCode(), "验证码已过期，请刷新后重试");
        }

        String verifyCodeCache = redisUtil.get(verifyCodeKey);
        if (!verifyCodeCache.equalsIgnoreCase(captchaCode)) {
            return new Result(ResponseCode.ERROR_CAPTCHA_CODE.getCode(), "验证码不正确");
        }
        redisUtil.delete(verifyCodeKey);

        Map<String, String> retMap = new HashMap<>();
        retMap.put(verifyCodeKey, verifyCodeCache);
        return Result.success(retMap);
    }
}