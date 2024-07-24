package com.mz.model.base;

import com.mz.common.annotation.FieldMeta;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 用户角色表(BaseUserRole)实体类
 *
 * @author makejava
 * @since 2021-03-17 09:08:18
 */
@Setter
@Getter
@ToString
public class BaseUserRole implements Serializable {
    private static final long serialVersionUID = 362559093799804804L;
    /**
     * 主键
     */
    private String id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 职务ID
     */
    private String dutyId;
    /**
     * 用户ID
     */
    private String roleId;
    /**
     * 当前角色1是2否
     */
    @FieldMeta(name = "当前角色", readConverterExp = "1=是,2=否")
    private Integer currentRole;

}