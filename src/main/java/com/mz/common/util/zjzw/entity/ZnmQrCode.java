package com.mz.common.util.zjzw.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class ZnmQrCode implements Serializable {
    /**
     * 唯一性标识
     */
    private String extendCode;
    /**
     * 浙农码
     */
    private String znmCode;
    /**
     * 浙农码base64
     */
    private String qrCodeBase64;
}
