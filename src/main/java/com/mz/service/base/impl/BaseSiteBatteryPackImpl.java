package com.mz.service.base.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.github.pagehelper.PageHelper;
import com.mz.mapper.localhost.BaseSiteBatteryPackMapper;
import com.mz.model.base.*;
import com.mz.model.base.vo.BaseSiteBatteryPackVO;
import com.mz.service.base.*;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.ConstantsUtil;
import com.mz.common.util.IdWorker;
import com.mz.common.context.PageInfo;

import java.util.List;

import com.mz.framework.util.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 站点电池组信息表(BaseSiteBatteryPack)表服务实现类
 *
 * @author makejava
 * @since 2024-12-25 11:18:09
 */
@Service("baseSiteBatteryPackService")
public class BaseSiteBatteryPackImpl extends ServiceImpl<BaseSiteBatteryPackMapper, BaseSiteBatteryPack> implements BaseSiteBatteryPackService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private BaseSiteInformationService baseSiteInformationService;
    @Autowired
    private BaseSiteHostService baseSiteHostService;
    @Autowired
    private SysDataDictService sysDataDictService;

    @Override
    public BaseSiteBatteryPack insert(BaseSiteBatteryPack pojo, String loginID) {
        String baseUserStr = redisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginID);
        JSONObject baseUserJson = JSONObject.parseObject(baseUserStr);
        BaseUser baseUser = JSONObject.toJavaObject(baseUserJson, BaseUser.class);
        if(ObjectUtil.isNotEmpty(pojo.getSiteId())){
            BaseSiteInformation baseSiteInformation = baseSiteInformationService.getById(pojo.getSiteId());
            if(ObjectUtil.isNotEmpty(baseSiteInformation)){
                pojo.setSiteName(baseSiteInformation.getSiteName());
            }
        }
        if(ObjectUtil.isNotEmpty(pojo.getHostId())){
            BaseSiteHost baseSiteHost = baseSiteHostService.getById(pojo.getHostId());
            if(ObjectUtil.isNotEmpty(baseSiteHost)){
                pojo.setHostName(baseSiteHost.getHostName());
            }
        }
        if (ObjectUtil.isNotEmpty(pojo.getCurrentStateCode())) {
            SysDataDict sysDataDict = sysDataDictService.queryByCode(SysDataDict.BATTERY_PACK_STATE_CODE, pojo.getCurrentStateCode());
            if (ObjectUtil.isNotEmpty(sysDataDict)) {
                pojo.setCurrentStateName(sysDataDict.getDictName());
            }
        }
        if (pojo.getId() == null) {
            IdWorker idWorker = new IdWorker(0L, 0L);
            pojo.setId(idWorker.nextId());
            pojo.setCreateTime(DateUtil.now());
            pojo.setCreateUser(baseUser.getRealName());
            pojo.setModifyUser(baseUser.getRealName());
            pojo.setModifyTime(DateUtil.now());
            pojo.setDelState(ConstantsUtil.IS_DONT_DEL);
            pojo.setState(ConstantsUtil.STATE_NORMAL);
            save(pojo);
        } else {
            pojo.setModifyUser(baseUser.getRealName());
            pojo.setModifyTime(DateUtil.now());
            updateById(pojo);
        }
        return pojo;
    }

    @Override
    public PageInfo<BaseSiteBatteryPack> queryAllByLimit(BaseSiteBatteryPackVO vo) {
        PageHelper.startPage(vo.getPageNo(), vo.getPageSize());
        List<BaseSiteBatteryPack> list = queryAll(vo);
        PageInfo<BaseSiteBatteryPack> pageInfo = new PageInfo<BaseSiteBatteryPack>(list);
        return pageInfo;
    }

    @Override
    public List<BaseSiteBatteryPack> queryAll(BaseSiteBatteryPackVO vo) {
        LambdaQueryChainWrapper<BaseSiteBatteryPack> lambdaQuery = lambdaQuery();
        lambdaQuery.eq(BaseSiteBatteryPack::getDelState, ConstantsUtil.IS_DONT_DEL);
        if (vo.getState() != null) {
            lambdaQuery.eq(BaseSiteBatteryPack::getState, vo.getState());
        }
        if (ObjectUtil.isNotEmpty(vo.getSiteId())) {
            lambdaQuery.eq(BaseSiteBatteryPack::getSiteId, vo.getSiteId());
        }
        if (ObjectUtil.isNotEmpty(vo.getHostId())) {
            lambdaQuery.eq(BaseSiteBatteryPack::getHostId, vo.getHostId());
        }
        if (ObjectUtil.isNotEmpty(vo.getSiteName())) {
            lambdaQuery.like(BaseSiteBatteryPack::getSiteName, vo.getSiteName());
        }
        if (ObjectUtil.isNotEmpty(vo.getHostName())) {
            lambdaQuery.like(BaseSiteBatteryPack::getHostName, vo.getHostName());
        }
        if (ObjectUtil.isNotEmpty(vo.getBatteryPackName())) {
            lambdaQuery.like(BaseSiteBatteryPack::getBatteryPackName, vo.getBatteryPackName());
        }
        if (ObjectUtil.isNotEmpty(vo.getCurrentStateCode())) {
            lambdaQuery.eq(BaseSiteBatteryPack::getCurrentStateCode, vo.getCurrentStateCode());
        }
        List<BaseSiteBatteryPack> list = lambdaQuery.orderByDesc(BaseSiteBatteryPack::getCreateTime).list();
        return list;
    }
}
