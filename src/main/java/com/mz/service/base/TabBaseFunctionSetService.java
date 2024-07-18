package com.mz.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mz.common.context.PageInfo;
import com.mz.common.util.Result;
import com.mz.model.base.TabBaseFunctionSet;
import com.mz.model.base.vo.TabBaseFunctionSetVO;

import java.util.List;

/**
 * 功能设置基础表(TabBaseFunctionSet)表服务接口
 *
 * @author makejava
 * @since 2022-12-12 11:32:34
 */
public interface TabBaseFunctionSetService extends IService<TabBaseFunctionSet> {
    /**
     * 保存
     */
    Result insert(TabBaseFunctionSet pojo, String loginID);

    /**
     * 查询所有
     */
    List<TabBaseFunctionSet> queryAll(TabBaseFunctionSetVO vo);

    /**
     * 列表
     */
    PageInfo<TabBaseFunctionSet> queryAllByLimit(TabBaseFunctionSetVO vo);
}
