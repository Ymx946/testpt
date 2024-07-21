package com.mz.model.base;

import com.mz.common.annotation.FieldMeta;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 流程控制表(ActFlowInfo)实体类
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class TabActivitiFlowInfo implements Serializable {
    private static final long serialVersionUID = -52124323983975879L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 流程名称
     */
    @FieldMeta(name = "流程名称")
    private String flowName;
    /**
     * 流程key
     */
    @FieldMeta(name = "流程key")
    private String flowKey;
    /**
     * 流程路径
     */
    @FieldMeta(name = "流程路径")
    private String filePath;
    /**
     * 逻辑删除
     */
    private Integer delState;
    /**
     * 流程状态1- 没有部署  0- 已经部署
     */
    @FieldMeta(name = "流程状态", readConverterExp = "1=没有部署,0=已经部署")
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
    @FieldMeta(name = "备注")
    private String remarks;

}
