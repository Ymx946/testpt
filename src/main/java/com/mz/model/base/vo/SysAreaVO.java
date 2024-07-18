package com.mz.model.base.vo;

import com.mz.model.base.SysArea;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 区域表(SysArea)实体类
 *
 * @author makejava
 * @since 2021-03-17 10:58:48
 */
@Setter
@Getter
@ToString
public class SysAreaVO extends SysArea {
    /**
     * 选中状态 1选中 2未选中
     */
    private Integer selectState;
    /**
     * 下级列表
     */
    private List<SysAreaVO> sonList;

}