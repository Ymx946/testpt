package com.mz.model.base.vo;

import com.mz.model.base.SysDeft;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SysDeftSelectVO extends SysDeft {
    /**
     * 选中状态 1选中2未选中
     */
    private String selectState;
    /**
     * 开通状态 1未开通2已开通
     */
    private Integer openState;
    /**
     * 是否被其他分类选择1是2否
     */
    private Integer otherSelect;
}
