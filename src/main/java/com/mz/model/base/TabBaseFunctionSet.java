package com.mz.model.base;


import com.mz.common.annotation.FieldMeta;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 功能设置基础表(TabBaseFunctionSet)实体类
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class TabBaseFunctionSet implements Serializable {
    private static final long serialVersionUID = -74254981752062253L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 功能代码
     */
    @FieldMeta(name = "功能代码")
    private String functionCode;
    /**
     * 功能名称
     */
    @FieldMeta(name = "功能名称")
    private String functionName;
    /**
     * 说明
     */
    @FieldMeta(name = "说明")
    private String description;
    /**
     * 授权类型 1区域2租户3主体
     */
    @FieldMeta(name = "授权类型", readConverterExp = "1=区域,2=租户,3=主体")
    private Integer accreditType;
    /**
     * 逻辑删除
     */
    private Integer delState;
    /**
     * 是否开启审核(1正常-1禁用))
     */
    @FieldMeta(name = "是否开启审核", readConverterExp = "1=开启,-1=关闭")
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
