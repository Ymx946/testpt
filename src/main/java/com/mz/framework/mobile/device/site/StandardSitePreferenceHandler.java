package com.mz.framework.mobile.device.site;


import com.mz.framework.mobile.device.Device;
import com.mz.framework.mobile.device.DeviceUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StandardSitePreferenceHandler implements SitePreferenceHandler {
    private static final String SITE_PREFERENCE_PARAMETER = "site_preference";
    private final SitePreferenceRepository sitePreferenceRepository;

    public StandardSitePreferenceHandler(SitePreferenceRepository sitePreferenceRepository) {
        this.sitePreferenceRepository = sitePreferenceRepository;
    }

    public SitePreference handleSitePreference(HttpServletRequest request, HttpServletResponse response) {
        SitePreference preference = this.getSitePreferenceQueryParameter(request);
        if (preference != null) {
            this.sitePreferenceRepository.saveSitePreference(preference, request, response);
        } else {
            preference = this.sitePreferenceRepository.loadSitePreference(request);
        }

        if (preference == null) {
            preference = this.getDefaultSitePreferenceForDevice(DeviceUtils.getCurrentDevice(request));
        }

        if (preference != null) {
            request.setAttribute("currentSitePreference", preference);
        }

        return preference;
    }

    private SitePreference getSitePreferenceQueryParameter(HttpServletRequest request) {
        String string = request.getParameter("site_preference");

        try {
            return string != null && string.length() > 0 ? SitePreference.valueOf(string.toUpperCase()) : null;
        } catch (IllegalArgumentException var4) {
            return null;
        }
    }

    private SitePreference getDefaultSitePreferenceForDevice(Device device) {
        if (device == null) {
            return null;
        } else if (device.isMobile()) {
            return SitePreference.MOBILE;
        } else {
            return device.isTablet() ? SitePreference.TABLET : SitePreference.NORMAL;
        }
    }
}