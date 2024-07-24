package com.mz.framework.mobile.device.switcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface SiteSwitcherHandler {
    boolean handleSiteSwitch(HttpServletRequest var1, HttpServletResponse var2) throws IOException;
}
