package com.mz.model.base.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserLoginVO extends SysFullAreaVO {

    /**
     * 密码是否过期 N未过期 Y-已过期
     */
    private String pwdExpired = "N";
    /**
     * 密码过期提示
     */
    private String pwdExpiredContent;
    /**
     * 登录ID（放入请求头验证登录）
     */
    private String loginID;
    /**
     * 所属租户
     */
    private String tenantId;
    /**
     * 管理区域
     */
    private String manageArea;
    /**
     * 管理区域名称
     */
    private String manageAreaName;
    /**
     * 租户名称
     */
    private String tenantName;
    /**
     * 操作手册地址
     */
    private String userGuideUrl;
    /**
     * 一体化平台名称
     */
    private String platformName;
    /**
     * 一体化顶部图片
     */
    private String platformPic;
    /**
     * 区域代码
     */
    private String areaCode;
    /**
     * 区域名称
     */
    private String areaName;
    /**
     * 登录用户名
     */
    private String loginName;
    /**
     * 姓名
     */
    private String realName;
    /**
     * 用户等级1-超级管理员 2-租户管理员 3-主体管理员 4-干部用户 5-普通用户 6-网格员
     */
    private Integer userLevel;
    /**
     * 用户类型(1-监管端2-农企端)
     */
    private Integer userType;
    /**
     * 登录状态
     */
    private Integer loginState;
    /**
     * 登录结果: 1登录成功  2用户不存在  3密码错误  4登录异常
     */
    private Integer loginResult;
    /**
     * 头像地址
     */
    private String headPortrait;
    /**
     * 登录返回token
     */
    private String token;
    /**
     * 所属主体ID
     */
    private String mainBodyId;
    /**
     * 所属主体代码
     */
    private String mainBodyCode;
    /**
     * 所属主体名称
     */
    private String mainBodyName;
    /**
     * 主体类型1政府机关 2-村主体  3企业  4个体工商户 5自然人6交易市场7场所(在线商城)8文明实践站9志愿者队伍10联村党委
     */
    private Integer mainBodyType;
    /**
     * 所属机构ID
     */
    private String instId;
    /**
     * 所属机构代码
     */
    private String instCode;
    /**
     * 所属机构名称
     */
    private String instName;
    /**
     * 网点数组字符串
     */
    private String gridIds;
    /**
     * 评分类型数组字符串
     */
    private String groupTypes;
    /**
     * 版本号
     */
    private String versionNumber;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 是否开启水印 1是2否
     */
    private Integer isOpenWatermark;
}
