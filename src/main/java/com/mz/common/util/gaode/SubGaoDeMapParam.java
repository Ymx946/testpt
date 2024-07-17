package com.mz.common.util.gaode;

import java.io.Serializable;

/**
 * 批量查询 高德地图 自输入参数
 *
 * @Description:
 * @author: yangh
 * @date: 2020年3月20日 下午9:22:04
 */
public class SubGaoDeMapParam implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -8907436137188116798L;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
