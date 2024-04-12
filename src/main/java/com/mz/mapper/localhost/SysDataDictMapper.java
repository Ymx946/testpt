package com.mz.mapper.localhost;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mz.model.base.SysDataDict;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统数据字典(SysDataDict)表数据库访问层
 *
 * @author makejava
 * @since 2021-03-17 10:58:51
 */
public interface SysDataDictMapper extends BaseMapper<SysDataDict> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysDataDict queryById(String id);

    /**
     * 通过代码查询单条数据
     *
     * @param dictTypeCode 类型代码
     * @param dictCode     字典代码
     * @return 实例对象
     */
    SysDataDict queryByCode(@Param("dictTypeCode") String dictTypeCode, @Param("dictCode") String dictCode);

    /**
     * 通过代码查询单条数据
     *
     * @param dictTypeCode 类型代码
     * @param dictCode     字典代码
     * @return 实例对象
     */
    SysDataDict queryByCodeWithArea(String areaCode, @Param("dictTypeCode") String dictTypeCode, @Param("dictCode") String dictCode);

    /**
     * 通过代码查询单条数据
     *
     * @param dictTypeCode 类型代码
     * @param dictName     字典名称
     * @return 实例对象
     */
    SysDataDict queryByName(@Param("dictTypeCode") String dictTypeCode, @Param("dictName") String dictName);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<SysDataDict> queryAllByLimit(@Param("offset") int offset,
                                      @Param("limit") int limit,
                                      @Param("areaCode") String areaCode,
                                      @Param("dictTypeCode") String dictTypeCode,
                                      @Param("dictTypeName") String dictTypeName,
                                      @Param("dictName") String dictName,
                                      @Param("useState") Integer useState,
                                      @Param("startTime") String startTime,
                                      @Param("endTime") String endTime);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param sysDataDict 实例对象
     * @return 对象列表
     */
    List<SysDataDict> queryAll(SysDataDict sysDataDict);

    /**
     * 通过大类查找小类
     *
     * @param dictTypeCode 数据字典类型代码
     * @param dictCode     数据代码
     * @return 对象列表
     */
    List<SysDataDict> querySubByMajor(@Param("dictTypeCode") String dictTypeCode, @Param("dictCode") String dictCode);

    /**
     * 新增数据
     *
     * @param sysDataDict 实例对象
     * @return 影响行数
     */
    int insert(SysDataDict sysDataDict);

    /**
     * 修改数据
     *
     * @param sysDataDict 实例对象
     * @return 影响行数
     */
    int update(SysDataDict sysDataDict);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

}