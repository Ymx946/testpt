package com.mz.service.base.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.github.pagehelper.PageHelper;
import com.mz.common.util.Result;
import com.mz.mapper.localhost.BaseTenantMapper;
import com.mz.model.base.BaseTenant;
import com.mz.model.base.vo.BaseTenantVO;
import com.mz.service.base.BaseTenantService;
import com.mz.service.base.SysAreaService;
import com.mz.model.base.SysArea;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.ConstantsUtil;
import com.mz.common.util.IdWorker;
import com.mz.common.context.PageInfo;
import java.util.List; 
import com.mz.framework.util.redis.RedisUtil;
import com.mz.model.base.BaseUser;
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

    @Override
    public Result insert(BaseTenant pojo, String loginID){
        String baseUserStr = redisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginID);
        JSONObject baseUserJson = JSONObject.parseObject(baseUserStr);
        BaseUser baseUser = JSONObject.toJavaObject(baseUserJson, BaseUser.class);
        if(!StringUtils.isEmpty(pojo.getAreaCode())){
            SysArea sysArea =  sysAreaService.queryByCode(pojo.getAreaCode());
            if(sysArea!=null){
                pojo.setAreaName(sysArea.getAreaName());
            }
        }
        if(pojo.getId()==null){
            BaseTenantVO newVO = new BaseTenantVO();
            newVO.setTenantNameEQ(pojo.getTenantName());
            List<BaseTenant> list = queryAll(newVO);
            if(CollectionUtil.isNotEmpty(list)){
                return Result.failed("租户名称重复");
            }
            IdWorker idWorker = new IdWorker(0L, 0L);
            pojo.setId(idWorker.nextId());
            pojo.setCreatUser(baseUser.getRealName());
            pojo.setCreatTime(DateUtil.now());
            pojo.setModifyUser(baseUser.getRealName());
            pojo.setModifyTime(DateUtil.now());
            pojo.setUseState(ConstantsUtil.STATE_NORMAL);
            save(pojo);
        }else{
            BaseTenantVO newVO = new BaseTenantVO();
            newVO.setId(pojo.getId());
            newVO.setTenantNameEQ(pojo.getTenantName());
            List<BaseTenant> list = queryAll(newVO);
            if(CollectionUtil.isNotEmpty(list)){
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
        if(ObjectUtil.isNotEmpty(vo.getId())){
            lambdaQuery.ne(BaseTenant::getId, vo.getId());
        }
        if (ObjectUtil.isNotEmpty(vo.getServiceNodeId())) {
            lambdaQuery.eq(BaseTenant::getServiceNodeId, vo.getServiceNodeId());
        }
        if (ObjectUtil.isNotEmpty(vo.getTenantNameEQ())) {
            lambdaQuery.eq(BaseTenant::getTenantName, vo.getTenantName());
        }
        if (ObjectUtil.isNotEmpty(vo.getTenantName())) {
            lambdaQuery.like(BaseTenant::getTenantName, vo.getTenantName());
        }
        List<BaseTenant> list = lambdaQuery.orderByDesc(BaseTenant::getCreatTime).list();
        return list;
    }
}
