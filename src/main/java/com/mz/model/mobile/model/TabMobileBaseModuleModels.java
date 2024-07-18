package com.mz.model.mobile.model;


import com.mz.model.mobile.TabMobileBaseModule;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 区域组件表(TabMobileManageModule)实体类
 */
@Setter
@Getter
@ToString
public class TabMobileBaseModuleModels {
    /**
     * 分类1-基础组件2-业务组件
     */
    private Integer moduleType;
    /**
     * 模块信息集合
     */
    private List<TabMobileBaseModule> tabMobileBaseModuleList;
}
