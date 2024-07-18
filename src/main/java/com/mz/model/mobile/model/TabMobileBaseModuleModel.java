package com.mz.model.mobile.model;


import com.mz.model.mobile.TabMobileBaseModule;
import com.mz.model.mobile.TabMobileBaseModuleStyle;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 移动组件表(TabMobileBaseModule)实体类
 */
@Setter
@Getter
@ToString
public class TabMobileBaseModuleModel extends TabMobileBaseModule {
    /**
     * 模块列表
     */
    private List<TabMobileBaseModuleStyle> tabMobileBaseModuleStyleList;
}
