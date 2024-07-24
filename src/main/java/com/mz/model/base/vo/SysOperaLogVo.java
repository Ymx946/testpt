package com.mz.model.base.vo;

import com.mz.common.model.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SysOperaLogVo extends BaseDTO {
    /**
     * 搜索关键字
     */
    private String findStr;
    /**
     * 操作开始时间
     */
    private String operaTimeS;
    /**
     * 操作结束时间
     */
    private String operaTimeE;

    /**
     * 主键
     */
    private String id;
    /**
     * 所属租户
     */
    private String tenantId;
    /**
     * 区域代码
     */
    private String areaCode;
    /**
     * 区域名称
     */
    private String areaName;
    /**
     * 所属主体ID
     */
    private String mainBodyId;
    /**
     * 所属主体名称
     */
    private String mainBodyName;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 账号
     */
    private String loginName;
    /**
     * 姓名
     */
    private String realName;
    /**
     * IP地址
     */
    private String ip;
    /**
     * IP地址
     */
    private String ipAddress;
    /**
     * 所属系统
     */
    private String sysNo;
    /**
     * 请求路径
     */
    private String reqPath;
    /**
     * 请求方法
     */
    private String reqMethod;
    /**
     * header参数
     */
    private String headerParam;
    /**
     * 请求参数
     */
    private String reqParam;
    /**
     * 返回参数
     */
    private String jsonResult;
    /**
     * 所属模块名称
     */
    private String moduleName;

    /**
     * 操作内容
     */
    private String operaContent;
    /**
     * 类型 1登录 2业务操作
     */
    private Integer logType;
    /**
     * 设备
     */
    private String operaEqpm;

    /**
     * 操作时间
     */
    private String errorMsg;
    /**
     * 操作日志统计表id
     */
    private Long operaStatisticsId;
    /**
     * 操作时间
     */
    private String operaTime;

}
