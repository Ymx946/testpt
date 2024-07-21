package com.mz.framework.service.common.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mz.common.util.reflect.ReflectionUtils;
import com.mz.framework.annotation.OperationLog;
import com.mz.framework.service.common.ContentParser;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.Serializable;

@Component
public class DefaultContentParse implements ContentParser {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Object getResult(JoinPoint joinPoint, OperationLog operationLog, String tableId) {
        Object info = joinPoint.getArgs()[0];
        Object id = ReflectionUtils.getFieldValue(info, tableId);
        Assert.notNull(id, "id不能为空");
        Class<?> idType = operationLog.idType();
        if (idType.isInstance(id)) {
            Class<?> cls = operationLog.serviceclass();
            IService service = (IService) applicationContext.getBean(cls);
            return service.getById((Serializable) id);
        } else {
            throw new RuntimeException("请核实id type");
        }
    }

}