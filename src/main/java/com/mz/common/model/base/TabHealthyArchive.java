package com.mz.common.model.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 健康档案表(TabHealthyArchive)实体类
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class TabHealthyArchive {
    private static final long serialVersionUID = 721029996321589602L;
    public static int REMOTE_DOCTOR = 1;// 远程问诊
    public static int INTERVIEW_FOCUS_GROUPS = 2;// 重点人群走访
    public static int CHECKUP_REPORTING = 3;// 体检档案
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
     * 人员ID
     */
    private Long personnelId;
    /**
     * 人员名称
     */
    private String realName;
    /**
     * 会员ID
     */
    private Long memberId;
    /**
     * 档案时间(走访/就诊时间)
     */
    private String archiveTime;
    /**
     * 档案内容(走访/就诊内容)
     */
    private String archiveContent;
    /**
     * 来源相关人员ID(走访人/医生)
     */
    private Long sourceLinkUserId;
    /**
     * 来源相关人员名称(走访人/医生)
     */
    private String sourceLinkUserName;
    /**
     * 档案类型 1-远程问诊 2-重点人群走访 3-体检档案
     */
    private Integer archiveType;
    /**
     * 来源ID
     */
    private Long sourceId;
    /**
     * 逻辑删除
     */
    private Integer delState;
    /**
     * 状态1正常-1禁用
     */
    private Integer state;
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
