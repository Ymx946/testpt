package com.mz.common.context;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 上下文工具类
 *
 * @author laughdie
 */
public abstract class CommonContext {

    /**
     * 获取http请求对象
     *
     * @return HttpRequest
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null == attrs) {
            return null;
        }
        return attrs.getRequest();
    }

}
