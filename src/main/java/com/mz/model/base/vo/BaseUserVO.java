package com.mz.model.base.vo;

import com.mz.common.model.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BaseUserVO extends BaseDTO {
    /**
     * 主体类型1政府机关 2-村主体  3企业  4个体工商户 5自然人6交易市场7场所(在线商城)8文明实践站9志愿者队伍10联村党委
     */
    private Integer mainBodyType;

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
     * 所属租户
     */
    private String tenantName;
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
     * 姓名
     */
    private String realName;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 职务名称
     */
    private String dutyName;
    /**
     * 用户等级 1-超级管理员 2-租户管理员 3-主体管理员 4-干部用户 5-普通用户 6-网格员
     */
    private Integer userLevel;
    /**
     * 用户类型(1-监管端2-农企端)
     */
    private Integer userType;
    /**
     * 可用状态(1可用 2不可用 3锁定)
     */
    private Integer useState;
    /**
     * 是否在线，Y-在线 N-离线
     */
    private String isOnlne = "N";
    /**
     * 最新登录时间
     */
    private String loginTime;
    /**
     * 创建时间
     */
    private String creatTime;
    /**
     * 创建人
     */
    private String creatUser;
    /**
     * 所属主体名称（多个）
     */
    private String mainBodyNameS;
}
