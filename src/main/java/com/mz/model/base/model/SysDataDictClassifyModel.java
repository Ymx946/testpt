package com.mz.model.base.model;

import com.mz.model.base.SysDataDictClassify;
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
public class SysDataDictClassifyModel extends SysDataDictClassify {
    /**
     * 分类下字典数量
     */
    private Integer dataDictNum;

}
