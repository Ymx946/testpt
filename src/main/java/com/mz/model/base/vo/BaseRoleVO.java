package com.mz.model.base.vo;

import com.mz.model.base.BaseRole;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BaseRoleVO extends BaseRole {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 主体代码
     */
    private String mainBodyCode;
    /**
     * 主体名称
     */
    private String mainBodyName;
    /**
     * 主体类型
     */
    private Integer mainBodyType;

    /**
     * 用户是否拥有角色 1是2否
     */
    private int userHasRole;


}
