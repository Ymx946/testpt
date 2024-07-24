package com.mz.framework.mobile.device.switcher;

import javax.servlet.http.HttpServletRequest;

public interface SiteUrlFactory {
    boolean isRequestForSite(HttpServletRequest var1);

    String createSiteUrl(HttpServletRequest var1);
}
