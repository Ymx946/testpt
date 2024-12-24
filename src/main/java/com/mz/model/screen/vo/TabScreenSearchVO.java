package com.mz.model.screen.vo;

import com.mz.common.model.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 大屏查询
 *
 * @author makejava
 * @since 2022-10-17 15:18:14
 */
@Setter
@Getter
@ToString
public class TabScreenSearchVO extends BaseDTO {
    /**
     * 名称查询
     */
    private String name;
    /**
     * 登录人的id
     */
    private String loginID;
    /**
     * 地图点位-数据对象(1站点)
     */
    private Integer dataObject;
    /**
     * 地图点位-数据代码
     */
    private String dataCode;
    /**
     * 数据字典类型代码
     */
    private String dictTypeCode;
    /**
     * 区域代码
     */
    private String areaCode;
    /**
     * 子区域长度
     */
    private Integer sonLen;
    /**
     * 类型 1-当前 2-历史
     */
    private Integer type;



}
