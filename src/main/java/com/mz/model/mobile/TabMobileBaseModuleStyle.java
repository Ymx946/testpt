package com.mz.model.mobile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 移动组件样式表(TabMobileBaseModuleStyle)实体类
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class TabMobileBaseModuleStyle {
    private static final long serialVersionUID = 335388394300283500L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 模块ID
     */
    private Long moduleId;
    /**
     * 样式名称
     */
    private String styleName;
    /**
     * 逻辑删除
     */
    private Integer delState;
    /**
     * 状态 1已启用 -1禁用
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
