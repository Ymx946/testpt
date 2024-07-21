package com.mz.framework.service.common;

import com.mz.framework.annotation.OperationLog;
import org.aspectj.lang.JoinPoint;

public interface ContentParser {

    /**
     * 获取信息返回查询出的对象
     *
     * @param joinPoint    查询条件的参数
     * @param operationLog 注解
     * @return 获得的结果
     */
    Object getResult(JoinPoint joinPoint, OperationLog operationLog, String tableId);


}