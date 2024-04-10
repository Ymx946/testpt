package com.mz.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mz.common.context.PageInfo;
import com.mz.common.util.Result;
import com.mz.model.base.TabBasicColumn;
import com.mz.model.base.vo.TabBasicColumnVO;

import java.util.List;

/**
 * 基础栏目表(TabBasicColumn)表服务接口
 *
 * @author makejava
 * @since 2022-12-28 09:52:42
 */
public interface TabBasicColumnService extends IService<TabBasicColumn>{
    /**
     * 保存
     *
     */
    Result insert(TabBasicColumn pojo, String loginID);

    /**
     * 查询全部
     * @param vo
     * @return
     */
    List<TabBasicColumn> queryAll(TabBasicColumnVO vo);
    /**
     * 根据栏目类型查询子栏目
     * @param vo
     * @return
     */
    List<TabBasicColumn> querySonByType(TabBasicColumnVO vo);
    /**
     * 按代码查询
     */
    TabBasicColumn queryByCode(TabBasicColumnVO vo);
    /**
     * 列表
     */
    PageInfo<TabBasicColumn> queryAllByLimit(TabBasicColumnVO vo);
}
