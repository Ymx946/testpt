package com.mz.service.base.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.github.pagehelper.PageHelper;
import com.mz.common.util.Result;
import com.mz.framework.util.redis.RedisUtil;
import com.mz.mapper.localhost.BaseUnitInformationMapper;
import com.mz.model.base.*;
import com.mz.model.base.model.BaseSiteHostModel;
import com.mz.model.base.model.BaseSiteInformationModel;
import com.mz.model.base.model.BaseUnitInformationModel;
import com.mz.model.base.vo.BaseSiteBatteryPackVO;
import com.mz.model.base.vo.BaseSiteHostVO;
import com.mz.model.base.vo.BaseSiteInformationVO;
import com.mz.model.base.vo.BaseUnitInformationVO;
import com.mz.service.base.BaseSiteBatteryPackService;
import com.mz.service.base.BaseSiteHostService;
import com.mz.service.base.BaseSiteInformationService;
import com.mz.service.base.BaseUnitInformationService;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.ConstantsUtil;
import com.mz.common.util.IdWorker;
import com.mz.common.context.PageInfo;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 单位信息表(BaseUnitInformation)表服务实现类
 *
 * @author makejava
 * @since 2024-12-23 10:17:40
 */
@Service("baseUnitInformationService")
public class BaseUnitInformationImpl extends ServiceImpl<BaseUnitInformationMapper, BaseUnitInformation> implements BaseUnitInformationService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private BaseSiteInformationService baseSiteInformationService;
    @Autowired
    private BaseSiteHostService baseSiteHostService;
    @Autowired
    private BaseSiteBatteryPackService baseSiteBatteryPackService;

    @Override
    public Result insert(BaseUnitInformation pojo, String loginID) {
        String baseUserStr = redisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginID);
        JSONObject baseUserJson = JSONObject.parseObject(baseUserStr);
        BaseUser baseUser = JSONObject.toJavaObject(baseUserJson, BaseUser.class);
        if(ObjectUtil.isNotEmpty(pojo.getPid())){
            BaseUnitInformation information = getById(pojo.getPid());
            if(ObjectUtil.isNotEmpty(information) && ObjectUtil.isNotEmpty(information.getUnitName())){
                pojo.setHigherLevelUnit(information.getUnitName());
            }
        }
        if(ObjectUtil.isEmpty(pojo.getPid())){
            pojo.setLevel(1);
        }else {
            BaseUnitInformation information = getById(pojo.getPid());
            if(ObjectUtil.isNotEmpty(information) && ObjectUtil.isNotEmpty(information.getLevel())){
                pojo.setLevel(information.getLevel()+1);
            }
        }
        if(ObjectUtil.isEmpty(pojo.getState())){
            pojo.setState(ConstantsUtil.STATE_NORMAL);
        }
        if (pojo.getId() == null) {
            BaseUnitInformationVO newVO = new BaseUnitInformationVO();
            newVO.setUnitName(pojo.getUnitName());
            List<BaseUnitInformation> list = queryAll(newVO);
            if (CollectionUtil.isNotEmpty(list)) {
                return Result.failed("单位名称重复");
            }
            IdWorker idWorker = new IdWorker(0L, 0L);
            pojo.setId(idWorker.nextId());
            pojo.setCreateUser(baseUser.getRealName());
            pojo.setCreateTime(DateUtil.now());
            pojo.setModifyUser(baseUser.getRealName());
            pojo.setModifyTime(DateUtil.now());
            pojo.setDelState(ConstantsUtil.IS_DONT_DEL);
            save(pojo);
        } else {
            BaseUnitInformationVO findVO = new BaseUnitInformationVO();
            findVO.setId(pojo.getId());
            findVO.setUnitName(pojo.getUnitName());
            List<BaseUnitInformation> list = queryAll(findVO);
            if (CollectionUtil.isNotEmpty(list)) {
                return Result.failed("单位名称重复");
            }
            pojo.setModifyUser(baseUser.getRealName());
            pojo.setModifyTime(DateUtil.now());
            updateById(pojo);
        }
        return Result.success(pojo);
    }

    @Override
    public PageInfo<BaseUnitInformation> queryAllByLimit(BaseUnitInformationVO vo) {
        PageHelper.startPage(vo.getPageNo(), vo.getPageSize());
        List<BaseUnitInformation> list = queryAll(vo);
        PageInfo<BaseUnitInformation> pageInfo = new PageInfo<BaseUnitInformation>(list);
        return pageInfo;
    }

    @Override
    public List<BaseUnitInformation> queryAll(BaseUnitInformationVO vo) {
        LambdaQueryChainWrapper<BaseUnitInformation> lambdaQuery = lambdaQuery();
        lambdaQuery.eq(BaseUnitInformation::getDelState, ConstantsUtil.IS_DONT_DEL);
        if (ObjectUtil.isNotEmpty(vo.getId())) {
            lambdaQuery.ne(BaseUnitInformation::getId, vo.getId());
        }
        if (vo.getState() != null) {
            lambdaQuery.eq(BaseUnitInformation::getState, vo.getState());
        }
        if (ObjectUtil.isNotEmpty(vo.getUnitName())) {
            lambdaQuery.eq(BaseUnitInformation::getUnitName, vo.getUnitName());
        }
        if (!StringUtils.isEmpty(vo.getFindStr())) {
            lambdaQuery.like(BaseUnitInformation::getUnitName, vo.getFindStr());
        }
        List<BaseUnitInformation> list = lambdaQuery.orderByDesc(BaseUnitInformation::getCreateTime).list();
        return list;
    }
    @Override
    public List<BaseUnitInformationModel> queryTreeThree(BaseUnitInformationVO vo) {
        vo.setState(1);
        List<BaseUnitInformation> list = queryAll(vo);
        List<BaseUnitInformationModel> treeList = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(list)) {
            // Create a map of models indexed by their ID
            Map<Long, BaseUnitInformationModel> parentMap = list.stream()
                    .map(unit -> {
                        BaseUnitInformationModel model = new BaseUnitInformationModel();
                        BeanUtils.copyProperties(unit, model);
                        return model;
                    })
                    .collect(Collectors.toMap(BaseUnitInformationModel::getId, model -> model));

            // 构建树结构
            parentMap.values().forEach(model -> {
                if (StringUtils.isEmpty(model.getPid())) {
                    treeList.add(model);
                } else {
                    BaseUnitInformationModel parentModel = parentMap.get(model.getPid());
                    if (parentModel != null) {
                        if (parentModel.getChildrenList() == null) {
                            parentModel.setChildrenList(new ArrayList<>());
                        }
                        parentModel.getChildrenList().add(model);
                    }
                }
            });
        }
        return treeList;
    }

    @Override
    public Map<String, Object> queryTreeFive(BaseUnitInformationVO vo) {
        List<BaseUnitInformation> units = queryAllUnits(vo); // Fetch units
        List<BaseSiteInformation> sites = queryAllSites(); // Fetch sites
        List<BaseSiteHost> hosts = queryAllHosts(); // Fetch hosts

        // Map for the first three layers
        Map<Long, Map<String, Object>> unitMap = new HashMap<>();
        // Track added unit IDs
        Set<Long> addedUnitIds = new HashSet<>();
        // Store BaseUnitInformation by id
        Map<Long, BaseUnitInformation> unitInfoMap = new HashMap<>();

        // Build the first three layers
        for (BaseUnitInformation unit : units) {
            unitInfoMap.put(unit.getId(), unit);
            Map<String, Object> unitData = new HashMap<>();
            unitData.put("id", unit.getId());
            unitData.put("name", unit.getUnitName());
            unitData.put("children", new ArrayList<Map<String, Object>>());
            unitMap.put(unit.getId(), unitData);
        }

        // Prepare final structure
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> unitList = new ArrayList<>();
        result.put("units", unitList);

        // Build the first layer
        for (BaseUnitInformation unit : units) {
            if (unit.getPid() == null) { // First layer
                if (!addedUnitIds.contains(unit.getId())) {
                    addedUnitIds.add(unit.getId());
                    Map<String, Object> unitData = unitMap.get(unit.getId());
                    unitData.put("children", new ArrayList<>());
                    unitList.add(unitData);
                }
            }
        }

        // Build the second and third layers
        for (BaseUnitInformation unit : units) {
            if (unit.getPid()!= null) { // Second and third layers
                Map<String, Object> parent = unitMap.get(unit.getPid());
                if (parent!= null) {
                    List<Map<String, Object>> children = (List<Map<String, Object>>) parent.get("children");
                    // Use Set to check duplicates
                    Set<Long> childIds = new HashSet<>();
                    for (Map<String, Object> child : children) {
                        childIds.add((Long) child.get("id"));
                    }
                    if (!childIds.contains(unit.getId())) {
                        children.add(unitMap.get(unit.getId()));
                    }
                }
            }
        }

        // Build fourth layer
        Map<Long, Map<String, Object>> siteMap = new HashMap<>();
        Set<Long> addedSiteIds = new HashSet<>(); // Track added site IDs
        for (BaseSiteInformation site : sites) {
            Map<String, Object> siteData = new HashMap<>();
            siteData.put("id", site.getId());
            siteData.put("name", site.getSiteName());
            siteData.put("children", new ArrayList<Map<String, Object>>());
            siteMap.put(site.getId(), siteData);
        }

        for (BaseSiteInformation site : sites) {
            if (unitMap.containsKey(site.getUnitId())) {
                List<Map<String, Object>> children = (List<Map<String, Object>>) unitMap.get(site.getUnitId()).get("children");
                // Use Set to check duplicates
                Set<Long> childIds = new HashSet<>();
                for (Map<String, Object> child : children) {
                    childIds.add((Long) child.get("id"));
                }
                if (!childIds.contains(site.getId())) {
                    children.add(siteMap.get(site.getId()));
                }
            }
        }

        // Build fifth layer
        for (BaseSiteHost host : hosts) {
            Map<String, Object> hostData = new HashMap<>();
            hostData.put("id", host.getId());
            hostData.put("name", host.getHostName());
            hostData.put("children", new ArrayList<Map<String, Object>>());
            if (siteMap.containsKey(host.getSiteId())) {
                List<Map<String, Object>> children = (List<Map<String, Object>>) siteMap.get(host.getSiteId()).get("children");
                // Use Set to check duplicates
                Set<Long> childIds = new HashSet<>();
                for (Map<String, Object> child : children) {
                    childIds.add((Long) child.get("id"));
                }
                if (!childIds.contains(host.getId())) {
                    children.add(hostData);
                }
            }
        }

        return result;
    }

    @Override
    public Map<String, Object> queryTreeSix(BaseUnitInformationVO vo) {
        List<BaseUnitInformation> units = queryAllUnits(vo); // Fetch units
        List<BaseSiteInformation> sites = queryAllSites(); // Fetch sites
        List<BaseSiteHost> hosts = queryAllHosts(); // Fetch hosts
        List<BaseSiteBatteryPack> packs = queryAllPacks();

        // Map for the first three layers
        Map<Long, Map<String, Object>> unitMap = new HashMap<>();
        // Track added unit IDs
        Set<Long> addedUnitIds = new HashSet<>();
        // Store BaseUnitInformation by id
        Map<Long, BaseUnitInformation> unitInfoMap = new HashMap<>();

        // Build the first three layers
        for (BaseUnitInformation unit : units) {
            unitInfoMap.put(unit.getId(), unit);
            Map<String, Object> unitData = new HashMap<>();
            unitData.put("id", unit.getId());
            unitData.put("name", unit.getUnitName());
            unitData.put("children", new ArrayList<Map<String, Object>>());
            unitMap.put(unit.getId(), unitData);
        }

        // Prepare final structure
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> unitList = new ArrayList<>();
        result.put("units", unitList);

        // Build the first layer
        for (BaseUnitInformation unit : units) {
            if (unit.getPid() == null) { // First layer
                if (!addedUnitIds.contains(unit.getId())) {
                    addedUnitIds.add(unit.getId());
                    Map<String, Object> unitData = unitMap.get(unit.getId());
                    unitData.put("children", new ArrayList<>());
                    unitList.add(unitData);
                }
            }
        }

        // Build the second and third layers
        for (BaseUnitInformation unit : units) {
            if (unit.getPid()!= null) { // Second and third layers
                Map<String, Object> parent = unitMap.get(unit.getPid());
                if (parent!= null) {
                    List<Map<String, Object>> children = (List<Map<String, Object>>) parent.get("children");
                    // Use Set to check duplicates
                    Set<Long> childIds = new HashSet<>();
                    for (Map<String, Object> child : children) {
                        childIds.add((Long) child.get("id"));
                    }
                    if (!childIds.contains(unit.getId())) {
                        children.add(unitMap.get(unit.getId()));
                    }
                }
            }
        }

        // Build fourth layer
        Map<Long, Map<String, Object>> siteMap = new HashMap<>();
        Set<Long> addedSiteIds = new HashSet<>(); // Track added site IDs
        for (BaseSiteInformation site : sites) {
            Map<String, Object> siteData = new HashMap<>();
            siteData.put("id", site.getId());
            siteData.put("name", site.getSiteName());
            siteData.put("children", new ArrayList<Map<String, Object>>());
            siteMap.put(site.getId(), siteData);
        }

        for (BaseSiteInformation site : sites) {
            if (unitMap.containsKey(site.getUnitId())) {
                List<Map<String, Object>> children = (List<Map<String, Object>>) unitMap.get(site.getUnitId()).get("children");
                // Use Set to check duplicates
                Set<Long> childIds = new HashSet<>();
                for (Map<String, Object> child : children) {
                    childIds.add((Long) child.get("id"));
                }
                if (!childIds.contains(site.getId())) {
                    children.add(siteMap.get(site.getId()));
                }
            }
        }

        // Build fifth layer
        Map<Long, Map<String, Object>> hostMap = new HashMap<>();
        Set<Long> addedHostIds = new HashSet<>(); // Track added site IDs
        for (BaseSiteBatteryPack baseSiteBatteryPack : packs) {
            Map<String, Object> siteData = new HashMap<>();
            siteData.put("id", baseSiteBatteryPack.getId());
            siteData.put("name", baseSiteBatteryPack.getHostName());
            siteData.put("children", new ArrayList<Map<String, Object>>());
            hostMap.put(baseSiteBatteryPack.getId(), siteData);
        }

        for (BaseSiteHost host : hosts) {
            Map<String, Object> hostData = new HashMap<>();
            hostData.put("id", host.getId());
            hostData.put("name", host.getHostName());
            hostData.put("children", new ArrayList<Map<String, Object>>());
            if (siteMap.containsKey(host.getSiteId())) {
                List<Map<String, Object>> children = (List<Map<String, Object>>) siteMap.get(host.getSiteId()).get("children");
                // Use Set to check duplicates
                Set<Long> childIds = new HashSet<>();
                for (Map<String, Object> child : children) {
                    childIds.add((Long) child.get("id"));
                }
                if (!childIds.contains(host.getId())) {
                    children.add(hostData);
                }
            }
        }

        // Build sixth layer
        Map<Long, Map<String, Object>> packMap = new HashMap<>();
        Set<Long> addedPackIds = new HashSet<>(); // Track added pack IDs
        for (BaseSiteBatteryPack pack : packs) {
            Map<String, Object> packData = new HashMap<>();
            packData.put("id", pack.getId());
            packData.put("name", pack.getBatteryPackName());
            packData.put("children", new ArrayList<Map<String, Object>>());
            packMap.put(pack.getId(), packData);
        }

        for (BaseSiteBatteryPack pack : packs) {
            if (hostMap.containsKey(pack.getHostId())) {
                List<Map<String, Object>> children = (List<Map<String, Object>>) hostMap.get(pack.getHostId()).get("children");
                // Use Set to check duplicates
                Set<Long> childIds = new HashSet<>();
                for (Map<String, Object> child : children) {
                    childIds.add((Long) child.get("id"));
                }
                if (!childIds.contains(pack.getId())) {
                    children.add(packMap.get(pack.getId()));
                }
            }
        }

        return result;
    }

    private List<BaseUnitInformation> queryAllUnits(BaseUnitInformationVO vo) {
        vo.setState(1);
        List<BaseUnitInformation> list = queryAll(vo);
        return list;
    }

    private List<BaseSiteInformation> queryAllSites() {
        BaseSiteInformationVO siteInformationVO = new BaseSiteInformationVO();
        List<BaseSiteInformation> list = baseSiteInformationService.queryAll(siteInformationVO);
        return list;
    }

    private List<BaseSiteHost> queryAllHosts() {
        BaseSiteHostVO hostVO = new BaseSiteHostVO();
        List<BaseSiteHost> list = baseSiteHostService.queryAll(hostVO);
        return list;
    }
    private List<BaseSiteBatteryPack> queryAllPacks() {
        BaseSiteBatteryPackVO packVO = new BaseSiteBatteryPackVO();
        List<BaseSiteBatteryPack> list = baseSiteBatteryPackService.queryAll(packVO);
        return list;
    }

}
