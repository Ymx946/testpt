package com.mz.service.base.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mz.common.util.Result;
import com.mz.framework.util.redis.RedisUtil;
import com.mz.mapper.localhost.SysDataDictClassifyMapper;
import com.mz.mapper.localhost.SysDataDictMapper;
import com.mz.model.base.SysDataDict;
import com.mz.model.base.SysDataDictClassify;
import com.mz.service.base.SysDataDictClassifyService;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.ConstantsUtil;
import com.mz.common.util.IdWorker;
import com.mz.model.base.BaseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private RedisUtil myRedisUtil;
    @Autowired
    public SysDataDictMapper sysDataDictMapper;

    @Override
    public Result insert(SysDataDictClassify pojo, String loginID){
        String baseUserStr = myRedisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO + loginID);
        JSONObject baseUserJson = JSONObject.parseObject(baseUserStr);
        BaseUser baseUser = JSONObject.toJavaObject(baseUserJson, BaseUser.class);
        SysDataDict find = new SysDataDict();
        if (ObjectUtil.isNotEmpty(pojo.getId())){
            find.setId(String.valueOf(pojo.getId()));
        }
        find.setDictTypeCode(pojo.getDictTypeCode());
        List<SysDataDict> codeList = sysDataDictMapper.queryAll(find);
        if(CollectionUtil.isNotEmpty(codeList)){
            return Result.failed("该类型代码已存在");
        }
        SysDataDict find1 = new SysDataDict();
        if (ObjectUtil.isNotEmpty(pojo.getId())){
            find1.setId(String.valueOf(pojo.getId()));
        }
        find1.setDictTypeName(pojo.getDictTypeName());
        List<SysDataDict> nameList = sysDataDictMapper.queryAll(find1);
        if(CollectionUtil.isNotEmpty(nameList)){
            return Result.failed("该类型名称已存在");
        }
        if(pojo.getId()==null){
            IdWorker idWorker = new IdWorker(0L, 0L);
            pojo.setId(idWorker.nextId());
            pojo.setCreateUser(baseUser.getRealName());
            pojo.setCreateTime(DateUtil.now());
            pojo.setModifyUser(baseUser.getRealName());
            pojo.setModifyTime(DateUtil.now());
            pojo.setDelState(ConstantsUtil.IS_DONT_DEL);
            pojo.setState(ConstantsUtil.STATE_NORMAL);
            save(pojo);
        }else{
            pojo.setModifyUser(baseUser.getRealName());
            pojo.setModifyTime(DateUtil.now());
            updateById(pojo);
        }
        return Result.success(pojo);
    }

}
