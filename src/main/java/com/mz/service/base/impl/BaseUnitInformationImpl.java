package com.mz.service.base.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.github.pagehelper.PageHelper;
import com.mz.common.util.Result;
import com.mz.framework.util.redis.RedisUtil;
import com.mz.mapper.localhost.BaseUnitInformationMapper;
import com.mz.model.base.BaseUnitInformation;
import com.mz.model.base.model.BaseUnitInformationModel;
import com.mz.model.base.vo.BaseUnitInformationVO;
import com.mz.service.base.BaseUnitInformationService;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.ConstantsUtil;
import com.mz.common.util.IdWorker;
import com.mz.common.context.PageInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mz.model.base.BaseUser;
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
    public List<BaseUnitInformationModel> queryTreeList(BaseUnitInformationVO vo) {
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


}
