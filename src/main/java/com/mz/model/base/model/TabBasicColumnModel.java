package com.mz.model.base.model;

import com.mz.model.base.TabBasicColumn;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 基础栏目表(TabBasicColumn)实体类
 */
@Setter
@Getter
@ToString
public class TabBasicColumnModel extends TabBasicColumn {
    /**
     * 选择状态：1已选2未选
     */
    private Integer selectState;

}
