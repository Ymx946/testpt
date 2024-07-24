package com.mz.framework.mobile.device;

public class LiteDevice implements Device {
    public static final com.mz.framework.mobile.device.LiteDevice NORMAL_INSTANCE;
    public static final com.mz.framework.mobile.device.LiteDevice MOBILE_INSTANCE;
    public static final com.mz.framework.mobile.device.LiteDevice TABLET_INSTANCE;

    static {
        NORMAL_INSTANCE = new com.mz.framework.mobile.device.LiteDevice(DeviceType.NORMAL, DevicePlatform.UNKNOWN);
        MOBILE_INSTANCE = new LiteDevice(DeviceType.MOBILE, DevicePlatform.UNKNOWN);
        TABLET_INSTANCE = new LiteDevice(DeviceType.TABLET, DevicePlatform.UNKNOWN);
    }

    private final DeviceType deviceType;
    private final DevicePlatform devicePlatform;

    private LiteDevice(DeviceType deviceType, DevicePlatform devicePlatform) {
        this.deviceType = deviceType;
        this.devicePlatform = devicePlatform;
    }

    public static Device from(DeviceType deviceType, DevicePlatform devicePlatform) {
        return new LiteDevice(deviceType, devicePlatform);
    }

    public boolean isNormal() {
        return this.deviceType == DeviceType.NORMAL;
    }

    public boolean isMobile() {
        return this.deviceType == DeviceType.MOBILE;
    }

    public boolean isTablet() {
        return this.deviceType == DeviceType.TABLET;
    }

    public DevicePlatform getDevicePlatform() {
        return this.devicePlatform;
    }

    public DeviceType getDeviceType() {
        return this.deviceType;
    }

    public String toString() {
        String builder = "[LiteDevice " +
                "type" + "=" + this.deviceType +
                "]";
        return builder;
    }
}