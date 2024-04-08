package com.mz.framework.handler;

import com.mz.common.context.DateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.Date;

@ControllerAdvice
public class DateHandler {

    /**
     * 初始化数据绑定
     *
     * @param binder 数据绑定对象
     */
    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
        // 自动转换日期类型的字段格式
        binder.registerCustomEditor(Date.class, new DateEditor(true));
    }
}