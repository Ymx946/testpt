package com.mz.framework.mobile.device.util;


import com.mz.framework.mobile.device.Device;
import com.mz.framework.mobile.device.site.SitePreference;

public class ResolverUtils {
    private ResolverUtils() {
    }

    public static boolean isNormal(Device device, SitePreference sitePreference) {
        return sitePreference == SitePreference.NORMAL || device == null || device.isNormal() && sitePreference == null;
    }

    public static boolean isMobile(Device device, SitePreference sitePreference) {
        return sitePreference == SitePreference.MOBILE || device != null && device.isMobile() && sitePreference == null;
    }

    public static boolean isTablet(Device device, SitePreference sitePreference) {
        return sitePreference == SitePreference.TABLET || device != null && device.isTablet() && sitePreference == null;
    }
}
