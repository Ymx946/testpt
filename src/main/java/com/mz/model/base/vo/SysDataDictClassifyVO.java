package com.mz.model.base.vo;

import com.mz.common.model.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统数据字典分类(SysDataDictClassify)实体类
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class SysDataDictClassifyVO extends BaseDTO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 类型代码
     */
    private String dictTypeCode;
    /**
     * 类型名称
     */
    private String dictTypeName;
    /**
     * 逻辑删除 1正常 -1删除
     */
    private Integer delState;
    /**
     * 状态 1运行 -1维护
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
