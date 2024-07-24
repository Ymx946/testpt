package com.mz.framework.mobile.device;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeviceResolverRequestFilter extends OncePerRequestFilter {
    private final DeviceResolver deviceResolver;

    public DeviceResolverRequestFilter() {
        this(new LiteDeviceResolver());
    }

    public DeviceResolverRequestFilter(DeviceResolver deviceResolver) {
        this.deviceResolver = deviceResolver;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Device device = this.deviceResolver.resolveDevice(request);
        request.setAttribute("currentDevice", device);
        filterChain.doFilter(request, response);
    }
}
