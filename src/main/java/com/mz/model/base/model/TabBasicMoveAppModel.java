package com.mz.model.base.model;

import com.mz.model.base.TabBasicMoveApp;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *基础移动应用表(TabBasicMoveApp)
 */
@Setter
@Getter
@ToString
public class TabBasicMoveAppModel extends TabBasicMoveApp {
    /**
     * 选择状态：1已选2未选
     */
    private Integer selectState;
}
