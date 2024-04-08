package com.mz.common.model.base.model;

import com.mz.common.model.base.TabApplicationBase;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 应用申请表(TabApplicationBase)实体类
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class TabApplicationBaseModel extends TabApplicationBase {
    /**
     * 任务ID
     */
    private String taskId;
}
