package com.mz.model.base;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 基础栏目表(TabBasicColumn)实体类
 */
@Setter
@Getter
@ToString
public class TabBasicColumn {
    private static final long serialVersionUID = -76673538037850842L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 上级栏目代码
     */
    private String paraColumnCode;
    /**
     * 上级栏目名称
     */
    private String paraColumnName;
    /**
     * 栏目代码
     */
    private String columnCode;
    /**
     * 栏目名称
     */
    private String columnName;
    /**
     * 栏目类型 （数据字典）
     */
    private Integer columnType;
    /**
     * 栏目类型名称 （数据字典）
     */
    private String columnTypeName;
    /**
     * 页面类型1单页2列表
     */
    private Integer pageType;
    /**
     * 排序
     */
    private Integer columnSort;
    /**
     * 栏目级别
     */
    private Integer columnLevel;
    /**
     * 是否末级1是2否
     */
    private Integer lastStage;
    /**
     * 是否显示
     */
    private Integer showState;
    /**
     * 逻辑删除
     */
    private Integer delState;
    /**
     * 状态 1已启用-1禁用
     */
    private Integer state;
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

}
