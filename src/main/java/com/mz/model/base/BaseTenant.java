package com.mz.model.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 租户表(BaseTenant)实体类
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class BaseTenant  {
    private static final long serialVersionUID = 825469444965246936L;
    /**
    * 主键
    */
    private Long id;
    /**
    * 租户名称
    */
    private String tenantName;
    /**
    * 备注
    */
    private String remarks;
    /**
    * 有限期开始
    */
    private String periodStart;
    /**
    * 有限期结束
    */
    private String periodEnd;
    /**
    * 可用状态 1正常2删除
    */
    private Integer useState;
    /**
    * 一体化平台名称
    */
    private String platformName;
    /**
    * 一体化顶部图片
    */
    private String platformPic;
    /**
    * 区域代码
    */
    private String areaCode;
    /**
    * 区域名称
    */
    private String areaName;
    /**
    * 密码类型1-固定密码 2-随机密码 3-手机号后6位
    */
    private Integer passwordType;
    /**
    * 初始密码
    */
    private String passwordDefault;
    /**
    * 是否开启水印 1是2否
    */
    private Integer isOpenWatermark;
    /**
    * 服务节点ID
    */
    private Long serviceNodeId;
    /**
    * 服务节点名称
    */
    private String serviceNodeName;
    /**
    * 授权主体数
    */
    private Integer accreditMainNum;
    /**
     * 下发状态 0-未下发 1已下发 -1下发失败
     */
    private Integer issuedState;
    /**
    * 创建时间
    */
    private String creatTime;
    /**
    * 创建人
    */
    private String creatUser;
    /**
    * 修改时间
    */
    private String modifyTime;
    /**
    * 修改人
    */
    private String modifyUser;

}
