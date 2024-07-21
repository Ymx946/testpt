package com.mz.model.base;

import com.mz.common.annotation.FieldMeta;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


/**
 * 操作日志表(SysOperaLog)实体类
 *
 * @author makejava
 * @since 2021-03-17 11:00:37
 */
@Setter
@Getter
@ToString
public class SysOperaLog implements Serializable {
    private static final long serialVersionUID = -68589081165542545L;
    public static int LOG_TYPE_LOGIN = 1;
    public static int LOG_TYPE_BUS = 2;
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
    @FieldMeta(name = "区域名称")
    private String areaName;
    /**
     * 所属主体ID
     */
    private String mainBodyId;
    /**
     * 所属主体名称
     */
    @FieldMeta(name = "所属主体名称")
    private String mainBodyName;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 账号
     */
    @FieldMeta(name = "账号")
    private String loginName;
    /**
     * 姓名
     */
    @FieldMeta(name = "姓名")
    private String realName;
    /**
     * IP
     */
    @FieldMeta(name = "IP")
    private String ip;
    /**
     * IP地址
     */
    @FieldMeta(name = "IP地址")
    private String ipAddress;
    /**
     * 所属系统
     */
    @FieldMeta(name = "所属系统")
    private String sysNo;
    /**
     * 请求路径
     */
    @FieldMeta(name = "请求路径")
    private String reqPath;
    /**
     * 请求方法
     */
    @FieldMeta(name = "请求方法")
    private String reqMethod;
    /**
     * header参数
     */
    @FieldMeta(name = "header参数")
    private String headerParam;
    /**
     * 请求参数
     */
    @FieldMeta(name = "请求参数")
    private String reqParam;
    /**
     * 返回参数
     */
    @FieldMeta(name = "返回参数")
    private String jsonResult;
    /**
     * 所属模块名称
     */
    @FieldMeta(name = "所属模块名称")
    private String moduleName;
    /**
     * 操作内容
     */
    @FieldMeta(name = "操作内容")
    private String operaContent;
    /**
     * 类型1登录2业务操作
     */
    @FieldMeta(name = "类型", readConverterExp = "1=登录,2=业务操作")
    private Integer logType;
    /**
     * 设备
     */
    @FieldMeta(name = "设备")
    private String operaEqpm;
    /**
     * 错误信息
     */
    @FieldMeta(name = "错误信息")
    private String errorMsg;
    /**
     * 操作日志统计表id
     */
    private Long operaStatisticsId;
    /**
     * 操作时间
     */
    @FieldMeta(name = "操作时间")
    private String operaTime;

}