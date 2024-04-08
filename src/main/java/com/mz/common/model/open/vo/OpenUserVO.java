package com.mz.common.model.open.vo;

import com.mz.common.model.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
public class OpenUserVO extends BaseDTO {

    /**
     * 租户id
     */
    private Long tenantId;
    /**
     * 站点id
     */
    private Long siteId;
    /**
     * 当前日期
     */
    private String curDate;
    /**
     * 区域编码
     */
    private String areaCode;
}
