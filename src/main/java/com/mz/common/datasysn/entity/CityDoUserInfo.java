package com.mz.common.datasysn.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CityDoUserInfo {

    /**
     * 系统⽤户编号
     */
    private Long userId;
    /**
     * 三⽅⽤户编号 openId
     */
    private String openId;
    /**
     * 姓名
     */
    private String userName;
    /**
     * ⼿机号
     */
    private String userPhone;
    /**
     * 性别 男，⼥
     */
    private String gender;
    /**
     * 身份证
     */
    private String idCard;
    /**
     * ⽤户类型 VILLAGER-村⺠、TOURIST-游客
     */
    private String userType;
    /**
     * ⽤户类型说明
     */
    private String userTypeName;
    /**
     * ⽤户所属村庄编号(统计局数据-⾏政区划),注意：该绑定关系⽤户可以⾃⾏变
     * 更
     */
    private String tenantId;
    /**
     * ⽤于所属村庄名称
     */
    private String tenantName;
    /**
     * ⽤于所属村庄全称
     */
    private String tenantFullName;
    /**
     * 创建时间
     */
    private String gmtCreate;
    /**
     * 更新时间
     */
    private String gmtModified;
    /**
     * 登录渠道（SYSTEM-系统登录，ZLB-浙⾥办，ZZD-浙政钉）
     */
    private String loginChannel;
}
