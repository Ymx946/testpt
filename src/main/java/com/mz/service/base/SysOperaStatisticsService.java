package com.mz.service.base;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mz.common.context.PageInfo;
import com.mz.model.base.SysOperaStatistics;
import com.mz.model.base.vo.SysOperaStatisticsVO;

import java.util.List;

/**
 * 操作统计表(SysOperaStatistics)表服务接口
 *
 * @author makejava
 * @since 2024-05-11 10:52:17
 */
public interface SysOperaStatisticsService extends IService<SysOperaStatistics> {
    /**
     * 保存
     */
    SysOperaStatistics insert(SysOperaStatistics pojo);

    SysOperaStatistics insert(SysOperaStatistics pojo, String loginID);


    Long queryAllCount(SysOperaStatisticsVO vo);

    /**
     * 查询所有
     */
    List<SysOperaStatistics> queryAll(SysOperaStatisticsVO vo);

    /**
     * 分页列表
     *
     * @param vo
     * @return
     */
    PageInfo<SysOperaStatistics> queryAllByLimit(SysOperaStatisticsVO vo);


}
