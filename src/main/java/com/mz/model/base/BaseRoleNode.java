package com.mz.model.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 角色节点表(BaseRoleNode)实体类
 *
 * @author makejava
 * @since 2021-03-17 09:08:17
 */
@Setter
@Getter
@ToString
public class BaseRoleNode {
    private static final long serialVersionUID = -54957374584693313L;
    /**
     * 主键
     */
    private String id;
    /**
     * 用户ID
     */
    private String roleId;
    /**
     * 节点ID
     */
    private String nodeId;

}