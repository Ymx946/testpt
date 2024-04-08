package com.mz.common.model.seal.vo;

import com.mz.common.model.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class TabSealTemplateVO extends BaseDTO {
    /**
     * 登录id
     */
    private String loginID;
    private String startDate;
    private String endDate;

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
     * 模板名称
     */
    private String templateName;
    /**
     * 用户ID
     */
    private Long sealId;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 自定义表单id
     */
    private Long formDefineId;
    /**
     * 内容
     */
    private String formContent;
    /**
     * 逻辑删除
     */
    private Integer delState;
    /**
     * 状态 -1:无效 1:有效
     */
    private Integer state;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private String createUser;
    /**
     * 修改时间
     */
    private Date modifyTime;
    /**
     * 修改人
     */
    private String modifyUser;
}
