package com.mz.model.base.model;

import com.mz.model.base.BaseSoftwareGroupClassifySys;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 应用分类关联应用(BaseSoftwareGroupClassifySys)实体类
 *
 * @author makejava
 * @since 2023-08-30 10:36:37
 */
@Setter
@Getter
@ToString
public class BaseSoftwareGroupClassifySysModel extends BaseSoftwareGroupClassifySys {
    /**
     * 开通状态
     */
    private Integer openState;

}
