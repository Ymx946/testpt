package com.mz.framework.util.weixin;

import java.io.Serializable;

public class NameValuePair implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String name;
    private String value;

    public NameValuePair(String name, String value) {
        super();
        this.name = name;
        this.value = value;
    }

    public NameValuePair() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
