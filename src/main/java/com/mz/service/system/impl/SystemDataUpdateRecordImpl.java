package com.mz.service.system.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.ConstantsUtil;
import com.mz.common.context.PageInfo;
import com.mz.common.util.IdWorker;
import com.mz.common.util.StringUtils;
import com.mz.framework.util.redis.RedisUtil;
import com.mz.mapper.system.SystemDataUpdateRecordMapper;
import com.mz.model.base.BaseUser;
import com.mz.model.system.SystemDataServiceNode;
import com.mz.model.system.SystemDataUpdateRecord;
import com.mz.model.system.vo.SystemDataUpdateRecordVO;
import com.mz.service.system.SystemDataServiceNodeService;
import com.mz.service.system.SystemDataUpdateRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * 服务节点版本更新记录(SystemDataUpdateRecord)表服务实现类
 *
 * @author makejava
 * @since 2023-12-29 10:01:10
 */
@Service("systemDataUpdateRecordService")
public class SystemDataUpdateRecordImpl extends ServiceImpl<SystemDataUpdateRecordMapper, SystemDataUpdateRecord> implements SystemDataUpdateRecordService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SystemDataServiceNodeService systemDataServiceNodeService;

    @Override
    public SystemDataUpdateRecord insert(SystemDataUpdateRecord pojo, String loginID){
        String baseUserStr = redisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO + loginID);
        JSONObject baseUserJson = JSONObject.parseObject(baseUserStr);
        BaseUser baseUser = JSONObject.toJavaObject(baseUserJson, BaseUser.class);
        pojo.setModifyUser(baseUser.getRealName());
        pojo.setModifyTime(DateUtil.now());
        SystemDataServiceNode serviceNode = systemDataServiceNodeService.getById(pojo.getNodeId());
        pojo.setNodeName(serviceNode.getNodeName());
        if(pojo.getId()==null){
            IdWorker idWorker = new IdWorker(0L, 0L);
            pojo.setId(idWorker.nextId());
            pojo.setCreateUser(baseUser.getRealName());
            pojo.setCreateTime(DateUtil.now());
            pojo.setDelState(ConstantsUtil.IS_DONT_DEL);
            pojo.setState(ConstantsUtil.STATE_NORMAL);//启用状态 1正常-1禁用
            save(pojo);
        }else{
            updateById(pojo);
        }
        return pojo;
    }

    public PageInfo<SystemDataUpdateRecord> queryAllByLimit(SystemDataUpdateRecordVO vo) {
        PageHelper.startPage(vo.getPageNo(), vo.getPageSize());
        List<SystemDataUpdateRecord> list = queryAll(vo);
        PageInfo<SystemDataUpdateRecord> pageInfo = new PageInfo<SystemDataUpdateRecord>(list);
        return pageInfo;
    }

    @Override
    public List<SystemDataUpdateRecord> queryAll(SystemDataUpdateRecordVO vo) {
        LambdaQueryChainWrapper<SystemDataUpdateRecord> lambdaQuery = lambdaQuery();
        lambdaQuery.eq(SystemDataUpdateRecord::getDelState, ConstantsUtil.IS_DONT_DEL);
        if(vo.getNodeId()!=null){
            lambdaQuery.eq(SystemDataUpdateRecord::getNodeId,vo.getNodeId());
        }
        if(!StringUtils.isEmpty(vo.getCreateTimeS())){
            lambdaQuery.ge(SystemDataUpdateRecord::getCreateTime,vo.getCreateTimeS());
        }
        if(!StringUtils.isEmpty(vo.getCreateTimeE())){
            lambdaQuery.le(SystemDataUpdateRecord::getCreateTime,vo.getCreateTimeE()+" 23:59:59");
        }
        if(!StringUtils.isEmpty(vo.getVersionNo())){
            lambdaQuery.like(SystemDataUpdateRecord::getVersionNo,vo.getVersionNo());
        }
        if(!StringUtils.isEmpty(vo.getUpdateContent())){
            lambdaQuery.like(SystemDataUpdateRecord::getUpdateContent,vo.getUpdateContent());
        }
        List<SystemDataUpdateRecord> list = lambdaQuery.orderByDesc(SystemDataUpdateRecord::getCreateTime).list();
        return list;
    }

}
