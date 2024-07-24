package com.mz.framework.mobile.device.site;


public enum SitePreference {
    NORMAL {
        public boolean isNormal() {
            return true;
        }
    },
    MOBILE {
        public boolean isMobile() {
            return true;
        }
    },
    TABLET {
        public boolean isTablet() {
            return true;
        }
    };

    SitePreference() {
    }

    public boolean isNormal() {
        return !this.isMobile() && !this.isTablet();
    }

    public boolean isMobile() {
        return false;
    }

    public boolean isTablet() {
        return false;
    }
}
