package com.mz.common.model.seal.vo;

import com.mz.common.model.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 电子印章信息表(TabElectronicSeal)实体类
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class TabElectronicSealVO extends BaseDTO {

    private String startDate;
    private String endDate;
    /**
     * 印章名称/单位名称/统一信用代码
     **/
    private String qparam1;
    /**
     * 负责人/联系方式/备注
     **/
    private String qparam2;
    /**
     * 登录id
     */
    private String loginID;
    /**
     * 主键
     */
    private Long id;
    /**
     * 所属租户
     */
    private Long tenantId;
    /**
     * 区域代码
     */
    private String areaCode;
    /**
     * 区域名称
     */
    private String areaName;
    /**
     * 单位名称
     */
    private String unitName;
    /**
     * 部门
     */
    private String department;
    /**
     * 印章负责人
     */
    private String stampPrincipal;
    /**
     * 负责人联系方式
     */
    private String principalPhone;
    /**
     * 印章名称
     */
    private String stampName;
    /**
     * 印章类型(1-圆章2-扁章)
     */
    private Integer stampType;
    /**
     * 企业名称
     */
    private String enterpriseName;
    /**
     * 横向文
     */
    private String horizontalText;
    /**
     * 下弦文
     */
    private String lowerChord;
    /**
     * 添加人
     */
    private String addPeople;
    /**
     * 添加人联系方式
     */
    private String addPeoplePhone;
    /**
     * 密码
     */
    private String password;
    /**
     * 企业全称
     */
    private String enterpriseFullName;
    /**
     * 信用代码类型1-组织机构代码 2-社会统一信用代码
     */
    private Integer creditCodeType;
    /**
     * 信用代码
     */
    private String creditCode;
    /**
     * 法人姓名
     */
    private String legalPersonName;
    /**
     * 法人身份证
     */
    private String legalPersonIdNo;
    /**
     * P12证书路径
     */
    private String keyStorePath;
    /**
     * P12证书密码
     */
    private String keyStorePass;
    /**
     * 章路径
     */
    private String sealUrl;
    /**
     * 逻辑删除
     */
    private Integer delState;
    /**
     * 认证状态 1已认证-1未认证
     */
    private Integer state;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 创建人
     */
    private String createUser;
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
