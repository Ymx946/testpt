package com.mz.framework.mobile.device.site;

import org.springframework.web.context.request.RequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class SitePreferenceUtils {
    private SitePreferenceUtils() {
    }

    public static SitePreference getCurrentSitePreference(HttpServletRequest request) {
        return (SitePreference) request.getAttribute("currentSitePreference");
    }

    public static SitePreference getCurrentSitePreference(RequestAttributes attributes) {
        return (SitePreference) attributes.getAttribute("currentSitePreference", 0);
    }
}
