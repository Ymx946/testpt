package com.mz.framework.mobile.device;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class DeviceHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    public DeviceHandlerMethodArgumentResolver() {
    }

    public boolean supportsParameter(MethodParameter parameter) {
        return Device.class.isAssignableFrom(parameter.getParameterType());
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest request, WebDataBinderFactory binderFactory) throws Exception {
        return DeviceUtils.getCurrentDevice(request);
    }
}

