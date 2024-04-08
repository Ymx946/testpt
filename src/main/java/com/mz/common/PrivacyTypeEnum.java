package com.mz.common;

import lombok.Getter;

/**
 * @description:隐私数据类型枚举
 * @author: yangh
 * @createDate: 2022/7/26
 */

@Getter
public enum PrivacyTypeEnum {

    /**
     * 自定义（此项需设置脱敏的范围）
     */
    CUSTOMER,

    /**
     * 姓名
     */
    NAME,

    /**
     * 身份证号
     */
    ID_CARD,

    /**
     * 手机号
     */
    PHONE,

    /**
     * 地址
     */
    ADDRESS,

    /**
     * 邮箱
     */
    EMAIL,

    /**
     * 银行卡
     */
    BANK_CARD,

    /**
     * 密码
     */
    PASSWORD
}
