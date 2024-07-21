package com.mz.framework.mobile.device;

import com.mz.framework.mobile.device.Device;
import com.mz.framework.mobile.device.DeviceResolver;
import com.mz.framework.mobile.device.LiteDeviceResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeviceResolverHandlerInterceptor extends HandlerInterceptorAdapter {
    private final DeviceResolver deviceResolver;

    public DeviceResolverHandlerInterceptor() {
        this(new LiteDeviceResolver());
    }

    public DeviceResolverHandlerInterceptor(DeviceResolver deviceResolver) {
        this.deviceResolver = deviceResolver;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Device device = this.deviceResolver.resolveDevice(request);
        request.setAttribute("currentDevice", device);
        return true;
    }
}
