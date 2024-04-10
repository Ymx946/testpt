package com.mz.service.base.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.ConstantsUtil;
import com.mz.common.context.PageInfo;
import com.mz.common.util.IdWorker;
import com.mz.common.util.Result;
import com.mz.common.util.StringFormatUtil;
import com.mz.framework.util.redis.RedisUtil;
import com.mz.mapper.localhost.BaseRoleNodeMapper;
import com.mz.mapper.localhost.SysDataDictMapper;
import com.mz.mapper.localhost.SysDeftMapper;
import com.mz.mapper.localhost.SysNodeMapper;
import com.mz.model.base.*;
import com.mz.model.base.model.SysNodeSelectModel;
import com.mz.model.base.vo.SysNodeVo;
import com.mz.service.base.BaseRoleService;
import com.mz.service.base.BaseSoftwareGroupClassifyNodeService;
import com.mz.service.base.BaseUserService;
import com.mz.service.base.SysNodeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 功能节点表(SysNode)表服务实现类
 *
 * @author makejava
 * @since 2021-03-17 11:00:22
 */
@Service("sysNodeService")
public class SysNodeServiceImpl extends ServiceImpl<SysNodeMapper, SysNode> implements SysNodeService {
    @Resource
    private SysNodeMapper sysNodeMapper;
    @Resource
    private BaseRoleNodeMapper baseRoleNodeMapper;
    @Resource
    private SysDataDictMapper sysDataDictMapper;
    @Resource
    private SysDeftMapper sysDeftMapper;
    @Resource
    private BaseRoleService baseRoleService;
    @Autowired
    private RedisUtil myRedisUtil;
    @Resource
    private SysNodeService sysNodeService;

    /**
     * 用户服务对象
     */
    @Resource
    private BaseUserService baseUserService;

    public static String getCode(String old, String num) {
        int n = num.length();
        int nums = Integer.parseInt(num) + 1;
        String newNum = String.valueOf(nums);
        n = Math.min(n, newNum.length());
        return old.subSequence(0, old.length() - n) + newNum;
    }

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SysNode queryById(String id) {

        return this.sysNodeMapper.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public PageInfo<SysNode> queryAllByLimit(int offset, int limit, String sysCode, Integer applicationType, String nodeName, String paraNodeCode, Integer nodeType, Integer button, Integer useState, HttpServletRequest request) {
        String[] sysCodeArr = null;
        BaseUser baseUser = baseUserService.getUser(request);
        if (baseUser.getUserLevel() > 1) {//除了超级管理员
            List<SysDeft> sysDeftList = this.sysDeftMapper.queryAllForTenant(baseUser.getTenantId(), applicationType,null);
            if (sysDeftList != null && sysDeftList.size() > 0) {
                sysCodeArr = new String[sysDeftList.size()];
                int i = 0;
                for (SysDeft sysDeft : sysDeftList) {
                    sysCodeArr[i] = sysDeft.getSysCode();
                    i++;
                }
            } else {//无权限则查询不到
                sysCodeArr = new String[]{"-1"};
            }
        }
        PageHelper.startPage(offset, limit);
        List<SysNode> list = sysNodeMapper.queryAllByLimit(sysCode, nodeName, paraNodeCode, nodeType, button, useState, sysCodeArr);
        return new PageInfo<SysNode>(list);
    }

    /**
     * 查询用户是否有权限
     *
     * @param userId  用户ID
     * @param sysCode 系统代码
     * @return 对象列表
     */
    @Override
    public int querySysPowerUser(String userId, String mainBodyId, String sysCode, Integer terminalType) {
        int a = sysNodeMapper.querySysPowerUser(userId, mainBodyId, sysCode, terminalType);//查出第一层
        if (a > 0) {
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * 查询权限节点
     *
     * @param sysCode 系统代码
     * @return 对象列表
     */
    @Override
    public List<SysNode> queryAllBySys(String sysCode) {
        List<SysNode> sysNodeList = sysNodeMapper.queryAllBySys(sysCode);//
        return sysNodeList;
    }

    /**
     * 查询权限节点
     *
     * @param userId  用户ID
     * @param sysCode 系统代码
     * @return 对象列表
     */
    @Override
    public List<SysNode> queryAllByUserApp(String userId, String mainBodyId, String sysCode, String paraNodeCode, Integer nodeType, Integer terminalType) {
        List<SysNode> sysNodeList = sysNodeMapper.queryAllByUserApp(userId, mainBodyId, sysCode, paraNodeCode, terminalType);//查出第一层
        return sysNodeList;
    }

    /**
     * 查询权限节点
     *
     * @param userId  用户ID
     * @param sysCode 系统代码
     * @return 对象列表
     */
    @Override
    public List<SysNodeVo> queryAllByUser(String userId, String mainBodyId, String sysCode, String paraNodeCode, Integer nodeType, Integer terminalType) {
        List<SysNodeVo> sysNodeVoList = new ArrayList<SysNodeVo>();
        if (nodeType == null || nodeType == 1) {//查全部或者查页面节点，需要分级
            List<SysNode> sysNodeList = sysNodeMapper.queryAllByUser(userId, mainBodyId, sysCode, paraNodeCode, terminalType);//查出第一层
            for (SysNode sysNode : sysNodeList) {//遍历第一层
                SysNodeVo sysNodeVo = new SysNodeVo();
                BeanUtils.copyProperties(sysNode, sysNodeVo);
                if (sysNodeVo.getLastStage() == 2) {//不是末级，则继续往下查
                    List<SysNodeVo> sysNodeVoList1 = getSonNodes(userId, mainBodyId, sysNodeVo, terminalType);//该查询一直循环至末级
                    sysNodeVo.setSonNodeVoList(sysNodeVoList1);
                }
                sysNodeVoList.add(sysNodeVo);
            }
        } else {//只查按钮，只加载一层
            List<SysNode> sysNodeList = sysNodeMapper.queryNodeByUserAndType(userId, mainBodyId, sysCode, paraNodeCode, nodeType, terminalType);//只查按钮
            for (SysNode sysNode : sysNodeList) {
                SysNodeVo sysNodeVo = new SysNodeVo();
                BeanUtils.copyProperties(sysNode, sysNodeVo);
                sysNodeVoList.add(sysNodeVo);
            }
        }
        return sysNodeVoList;

    }

    public List<SysNodeVo> getSonNodes(String userId, String mainBodyId, SysNodeVo sysNodeVo, Integer terminalType) {
        List<SysNodeVo> sysNodeVoList = null;
        List<SysNode> sysNodeList = sysNodeMapper.queryAllByUser(userId, mainBodyId, sysNodeVo.getSysCode(), sysNodeVo.getNodeCode(), terminalType);
        if (sysNodeList != null && sysNodeList.size() > 0) {
            sysNodeVoList = new ArrayList<SysNodeVo>();
            for (SysNode sysNode : sysNodeList) {
                SysNodeVo sonsysNodeVo = new SysNodeVo();
                BeanUtils.copyProperties(sysNode, sonsysNodeVo);
                if (sonsysNodeVo.getLastStage() == 2) {//不是末级，则继续往下查
                    List<SysNodeVo> sysNodeVoList1 = getSonNodes(userId, mainBodyId, sonsysNodeVo, terminalType);
                    sonsysNodeVo.setSonNodeVoList(sysNodeVoList1);
                }
                sysNodeVoList.add(sonsysNodeVo);
            }
        }
        return sysNodeVoList;

    }

    /**
     * 查询所有节点（分层级）
     *
     * @param sysCode 系统代码
     * @return 对象列表
     */
    @Override
    public List<SysNodeVo> queryAllForList(String roleId, String sysCode, Integer terminalType) {
        //传入角色ID判断节点是否被选中
        List<String> selectNodeIdList = new ArrayList<String>();
        if (roleId != null) {
            BaseRoleNode baseRoleNode = new BaseRoleNode();
            baseRoleNode.setRoleId(roleId);
            List<BaseRoleNode> baseRoleNodeList = baseRoleNodeMapper.queryAll(baseRoleNode);
            for (BaseRoleNode roleNodeResult : baseRoleNodeList) {
                selectNodeIdList.add(roleNodeResult.getNodeId());
            }
        }

        List<SysNodeVo> sysNodeVoList = new ArrayList<SysNodeVo>();
        List<SysNode> sysNodeList = sysNodeMapper.queryAllForList(sysCode, null, terminalType);//查出第一层
        for (SysNode sysNode : sysNodeList) {//遍历第一层
            SysNodeVo sysNodeVo = new SysNodeVo();
            BeanUtils.copyProperties(sysNode, sysNodeVo);
            if (selectNodeIdList.contains(sysNodeVo.getId())) {//在选中节点ID列表中能找到，则判定节点被选中
                if (sysNodeVo.getLastStage().intValue() == 1) {//最末级的节点添加选中状态，父节点由前端渲染
                    sysNodeVo.setSelectState(1);
                } else {
                    sysNodeVo.setSelectState(2);
                }
            } else {
                sysNodeVo.setSelectState(2);
            }
            if (sysNodeVo.getLastStage() == 2) {//不是末级，则继续往下查
                List<SysNodeVo> sysNodeVoList1 = getSonNodesForList(sysNodeVo, selectNodeIdList, terminalType);//该查询一直循环至末级
                sysNodeVo.setSonNodeVoList(sysNodeVoList1);
            }
            sysNodeVoList.add(sysNodeVo);
        }
        return sysNodeVoList;
    }

    public List<SysNodeVo> getSonNodesForList(SysNodeVo sysNodeVo, List<String> selectNodeIdList, Integer terminalType) {
        List<SysNodeVo> sysNodeVoList = null;
        List<SysNode> sysNodeList = sysNodeMapper.queryAllForList(sysNodeVo.getSysCode(), sysNodeVo.getNodeCode(), terminalType);
        if (sysNodeList != null && sysNodeList.size() > 0) {
            sysNodeVoList = new ArrayList<SysNodeVo>();
            for (SysNode sysNode : sysNodeList) {
                SysNodeVo sonsysNodeVo = new SysNodeVo();
                BeanUtils.copyProperties(sysNode, sonsysNodeVo);
                if (selectNodeIdList.contains(sonsysNodeVo.getId())) {//在选中节点ID列表中能找到，则判定节点被选中
                    if (sonsysNodeVo.getLastStage().intValue() == 1) {//最末级的节点添加选中状态，父节点由前端渲染
                        sonsysNodeVo.setSelectState(1);
                    } else {
                        sonsysNodeVo.setSelectState(2);
                    }
                } else {
                    sonsysNodeVo.setSelectState(2);
                }
                if (sonsysNodeVo.getLastStage() == 2) {//不是末级，则继续往下查
                    List<SysNodeVo> sysNodeVoList1 = getSonNodesForList(sonsysNodeVo, selectNodeIdList, terminalType);
                    sonsysNodeVo.setSonNodeVoList(sysNodeVoList1);
                }
                sysNodeVoList.add(sonsysNodeVo);
            }
        }
        return sysNodeVoList;

    }

    /**
     * 新增数据
     *
     * @param sysNode 实例对象
     * @return 实例对象
     */
    @Override
    public Result insert(SysNode sysNode, HttpServletRequest request) throws Exception {
        BaseUser baseUser = baseUserService.getUser(request);
        if (sysNode.getButton() != null) {
            SysDataDict sysDataDict = sysDataDictMapper.queryByCode(SysDataDict.NODE_BUTTON_TYPE_CODE, String.valueOf(sysNode.getButton()));
            if (sysDataDict != null) {
                sysNode.setButtonName(sysDataDict.getDictName());
            }
        }
        sysNode.setModifyTime(DateUtil.now());
        sysNode.setModifyUser(baseUser.getRealName());
        if (sysNode.getId() != null) {
            SysNode oldSysNode = sysNodeMapper.queryById(sysNode.getId());
            sysNodeMapper.updateForModify(sysNode);
            if (!oldSysNode.getNodeName().equals(sysNode.getNodeName())) {
                SysNode fiandSysNode = new SysNode();
                fiandSysNode.setParaNodeCode(oldSysNode.getNodeCode());
                fiandSysNode.setSysCode(sysNode.getSysCode());
                List<SysNode> sonSysNodeList = sysNodeMapper.queryAll(fiandSysNode);
                for (SysNode sonSysNode : sonSysNodeList) {
                    sonSysNode.setParaNodeName(sysNode.getNodeName());
                    sonSysNode.setSysCode(sysNode.getSysCode());
                    sonSysNode.setModifyTime(DateUtil.now());
                    sonSysNode.setModifyUser(baseUser.getRealName());
                    sysNodeMapper.update(sonSysNode);
                }
            }
            return Result.success(this.queryById(sysNode.getId()));
        } else {
            if (!StringUtils.isEmpty(sysNode.getParaNodeCode())) {//上级代码不为空则更新上级机构分类的末级状态
                SysNode fiandSysNode = new SysNode();
                fiandSysNode.setParaNodeCode(sysNode.getParaNodeCode());
                fiandSysNode.setSysCode(sysNode.getSysCode());
                List<SysNode> sysNodeList = sysNodeMapper.queryAll(fiandSysNode);
                int no = 1;
                int orderNo = 1;
                if (CollectionUtil.isNotEmpty(sysNodeList)) {
                    //如果该分类已有机构信息，则编码按原来机构编码加1---获取原编码，去除上级编码，获取序列码转int计算。
                    SysNode sysNode1 = sysNodeList.stream().findFirst().get();
                    if (sysNode1.getNodeCode().length() != sysNode.getParaNodeCode().length()) {
                        no = Integer.valueOf(sysNode1.getNodeCode().substring(sysNode.getParaNodeCode().length())) + 1;
                        orderNo = sysNode1.getOrderNo() != null ? sysNode1.getOrderNo().intValue() + 1 : 1;
                        String newNodeCode = sysNode.getParaNodeCode() + StringFormatUtil.stringCompl(String.valueOf(no), 2);
                        sysNode.setNodeCode(newNodeCode);
                    } else {
                        String newNodeCode = StringFormatUtil.stringCompl(String.valueOf(Integer.valueOf(sysNode1.getNodeCode()) + 1), 2);
                        sysNode.setNodeCode(newNodeCode);
                        orderNo = sysNode1.getOrderNo() != null ? sysNode1.getOrderNo().intValue() + 1 : 1;
                    }
                } else {
                    String newNodeCode = sysNode.getParaNodeCode() + StringFormatUtil.stringCompl(String.valueOf(no), 2);
                    sysNode.setNodeCode(newNodeCode);
                    sysNode.setOrderNo(orderNo);
                }
                SysNode paraSysNode = sysNodeMapper.queryByCode(sysNode.getParaNodeCode(), sysNode.getSysCode());
                if (paraSysNode != null) {
                    paraSysNode.setLastStage(2);//有下级分机构，则不为末级
                    paraSysNode.setModifyTime(DateUtil.now());
                    paraSysNode.setModifyUser(baseUser.getRealName());
                    sysNodeMapper.update(paraSysNode);
                    sysNode.setParaNodeName(paraSysNode.getNodeName());//保存上级分类名称
                    sysNode.setNodeLevel(paraSysNode.getNodeLevel() + 1);//节点级别在上级节点的级别上加一级
                }

            } else {//上级代码为空,则为顶层机构
                List<SysNode> sysNodeList = sysNodeMapper.queryTopAll(sysNode.getSysCode());
                int no = 1;
                int orderNo = 1;
                if (CollectionUtil.isNotEmpty(sysNodeList)) {
                    //如果已有机构信息，则编码按原来机构编码加1---获取原编码，获取序列码转int计算。
                    SysNode sysNode1 = sysNodeList.stream().findFirst().get();
                    no = Integer.valueOf(sysNode1.getNodeCode()) + 1;
                    orderNo = sysNode1.getOrderNo() != null ? sysNode1.getOrderNo().intValue() + 1 : 1;
                }
                String newNodeCode = StringFormatUtil.stringCompl(String.valueOf(no), 2);
                sysNode.setNodeCode(newNodeCode);
                sysNode.setOrderNo(orderNo);
                sysNode.setParaNodeName("");
                sysNode.setNodeLevel(1);
            }
            IdWorker idWorker = new IdWorker(0L, 0L);
            long newId = idWorker.nextId();
            sysNode.setId(String.valueOf(newId));
            sysNode.setCreateTime(DateUtil.now());
            sysNode.setCreateUser(baseUser.getRealName());
            this.sysNodeMapper.insert(sysNode);
            return Result.success(sysNode);
        }
    }

    /**
     * 初始化节点管理
     *
     * @return 实例对象
     */
    @Override
    public Result initNode(String sysCode) {
        int no = 1;
        int orderNo = 1;
        IdWorker idWorker = new IdWorker(0L, 0L);
        long newId = idWorker.nextId();
        SysNode firstSysNode = new SysNode();
        firstSysNode.setId(String.valueOf(newId));
        firstSysNode.setLastStage(1);//新建时为末级分类
        firstSysNode.setUseState(1);//新建时为可用状态1
        firstSysNode.setSysCode(sysCode);
        firstSysNode.setParaNodeCode("");
        firstSysNode.setParaNodeName("");
        String newNodeCode = firstSysNode.getParaNodeCode() + StringFormatUtil.stringCompl(String.valueOf(no), 2);
        firstSysNode.setNodeCode(newNodeCode);
        firstSysNode.setOrderNo(orderNo);
        firstSysNode.setNodeName("基础设置");
        firstSysNode.setNodeUrl("");
        firstSysNode.setNodeIcon("");
        firstSysNode.setNamePath("/basicSet");
        firstSysNode.setNodeType(1);
        firstSysNode.setNodeLevel(1);
        firstSysNode.setRemarks("初始化");
        firstSysNode.setButton(null);
        firstSysNode.setOrderNo(1);
        firstSysNode.setTerminalType(1);
        firstSysNode.setLinkType(1);
        this.sysNodeMapper.insert(firstSysNode);
        SysNode sonSysNode = new SysNode();
        long newId1 = idWorker.nextId();
        sonSysNode.setId(String.valueOf(newId1));
        sonSysNode.setLastStage(1);//新建时为末级分类
        sonSysNode.setUseState(1);//新建时为可用状态1
        sonSysNode.setSysCode(sysCode);
        sonSysNode.setParaNodeCode(firstSysNode.getNodeCode());
        sonSysNode.setParaNodeName(firstSysNode.getNodeName());
        newNodeCode = sonSysNode.getParaNodeCode() + StringFormatUtil.stringCompl(String.valueOf(no), 2);
        sonSysNode.setNodeCode(newNodeCode);
        sonSysNode.setOrderNo(orderNo);
        sonSysNode.setNodeName("节点管理");
        sonSysNode.setNodeUrl("views/basicSet/nodeManage");
        sonSysNode.setNodeIcon("");
        sonSysNode.setNamePath("nodeManage");
        sonSysNode.setNodeType(1);
        sonSysNode.setNodeLevel(firstSysNode.getNodeLevel().intValue() + 1);
        sonSysNode.setRemarks("初始化");
        sonSysNode.setButton(null);
        sonSysNode.setOrderNo(1);
        sonSysNode.setTerminalType(1);
        sonSysNode.setLinkType(1);
        this.sysNodeMapper.insert(sonSysNode);
        return Result.success("");
    }

    /**
     * 修改数据
     *
     * @param sysNode 实例对象
     * @return 实例对象
     */
    @Override
    public SysNode update(SysNode sysNode) {
        this.sysNodeMapper.update(sysNode);
        return this.queryById(sysNode.getId());
    }

    /**
     * 变更可用状态
     *
     * @return 实例对象
     */
    @Override
    public int changeUseState(String id, Integer useState, HttpServletRequest request) {
        BaseUser baseUser = baseUserService.getUser(request);
        SysNode sysNode = new SysNode();
        sysNode.setId(id);
        sysNode.setUseState(useState);
        int a = this.sysNodeMapper.update(sysNode);
        SysNode fiandSysNode = new SysNode();
        fiandSysNode.setParaNodeCode(sysNode.getParaNodeCode());
        fiandSysNode.setSysCode(sysNode.getSysCode());
        fiandSysNode.setUseState(1);
        List<SysNode> sysNodeList = sysNodeMapper.queryAll(fiandSysNode);
        if (sysNodeList == null && sysNodeList.size() == 0) {//上级没有下级了，变位末级
            SysNode paraSysNode = sysNodeMapper.queryByCode(sysNode.getParaNodeCode(), sysNode.getSysCode());
            if (paraSysNode != null) {
                paraSysNode.setLastStage(1);//没有有下级，为末级
                sysNodeMapper.update(paraSysNode);
            }
        }
        return a;
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String id) {
        return this.sysNodeMapper.deleteById(id) > 0;
    }

    @Override
    public List<SysNode> queryBySysCode(SysNodeVo vo) {
        LambdaQueryChainWrapper<SysNode> lambdaQuery = lambdaQuery();
        lambdaQuery.eq(SysNode::getUseState, ConstantsUtil.IS_DONT_DEL);
        if (!StringUtils.isEmpty(vo.getSysCode())) {
            lambdaQuery.eq(SysNode::getSysCode, vo.getSysCode());
        }
        List<SysNode> roomList = lambdaQuery.orderByAsc(SysNode::getNodeCode).list();
        roomList = roomList.stream().filter(t -> t.getNodeCode().length() < 5).collect(Collectors.toList());
        return roomList;
    }

    @Override
    public Result copySysNode(SysNodeVo vo) {
        Boolean a = false;
        String baseUserStr = myRedisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO + vo.getLoginID());
        JSONObject baseUserJson = JSONObject.parseObject(baseUserStr);
        BaseUser baseUser = JSONObject.toJavaObject(baseUserJson, BaseUser.class);
        if (StringUtils.isEmpty(vo.getPreviousMenuId())) {
            SysNode sysNode3 = sysNodeService.getById(vo.getCopyNodeId());
            SysNodeVo vo1 = new SysNodeVo();
            vo1.setSysCode(sysNode3.getSysCode());
            List<SysNode> list = queryBySysCode(vo1);
            SysNodeVo vo2 = new SysNodeVo();
            vo2.setSysCode(vo.getSysCode());
            List<SysNode> list1 = queryBySysCode(vo2);
            if (list != null && list.size() > 0) {
                IdWorker idWorker = new IdWorker(0L, 0L);
                List<SysNode> newList = new ArrayList<SysNode>();
                for (SysNode sysNode : list) {
                    SysNode newSysNode = new SysNode();
                    BeanUtils.copyProperties(sysNode, newSysNode);
                    if (newSysNode.getId().equals(vo.getCopyNodeId())) {
                        newSysNode.setNodeShowName(vo.getAliasNodeName());
                    }
                    long newId = idWorker.nextId();
                    newSysNode.setId(String.valueOf(newId));
                    int no = 1;
                    if (CollectionUtil.isNotEmpty(list)) {
                        if (list1.size() > 0) {
                            //如果已有机构信息，则编码按原来机构编码加1---获取原编码，获取序列码转int计算。
                            SysNode sysNode1 = list1.stream().max(Comparator.comparing(SysNode::getNodeCode)).get();
                            if (ObjectUtil.isEmpty(sysNode1)) {
                                no = 1;
                            }
                            no = Integer.valueOf(sysNode1.getNodeCode().substring(0, 2)) + 1;
                        }
                    }
                    String newNodeCode = StringFormatUtil.stringCompl(String.valueOf(no), 2);
                    String code = sysNode.getNodeCode();
                    if (code.length() == 2) {
                        newSysNode.setNodeCode(newNodeCode);
                        newSysNode.setLastStage(2);
                    }
                    String code1 = code.substring(2);
                    newSysNode.setNodeCode(newNodeCode + code1);
                    String code3 = newNodeCode + code1;
                    String substring = code3.substring(0, code3.length() - 2);
                    newSysNode.setParaNodeCode(substring);
                    if (newSysNode.getNodeCode().length() < 5) {
                        newSysNode.setLastStage(2);
                    }
                    newSysNode.setCreateTime(DateUtil.now());
                    newSysNode.setModifyTime(DateUtil.now());
                    newSysNode.setCreateUser(baseUser.getRealName());
                    newSysNode.setCreateUser(baseUser.getRealName());
                    newList.add(newSysNode);
                }
                for (SysNode node : newList) {
                    node.setSysCode(vo.getSysCode());
                    updateById(node);
                }
                a = saveBatch(newList);
                //新增节点查询该系统代码有权限的主体管理员角色，自动赋予权限
                List<BaseRole> baseRoleList = baseRoleService.queryListForSys(vo.getSysCode(), 3);
                List<BaseRoleNode> baseRoleNodeList = new ArrayList<BaseRoleNode>();
                if (CollectionUtil.isNotEmpty(baseRoleList)) {
                    for (BaseRole baseRole : baseRoleList) {
                        for (SysNode node : newList) {
                            BaseRoleNode baseRoleNode = new BaseRoleNode();
                            baseRoleNode.setId(String.valueOf(idWorker.nextId()));
                            baseRoleNode.setRoleId(baseRole.getId());
                            baseRoleNode.setNodeId(node.getId());
                            baseRoleNodeList.add(baseRoleNode);
                        }
                    }
                }
                if (baseRoleNodeList != null && baseRoleNodeList.size() > 0) {
                    baseRoleNodeMapper.batchInsert(baseRoleNodeList);
                }
            }
        } else {
            IdWorker idWorker = new IdWorker(0L, 0L);
            SysNode fiandSysNode = new SysNode();
            SysNode sysNode = sysNodeService.getById(vo.getCopyNodeId()); //复制节点
            SysNode sysNode5 = sysNodeService.getById(vo.getPreviousMenuId()); //上级节点
            if (ObjectUtil.isNotEmpty(sysNode5)) {
                sysNode5.setLastStage(2);
                sysNodeService.updateById(sysNode5);
            }
            sysNode.setParaNodeCode(sysNode5.getNodeCode());
            fiandSysNode.setSysCode(sysNode.getSysCode());
            fiandSysNode.setParaNodeCode(sysNode.getNodeCode());
            List<SysNode> sysNodeList = sysNodeMapper.queryAll(fiandSysNode);
            sysNode.setNodeShowName(vo.getAliasNodeName());
            SysNode vo2 = new SysNode();
            vo2.setSysCode(vo.getSysCode());
            vo2.setParaNodeCode(sysNode5.getNodeCode());
            List<SysNode> list = sysNodeMapper.queryAll(vo2);
            if (CollectionUtil.isEmpty(list)) {
                String newNodeCode = sysNode5.getNodeCode() + StringFormatUtil.stringCompl(1 + "", 2);
                sysNode.setNodeCode(newNodeCode);
            }
            if (CollectionUtil.isNotEmpty(list)) {
                SysNode sysNode3 = list.stream().max(Comparator.comparing(SysNode::getNodeCode)).orElse(null);
                if (ObjectUtil.isNotEmpty(sysNode3)) {
                    String nodeCode = sysNode3.getNodeCode();
                    Integer newNodeCode1 = Integer.valueOf(nodeCode);
                    Integer newNodeCode2 = newNodeCode1.intValue() + 1;
                    String s = newNodeCode2.toString();
//                    String newNodeCode = sysNode5.getNodeCode() + StringFormatUtil.stringCompl(s.substring(nodeCode.length() - 2), 2);
                    String newNodeCode = sysNode5.getNodeCode() + s.substring(s.length() - 2);
                    sysNode.setNodeCode(newNodeCode);
                }
            }
            String code1 = sysNode.getNodeCode();
            sysNode.setId(String.valueOf(idWorker.nextId()));
            sysNode.setParaNodeName(sysNode5.getNodeName());
            if (sysNodeList != null && sysNodeList.size() > 0) {
                List<SysNode> newList = new ArrayList<SysNode>();
                newList.add(sysNode);
                for (SysNode sysNode1 : sysNodeList) {
                    SysNode newSysNode = new SysNode();
                    BeanUtils.copyProperties(sysNode1, newSysNode);
                    long newId = idWorker.nextId();
                    newSysNode.setId(String.valueOf(newId));
                    String code5 = sysNode1.getNodeCode();
                    if (code5.length() == 6) {
                        String code6 = code5.substring(4);
                        newSysNode.setNodeCode(code1 + code6);
                    }
                    if (code5.length() == 8) {
                        String code6 = code5.substring(6);
                        newSysNode.setNodeCode(code1 + code6);
                        newSysNode.setParaNodeCode(newSysNode.getNodeCode().substring(0, 4));
                    }
                    if (newSysNode.getNodeCode().length() < 5) {
                        newSysNode.setLastStage(2);
                    }
                    newSysNode.setParaNodeCode(newSysNode.getNodeCode().substring(0, newSysNode.getNodeCode().length() - 2));
                    newSysNode.setCreateTime(DateUtil.now());
                    newSysNode.setModifyTime(DateUtil.now());
                    newSysNode.setCreateUser(baseUser.getRealName());
                    newSysNode.setCreateUser(baseUser.getRealName());
                    newList.add(newSysNode);
                }
                for (SysNode node : newList) {
                    node.setSysCode(vo.getSysCode());
                    updateById(node);
                }
                a = saveBatch(newList);
                //新增节点查询该系统代码有权限的主体管理员角色，自动赋予权限
                List<BaseRole> baseRoleList = baseRoleService.queryListForSys(vo.getSysCode(), 3);
                List<BaseRoleNode> baseRoleNodeList = new ArrayList<BaseRoleNode>();
                if (CollectionUtil.isNotEmpty(baseRoleList)) {
                    for (BaseRole baseRole : baseRoleList) {
                        for (SysNode node : newList) {
                            BaseRoleNode baseRoleNode = new BaseRoleNode();
                            baseRoleNode.setId(String.valueOf(idWorker.nextId()));
                            baseRoleNode.setRoleId(baseRole.getId());
                            baseRoleNode.setNodeId(node.getId());
                            baseRoleNodeList.add(baseRoleNode);
                        }
                    }
                }
                if (baseRoleNodeList != null && baseRoleNodeList.size() > 0) {
                    baseRoleNodeMapper.batchInsert(baseRoleNodeList);
                }
            }
        }
        return Result.success(a);
    }


    public List<SysNodeSelectModel> getSonNodes(SysNodeSelectModel sysNodeVo, Map<String, List<SysNode>> nodeListMap, List<Long> selectNodeIdList) {
        List<SysNodeSelectModel> list = null;
        List<SysNode> resList = nodeListMap.get(sysNodeVo.getNodeCode());
        if (CollectionUtil.isNotEmpty(resList)) {
            list = new ArrayList<>();
            for (SysNode sysNode : resList) {
                SysNodeSelectModel sonVO = new SysNodeSelectModel();
                BeanUtils.copyProperties(sysNode, sonVO);
                if (selectNodeIdList.contains(Long.valueOf(sonVO.getId()))) {//在选中节点ID列表中能找到，则判定节点被选中
                    if (sonVO.getLastStage().intValue() == 1) {//最末级的节点添加选中状态，父节点由前端渲染
                        sonVO.setSelectState(1);
                    } else {
                        sonVO.setSelectState(2);
                    }
                } else {
                    sonVO.setSelectState(2);
                }
                sonVO.setSonNodeVoList(getSonNodes(sonVO, nodeListMap, selectNodeIdList));
                list.add(sonVO);//获取第一层
            }
        }
        return list;
    }
}