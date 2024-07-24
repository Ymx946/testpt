package com.mz.framework.mobile.device;


import javax.servlet.http.HttpServletRequest;

public interface DeviceResolver {
    Device resolveDevice(HttpServletRequest var1);
}
