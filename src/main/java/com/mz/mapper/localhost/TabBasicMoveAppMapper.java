package com.mz.mapper.localhost;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mz.model.base.TabBasicMoveApp;
import com.mz.model.base.vo.TabBasicMoveAppVO;

import java.util.List;

/**
 * 基础移动应用表(TabBasicMoveApp)表数据库访问层
 *
 * @author makejava
 * @since 2022-12-29 09:22:23
 */
public interface TabBasicMoveAppMapper extends BaseMapper<TabBasicMoveApp> {
    /**
     * 查询所有
     */
    List<TabBasicMoveApp> queryAll(TabBasicMoveAppVO vo);
}
