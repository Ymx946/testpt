package com.mz.common.model.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 应用申请表(TabApplicationBase)实体类
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class TabApplicationBase  {
    private static final long serialVersionUID = 370499761818474649L;
    /**
    * 主键
    */
    private Long id;
    /**
    * 应用类型1-PC 2-H5
    */
    private Integer applicationType;
    /**
    * 应用名称
    */
    private String applicationName;
    /**
    * 应用级别 1-乡镇级 2-区县级 3-市级 4-省级
    */
    private Integer applicationLevel;
    /**
    * 申报主体
    */
    private String declarationSubject;
    /**
    * 应用英文名称
    */
    private String applicationNameE;
    /**
    * 应用图标
    */
    private String applicationIcon;
    /**
    * 应用简介
    */
    private String applicationIntroduction;
    /**
    * 应用正式地址
    */
    private String applicationLinkAddress;
    /**
     * 应用测试地址
     */
    private String applicationTestUrl;
    /**
     * 文档跳转地址
     */
    private String docUrl;
    /**
    * 业主统一社会信用代码
    */
    private String ownerCode;
    /**
    * 业务负责人
    */
    private String ownerChargePerson;
    /**
    * 业务负责人电话
    */
    private String ownerChargePhone;
    /**
    * 业务负责邮箱
    */
    private String ownerChargeEmail;
    /**
    * 开发商名称
    */
    private String developerName;
    /**
    * 开发商区域代码
    */
    private String developerAreaCode;
    /**
    * 开发商区域名称
    */
    private String developerAreaName;
    /**
    * 运维负责姓名
    */
    private String devOpsPerson;
    /**
    * 运维负责电话
    */
    private String devOpsPhone;
    /**
    * 运维负责邮箱
    */
    private String devOpsEmail;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 技术审批人ID
     */
    private String technologyId;
    /**
     * 技术审批人
     */
    private String technology;
    /**
     * 乡镇审批人ID
     */
    private String villageRoverId;
    /**
     * 乡镇审批人
     */
    private String villageRover;
    /**
     * 区级审批人ID
     */
    private String districtRoverId;
    /**
     * 区级审批人
     */
    private String districtRover;
    /**
     * 审核结果1通过 0未通过
     */
    private Integer result;
    /**
    * 状态 0 提交审核 1 审核中 2审核不通过 3已审核(上架) 4已撤销
    */
    private Integer state;
    /**
    * 逻辑删除
    */
    private Integer delState;
    /**
    * 创建时间
    */
    private String createTime;
    /**
    * 创建人
    */
    private String createUser;
    /**
    * 修改时间
    */
    private String modifyTime;
    /**
    * 修改人
    */
    private String modifyUser;
    /**
    * 备注
    */
    private String remarks;

}
