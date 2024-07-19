package com.mz.service.base.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.mz.common.util.Result;
import com.mz.common.util.wxaes.HttpKit;
import com.mz.mapper.localhost.BaseTenantMapper;
import com.mz.model.base.*;
import com.mz.model.base.vo.BaseTenantVO;
import com.mz.model.system.SystemDataServiceNode;
import com.mz.service.base.BaseTenantService;
import com.mz.service.base.SysAreaService;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.ConstantsUtil;
import com.mz.common.util.IdWorker;
import com.mz.common.context.PageInfo;

import java.util.List;

import com.mz.framework.util.redis.RedisUtil;
import com.mz.service.system.SystemDataServiceNodeService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 租户表(BaseTenant)表服务实现类
 *
 * @author makejava
 * @since 2024-07-18 13:47:46
 */
@Service("baseTenantService")
public class BaseTenantImpl extends ServiceImpl<BaseTenantMapper, BaseTenant> implements BaseTenantService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SysAreaService sysAreaService;
    @Autowired
    private SystemDataServiceNodeService systemDataServiceNodeService;

    @Override
    public Result insert(BaseTenant pojo, String loginID) {
        String baseUserStr = redisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginID);
        JSONObject baseUserJson = JSONObject.parseObject(baseUserStr);
        BaseUser baseUser = JSONObject.toJavaObject(baseUserJson, BaseUser.class);
        if (!StringUtils.isEmpty(pojo.getAreaCode())) {
            SysArea sysArea = sysAreaService.queryByCode(pojo.getAreaCode());
            if (sysArea != null) {
                pojo.setAreaName(sysArea.getAreaName());
            }
        }
        if (ObjectUtil.isNotEmpty(pojo.getServiceNodeId())) {
            SystemDataServiceNode serviceNode = systemDataServiceNodeService.getById(pojo.getServiceNodeId());
            if (ObjectUtil.isNotEmpty(serviceNode) && ObjectUtil.isNotEmpty(serviceNode.getNodeName())) {
                pojo.setServiceNodeName(serviceNode.getNodeName());
            }
        }
        if (pojo.getId() == null) {
            BaseTenantVO newVO = new BaseTenantVO();
            newVO.setTenantNameEQ(pojo.getTenantName());
            List<BaseTenant> list = queryAll(newVO);
            if (CollectionUtil.isNotEmpty(list)) {
                return Result.failed("租户名称重复");
            }
            IdWorker idWorker = new IdWorker(0L, 0L);
            pojo.setId(idWorker.nextId());
            pojo.setCreatUser(baseUser.getRealName());
            pojo.setCreatTime(DateUtil.now());
            pojo.setModifyUser(baseUser.getRealName());
            pojo.setModifyTime(DateUtil.now());
            pojo.setUseState(ConstantsUtil.STATE_NORMAL);
            pojo.setIssuedState(0);
            save(pojo);
        } else {
            BaseTenantVO newVO = new BaseTenantVO();
            newVO.setId(pojo.getId());
            newVO.setTenantNameEQ(pojo.getTenantName());
            List<BaseTenant> list = queryAll(newVO);
            if (CollectionUtil.isNotEmpty(list)) {
                return Result.failed("租户名称重复");
            }
            pojo.setModifyUser(baseUser.getRealName());
            pojo.setModifyTime(DateUtil.now());
            updateById(pojo);
        }
        return Result.success(pojo);
    }

    @Override
    public PageInfo<BaseTenant> queryAllByLimit(BaseTenantVO vo) {
        PageHelper.startPage(vo.getPageNo(), vo.getPageSize());
        List<BaseTenant> list = queryAll(vo);
        PageInfo<BaseTenant> pageInfo = new PageInfo<BaseTenant>(list);
        return pageInfo;
    }

    @Override
    public List<BaseTenant> queryAll(BaseTenantVO vo) {
        LambdaQueryChainWrapper<BaseTenant> lambdaQuery = lambdaQuery();
        lambdaQuery.eq(BaseTenant::getUseState, ConstantsUtil.IS_DONT_DEL);
        if (ObjectUtil.isNotEmpty(vo.getId())) {
            lambdaQuery.ne(BaseTenant::getId, vo.getId());
        }
        if (ObjectUtil.isNotEmpty(vo.getServiceNodeId())) {
            lambdaQuery.eq(BaseTenant::getServiceNodeId, vo.getServiceNodeId());
        }
        if (ObjectUtil.isNotEmpty(vo.getTenantNameEQ())) {
            lambdaQuery.eq(BaseTenant::getTenantName, vo.getTenantNameEQ());
        }
        if (ObjectUtil.isNotEmpty(vo.getTenantName())) {
            lambdaQuery.like(BaseTenant::getTenantName, vo.getTenantName());
        }
        List<BaseTenant> list = lambdaQuery.orderByDesc(BaseTenant::getCreatTime).list();
        return list;
    }

    @SneakyThrows
    @Override
    public Integer issued(Long id) {
        BaseTenant tenant = getById(id);
        Integer reissuedState = 0;

        if (tenant != null && ObjectUtil.isNotEmpty(tenant.getServiceNodeId())) {
            SystemDataServiceNode systemDataServiceNode = systemDataServiceNodeService.getById(tenant.getServiceNodeId());

            if (systemDataServiceNode != null && ObjectUtil.isNotEmpty(systemDataServiceNode.getNodeUrl())) {
                String url = systemDataServiceNode.getNodeUrl();
                String tenantUrl = url + "/baseTenant/receipt";

                Gson gson = new Gson();
                String result = gson.toJson(tenant);

                try {
                    String response = HttpKit.post(tenantUrl, result);

                    if (response.contains("10000")) {
                        JSONObject jsonObjectAuth = JSONObject.parseObject(response);
                        String returnCode = jsonObjectAuth.getString("code");

                        if (returnCode.equals("10000")) {
                            tenant.setIssuedState(1);
                            reissuedState = 1;
                        }
                    } else {
                        tenant.setIssuedState(-1);
                        reissuedState = -1;
                    }

                    updateById(tenant);
                } catch (Exception e) {
                    e.printStackTrace();
                    reissuedState = -1;
                }
            }
        }
        return reissuedState;
    }

    @SneakyThrows
    @Override
    public Integer reissued(Long id) { //1-重新下发成功 -1 重新下发失败
        BaseTenant tenant = getById(id);
        Integer reissuedState = 0;

        if (tenant != null && ObjectUtil.isNotEmpty(tenant.getServiceNodeId())) {
            SystemDataServiceNode systemDataServiceNode = systemDataServiceNodeService.getById(tenant.getServiceNodeId());

            if (systemDataServiceNode != null && ObjectUtil.isNotEmpty(systemDataServiceNode.getNodeUrl())) {
                String url = systemDataServiceNode.getNodeUrl();
                String tenantUrl = url + "/baseTenant/receipt";

                Gson gson = new Gson();
                String result = gson.toJson(tenant);

                try {
                    String response = HttpKit.post(tenantUrl, result);

                    if (response.contains("10000")) {
                        JSONObject jsonObjectAuth = JSONObject.parseObject(response);
                        String returnCode = jsonObjectAuth.getString("code");

                        if (returnCode.equals("10000")) {
                            tenant.setIssuedState(1);
                            reissuedState = 1;
                        }
                    } else {
                        tenant.setIssuedState(-1);
                        reissuedState = -1;

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    reissuedState = -1;
                }
            }
        }
        return reissuedState;
    }

}
