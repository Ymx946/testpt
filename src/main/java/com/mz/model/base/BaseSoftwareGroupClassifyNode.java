package com.mz.model.base;

import com.mz.common.annotation.FieldMeta;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 应用分类关联应用节点(BaseSoftwareGroupClassifyNode)实体类
 *
 * @author makejava
 * @since 2023-08-30 10:36:37
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class BaseSoftwareGroupClassifyNode implements Serializable {
    private static final long serialVersionUID = -40467561428689453L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 所属租户
     */
    private Long tenantId;
    /**
     * 分组ID
     */
    private Long groupId;
    /**
     * 分类ID
     */
    private Long classifyId;
    /**
     * 关联应用ID
     */
    private Long classifySysId;
    /**
     * 系统ID
     */
    private Long sysId;
    /**
     * 系统代码
     */
    private String sysCode;
    /**
     * 节点ID
     */
    private Long nodeId;
    /**
     * 节点名称
     */
    @FieldMeta(name = "节点名称")
    private String nodeName;
    /**
     * 逻辑删除
     */
    private Integer delState;
    /**
     * 状态 1启用 -1 禁用
     */
    @FieldMeta(name = "状态", readConverterExp = "1=启用,-1=禁用")
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
