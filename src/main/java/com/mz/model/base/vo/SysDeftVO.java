package com.mz.model.base.vo;

import com.mz.model.base.SysDeft;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SysDeftVO extends SysDeft {
    /**
     * 所属租户
     */
    private String tenantId;
    /**
     * 是否拥有权限 1是2否
     */
    private Integer powerHas;
}
