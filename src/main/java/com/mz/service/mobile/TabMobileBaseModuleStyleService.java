package com.mz.service.mobile;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mz.model.mobile.TabMobileBaseModuleStyle;
import com.mz.model.mobile.vo.TabMobileBaseModuleStyleVO;

import java.util.List;

/**
 * 移动组件样式表(TabMobileBaseModuleStyle)表服务接口
 *
 * @author makejava
 * @since 2022-11-21 16:59:33
 */
public interface TabMobileBaseModuleStyleService extends IService<TabMobileBaseModuleStyle> {
    /**
     * 保存
     */
    List<TabMobileBaseModuleStyle> insert(Long moduleId, String styleJson);

    /**
     * 编辑
     */
    TabMobileBaseModuleStyle update(TabMobileBaseModuleStyle pojo);

    /**
     * 列表
     */
    List<TabMobileBaseModuleStyle> queryAll(TabMobileBaseModuleStyleVO vo);
}
