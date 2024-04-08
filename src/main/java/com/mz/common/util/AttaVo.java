package com.mz.common.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AttaVo {

    /**
     * 地址
     */
    private String url;

    /**
     * 名称
     */
    private String oldName;

    /**
     * 时长
     */
    private Long timeLong;

    /**
     * 时长
     */
    private String timeLongStr;
    /**
     * 高
     */
    private Integer height;
    /**
     * 宽
     */
    private Integer width;

}
