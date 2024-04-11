package com.mz.model.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统数据字典(SysDataDict)实体类
 *
 * @author makejava
 * @since 2021-03-17 10:58:51
 */
@Setter
@Getter
@ToString
public class SysDataDict {
    private static final long serialVersionUID = 380589106554752266L;
    public static String PRODUCE_MAJOR_DICT_TYPE_CODE = "NCPLX";// 数据字典类型代码--农产品大类代码
    public static String PRODUCE_SUB_DICT_TYPE_CODE = "NCPEJFL";// 数据字典类型代码--农产品小类代码

    public static String PLACE_MAJOR_DICT_TYPE_CODE = "PLACE_MAJOR";// 数据字典类型代码--场所大类代码
    public static String PLACE_SUB_DICT_TYPE_CODE = "PLACE_SUB";// 数据字典类型代码--场所小类代码

    public static String PLACE_SERVICE_MAJOR_DICT_TYPE_CODE = "PLACE_SERVICE_MAJOR";// 数据字典类型代码--场所特色服务大类代码
    public static String PLACE_SERVICE_SUB_DICT_TYPE_CODE = "PLACE_SERVICE_SUB";// 数据字典类型代码--场所特色服务小类代码

    public static String CPRZ_DICT_TYPE_CODE = "CPRZ";// 数据字典类型代码--产品认证

    public static String HY_DICT_TYPE_CODE = "HY";// 数据字典类型代码--产品认证

    //    01 中共党员；02 中共预备党员；03共青团员；04 民革党员；05 民盟盟员；06 民建会员；07 民进会员；08 农工党党员；09 致公党党员；10 九三学社社员；11 台盟盟员；12 无党派人士；13群众
    public static String POLITICAL_TYPE_CODE = "POLITICAL";// 数据字典类型代码--政治面貌
    //    未知、文盲、小学、初中、高中、中专、大专、本科、研究生、博士
    public static String EDUCATION_TYPE_CODE = "EDUCATION";// 数据字典类型代码--文化程度
    //     1-乡镇级 2-区县级 3-市级 4-省级
    public static String HONOR_LEVEL_TYPE_CODE = "HONOR_LEVEL";// 数据字典类型代码--荣誉级别
    //     1-乡镇级 2-区县级 3-市级 4-省级
    public static String HONOR_LEVEL_TYPE_MLTY_CODE = "HONOR_LEVEL_MLTY";// 数据字典类型代码--美丽庭院荣誉级别
    //     1-乡镇级 2-区县级 3-市级 4-省级
    public static String HONOR_LEVEL_TYPE_WMJT_CODE = "HONOR_LEVEL_WMJT";// 数据字典类型代码--文明家庭荣誉级别
    //     1-乡镇级 2-区县级 3-市级 4-省级
    public static String HONOR_LEVEL_TYPE_ZMJT_CODE = "HONOR_LEVEL_ZMJT";// 数据字典类型代码--最美家庭荣誉级别
    //    民族1汉族2少数名族
    public static String NATION_TYPE_CODE = "NATION";// 数据字典类型代码--民族
    //    按钮类型
    public static String NODE_BUTTON_TYPE_CODE = "NODE_BUTTON";// 数据字典类型代码--按钮类型
    //    数据展示
    public static String BIG_SHOW_TYPE_CODE = "BIG_SHOW";// 数据字典类型代码--数据展示
    // 场景类型
    public static String SCENE_TYPE_DICT_TYPE_CODE = "SCENE_TYPE";// 数据字典类型代码--场景类型
    // 入党流程步骤
    public static String PARTY_PROCESS_TYPE_CODE = "PARTY_PROCESS";// 数据字典类型代码--入党流程步骤
    public static String PARTY_PROCESS_TIME_TYPE_CODE = "PARTY_PROCESS_TIME";// 数据字典类型代码--入党流程时间点类型
    public static String PARTY_PROCESS_REMIND_OBJECT_TYPE_CODE = "PARTY_PROCESS_REMIND_OBJECT";// 数据字典类型代码--入党流程提醒人类型
    // 闲置物品
    public static String GOODS_TYPE_TYPE_CODE = "GOODS_TYPE";// 数据字典类型代码--闲置物品
    // 好物推荐
    public static String RECOMMEND_TYPE_TYPE_CODE = "RECOMMEND_TYPE";// 数据字典类型代码--好物推荐
    // 闲置资源
    public static String IDLE_RESOURCES_TYPE_CODE = "IDLE_RESOURCES";// 数据字典类型代码--闲置资源
    // 管控原因
    public static String CONTROL_REASON_TYPE_CODE = "CONTROL_REASON";// 数据字典类型代码--管控原因
    // 隔离方式
    public static String ISOLATION_MODE_TYPE_CODE = "ISOLATION_MODE";// 数据字典类型代码--隔离方式

    public static String VEHICLE_TYPE_CODE = "VEHICLE";// 数据字典类型代码--交通工具

    public static String UNIT_TYPE_CODE = "UNIT";// 数据字典类型代码--计量单位
    public static String SHELF_LIFE_TIME_TYPE_CODE = "SHELF_LIFE_TIME_TYPE";// 保质期时间类型

    public static String GARDEN_SERVICE_TYPE_CODE = "GARDEN_SERVICE";// 数据字典类型代码--菜园服务

    public static String GARDEN_AREA_TYPE_CODE = "GARDEN_AREA";// 数据字典类型代码--菜园面积

    public static String STUDY_TYPE_TYPE_CODE = "STUDY_TYPE";// 数据字典类型代码--研学类型

    public static String STUDY_SON_TYPE_TYPE_CODE = "STUDY_SON_TYPE";// 数据字典类型代码--研学子类型

    public static String STUDY_FEATURE_TYPE_CODE = "STUDY_FEATURE";// 数据字典类型代码--研学特色

    public static String TICKET_TYPE_TYPE_CODE = "TICKET_TYPE";// 数据字典类型代码--票种类型

    public static String WECHAT_NODE_SORT_TYPE_CODE = "WECHAT_NODE_SORT";// 数据字典类型代码--小程序功能场景分类

    public static String WECHAT_NODE_USE_TYPE_CODE = "WECHAT_USE_SORT";// 数据字典类型代码--小程序功能应用分类

    public static String MEETING_MAJOR_TYPE_CODE = "MEETING_MAJOR";// 数据字典类型代码--会议大类

    public static String MEETING_SUB_TYPE_CODE = "MEETING_SUB";// 数据字典类型代码--会议小类

    public static String PHONE_BOOK_TYPE_CODE = "PHONE_BOOK";// 数据字典类型代码--通讯录类型

    public static String CULTURE_FEATURES_TYPE_CODE = "CULTURE_FEATURES";// 数据字典类型代码--文化特质culture features

    public static String SHOP_GOODS_MAJOR_TYPE_CODE = "SHOP_GOODS_MAJOR";// 数据字典类型代码--电商商品大类代码
    public static String SHOP_GOODS_SUB_TYPE_CODE = "SHOP_GOODS_SUB";// 数据字典类型代码--电商商品小类代码
    public static String SHOP_GOODS_FEATURE_TYPE_CODE = "SHOP_GOODS_FEATURE";// 数据字典类型代码--电商商品特色
    public static String COUPON_USE_OBJ_TYPE_CODE = "COUPON_USE_OBJ";// 数据字典类型代码--优惠券场景
    public static String EXPRESS_TYPE_TYPE_CODE = "EXPRESS_TYPE";// 数据字典类型代码--快递
    public static String AFTER_SALE_REASON_TYPE_CODE = "AFTER_SALE_REASON";// 数据字典类型代码--售后原因

    public static String ENTRANCE_TICKET_SORT_TYPE_CODE = "ENTRANCE_TICKET_SORT";// 数据字典类型代码--门票分类【门票、游玩项目、景区联票】

    public static String HOTEL_FACILITIES_SORT_TYPE_CODE = "HOTEL_FACILITIES_SORT";// 数据字典类型代码--酒店设施分类
    // 大屏
    public static String PERSONNEL_SCREEN_DATA_TYPE_CODE = "PERSONNEL_SCREEN_DATA";// 数据字典类型代码--大屏人员数据类型
    public static String EVENT_SCREEN_DATA_TYPE_CODE = "EVENT_SCREEN_DATA";// 数据字典类型代码--大屏事件数据类型
    public static String BLOCK_SCREEN_DATA_TYPE_CODE = "BLOCK_SCREEN_DATA";// 数据字典类型代码--大屏区块数据类型
    public static String DEVICE_SCREEN_DATA_TYPE_CODE = "DEVICE_SCREEN_DATA";// 数据字典类型代码--大屏设备数据类型
    public static String OTHER_SCREEN_DATA_TYPE_CODE = "OTHER_SCREEN_DATA";// 数据字典类型代码--其他数据类型
    public static String SCORE_GIVE_PROJECT_DATA_TYPE_CODE = "SCORE_GIVE_PROJECT";// 数据字典类型代码--积分赋分项目类型
    public static String XTLMGL_DATA_TYPE_CODE = "XTLMGL";// 数据字典类型代码--系统栏目管理
    public static String DATA_SOURCE_TYPE_CODE = "JCZJSJYLX";// 数据字典类型代码--基础组件数据源类型

    public static String PUSH_PLATFORM_TYPE_CODE = "TSPT"; // 数据字典类型代码--推送拼平台;

    public static String SITE_ADDRESS_TYPE_TYPE_CODE = "SITE_ADDRESS_TYPE"; // 数据字典类型代码--站点业务地址类型;
//    public static String FAMILY_EVENT_BUS_TYPE_TYPE_CODE = "FAMILY_EVENT_BUS_TYPE"; //数据字典类型代码--户事件业务类型;(作废)

    public static String SCREEN_MODULAR_SOURCE_TYPE_CODE = "SCREEN_MODULAR_SOURCE"; // 数据字典类型代码--驾驶舱模块统计数据来源分类;
    public static String SCREEN_MODULAR_TIME_AXIS_SOURCE_TYPE_CODE = "SCREEN_MODULAR_TIME_AXIS_SOURCE"; // 数据字典类型代码--驾驶舱模块动态感知来源分类;

    public static String SPACE_PLAN_TYPE_CODE = "SPACE_PLAN_TYPE"; // 数据字典类型代码--空间规划数据类型

    public static String QCLM_CZTS_CODE = "QCLM_CZTS"; // 数据字典类型代码--千村联盟村庄特色类型

    public static String HOUSE_RESIDENT_TYPE_TYPE_CODE = "HOUSE_RESIDENT_TYPE"; // 数据字典类型代码--房屋住户类型
    public static String BANK_TYPE_CODE = "BANK_TYPE";// 数据字典类型代码--银行类型
    public static String HOLDER_RELATION_CODE = "HOLDER_RELATION";// 数据字典类型代码--户主关系
    public static String SYSTEM_TYPE_CODE = "SYSTEM_TYPE"; // 数据字典类型代码--系统类型
    public static String SGJFJJF_TYPE_CODE = "SGJFJJF_TYPE"; // 数据字典类型代码--手工计分加减分类型
    public static String REPORTING_TYPE_CODE = "REPORTING_TYPE"; // 数据字典类型代码--事件反馈上报类型
    public static String DUTY_TYPE_CODE = "DUTY_TYPE"; // 数据字典类型代码--职务类型
    public static String TASK_ISSUE_TYPE_CODE = "TASK_ISSUE_TYPE"; // 数据字典类型代码--任务派发类型
    public static String EXAMINE_RULE_TYPE_CODE = "EXAMINE_RULE_TYPE"; // 数据字典类型代码--考核管理评分规则类型
    public static String CHECK_LINK_APP_TYPE_CODE = "CHECK_LINK_APP_TYPE"; // 数据字典类型代码--考核管理关联应用类型
    public static String ZBBSDP_TYPE_CODE = "ZBBSDP"; // 淄博博山店铺上报
    public static String WORK_GUIDE_TYPE_CODE = "WORK_GUIDE_TYPE"; // 办事指南类型
    public static String CONVENIENT_RESERVATION_TYPE_CODE = "CONVENIENT_RESERVATION"; // 办事预约类型
    public static String FIRE_PROTECTION_AREA_TYPE_CODE = "FIRE_PROTECTION_AREA_TYPE"; // 防火区域类型
    public static String INTEGRAL_RULE_TYPE_CODE = "INTEGRAL_RULE_TYPE"; // 积分规则类型
    public static String INTEGRAL_LINK_USE_MAJOR_CODE = "INTEGRAL_LINK_USE_MAJOR"; // 积分管理关联应用大类代码
    public static String INTEGRAL_LINK_USE_SUB_CODE = "INTEGRAL_LINK_USE_SUB"; // 积分管理关联应用小类代码
    public static String VISIT_SITUATION_CODE = "VISIT_SITUATION"; // 探访情况
    public static String VISIT_REALISTIC_SITUATION_CODE = "VISIT_REALISTIC_SITUATION"; // 现实状况
    public static String ENTERPRISE_RUN_TYPE_CODE = "ENTERPRISE_RUN_TYPE"; // 企业跑团服务类型
    public static String VILLAGER_RUN_TYPE_CODE = "VILLAGER_RUN_TYPE"; // 村民跑团服务类型
    public static String PEACH_ADOPT_CYCLE_TYPE_CODE = "PEACH_ADOPT_CYCLE_TYPE"; // 桃树认养周期类型
    public static String PEACH_ADOPT_PRICE_TYPE_CODE = "PEACH_ADOPT_PRICE_TYPE"; // 桃树认养价格类型
    public static String COMM_MESSAGE_FUNCTION_TYPE_CODE = "COMM_MESSAGE_FUNCTION"; // 消息推送功能类型
    public static String COMM_MESSAGE_TIME_TYPE_TYPE_CODE = "COMM_MESSAGE_TIME_TYPE"; // 消息推送时间类型
    public static String SPOT_TYPE_CODE = "SPOT_TYPE"; // 图斑类型
    public static String VILLAGE_SEAL_TYPE_CODE = "VILLAGE_SEAL_TYPE"; // 村公章类型
    public static String VILLAGE_PARTY_SEAL_TYPE_CODE = "VILLAGE_PARTY_SEAL_TYPE"; // 村党组织章类型
    public static String PARTY_ORGANIZATION_TYPE_CODE = "PARTY_ORGANIZATION_TYPE"; // 党组织类型
    public static String MAIN_BODY_TYPE_CODE = "MAIN_BODY_TYPE"; // 主体类型
    public static String FUND_APPLY_TYPE_CODE = "FUND_APPLY_TYPE"; // 资金申请事项类型
    public static String QUESTION_BANK_TYPE_CODE = "QUESTION_BANK_TYPE"; // 题库类型
    public static String QUESTION_BANK_HARD_CODE = "QUESTION_BANK_HARD"; // 题库难度
    public static String VISIT_APPEAL_TYPE_CODE = "VISIT_APPEAL"; // 大走访诉求类型
    public static String COMPLAINT_HANDLE_RESULT_CODE = "COMPLAINT_HANDLE_RESULT"; // 12345走访处理结果
    public static String SAFE_COMPLAINT_TYPE_CODE = "SAFE_COMPLAINT_TYPE"; // 陡平安投诉类型
    public static String INDEX_PUSH_TYPE_CODE = "INDEX_PUSH"; // 首页推荐业务类型
    public static String RELEASE_OBJECT_TYPE_CODE = "RELEASE_OBJECT"; // 发布对象类型
    public static String DUTY_UNIT_CODE = "DUTY_UNIT"; // 责任单位
    public static String COMPLAINT_PROBLEM_TYPE_CODE = "COMPLAINT_PROBLEM_TYPE"; // 问题分类
    public static String BOTTOM_NAVIGATION_DEFAULT_CODE = "BOTTOM_NAVIGATION_DEFAULT"; // 底部导航默认跳转
    public static String BOTTOM_NAVIGATION_TAB_CODE = "BOTTOM_NAVIGATION_TAB"; // 底部导航TAB跳转
    public static String CLASSIFICATION_OF_EQUIPMENT_CODE = "CLASSIFICATION_OF_EQUIPMENT"; // 设备分类
    public static String MOBILE_PAGE_IDENTITY_TYPE_CODE = "MOBILE_PAGE_IDENTITY_TYPE"; // 微页面身份类型
    public static String VISIT_OLD_LIFE_INFO_TYPE_CODE = "VISIT_OLD_LIFE_INFO"; // 养老情况
    public static String VISIT_MEDICAL_INFO_TYPE_CODE = "VISIT_MEDICAL_INFO"; // 医疗情况
    public static String FLOOD_AREA_TYPE_CODE = "FLOOD_AREA_TYPE"; // 洪水受灾区类型
    public static String CROP_GROW_CYCLE_CODE = "CROP_GROW_CYCLE"; // 作物生长周期
    public static String ACTIVE_TEMPLATE_TYPE_CODE = "ACTIVE_TEMPLATE_TYPE"; // 活动模板类型
    public static String CITY_CLASSIFY_CODE = "CITY_CLASSIFY"; // 城乡分类代码
    public static String MOBILE_MANAGE_PAGE_TYPE_CODE = "MOBILE_MANAGE_PAGE_TYPE"; // 微页面类型

    public static String MERCHANT_APPLYMENT_STATE = "MERCHANT_APPLYMENT_STATE"; // 特约商户-申请单状态
    public static String MERCHANT_CONTACT_ID_DOC_TYPE = "MERCHANT_CONTACT_ID_DOC_TYPE"; // 特约商户-超级管理员证件类型
    public static String MERCHANT_SUBJECT_TYPE = "MERCHANT_SUBJECT_TYPE"; // 特约商户-主体类型
    public static String MERCHANT_CERTIFICATE_TYPE = "MERCHANT_CERTIFICATE_TYPE"; // 特约商户-登记证书类型
    public static String MERCHANT_CONTACT_TYPE = "MERCHANT_CONTACT_TYPE"; // 特约商户-超级管理员类型
    public static String MERCHANT_FINANCE_TYPE = "MERCHANT_FINANCE_TYPE"; // 特约商户-金融机构类型
    public static String MERCHANT_ID_DOC_TYPE = "MERCHANT_ID_DOC_TYPE"; // 特约商户-经营者/法人身份证件-证件类型
    public static String MERCHANT_SALES_SCENES_TYPE = "MERCHANT_SALES_SCENES_TYPE"; // 特约商户-经营场景类型
    public static String MERCHANT_BANK_ACCOUNT_TYPE = "MERCHANT_BANK_ACCOUNT_TYPE"; // 特约商户-账户类型
    public static String CAN_CONFIGURE_PAGE_TYPE_CODE = "CAN_CONFIGURE_PAGE_TYPE"; // 可配置页类型
    public static String MOBILE_MANAGE_PAGE_JUMP_LINK_CODE = "MOBILE_MANAGE_PAGE_JUMP_LINK"; // 微页面跳转链接
    public static String SCREEN_SECTION_TYPE_CODE = "SCREEN_SECTION_TYPE"; // 驾驶舱组件分类
    public static String BANK_ACCOUNT_TYPE_CORPORATE = "BANK_ACCOUNT_TYPE_CORPORATE"; // 驾驶舱组件分类
    public static String RESPONSIBLE_PLATE_TYPE_CODE = "RESPONSIBLE_PLATE_TYPE"; // 负责板块类型

    public static String SPACE_LAND_NATURE = "SPACE_LAND_NATURE"; // 招商空间-土地性质
    public static String PERSONNEL_TAG_TYPE_CODE = "PERSONNEL_TAG_TYPE"; // 人员标签类型
    public static String BRAND_FILL_UNIT_TYPE_CODE = "BRAND_FILL_UNIT_TYPE"; // 品牌填报单位类型

    public static String BRAND_ENDORSEMENT_AWARD_TYPE_CODE = "BRAND_ENDORSEMENT_AWARD"; // 品牌背书奖项类型--区域公共品牌
    public static String BRAND_IMPORTANCE_LEVEL_TYPE_CODE = "BRAND_IMPORTANCE_LEVEL"; // 品牌重要程度

    public static String BRAND_CHECK_SYSTEM_TYPE_CODE = "BRAND_CHECK_SYSTEM"; // 果品生产经营中的检测体系
    public static String BRAND_TRACE_SYSTEM_TYPE_CODE = "BRAND_TRACE_SYSTEM"; // 产品质量追溯体系
    public static String BRAND_STANDARD_TYPE_CODE = "BRAND_STANDARD"; // 本品牌主导制定以下相关标准
    public static String BRAND_AUTHENTICATION_TYPE_CODE = "BRAND_AUTHENTICATION"; // 认证体系
    public static String BRAND_PROMOTION_TYPE_CODE = "PROMOTION_TYPE"; //主要品牌推广方式
    public static String BRAND_FILL_MAIN_UNIT_TYPE_CODE = "BRAND_FILL_MAIN_UNIT_TYPE"; //自主品牌填报主体类型
    public static String BRAND_FILL_FRUIT_TYPE_CODE = "BRAND_FILL_FRUIT_TYPE"; //自主品牌填报果品类别
    public static String BRAND_FILL_LEAD_LEVEL_FIRST_TYPE_CODE = "BRAND_FILL_LEAD_LEVEL_FIRST"; //龙头级别一级分类
    public static String BRAND_FILL_LEAD_LEVEL_SECOND_TYPE_CODE = "BRAND_FILL_LEAD_LEVEL_SECOND"; //龙头级别二级分类

    public static String BRAND_ENDORSEMENT_AWARD_TYPE_CODE2 = "BRAND_ENDORSEMENT_AWARD2"; // 品牌背书奖项类型2--企业自主品牌
    public static String BRAND_FILL_PACKAGE_TYPE_CODE = "BRAND_FILL_PACKAGE_TYPE"; // 果品包装形式--企业自主品牌
    public static String GROUP_MEMBER_TYPE_CODE = "GROUP_MEMBER_TYPE"; // 团队成员类型
    public static String SIGNING_DOCTOR_ECONOMIC_NATURE_TYPE_CODE = "SIGNING_DOCTOR_ECONOMIC_NATURE"; // 家庭医生签约-人口经济性质
    public static String SIGNING_DOCTOR_SERVICE_TYPE_TYPE_CODE = "SIGNING_DOCTOR_SERVICE_TYPE"; // 家庭医生签约-服务类型
    public static String MARITAL_STATUS_CODE = "MARITAL_STATUS";// 数据字典类型代码--婚姻状况
    public static String RESCUE_VISIT_FAMILY_TYPE_CODE = "RESCUE_VISIT_FAMILY_TYPE";// 救助探访分配-家庭类型
    public static String PERMISSION_CONFIG_SYSTEM_TYPE_CODE = "PERMISSION_CONFIG_SYSTEM_TYPE";// 权限配置系统类型
    public static String UPDATE_SEND_DATA_TYPE_CODE = "UPDATE_SEND_DATA_TYPE";// 版本更新下发数据类型

    /**
     * 主键
     */
    private String id;
    /**
     * 类型代码
     */
    private String dictTypeCode;
    /**
     * 类型名称
     */
    private String dictTypeName;
    /**
     * 字典代码
     */
    private String dictCode;
    /**
     * 字典名称
     */
    private String dictName;
    /**
     * 区域代码
     */
    private String areaCode;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 可用状态(1可用2不可用)
     */
    private Integer useState;

}