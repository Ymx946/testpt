/*
 * @(#)ConstantsUtil.java 2013-11-12下午03:25:14
 * Copyright 2013 sinovatech, Inc. All rights reserved.
 */
package com.mz.common;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局常量工具类
 *
 * @author apple
 */
@Component("ConstantsUtil")
public class ConstantsUtil {
    /**
     * CPU密集型：通常采用cpu核数＋1
     */
    public static final Integer CPU_NUM = Runtime.getRuntime().availableProcessors() + 1;

    /**
     * 线程池大小设置为 2N+1
     */
    public static final Integer IO_NUM = 2 * Runtime.getRuntime().availableProcessors() + 1;

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    public static final String BASE64_HEADER = "data:image/jpg;base64,";
    /**
     * 验证 url 是否以 http:// 或 https:// 开头
     */
    public static final String HTTP_REGEX = "^https?://.*$";

    public final static String EXCEL2003 = ".xls";
    public final static String EXCEL2007 = ".xlsx";

    /**
     * 最小距离(米)
     */
    public static final int LNGLAT_LATEST_DISTANCE = 10;

    /**
     * 订单过期时间 30分钟=30*60*1000
     */
    public static final Long ORDER_TIME_OUT = 1800000L;

    /**
     * 订单过期时间 29分钟
     */
    public static final int PAY_TIME_OUT = 29;


    /**
     * 经纬度 距离缓存
     */
    public static final Map<String, String> DISTANCE_MAP = new ConcurrentHashMap<String, String>();


    /**
     * 是否
     */
    public static final String YES = "Y";
    public static final String NO = "N";
    public static final String M = "M";
    /**
     * 是否int
     */
    public static final int YES_INT = 1;
    public static final int NO_INT = 2;
    /**
     * 1未完成2已完成
     */
    public static final int NO_FINISH_INT = 1;
    public static final int FINISH_INT = 2;
    /**
     * 逻辑删除: -1删除 1-正常
     */
    public static final int IS_DEL = -1;
    public static final int IS_DONT_DEL = 1;
    /**
     * 状态 -2:已下架 -1:未发布 1:已上架/出售中
     **/
    public static final int GARDEN_STATE_OFFLINE = -2;
    public static final int GARDEN_STATE_NEW = -1;
    public static final int GARDEN_STATE_ONLINE = 1;
    /**
     * 类型 1-自由模式 2-全托管模式
     */
    public static final int GARDEN_MEAL_TYPE_FREE = 1;
    public static final int GARDEN_MEAL_TYPE_HOSTING = 2;
    /**
     * 来源类型： 1-下单购买的 2-通过分享得来的
     */
    public static final int GARDEN_MEMBER_SOURCE_TYPE_ORDER = 1;
    public static final int GARDEN_MEMBER_SOURCE_TYPE_SHARE = 2;
    /**
     * 地块状态 -1：空闲(未租) 1-已租 2-结束
     */
    public static final int PARCEL_STATE_NEW = -1;
    public static final int PARCEL_STATE_LET = 1;
    public static final int PARCEL_STATE_OVER = 2;
    /**
     * 启用状态 1正常-1禁用
     **/
    public static final int STATE_NORMAL = 1;
    public static final int STATE_UN_NORMAL = -1;
    /**
     * 1-年 2-季 3-月 4-天
     */
    public static final int MEAL_UNITTYPE_YEAR = 1;
    public static final int MEAL_UNITTYPE_QUARTER = 2;
    public static final int MEAL_UNITTYPE_MONTH = 3;
    public static final int MEAL_UNITTYPE_DAY = 4;
    /**
     * 文件类型 1-图片 2-音频 3-视频
     */
    public static final int FILE_TYPE_IMG = 1;
    public static final int FILE_TYPE_RADIO = 2;
    public static final int FILE_TYPE_VIDEO = 3;
    /**
     * 1.仅退款 2.申请换货  3.退货退款
     */
    public static final int REFUND_TYPE_ONLY = 1;
    public static final int REFUND_TYPE_CHANGE = 2;
    public static final int REFUND_TYPE_REFUNDS = 3;
    /**
     * 1.全退 2.部分退
     */
    public static final int REFUND_TYPE_ALL = 1;
    public static final int REFUND_TYPE_PORTION = 2;
    /**
     * 1.全退 2.部分退
     */
    public static final String REFUND_TYPE_ALL_STR = "全退";
    public static final String REFUND_TYPE_PORTION_STR = "部分退";
    /**
     * 异常订单状态 -2 拒绝/失败 -1申请 1申请通过 2已退款/退换货 -3撤销 -4申请不通过
     */
    public static final int REFUND_STATE_FAIL = -2;
    public static final int REFUND_STATE_APPLY = -1;
    public static final int REFUND_STATE_AUDIT_PASS = 1;
    public static final int REFUND_STATE_PAID = 2;
    public static final int REFUND_STATE_REVOCATION = -3;
    public static final int REFUND_STATE_UN_PASS = -4;
    /**
     * 微信退款状态 1.待退款 2.已退款 -1.退款失败
     */
    public static final int WEIXIN_REFUND_WAIT = 1;
    public static final int WEIXIN_REFUND_PASS = 2;
    public static final int WEIXIN_REFUND_FAIL = -1;
    /**
     * 实施类型 1-本人到现场实施 2-农场管家托管
     */
    public static final int OPERATE_TYPE_ONSITE = 1;
    public static final int OPERATE_TYPE_HOSTING = 2;
    /**
     * 消息 1-菜园服务2-会议提醒 3-研学消息 31-研学通知 4物品供求 99-公开分端消息
     */
    public static final int MESSAGE_TYPE_GARDEN = 1;
    public static final int MESSAGE_TYPE_MEETING = 2;
    public static final int MESSAGE_TYPE_STUDY = 3;
    public static final int MESSAGE_TYPE_STUDY_NOTICE = 31;
    public static final int MESSAGE_TYPE_GOODS_SUPPLY = 4;
    public static final int MESSAGE_TYPE_OPEN = 99;
    /**
     * 处理状态 -1待处理 1已处理
     */
    public static final int DEAL_STATE_WAITING = -1;
    public static final int DEAL_STATE_ALREADY = 1;
    /**
     * 研学状态 1正常 -1未付款 -2已退款 -3商家取消中 -4已取消 -11拼团中
     **/
    public static final int STUDY_STATE_NORMAL = 1;
    public static final int STUDY_STATE_UN_PAY = -1;
    public static final int STUDY_STATE_REFUND = -2;
    public static final int STUDY_STATE_CANCEL_WAITING = -3;
    public static final int STUDY_STATE_CANCEL_ALREADY = -4;
    public static final int STUDY_STATE_CANCEL_GROUPING = -11;
    /**
     * 票种 1亲子票（特殊处理） 3儿童票
     */
    public static final String TICKET_TYPE_CODE_PARENTING = "1";
    public static final String TICKET_TYPE_CODE_CHILDREN = "3";
    /**
     * 人员类型 1成人 2儿童
     */
    public static final int PERSONNEL_TYPE_ADULT = 1;
    public static final int PERSONNEL_TYPE_CHILDREN = 2;
    /**
     * 下单类型1普通2团购
     */
    public static final int ORDER_BUY_TYPE_ORDINARY = 1;
    public static final int ORDER_BUY_TYPE_GROUP = 2;
    /**
     * 码颜色类型1绿2黄3红
     */
    public static final int QR_CODE_COLOUR_GREEN = 1;
    public static final int QR_CODE_COLOUR_YELLOW = 2;
    public static final int QR_CODE_COLOUR_RED = 3;
    /**
     * 状态1成功-1拼团中-2拼团失败 -3已退款
     **/
    public static final int GROUP_STATE_SUCCESS = 1;
    public static final int GROUP_STATE_PROGRESS = -1;
    public static final int GROUP_STATE_FAIL = -2;
    public static final int GROUP_STATE_REFUND = -3;
    /**
     * 小程序端分类：1游客2村民3村委4主体5村医6健康网格员7村委领导
     **/
    public static final int SITE_OBJECT_TYPE_TOURIST = 1;
    public static final int SITE_OBJECT_TYPE_VILLAGER = 2;
    public static final int SITE_OBJECT_TYPE_COMMITTEE = 3;
    public static final int SITE_OBJECT_TYPE_COMPANY = 4;
    public static final int SITE_OBJECT_TYPE_VILLAGE_DOCTOR = 5;
    public static final int SITE_OBJECT_TYPE_HEALTHY_GRID_INSPECTOR = 6; //健康网格员 == 计生小组长
    public static final int SITE_OBJECT_TYPE_VILLAGE_COMMITTEE = 7;
    public static final String SITE_OBJECT_TYPE_TOURIST_STR = "游客端";
    public static final String SITE_OBJECT_TYPE_VILLAGER_STR = "村民端";
    public static final String SITE_OBJECT_TYPE_COMMITTEE_STR = "村委端";
    public static final String SITE_OBJECT_TYPE_COMPANY_STR = "主体端";
    public static final String SITE_OBJECT_TYPE_VILLAGE_DOCTOR_STR = "村医";
    public static final String SITE_OBJECT_TYPE_HEALTHY_GRID_INSPECTOR_STR = "健康网格员";
    public static final String SITE_OBJECT_TYPE_VILLAGE_COMMITTEE_STR = "村委领导";
    /**
     * 特殊身份：1村委 2主体 3农技专家 4医生 5-企业员工 6党员
     **/
    public static final int SPECIAL_IDENTITY_COMMITTEE = 1;
    public static final int SPECIAL_IDENTITY_COMPANY = 2;
    public static final int SPECIAL_IDENTITY_EXPERT = 3;
    public static final int SPECIAL_IDENTITY_DOCTOR = 4;
    public static final int SPECIAL_IDENTITY_STAFF = 5;
    public static final int SPECIAL_IDENTITY_PARTY = 6;
    /**
     * 审核状态：-2审核驳回 1申请中 2审核通过(同意)
     **/
    public static final int CHECK_STATE_APPLY = 1;
    public static final int CHECK_STATE_PASS = 2;
    public static final int CHECK_STATE_REJECT = -2;
    /**
     * 跳转类型 1消费者跳转商铺 2成员跳转家园 3村委跳转户
     **/
    public static final int FAMILY_SCAN_JUMP_SHOP = 1;
    public static final int FAMILY_SCAN_JUMP_SELF = 2;
    public static final int FAMILY_SCAN_JUMP_COMMITTEE = 3;
    /**
     * 发货方式1自提2快递
     **/
    public static final int SEND_TYPE_SELF = 1;
    public static final int SEND_TYPE_POST = 2;
    public static final String SEND_TYPE_SELF_STR = "到店自提";
    public static final String SEND_TYPE_POST_STR = "快递配送";
    /**
     * 使用对象类型1-全商品 2-指定商品
     **/
    public static final int COUPON_USE_OBJ_TYPE_ALL = 1;
    public static final int COUPON_USE_OBJ_TYPE_SINGLE_GOODS = 2;
    /**
     * 使用场景代码 1-商城 2-菜园 3-研学（数据字典）
     **/
    public static final int COUPON_USE_OBJ_SHOP = 1;
    public static final int COUPON_USE_OBJ_GARDEN = 2;
    public static final int COUPON_USE_OBJ_STUDY = 3;
    /**
     * 使用状态 1待使用 2已使用
     **/
    public static final int USE_WAITING = 1;
    public static final int USE_ALREADY = 2;
    /**
     * 领取方式 1-商城领取 2-运营发放 3活动发放
     **/
    public static final int RECEIVE_TYPE_SHOP = 1;
    public static final int RECEIVE_TYPE_SEND = 2;
    public static final int RECEIVE_TYPE_ACTIVITY = 3;
    /**
     * 状态 -3撤回 -2退回
     * -1待处理，
     * 1处理通过（待买家发货），2买家已发货（待卖家收货），3卖家收货确认（待卖家发货）4卖家发货（买家待收货）5 买家确认收货
     * 11待退款审核 12退款审核通过
     */
    public static final int AFTER_SALE_STATE_CANCEL = -3;
    public static final int AFTER_SALE_STATE_RETURN = -2;
    public static final int AFTER_SALE_STATE_DEAL_WAITING = -1;
    public static final int AFTER_SALE_STATE_DEAL_PASS = 1;
    public static final int AFTER_SALE_STATE_BUYER_SEND = 2;
    public static final int AFTER_SALE_STATE_SELLER_RECEIVE = 3;
    public static final int AFTER_SALE_STATE_SELLER_SEND = 4;
    public static final int AFTER_SALE_STATE_BUYER_RECEIVE = 5;
    public static final int AFTER_SALE_STATE_REFUND_WAITING = 11;
    public static final int AFTER_SALE_STATE_REFUND_ALREADY = 12;
    /**
     * 1买家 2卖家
     */
    public static final int BUYER_INT = 1;
    public static final int SELLER_INT = 2;
    /**
     * 运费类型1-自定义 2-卖家承担（包邮）
     */
    public static final int POSTAGE_BUYER_INT = 1;
    public static final int POSTAGE_SELLER_INT = 2;
    /**
     * 积分变更类型 1初始2后台变更3积分兑换4其他
     */
    public static final int CHANGE_TYPE_INITIAL = 1;
    public static final int CHANGE_TYPE_SYS_MODIFY = 2;
    public static final int CHANGE_TYPE_EXCHANGE = 3;
    public static final int CHANGE_TYPE_OTHER = 4;
    /**
     * 积分变更类型 1初始2后台变更3积分兑换4其他
     */
    public static final String CHANGE_TYPE_INITIAL_STR = "初始";
    public static final String CHANGE_TYPE_SYS_MODIFY_STR = "后台变更";
    public static final String CHANGE_TYPE_EXCHANGE_STR = "积分兑换";
    public static final String CHANGE_TYPE_OTHER_STR = "其他";
    /**
     * 收支类型 1收2支
     */
    public static final int INOUT_TYPE_IN = 1;
    public static final int INOUT_TYPE_OUT = 2;
    /**
     * 收支类型 1收2支
     */
    public static final String INOUT_TYPE_IN_STR = "收入";
    public static final String INOUT_TYPE_OUT_STR = "支出";
    /**
     * 设施类型1房屋设施 2民宿酒店
     */
    public static final int FACILITIES_TYPE_HOUSE = 1;
    public static final int FACILITIES_TYPE_HOTEL = 2;
    /**
     * 房屋租赁状态 -3已下架1已发布2已签约
     */
    public static final int HOUSE_LEASE_OFFLINE = -3;
    public static final int HOUSE_LEASE_UN_PASS = -2;
    public static final int HOUSE_LEASE_CHECK_WAITING = -1;
    public static final int HOUSE_LEASE_ONLINE = 1;
    public static final int HOUSE_LEASE_SIGN = 2;
    /**
     * 互动类型1收藏2沟通
     */
    public static final int COMM_TYPE_COLLECTION = 1;
    public static final int COMM_TYPE_COMMUNICATE = 2;
    /**
     * 状态-2已取消-1待确认1已签约
     */
    public static final int RENTER_STATE_CANCEL = -2;
    public static final int RENTER_STATE_WAITING = -1;
    public static final int RENTER_STATE_SIGN = 1;
    /**
     * 季节分类 1（上半年） 2（下半年） 3全年
     */
    public static final int HALF_YEAR_TYPE_FIRST = 1;
    public static final int HALF_YEAR_TYPE_LAST = 2;
    //    /**
//     * 季节分类 1春季2秋季
//     */
//    public static final int SEASON_TYPE_SPRING = 1;
//    public static final int SEASON_TYPE_AUTUMN = 2;
    // 春季月份（3， 4， 5， 6， 7， 8）20220906作废
    // 秋季月份（9， 10，11，12， 1，2）20220906作废
    // 上半年（1-6月），下半年（7-12月）20220906更新启用
//    public static final Integer[] SPRING_MONTH = new Integer[]{3,4,5,6,7,8};
//    public static final Integer[] AUTUMN_MONTH = new Integer[]{9,10,11,12,1,2};
//    public static final Integer[] SPRING_MONTH = new Integer[]{1, 2, 3, 4, 5, 6};
//    public static final Integer[] AUTUMN_MONTH = new Integer[]{7, 8, 9, 10, 11, 12};
    // 修改需求
    public static final int ALL_YEAR = 3;
    public static final Integer[] FIRST_HALF_YEAR = new Integer[]{1, 2, 3, 4, 5, 6};
    public static final Integer[] LAST_HALF_YEAR = new Integer[]{7, 8, 9, 10, 11, 12};
    /**
     * 监控类型 1菜园 2-桃园
     */
    public static final int MONITOR_BUS_TYPE_GARDEN = 1;
    public static final int MONITOR_BUS_TYPE_PEACH = 2;
    /**
     * 测试租户ID
     */
    public static final Long TEST_TENANTID = 1478277449404907520L;
    /**
     * 凤桥租户ID
     */
    public static final Long FQ_TENANTID = 1513101480867921920L;
    /**
     * 凤桥站点ID
     */
    public static final Long FQ_SITEID = 1538032818087526400L;
    /**
     * 凤桥默认的用户
     */
    public static final String FQ_USERNAME = "南湖区凤桥镇";
    /**
     * 崇贤租户ID
     */
    public static final Long CX_TENANTID = 1501750690320482304L;
    /**
     * 崇贤站点ID
     */
    public static final Long CX_SITEID = 1574576476966092800L;
    /**
     * 崇贤默认的用户
     */
    public static final String CX_USERNAME = "崇贤";
    /**
     * 三星租户ID
     */
    public static final Long SX_TENANTID = 1666767959801987072L;
    /**
     * 三星站点ID
     */
    public static final Long SX_SITEID = 1669545181625450496L;
    /**
     * 三星默认的用户
     */
    public static final String SX_USERNAME = "三星";
    /**
     * 博山租户ID
     */
    public static final Long BS_TENANTID = 1633398000652910592L;
    /**
     * 1淡季2旺季
     * //淡季一般指的是每年的11月份至来年的三月份，旺季是每年四月至十月
     */
    public static final int SEASON_TYPE_LOW = 1;
    public static final int SEASON_TYPE_BUSY = 2;
    public static final Integer[] SEASON_TYPE_LOW_MONTH = new Integer[]{11, 12, 1, 2, 3};
    public static final Integer[] SEASON_TYPE_BUSY_MONTH = new Integer[]{4, 5, 6, 7, 8, 9, 10};
    /**
     * 库存模式1普通库存2日历库存
     */
    public static final int STOCK_MODE_ORDINARY = 1;
    public static final int STOCK_MODE_CALENDAR = 2;
    /**
     * 门票类型1单票2联票
     */
    public static final int ENTRANCE_TICKET_TYPE_SINGLE = 1;
    public static final int ENTRANCE_TICKET_TYPE_MULTIPLE = 2;
    /**
     * 用章申请状态： -1-初始录入(待受理)  1-待审批(处理中)  2-审批通过(成功办结)  -2审批不通过(不予受理)
     */
    public static final int SEAL_APPLY_STATE_NEW = -1;
    public static final int SEAL_APPLY_STATE_PROCESSING = 1;
    public static final int SEAL_APPLY_STATE_PASS = 2;
    public static final int SEAL_APPLY_STATE_REJECT = -2;
    /**
     * 办事预约状态： -1-待确认  1-已受理  2-已完成  -2已驳回
     */
    public static final int RESERVATION_STATE_NEW = -1;
    public static final int RESERVATION_STATE_PROCESSING = 1;
    public static final int RESERVATION_STATE_PASS = 2;
    public static final int RESERVATION_STATE_REJECT = -2;
    /**
     * 上报类型 1-入住 2-退房
     */
    public static final int PLACE_REPORT_IN = 1;
    public static final int PLACE_REPORT_OUT = 2;
    /**
     * 状态  -1 待复检 1已复检
     */
    public static final int ENTERPRISE_STATE_UNCHECK = -1;
    public static final int ENTERPRISE_STATE_CHECKED = 1;
    /**
     * 陡平安投诉状态 1待受理 2待处理 3不予受理 4已处理
     **/
    public static final int SAFE_WAIT_ACCEPT_STATE = 1;
    public static final int SAFE_WAIT_HANDLE_STATE = 2;
    public static final int SAFE_NOT_ACCEPT_STATE = 3;
    public static final int SAFE_FINISH_HANDLE_STATE = 4;
    /**
     * 分账延迟时间 == 70秒
     */
    // public static final long SEPARATE_DELAY_TIME = 70000;
    public static final long SEPARATE_DELAY_TIME = 5000;
    /**
     * https://kf.qq.com/touch/sappfaq/220420FbQjaQ220420Eruu2y.html?scene_id=kf594&platform=15
     * 腾分手续费
     */
    public static final Double TENCENT_FEES_RADIO = 0.006;
    /**
     * 分账状态：-6已退款 -4: 退款失败 -2: 不满足分账  -1未分账 1-分账进行中 2-重新分账 3-分账关闭 4-分账失败 5-部分分账成功 6-已分账成功
     */
    public static final Integer PROFIT_DIVIDED_STATE_REFUND_SUCCESS = -6;
    public static final Integer PROFIT_DIVIDED_STATE_REFUND_FAIL = -4;
    public static final Integer PROFIT_DIVIDED_STATE_NOTENOUGH = -2;
    public static final Integer PROFIT_DIVIDED_STATE_UNDIVIDED = -1;
    public static final Integer PROFIT_DIVIDED_STATE_DIVIDING = 1;
    public static final Integer PROFIT_DIVIDED_STATE_REDIVID = 2;

    public static final Integer PROFIT_DIVIDED_STATE_CLOSED = 3;
    public static final Integer PROFIT_DIVIDED_STATE_FAIL = 4;
    public static final Integer PROFIT_DIVIDED_STATE_PART_SUCCESS = 5;
    public static final Integer PROFIT_DIVIDED_STATE_ALL_SUCCESS = 6;

    public static final Integer PROFIT_ACCEPTOR_TYPE_PERSON = 1;
    public static final Integer PROFIT_ACCEPTOR_TYPE_MERCHANT = 2;

    /**
     * 分账支付类型：1微信支付
     */
    public static final Integer PROFIT_SHARING_PAY_TYPE_WECHAT = 1;

    public static final String PROFIT_SHARING_RECEIVER_MERCHANT = "MERCHANT_ID";
    public static final String PROFIT_SHARING_RECEIVER_PERSONAL = "PERSONAL_OPENID";

    /**
     * 分账回退结果 4失败 6成功
     */
    public static final int PROFIT_SHARING_RETURN_RESULT_FAIL = 4;
    public static final int PROFIT_SHARING_RETURN_RESULT_SUCCESS = 6;

    /**
     * 订单状态
     * 1待付款
     * 9已完成
     * 99关闭/失效
     * -3异常申请，
     * -4异常审核通过，
     * -5 异常审核不通过
     * -6已退款
     */
    public static final int RECEIVE_ORDER_STATE_NEW = 1;
    public static final int RECEIVE_ORDER_STATE_FINISH = 9;
    public static final int RECEIVE_ORDER_STATE_CLOSED = 99;
    // 退款 -3异常申请，-4异常审核通过，-5 异常审核不通过 -6已退款
    public static final int RECEIVE_ORDER_STATE_REFUND_NEW = -3;
    public static final int RECEIVE_ORDER_STATE_REFUND_AUDITPASS = -4;
    public static final int RECEIVE_ORDER_STATE_REFUND_FAIL = -5;
    public static final int RECEIVE_ORDER_STATE_REFUND_REFUNDED = -6;

    /**
     * excel 导出状态 -1：未导出 1: 已导出
     */
    public static final int EXCEL_EXPORT_STATE_NEW = -1;
    public static final int EXCEL_EXPORT_STATE_SUCCESS = 1;


    // 库存变更类型 1增加2减少
    public static int UPDATE_TYPE_INCREASE = 1;
    public static int UPDATE_TYPE_REDUCE = 2;
    /**
     * 短信签名
     */
    public static String SMS_SIGN = "数字乡村";
    /**
     * 评论记录 类型  1-游记(目前游记有自己的点赞评论) 2-物品 3-评论
     */
    public static Integer COMMENT_TYPE_TRAVELS = 1;
    public static Integer COMMENT_TYPE_GOODS = 2;
    public static Integer COMMENT_TYPE_COMMENT = 3;
    /**
     * 批量提交的个数
     */
    public static Integer BATCH_COMMIT_NUM = 1000;
    /**
     * 用户默认头像
     */
    public static String MEMBERUSER_DEFAULT_HEADIMG = "https://img.mzszxc.com/mzsz/futurevillage/zhejiang/my-menu/default.png";
    /**
     * 默认密码
     */
    public static String PASSWORD_DEFAULT = "mz1234567";
    /**
     * 行程类型1去程2返程
     */
    public static int CAR_TRIP_TYPE_GO = 1;
    public static int CAR_TRIP_TYPE_BACK = 2;
    /**
     * 评价类型1场所评价2订单消费评价
     */
    public static int EVALUATE_TYPE_PLACE = 1;
    public static int EVALUATE_TYPE_ORDER = 2;
    /**
     * 站台类型1起始站2中间站3终点站
     */
    public static int STATION_TYPE_START = 1;
    public static int STATION_TYPE_MIDDLE = 2;
    public static int STATION_TYPE_END = 3;
    /**
     * 票种1成人2老人3儿童
     */
    public static int TICKET_SORT_ADULT = 1;
    public static int TICKET_SORT_ELDERLY = 2;
    public static int TICKET_SORT_CHILDREN = 3;
    public static String TICKET_SORT_ADULT_STR = "成人票";
    public static String TICKET_SORT_ELDERLY_STR = "老人票";
    public static String TICKET_SORT_CHILDREN_STR = "儿童票";
    /**
     * 订单状态1待付款2待接单3待上车4待下车9已完成99已取消-6已退款
     */
    public static int PASSENGER_ORDER_STATE_PAY_WAITING = 1;
    public static int PASSENGER_ORDER_STATE_DRIVER_RECEIVE_WAITING = 2;
    public static int PASSENGER_ORDER_STATE_ON_CAR_WAITING = 3;
    public static int PASSENGER_ORDER_STATE_OFF_CAR_WAITING = 4;
    public static int PASSENGER_ORDER_STATE_FINISH = 9;
    public static int PASSENGER_ORDER_STATE_CANCEL = 99;
    public static int PASSENGER_ORDER_STATE_REFUND = -6;
    public static Long AGRI_TENANTID = 1478277449404907520L;
    public static Long AGRI_SITEID = 1514873032714223616L;
    public static ConcurrentHashMap<String, String> nodeMap = new ConcurrentHashMap<String, String>() {{
        put("WDC", 1535524473489326081L + "@我的村"); // 我的村 查看5秒
        put("YGCW", 0L + "@阳光村务"); // 阳光村务 查看10秒
        put("CTXL", 1540206894252032000L + "@村通讯录"); // 村通讯录 查看5秒
        put("NYFW", 0L + "@农技服务"); // 农技服务 查看10秒
        put("CQSB", 1535524969591603200L + "@村情上报"); // 村情上报 完成一次业务闭环(村民上报+村委后台处理完成)
        put("CGYY", 1535516645039013888L + "@场馆预约"); // 场馆预约 完成一次业务闭环(村委后台添加场所预约-场馆管理基础数据，村民手机提交预约，村委后台审核同意)
        put("FXBB", 1535524821868216320L + "@返乡报备"); // 返乡报备 完成一次业务闭环(村民手机端提交出行报备、外来申报)
        put("ZLGA", 1535524473489326082L + "@助老关爱"); // 一键呼叫拨打电话 即 助老关爱
    }};
    /**
     * 小程序码 类型1游记 2研学 3研学团购 4菜园 5商城 6门票 7民宿 8店铺支付(豆)9会员支付(豆)10村民支付(豆)11流动人口支付(豆)
     */
    public static int WX_ACODEUN_TYPE_STUDY = 2;
    public static int WX_ACODEUN_TYPE_GARDEN = 4;
    public static int WX_ACODEUN_TYPE_SHOP = 5;
    public static int WX_ACODEUN_TYPE_SHOP_BEAN_PAY = 8;
    public static int WX_ACODEUN_TYPE_MEMBER_BEAN_PAY = 9;
    public static int WX_ACODEUN_TYPE_VILLAGER_BEAN_PAY = 10;
    public static int WX_ACODEUN_TYPE_NONLOCAL_BEAN_PAY = 11;
    public static String WX_ACODEUN_TYPE_STUDY_URL = "researchStudy/info/index";// 研学
    public static String WX_ACODEUN_TYPE_GARDEN_URL = "shareGarden/garden-item/garden-item";// 菜园
    public static String WX_ACODEUN_TYPE_SHOP_URL = "shopStore/item/item";// 商城
    public static String WX_ACODEUN_TYPE_SHOP_BEAN_PAY_URL = "co-rich/pay/pay";// 店铺支付(豆)
    public static String WX_ACODEUN_TYPE_BEAN_PAY_URL = "shopStore/item/item";// 人员支付(豆)--待前端提供…………
    public static String WX_ACODEUN_TYPE_STUDY_PARAM = "id=";
    public static String WX_ACODEUN_TYPE_GARDEN_PARAM = "id=";
    public static String WX_ACODEUN_TYPE_SHOP_PARAM = "id=";
    public static String WX_ACODEUN_TYPE_SHOP_BEAN_PAY_PARAM = "placeId=";
    public static String WX_ACODEUN_TYPE_MEMBER_BEAN_PAY_PARAM = "memberId=";
    public static String WX_ACODEUN_TYPE_VILLAGER_BEAN_PAY_PARAM = "personnelId=";
    public static String WX_ACODEUN_TYPE_NONLOCAL_BEAN_PAY_PARAM = "nonLocalPeopleId=";
    //    业务类型1村庄风采视频2种业信息3数字监控4生活圈5特色产品
    //    6文化活动7研学实景8爱乡积分宝9医保采访视频10日间养老中心
    //    11风貌视频12乡村记忆13乡村生态14村庄环境15电子导览16VR展示
    public static int SCREEN_FILE_TYPE_VILLAGE_VIDEO = 1;
    public static int SCREEN_FILE_TYPE_SEED_PIC = 2;
    public static int SCREEN_FILE_TYPE_MONITOR = 3;
    public static int SCREEN_FILE_TYPE_LIFE_CIRCLE = 4;
    public static int SCREEN_FILE_TYPE_FEATURED_PRODUCTS = 5;
    public static int SCREEN_FILE_TYPE_CULTURAL_ACTIVITY = 6;
    public static int SCREEN_FILE_TYPE_STUDY_REALISTIC = 7;
    public static int SCREEN_FILE_TYPE_SCORE_PIC = 8;
    public static int SCREEN_FILE_TYPE_INSURANCE_INTERVIEW = 9;
    public static int SCREEN_FILE_TYPE_DAY_CARE_CENTER = 10;
    public static int SCREEN_FILE_TYPE_STYLE_VIDEO = 11;
    public static int SCREEN_FILE_TYPE_RURAL_MEMORY = 12;
    public static int SCREEN_FILE_TYPE_RURAL_ECOLOGY = 13;
    public static int SCREEN_FILE_TYPE_VILLAGE_ENVIRONMENT = 14;
    public static int SCREEN_FILE_TYPE_ELECTRONIC_GUIDE = 15;
    public static int SCREEN_FILE_TYPE_VR_EXHIBITION = 16;
    // 开关功能代码
    public static String FUNCTION_CODE_BIRTHDAY_MSG = "birthdayMsg";// 生日短信开关
    // 业务类型 1闲置物品2好物推荐3-人员招聘 4-闲置资源
    public static String FUNCTION_CODE_SUPPLY1 = "supply1";// 邻里交易审核
    public static String FUNCTION_CODE_SUPPLY2 = "supply2";// 好物推荐审核
    public static String FUNCTION_CODE_SUPPLY3 = "supply3";// 人员招聘审核
    public static String FUNCTION_CODE_SUPPLY4 = "supply4";// 闲置资源审核
    public static String FUNCTION_CODE_HOUSE_LEASE = "houseLease";// 房屋租赁
    public static String FUNCTION_CODE_WISH = "wish";// 微心愿
    public static String FUNCTION_CODE_TRAVELS = "travels";// 游记
    public static String FUNCTION_CODE_PEACH_DYNAMIC = "peachDynamic";// 桃园动态
    public static String FUNCTION_CODE_PERSONNEL_FAMILY_TYPE = "personnelFamilyType";// 人员住户类型变更
    public static String FUNCTION_CODE_URGENT_VISIT = "urgentVisit";// 紧急走访开关
    public static String IDENTITY_CODE_ISSUING = "identityCodeIssuing";// 产品溯源身份码审核
    public static String FAMILY_MEMBER_ADD = "familyMemberAdd";// 家庭成员添加审核
    /**
     * 启动状态 -1:未启动 1:已启动
     */
    public static int DEFINE_FORM_ACTIVE_UNSTARTED = -1;
    public static int DEFINE_FORM_ACTIVE_STARTED = 1;
    /**
     * 状态 1已启用 -1已冻结 -2已注销
     */
    public static int IOTCARD_STATE_ACTIVATE = 1;
    public static int IOTCARD_STATE_FREEZE = -1;
    public static int IOTCARD_STATE_CANCEL = -2;
    /**
     * 状态 0规则未生效1规则生效中-1规则已过期
     */
    public static int RULE_SET_NOT_EFFECT = 0;
    public static int RULE_SET_EFFECT = 1;
    public static int RULE_SET_OVERDUE = -1;
    /**
     * 就餐扫码类型： type 1-实体码 2-虚拟码
     */
    public static int MEAL_SCAN_TYPE_IOTCARD = 1;
    public static int MEAL_SCAN_TYPE_VIRTUAL = 2;
    /**
     * 类型：1-早餐 2-午餐 3-晚餐
     */
    public static int MEAL_TYPE_BREAKFAST = 1;
    public static int MEAL_TYPE_LUNCH = 2;
    public static int MEAL_TYPE_DINNER = 3;
    /**
     * 状态 -1待启用 1启用中 -2已停用
     */
    public static int EXAMINE_RULE_STATE_NOT_START = -1;
    public static int EXAMINE_RULE_STATE_START = 1;
    public static int EXAMINE_RULE_STATE_DEAD = -2;
    /**
     * 状态 -1未开始 1-进行中 2-已结束
     */
    public static int PROJECT_STATE_NOTSTART = -1;
    public static int PROJECT_STATE_INPROGRESS = 1;
    public static int PROJECT_STATE_ENDED = 2;
    /**
     * 类型：1- 普通安全检查 2-专家安全检查 3-环保安全检查
     */
    public static int ENTERPRISE_CHECK_TYPE_COMMON = 1;
    public static int ENTERPRISE_CHECK_TYPE_EXPERT = 2;
    public static int ENTERPRISE_CHECK_TYPE_ENV = 3;
    /**
     * 安全检查、专家安全检查与环保检查三个都改，在检查记录页面添加查询条件已检查与未检查，已检查与之前一样查询已复检与待复检的企业，未检查查询一次都没检查过的企业，只做列表展示
     */
    public static int ENTERPRISE_CHECK_STATE_UNCHECK = -1;
    public static int ENTERPRISE_CHECK_STATE_CHECKED = 1;
    /**
     * 需求类型：1-村民(默认) 2-政府
     */
    public static int ENTERPRISE_JOB_TYPE_VILLAGE = 1;
    public static int ENTERPRISE_JOB_TYPE_GOV = 2;
    /**
     * 类型 1-村 2社区
     */
    public static int AREA_EXAM_TYPE_VILLAGE = 1;
    public static int AREA_EXAM_TYPE_COMMUNITY = 2;
    /**
     * 专题库类型 1安全生产 2党建学习
     */
    public static int SAFETY_PRODUCTION = 1;
    public static int PARTY_BUILDING_STUDY = 2;
    public static int USER_ACCOUNT_UNLOCK = -1;
    public static int USER_ACCOUNT_LOCKED = 1;
    /**
     * 12345满意情况：1- 满意 2-结果不满意 3-过程不满意
     */
    public static int COMPLAINT_SATISFY = 1;
    public static int COMPLAINT_RESULT_NOT_SATISFY = 2;
    public static int COMPLAINT_COURSE_NOT_SATISFY = 3;
    /**
     * 共富豆明细冻结状态
     */
    public static int UNFREEZE_STATE = 1;
    public static int FREEZE_STATE = -1;
    /**
     * 活动模板状态 1未开始 2进行中 3草稿 4已结束
     */
    public static int ACTIVITY_NOT_START_STATE = 1;
    public static int ACTIVITY_UNDERWAY_STATE = 2;
    public static int ACTIVITY_DRAFT_STATE = 3;
    public static int ACTIVITY_OVER_STATE = 4;

    /**
     * 是否走分账
     */
    public static String PROFIT_SHARING_YES = "Y";
    public static String PROFIT_SHARING_NO = "N";
    /**
     * 是否中奖
     */
    public static int WIN_PRIZE_NO = 1;
    public static int WIN_PRIZE_YES = 2;

    /**
     * 审核结果：-2-驳回 -未审核 1-同意申请
     */
    public static int SPACE_APPLY_AUDIT_STATE_NEW = -1;
    public static int SPACE_APPLY_AUDIT_STATE_REJECT = -2;
    public static int SPACE_APPLY_AUDIT_STATE_AGREE = 1;


    /**
     * 招商空间申请 处理状态：-1未处理 1已处理
     */
    public static int SPACE_APPLY_DEAL_STATE_NEW = -1;
    public static int SPACE_APPLY_DEAL_STATE_PROCESSED = 1;

    /**
     * 游记展示类型 1公开2自己可见3好友可见4指定人可见5敏感不可见
     */
    public static int TRAVELS_OPEN = 1;
    public static int TRAVELS_ONESELF = 2;
    public static int TRAVELS_FRIEND = 3;
    public static int TRAVELS_APPOINT = 4;
    public static int TRAVELS_INVISIBLE = 5;

    /**
     * 是否开启微信小程序 发货信息管理功能 -1否 1是
     */
    public static int WXAPPLET_SEND_GOODS_NO = -1;
    public static int WXAPPLET_SEND_GOODS_YES = 1;

    /**
     * 关联状态： -1未关联 1-已关联
     */
    public static int ASSOCIATED_STATE_NO = -1;
    public static int ASSOCIATED_STATE_YES = 1;

    /**
     * 类型：1-65岁以上(默认)、2-65岁以下
     */
    public static int HEALTHY_EXAM_AGE65 = 65;
    public static int HEALTHY_EXAM_AGE65_UP = 1;
    public static int HEALTHY_EXAM_AGE65_DOWN = 2;

    public static String SIGN_LOCATION = "甲方签字：";
    public static String SEAL_LOCATION = "乙方（盖章）：";
    public static String KEY_STORE_URL = "https://img.mzszxc.com/mzsz/server/signDoctor/client.p12";

    public static String SIGN_DOCTOR_LICENSE_URL = "https://img.mzszxc.com/mzsz/server/signDoctor/license.xml";
    public static String SIGN_DOCTOR_TEMPLATE_URL = "https://img.mzszxc.com/mzsz/server/signDoctor/healthy_signing_doctor.docx";

    /**
     * 家庭医生签约状态：-6已终止 -2已过期 -1未生效 1-生效中
     */
    public static int SIGN_STATE_STOP = -6;
    public static int SIGN_STATE_EXPIRED = -2;
    public static int SIGN_STATE_UNEFFECT = -1;
    public static int SIGN_STATE_INEFFECT = 1;

}
