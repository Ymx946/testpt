package com.mz.model.system.model;

import com.mz.model.system.SystemDataServiceNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@SuppressWarnings("serial")
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class SystemDataServiceNodeModel extends SystemDataServiceNode {
    /**
     * 更新时间
     */
    private String msg;
    /**
     * 更新内容
     */
    private String content;
}
