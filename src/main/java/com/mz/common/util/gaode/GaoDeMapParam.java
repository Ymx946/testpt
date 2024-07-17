package com.mz.common.util.gaode;

import java.io.Serializable;
import java.util.List;

/**
 * 批量查询 高德地图 输入参数
 *
 * @Description:
 * @author: yangh
 * @date: 2020年3月20日 下午9:21:34
 */
public class GaoDeMapParam implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6793781402034843997L;

    private List<SubGaoDeMapParam> ops;

    public List<SubGaoDeMapParam> getOps() {
        return ops;
    }

    public void setOps(List<SubGaoDeMapParam> ops) {
        this.ops = ops;
    }

}
