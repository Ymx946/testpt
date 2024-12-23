package com.mz.model.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 单位信息表(BaseUnitInformation)实体类
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class BaseUnitInformation {
    private static final long serialVersionUID = 897575279428771546L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 上级单位ID
     */
    private Long pid;
    /**
     * 上级单位名称
     */
    private String higherLevelUnit;
    /**
     * 单位名称
     */
    private String unitName;
    /**
     * 单位层级
     */
    private Integer level;
    /**
     * 单位logo
     */
    private String unitLogo;
    /**
     * 逻辑删除 1正常-1删除
     */
    private Integer delState;
    /**
     * 状态 1启用 -1停用
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
