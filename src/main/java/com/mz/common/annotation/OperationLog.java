package com.mz.framework.annotation;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mz.common.annotation.enums.LogType;
import com.mz.common.annotation.enums.OperationType;
import com.mz.framework.service.common.impl.DefaultContentParse;

import java.lang.annotation.*;

/**
 * @author yangh
 * @date 2024/05/07 15:19
 * @desc 操作日志注解
 */
@Documented
// 注解类型为方法注解
@Target({ElementType.METHOD})
// 在运行是执行
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLog {

    /**
     * 获取编辑信息的解析类，目前为使用id获取
     *
     * @return
     */
    Class<?> parseclass() default DefaultContentParse.class;

    /**
     * 查询数据库所调用的class文件 selectById方法所在的Service类
     *
     * @return
     */
    Class<?> serviceclass() default IService.class;

    /**
     * id的类型
     *
     * @return
     */
    Class<?> idType() default Long.class;

    /**
     * 操作对象的id字段名称
     *
     * @return
     */
    String tableId() default "id";

    /**
     * 操作类型
     *
     * @return
     */
    OperationType operatorType() default OperationType.MANAGE;

    /**
     * 所属模块
     *
     * @return
     */
    String moduleName() default "数字管理后台";

    /**
     * 日志详情
     *
     * @return
     */
    String logDetails() default "";

    /**
     * 类型 1登录 2业务操作
     *
     * @return
     */
    LogType logType() default LogType.BUSINESS;

    /**
     * 是否保存请求头部的参数
     *
     * @return
     */
    boolean isSaveHeaderData() default true;

    /**
     * 是否保存请求的参数
     *
     * @return
     */
    boolean isSaveReqData() default true;

    /**
     * 是否保存响应的参数
     *
     * @return
     */
    boolean isSaveRespData() default false;

}