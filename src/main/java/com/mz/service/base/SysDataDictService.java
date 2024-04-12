package com.mz.service.base;


import com.mz.common.context.PageInfo;
import com.mz.common.util.Result;
import com.mz.model.base.SysDataDict;
import com.mz.model.base.vo.SysDataDictSubVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 系统数据字典(SysDataDict)表服务接口
 *
 * @author makejava
 * @since 2021-03-17 10:58:51
 */
public interface SysDataDictService {

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
    SysDataDict queryByCode(String dictTypeCode, String dictCode);

    /**
     * 通过代码查询单条数据
     *
     * @param dictTypeCode 类型代码
     * @param dictCode     字典代码
     * @return 实例对象
     */
    SysDataDict queryByCodeWithArea(String areaCode, String dictTypeCode, String dictCode);

    /**
     * 根据类型和上级代码查询
     *
     * @param dictTypeCode 类型代码
     * @return 对象列表
     */
    List<SysDataDict> querySubByMajor(String dictTypeCode, String dictCode);

    /**
     * 根据类型查询多条数据
     *
     * @param dictTypeCode 类型代码
     * @param dictTypeName 类型名称
     * @return 对象列表
     */
    List<SysDataDict> queryAll(String areaCode, String dictTypeCode, String dictTypeName, String dictName);

    /**
     * 查询数据字典
     *
     * @return map  key = DictTypeCode+DictCode
     */
    Map<String, String> queryAllMap();

    /**
     * 查询多条数据
     *
     * @return map  key =DictTypeCode+DictName
     */
    Map<String, String> queryAreaNameForCodeMap();

    /**
     * 查询数据字典大类带小类
     *
     * @param dictTypeCodeMajor 大类数据字典类型代码
     * @param dictTypeCodeSub   小类数据类型代码
     * @return 对象列表
     */
    List<SysDataDictSubVO> queryAllWithSub(String dictTypeCodeMajor, String dictTypeCodeSub);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    PageInfo<SysDataDict> queryAllByLimit(int offset, int limit, String areaCode, String dictTypeCode, String dictTypeName, String dictName,Integer useState,String startTime,String endTime);

    /**
     * 新增数据
     *
     * @param sysDataDict 实例对象
     * @return 实例对象
     */
    Result insert(SysDataDict sysDataDict, HttpServletRequest request);

    /**
     * 修改数据
     *
     * @param sysDataDict 实例对象
     * @return 实例对象
     */
    SysDataDict update(SysDataDict sysDataDict);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(String id);

    /**
     * 根据用章类型查询不同的数据字典
     */
    List<SysDataDict> queryAllBySealType(SysDataDict pojo, String loginID, Integer sealType);

    /**
     * 根据跳转方式查询不同的数据字典
     */
    List<SysDataDict> queryAllByJumpWay(SysDataDict pojo, Integer jumpWay);
    List<SysDataDict> queryAllByName(SysDataDict vo);

}