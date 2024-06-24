package com.mz.service.system.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.ConstantsUtil;
import com.mz.common.context.PageInfo;
import com.mz.common.util.IdWorker;
import com.mz.common.util.StringUtils;
import com.mz.framework.util.redis.RedisUtil;
import com.mz.framework.websocket.entity.EnumMessageType;
import com.mz.framework.websocket.entity.Message;
import com.mz.mapper.system.SystemDataServiceNodeMapper;
import com.mz.model.base.BaseUser;
import com.mz.model.system.SystemDataServiceNode;
import com.mz.model.system.SystemDataUpdateRecord;
import com.mz.model.system.model.SystemDataServiceNodeModel;
import com.mz.model.system.vo.SystemDataServiceNodeVO;
import com.mz.model.system.vo.SystemDataUpdateRecordVO;
import com.mz.service.system.SystemDataServiceNodeService;
import com.mz.service.system.SystemDataUpdateRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 服务节点(SystemDataServiceNode)表服务实现类
 *
 * @author makejava
 * @since 2023-12-29 10:01:11
 */
@Slf4j
@Service("systemDataServiceNodeService")
public class SystemDataServiceNodeImpl extends ServiceImpl<SystemDataServiceNodeMapper, SystemDataServiceNode> implements SystemDataServiceNodeService {
    @Autowired
    private RedisUtil redisUtil;
    @Resource
    private SystemDataUpdateRecordService systemDataUpdateRecordService;

    @Override
    public SystemDataServiceNode insert(SystemDataServiceNode pojo, String loginID) {
        String baseUserStr = redisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginID);
        JSONObject baseUserJson = JSONObject.parseObject(baseUserStr);
        BaseUser baseUser = JSONObject.toJavaObject(baseUserJson, BaseUser.class);
        pojo.setModifyUser(baseUser.getRealName());
        Date curDate = new Date();
        pojo.setModifyTime(curDate);
        if (pojo.getId() == null) {
            IdWorker idWorker = new IdWorker(0L, 0L);
            pojo.setId(idWorker.nextId());
            pojo.setCreateUser(baseUser.getRealName());
            pojo.setCreateTime(curDate);
            pojo.setDelState(ConstantsUtil.IS_DONT_DEL);
            pojo.setState(ConstantsUtil.STATE_NORMAL);//启用状态 1正常-1禁用
            pojo.setOnlineState(ConstantsUtil.STATE_NORMAL);//启用状态 1正常-1禁用
            save(pojo);
        } else {
            updateById(pojo);
        }
        return pojo;
    }

    public PageInfo<SystemDataServiceNode> queryAllByLimit(SystemDataServiceNodeVO vo) {
        PageHelper.startPage(vo.getPageNo(), vo.getPageSize());
        List<SystemDataServiceNode> list = queryAll(vo);
        PageInfo<SystemDataServiceNode> pageInfo = new PageInfo<SystemDataServiceNode>(list);
        return pageInfo;
    }

    @Override
    public List<SystemDataServiceNode> queryAll(SystemDataServiceNodeVO vo) {
        LambdaQueryChainWrapper<SystemDataServiceNode> lambdaQuery = lambdaQuery();
        lambdaQuery.eq(SystemDataServiceNode::getDelState, ConstantsUtil.IS_DONT_DEL);
        Integer state = vo.getState();
        if (ObjectUtil.isNotNull(state)) {
            lambdaQuery.eq(SystemDataServiceNode::getState, state);
        }
        Integer onlineState = vo.getOnlineState();
        if (ObjectUtil.isNotNull(onlineState)) {
            lambdaQuery.eq(SystemDataServiceNode::getOnlineState, onlineState);
        }
        String nodeName = vo.getNodeName();
        if (StringUtils.isNotEmpty(nodeName)) {
            lambdaQuery.like(SystemDataServiceNode::getNodeName, nodeName);
        }
        String remarks = vo.getRemarks();
        if (StringUtils.isNotEmpty(remarks)) {
            lambdaQuery.eq(SystemDataServiceNode::getRemarks, remarks);
        }
        List<SystemDataServiceNode> list = lambdaQuery.orderByDesc(SystemDataServiceNode::getCreateTime).list();
        return list;
    }

    @Override
    public SystemDataServiceNodeModel getSystemDataServiceNodeCurMsg(SystemDataServiceNodeVO vo) {
        List<SystemDataServiceNode> systemDataServiceNodeList = this.queryAll(vo);
        if (CollectionUtil.isNotEmpty(systemDataServiceNodeList)) {
            SystemDataServiceNode systemDataServiceNode = systemDataServiceNodeList.stream().findFirst().get();

            SystemDataServiceNodeModel systemDataServiceNodeModel = new SystemDataServiceNodeModel();
            BeanUtil.copyProperties(systemDataServiceNode, systemDataServiceNodeModel);

            SystemDataUpdateRecordVO systemDataUpdateRecordVO = new SystemDataUpdateRecordVO();
            systemDataUpdateRecordVO.setNodeId(systemDataServiceNode.getId());
            List<SystemDataUpdateRecord> systemDataUpdateRecordList = this.systemDataUpdateRecordService.queryAll(systemDataUpdateRecordVO);
            if (CollectionUtil.isNotEmpty(systemDataUpdateRecordList)) {
                SystemDataUpdateRecord systemDataUpdateRecord = systemDataUpdateRecordList.stream().findFirst().get();
                systemDataServiceNodeModel.setMsg(systemDataUpdateRecord.getUpdateTime());
                systemDataServiceNodeModel.setContent(systemDataUpdateRecord.getUpdateContent());

            }
            return systemDataServiceNodeModel;
        }
        return null;
    }
}
