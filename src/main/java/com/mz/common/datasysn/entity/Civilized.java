package com.mz.common.datasysn.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class Civilized implements Serializable {
    /**
     * 主键
     **/
    private Long id;
    /**
     * 户主名称
     **/
    private String householder;
    /**
     * 家庭地址
     **/
    private String address;
    /**
     * 家庭简介
     **/
    private String introduction;
    /**
     * 图片(多个文件用英文,分割)
     **/
    private String picture;
    /**
     * 家庭类型(文明家庭-1,最美家庭-2)
     **/
    private String type;
    /**
     * 归集方式(人工填报-page)
     **/
    private String collect_method;
    /**
     * 经度
     **/
    private String longitude;
    /**
     * 纬度
     **/
    private String latitude;

}
