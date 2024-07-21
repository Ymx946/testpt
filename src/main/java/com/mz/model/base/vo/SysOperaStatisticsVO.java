package com.mz.model.base.vo;

import com.mz.common.model.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 操作统计表(SysOperaStatistics)实体类
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class SysOperaStatisticsVO extends BaseDTO {
    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序： 升序 asc， 降序 desc
     */
    private String orderBy;

    /**
     * 开始日期
     */
    private String startDate;
    /**
     * 结束日期
     */
    private String endDate;
    /**
     * 登录id
     */
    private String loginID;

    /**
     * 主键
     */
    private Long id;
    /**
     * 所属租户
     */
    private Long tenantId;
    /**
     * 区域代码
     */
    private String areaCode;
    /**
     * 区域名称
     */
    private String areaName;
    /**
     * 主体ID
     */
    private Long mainBodyId;
    /**
     * 主体名称
     */
    private String mainBodyName;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 账号
     */
    private String loginName;
    /**
     * 姓名
     */
    private String realName;
    /**
     * 最后登录IP
     */
    private String ip;
    /**
     * 最后登录操作地址
     */
    private String ipAddress;
    /**
     * 登录次数
     */
    private Integer loginNum;
    /**
     * 业务操作次数
     */
    private Integer busOperaNum;
    /**
     * 最后登录设备
     */
    private String operaEqpm;
    /**
     * 操作时间
     */
    private String operaTime;
    /**
     * 创建时间
     */
    private String createTime;
}
