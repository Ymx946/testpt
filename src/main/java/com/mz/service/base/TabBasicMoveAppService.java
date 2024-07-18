package com.mz.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mz.common.context.PageInfo;
import com.mz.model.base.TabBasicMoveApp;
import com.mz.model.base.vo.TabBasicMoveAppVO;

/**
 * 基础移动应用表(TabBasicMoveApp)表服务接口
 *
 * @author makejava
 * @since 2022-12-29 09:22:40
 */
public interface TabBasicMoveAppService extends IService<TabBasicMoveApp> {
    /**
     * 保存
     */
    TabBasicMoveApp insert(TabBasicMoveApp pojo, String loginID);

    /**
     * 列表
     */
    PageInfo<TabBasicMoveApp> queryAllByLimit(TabBasicMoveAppVO vo);
}
