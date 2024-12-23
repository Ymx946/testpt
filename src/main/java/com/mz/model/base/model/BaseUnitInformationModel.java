package com.mz.model.base.model;

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
public class BaseUnitInformationModel extends BaseUnitInformation {
    /**
     * 下层单位集合
     */
    private List<BaseUnitInformation> childrenList;

}
