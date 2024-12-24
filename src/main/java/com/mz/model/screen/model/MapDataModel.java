package com.mz.model.screen.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 大屏点位数据
 *
 * @author makejava
 * @since 2021-07-05 11:49:26
 */
@Setter
@Getter
@ToString
public class MapDataModel {
    /**
     * 主键
     */
    private Long id;
    /**
     * 区域代码
     */
    private String areaCode;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 区
     */
    private String county;
    /**
     * 站点编码
     */
    private String siteCode;
    /**
     * 站点名称
     */
    private String siteName;
    /**
     * 电压等级代码
     */
    private String voltageLevelCode;
    /**
     * 电压等级名称
     */
    private String voltageLevelName;
    /**
     * 站点描述
     */
    private String siteDescribe;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 所属单位ID
     */
    private Long unitId;
    /**
     * 所属单位名称
     */
    private String unitName;
    /**
     * 运维单位ID
     */
    private Long opsId;
    /**
     * 运维单位名称
     */
    private String opsName;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 坐标(高德)
     */
    private String coordinate;
    /**
     * 坐标(天地图84)
     */
    private String coordinate84;
    /**
     * 图片列表
     */
    private List<String> picList;

}