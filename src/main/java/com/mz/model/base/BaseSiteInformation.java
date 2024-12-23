package com.mz.model.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 站点信息表(BaseSiteInformation)实体类
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class BaseSiteInformation {
    private static final long serialVersionUID = -73210261304381037L;
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
     * 坐标经度
     */
    private String localLng;
    /**
     * 坐标纬度
     */
    private String localLat;
    /**
     * 逻辑删除 1正常-1删除
     */
    private Integer delState;
    /**
     * 状态 1启用 -1停用
     */
    private Integer state;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 创建人
     */
    private String createUser;
    /**
     * 修改时间
     */
    private String modifyTime;
    /**
     * 修改人
     */
    private String modifyUser;
    /**
     * 备注
     */
    private String remarks;

}
