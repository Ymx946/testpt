package com.mz.service.mobile;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mz.common.context.PageInfo;
import com.mz.model.mobile.TabMobileBaseModule;
import com.mz.model.mobile.model.TabMobileBaseModuleModel;
import com.mz.model.mobile.model.TabMobileBaseModuleModels;
import com.mz.model.mobile.vo.TabMobileBaseModuleVO;

import java.util.List;


/**
 * 移动组件表(TabMobileBaseModule)表服务接口
 *
 * @author makejava
 * @since 2022-11-21 16:59:11
 */
public interface TabMobileBaseModuleService extends IService<TabMobileBaseModule> {
    /**
     * 保存
     */
    TabMobileBaseModule insert(TabMobileBaseModule pojo, String loginID, String styleNameS);

    /**
     * 模块详情
     */
    PageInfo<TabMobileBaseModule> queryAllByLimit(TabMobileBaseModuleVO vo);

    /**
     * 根据id查询
     */
    TabMobileBaseModuleModel queryById(Long id);

    List<TabMobileBaseModuleModels> queryModule(TabMobileBaseModule vo);
}
