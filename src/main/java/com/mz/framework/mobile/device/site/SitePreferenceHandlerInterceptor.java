package com.mz.framework.mobile.device.site;


import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SitePreferenceHandlerInterceptor extends HandlerInterceptorAdapter {
    private final SitePreferenceHandler sitePreferenceHandler;

    public SitePreferenceHandlerInterceptor() {
        this(new StandardSitePreferenceHandler(new CookieSitePreferenceRepository()));
    }

    public SitePreferenceHandlerInterceptor(SitePreferenceHandler sitePreferenceHandler) {
        this.sitePreferenceHandler = sitePreferenceHandler;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        this.sitePreferenceHandler.handleSitePreference(request, response);
        return true;
    }
}
