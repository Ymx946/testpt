package com.mz.common.model.seal.vo;



import com.mz.common.model.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *使用人信息表(TabActivitiUserInformation)实体类
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class TabActivitiUserInformationVO extends BaseDTO {
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
    * 印章使用人姓名
    */
    private String userName;
    /**
    * 印章使用人联系方式
    */
    private String userPhone;
    /**
    * 印章使用人单位
    */
    private String userUnit;
    /**
    * 电子印章信息表ID
    */
    private Long electronicSealId;
    /**
    * 印章使用人部门
    */
    private String userDept;
    /**
    * 逻辑删除
    */
    private Integer delState;
    /**
    * 启用状态1正常-1禁用
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
