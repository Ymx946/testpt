package com.mz.common.model.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 通用业务文件表(TabCommonFile)实体类
 *
 * @author makejava
 * @since 2022-05-20 16:14:08
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class TabCommonFile implements Serializable {
    private static final long serialVersionUID = -17903796988365434L;
    // 文件类型 1菜园图片2菜园日志 3课程 4研学 5研学相册 6关爱上门 7通知消息 8电商商品 9订单评论
    //          10建房 11售后申请 12客户积分记录
    //          13房屋租赁 14租房评价
    //          15户违纪图 16户违纪附件
    //          17车辆图片
    //          18民宿酒店房间图
    //          19线下研学记录
    //          20会员身份证（正面） 21 会员身份证（反面）
    //          22积分赋分
    //          23慰问探访
    //          24美丽庭院
    //          25文明家庭
    //          26最美家庭
    //          27户事件图片
    //          28动态图片
    //          29村情上报图片
    //          30云公章申请图片
    //          31任务派发图片
    //          32任务派发处理图片
    //          33店铺结算图片
    //          34便民中心申请材料附件
    //          35环境推进图片
    //          36桃树认养介绍图片
    //          37图斑整改完成图
    //          38村公章盖章材料
    //          39村公章重大决策材料
    //          40云会议图片
    //          41党组织章盖章材料
    //          42党组织章重大决策材料
    //          43大走访（群众大走访/扶贫大走访）
    //          44党员风采
    //          45主题党日活动封面图
    //          46资金审批补充材料
    //          47资金审批重大事项(民主程序)材料
    //          48陡平安信息发布材料
    //          49(12345走访照片)
    //          50陡平安投诉专区投诉图片
    //          51陡平安投诉专区处理图片
    //          52十必报图片
    //          53品牌包装
    //          54品牌文创
    //          55场景相似图
    //          56庭院经济-物种
    //          57高山蔬菜供求
    //          58庭院种物下发
    //          59庭院种物回收
    //          60洪水聚集区受灾图片
    //          61洪水聚集区灾后图片
    //          62资金申请明细说明图片
    //          63高山蔬菜作物生长周期图片
    //          64回收作物信息图片
    //          65乡村产品图片
    //          66参观预约单图片
    //          67参观预约单发票图片
    //          68重点人群走访照片
    //          71 招商空间图片
    //          72 招商空间-入驻申请图片
    //          73 办事指南事项办理流程图
    //          74 村民说事反馈图片
    //          75 村民说事处理图片

    //          99订单退款凭证

    public static int TYPE_GARDEN_BASE = 1;
    public static int TYPE_GARDEN_DIARY = 2;
    public static int TYPE_STUDY_CURRICULUM = 3;
    public static int TYPE_STUDY_DIGITAL = 4;
    public static int TYPE_STUDY_EXPERIENCE = 5;
    public static int TYPE_HEALTHY_CARE_VISIT = 6;
    public static int TYPE_COMMON_MESSAGE = 7;
    public static int TYPE_SHOP_GOODS = 8;
    public static int TYPE_MALL_ORDER_COMMENT = 9;
    public static int TYPE_FARMER_PLAN = 10;
    public static int TYPE_MALL_AFTER_SALE = 11;
    public static int TYPE_CUSTOMER_SCORE_RECORD = 12;
    public static int TYPE_HOUSE_LEASE = 13;
    public static int TYPE_HOUSE_LEASE_EVALUATE = 14;
    public static int TYPE_HOUSEHOLD_BREACH_PIC = 15;
    public static int TYPE_HOUSEHOLD_BREACH_ANNEX = 16;
    public static int TYPE_SIGHTSEE_CAR = 17;
    public static int TYPE_HOTEL_ROOM = 18;
    public static int TYPE_STUDY_RECORD = 19;
    public static int TYPE_MEMBER_ID_CARD_FRONT = 20;
    public static int TYPE_MEMBER_ID_CARD_BACK = 21;
    public static int TYPE_PUBLIC_SCORE_ASSIGNMENT = 22;
    public static int TYPE_CONDOLE_VISIT = 23;
    public static int TYPE_BEAUTIFUL_COURTYARD = 24;
    public static int TYPE_CULTURE_FAMILY = 25;
    public static int TYPE_BEST_BEAUTIFUL_FAMILY = 26;
    public static int TYPE_FAMILY_EVENT = 27;
    public static int TYPE_DYNAMIC_STATE = 28;
    public static int TYPE_VILLAGE_REPORT = 29;
    public static int TYPE_COMMON_SEAL = 30;
    public static int TYPE_TASK_ISSUE = 31;
    public static int TYPE_TASK_ISSUE_DEAL = 32;
    public static int TYPE_SHOP_CHECKOUT = 33;
    public static int TYPE_WORK_GUIDE = 34;
    public static int TYPE_ENV_ADVANCE_PICTURE = 35;
    public static int TYPE_PEACH_ADOPT_INTRODUCE_PICTURE = 36;
    public static int TYPE_FARM_SPOT_CORRECTION_MESSAGE = 37;// 整改后图片
    public static int TYPE_FARM_SPOT_CORRECTION_MESSAGE_1 = 371;// 整改前图片
    public static int TYPE_VILLAGE_SEAL_MATERIAL = 38;
    public static int TYPE_VILLAGE_SEAL_GREAT_DECISION = 39;
    public static int TYPE_CLOUD_CONFERENCE = 40;    // 40云会议图片
    public static int TYPE_VILLAGE_PARTY_SEAL_MATERIAL = 41;
    public static int TYPE_VILLAGE_PARTY_SEAL_GREAT_DECISION = 42;
    public static int TYPE_VISIT_RECORD = 43;
    public static int TYPE_PARTY_ELEGANT_DEMEANOUR = 44;
    public static int TYPE_PARTY_THEME_DAY_COVER = 45;
    public static int TYPE_FUND_APPROVE_REPLENISH = 46;
    public static int TYPE_FUND_APPROVE_DEMOCRATIC_PROCESS = 47;
    public static int TYPE_SAFE_INFORMATION_PUBLISH = 48;
    public static int TYPE_COMPLAINT_INTERVIEW = 49;
    public static int TYPE_SAFE_COMPLAINT = 50;
    public static int TYPE_SAFE_HANDLE = 51;
    public static int TYPE_TEN_MUST_REPORT = 52;
    public static int TYPE_BRAND_PACKAGING = 53;
    public static int TYPE_BRAND_CULTURAL_CREATION = 54;
    public static int TYPE_SCENE_SIMILARITY = 55;
    public static int TYPE_COURTYARD_SEED = 56;
    public static int TYPE_ALPINE_VEGETABLE_TRADE_LEADS = 57;
    public static int TYPE_COURTYARD_DISTRIBUTE_SEED = 58;
    public static int TYPE_COURTYARD_RECOVERY = 59;
    public static int TYPE_FLOOD_AREA_VICTIM = 60;
    public static int TYPE_FLOOD_AREA_AFTER_CALAMITY = 61;
    public static int TYPE_FUND_APPLY_DETAILS_PIC = 62;
    public static int TYPE_CROP_GROW_CYCLE_PIC = 63;
    public static int TYPE_CROP_RECYCLE_DETAIL_PIC = 64;
    public static int TYPE_VILLAGE_PRODUCT_PIC = 65;
    public static int TYPE_VISIT_APPOINTMENT_MEMBER_PIC = 66;//66参观预约单图片
    public static int TYPE_VISIT_APPOINTMENT_MEMBER_INVOICING_PIC = 67;//67参观预约单发票图片
    public static int TYPE_FOCUS_GROUPS_INTERVIEW_PIC = 68;//68重点人群走访照片

    public static int TYPE_INVESTMENT_SPACE_PIC = 71;
    public static int TYPE_INVESTMENT_SPACE_APPLY_PIC = 72;
    public static int WORK_GUIDE_WORK_PROCESS_PIC = 73;
    public static int VILLAGERS_TELL_FEEDBACK_PIC = 74;//村民说事反馈图片
    public static int VILLAGERS_TELL_MANAGE_PIC = 75;//村民说事处理图片
    public static int BUSINESS_HOSTING_SHOP_PIC = 76;//商家入驻店铺图片
    public static int BUSINESS_HOSTING_LICENSE_PIC = 77;//商家入驻营业执照图片
    public static int BUSINESS_HOSTING_OTHER_LICENSE_PIC = 78;//商家入驻其他营业许可证图片
    public static int RESCUE_VISIT_RECORD_PIC = 79;//救助探访图片
    public static int TRIP_CUSTOM_ACTIVITY_PIC = 80;//团队定制活动
    public static int TRIP_CUSTOM_LINE_PIC = 81;//团队定制线路


    public static int TYPE_ORDER_REFUND = 99;

    //    -----公共品牌填报
    //          1101公共品牌-品牌logo
    public static int TYPE_BRAND_PUBLIC_LOGO = 1101;
    //          1102公共品牌-品牌保护相关证明材料
    public static int TYPE_BRAND_PUBLIC_PROTECT = 1102;
    //          1103公共品牌-种植、加工历史资料
    public static int TYPE_BRAND_PUBLIC_PROCESSING_HISTORY = 1103;
    //          1104公共品牌-品牌背书文件
    public static int TYPE_BRAND_PUBLIC_ENDORSEMENT_AWARD = 1104;
    //          1105公共品牌-重要程度佐证材料
    public static int TYPE_BRAND_PUBLIC_IMPORTANCE_LEVEL = 1105;
    //          1106公共品牌-生态环境保护的相关措施相关资料
    public static int TYPE_BRAND_PUBLIC_MEASURES = 1106;
    //          1107公共品牌-相关品牌材料
    public static int TYPE_BRAND_PUBLIC_CULTIVATE_BRAND = 1107;
    //          1108公共品牌-产品质量追溯体系二维码照片
    public static int TYPE_BRAND_PUBLIC_QR_CODE = 1108;
    //          1109公共品牌-标准相关照片
    public static int TYPE_BRAND_PUBLIC_STANDARD_PHOTOS = 1109;
    //          1110公共品牌-区域经济中的重要程度佐证材料
    public static int TYPE_BRAND_PUBLIC_ECONOMIC_IMPORTANT = 1110;
    //          1111公共品牌-提供获奖清单及证明
    public static int TYPE_BRAND_PUBLIC_AWARD = 1111;
    //          1112公共品牌-承若书
    public static int TYPE_BRAND_PUBLIC_COMMITMENT_LETTER = 1112;

    //          1113公共品牌-统一品牌标识体系
    public static int TYPE_BRAND_PUBLIC_BRAND_IDENTITY = 1113;
    //          1114公共品牌-统一包装应用体系
    public static int TYPE_BRAND_PUBLIC_PACKAGING = 1114;


    //          1201企业自主品牌-技术证明材料
    public static int TYPE_BRAND_AUTONOMOUSLY_TECHNICAL = 1201;
    //          1202企业自主品牌-商标注册证
    public static int TYPE_BRAND_AUTONOMOUSLY_TRADEMARK = 1202;
    //          1203企业自主品牌-区域公用品牌相关证明
    public static int TYPE_BRAND_AUTONOMOUSLY_REGIONAL_PUBLIC = 1203;
    //          1204企业自主品牌-品牌背书照片
    public static int TYPE_BRAND_AUTONOMOUSLY_ENDORSEMENT = 1204;
    //          1205企业自主品牌-生态环境保护的相关措施相关资料
    public static int TYPE_BRAND_AUTONOMOUSLY_MEASURES = 1205;
    //     1206          企业自主品牌-品牌标志
    public static int TYPE_BRAND_AUTONOMOUSLY_LOGO = 1206;
    //     1207         企业自主品牌-包装照片
    public static int TYPE_BRAND_AUTONOMOUSLY_PACKAGE = 1207;
    //     1208          企业自主品牌-产品质量追溯体系二维码照片
    public static int TYPE_BRAND_AUTONOMOUSLY_QRCODE = 1208;
    //     1209      企业自主品牌-第三方质量检测报告
    public static int TYPE_BRAND_AUTONOMOUSLY_TEST_REPORT = 1209;
    //     1210  企业自主品牌-质量信用报告发布
    public static int TYPE_BRAND_AUTONOMOUSLY_QUALITY_CREDIT = 1210;
    //     1211          企业自主品牌-相关品牌材料
    public static int TYPE_BRAND_AUTONOMOUSLY_BRAND = 1211;
    //     1212 企业自主品牌-标准相关照片
    public static int TYPE_BRAND_AUTONOMOUSLY_STANDARD_PHOTOS = 1212;
    //     1213 企业自主品牌-提供获奖清单及证明
    public static int TYPE_BRAND_AUTONOMOUSLY_SWARD_PROVE = 1213;

    //         1214 企业自主品牌-承若书
    public static int TYPE_BRAND_AUTONOMOUSLY_COMMITMENT_LETTER = 1214;


    // 文件类型 1-图片 2-音频 3-视频 4-文件
    public static int FILE_TYPE_PIC = 1;
    public static int FILE_TYPE_VOICE = 2;
    public static int FILE_TYPE_VIDEO = 3;
    public static int FILE_TYPE_FILE = 4;
    /**
     * 主键
     */
    private Long id;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 文件类型 1-图片 2-音频 3-视频 4-文件
     */
    private Integer fileType;
    /**
     * 关联ID
     */
    private Long linkId;
    /**
     * 文件名称
     */
    private String name;
    /**
     * 文件地址
     */
    private String url;
    /**
     * 图片高
     */
    private String picHeight;
    /**
     * 图片宽
     */
    private String picWidth;
    /**
     * 创建时间
     */
    private Date createTime;

}