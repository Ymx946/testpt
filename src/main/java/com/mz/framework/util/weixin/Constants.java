package com.mz.framework.util.weixin;

/**
 * 微信小程序API：https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/qrcode-link/qr-code/getQRCode.html
 */
public class Constants {

    //    // 芒种数字乡村小服务号ID
//    public static final String APP_ID_WX = "wxc858f15e13603e4d";
//
//    //芒种数字乡村小服务号密钥
//    public static final String APP_SECRET_WX = "98df5f5ee375e3ba570ca52b827ce1ac";
//
//    // 芒种数字小程序id
//    public static final String APP_ID_APPLET = "wx3a8bdd56f81e5dff";
//
//    // 芒种数字小程序密钥
//    public static final String APP_SECRET_APPLET = "729a9e0a6693a464907ca70e8eddd9f1";
//
//    // 芒种数字商户号
//    public static final String MCH_ID = "1623630223";
//
    // API密钥，在商户平台设置
    public static final String API_KEY = "mangzhongshuzixiangcun1234567890";
//
//    // 微信证书ip
//    public static final String CREATE_IP = "47.118.49.82";
//
//    // HTTPS证书的本地路径
//    public static final String certLocalPath = "apiclient_cert.p12";

    // 是否使用异步线程的方式来上报API测速，默认为异步模式
    public static final boolean useThreadToDoReport = true;

    // 获取token接口(GET)
    public final static String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    // 获取已关注者的URL
    public final static String SUBSCRIBED_USERS_URL = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN";

    // 获取票据的URL
    public final static String TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

    /**
     * 获取客服基本信息
     */
    public final static String KFLIST_URL = "https://api.weixin.qq.com/cgi-bin/customservice/getkflist?access_token=ACCESS_TOKEN";
    /**
     * 获取客户基本信息
     */
    public final static String USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    /**
     * 获取客户基本信息(未关注公众号)
     */
    public final static String USER_INFO_NOCARE_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    // 刷新access_token接口（GET）
    public final static String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";

    public static final String OAUTH2_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    public static final String OAUTH2 = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";

    public static final String OAUTH2_URL_APPLET = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";

    public static final String OAUTH_URL_APPLET = "https://api.weixin.qq.com/sns/jscode2session";

    public static final String MEDIA_URL = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";

    public static final String LIVEINFO_URL = "http://api.weixin.qq.com/wxa/business/getliveinfo?access_token=ACCESS_TOKEN";

    // 以下是几个API的路径：
    public static final String UNIFIEDORDER_API = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    // 1）被扫支付API
    public static final String PAY_API = "https://api.mch.weixin.qq.com/pay/micropay";

    // 2）被扫支付查询API
    public static final String PAY_QUERY_API = "https://api.mch.weixin.qq.com/pay/orderquery";

    // 3）退款API
    public static final String REFUND_API = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    // 4）退款查询API
    public static final String REFUND_QUERY_API = "https://api.mch.weixin.qq.com/pay/refundquery";

    // 5）撤销API
    public static final String REVERSE_API = "https://api.mch.weixin.qq.com/secapi/pay/reverse";

    // 6）下载对账单API
    public static final String DOWNLOAD_BILL_API = "https://api.mch.weixin.qq.com/pay/downloadbill";

    // 7) 统计上报API
    public static final String REPORT_API = "https://api.mch.weixin.qq.com/payitil/report";

    // 8) 企业付款接口
    public static final String ENTERPRISE_TRANSFERS_API = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

    // 9) 企业付款查询API
    public static final String ENTERPRISE_TRANSFERS_PAY_QUERY_API = "https://api.mch.weixin.qq.com/mmpaymkttransfers/gettransferinfo";

    // 10) 企业付款到银行卡API
    public static final String ENTERPRISE_TRANSFERS_PAY_BANK_API = "https://api.mch.weixin.qq.com/mmpaysptrans/pay_bank";

    // 11) 查询企业付款银行卡API
    public static final String ENTERPRISE_TRANSFERS_QUERY_BANK_API = "https://api.mch.weixin.qq.com/mmpaysptrans/query_bank";

    // 12) 获取RSA加密公钥API
    public static final String ENTERPRISE_TRANSFERS_GETPUBLICKEY_API = "https://fraud.mch.weixin.qq.com/risk/getpublickey";

    // 分享url
    public static final String SHARE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECTURI&response_type=code&scope=snsapi_userinfo&state=STATE";

    /**
     * 微信服务号 模板消息
     */
    public static final String TEMPLATE_MSG_WX = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=AccessToken";

    /**
     * 微信小程序 模板消息
     */
    public static final String TEMPLATE_MSG_APPLET = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=AccessToken";

    /**
     * 开发环境异步回调URL
     */
    public static final String NOTIFY_URL_TEST = "https://test.mzszxc.com/future-rural/mobileMallOrder/wxAppletNotifyUrl";//商城订单

    /**
     * 生成环境异步回调URL
     */
    public static final String NOTIFY_URL_PROD = "https://dev.mzszxc.com/future-rural/mobileMallOrder/wxAppletNotifyUrl";//商城订单

    public static final String NOTIFY_URL_YA = "https://apiyongancun.yushangdaoxiang.com/future-rural/mobileMallOrder/wxAppletNotifyUrl";//商城订单

    /**
     * 农业厅环境异步回调URL
     */
    public static final String NOTIFY_URL_AGRI = "https://wlxc.zjagri.cn:8086/future-rural/mobileMallOrder/wxAppletNotifyUrl";

    /**
     * 微信服务号模板消息id-订单服务提醒
     */
    public static final String WX_TEMPLATEID_ORDERSERVICE = "q7uhJB5KYYxBlMLl6IfVU4yPCsl4c-jFMsn09BNJV1w";


    /**
     * 获取微信小程序码
     */
    public static final String WX_APPLET_WXACODE = "POST https://api.weixin.qq.com/wxa/getwxacode?access_token=ACCESS_TOKEN";

    /**
     * 获取微信小程序二维码
     */
    public static final String WX_APPLET_WXAQRCODE = "https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode?access_token=ACCESS_TOKEN";

    /********************************************************** 分账 *****************************************************************/
    /**
     * 请求分账
     */
    public static final String ORDERS_SHARING = "https://api.mch.weixin.qq.com/v3/profitsharing/orders";
//    /**
//     * 请求多次分账
//     */
//    public static final String MULTIPLE_DIVISION ="https://api.mch.weixin.qq.com/secapi/pay/multiprofitsharing";

    /**
     * 查询分账结果
     */
    public static final String DISTRIBUTION_RESULT = "https://api.mch.weixin.qq.com/v3/profitsharing/orders/{out_order_no}";
    /**
     * 分账回退
     */
    public static final String DISTRIBUTION_ROLLBACK = "https://api.mch.weixin.qq.com/v3/profitsharing/return-orders";
    /**
     * 查询分账回退结果
     */
    public static final String DISTRIBUTION_ROLLBACK_RESULT = "https://api.mch.weixin.qq.com/v3/profitsharing/return-orders/{out_return_no}";
    /**
     * 解冻剩余资金
     */
    public static final String DISTRIBUTION_ORDERS_UNFREEZE = "https://api.mch.weixin.qq.com/v3/profitsharing/orders/unfreeze";
    /**
     * 查询订单剩余待分金额
     */
    public static final String SHARING_AMOUNT_BALANCE = "https://api.mch.weixin.qq.com/v3/profitsharing/transactions/{transaction_id}/amounts";
    /**
     * 查询最大分账比例A
     */
    public static final String SHARING_AMOUNT_MAX_RADIO = "https://api.mch.weixin.qq.com/v3/profitsharing/merchant-configs/{sub_mchid}";

    /**
     * 添加分账接收方
     */
    public static final String INSERT_DISTRIBUTION_ACCEPTOR = "https://api.mch.weixin.qq.com/v3/profitsharing/receivers/add";
    public static final String SEPARATE_ADD_API = "https://api.mch.weixin.qq.com/pay/profitsharingaddreceiver";
    /**
     * 删除分账接收方
     */
    public static final String DELETE_DISTRIBUTION_ACCEPTOR = "https://api.mch.weixin.qq.com/v3/profitsharing/receivers/delete";
    /**
     * 申请分账账单
     */
    public static final String SHARING_PROFITSHARING_BILLS = "https://api.mch.weixin.qq.com/v3/profitsharing/bills";
    /**
     * 完结分账
     */
    public static final String FINISH_DISTRIBUTION = "https://api.mch.weixin.qq.com/secapi/pay/profitsharingfinish";

    /********************************************************** 小程序发货信息管理服务 *****************************************************************/
    /**
     * https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/order-shipping/order-shipping.html#%E4%B8%80%E3%80%81%E5%8F%91%E8%B4%A7%E4%BF%A1%E6%81%AF%E5%BD%95%E5%85%A5%E6%8E%A5%E5%8F%A3
     */

    /**
     * 发货信息录入接口
     */
    public static final String UPLOAD_SHIPPING_INFO = "https://api.weixin.qq.com/wxa/sec/order/upload_shipping_info?access_token=ACCESS_TOKEN";
    /**
     * 发货信息合单录入接口
     */
    public static final String UPLOAD_COMBINED_SHIPPING_INFO = "https://api.weixin.qq.com/wxa/sec/order/upload_combined_shipping_info?access_token=ACCESS_TOKEN";
    /**
     * 查询订单发货状态
     */
    public static final String GET_ORDER = "https://api.weixin.qq.com/wxa/sec/order/get_order?access_token=ACCESS_TOKEN";
    /**
     * 查询订单列表
     */
    public static final String GET_ORDER_LIST = "https://api.weixin.qq.com/wxa/sec/order/get_order_list?access_token=ACCESS_TOKEN";
    /**
     * 确认收货提醒接口
     */
    public static final String NOTIFY_CONFIRM_RECEIVE = "https://api.weixin.qq.com/wxa/sec/order/notify_confirm_receive?access_token=ACCESS_TOKEN";
    /**
     * 消息跳转路径设置接口
     */
    public static final String SET_MSG_JUMP_PATH = "https://api.weixin.qq.com/wxa/sec/order/set_msg_jump_path?access_token=ACCESS_TOKEN";
    /**
     * 查询小程序是否已开通发货信息管理服务
     */
    public static final String IS_TRADE_MANAGED = "https://api.weixin.qq.com/wxa/sec/order/is_trade_managed?access_token=ACCESS_TOKEN";
    /**
     * 查询小程序是否已完成交易结算管理确认
     */
    public static final String IS_TRADE_MANAGEMENT_CONFIRMATION_COMPLETED = "https://api.weixin.qq.com/wxa/sec/order/is_trade_management_confirmation_completed?access_token=ACCESS_TOKEN";

    /**
     * 枚举值1，使用下单商户号和商户侧单号；
     * 枚举值2，使用微信支付单号。
     */
    public static final  Integer ORDER_NUMBER_TYPE_MCHID = 1;
    public static final  Integer ORDER_NUMBER_TYPE_TRANSACTIONID = 2;

    /**
     * 发货模式，发货模式
     * 枚举值：
     * 1、UNIFIED_DELIVERY（统一发货）
     * 2、SPLIT_DELIVERY（分拆发货） 示例值: UNIFIED_DELIVERY
     */
    public static final  Integer DELIVERY_MODE_UNIFIED_DELIVERY = 1;
    public static final  Integer DELIVERY_MODE_SPLIT_DELIVERY = 2;

    /**
     * 物流模式，发货方式
     * 枚举值：
     * 1、实体物流配送采用快递公司进行实体物流配送形式
     * 2、同城配送
     * 3、虚拟商品，虚拟商品，例如话费充值，点卡等，无实体配送形式
     * 4、用户自提
     */
    public static final  Integer LOGISTICS_TYPE_COMPANY = 1;
    public static final  Integer LOGISTICS_TYPE_CITY = 2;
    public static final  Integer LOGISTICS_TYPE_VIRTUAL = 3;
    public static final  Integer LOGISTICS_TYPE_USER = 4;

    /**
     * 订单状态枚举：(1) 待发货；(2) 已发货；(3) 确认收货；(4) 交易完成；(5) 已退款。
     */
    public static final  Integer ORDER_STATE_DELIVER_WAITING = 1;
    public static final  Integer ORDER_STATE_DELIVER_ALREADY = 2;
    public static final  Integer ORDER_STATE_WAITING_RECEIVE = 3;
    public static final  Integer ORDER_STATE_FINISH = 4;
    public static final  Integer ORDER_STATE_REFUND_REFUNDED = 5;

}
