package com.mz.model.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 用户表(BaseUser)实体类
 *
 * @author makejava
 * @since 2021-03-17 09:08:18
 */
@Setter
@Getter
@ToString
@JsonIgnoreProperties(value = {"rawPwd"})
public class BaseUser {
    private static final long serialVersionUID = 285554897944761788L;
    //    用户类型(1-监管端2-农企端 3-演示(免登) 4-大屏演示(免登))
    public static int USER_TYPE_SUPERVISE = 1;
    public static int USER_TYPE_ENTERPRISE = 2;
    public static int USER_TYPE_LOGIN_FREE = 3;
    public static int USER_TYPE_LOGIN_FREE_SCREEN = 4;
    //用户权限类型（用于免登录区域用户）
    public static String BUS_NODE_LOGIN_FREE = "Manage";//后台关联
    public static String BUS_NODE_LOGIN_FREE_SCREEN = "Bulletin";//演示大屏
    /**
     * 主键
     */
    private String id;
    /**
     * 所属租户
     */
    private String tenantId;
    /**
     * 管理区域
     */
    private String manageArea;
    /**
     * 管理区域名称
     */
    private String manageAreaName;
    /**
     * 区域代码
     */
    private String areaCode;
    /**
     * 登录用户名
     */
    private String loginName;
    /**
     * 手机号
     */
    private String phoneNo;
    /**
     * 密码（明文密码）
     */
    private String rawPwdMd5;
    /**
     * 密码（明文密码）
     */
    private String rawPwd;
    /**
     * 密码（密文密码）
     */
    private String password;
    /**
     * 盐值
     */
    private String pwdSalt;
    /**
     * 密码修改时间
     */
    private String pwdModifyTime;
    /**
     * 姓名
     */
    private String realName;
    /**
     * 用户等级1-超级管理员 2-租户管理员 3-主体管理员 4-干部用户 5-普通用户 6-网格员
     */
    private Integer userLevel;
    /**
     * 用户类型(1-监管端2-农企端 3-演示用户)
     */
    private Integer userType;
    /**
     * 可用状态(1可用2不可用)
     */
    private Integer useState;
    /**
     * 登录状态
     */
    private Integer loginState;
    /**
     * 最新登录时间
     */
    private String loginTime;
    /**
     * 最新登录时间（APP）
     */
    private String appLoginTime;
    /**
     * 创建时间
     */
    private String creatTime;
    /**
     * 创建人
     */
    private String creatUser;

}