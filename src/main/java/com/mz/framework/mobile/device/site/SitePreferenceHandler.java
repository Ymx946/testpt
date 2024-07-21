package com.mz.framework.mobile.device.site;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SitePreferenceHandler {
    String CURRENT_SITE_PREFERENCE_ATTRIBUTE = "currentSitePreference";

    SitePreference handleSitePreference(HttpServletRequest var1, HttpServletResponse var2);
}
