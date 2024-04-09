package com.mz.service.base;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mz.common.context.PageInfo;
import com.mz.common.util.Result;
import com.mz.model.base.SysArea;
import com.mz.model.base.vo.SysAreaLimitVO;
import com.mz.model.base.vo.SysAreaVO;
import com.mz.model.base.vo.SysFullAreaVO;

import java.util.List;
import java.util.Map;

/**
 * 区域表(SysArea)表服务接口
 *
 * @author makejava
 * @since 2021-03-17 10:58:49
 */
public interface SysAreaService extends IService<SysArea> {

    List<SysArea> queryAllByAreaCode(SysArea sysArea);

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysArea queryById(String id);

    /**
     * 通过ID查询单条数据
     *
     * @return 实例对象
     */
    SysArea queryByCode(String areaCode);

    /**
     * 获取名称map
     *
     * @return 实例对象
     */
    Map<String ,SysArea> queryNameMap(String areaCode, Integer len);

    /**
     * 获取code map
     *直接数据库获取
     * @return 实例对象
     */
    Map<String ,SysArea> queryCodeMap(String areaCode, Integer len);

    /**
     * 查缓存列表
     *
     * @return
     */
    Map<String, String> getSysAreaCacheMap(SysArea sysArea);

    /**
     * 通过ID查询单条数据
     *
     * @return 实例对象
     */
    SysArea queryAreaByName(String paraCode, String areaName, Integer len);

    /**
     * 通过ID查询单条数据
     *
     * @return 实例对象
     */
    SysArea queryByCodeAndLen(String areaCode, Integer len);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<SysArea> queryAllByLimit(int offset, int limit);

    /**
     * 查询多条数据
     *
     * @return 对象列表
     */
    List<SysArea> queryAreaList(String paraCode, Integer len);

    /**
     * 根据上级查询（有村的下级）
     *
     * @return 对象列表
     */
    List<SysArea> queryAreaListForVillage(String paraCode, Integer len);

    /**
     * 查询区域(所有层级)
     *
     * @return 对象列表
     */
    List<SysAreaVO> queryAllByCode(String paraCode, Integer len);

    /**
     * 查询区域(所有层级--有村的上级)
     *
     * @return 对象列表
     */
    List<SysAreaVO> queryAllByCodeForVillage(String paraCode);

    /**
     * 查询区域分层机
     *
     * @return 对象列表
     */
    List<SysAreaVO> queryAreaListAndSon(String paraCode, Integer maxLen);

    /**
     * 查询区域分层机
     *
     * @return 对象列表
     */
    List<SysAreaVO> queryListSelfAndSon(String paraCode, Integer maxLen);

    /**
     * 村查询-村/社区排序
     *
     * @return 对象列表
     */
    List<SysArea> queryVillageList(String paraCode);

    /**
     * 查询区域代码查看全区域名称
     *
     * @return 对象列表
     */
    SysFullAreaVO getFullAreaNameByAreaCode(String areaCode);

    /**
     * 查询区域代码查看全区域名称(拼接字符串)
     *
     * @return 对象列表
     */
    String getFullAreaName(String areaCode);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(String id);

    /**
     * 通过区域名称查询单条数据
     *
     * @param provinceName 省
     * @param cityName     市
     * @param areaName     区
     * @return 实例对象
     */
    SysArea queryByAreaName(String provinceName, String cityName, String areaName);

    /**
     * 通过区域名称查询单条数据
     *
     * @param paraCode 区域代码（越长越准确）
     * @param areaName 区
     * @return 实例对象
     */
    SysArea queryByParaAndName(String paraCode, String areaName, Integer areaLevel);

    /**
     * 新增
     * @param pojo
     * @param loginID
     * @return
     */
    Result insert(SysArea pojo, String loginID);
    /**
     * 查询所有
     * @param vo
     * @return
     */
    List<SysArea> queryAll(SysAreaLimitVO vo);

    /**
     * 分页列表
     * @param vo
     * @return
     */
    PageInfo<SysArea> queryAllByLimitNew(SysAreaLimitVO vo);

    Map<String, SysArea> queryAreaMap(SysAreaLimitVO vo);


}