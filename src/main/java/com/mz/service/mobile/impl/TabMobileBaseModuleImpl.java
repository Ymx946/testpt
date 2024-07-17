package com.mz.service.mobile.impl;

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
import com.mz.framework.util.redis.RedisUtil;
import com.mz.mapper.localhost.TabMobileBaseModuleMapper;
import com.mz.model.base.BaseUser;
import com.mz.model.mobile.TabMobileBaseModule;
import com.mz.model.mobile.TabMobileBaseModuleStyle;
import com.mz.model.mobile.model.TabMobileBaseModuleModel;
import com.mz.model.mobile.model.TabMobileBaseModuleModels;
import com.mz.model.mobile.vo.TabMobileBaseModuleStyleVO;
import com.mz.model.mobile.vo.TabMobileBaseModuleVO;
import com.mz.service.mobile.TabMobileBaseModuleService;
import com.mz.service.mobile.TabMobileBaseModuleStyleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 移动组件表(TabMobileBaseModule)表服务实现类
 *
 * @author makejava
 * @since 2022-11-21 16:59:12
 */
@Service("tabMobileBaseModuleService")
public class TabMobileBaseModuleImpl extends ServiceImpl<TabMobileBaseModuleMapper, TabMobileBaseModule> implements TabMobileBaseModuleService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private TabMobileBaseModuleStyleService tabMobileBaseModuleStyleService;

    @Override
    public TabMobileBaseModule insert(TabMobileBaseModule pojo, String loginID, String styleJson) {
        String baseUserStr = redisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginID);
        JSONObject baseUserJson = JSONObject.parseObject(baseUserStr);
        BaseUser baseUser = JSONObject.toJavaObject(baseUserJson, BaseUser.class);
        if (pojo.getId() == null) {
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
            pojo.setModifyUser(baseUser.getRealName());
            pojo.setModifyTime(DateUtil.now());
            updateById(pojo);
        }
        if(ObjectUtil.isNotEmpty(styleJson)) {
            tabMobileBaseModuleStyleService.insert(pojo.getId(), styleJson);
        }
        return pojo;
    }

    @Override
    public PageInfo<TabMobileBaseModule> queryAllByLimit(TabMobileBaseModuleVO vo) {
        PageHelper.startPage(vo.getPageNo(), vo.getPageSize());
        LambdaQueryChainWrapper<TabMobileBaseModule> lambdaQuery = lambdaQuery();
        lambdaQuery.eq(TabMobileBaseModule::getDelState, ConstantsUtil.IS_DONT_DEL);
        if (vo.getState() != null) {
            lambdaQuery.eq(TabMobileBaseModule::getState, vo.getState());
        }
        if (vo.getModuleType() != null) {
            lambdaQuery.eq(TabMobileBaseModule::getModuleType, vo.getModuleType());
        }
        if (ObjectUtil.isNotEmpty(vo.getStartTime())) {
            String start = vo.getStartTime().substring(0, 10);
            lambdaQuery.ge(TabMobileBaseModule::getModifyTime, start);
        }
        if (ObjectUtil.isNotEmpty(vo.getEndTime())) {
            String end = vo.getEndTime().substring(0, 10);
            lambdaQuery.le(TabMobileBaseModule::getModifyTime,  end+ " 23:59:59");
        }
        if (ObjectUtil.isNotEmpty(vo.getModuleName())) {
            lambdaQuery.like(TabMobileBaseModule::getModuleName, vo.getModuleName());
        }
        List<TabMobileBaseModule> roomList = lambdaQuery.orderByDesc(TabMobileBaseModule::getModifyTime).list();
        PageInfo<TabMobileBaseModule> tabMobileBaseModulePageInfo = new PageInfo<TabMobileBaseModule>(roomList);
        return tabMobileBaseModulePageInfo;
    }

    @Override
    public TabMobileBaseModuleModel queryById(Long id) {
        TabMobileBaseModule tabMobileBaseModule = getById(id);
        TabMobileBaseModuleModel tabMobileBaseModuleModel = new TabMobileBaseModuleModel();
        BeanUtils.copyProperties(tabMobileBaseModule, tabMobileBaseModuleModel);
        TabMobileBaseModuleStyleVO vo = new TabMobileBaseModuleStyleVO();
        vo.setModuleId(id);
        List<TabMobileBaseModuleStyle> styleList = tabMobileBaseModuleStyleService.queryAll(vo);
        tabMobileBaseModuleModel.setTabMobileBaseModuleStyleList(styleList);
        return tabMobileBaseModuleModel;
    }

    @Override
    public List<TabMobileBaseModuleModels> queryModule(TabMobileBaseModule vo) {
        LambdaQueryChainWrapper<TabMobileBaseModule> lambdaQuery = lambdaQuery();
        lambdaQuery.eq(TabMobileBaseModule::getDelState, ConstantsUtil.IS_DONT_DEL);
        lambdaQuery.eq(TabMobileBaseModule::getState, ConstantsUtil.STATE_NORMAL);
        if (vo.getUseScene() != null) {
            lambdaQuery.like(TabMobileBaseModule::getUseScene, vo.getUseScene());
        }
        List<TabMobileBaseModule> moduleList = lambdaQuery.orderByAsc(TabMobileBaseModule::getModuleType).orderByDesc(TabMobileBaseModule::getCreateTime).list();
        List<TabMobileBaseModuleModels> addList = new ArrayList<TabMobileBaseModuleModels>();
        if (moduleList != null && moduleList.size() > 0) {
            List<TabMobileBaseModule> mobileBaseModuleList = new ArrayList<TabMobileBaseModule>();
            int type = moduleList.get(0).getModuleType().intValue();
            TabMobileBaseModuleModels tabMobileBaseModuleModels = new TabMobileBaseModuleModels();
            tabMobileBaseModuleModels.setModuleType(type);

            for (TabMobileBaseModule module : moduleList) {
                if (type == module.getModuleType().intValue()) {
                    mobileBaseModuleList.add(module);
                } else {
                    tabMobileBaseModuleModels.setTabMobileBaseModuleList(mobileBaseModuleList);
                    addList.add(tabMobileBaseModuleModels);
                    mobileBaseModuleList = new ArrayList<TabMobileBaseModule>();
                    type = module.getModuleType().intValue();
                    tabMobileBaseModuleModels = new TabMobileBaseModuleModels();
                    tabMobileBaseModuleModels.setModuleType(type);
                    mobileBaseModuleList.add(module);
                }
            }
            tabMobileBaseModuleModels.setTabMobileBaseModuleList(mobileBaseModuleList);
            addList.add(tabMobileBaseModuleModels);
        }
        return addList;
    }
}
