package com.mz.common.model.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 信息站点表(TabPublicSite)实体类
 *
 * @author makejava
 * @since 2021-04-20 16:02:02
 */
@Setter
@Getter
@ToString
public class TabPublicSite {
    private static final long serialVersionUID = 173206370736024804L;
    //    授权状态1未授权2已授权
    public static int AUTH_STATUS_NO = 1;
    public static int AUTH_STATUS_ALREADY = 2;

    //    审核状态 1 未提交审核 2.审核中 3 撤销审核  4审核通过 5 审核失败 6 全网发布 7 回退版本
    public static int REVIEW_STATUS_WAIT = 1;
    public static int REVIEW_STATUS_CHECKING = 2;
    public static int REVIEW_STATUS_CANCEL = 3;
    public static int REVIEW_STATUS_SUCCESS = 4;
    public static int REVIEW_STATUS_FAIL = 5;
    public static int REVIEW_STATUS_RELEASE = 6;
    public static int REVIEW_STATUS_RETURN = 7;
    /**
     * 主键
     */
    private String id;
    /**
     * 所属租户
     */
    private String tenantId;
    /**
     * 区域代码
     */
    private String areaCode;
    /**
     * 区域名称
     */
    private String areaName;
    /**
     * 站点代码
     */
    private String siteCode;
    /**
     * 站点名称
     */
    private String siteName;
    /**
     * 类型1pc端2app3小程序
     */
    private Integer siteType;
    /**
     * 小程序appID
     */
    private String appId;
    /**
     * 小程序appSecret
     */
    private String appSecret;
    /**
     * 商户号
     */
    private String merchantNo;
    /**
     * 子商户号
     */
    private String subMchid;
    /**
     * 商户密钥
     */
    private String merchantKey;
    /**
     * 微信证书ip
     */
    private String createIp;
    /**
     * 证书的路径
     */
    private String certLocalPath;
    /**
     * p12证书密钥(退款)
     */
    private String certPassword;
    /**
     * 服务号ID
     */
    private String appIdWx;
    /**
     * 服务号密钥
     */
    private String appSecretWx;
    /**
     * 站点图标
     */
    private String siteIcon;
    /**
     * 小程序token(定时器获取小程序使用量数据用到的TOKEN)
     */
    private String accessToken;
    /**
     * 小程序token获取时间(定时器获取小程序使用量数据用到的TOKEN)
     */
    private String accessTokenTime;
    /**
     * 启用状态1启用2停用
     */
    private Integer siteState;
    /**
     * 是否可用1是2否
     */
    private Integer useState;
    /**
     * 语言类型1中文2英文
     */
    private Integer language;
    /**
     * 授权状态1未授权2已授权
     */
    private Integer authStatus;
    /**
     * 审核状态 1 未提交审核 2.审核中 3 撤销审核  4审核通过 5 审核失败 6 全网发布 7 回退版本
     */
    private Integer reviewStatus;
    /**
     * 当前版本号
     */
    private String currentVer;
    /**
     * 站点区域类型1本区域2跨区域
     */
    private Integer areaType;
    /**
     * 站点跨区等级1国内2省内3市内4区内5镇内
     */
    private Integer otherAreaLevel;
    /**
     * 是否作为模板1是2否（作为模板供新建站点拷贝基本信息）
     */
    private Integer templateState;

    /**
     * 支付密码
     */
    private String payPassword;

    /**
     * 盐值
     */
    private String pwdSalt;
    /**
     * 公众号名称
     */
    private String wxAccountName;
    /**
     * 公众号链接
     */
    private String wxAccountLink;
    /**
     * 公众号跳转地址
     */
    private String wxAccountJump;
    /**
     * 产品版本ID
     */
    private Long productId;
    /**
     * 产品版本名称
     */
    private String productName;
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
     * 备注--订阅消息小程序类型：developer为开发版；trial为体验版；formal为正式版
     */
    private String remarks;
    /**
     * 区域查询方式:1-本级 2-本级级以上 (微页面查询)
     */
    private Integer areaSearchType;
    /**
     * 小程序名称 1-区域名称 2-站点名称
     */
    private Integer appletNameShowType;


}