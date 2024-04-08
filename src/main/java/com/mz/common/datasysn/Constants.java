package com.mz.common.datasysn;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    /**
     * 访问公钥测试
     */
    public final static String CITYDO_ACCESSKEY_TEST = "WLXC_TEST";
    public final static String CITYDO_ACCESSKEY_AGRI = "WLXC_TEST";
    /**
     * 访问秘钥 测试
     */
    public final static String CITYDO_SECRETKEY_TEST = "u3ou6yww9e4epxd7u315sngn";
    public final static String CITYDO_SECRETKEY_AGRI = "u3ou6yww9e4epxd7u315sngn";
    /**
     * 访问票据（url上 c_ticket 参数获取）,使⽤之后失效
     */
    public final static String CITYDO_TICKET_TEST = "TEST";
    public final static String CITYDO_TICKET_PROD = "TEST";
    public final static String CITYDO_URL_TEST = "http://125.124.53.202:8900";
    public final static String CITYDO_URL_AGRI = "http://125.124.53.202:8900";
    /**
     * 获取访问令牌（accessToken）
     */
    public final static String CITYDO_ISSUEACCESSTOKEN_URL = "/api/user/open/issueAccessToken";
    /**
     * 获取web端当前登录⽤户信息
     */
    public final static String CITYDO_WEB_USERINFO_URL = "/api/user/open/web/user/info";
    /**
     * 获取移动端当前登录⽤户信息
     */
    public final static String CITYDO_APP_USERINFO_URL = "/api/user/open/app/user/info";

    // 用户名
    public final static String USERNAME = "dataReport";
    public final static String APPKEY = "ydsjcx";
    // 密码
    public final static String USERPASSWORD = "0b374f6bb6dfa28800dc0078f5c4afab";
    public final static String APPSECRET = "fbf7b7d35c406af1f44e1e2dc87a7168";

    public final static String APPKEYNEW = "8dc9551b5549";
    public final static String APPSECRETNEW = "62295f5bcec148e681fcf14a270a25b6";

    //域名
    public final static String DOMAIN_DEV_URL = "http://125.124.234.105:8521";
    public final static String DOMAIN_PROD_URL = "https://wlxc.zjagri.cn/gateway/open/index";

    //   public final static String DOMAIN_AGRI_URL = "https://wlxc.zjagri.cn/gateway/open/index/base";
    public final static String DOMAIN_AGRI_URL = "http://10.146.77.222:4000/open/index";
    // 登录
    public final static String TOKEN_URL = "/base/login";

    // 乡村风貌
    public final static String COUNTRY_STYLE = "COUNTRY_STYLE";
    public final static String COUNTRY_STYLE_URL = "/base/open/data/query/07a6670f8091";

    // 党建-班子组成
    public final static String PARTY_ORGANIZATION = "PARTY_ORGANIZATION";
    // 党建-村委会
    public final static String PARTY_COMMITTEE = "PARTY_COMMITTEE";
    // 党建-村监会
    public final static String PARTY_SUPERVISORY = "PARTY_SUPERVISORY";
    // 党建-合作社
    public final static String PARTY_COOPERATIVE = "PARTY_COOPERATIVE";
    public final static String PARTY_ORGANIZATION_URL = "/base/open/data/query/cc9f2eb22d57";

    // 组织信息
//    public final static String PARTY_ORGANIZATIONAL_STRUCTURE_URL = "https://wlxc.zjagri.cn/gateway/open/index/executor/api/yd/organizationalStructure";
    public final static String PARTY_ORGANIZATIONAL_STRUCTURE_URL = "/executor/api/yd/organizationalStructure";

    // 党建-先锋示范-微心愿
    public final static String PARTY_LITTLE_WISH = "PARTY_LITTLE_WISH";
    public final static String PARTY_LITTLE_WISH_URL = "/base/open/data/query/8439e9e222b6";

    // 党建-先锋示范-党员积分
    public final static String PARTY_MEMBER_POINTS = "PARTY_MEMBER_POINTS";
    public final static String PARTY_MEMBER_POINTS_URL = "/base/open/data/query/f064a5c93411";

    // 党建-先锋示范-党性分析
    public final static String PARTY_ANALYSIS = "PARTY_ANALYSIS";
    public final static String PARTY_ANALYSIS_URL = "/open/data/query/ddf5f1b7a19a";

    // 村域导览- 快递站点
    public final static String COURIER_SITE = "COURIER_SITE";
    public final static String COURIER_SITE_URL = "/base/open/data/query/ea57a1bff3d1";

    //村域导览-书屋详情
    public final static String FARMHOUSE_BOOKSTORE = "FARMHOUSE_BOOKSTORE";
    public final static String FARMHOUSE_BOOKSTORE_URL = "/base/open/data/query/70c06538437d";

    //村域导览-公共厕所
    public final static String PUBLIC_TOILET = "PUBLIC_TOILET";
    public final static String PUBLIC_TOILET_URL = "/base/open/data/query/e79dee341318";

    // 村域导览-乡村民宿
    public final static String COUNTRY_HOUSE = "COUNTRY_HOUSE";
    public final static String COUNTRY_HOUSE_URL = "/base/open/data/query/57bfd990136e";

    // 村域导览-水库检测
    public final static String RESERVOIR_DETECTION = "RESERVOIR_DETECTION";
    public final static String RESERVOIR_DETECTION_URL = "/base/open/data/query/916f1d40c3fe";

    // 村庄简介
    public final static String VILLAGE_INTRODUCTION = "VILLAGE_INTRODUCTION";
    public final static String VILLAGE_INTRODUCTION_URL = "/base/open/data/query/a6dbff90e392";

    // 村庄荣誉
    public final static String VILLAGE_HONOR = "VILLAGE_HONOR";
    public final static String VILLAGE_HONOR_URL = "/base/open/data/query/a20906690d83";

    // 文明家庭
    public final static String CIVILIZED_FAMILY = "CIVILIZED_FAMILY";

    // 最美家庭
    public final static String BEAUTIFUL_FAMILY = "BEAUTIFUL_FAMILY";
    public final static String CIVILIZED_FAMILY_URL = "/base/open/data/query/f64eda7de8ba";

    // 美丽庭院
    public final static String BEAUTIFUL_GARDEN = "BEAUTIFUL_GARDEN";
    public final static String BEAUTIFUL_GARDEN_URL = "/base/open/data/query/6730750af67e";

    // 智慧出行-公交统计
    public final static String BUS_STATISTICS = "BUS_STATISTICS";
    public final static String BUS_STATISTICS_URL = "/base/open/data/query/e41f1542b9a2";

    // 智慧出行-公交信息
    public final static String BUS_INFO = "BUS_INFO";
    public final static String BUS_INFO_URL = "/base/open/data/query/c773561f4732";

    // 违停抓拍
    public final static String STOP_TAKING_PICTURES = "STOP_TAKING_PICTURES";
    public final static String STOP_TAKING_PICTURES_URL = "/base/open/data/query/a508e5788f96";

    // 乡村优品
    public final static String COUNTRY_PREMIUM = "COUNTRY_PREMIUM";
    public final static String COUNTRY_PREMIUM_URL = "/base/open/data/query/dc5d327376e1";

    // 历史文保
    public final static String HISTORICAL_PRESERVATION = "HISTORICAL_PRESERVATION";
    public final static String HISTORICAL_PRESERVATION_URL = "/base/open/data/query/48e543c6fc91";

    // 气象检测
    public final static String WEATHER_MONITORING = "WEATHER_MONITORING";
    public final static String WEATHER_MONITORING_URL = "/base/open/data/query/49afc9c7006c";

    public final static Map<String, String> XCaDeptIdMap = new HashMap<String, String>() {{
        put("330522202208", "44f071dbbca4"); // 1.龙溪村
        put("330522102206", "e23b4f28ffcc"); // 2.老虎洞村
        put("330521115211", "ea516c822cb5"); // 3.仙潭村
        put("330521101209", "827ed034e951"); // 4.幸福村
        put("330521102218", "c964d83dddd0"); // 5.宋市村
        put("330604202203", "45a908294341"); // 6.丁宅片区
        put("330604111237", "299b6865c024"); // 7.晋生村
        put("330483103205", "7e917daca96f"); // 8.墅丰村
        put("330825109225", "9f31599455c0"); // 9.溪口村
        put("330825102253", "dd1f9ebd3c61"); // 10.团石村
        put("330825106200", "ff59bb0aad30"); // 11.浦山村
        put("330521115220", "59ea13d012ca"); // 12.五四村
    }};

}
