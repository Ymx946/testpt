package com.mz.common.util.zjzw;

import java.util.concurrent.ConcurrentHashMap;

public class Constants {

    public static final String ZJZW_SECRET = "98df5f5ee375e3ba570ca52b827ce1ac";

    /**
     * domain_url
     */
    public final static String DOMAIN_URL = "https://wlxc.zjagri.cn";

    /**
     * 浙农码 clientId 测试环境
     */
    public final static String ZNM_CLIENTID_TEST = "zheliweilaixiangcunzaixian";
    /**
     * 浙农码 clientId 正式环境
     */
    public final static String ZNM_CLIENTID_PROD = "zheliweilaixiangcunzaixian";
    /**
     * 浙农码 密钥 测试环境
     */
    public final static String ZNM_CLIENTSECRET_TEST = "D88FF02A8AE2438C810B104933AB5CF9";
    /**
     * 浙农码 密钥 正式环境
     */
    public final static String ZNM_CLIENTSECRET_PROD = "1E03DEE814EC486DA53FA9121E21F981";
    /**
     * 浙农码 URL 测试环境
     */
    public final static String ZNM_URL_TEST = "https://znm.kf315.net/api";
    /**
     * 浙农码 URL 正式环境
     */
    public final static String ZNM_URL_PROD = "https://znm.zjagri.cn/api";
//    public final static String ZNM_URL_PROD = "https://znm.kf315.net/api";
    /**
     * h5 测试服务地址
     */
    public final static String RESOLVE_URL_TEST = "https://test.mzszxc.com:8099/wlxczxH5/";
    /**
     * h5 正式服务地址
     */
    public final static String RESOLVE_URL_PROD = "https://dev.mzszxc.com/wlxczxH5/";
    /**
     * h5 正式服务地址(永安)
     */
    public final static String RESOLVE_URL_YA = "https://h5yongancun.yushangdaoxiang.com/wlxczxH5/";
    /**
     * h5 正式服务地址 - 农业厅
     */
    public final static String RESOLVE_URL_AGRI = "https://wlxc.zjagri.cn/wlxczxH5/";
    /**
     * 浙农码 token
     */
    public final static String ZNM_TOKEN_URL = "/hydra-znm-api/api/v1/oauth/token";

    /**
     * 浙农码 申请赋码
     */
    public final static String ZNM_APPLY_URL = "/hydra-znm-api/api/v3/znm/code-apply";

    public final static String ZNM_USEFOR_CODE = "040100";
    public final static Integer ZNM_EXTENDCODE_TYPE = 4;
    /**
     * 获取二维码图片及设置预警赋色
     * 用于批量获取浙农码对应的二维码及设置二维码预警赋色
     */
    public final static String ZNM_QRCODELIST_URL = "/hydra-znm-api/api/v1/znm/getZnmQrCodeList";
    /**
     * 更新赋码信息
     */
    public final static String ZNM_UPDATE_URL = "/hydra-znm-api/api/v3/update/znm-code-params";
    /**
     * 通过 znm-uid 获取码信息
     */
    public final static String ZNM_CODE_INFO_URL = "/hydra-znm-api/api/v1/znm/code/info?znm-uid=ZNMUID";
    /**
     * 获取浙里办当前登录用户信息
     */
    public final static String ZNM_USER_INFO_URL = "/hydra-znm-api/api/v1/znm/zww-user/login-info?Authorization=AUTHORIZATION&znm-token=ZNMTOKEN";

    public final static String SERVICE_CODE = "5470e8db64db43148bfd4b6ad006037f";
    public final static String SERVICE_CODE_PWD = "db36205437514773b644fdff6d9e6109";
    /**
     * 多少秒内算重复请求
     */
    public final static int REPEAT_REQUEST_TIME = 1500;
    /**
     * Base URL
     **/
    public final static String BASE_URL = "https://appapi.zjzwfw.gov.cn/sso/servlet/simpleauth?servicecode=" + SERVICE_CODE;
    /**
     * 令牌URL
     **/
    public final static String TICKET_VALIDATION_URL = BASE_URL + "&method=ticketValidation";
    /**
     * 用户URL
     **/
    public final static String USER_INFO_URL = BASE_URL + "&method=getUserInfo";

    /**
     * 跳转URL-测试环境
     **/
    public final static String JUMP_URL_TEST = "https://test.mzszxc.com/mzplatform/?token=";
    /**
     * 跳转URL-生产环境
     **/
    public final static String JUMP_URL_PROD = "https://dev.mzszxc.com/mzplatform/?token=";

    /**
     * 跳转URL-农业厅环境
     **/
    public final static String JUMP_URL_AGRI = "https://wlxc.zjagri.cn/mzplatform/?token=";

    /**
     * 浙里办微信domain开发环境 互联网地址
     */
    public final static String ZLB_APPLET_DOMAIN_DEV = "https://ibcdsg.zj.gov.cn:8443/";
    /**
     * 浙里办微信domain开发环境 政务外网地址
     */
//    public final static String ZLB_APPLET_DOMAIN_AGRI = "https://bcdsg.zj.gov.cn:8443/";
    public final static String ZLB_APPLET_DOMAIN_AGRI = "https://ibcdsg.zj.gov.cn:8443/";

    /**
     * IRS请求携带的请求头
     */
    public final static String X_BG_HMAC_ACCESS_KEY = "X-BG-HMAC-ACCESS-KEY";
    public final static String X_BG_HMAC_SIGNATURE = "X-BG-HMAC-SIGNATURE";
    public final static String X_BG_HMAC_ALGORITHM = "X-BG-HMAC-ALGORITHM";
    public final static String X_BG_DATE_TIME = "X-BG-DATE-TIME";
    /**
     * 单点登录 ticketId换token的地址
     */
    public final static String ZLB_APPLET_ACCESS_TOKEN_URL = "restapi/prod/IC33000020220329000007/uc/sso/access_token";
    /**
     * 单点登录 token获取用户信息地址
     */
    public final static String ZLB_APPLET_USER_INFO_URL = "restapi/prod/IC33000020220329000008/uc/sso/getUserInfo";
    /**
     * IRS签名算法
     */
    public final static String DEFAULT_HMAC_SIGNATURE = "hmac-sha256";

    /**
     * 应用ID
     */
    public final static String APP_ID = "2002202947";

    /**
     * IRS 申请组件生成的AK
     */
    public final static String IRS_AK = "5470e8db64db43148bfd4b6ad006037f";
    /**
     * IRS 申请组件生成的SK
     */
    public final static String IRS_SK = "db36205437514773b644fdff6d9e6109";

    /**
     * 移动BaseUrl
     */
    public final static String YD_BASE_URL = "https://wlxc.zjagri.cn/hailiDigitalVillage-api/";
    /**
     * cityDo 用户信息
     */
    public final static String AGRI_CITYDO_USER = "nyt/sso/cityDo/user";
    /**
     * 网格员数据是从助老关爱后台上来
     */
    public final static String AGRI_YD_WGY_USER = "digitalVillage/get/synzlga";

    public static ConcurrentHashMap<String, Long> userMap = new ConcurrentHashMap<>();

}
