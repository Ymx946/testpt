package com.mz.service.base.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.github.pagehelper.PageHelper;
import com.mz.framework.util.redis.RedisUtil;
import com.mz.mapper.localhost.BaseSiteHostMapper;
import com.mz.model.base.BaseSiteHost;
import com.mz.model.base.BaseSiteInformation;
import com.mz.model.base.SysDataDict;
import com.mz.model.base.vo.BaseSiteHostVO;
import com.mz.service.base.BaseSiteHostService;
import com.mz.service.base.BaseSiteInformationService;
import com.mz.service.base.SysAreaService;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.ConstantsUtil;
import com.mz.common.util.IdWorker;
import com.mz.common.context.PageInfo;

import java.util.List;

import com.mz.model.base.BaseUser;
import com.mz.service.base.SysDataDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 站点主机信息表(BaseSiteHost)表服务实现类
 *
 * @author makejava
 * @since 2024-12-25 09:03:18
 */
@Service("baseSiteHostService")
public class BaseSiteHostImpl extends ServiceImpl<BaseSiteHostMapper, BaseSiteHost> implements BaseSiteHostService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SysDataDictService sysDataDictService;
    @Autowired
    private BaseSiteInformationService baseSiteInformationService;

    @Override
    public BaseSiteHost insert(BaseSiteHost pojo, String loginID) {
        String baseUserStr = redisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginID);
        JSONObject baseUserJson = JSONObject.parseObject(baseUserStr);
        BaseUser baseUser = JSONObject.toJavaObject(baseUserJson, BaseUser.class);
        if(ObjectUtil.isNotEmpty(pojo.getSiteId())){
            BaseSiteInformation baseSiteInformation = baseSiteInformationService.getById(pojo.getSiteId());
            if(ObjectUtil.isNotEmpty(baseSiteInformation)){
                pojo.setSiteName(baseSiteInformation.getSiteName());
            }
        }
        if (ObjectUtil.isNotEmpty(pojo.getDeviceTypeCode())) {
            SysDataDict sysDataDict = sysDataDictService.queryByCode(SysDataDict.HOST_DEVICE_TYPE_CODE, pojo.getDeviceTypeCode());
            if (ObjectUtil.isNotEmpty(sysDataDict)) {
                pojo.setDeviceTypeName(sysDataDict.getDictName());
            }
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
            pojo.setModifyUser(baseUser.getRealName());
            pojo.setModifyTime(DateUtil.now());
            updateById(pojo);
        }
        return pojo;
    }

    @Override
    public PageInfo<BaseSiteHost> queryAllByLimit(BaseSiteHostVO vo) {
        PageHelper.startPage(vo.getPageNo(), vo.getPageSize());
        List<BaseSiteHost> list = queryAll(vo);
        PageInfo<BaseSiteHost> pageInfo = new PageInfo<BaseSiteHost>(list);
        return pageInfo;
    }

    @Override
    public List<BaseSiteHost> queryAll(BaseSiteHostVO vo) {
        LambdaQueryChainWrapper<BaseSiteHost> lambdaQuery = lambdaQuery();
        lambdaQuery.eq(BaseSiteHost::getDelState, ConstantsUtil.IS_DONT_DEL);
        if (ObjectUtil.isNotEmpty(vo.getSiteId())) {
            lambdaQuery.eq(BaseSiteHost::getSiteId, vo.getSiteId());
        }
        if (vo.getState() != null) {
            lambdaQuery.eq(BaseSiteHost::getState, vo.getState());
        }
        if (ObjectUtil.isNotEmpty(vo.getDeviceTypeCode())) {
            lambdaQuery.eq(BaseSiteHost::getDeviceTypeCode, vo.getDeviceTypeCode());
        }
        List<BaseSiteHost> list = lambdaQuery.orderByDesc(BaseSiteHost::getCreateTime).list();
        return list;
    }
}
