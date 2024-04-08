package com.mz.common.datasysn.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CityDoWebUserInfo {

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
     * 账号
     */
    private String account;
    /**
     * ⽤户所属村庄编号(统计局数据-⾏政区划)
     */
    private String tenantId;
    /**
     * ⽤于所属村庄名称
     */
    private String tenantName;
    /**
     * ⻆⾊编号 1-超级管理员 2-村级管理员
     */
    private String roleId;
    /**
     * ⻆⾊名称
     */
    private String roleName;
    /**
     * ⽤于所属村庄全称
     */
    private String tenantFullName;
    /**
     * 账号创建时间
     */
    private String gmtCreate;
    /**
     * 账号更新时间
     */
    private String gmtModified;
    /**
     * 当前登录渠道 （SYSTEM-系统登录,ZZD-浙政钉登录）
     */
    private String loginChannel;
}
