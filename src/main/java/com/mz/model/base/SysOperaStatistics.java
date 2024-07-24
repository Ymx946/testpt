package com.mz.model.base;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.mz.common.annotation.FieldMeta;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 操作统计表(SysOperaStatistics)实体类
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class SysOperaStatistics extends Model<SysOperaStatistics> {
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
    @FieldMeta(name = "区域名称")
    private String areaName;
    /**
     * 主体ID
     */
    private Long mainBodyId;
    /**
     * 主体名称
     */
    @FieldMeta(name = "主体名称")
    private String mainBodyName;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 账号
     */
    @FieldMeta(name = "账号")
    private String loginName;
    /**
     * 姓名
     */
    @FieldMeta(name = "姓名")
    private String realName;
    /**
     * 最后登录IP
     */
    @FieldMeta(name = "IP")
    private String ip;
    /**
     * 最后登录操作地址
     */
    @FieldMeta(name = "最后登录操作地址")
    private String ipAddress;
    /**
     * 登录次数
     */
    @FieldMeta(name = "登录次数")
    private Integer loginNum;
    /**
     * 业务操作次数
     */
    @FieldMeta(name = "业务操作次数")
    private Integer busOperaNum;
    /**
     * 最后登录设备
     */
    @FieldMeta(name = "最后登录设备")
    private String operaEqpm;
    /**
     * 操作时间
     */
    @FieldMeta(name = "操作时间")
    private String operaTime;
    /**
     * 创建时间
     */
    private String createTime;
}
