package com.mz.framework.mobile.device.site;


import org.springframework.web.util.CookieGenerator;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieSitePreferenceRepository extends CookieGenerator implements SitePreferenceRepository {
    private static final String DEFAULT_COOKIE_NAME = CookieSitePreferenceRepository.class.getName() + ".SITE_PREFERENCE";

    public CookieSitePreferenceRepository() {
        this.setCookieName(DEFAULT_COOKIE_NAME);
    }

    public CookieSitePreferenceRepository(String cookieDomain) {
        this.setCookieName(DEFAULT_COOKIE_NAME);
        this.setCookieDomain(cookieDomain);
    }

    public void saveSitePreference(SitePreference preference, HttpServletRequest request, HttpServletResponse response) {
        this.addCookie(response, preference.name());
    }

    public SitePreference loadSitePreference(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, this.getCookieName());
        return cookie != null ? SitePreference.valueOf(cookie.getValue()) : null;
    }
}
