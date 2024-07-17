package com.mz.service.mobile.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mz.common.ConstantsUtil;
import com.mz.common.util.IdWorker;
import com.mz.mapper.localhost.TabMobileBaseModuleStyleMapper;
import com.mz.model.mobile.TabMobileBaseModule;
import com.mz.model.mobile.TabMobileBaseModuleStyle;
import com.mz.model.mobile.vo.TabMobileBaseModuleStyleVO;
import com.mz.service.mobile.TabMobileBaseModuleService;
import com.mz.service.mobile.TabMobileBaseModuleStyleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 移动组件样式表(TabMobileBaseModuleStyle)表服务实现类
 *
 * @author makejava
 * @since 2022-11-21 16:59:33
 */
@Service("tabMobileBaseModuleStyleService")
public class TabMobileBaseModuleStyleImpl extends ServiceImpl<TabMobileBaseModuleStyleMapper, TabMobileBaseModuleStyle> implements TabMobileBaseModuleStyleService {
    @Autowired
    private TabMobileBaseModuleService tabMobileBaseModuleService;

    @Override
    public List<TabMobileBaseModuleStyle> insert(Long moduleId, String styleJson) {
        TabMobileBaseModule tabMobileBaseModule = tabMobileBaseModuleService.queryById(moduleId);
        List<TabMobileBaseModuleStyle> reList = new ArrayList<TabMobileBaseModuleStyle>();
        List<Long> oldIdList = new ArrayList<Long>();
        LambdaQueryChainWrapper<TabMobileBaseModuleStyle> lambdaQuery = lambdaQuery();
        lambdaQuery.eq(TabMobileBaseModuleStyle::getDelState, ConstantsUtil.IS_DONT_DEL);
        lambdaQuery.eq(TabMobileBaseModuleStyle::getModuleId, moduleId);
        List<TabMobileBaseModuleStyle> oldList = lambdaQuery.list();
        for (TabMobileBaseModuleStyle tabMobileBaseModuleStyle : oldList) {
            oldIdList.add(tabMobileBaseModuleStyle.getId());
        }
        if (!StringUtils.isEmpty(styleJson)) {
            List<TabMobileBaseModuleStyle> styleList = JSONUtil.toList(styleJson, TabMobileBaseModuleStyle.class);
            if (CollectionUtil.isNotEmpty(styleList)) {
                List<TabMobileBaseModuleStyle> addList = new ArrayList<TabMobileBaseModuleStyle>();
                IdWorker idWorker = new IdWorker(0L, 0L);
                for (TabMobileBaseModuleStyle pojo : styleList) {
                    if (pojo.getId() != null) {
                        oldIdList.remove(pojo.getId());
                        updateById(pojo);
                    } else {
                        BeanUtils.copyProperties(tabMobileBaseModule, pojo);
                        pojo.setId(idWorker.nextId());
                        pojo.setModuleId(moduleId);
                        pojo.setCreateTime(DateUtil.now());
                        pojo.setModifyTime(DateUtil.now());
                        pojo.setDelState(ConstantsUtil.IS_DONT_DEL);
                        pojo.setState(ConstantsUtil.STATE_NORMAL);
                        pojo.setCreateUser(tabMobileBaseModule.getModifyUser());
                        pojo.setModifyUser(tabMobileBaseModule.getModifyUser());
                        addList.add(pojo);
                    }
                    reList.add(pojo);
                }
                saveBatch(addList);
            }
        }
        if (oldIdList != null && oldIdList.size() > 0) {
            for (Long aLong : oldIdList) {
                TabMobileBaseModuleStyle delPojo = new TabMobileBaseModuleStyle();
                delPojo.setId(aLong);
                delPojo.setDelState(ConstantsUtil.IS_DEL);
                updateById(delPojo);//删除数据
            }
        }
        return reList;
    }

    @Override
    public TabMobileBaseModuleStyle update(TabMobileBaseModuleStyle pojo) {
        pojo.setModifyUser(pojo.getModifyUser());
        pojo.setModifyTime(DateUtil.now());
        updateById(pojo);
        return pojo;
    }

    @Override
    public List<TabMobileBaseModuleStyle> queryAll(TabMobileBaseModuleStyleVO vo) {
        LambdaQueryChainWrapper<TabMobileBaseModuleStyle> lambdaQuery = lambdaQuery();
        lambdaQuery.eq(TabMobileBaseModuleStyle::getDelState, ConstantsUtil.IS_DONT_DEL);
        lambdaQuery.eq(TabMobileBaseModuleStyle::getState, ConstantsUtil.STATE_NORMAL);
        lambdaQuery.eq(TabMobileBaseModuleStyle::getModuleId, vo.getModuleId());
        List<TabMobileBaseModuleStyle> roomList = lambdaQuery.orderByAsc(TabMobileBaseModuleStyle::getModifyTime).list();
        return roomList;
    }
}
