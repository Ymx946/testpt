package com.mz.model.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 角色表(BaseRole)实体类
 *
 * @author makejava
 * @since 2021-03-17 09:08:17
 */
@Setter
@Getter
@ToString
public class BaseRole {
    private static final long serialVersionUID = -18524636426531924L;
    /**
     * 主键
     */
    private String id;
    /**
     * 所属租户
     */
    private String tenantId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 所属部门
     */
    private String deptId;
    /**
     * 所属主体ID
     */
    private String mainBodyId;
    /**
     * 角色等级1-超级管理员 2-租户管理员 3-主体管理员 4-干部用户 5普通用户
     */
    private Integer rolerLevel;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 可用状态 1可用2不可用
     */
    private Integer useState;
    /**
     * 职位类型代码
     */
    private String dutyTypeCode;
    /**
     * 职位类型名称
     */
    private String dutyTypeName;
    /**
     * 系统职务角色ID
     */
    private Long sysDutyRoleId;
    /**
     * 是否系统角色 1系统角色（控制不可修改） 2用户角色
     */
    private Integer systemBelong;

}