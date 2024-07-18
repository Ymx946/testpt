package com.mz.model.base.vo;

import com.mz.model.base.SysDataDict;
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
public class SysDataDictSubVO extends SysDataDict {

    /**
     * 小类列表
     */
    private List<SysDataDict> subList;

}