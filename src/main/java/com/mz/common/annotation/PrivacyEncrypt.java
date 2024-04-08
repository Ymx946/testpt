package com.mz.common.annotation;


import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mz.common.PrivacyTypeEnum;
import com.mz.common.context.PrivacySerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @description: 自定义数据脱敏注解
 * @author: yangh
 * @createDate: 2022/7/26
 */

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME) // class文件中保留，运行时也保留，能通过反射读取到
@JacksonAnnotationsInside // 表示自定义自己的注解PrivacyEncrypt
@JsonSerialize(using = PrivacySerializer.class) // 该注解使用序列化的方式
public @interface PrivacyEncrypt {

    /**
     * 脱敏数据类型（没给默认值，所以使用时必须指定type）
     */
    PrivacyTypeEnum type();

    /**
     * 前置不需要打码的长度
     */
    int prefixNoMaskLen() default 1;

    /**
     * 后置不需要打码的长度
     */
    int suffixNoMaskLen() default 1;

    /**
     * 用什么打码
     */
    String symbol() default "*";
}


