package com.mz.common.model.seal.model;

import com.mz.common.model.seal.TabElectronicSeal;
import com.mz.common.model.seal.TabSealUserInformation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author mx
 * @date 2023/3/17
 */
@Setter
@Getter
@ToString
public class TabElectronicSealModel extends TabElectronicSeal {
    /**
     * 使用人信息集合
     */
    private List<TabSealUserInformation> tabActivitiUserInformationList;
}
