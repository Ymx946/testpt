package com.mz.model.base.model;

import com.mz.model.base.SysNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class SysNodeSelectModel extends SysNode {
    /**
     * 是否被选中1是2否
     */
    private Integer selectState;

    /**
     * 子列表
     */
    private List<SysNodeSelectModel> sonNodeVoList;

}
