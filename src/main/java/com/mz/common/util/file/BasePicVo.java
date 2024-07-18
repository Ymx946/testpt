package com.mz.common.util.file;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BasePicVo {

    /**
     * 地址数组
     */
    private String[] urlArr;

    /**
     * 名称数组
     */
    private String[] oldNameArr;

    /**
     * 文件大小（KB）
     */
    private Float[] sizeArr;
    /**
     * 高
     */
    private Integer[] heightArr;
    /**
     * 宽
     */
    private Integer[] widthArr;

}
