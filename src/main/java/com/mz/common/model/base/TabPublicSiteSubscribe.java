package com.mz.common.model.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 站点订阅消息模板(TabPublicSiteSubscribe)实体类
 *
 * @author makejava
 * @since 2022-01-12 14:03:08
 */
@Setter
@Getter
@ToString
public class TabPublicSiteSubscribe {
    private static final long serialVersionUID = -84197294034624576L;

    public static  String TEMPLATE_NAME_CALL ="预约叫号通知";
    public static  String TEMPLATE_NAME_CHANGE ="服务变更通知";
    public static  String TEMPLATE_NAME_DOCTOR_REMIND ="开始问诊提醒";
    public static  String TEMPLATE_NAME_PLACE_APPOINT_SUCCESS ="预约成功通知";
    public static  String TEMPLATE_NAME_PLACE_APPOINT_RETURN ="预约取消通知";
    public static  String TEMPLATE_NAME_WORK_APPOINT_SUCCESS ="办事预约成功通知";
    public static  String TEMPLATE_NAME_WORK_APPOINT_RETURN ="办事预约取消通知";

    public static  String TEMPLATE_STUDY_NOTICE ="活动通知";

    public static  String[] TEMPLATE_NAME_PLACE_APPOINT_ARR =new String[]{"预约成功通知","预约取消通知"};//场所预约订阅模板列表
    public static  String[] TEMPLATE_NAME_REGISTER_ARR =new String[]{"预约叫号通知","服务变更通知"};// 挂号订阅模板列表
    public static  String[] TEMPLATE_NAME_DOCTOR_ARR =new String[]{"开始问诊提醒"};//医生订阅模板列表
    public static  String[] TEMPLATE_NAME_STUDY_ARR =new String[]{"活动通知"};//研学订阅模板列表
    public static  String[] TEMPLATE_NAME_WORK_APPOINT_ARR =new String[]{"办事预约成功通知","办事预约取消通知"};//办事预约订阅模板列表

    /**
    * 主键
    */
    private String id;
    /**
    * 所属租户
    */
    private String tenantId;
    /**
    * 站点ID
    */
    private String siteId;
    /**
    * 站点代码
    */
    private String siteCode;
    /**
    * 站点名称
    */
    private String siteName;
    /**
    * 模板名称
    */
    private String templateName;
    /**
    * 模板ID
    */
    private String templateId;
    /**
    * 模板内容
    */
    private String templateContent;
    /**
    * 参数名
    */
    private String templatePara;
    /**
    * 跳转页面地址
    */
    private String pageName;
    /**
     * 模板类型 1-小程序模板2-公众号模板
     */
    private Integer subscribeType;
    /**
     * 推送对象 1-全体村民 2-村医 3-家庭医生 4-户成员
     */
    private String pushObject;
    /**
     * 授权范围 1-重点人群走访
     */
    private String accreditRange;
    /**
     * 模板描述
     */
    private String templateDescription;
    /**
     * 逻辑删除: -1删除 1-正常
     */
    private Integer delState;
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
    * 备注
    */
    private String remarks;

}