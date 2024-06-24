package com.mz.service.base.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.ConstantsUtil;
import com.mz.common.context.PageInfo;
import com.mz.common.util.IdWorker;
import com.mz.framework.util.redis.RedisUtil;
import com.mz.mapper.localhost.TabActivitiFlowInfoMapper;
import com.mz.model.base.BaseUser;
import com.mz.model.base.TabActivitiFlowInfo;
import com.mz.model.base.vo.TabActivitiFlowInfoVO;
import com.mz.service.base.TabActivitiFlowInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程控制表(ActFlowInfo)表服务实现类
 *
 * @author makejava
 * @since 2023-02-06 18:24:29
 */
@Service("actFlowInfoService")
public class TabActivitiFlowInfoImpl extends ServiceImpl<TabActivitiFlowInfoMapper, TabActivitiFlowInfo> implements TabActivitiFlowInfoService {
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public TabActivitiFlowInfo insert(TabActivitiFlowInfo tabActivitiFlowInfo, String loginID) {
        String baseUserStr = redisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginID);
        JSONObject baseUserJson = JSONObject.parseObject(baseUserStr);
        BaseUser baseUser = JSONObject.toJavaObject(baseUserJson, BaseUser.class);
        if (tabActivitiFlowInfo.getId() == null) {
            IdWorker idWorker = new IdWorker(0L, 0L);
            tabActivitiFlowInfo.setId(idWorker.nextId());
            tabActivitiFlowInfo.setCreateTime(DateUtil.now());
            tabActivitiFlowInfo.setState(1);
            tabActivitiFlowInfo.setCreateUser(baseUser.getRealName());
            tabActivitiFlowInfo.setModifyUser(baseUser.getRealName());
            tabActivitiFlowInfo.setModifyTime(DateUtil.now());
            tabActivitiFlowInfo.setDelState(ConstantsUtil.IS_DONT_DEL);
            save(tabActivitiFlowInfo);
        } else {
            tabActivitiFlowInfo.setModifyUser(baseUser.getRealName());
            tabActivitiFlowInfo.setModifyTime(DateUtil.now());
            updateById(tabActivitiFlowInfo);
        }
        return tabActivitiFlowInfo;
    }

    @Override
    public PageInfo<TabActivitiFlowInfo> queryAllByLimit(TabActivitiFlowInfoVO vo) {
        PageHelper.startPage(vo.getPageNo(), vo.getPageSize());
        LambdaQueryChainWrapper<TabActivitiFlowInfo> lambdaQuery = lambdaQuery();
        lambdaQuery.eq(TabActivitiFlowInfo::getDelState, ConstantsUtil.IS_DONT_DEL);
//        lambdaQuery.eq(ActFlowInfo::getState, 0);
        if (!StringUtils.isEmpty(vo.getFlowName())) {
            lambdaQuery.like(TabActivitiFlowInfo::getFlowName, vo.getFlowName());
        }
        List<TabActivitiFlowInfo> roomList = lambdaQuery.orderByDesc(TabActivitiFlowInfo::getCreateTime).list();
        PageInfo<TabActivitiFlowInfo> pageInfo = new PageInfo<TabActivitiFlowInfo>(roomList);
        return pageInfo;
    }

    @Override
    public List<TabActivitiFlowInfo> queryAll(TabActivitiFlowInfoVO vo) {
        LambdaQueryChainWrapper<TabActivitiFlowInfo> lambdaQuery = lambdaQuery();
        lambdaQuery.eq(TabActivitiFlowInfo::getDelState, ConstantsUtil.IS_DONT_DEL);
        lambdaQuery.eq(TabActivitiFlowInfo::getState, 0);
        if (!StringUtils.isEmpty(vo.getFlowName())) {
            lambdaQuery.like(TabActivitiFlowInfo::getFlowName, vo.getFlowName());
        }
        List<TabActivitiFlowInfo> roomList = lambdaQuery.orderByDesc(TabActivitiFlowInfo::getCreateTime).list();
        if (CollectionUtil.isNotEmpty(roomList)) {
            return roomList;
        }
        return new ArrayList<>();
    }
}
