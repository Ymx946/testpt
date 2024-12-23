package com.mz.model.base;

import com.mz.common.annotation.FieldMeta;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 系统数据字典分类(SysDataDictClassify)实体类
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class SysDataDictClassify implements Serializable {
    private static final long serialVersionUID = 124708190704828571L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 类型代码
     */
    @FieldMeta(name = "类型代码")
    private String dictTypeCode;
    /**
     * 类型名称
     */
    @FieldMeta(name = "类型名称")
    private String dictTypeName;
    /**
     * 逻辑删除 1正常 -1删除
     */
    private Integer delState;
    /**
     * 状态 1启用-1停用
     */
    @FieldMeta(name = "状态", readConverterExp = "1=启用,-1=停用")
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
    @FieldMeta(name = "备注")
    private String remarks;

}
