package com.mz.common.util.verifyCode;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class VerifyCodeResp implements Serializable {
    /**
     * header头参数：Captcha-Key
     */
    private String captchaKey;

    /**
     * 验证码图片
     */
    private String captchaImg;
}
