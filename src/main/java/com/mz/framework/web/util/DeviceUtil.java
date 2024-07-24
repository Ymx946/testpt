package com.mz.framework.web.util;

import com.mz.framework.mobile.device.Device;
import com.mz.framework.mobile.device.LiteDeviceResolver;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

import javax.servlet.http.HttpServletRequest;

public class DeviceUtil {

    public static String getDeviceType(HttpServletRequest request) {
        LiteDeviceResolver deviceResolver = new LiteDeviceResolver();
        Device device = deviceResolver.resolveDevice(request);
        String deviceType = getBrowseDeviceType(request);
        if ("Unknown".equalsIgnoreCase(deviceType)) {
            //可以在这做判断设备类型
            if (device.isMobile()) {
                return "Mobile";
            } else if (device.isTablet()) {
                return "Pad";
            } else {
                return "PC";
            }
        }
        return deviceType;
    }


    public static String getBrowseDeviceType(HttpServletRequest request) {
        String agentString = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(agentString);
        OperatingSystem operatingSystem = userAgent.getOperatingSystem(); // 操作系统信息
        eu.bitwalker.useragentutils.DeviceType deviceType = operatingSystem.getDeviceType(); // 设备类型

        switch (deviceType) {
            case COMPUTER:
                return "PC";
            case TABLET: {
                if (agentString.contains("Android")) {
                    return "Android Pad";
                }
                if (agentString.contains("iOS")) {
                    return "iPad";
                }
                return "Unknown";
            }
            case MOBILE: {
                if (agentString.contains("Android")) {
                    return "Android";
                }
                if (agentString.contains("iOS")) {
                    return "IOS";
                }
                return "Unknown";
            }
            default:
                return "Unknown";
        }
    }
}
