package com.mz.model.system.vo;

import com.mz.common.model.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *下发数据选择查询
 */
@Setter
@Getter
@ToString
public class SelectSearchVO extends BaseDTO {
    /**
     * 下发数据类型代码1-数据字典 2-PC应用市场 3-移动应用市场 4-节点管理
    */
    private Integer type;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
    * 名称
    */
    private String name;
    /**
     * 状态
     */
    private Integer state;
    /**
     * 区域代码
     */
    private String areaCode;

}
