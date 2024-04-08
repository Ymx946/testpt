package com.mz.common.util.zjzw.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class QrCodeByExtendCode implements Serializable {

    /**
     * 字段英文名称 如: address
     */
    private String address;
    /**
     * 行政区划
     */
    private String areaNumber;
    /**
     * 示码颜色原因
     */
    private String colorReason;
    /**
     * 颜色设置方式1-二维码颜色设置
     */
    private Integer colorSettingMode;
    /**
     * 扩展码,对接应用中的唯一属性id 的统称,例如,农户的身份证号,企业的统一社会信用代码,农产品编号,地块编号
     */
    private String extendCode;
    /**
     * ip
     */
    private String ip;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 二维码预警赋色 0-绿色 1-红色 3-黄色
     */
    private String qrCodeColor;
    /**
     * 二维码尺寸
     */
    private String qrSize;
    /**
     * 系统类型
     */
    private String systemType;
    /**
     * 系统版本
     */
    private String systemVersion;

}
