package com.mz.model.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 站点主机信息表(BaseSiteHost)实体类
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class BaseSiteHost {
    private static final long serialVersionUID = -43584733980570201L;
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
     * 设备类型代码
     */
    private String deviceTypeCode;
    /**
     * 设备类型名称
     */
    private String deviceTypeName;
    /**
     * IP地址
     */
    private String ipAddress;
    /**
     * IP网关
     */
    private String ipGateway;
    /**
     * IP子网掩码
     */
    private String ipSubnetMask;
    /**
     * IP端口
     */
    private String ipPort;
    /**
     * 设备编码
     */
    private String hostCode;
    /**
     * 设备序列号
     */
    private String hostSerialNumber;
    /**
     * 设备名称
     */
    private String hostName;
    /**
     * 工作模式
     */
    private String workPattern;
    /**
     * 协议转换器编码
     */
    private String transverterCode;
    /**
     * 所属屏位
     */
    private String booth;
    /**
     * 设备厂商
     */
    private String hostVendor;
    /**
     * 设备型号
     */
    private String hostModel;
    /**
     * 生产日期
     */
    private String productionDate;
    /**
     * 放电电流
     */
    private String dischargeCurrent;
    /**
     * 放电容量
     */
    private String dischargeCapacity;
    /**
     * 放电时间
     */
    private String dischargeTime;
    /**
     * 单体电压下限
     */
    private String monomerVoltLo;
    /**
     * 单体下限个数
     */
    private String lowerLimitNumber;
    /**
     * 组端电压下限
     */
    private String classVoltLo;
    /**
     * 充电电流
     */
    private String chargeCurrent;
    /**
     * 升压上限
     */
    private String boostLimit;
    /**
     * 本地屏保时间
     */
    private String screensaverTime;
    /**
     * 监控数据保存间隔
     */
    private String monitorSave;
    /**
     * 测试数据保存间隔
     */
    private String testDataSave;
    /**
     * 干接点启用1-第1路2-第2路3-第3路4-第4路
     */
    private Integer dryContactType;
    /**
     * 硬件版本
     */
    private String hardwareVersion;
    /**
     * 本地软件版本
     */
    private String softwareVersion;
    /**
     * DCDC模块数
     */
    private Integer dcdcModuleNum;
    /**
     * 负荷监测模块数
     */
    private Integer monitoringModuleNum;
    /**
     * 外部电流绀量程
     */
    private String externalCyanosisRange;
    /**
     * 电池采集模块数
     */
    private Integer collectModuleNum;
    /**
     * 电池采集模块地址
     */
    private String collectModuleAddress;
    /**
     * 电池组数
     */
    private Integer batteryNum;
    /**
     * 每组电池节数
     */
    private Integer batteryGroupNum;
    /**
     * 内部电流绀量程
     */
    private String interiorCyanosisRange;
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
