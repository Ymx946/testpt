package com.mz.model.base.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 系统数据字典(SysDataDict)实体类
 *
 * @author makejava
 * @since 2021-03-17 10:58:51
 */
@Setter
@Getter
@ToString
public class SysDataDictVO {
    /**
     * 主键
     */
    private String id;
    /**
     * 字典代码
     */
    private String dictCode;
    /**
     * 字典名称
     */
    private String dictName;
    /**
     * 下级分类
     */
    private List<SysDataDictVO> sysDataDictVOList;

}