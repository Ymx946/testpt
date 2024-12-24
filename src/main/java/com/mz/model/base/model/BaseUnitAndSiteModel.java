package com.mz.model.base.model;

import com.mz.model.base.BaseSiteInformation;
import com.mz.model.base.BaseUnitInformation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 单位信息表(BaseUnitInformation)实体类
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class BaseUnitAndSiteModel extends BaseUnitInformation {
    /**
     * 站点信息
     */
    private List<BaseSiteInformation> siteList;

}
