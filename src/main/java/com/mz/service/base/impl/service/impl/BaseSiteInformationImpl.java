package com.mz.service.base.impl.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.github.pagehelper.PageHelper;
import com.mz.framework.util.redis.RedisUtil;
import com.mz.mapper.localhost.BaseSiteInformationMapper;
import com.mz.model.base.*;
import com.mz.model.base.vo.BaseSiteInformationVO;
import com.mz.service.base.BaseSiteInformationService;
import com.mz.service.base.BaseUnitInformationService;
import com.mz.service.base.SysAreaService;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.ConstantsUtil;
import com.mz.common.util.IdWorker;
import com.mz.common.context.PageInfo;
import java.util.List;
import java.util.Map;

import com.mz.service.base.SysDataDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 站点信息表(BaseSiteInformation)表服务实现类
 *
 * @author makejava
 * @since 2024-12-23 14:06:40
 */
@Service("baseSiteInformationService")
public class BaseSiteInformationImpl extends ServiceImpl<BaseSiteInformationMapper, BaseSiteInformation> implements BaseSiteInformationService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SysAreaService sysAreaService;
    @Autowired
    private BaseUnitInformationService baseUnitInformationService;
    @Autowired
    private SysDataDictService sysDataDictService;

    @Override
    public BaseSiteInformation insert(BaseSiteInformation pojo, String loginID){
        String baseUserStr = redisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginID);
        JSONObject baseUserJson = JSONObject.parseObject(baseUserStr);
        BaseUser baseUser = JSONObject.toJavaObject(baseUserJson, BaseUser.class);
        SysArea findArea = new SysArea();
        findArea.setAreaCode(pojo.getAreaCode().substring(0, 2));
        Map<String, String> areaCatchMap = sysAreaService.getSysAreaCacheMap(findArea);

        if (ObjectUtil.isNotEmpty(pojo.getAreaCode())) {
            if (pojo.getAreaCode().length() >= 2) {
                pojo.setProvince(areaCatchMap.get(pojo.getAreaCode().substring(0, 2)));
            }
            if (pojo.getAreaCode().length() >= 4) {
                pojo.setCity(areaCatchMap.get(pojo.getAreaCode().substring(0, 4)));
            }
            if (pojo.getAreaCode().length() >= 6) {
                pojo.setCounty(areaCatchMap.get(pojo.getAreaCode().substring(0, 6)));
            }
        }

        if(ObjectUtil.isNotEmpty(pojo.getUnitId())){
            BaseUnitInformation information = baseUnitInformationService.getById(pojo.getUnitId());
           if(ObjectUtil.isNotEmpty(information) && ObjectUtil.isNotEmpty(information.getUnitName())){
               pojo.setUnitName(information.getUnitName());
           }
        }

        if(ObjectUtil.isNotEmpty(pojo.getOpsId())){
            BaseUnitInformation information = baseUnitInformationService.getById(pojo.getOpsId());
            if(ObjectUtil.isNotEmpty(information) && ObjectUtil.isNotEmpty(information.getUnitName())){
                pojo.setOpsName(information.getUnitName());
            }
        }

        if (ObjectUtil.isNotEmpty(pojo.getVoltageLevelCode())) {
            SysDataDict sysDataDict = sysDataDictService.queryByCode(SysDataDict.VOLTAGE_LEVEL_CODE, pojo.getVoltageLevelCode());
            if (ObjectUtil.isNotEmpty(sysDataDict)) {
                pojo.setVoltageLevelName(sysDataDict.getDictName());
            }
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
        return pojo;
    }
  @Override
    public PageInfo<BaseSiteInformation> queryAllByLimit(BaseSiteInformationVO vo) {
        PageHelper.startPage(vo.getPageNo(), vo.getPageSize());
        List<BaseSiteInformation> list = queryAll(vo);
        PageInfo<BaseSiteInformation> pageInfo = new PageInfo<BaseSiteInformation>(list);
        return pageInfo;
    }
  @Override
    public List<BaseSiteInformation> queryAll(BaseSiteInformationVO vo) {
        LambdaQueryChainWrapper<BaseSiteInformation> lambdaQuery = lambdaQuery();
        lambdaQuery.eq(BaseSiteInformation::getDelState, ConstantsUtil.IS_DONT_DEL);
        if (vo.getState() != null) {
            lambdaQuery.eq(BaseSiteInformation::getState, vo.getState());
        }
        if (ObjectUtil.isNotEmpty(vo.getAreaCode())) {
            lambdaQuery.likeRight(BaseSiteInformation::getAreaCode, vo.getAreaCode());
        }
        if (ObjectUtil.isNotEmpty(vo.getFindStr())) {
            lambdaQuery.like(BaseSiteInformation::getSiteName, vo.getSiteName());
        }
        List<BaseSiteInformation> list = lambdaQuery.orderByDesc(BaseSiteInformation::getCreateTime).list();
        return list;
    }
}
