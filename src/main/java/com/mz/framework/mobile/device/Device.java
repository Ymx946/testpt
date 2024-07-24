package com.mz.framework.mobile.device;


public interface Device {
    boolean isNormal();

    boolean isMobile();

    boolean isTablet();

    DevicePlatform getDevicePlatform();
}
