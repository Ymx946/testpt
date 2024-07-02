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
     * 类型1-监管端2-农企端
     */
    private Integer sysType;
    /**
     * 所属类型 1-自建 2-第三方
     */
    private Integer belongType;
    /**
     * 发布对象1村民2村友3游客4村委(多选)
     */
    private String bannerObj;
    /**
     * 应用类型 1-服务大厅 2-我的
     */
    private Integer appType;
    /**
     * 类型1-菜单 2-按钮
     */
    private Integer nodeType;
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
    /**
     * 所属系统
     */
    private String sysCode;
}
