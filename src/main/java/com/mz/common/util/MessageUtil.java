/*
 * @(#)MessageUtil.java 2014-2-19上午10:34:00
 * Copyright 2013 sinovatech, Inc. All rights reserved.
 */
package com.mz.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息工具类 提供请求xml消息解析、响应内容转换为xml、提示信息等方法
 */
@Slf4j
public class MessageUtil {
    /**
     * 获取请求参数
     *
     * @param request 请求
     * @return 请求参数Map集合
     * @throws Exception
     * @author sunju @creationDate. 2014-2-19 上午10:49:02
     * 来源
     */
    public static Map<String, String> getReqParams(HttpServletRequest request) {
        Map<String, String> reqParams = new HashMap<>();
        /**
         * 装载请求参数（通用）
         */
        putParameter(reqParams, request);
        return reqParams;
    }

    /**
     * 装载请求参数
     *
     * @param reqParams 请求参数Map集合
     * @param request   请求
     * @throws Exception
     * @author sunju @creationDate. 2014-5-5 下午03:01:14
     */
    private static void putParameter(Map<String, String> reqParams, HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap != null && parameterMap.size() > 0) {
            String key = null;
            String[] values = null;
            String value = null;
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                key = entry.getKey();

                /**
                 * 将请求参数装入键值对Map，一个参数多值的合并
                 */
                values = parameterMap.get(key);
                value = (values != null && values.length > 0) ? StringUtils.join(values, ",") : null;
                reqParams.put(key, value);
            }
        }
    }

}
