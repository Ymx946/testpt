package com.mz.mapper.localhost;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mz.model.base.SysArea;
import com.mz.model.base.vo.SysAreaVO;
import com.mz.model.base.vo.SysFullAreaVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 区域表(SysArea)表数据库访问层
 *
 * @author makejava
 * @since 2021-03-17 10:58:49
 */
public interface SysAreaMapper extends BaseMapper<SysArea> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysArea queryById(String id);

    /**
     * 通过areaCode查询单条数据
     *
     * @param areaCode 主键
     * @return 实例对象
     */
    SysArea queryByCode(@Param("areaCode") String areaCode);

    /**
     * 村查询-村/社区排序
     *
     * @return 实例对象
     */
    List<SysArea> queryVillageList(@Param("areaCode") String areaCode);

    /**
     * 查询所有
     *
     * @param len 区域代码长度
     * @return 实例对象
     */
    List<SysAreaVO> queryAllByLen(@Param("len") Integer len);

    /**
     * 查询所有
     *
     * @param paraCode 上级
     * @return 实例对象
     */
    List<SysAreaVO> queryAllByCode(@Param("paraCode") String paraCode);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<SysArea> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 查询指定行数据
     *
     * @param paraCode 查询起始位置
     * @param len      查询条数
     * @return 对象列表
     */
    List<SysArea> queryAreaList(@Param("paraCode") String paraCode, @Param("len") Integer len);

    /**
     * 查询指定行数据
     *
     * @param paraCode 查询起始位置
     * @param len      查询条数
     * @return 对象列表
     */
    List<SysAreaVO> queryAreaListAndSon(@Param("paraCode") String paraCode, @Param("len") Integer len, @Param("maxLen") Integer maxLen);

    /**
     * 查询区域代码查看全区域名称
     *
     * @param areaCode 区域d代码
     * @return 对象列表
     */
    SysFullAreaVO getFullAreaNameByAreaCode(String areaCode);

    /**
     * 根据名称查询指定行数据
     *
     * @param paraCode 查询起始位置
     * @param len      查询条数
     * @return 对象列表
     */
    SysArea queryAreaByName(@Param("paraCode") String paraCode, @Param("areaName") String areaName, @Param("len") Integer len);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param sysArea 实例对象
     * @return 对象列表
     */
    List<SysArea> queryAll(SysArea sysArea);

    List<SysArea> queryAllByAreaCode(SysArea sysArea);

    /**
     * 新增数据
     *
     * @param sysArea 实例对象
     * @return 影响行数
     */
    int insert(SysArea sysArea);

    /**
     * 修改数据
     *
     * @param sysArea 实例对象
     * @return 影响行数
     */
    int update(SysArea sysArea);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

    /**
     * 通过区域名称查询单条数据
     *
     * @param provinceName 省
     * @param cityName     市
     * @param areaName     区
     * @return 实例对象
     */
    SysArea queryByAreaName(@Param("provinceName") String provinceName, @Param("cityName") String cityName, @Param("areaName") String areaName);

    /**
     * 通过区域名称查询单条数据
     *
     * @param paraCode 区域代码（越长越准确）
     * @param areaName 区
     * @return 实例对象
     */
    SysArea queryByParaAndName(@Param("paraCode") String paraCode, @Param("areaName") String areaName, @Param("areaLevel") Integer areaLevel);

}