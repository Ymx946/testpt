package com.mz.common.model.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 人员表(TabPbPersonnel)实体类
 *
 * @author makejava
 * @since 2021-05-27 11:14:08
 */
@Setter
@Getter
@ToString
public class TabPbPersonnel {
    private static final long serialVersionUID = 746521997714357509L;
    /**
    * 主键
    */
    private String id;
    /**
    * 所属租户
    */
    private String tenantId;
    /**
    * 登录用户关联
    */
    private String userId;
    /**
     * 网格ID
     */
    private Long gridId;
    /**
     * 网格名称
     */
    private String gridName;
    /**
     * 户ID
     */
    private String familyId;
    /**
     * 户主姓名
     */
    private String holderName;
    /**
     * 与户主关系
     */
    private String holderRelation;
    /**
    * 区域代码
    */
    private String areaCode;
    /**
    * 省
    */
    private String province;
    /**
    * 市
    */
    private String city;
    /**
    * 区
    */
    private String county;
    /**
    * 镇
    */
    private String town;
    /**
    * 村
    */
    private String village;
    /**
    * 地址
    */
    private String address;
    /**
    * 姓名
    */
    private String realName;
    /**
    * 手机号
    */
    private String phoneNo;
    /**
    * 性别 1男2女
    */
    private Integer sex;
    /**
    * 民族代码（数据字典）
    */
    private Integer nation;
    /**
    * 民族名称
    */
    private String nationName;
    /**
    * 学历代码（数据字典）
    */
    private Integer education;
    /**
    * 学历名称
    */
    private String educationName;
    /**
    * 生日
    */
    private String birthDate;
    /**
    * 农历生日月份
    */
    private Integer birthLunarMouth;
    /**
    * 农历生日
    */
    private String birthLunar;
    /**
    * 生日备注
    */
    private String birthLunarRemark;
    /**
    * 参加工作时间
    */
    private String workTime;
    /**
    * 工作单位及职务
    */
    private String workUnit;
    /**
    * 政治面貌代码（数据字典）
    */
    private Integer political;
    /**
    * 政治面貌名称
    */
    private String politicalName;
    /**
    * 入党时间
    */
    private String partyDate;
    /**
    * 身份证号码
    */
    private String idNo;
    /**
    * 住址坐标
    */
    private String coordinate;
    /**
     * 标星置顶1置顶2取消置顶
     */
    private Integer signTop;
    /**
    * 状态1正常-1注销
    */
    private Integer state;
    /**
    * 注销原因
    */
    private String logoutReason;
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
    * 备注
    */
    private String remarks;
    /**
    * 是否有智能手机1是2否
    */
    private Integer smartphone;

}