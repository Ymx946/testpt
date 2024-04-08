package com.mz.common;

import org.springframework.stereotype.Component;

/**
 * redis 常量工具类
 */
@Component("ConstantsCacheUtil")
public class ConstantsCacheUtil {
    /**
     * redis 一年的秒数
     */
    public static final long REDIS_YEAR_SECOND = 31_536_000;
    /**
     * redis 一个月的秒数
     */
    public static final long REDIS_MONTH_SECOND = 2_592_000;
    /**
     * redis 一天的毫秒数
     */
    public static final long REDIS_DAY_MILSECOND = 86_400_000;
    /**
     * redis 一天的秒数
     */
    public static final long REDIS_DAY_SECOND = 86400;
    /**
     * redis 一小时的秒数
     */
    public static final long REDIS_HOUR_SECOND = 3600;
    /**
     * redis 10分钟的秒数
     */
    public static final long REDIS_MINUTE_TEN_SECOND = 600;
    /**
     * redis 一分钟的秒数
     */
    public static final long REDIS_MINUTE_SECOND = 60;

    /**
     * redis 一秒数
     */
    public static final long REDIS_SECOND = 1;


    /**
     * redis 200秒数
     */
    public static final long REDIS_MILLISECONDS = 200;


    /**
     * 定义图形大小
     */
    public static final int VERIFY_CODE_WIDTH = 130;
    /**
     * 定义图形大小
     */
    public static final int VERIFY_CODE_HEIGHT = 50;

    public static final String CACHE_KEY_OF_VERIFY_CODE = "CACHE_IMG_VERIFY_CODE";

    /**
     * 验证码缓存1分钟
     */
    public static final long CACHE_IMG_VERIFY_CODE_MINUTES = 1;

    /**
     * 用户锁定30分钟
     */
    public static final long LOGIN_USER_LOCK_MINUTES = 30;
    /**
     * redis 分割字符，默认[:]，使用:可用于分组查看 ；redis key默认使用冒号分割，好处在于可以以文件夹的形式分组查看
     */
    public static final String REDIS_DEFAULT_DELIMITER = ":";
    /**
     * 登录token
     */
    public static final String LOGIN_TOKEN = "LOGIN:TOKEN:token";
    /**
     * 登录用户
     */
    public static final String LOGIN_USER_INFO = "LOGIN:USER:baseUser";

    /**
     * 用户登录次数计数
     */
    public static final String LOGIN_USER_FAILCOUNT = "LOGIN:USER:baseUser:failCount";
    /**
     * 用户登录是否被锁定
     */
    public static final String LOGIN_USER_LOCK = "LOGIN:USER:baseUser:lock";
    /**
     * 用户登录失败次数
     */
    public static final int LOGIN_FAIL_NUM = 5;

    public static final int PWD_EXPIRE_DAYS = 90;

    public static final String MZSZ_ACCESS_TOKEN = "MZSZ:ACCESS:TOKEN";
    public static final String MZSZ_REFRESH_TOKEN = "MZSZ:REFRESH:TOKEN";
    public static final String OPENUSER_ACCESS_TOKEN = "OPENUSER:REFRESH:TOKEN";
    public static final String OPENUSER_REFRESH_TOKEN = "OPENUSER:REFRESH:TOKEN";
    /**
     * APP登录token
     */
    public static final String LOGIN_TOKEN_APP = "LOGIN:TOKEN:appToken";
    /**
     * APP登录用户
     */
    public static final String LOGIN_USER_INFO_APP = "LOGIN:USER:appBaseUser";
    /**
     * 萤石token
     */
    public static final String YS_ACCESS_TOKEN = "YS:ACCESS:ysAccessToken";
    /**
     * 小程序码
     */
    public static final String WXACODE = "WX:WXACODE:WxAcode";

    /**
     * 微信访问的token
     */
    public static final String WXACCESSTOKEN = "WX:ACCESSTOKEN:";
    /**
     * 微信校验文字、图片的accessToken
     */
    public static final String WXCHECKACCESSTOKEN = "WX:CHECK:ACCESSTOKEN:";
    /**
     * 微信检测图片
     */
    public static final String WXCHECKIMG = "WX:CHECK:IMG:";
    public static final String DEVICEACCESSTOKEN = "DEVICE:ACCESSTOKEN:";

    /**
     * 百度智能云token
     */
    public static final String BAIDU_TOKEN = "BAIDU:baiduToken";

    public static final String WECHAT_MEMBER_TOKEN = "WECHAT:member";

    public static final String BASEUSER_CODE_CACHE = "BASEUSER:code";

    public static final String MEMBER_CODE_CACHE = "MEMBER:code";

    public static final String MEMBER_COMMON_CODE_CACHE = "MEMBER:commonCode";

    /**
     * 删除所有匹配符
     */
    public static final String DELETE_ALL = "*";

    /**
     * 提交重复的token
     */
    public static final String DUPLICATE_TOKEN_KEY = "duplicate_token_key";

    /**
     * 防止短时间重复访问
     */
    public static final String ACCESS_KEY = "SZSL:ACCESS:KEY";

    /**
     * redis DB的索引编号0
     */
    public static final int REDIS_DB_FIRST_INDEX = 0;

    /**
     * redis 最后DB的索引编号
     */
    public static final int REDIS_DB_LAST_INDEX = 15;

    /**
     * 区域表 列表缓存
     */
    public static final String SYSAREA_CACHE_ALL_LIST = "SYSAREA:CACHE:ALL:LIST";

    /**
     * 密码是否过期 Y-已过期 N-未过期
     */
    public static final String pwd_EXPIRED_YES = "Y";
    public static final String pwd_EXPIRED_NO = "N";


}
