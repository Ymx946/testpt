package com.mz.mapper.localhost;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mz.model.base.SysOperaLog;
import com.mz.model.base.vo.SysOperaLogVo;

import java.util.List;

/**
 * 操作日志表(SysOperaLog)表数据库访问层
 *
 * @author makejava
 * @since 2021-03-17 11:00:37
 */
public interface SysOperaLogMapper extends BaseMapper<SysOperaLog> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysOperaLog queryById(String id);

    /**
     * 查询指定行数据
     *
     * @return 对象列表
     */
    List<SysOperaLogVo> queryAllByLimit(SysOperaLogVo vo);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param sysOperaLog 实例对象
     * @return 对象列表
     */
    List<SysOperaLog> queryAll(SysOperaLog sysOperaLog);

    /**
     * 新增数据
     *
     * @param sysOperaLog 实例对象
     * @return 影响行数
     */
    int insert(SysOperaLog sysOperaLog);

    /**
     * 修改数据
     *
     * @param sysOperaLog 实例对象
     * @return 影响行数
     */
    int update(SysOperaLog sysOperaLog);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

}