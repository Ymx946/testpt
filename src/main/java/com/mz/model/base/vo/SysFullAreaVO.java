package com.mz.model.base.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 区域表(SysArea)实体类
 *
 * @author makejava
 * @since 2021-03-17 10:58:48
 */
@Setter
@Getter
@ToString
public class SysFullAreaVO {
    /**
    * 区域代码
    */
    private String areaCode;
    /**
    * 省
    */
    private String province;
    /**
    * 市
    */
    private String city;
    /**
    * 区
    */
    private String county;
    /**
    * 街道
    */
    private String street;
    /**
    * 社区/村
    */
    private String village;

}