package com.mz.service.base.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.mz.common.ConstantsUtil;
import com.mz.common.context.PageInfo;
import com.mz.common.util.IdWorker;
import com.mz.common.util.Result;
import com.mz.mapper.localhost.SysDataDictMapper;
import com.mz.model.base.BaseUser;
import com.mz.model.base.SysDataDict;
import com.mz.model.base.vo.SysDataDictSubVO;
import com.mz.service.base.BaseUserService;
import com.mz.service.base.SysDataDictService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统数据字典(SysDataDict)表服务实现类
 *
 * @author makejava
 * @since 2021-03-17 10:58:51
 */
@Service("sysDataDictService")
public class SysDataDictServiceImpl extends ServiceImpl<SysDataDictMapper, SysDataDict> implements SysDataDictService {
    @Resource
    private SysDataDictMapper sysDataDictMapper;
    @Resource
    private BaseUserService baseUserService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SysDataDict queryById(String id) {
        return this.sysDataDictMapper.queryById(id);
    }

    /**
     * 通过代码查询单条数据
     *
     * @param dictTypeCode 类型代码
     * @param dictCode     字典代码
     * @return 实例对象
     */
    @Override
    public SysDataDict queryByCode(String dictTypeCode, String dictCode) {
        return this.sysDataDictMapper.queryByCode(dictTypeCode, dictCode);
    }

    /**
     * 通过代码查询单条数据
     *
     * @param areaCode     区域代码
     * @param dictTypeCode 类型代码
     * @param dictCode     字典代码
     * @return 实例对象
     */
    @Override
    public SysDataDict queryByCodeWithArea(String areaCode, String dictTypeCode, String dictCode) {
        return this.sysDataDictMapper.queryByCodeWithArea(areaCode, dictTypeCode, dictCode);
    }

    /**
     * 根据类型和上级代码查询
     *
     * @param dictTypeCode 类型代码
     * @return 对象列表
     */
    @Override
    public List<SysDataDict> querySubByMajor(String dictTypeCode, String dictCode) {
        return this.sysDataDictMapper.querySubByMajor(dictTypeCode, dictCode);
    }

    /**
     * 查询多条数据
     *
     * @param dictTypeCode 类型代码
     * @return 对象列表
     */
    @Override
    public List<SysDataDict> queryAll(String areaCode, String dictTypeCode, String dictTypeName, String dictName) {
        SysDataDict findSysDataDict = new SysDataDict();
        findSysDataDict.setAreaCode(areaCode);
        findSysDataDict.setDictTypeCode(dictTypeCode);
        findSysDataDict.setDictTypeName(dictTypeName);
        findSysDataDict.setDictName(dictName);
        findSysDataDict.setUseState(1);// 只查询可用状态的数据
        return this.sysDataDictMapper.queryAll(findSysDataDict);
    }

    /**
     * 查询多条数据
     *
     * @return map  key = DictTypeCode+DictCode
     */
    @Override
    public Map<String, String> queryAllMap() {
        Map<String, String> map = new HashMap<String, String>();
        List<SysDataDict> sysDataDictList = this.sysDataDictMapper.queryAll(new SysDataDict());
        for (SysDataDict sysDataDict : sysDataDictList) {
            map.put(sysDataDict.getDictTypeCode() + sysDataDict.getDictCode(), sysDataDict.getDictName());
        }
        return map;
    }

    /**
     * 查询多条数据
     *
     * @return map  key = AreaCode+DictTypeCode+DictName
     */
    @Override
    public Map<String, String> queryAreaNameForCodeMap() {
        Map<String, String> map = new HashMap<String, String>();
        List<SysDataDict> sysDataDictList = this.sysDataDictMapper.queryAll(new SysDataDict());
        for (SysDataDict sysDataDict : sysDataDictList) {
            map.put(sysDataDict.getAreaCode() + sysDataDict.getDictTypeCode() + sysDataDict.getDictName(), sysDataDict.getDictTypeCode() + sysDataDict.getDictCode());
        }
        return map;
    }

    /**
     * 查询数据字典大类带小类
     *
     * @param dictTypeCodeMajor 大类数据字典类型代码
     * @param dictTypeCodeSub   小类数据类型代码
     * @return 对象列表
     */
    @Override
    public List<SysDataDictSubVO> queryAllWithSub(String dictTypeCodeMajor, String dictTypeCodeSub) {
        List<SysDataDictSubVO> sysDataDictSubVOS = null;
        SysDataDict findSysDataDict = new SysDataDict();
        findSysDataDict.setDictTypeCode(dictTypeCodeMajor);
        findSysDataDict.setUseState(1);// 只查询可用状态的数据
        List<SysDataDict> sysDataDicts = this.sysDataDictMapper.queryAll(findSysDataDict);
        if (sysDataDicts != null && sysDataDicts.size() > 0) {
            sysDataDictSubVOS = new ArrayList<SysDataDictSubVO>();
            for (SysDataDict dataDict : sysDataDicts) {
                SysDataDictSubVO newSysDataDictSubVO = new SysDataDictSubVO();
                BeanUtil.copyProperties(dataDict, newSysDataDictSubVO);
                List<SysDataDict> sublist = this.sysDataDictMapper.querySubByMajor(dictTypeCodeSub, dataDict.getDictCode());
                newSysDataDictSubVO.setSubList(sublist);
                sysDataDictSubVOS.add(newSysDataDictSubVO);
            }
        }
        return sysDataDictSubVOS;
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public PageInfo<SysDataDict> queryAllByLimit(int offset, int limit, String areaCode, String dictTypeCode, String dictTypeName, String dictName, Integer useState, String startTime, String endTime) {
        PageHelper.startPage(offset, limit);
        List<SysDataDict> list = sysDataDictMapper.queryAllByLimits(offset, limit, areaCode, dictTypeCode, dictTypeName, dictName, useState, startTime, endTime);
        return new PageInfo<SysDataDict>(list);
    }

    @Override
    public PageInfo<SysDataDict> queryAllByLimits(int offset, int limit, String areaCode, String dictTypeCode, String dictTypeName, String dictName, Integer useState, String startTime, String endTime) {
        PageHelper.startPage(offset, limit);
        List<SysDataDict> list = sysDataDictMapper.queryAllByLimit(offset, limit, areaCode, dictTypeCode, dictTypeName, dictName, useState, startTime, endTime);
        return new PageInfo<SysDataDict>(list);
    }

    /**
     * 新增数据
     *
     * @param sysDataDict 实例对象
     * @return 实例对象
     */
    @Override
    public Result insert(SysDataDict sysDataDict, HttpServletRequest request) {
        BaseUser baseUser = baseUserService.getUser(request);
        sysDataDict.setModifyTime(DateUtil.now());
        sysDataDict.setModifyUser(baseUser.getRealName());
        if (sysDataDict.getId() != null) {
            SysDataDict findSysDataDict = new SysDataDict();
            findSysDataDict.setDictTypeCode(sysDataDict.getDictTypeCode());
            findSysDataDict.setDictCode(sysDataDict.getDictCode());
            findSysDataDict.setId(sysDataDict.getId());
            List<SysDataDict> nameDeftList = sysDataDictMapper.queryAll(findSysDataDict);
            if (nameDeftList != null && nameDeftList.size() > 0) {
                return Result.failed("该类型数据字典代码重复");
            }
            SysDataDict find = new SysDataDict();
            find.setDictTypeCode(sysDataDict.getDictTypeCode());
            find.setDictName(sysDataDict.getDictName());
            find.setId(sysDataDict.getId());
            List<SysDataDict> list = queryAllByName(find);
            if (CollectionUtil.isNotEmpty(list)) {
                return Result.failed("该类型数据字典名称重复");
            }
            this.sysDataDictMapper.updateModify(sysDataDict);
            return Result.success(sysDataDictMapper.queryById(sysDataDict.getId()));

        } else {
            SysDataDict findSysDataDict = new SysDataDict();
            findSysDataDict.setDictTypeCode(sysDataDict.getDictTypeCode());
            findSysDataDict.setDictCode(sysDataDict.getDictCode());
            List<SysDataDict> nameDeftList = sysDataDictMapper.queryAll(findSysDataDict);
            if (nameDeftList != null && nameDeftList.size() > 0) {
                return Result.failed("数据字典代码重复");
            }
            SysDataDict find = new SysDataDict();
            find.setDictTypeCode(sysDataDict.getDictTypeCode());
            find.setDictName(sysDataDict.getDictName());
            find.setId(sysDataDict.getId());
            List<SysDataDict> list = queryAllByName(find);
            if (CollectionUtil.isNotEmpty(list)) {
                return Result.failed("该类型数据字典名称重复");
            }
            IdWorker idWorker = new IdWorker(0L, 0L);
            long newId = idWorker.nextId();
            sysDataDict.setId(String.valueOf(newId));
            sysDataDict.setCreateUser(baseUser.getRealName());
            sysDataDict.setCreateTime(DateUtil.now());
            sysDataDict.setUseState(1);
            this.sysDataDictMapper.insert(sysDataDict);
            return Result.success(sysDataDict);
        }
    }

    @Override
    public List<SysDataDict> queryAllByName(SysDataDict vo) {
        LambdaQueryChainWrapper<SysDataDict> lambdaQuery = lambdaQuery();
        lambdaQuery.eq(SysDataDict::getUseState, ConstantsUtil.IS_DONT_DEL);
        if (ObjectUtil.isNotEmpty(vo.getId())) {
            lambdaQuery.ne(SysDataDict::getId, vo.getId());
        }
        if (ObjectUtil.isNotEmpty(vo.getDictTypeCode())) {
            lambdaQuery.eq(SysDataDict::getDictTypeCode, vo.getDictTypeCode());
        }
        if (ObjectUtil.isNotEmpty(vo.getDictName())) {
            lambdaQuery.eq(SysDataDict::getDictName, vo.getDictName());
        }
        List<SysDataDict> roomList = lambdaQuery.orderByDesc(SysDataDict::getDictCode).list();
        return roomList;
    }

    /**
     * 修改数据
     *
     * @param sysDataDict 实例对象
     * @return 实例对象
     */
    @Override
    public SysDataDict update(SysDataDict sysDataDict) {
        this.sysDataDictMapper.update(sysDataDict);
        return this.queryById(sysDataDict.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String id) {
        return this.sysDataDictMapper.deleteById(id) > 0;
    }


    @Override
    public List<SysDataDict> queryAllBySealType(SysDataDict pojo, String loginID, Integer sealType) {
        pojo.setUseState(1);
        List<SysDataDict> list = new ArrayList<>();
        if (sealType.intValue() == 1) {
            pojo.setDictTypeCode("VILLAGE_SEAL_TYPE");
            list = sysDataDictMapper.queryAll(pojo);
        }
        if (sealType.intValue() == 2) {
            pojo.setDictTypeCode("VILLAGE_PARTY_SEAL_TYPE");
            list = sysDataDictMapper.queryAll(pojo);
        }
        return list;
    }

    @Override
    public List<SysDataDict> queryAllByJumpWay(SysDataDict pojo, Integer jumpWay) {
        pojo.setUseState(1);
        List<SysDataDict> list = new ArrayList<>();
        if (jumpWay.intValue() == 1) {
            pojo.setDictTypeCode("BOTTOM_NAVIGATION_DEFAULT");
            list = sysDataDictMapper.queryAll(pojo);
        }
        if (jumpWay.intValue() == 2) {
            pojo.setDictTypeCode("BOTTOM_NAVIGATION_TAB");
            list = sysDataDictMapper.queryAll(pojo);
        }
        return list;
    }
}