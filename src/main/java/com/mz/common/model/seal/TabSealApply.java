package com.mz.common.model.seal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 用章申请表(TabSealApply)表实体类
 *
 * @author yangh
 * @since 2023-04-06 18:47:00
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class TabSealApply implements Serializable {
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
     * 用章使用场景字典代码
     */
    private String dictCode;
    /**
     * 用章使用场景字典名称
     */
    private String dictName;
    /**
     * 模板id
     */
    private Long templateId;
    /**
     * 模板名称
     */
    private String templateName;
    /**
     * 内容(用途说明)
     */
    private String applyContent;
    /**
     * 用章申请图片
     */
    private String applyPic;
    /**
     * 申请人的用户id
     */
    private String applicantId;
    /**
     * 申请人
     */
    private String applicant;
    /**
     * 联系电话
     */
    private String applicantPhone;
    /**
     * 审批人ID
     */
    private Long approverId;
    /**
     * 审批人
     */
    private String approver;
    /**
     * 电子签名路径
     */
    private String signatureUrl;
    /**
     * 章id
     */
    private Long sealId;
    /**
     * 章路径
     */
    private String sealUrl;
    /**
     * 逻辑删除
     */
    private Integer delState;
    /**
     * 状态 -1-初始录入  1-待审批  2-审批通过  -2审批不通过
     */
    private Integer state;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private String createUser;
    /**
     * 修改时间
     */
    private Date modifyTime;
    /**
     * 修改人
     */
    private String modifyUser;
}

