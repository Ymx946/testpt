package com.mz.service.base.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.ConstantsUtil;
import com.mz.common.context.PageInfo;
import com.mz.common.util.IdWorker;
import com.mz.common.util.Result;
import com.mz.framework.util.redis.RedisUtil;
import com.mz.mapper.localhost.TabBaseFunctionSetMapper;
import com.mz.model.base.BaseUser;
import com.mz.model.base.TabBaseFunctionSet;
import com.mz.model.base.vo.TabBaseFunctionSetVO;
import com.mz.service.base.TabBaseFunctionSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 功能设置基础表(TabBaseFunctionSet)表服务实现类
 *
 * @author makejava
 * @since 2022-12-12 11:32:35
 */
@Service("tabBaseFunctionSetService")
public class TabBaseFunctionSetImpl extends ServiceImpl<TabBaseFunctionSetMapper, TabBaseFunctionSet> implements TabBaseFunctionSetService {
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Result insert(TabBaseFunctionSet pojo, String loginID) {
        String baseUserStr = redisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO + loginID);
        JSONObject baseUserJson = JSONObject.parseObject(baseUserStr);
        BaseUser baseUser = JSONObject.toJavaObject(baseUserJson, BaseUser.class);
        if (pojo.getId() == null) {
            TabBaseFunctionSetVO setVO = new TabBaseFunctionSetVO();
            setVO.setFunctionCode(pojo.getFunctionCode());
            List<TabBaseFunctionSet> setList = queryAll(setVO);
            if(CollectionUtil.isNotEmpty(setList)){
                return Result.failed("功能代码重复");
            }
            IdWorker idWorker = new IdWorker(0L, 0L);
            pojo.setId(idWorker.nextId());
            pojo.setCreateUser(baseUser.getRealName());
            pojo.setCreateTime(DateUtil.now());
            pojo.setModifyUser(baseUser.getRealName());
            pojo.setModifyTime(DateUtil.now());
            pojo.setDelState(ConstantsUtil.IS_DONT_DEL);
            pojo.setState(ConstantsUtil.STATE_NORMAL);//启用状态 1正常-1禁用
            save(pojo);
        } else {
            TabBaseFunctionSetVO setVO = new TabBaseFunctionSetVO();
            setVO.setFunctionCode(pojo.getFunctionCode());
            setVO.setId(pojo.getId());
            List<TabBaseFunctionSet> setList = queryAll(setVO);
            if(CollectionUtil.isNotEmpty(setList)){
                return Result.failed("功能代码重复");
            }
            pojo.setModifyUser(baseUser.getRealName());
            pojo.setModifyTime(DateUtil.now());
            updateById(pojo);
        }
        return Result.success(pojo);
    }

    @Override
    public List<TabBaseFunctionSet> queryAll(TabBaseFunctionSetVO vo) {
        LambdaQueryChainWrapper<TabBaseFunctionSet> lambdaQuery = lambdaQuery();
        lambdaQuery.eq(TabBaseFunctionSet::getDelState, ConstantsUtil.IS_DONT_DEL);
        if (vo.getId() != null) {
            lambdaQuery.ne(TabBaseFunctionSet::getId, vo.getId()); //<!--id不等于，用于在修改时判断名称重复，其他不会有传ID的情况存在-->
        }
        if (ObjectUtil.isNotEmpty(vo.getFunctionCode())) {
            lambdaQuery.eq(TabBaseFunctionSet::getFunctionCode, vo.getFunctionCode());
        }
        if (ObjectUtil.isNotEmpty(vo.getAccreditType())) {
            lambdaQuery.eq(TabBaseFunctionSet::getAccreditType, vo.getAccreditType());
        }
        List<TabBaseFunctionSet> roomList = lambdaQuery.orderByDesc(TabBaseFunctionSet::getCreateTime).list();
        return roomList;
    }
    @Override
    public PageInfo<TabBaseFunctionSet> queryAllByLimit(TabBaseFunctionSetVO vo) {
        PageHelper.startPage(vo.getPageNo(), vo.getPageSize());
        List<TabBaseFunctionSet> roomList = queryAll(vo);
        PageInfo<TabBaseFunctionSet> tabFunctionSetPageInfo = new PageInfo<TabBaseFunctionSet>(roomList);
        return tabFunctionSetPageInfo;
    }
}
