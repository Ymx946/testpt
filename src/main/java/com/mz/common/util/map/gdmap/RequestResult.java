package com.mz.common.util.map.gdmap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 高德地图返回工具类
 */
@Getter
@Setter
@AllArgsConstructor
public class RequestResult {

    /**
     * 状态码
     */
    private int code;

    /**
     * 返回body
     */
    private String body;

}