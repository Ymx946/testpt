package com.mz.common.annotation;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface FieldMeta {

    String name() default ""; // 字段名称

    /**
     * 读取枚举内容转义表达式 (如: 0=男,1=女,2=未知)
     */
    String readConverterExp() default "";
}

