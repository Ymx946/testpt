package com.mz.model.base.model;

import com.mz.model.base.BaseSiteBatteryPack;
import com.mz.model.base.BaseSiteHost;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 站点主机信息表(BaseSiteHost)实体类
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class BaseSiteHostModel extends BaseSiteHost {
    /**
     * 下级电池组集合
     */
    private List<BaseSiteBatteryPack> batteryPackList;

}
