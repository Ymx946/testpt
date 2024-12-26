package com.mz.model.base.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mz.common.annotation.FieldMeta;
import com.mz.common.model.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


/**
 * 用户表(BaseUser)实体类
 *
 * @author makejava
 * @since 2021-03-17 09:08:18
 */
@Setter
@Getter
@ToString
public class BaseUserNewVO extends BaseDTO {
    /**
     * 搜索关键字
     */
    private String findStr;
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
    @FieldMeta(name = "管理区域名称")
    private String manageAreaName;
    /**
     * 区域代码
     */
    private String areaCode;
    /**
     * 登录用户名
     */
    @FieldMeta(name = "登录用户名")
    private String loginName;
    /**
     * 手机号
     */
    @FieldMeta(name = "手机号")
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
    @FieldMeta(name = "姓名")
    private String realName;
    /**
     * 用户等级1-超级管理员 2-租户管理员 3-主体管理员 4-干部用户 5-普通用户 6-网格员
     */
    @FieldMeta(name = "用户等级", readConverterExp = "1=超级管理员,2=租户管理员,3=主体管理员,4=干部用户,5=普通用户,6=网格员")
    private Integer userLevel;
    /**
     * 用户类型(1-监管端2-农企端 3-演示用户)
     */
    @FieldMeta(name = "用户类型", readConverterExp = "1=监管端,2=农企端,3=演示用户")
    private Integer userType;
    /**
     * 可用状态(1可用2不可用)
     */
    @FieldMeta(name = "可用状态", readConverterExp = "1=可用,2=不可用")
    private Integer useState;
    /**
     * 登录状态
     */
    @FieldMeta(name = "登录状态")
    private Integer loginState;
    /**
     * 最新登录时间
     */
    @FieldMeta(name = "最新登录时间")
    private String loginTime;
    /**
     * 最新登录时间（APP）
     */
    @FieldMeta(name = "最新登录时间(APP)")
    private String appLoginTime;
    /**
     * 创建时间
     */
    private String creatTime;
    /**
     * 创建人
     */
    private String creatUser;
    /**
     * 修改时间
     */
    private String modifyTime;
    /**
     * 修改人
     */
    private String modifyUser;
    /**
     * 所属单位ID
     */
    private Long unitId;
    /**
     * 所属单位名称
     */
    private String unitName;
    /**
     * 照片
     */
    private String userPhoto;

}