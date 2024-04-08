package com.mz.common.util.zjzw.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


@Setter
@Getter
@ToString
public class AttributeParamDto implements Serializable {
    /**
     * 字段英文名称 如: address
     */
    private String key;
    /**
     * 中文名称,如:地址
     */
    private String keyName;
    /**
     * 字段显示值,如: 浙江省杭州市西湖区
     */
    private String value;

}
