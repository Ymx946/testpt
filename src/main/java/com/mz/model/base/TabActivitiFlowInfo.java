package com.mz.model.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 流程控制表(ActFlowInfo)实体类
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class TabActivitiFlowInfo {
    private static final long serialVersionUID = -52124323983975879L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 流程名称
     */
    private String flowName;
    /**
     * 流程key
     */
    private String flowKey;
    /**
     * 流程路径
     */
    private String filePath;
    /**
     * 逻辑删除
     */
    private Integer delState;
    /**
     * 流程状态1- 没有部署  0- 已经部署
     */
    private Integer state;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 创建人
     */
    private String createUser;
    /**
     * 修改时间
     */
    private String modifyTime;
    /**
     * 修改人
     */
    private String modifyUser;
    /**
     * 备注
     */
    private String remarks;

}
