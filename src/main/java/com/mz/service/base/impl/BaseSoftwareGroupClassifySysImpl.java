package com.mz.service.base.impl;

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
import com.mz.mapper.localhost.BaseSoftwareGroupClassifySysMapper;
import com.mz.model.base.BaseSoftwareGroupClassifySys;
import com.mz.model.base.BaseUser;
import com.mz.model.base.SysArea;
import com.mz.model.base.vo.BaseSoftwareGroupClassifySysVO;
import com.mz.service.base.BaseSoftwareGroupClassifySysService;
import com.mz.service.base.SysAreaService;
import com.mz.service.base.SysDeftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;
/**
 * 应用分类关联应用(BaseSoftwareGroupClassifySys)表服务实现类
 *
 * @author makejava
 * @since 2023-08-30 10:36:37
 */
@Service("baseSoftwareGroupClassifySysService")
public class BaseSoftwareGroupClassifySysImpl extends ServiceImpl<BaseSoftwareGroupClassifySysMapper, BaseSoftwareGroupClassifySys> implements BaseSoftwareGroupClassifySysService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SysDeftService sysDeftService;
    @Autowired
    private SysAreaService sysAreaService;

    @Override
    public BaseSoftwareGroupClassifySys insert(BaseSoftwareGroupClassifySys pojo, String loginID) {
        String baseUserStr = redisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginID);
        JSONObject baseUserJson = JSONObject.parseObject(baseUserStr);
        BaseUser baseUser = JSONObject.toJavaObject(baseUserJson, BaseUser.class);
        pojo.setModifyUser(baseUser.getRealName());
        pojo.setModifyTime(DateUtil.now());
        if (!StringUtils.isEmpty(pojo.getReleaseScope())) {
            SysArea sysArea = sysAreaService.queryByCode(pojo.getReleaseScope());
            if (sysArea != null) {
                pojo.setReleaseScopeName(sysArea.getAreaName());
            }
        }
        if (pojo.getId() == null) {
            IdWorker idWorker = new IdWorker(0L, 0L);
            pojo.setId(idWorker.nextId());
            pojo.setCreateUser(baseUser.getRealName());
            pojo.setCreateTime(DateUtil.now());
            pojo.setDelState(ConstantsUtil.IS_DONT_DEL);
            pojo.setState(ConstantsUtil.STATE_NORMAL);//启用状态 1正常-1禁用
            save(pojo);
        } else {
            updateById(pojo);
        }
        return pojo;
    }


    @Override
    public PageInfo<BaseSoftwareGroupClassifySys> queryAllByLimit(BaseSoftwareGroupClassifySysVO vo) {
        PageHelper.startPage(vo.getPageNo(), vo.getPageSize());
        List<BaseSoftwareGroupClassifySys> list = queryAll(vo);
        PageInfo<BaseSoftwareGroupClassifySys> pageInfo = new PageInfo<BaseSoftwareGroupClassifySys>(list);
        return pageInfo;
    }

    public List<BaseSoftwareGroupClassifySys> queryAll(BaseSoftwareGroupClassifySysVO vo) {
        LambdaQueryChainWrapper<BaseSoftwareGroupClassifySys> lambdaQuery = lambdaQuery();
        lambdaQuery.eq(BaseSoftwareGroupClassifySys::getDelState, ConstantsUtil.IS_DONT_DEL);
        if (vo.getTenantId() != null) {
            lambdaQuery.eq(BaseSoftwareGroupClassifySys::getTenantId, vo.getTenantId());
        }
        if (vo.getGroupId() != null) {
            lambdaQuery.eq(BaseSoftwareGroupClassifySys::getGroupId, vo.getGroupId());
        }
        if (vo.getClassifyId() != null) {
            lambdaQuery.eq(BaseSoftwareGroupClassifySys::getClassifyId, vo.getClassifyId());
        }
        if (vo.getSysId() != null) {
            lambdaQuery.eq(BaseSoftwareGroupClassifySys::getSysId, vo.getSysId());
        }
        if (vo.getBelongType() != null) {
            lambdaQuery.eq(BaseSoftwareGroupClassifySys::getBelongType, vo.getBelongType());
        }
        if (vo.getState() != null) {
            lambdaQuery.eq(BaseSoftwareGroupClassifySys::getState, vo.getState());
        }
        if (!StringUtils.isEmpty(vo.getSysName())) {
            lambdaQuery.like(BaseSoftwareGroupClassifySys::getSysName, vo.getSysName());
        }
        List<BaseSoftwareGroupClassifySys> list = lambdaQuery.orderByAsc(BaseSoftwareGroupClassifySys::getSysOrder).orderByDesc(BaseSoftwareGroupClassifySys::getId).list();
        return list;
    }
}
