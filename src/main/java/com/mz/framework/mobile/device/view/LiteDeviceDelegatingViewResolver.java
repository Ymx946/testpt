package com.mz.framework.mobile.device.view;

import com.mz.framework.mobile.device.Device;
import com.mz.framework.mobile.device.DeviceUtils;
import com.mz.framework.mobile.device.site.SitePreference;
import com.mz.framework.mobile.device.site.SitePreferenceUtils;
import com.mz.framework.mobile.device.util.ResolverUtils;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ViewResolver;

import javax.servlet.http.HttpServletRequest;

public class LiteDeviceDelegatingViewResolver extends AbstractDeviceDelegatingViewResolver {
    private String normalPrefix = "";
    private String mobilePrefix = "";
    private String tabletPrefix = "";
    private String normalSuffix = "";
    private String mobileSuffix = "";
    private String tabletSuffix = "";

    public LiteDeviceDelegatingViewResolver(ViewResolver delegate) {
        super(delegate);
    }

    protected String getNormalPrefix() {
        return this.normalPrefix;
    }

    public void setNormalPrefix(String normalPrefix) {
        this.normalPrefix = normalPrefix != null ? normalPrefix : "";
    }

    protected String getMobilePrefix() {
        return this.mobilePrefix;
    }

    public void setMobilePrefix(String mobilePrefix) {
        this.mobilePrefix = mobilePrefix != null ? mobilePrefix : "";
    }

    protected String getTabletPrefix() {
        return this.tabletPrefix;
    }

    public void setTabletPrefix(String tabletPrefix) {
        this.tabletPrefix = tabletPrefix != null ? tabletPrefix : "";
    }

    protected String getNormalSuffix() {
        return this.normalSuffix;
    }

    public void setNormalSuffix(String normalSuffix) {
        this.normalSuffix = normalSuffix != null ? normalSuffix : "";
    }

    protected String getMobileSuffix() {
        return this.mobileSuffix;
    }

    public void setMobileSuffix(String mobileSuffix) {
        this.mobileSuffix = mobileSuffix != null ? mobileSuffix : "";
    }

    protected String getTabletSuffix() {
        return this.tabletSuffix;
    }

    public void setTabletSuffix(String tabletSuffix) {
        this.tabletSuffix = tabletSuffix != null ? tabletSuffix : "";
    }

    protected String getDeviceViewNameInternal(String viewName) {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        Assert.isInstanceOf(ServletRequestAttributes.class, attrs);
        HttpServletRequest request = ((ServletRequestAttributes) attrs).getRequest();
        Device device = DeviceUtils.getCurrentDevice(request);
        SitePreference sitePreference = SitePreferenceUtils.getCurrentSitePreference(request);
        String resolvedViewName = viewName;
        if (ResolverUtils.isNormal(device, sitePreference)) {
            resolvedViewName = this.getNormalPrefix() + viewName + this.getNormalSuffix();
        } else if (ResolverUtils.isMobile(device, sitePreference)) {
            resolvedViewName = this.getMobilePrefix() + viewName + this.getMobileSuffix();
        } else if (ResolverUtils.isTablet(device, sitePreference)) {
            resolvedViewName = this.getTabletPrefix() + viewName + this.getTabletSuffix();
        }

        return this.stripTrailingSlash(resolvedViewName);
    }

    private String stripTrailingSlash(String viewName) {
        return viewName.endsWith("//") ? viewName.substring(0, viewName.length() - 1) : viewName;
    }
}
