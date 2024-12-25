package com.mz.model.base.model;

import com.mz.model.base.BaseSiteHost;
import com.mz.model.base.BaseSiteInformation;
import com.mz.model.base.BaseUnitInformation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 站点信息表(BaseSiteInformation)实体类
 */
@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class BaseSiteInformationModel extends BaseSiteInformation {
    /**
     * 下级电机集合
     */
    private List<BaseSiteHost> hostList;

}
