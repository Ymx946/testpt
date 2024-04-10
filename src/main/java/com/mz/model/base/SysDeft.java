package com.mz.model.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统定义表(SysDeft)实体类
 *
 * @author makejava
 * @since 2021-03-17 11:00:04
 */
@Setter
@Getter
@ToString
public class SysDeft {
    private static final long serialVersionUID = -48317858247439920L;
    /**
    * 主键
    */
    private String id;
    /**
    * 系统代码
    */
    private String sysCode;
    /**
    * 系统名称
    */
    private String sysName;
    /**
    * 类型1-监管端2-农企端
    */
    private Integer sysType;
    /**
    * 备注
    */
    private String remarks;
    /**
    * 图片logo
    */
    private String sysLogo;
    /**
    * 系统性质1系统设置2业务系统
    */
    private Integer sysManage;
    /**
     * 介绍
     */
    private String sysIntroduce;
    /**
     * 发布范围(区域代码)
     */
    private String releaseScope;
    /**
     * 范围名称
     */
    private String scopeName;
    /**
     * 所属类型 1-自建 2-第三方
     */
    private Integer belongType;
    /**
     * 供应商名称
     */
    private String supplierName;
    /**
     * 是否应用 1是2否
     */
    private Integer applicationType;
    /**
     * 应用主体类型 1-政府机关2-村主体 3企业（多选）4医疗
     */
    private String useType;
    /**
     * 系统链接地址
     */
    private String sysAddr;
    /**
     * 区域级别 1-省2-市3-区县4-乡镇5-村社
     */
    private String areaLevel;
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
}