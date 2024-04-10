package com.mz.service.base.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.ConstantsUtil;
import com.mz.common.util.IdWorker;
import com.mz.framework.util.redis.RedisUtil;
import com.mz.mapper.localhost.BaseSoftwareGroupClassifyNodeMapper;
import com.mz.model.base.BaseSoftwareGroupClassifyNode;
import com.mz.model.base.BaseSoftwareGroupClassifySys;
import com.mz.model.base.BaseUser;
import com.mz.model.base.SysNode;
import com.mz.model.base.vo.BaseSoftwareGroupClassifyNodeVO;
import com.mz.service.base.BaseSoftwareGroupClassifyNodeService;
import com.mz.service.base.BaseSoftwareGroupClassifySysService;
import com.mz.service.base.SysNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应用分类关联应用节点(BaseSoftwareGroupClassifyNode)表服务实现类
 *
 * @author makejava
 * @since 2023-08-30 10:36:37
 */
@Service("baseSoftwareGroupClassifyNodeService")
public class BaseSoftwareGroupClassifyNodeImpl extends ServiceImpl<BaseSoftwareGroupClassifyNodeMapper, BaseSoftwareGroupClassifyNode> implements BaseSoftwareGroupClassifyNodeService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SysNodeService sysNodeService;
    @Autowired
    private BaseSoftwareGroupClassifySysService baseSoftwareGroupClassifySysService;

    @Override
    public void insertByNode(Long classifySysId, String nodeIdS, String loginID) {
        String baseUserStr = redisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO + loginID);
        JSONObject baseUserJson = JSONObject.parseObject(baseUserStr);
        BaseUser baseUser = JSONObject.toJavaObject(baseUserJson, BaseUser.class);
        BaseSoftwareGroupClassifySys classifySys = baseSoftwareGroupClassifySysService.getById(classifySysId);
        Map<String, SysNode> nodeMap = new HashMap<>();
        List<SysNode> nodeList = sysNodeService.queryAllBySys(classifySys.getSysCode());
        if (CollectionUtil.isNotEmpty(nodeList)) {
            for (SysNode sysNode : nodeList) {
                nodeMap.put(sysNode.getId(), sysNode);
            }
        }
        //删除原配置节点
        BaseSoftwareGroupClassifyNodeVO findClassifyNodeVO = new BaseSoftwareGroupClassifyNodeVO();
        findClassifyNodeVO.setClassifySysId(classifySysId);
        List<BaseSoftwareGroupClassifyNode> classifyNodeList = queryAll(findClassifyNodeVO);
        removeBatchByIds(classifyNodeList);
        if (!StringUtils.isEmpty(nodeIdS)) {
            List<BaseSoftwareGroupClassifyNode> newClassifyNodeList = new ArrayList<>();
            IdWorker idWorker = new IdWorker(0L, 0L);
            String[] nodeIdArr = nodeIdS.split(",");
            for (String s : nodeIdArr) {
                SysNode node = nodeMap.get(s.trim());
                if (node != null) {
                    BaseSoftwareGroupClassifyNode classifyNode = new BaseSoftwareGroupClassifyNode();
                    BeanUtil.copyProperties(classifySys, classifyNode);
                    classifyNode.setId(idWorker.nextId());
                    classifyNode.setClassifySysId(classifySys.getId());
                    classifyNode.setNodeId(Long.valueOf(s.trim()));
                    classifyNode.setNodeName(node.getNodeName());
                    classifyNode.setCreateUser(baseUser.getRealName());
                    classifyNode.setCreateTime(DateUtil.now());
                    classifyNode.setModifyUser(baseUser.getRealName());
                    classifyNode.setModifyTime(DateUtil.now());
                    classifyNode.setDelState(ConstantsUtil.IS_DONT_DEL);
                    classifyNode.setState(ConstantsUtil.STATE_NORMAL);
                    newClassifyNodeList.add(classifyNode);
                }
            }
            saveBatch(newClassifyNodeList);
        }
        updateSysState(classifySys);
    }

    public void updateSysState(BaseSoftwareGroupClassifySys classifySys) {
        BaseSoftwareGroupClassifyNodeVO findClassifyNodeVO = new BaseSoftwareGroupClassifyNodeVO();
        findClassifyNodeVO.setClassifySysId(classifySys.getId());
        List<BaseSoftwareGroupClassifyNode> classifyNodeList = queryAll(findClassifyNodeVO);
        if (CollectionUtil.isNotEmpty(classifyNodeList)) {//有节点配置-已配置
            classifySys.setState(ConstantsUtil.STATE_NORMAL);
        } else {//无节点配置-未配置
            classifySys.setState(ConstantsUtil.STATE_UN_NORMAL);
        }
        baseSoftwareGroupClassifySysService.updateById(classifySys);
    }

    public List<BaseSoftwareGroupClassifyNode> queryAll(BaseSoftwareGroupClassifyNodeVO vo) {
        LambdaQueryChainWrapper<BaseSoftwareGroupClassifyNode> lambdaQuery = lambdaQuery();
        lambdaQuery.eq(BaseSoftwareGroupClassifyNode::getDelState, ConstantsUtil.IS_DONT_DEL);
        if (vo.getTenantId() != null) {
            lambdaQuery.eq(BaseSoftwareGroupClassifyNode::getTenantId, vo.getTenantId());
        }
        if (vo.getGroupId() != null) {
            lambdaQuery.eq(BaseSoftwareGroupClassifyNode::getGroupId, vo.getGroupId());
        }
        if (vo.getClassifyId() != null) {
            lambdaQuery.eq(BaseSoftwareGroupClassifyNode::getClassifyId, vo.getClassifyId());
        }
        if (vo.getClassifySysId() != null) {
            lambdaQuery.eq(BaseSoftwareGroupClassifyNode::getClassifySysId, vo.getClassifySysId());
        }
        if (vo.getSysId() != null) {
            lambdaQuery.eq(BaseSoftwareGroupClassifyNode::getSysId, vo.getSysId());
        }
        List<BaseSoftwareGroupClassifyNode> list = lambdaQuery.orderByDesc(BaseSoftwareGroupClassifyNode::getCreateTime).list();
        return list;
    }

}
