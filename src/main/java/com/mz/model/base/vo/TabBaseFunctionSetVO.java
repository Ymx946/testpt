package com.mz.model.base.vo;


import com.mz.common.model.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *功能设置基础表(TabBaseFunctionSet)实体类
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class TabBaseFunctionSetVO extends BaseDTO {
    /**
     * 登录人的id
     */
    private String loginID;
    /**
    * 主键
    */
    private Long id;
    /**
    * 功能代码
    */
    private String functionCode;
    /**
    * 功能名称
    */
    private String functionName;
    /**
    * 说明
    */
    private String description;
    /**
     * 授权类型 1区域2租户3主体
     */
    private Integer accreditType;
    /**
    * 逻辑删除
    */
    private Integer delState;
    /**
    * 是否开启审核(1正常-1禁用))
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
