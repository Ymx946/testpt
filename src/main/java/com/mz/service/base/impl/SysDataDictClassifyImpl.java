package com.mz.service.base.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.ConstantsUtil;
import com.mz.common.util.IdWorker;
import com.mz.common.util.Result;
import com.mz.framework.util.redis.RedisUtil;
import com.mz.mapper.localhost.SysDataDictClassifyMapper;
import com.mz.mapper.localhost.SysDataDictMapper;
import com.mz.model.base.BaseUser;
import com.mz.model.base.SysDataDict;
import com.mz.model.base.SysDataDictClassify;
import com.mz.model.base.model.SysDataDictClassifyModel;
import com.mz.model.base.vo.SysDataDictClassifyVO;
import com.mz.service.base.SysDataDictClassifyService;
import com.mz.service.base.SysDataDictService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统数据字典分类(SysDataDictClassify)表服务实现类
 *
 * @author makejava
 * @since 2024-04-08 18:06:51
 */
@Service("sysDataDictClassifyService")
public class SysDataDictClassifyImpl extends ServiceImpl<SysDataDictClassifyMapper, SysDataDictClassify> implements SysDataDictClassifyService {
    @Autowired
    public SysDataDictMapper sysDataDictMapper;
    @Autowired
    public SysDataDictService sysDataDictService;
    @Autowired
    private RedisUtil myRedisUtil;

    @Override
    public Result insert(SysDataDictClassify pojo, String loginID) {
        String baseUserStr = myRedisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginID);
        JSONObject baseUserJson = JSONObject.parseObject(baseUserStr);
        BaseUser baseUser = JSONObject.toJavaObject(baseUserJson, BaseUser.class);
        SysDataDictClassifyVO find = new SysDataDictClassifyVO();
        if (ObjectUtil.isNotEmpty(pojo.getId())) {
            find.setId(pojo.getId());
        }
        find.setDictTypeCode(pojo.getDictTypeCode());
        List<SysDataDictClassify> codeList = queryAllByCode(find);
        if (CollectionUtil.isNotEmpty(codeList)) {
            return Result.failed("该类型代码已存在");
        }
        SysDataDictClassifyVO find1 = new SysDataDictClassifyVO();
        if (ObjectUtil.isNotEmpty(pojo.getId())) {
            find1.setId(pojo.getId());
        }
        find1.setDictTypeName(pojo.getDictTypeName());
        List<SysDataDictClassify> nameList = queryAllByCode(find1);
        if (CollectionUtil.isNotEmpty(nameList)) {
            return Result.failed("该类型名称已存在");
        }
        if (pojo.getId() == null) {
            IdWorker idWorker = new IdWorker(0L, 0L);
            pojo.setId(idWorker.nextId());
            pojo.setCreateUser(baseUser.getRealName());
            pojo.setCreateTime(DateUtil.now());
            pojo.setModifyUser(baseUser.getRealName());
            pojo.setModifyTime(DateUtil.now());
            pojo.setDelState(ConstantsUtil.IS_DONT_DEL);
            pojo.setState(ConstantsUtil.STATE_NORMAL);
            save(pojo);
        } else {
            SysDataDictClassify oldClassify = getById(pojo.getId());
            if (ObjectUtil.isNotEmpty(oldClassify)) {
                if (ObjectUtil.isNotEmpty(oldClassify.getDictTypeCode()) && ObjectUtil.isNotEmpty(oldClassify.getDictTypeName())) {
                    SysDataDict sysDataDict = new SysDataDict();
                    sysDataDict.setDictTypeCode(oldClassify.getDictTypeCode());
                    sysDataDict.setDictTypeName(oldClassify.getDictTypeName());
                    List<SysDataDict> oldList = sysDataDictService.queryAllByName(sysDataDict);
                    for (SysDataDict dataDict : oldList) {
                        if (ObjectUtil.isNotEmpty(pojo.getDictTypeCode()) && ObjectUtil.isNotEmpty(pojo.getDictTypeName())) {
                            dataDict.setDictTypeCode(pojo.getDictTypeCode());
                            dataDict.setDictTypeName(pojo.getDictTypeName());
                            sysDataDictService.update(dataDict);
                        }
                    }

                }
            }
            pojo.setModifyUser(baseUser.getRealName());
            pojo.setModifyTime(DateUtil.now());
            updateById(pojo);
        }
        return Result.success(pojo);
    }

    @Override
    public List<SysDataDictClassifyModel> queryAll(SysDataDictClassifyVO vo) {
        SysDataDictClassifyModel model = null;
        List<SysDataDictClassifyModel> modelList = new ArrayList<>();
        Integer num = 0;
        LambdaQueryChainWrapper<SysDataDictClassify> lambdaQuery = lambdaQuery();
        lambdaQuery.eq(SysDataDictClassify::getDelState, ConstantsUtil.IS_DONT_DEL);
        if (ObjectUtil.isNotEmpty(vo.getDictTypeName())) {
            lambdaQuery.like(SysDataDictClassify::getDictTypeName, vo.getDictTypeName());
        }
        List<SysDataDictClassify> list = lambdaQuery.orderByDesc(SysDataDictClassify::getCreateTime).orderByDesc(SysDataDictClassify::getId).list();
        if (CollectionUtil.isNotEmpty(list)) {
            for (SysDataDictClassify sysDataDictClassify : list) {
                model = new SysDataDictClassifyModel();
                BeanUtils.copyProperties(sysDataDictClassify, model);
                if (ObjectUtil.isNotEmpty(sysDataDictClassify.getDictTypeCode()) && ObjectUtil.isNotEmpty(sysDataDictClassify.getDictTypeName())) {
                    SysDataDict sysDataDict = new SysDataDict();
                    sysDataDict.setDictTypeCode(sysDataDictClassify.getDictTypeCode());
                    sysDataDict.setDictTypeName(sysDataDictClassify.getDictTypeName());
                    List<SysDataDict> oldList = sysDataDictService.queryAllByName(sysDataDict);
                    if (CollectionUtil.isNotEmpty(oldList)) {
                        num = oldList.size();
                    }
                }
                model.setDataDictNum(num);
                modelList.add(model);
            }
        }
        return modelList;
    }

    @Override
    public List<SysDataDictClassify> queryAllByCode(SysDataDictClassifyVO vo) {
        LambdaQueryChainWrapper<SysDataDictClassify> lambdaQuery = lambdaQuery();
        lambdaQuery.eq(SysDataDictClassify::getDelState, ConstantsUtil.IS_DONT_DEL);
        if (ObjectUtil.isNotEmpty(vo.getId())) {
            lambdaQuery.ne(SysDataDictClassify::getId, vo.getId());
        }
        if (ObjectUtil.isNotEmpty(vo.getDictTypeCode())) {
            lambdaQuery.eq(SysDataDictClassify::getDictTypeCode, vo.getDictTypeCode());
        }
        if (ObjectUtil.isNotEmpty(vo.getDictTypeName())) {
            lambdaQuery.eq(SysDataDictClassify::getDictTypeName, vo.getDictTypeName());
        }
        List<SysDataDictClassify> list = lambdaQuery.orderByDesc(SysDataDictClassify::getCreateTime).orderByDesc(SysDataDictClassify::getId).list();
        return list;
    }
}
