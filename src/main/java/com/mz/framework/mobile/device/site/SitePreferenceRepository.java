package com.mz.framework.mobile.device.site;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SitePreferenceRepository {
    SitePreference loadSitePreference(HttpServletRequest var1);

    void saveSitePreference(SitePreference var1, HttpServletRequest var2, HttpServletResponse var3);
}
