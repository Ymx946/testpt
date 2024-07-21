package com.mz.service.base.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.context.PageInfo;
import com.mz.common.util.IdWorker;
import com.mz.framework.util.redis.RedisUtil;
import com.mz.mapper.localhost.SysOperaStatisticsMapper;
import com.mz.model.base.BaseUser;
import com.mz.model.base.SysArea;
import com.mz.model.base.SysOperaStatistics;
import com.mz.model.base.vo.SysOperaStatisticsVO;
import com.mz.service.base.SysAreaService;
import com.mz.service.base.SysOperaStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作统计表(SysOperaStatistics)表服务实现类
 *
 * @author makejava
 * @since 2024-05-11 10:52:27
 */
@Service("sysOperaStatisticsService")
public class SysOperaStatisticsImpl extends ServiceImpl<SysOperaStatisticsMapper, SysOperaStatistics> implements SysOperaStatisticsService {
    @Autowired
    private RedisUtil myRedisUtil;
    @Autowired
    private SysAreaService sysAreaService;

    @Override
    public SysOperaStatistics insert(SysOperaStatistics pojo) {
        String areaCode = pojo.getAreaCode();
        if (StringUtil.isNotEmpty(areaCode) && StringUtil.isEmpty(pojo.getAreaName())) {
            SysArea sysArea = sysAreaService.queryByCode(areaCode);
            if (ObjectUtil.isNotEmpty(sysArea)) {
                pojo.setAreaName(sysArea.getAreaName());
            }
        }
        if (ObjectUtil.isEmpty(pojo.getId())) {
            IdWorker idWorker = new IdWorker(0L, 0L);
            pojo.setId(idWorker.nextId());
            pojo.setCreateTime(DateUtil.now());
            save(pojo);
        } else {
            updateById(pojo);
        }
        return pojo;
    }

    @Override
    public SysOperaStatistics insert(SysOperaStatistics pojo, String loginID) {
        JSONObject baseUserJson = JSONObject.parseObject(myRedisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginID));
        BaseUser baseUser = JSONObject.toJavaObject(baseUserJson, BaseUser.class);
        String areaCode = pojo.getAreaCode();
        if (StringUtil.isNotEmpty(areaCode) && StringUtil.isEmpty(pojo.getAreaName())) {
            SysArea sysArea = sysAreaService.queryByCode(areaCode);
            if (ObjectUtil.isEmpty(sysArea)) {
                pojo.setAreaName(sysArea.getAreaName());
            }
        }
        pojo.setTenantId(Long.valueOf(baseUser.getTenantId()));
        if (ObjectUtil.isEmpty(pojo.getId())) {
            IdWorker idWorker = new IdWorker(0L, 0L);
            pojo.setId(idWorker.nextId());
            pojo.setCreateTime(DateUtil.now());
            save(pojo);
        } else {
            updateById(pojo);
        }
        return pojo;
    }

    @Override
    public Long queryAllCount(SysOperaStatisticsVO vo) {
        return this.queryChainWrapper(vo).count();
    }

    @Override
    public List<SysOperaStatistics> queryAll(SysOperaStatisticsVO vo) {
        LambdaQueryChainWrapper<SysOperaStatistics> sysOperaStatisticsLambdaQueryChainWrapper = this.queryChainWrapper(vo);
        String sortField = vo.getSortField();
        String orderBy = vo.getOrderBy();
        if (StringUtil.isNotEmpty(sortField)) {
            if ("loginNum".equalsIgnoreCase(sortField)) {
                if ("asc".equalsIgnoreCase(orderBy)) {
                    return this.queryChainWrapper(vo).orderByAsc(SysOperaStatistics::getLoginNum).list();
                } else {
                    return this.queryChainWrapper(vo).orderByDesc(SysOperaStatistics::getLoginNum).list();
                }
            }
            if ("busOperaNum".equalsIgnoreCase(sortField)) {
                if ("asc".equalsIgnoreCase(orderBy)) {
                    return this.queryChainWrapper(vo).orderByAsc(SysOperaStatistics::getBusOperaNum).list();
                } else {
                    return this.queryChainWrapper(vo).orderByDesc(SysOperaStatistics::getBusOperaNum).list();
                }
            }
            if ("operaTime".equalsIgnoreCase(sortField)) {
                if ("asc".equalsIgnoreCase(orderBy)) {
                    return this.queryChainWrapper(vo).orderByAsc(SysOperaStatistics::getOperaTime).list();
                } else {
                    return this.queryChainWrapper(vo).orderByDesc(SysOperaStatistics::getOperaTime).list();
                }
            }
        }
        return sysOperaStatisticsLambdaQueryChainWrapper.orderByDesc(SysOperaStatistics::getId).list();
    }

    @Override
    public PageInfo<SysOperaStatistics> queryAllByLimit(SysOperaStatisticsVO vo) {
        PageHelper.startPage(vo.getPageNo(), vo.getPageSize());
        List<SysOperaStatistics> list = queryAll(vo);
        PageInfo<SysOperaStatistics> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    private LambdaQueryChainWrapper<SysOperaStatistics> queryChainWrapper(SysOperaStatisticsVO vo) {
        LambdaQueryChainWrapper<SysOperaStatistics> sysOperaStatisticsLambdaQuery = this.lambdaQuery();
        Long tenantId = vo.getTenantId();
        if (ObjectUtil.isNotEmpty(tenantId)) {
            sysOperaStatisticsLambdaQuery.eq(SysOperaStatistics::getTenantId, tenantId);
        }
        final String areaCode = vo.getAreaCode();
        if (StringUtil.isNotEmpty(areaCode)) {
            sysOperaStatisticsLambdaQuery.likeRight(SysOperaStatistics::getAreaCode, areaCode);
        }
        String areaName = vo.getAreaName();
        if (StringUtil.isNotEmpty(areaName)) {
            sysOperaStatisticsLambdaQuery.like(SysOperaStatistics::getAreaName, areaName);
        }
        Long mainBodyId = vo.getMainBodyId();
        if (ObjectUtil.isNotEmpty(mainBodyId)) {
            sysOperaStatisticsLambdaQuery.eq(SysOperaStatistics::getMainBodyId, mainBodyId);
        }
        String mainBodyName = vo.getMainBodyName();
        if (StringUtil.isNotEmpty(mainBodyName)) {
            sysOperaStatisticsLambdaQuery.like(SysOperaStatistics::getMainBodyName, mainBodyName);
        }
        Long userId = vo.getUserId();
        if (ObjectUtil.isNotEmpty(userId)) {
            sysOperaStatisticsLambdaQuery.eq(SysOperaStatistics::getUserId, userId);
        }
        String loginName = vo.getLoginName();
        if (StringUtil.isNotEmpty(loginName)) {
            sysOperaStatisticsLambdaQuery.like(SysOperaStatistics::getLoginName, loginName);
        }
        String realName = vo.getRealName();
        if (StringUtil.isNotEmpty(realName)) {
            sysOperaStatisticsLambdaQuery.like(SysOperaStatistics::getRealName, realName);
        }
        String ip = vo.getIp();
        if (StringUtil.isNotEmpty(ip)) {
            sysOperaStatisticsLambdaQuery.like(SysOperaStatistics::getIp, ip);
        }
        String ipAddress = vo.getIpAddress();
        if (StringUtil.isNotEmpty(ipAddress)) {
            sysOperaStatisticsLambdaQuery.like(SysOperaStatistics::getIpAddress, ipAddress);
        }
        String operaEqpm = vo.getOperaEqpm();
        if (StringUtil.isNotEmpty(operaEqpm)) {
            sysOperaStatisticsLambdaQuery.like(SysOperaStatistics::getOperaEqpm, operaEqpm);
        }

        String startDate = vo.getStartDate();
        if (StringUtil.isNotEmpty(startDate)) {
            sysOperaStatisticsLambdaQuery.ge(SysOperaStatistics::getOperaTime, startDate + " 00:00:00");
        }
        String endDate = vo.getEndDate();
        if (StringUtil.isNotEmpty(endDate)) {
            sysOperaStatisticsLambdaQuery.le(SysOperaStatistics::getOperaTime, endDate + " 23:59:59");
        }

        String qkeyword = vo.getQkeyword();
        if (StringUtil.isNotEmpty(qkeyword)) {
            sysOperaStatisticsLambdaQuery.and(wrapper -> {
                wrapper.like(SysOperaStatistics::getMainBodyName, qkeyword).or()
                        .like(SysOperaStatistics::getLoginName, qkeyword).or()
                        .like(SysOperaStatistics::getRealName, qkeyword).or()
                        .like(SysOperaStatistics::getIpAddress, qkeyword).or()
                        .like(SysOperaStatistics::getOperaEqpm, qkeyword);
            });
        }
        return sysOperaStatisticsLambdaQuery;
    }
}
