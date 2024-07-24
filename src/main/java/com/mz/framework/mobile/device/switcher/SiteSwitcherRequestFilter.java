package com.mz.framework.mobile.device.switcher;

import com.mz.framework.mobile.device.site.CookieSitePreferenceRepository;
import com.mz.framework.mobile.device.site.SitePreferenceHandler;
import com.mz.framework.mobile.device.site.StandardSitePreferenceHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SiteSwitcherRequestFilter extends OncePerRequestFilter {
    private SiteSwitcherHandler siteSwitcherHandler;
    private String switcherMode;
    private String serverName;
    private Boolean tabletIsMobile;
    private String mobilePath;
    private String tabletPath;
    private String rootPath;

    public SiteSwitcherRequestFilter() {
    }

    public SiteSwitcherRequestFilter(SiteUrlFactory normalSiteUrlFactory, SiteUrlFactory mobileSiteUrlFactory, SiteUrlFactory tabletSiteUrlFactory, SitePreferenceHandler sitePreferenceHandler) {
        this.siteSwitcherHandler = new StandardSiteSwitcherHandler(normalSiteUrlFactory, mobileSiteUrlFactory, tabletSiteUrlFactory, sitePreferenceHandler, this.tabletIsMobile);
    }

    public String getSwitcherMode() {
        return this.switcherMode;
    }

    public void setSwitcherMode(String switcherMode) {
        this.switcherMode = switcherMode;
    }

    public String getServerName() {
        return this.serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Boolean getTabletIsMobile() {
        return this.tabletIsMobile;
    }

    public void setTabletIsMobile(Boolean tabletIsMobile) {
        this.tabletIsMobile = tabletIsMobile;
    }

    public String getMobilePath() {
        return this.mobilePath;
    }

    public void setMobilePath(String mobilePath) {
        this.mobilePath = mobilePath;
    }

    public String getTabletPath() {
        return this.tabletPath;
    }

    public void setTabletPath(String tabletPath) {
        this.tabletPath = tabletPath;
    }

    public String getRootPath() {
        return this.rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    protected void initFilterBean() throws ServletException {
        if (this.switcherMode != null) {
            SiteSwitcherRequestFilter.SiteSwitcherMode mode;
            try {
                mode = SiteSwitcherRequestFilter.SiteSwitcherMode.valueOf(this.switcherMode.toUpperCase());
            } catch (IllegalArgumentException var3) {
                IllegalArgumentException ex = var3;
                throw new ServletException("Invalid switcherMode init parameter", ex);
            }

            if (mode == SiteSwitcherRequestFilter.SiteSwitcherMode.MDOT) {
                this.mDot();
            } else if (mode == SiteSwitcherRequestFilter.SiteSwitcherMode.DOTMOBI) {
                this.dotMobi();
            } else if (mode == SiteSwitcherRequestFilter.SiteSwitcherMode.URLPATH) {
                this.urlPath();
            }
        }

    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (this.siteSwitcherHandler.handleSiteSwitch(request, response)) {
            filterChain.doFilter(request, response);
        }

    }

    private void mDot() throws ServletException {
        if (this.serverName == null) {
            throw new ServletException("serverName init parameter not found");
        } else {
            this.siteSwitcherHandler = new StandardSiteSwitcherHandler(new StandardSiteUrlFactory(this.serverName), new StandardSiteUrlFactory("m." + this.serverName), null, new StandardSitePreferenceHandler(new CookieSitePreferenceRepository("." + this.serverName)), this.tabletIsMobile);
        }
    }

    private void dotMobi() throws ServletException {
        if (this.serverName == null) {
            throw new ServletException("serverName init parameter not found");
        } else {
            int lastDot = this.serverName.lastIndexOf(46);
            this.siteSwitcherHandler = new StandardSiteSwitcherHandler(new StandardSiteUrlFactory(this.serverName), new StandardSiteUrlFactory(this.serverName.substring(0, lastDot) + ".mobi"), null, new StandardSitePreferenceHandler(new CookieSitePreferenceRepository("." + this.serverName)), this.tabletIsMobile);
        }
    }

    private void urlPath() throws ServletException {
        SiteUrlFactory normalSiteUrlFactory = new NormalSitePathUrlFactory(this.mobilePath, this.tabletPath, this.rootPath);
        SiteUrlFactory mobileSiteUrlFactory = null;
        SiteUrlFactory tabletSiteUrlFactory = null;
        if (this.mobilePath != null) {
            mobileSiteUrlFactory = new MobileSitePathUrlFactory(this.mobilePath, this.tabletPath, this.rootPath);
        }

        if (this.tabletPath != null) {
            tabletSiteUrlFactory = new TabletSitePathUrlFactory(this.tabletPath, this.mobilePath, this.rootPath);
        }

        this.siteSwitcherHandler = new StandardSiteSwitcherHandler(normalSiteUrlFactory, mobileSiteUrlFactory, tabletSiteUrlFactory, new StandardSitePreferenceHandler(new CookieSitePreferenceRepository()), null);
    }

    private enum SiteSwitcherMode {
        MDOT,
        DOTMOBI,
        URLPATH;

        SiteSwitcherMode() {
        }
    }
}
