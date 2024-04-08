package com.mz.common.model.seal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.io.Serializable;

/**
 * 用章模板(TabSealTemplate)实体类
 *
 * @author yangh
 * @since 2023-04-06 15:43:21
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class TabSealTemplate implements Serializable {
    private static final long serialVersionUID = 650831117293947782L;
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

