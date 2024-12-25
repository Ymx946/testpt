package com.mz.model.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * (BaseSiteBatteryPack)实体类
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class BaseSiteBatteryPack {
    private static final long serialVersionUID = 891291839409857774L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 站点ID
     */
    private Long siteId;
    /**
     * 站点名称
     */
    private String siteName;
    /**
     * 所属主机ID
     */
    private Long hostId;
    /**
     * 所属主机名称
     */
    private String hostName;
    /**
     * 电池组名称
     */
    private String batteryPackName;
    /**
     * 标称容量
     */
    private String nominalCapacity;
    /**
     * 安全接入1合法2非法
     */
    private Integer secureAccess;
    /**
     * 当前状态代码
     */
    private String currentStateCode;
    /**
     * 当前状态名称
     */
    private String currentStateName;
    /**
     * 在线电压(V)
     */
    private String onLineVoltage;
    /**
     * 组端电压(V)
     */
    private String groupVoltage;
    /**
     * 组端电流(A)
     */
    private String groupCurrent;
    /**
     * 站点内部编号
     */
    private String siteInternalNo;
    /**
     * 电池组厂商
     */
    private String batteryManufacturer;
    /**
     * 电池组标称电压
     */
    private String batteryNominalVoltage;
    /**
     * 电池组标称容量
     */
    private String batteryNominalCapacity;
    /**
     * 生产日期
     */
    private String productionDate;
    /**
     * 投运日期
     */
    private String commissioningDate;
    /**
     * 在线电压最大阈值
     */
    private String onLineVoltageMax;
    /**
     * 在线电压最小阈值
     */
    private String onLineVoltageMin;
    /**
     * 电池组电压最大阈值
     */
    private String batteryVoltageMax;
    /**
     * 电池组电压最小阈值
     */
    private String batteryVoltageMin;
    /**
     * 环境温度最大阈值
     */
    private String ambientTemperatureMax;
    /**
     * 环境温度最小阈值
     */
    private String ambientTemperatureMin;
    /**
     * 单体电压最大阈值
     */
    private String cellVoltageMax;
    /**
     * 单体电压最小阈值
     */
    private String cellVoltageMin;
    /**
     * 单体内阻最大阈值
     */
    private String cellResistanceMax;
    /**
     * 单体温度最大阈值
     */
    private String cellTemperatureMax;
    /**
     * 单体温度最小阈值
     */
    private String cellTemperatureMin;
    /**
     * 单体容量最大阈值
     */
    private String cellCapacityMax;
    /**
     * 单体容量最小阈值
     */
    private String cellCapacityMin;
    /**
     * 最大充电电流
     */
    private String maxChargeCurrent;
    /**
     * 最大放电电流
     */
    private String maxDischargeCurrent;
    /**
     * 浮充电压阈值
     */
    private String floatVoltageThreshold;
    /**
     * 均充电压阈值
     */
    private String equalizingVoltageThreshold;
    /**
     * 浮充电流阈值
     */
    private String floatCurrentThreshold;
    /**
     * 单体内阻最小阈值
     */
    private String cellResistanceMin;
    /**
     * 单体电池节数
     */
    private String batteryCellNum;
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
