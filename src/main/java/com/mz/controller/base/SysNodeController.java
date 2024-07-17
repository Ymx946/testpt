package com.mz.controller.base;

import com.mz.common.util.ResponseCode;
import com.mz.common.util.Result;
import com.mz.common.util.StringFormatUtil;
import com.mz.framework.annotation.NeedLogin;
import com.mz.model.base.BaseUser;
import com.mz.model.base.SysNode;
import com.mz.model.base.vo.SysNodeVo;
import com.mz.service.base.BaseUserService;
import com.mz.service.base.SysNodeService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 功能节点表(SysNode)表控制层
 *
 * @author makejava
 * @since 2021-03-17 11:00:22
 */
@RestController
@Slf4j
@RequestMapping(value = {"datacenter/sysNode", "sysNode"})
public class SysNodeController {
    /**
     * 用户服务对象
     */
    @Resource
    private BaseUserService baseUserService;
    /**
     * 服务对象
     */
    @Resource
    private SysNodeService sysNodeService;


    /**
     * 根据主键查询
     */
    @SneakyThrows
    @PostMapping("queryById")
    public Result queryById(String id, String token, HttpServletRequest request) {
        if (baseUserService.checkLogin(token, request)) {
            if (StringUtils.isEmpty(id)) {
                return Result.failed("ID必填");
            }
            return Result.success(sysNodeService.queryById(id));
        } else {
            return Result.failedLogin();
        }
    }


    /**
     * 新增数据
     * --修改时不可修改上级机构
     */
    @SneakyThrows
    @PostMapping("insert")
    public Result insert(String id, String sysCode, String nodeName, String nodeShowName, String paraNodeCode, String nodeUrl, String namePath, String nodeIcon, Integer nodeType, Integer button,
                         String remarks, Integer orderNo, Integer terminalType, Integer linkType, String operatingBooklet, String token, HttpServletRequest request) {
        if (baseUserService.checkLogin(token, request)) {
            if (StringUtils.isEmpty(nodeName)) {
                return Result.failed("节点名称必填");
            }
            if (StringUtils.isEmpty(sysCode)) {
                return Result.failed("系统代码必填");
            }
            terminalType = terminalType != null ? terminalType : 1;//默认PC端
            linkType = linkType != null ? linkType : 1;//默认自建路由
            SysNode sysNode = new SysNode();
            if (!StringUtils.isEmpty(id)) {
                sysNode.setId(id);
            } else {
                sysNode.setLastStage(1);//新建时为末级分类
                sysNode.setUseState(1);//新建时为可用状态1
                sysNode.setParaNodeCode(paraNodeCode);
                sysNode.setId(null);
            }
            sysNode.setSysCode(sysCode);
            sysNode.setNodeName(nodeName);
            if (StringUtils.isEmpty(nodeShowName)) {
                sysNode.setNodeShowName(nodeName);
            } else {
                sysNode.setNodeShowName(nodeShowName);
            }
            sysNode.setNodeName(nodeName);
            sysNode.setNodeUrl(nodeUrl);
            sysNode.setNodeIcon(nodeIcon);
            sysNode.setNamePath(namePath);
            sysNode.setNodeType(nodeType);
            sysNode.setRemarks(remarks);
            sysNode.setButton(button);
            sysNode.setOrderNo(orderNo);
            sysNode.setTerminalType(terminalType);
            sysNode.setLinkType(linkType);
            sysNode.setOperatingBooklet(operatingBooklet);
            return sysNodeService.insert(sysNode, request);
        } else {
            return Result.failedLogin();
        }
    }

    /**
     * 根据用户查询该用户拥有权限的节点
     */
    @SneakyThrows
    @PostMapping("queryAllByUser")
    public Result queryAllByUser(String mainBodyId, String sysCode, String paraNodeCode, Integer nodeType, Integer terminalType, String token, HttpServletRequest request) {
        if (StringUtils.isEmpty(mainBodyId)) {
            mainBodyId = null;
        }
        if (StringUtils.isEmpty(sysCode)) {
            return Result.failed("系统代码必填");
        }
        terminalType = terminalType != null ? terminalType : 1;//默认PC端
        if (baseUserService.checkLogin(token, request)) {
            BaseUser baseUser = baseUserService.getUser(request);
            String userId = baseUser.getId();
            if (baseUser.getUserLevel() > 3) {
                if (baseUser.getUserType() != null && baseUser.getUserType().intValue() == BaseUser.USER_TYPE_LOGIN_FREE) {//----20220420  免登录演示用户没有权限概念，所有都能看见
                    return Result.success(sysNodeService.queryAllForList(null, sysCode, terminalType));
                } else {
                    return Result.success(sysNodeService.queryAllByUser(userId, mainBodyId, sysCode, paraNodeCode, nodeType, terminalType));
                }
            } else {//超级管理员/租户管理员/主体管理员不需要节点权限，所有节点都能看见
                return Result.success(sysNodeService.queryAllForList(null, sysCode, terminalType));
            }
        } else {
            return Result.failedLogin();
        }
    }

    /**
     * 根据用户查询该用户拥有权限的节点(黄冈品牌主体管理员可以看到所有节点)
     */
    @SneakyThrows
    @PostMapping("queryAllByUserNY")
    public Result queryAllByUserNY(String mainBodyId, String sysCode, String paraNodeCode, Integer nodeType, Integer terminalType, String token, HttpServletRequest request) {
        if (StringUtils.isEmpty(mainBodyId)) {
            mainBodyId = null;
        }
        if (StringUtils.isEmpty(sysCode)) {
            return Result.failed("系统代码必填");
        }
        terminalType = terminalType != null ? terminalType : 1;//默认PC端
        if (baseUserService.checkLogin(token, request)) {
            BaseUser baseUser = baseUserService.getUser(request);
            String userId = baseUser.getId();
            if (baseUser.getUserLevel() != 3) {//除主体管理员以外用户都需要节点权限加载节点。只有主体管理员可以看见所有节点
                return Result.success(sysNodeService.queryAllByUser(userId, mainBodyId, sysCode, paraNodeCode, nodeType, terminalType));
            } else {
                return Result.success(sysNodeService.queryAllForList(null, sysCode, terminalType));
            }
        } else {
            return Result.failedLogin();
        }
    }

    /**
     * 根据用户查询该用户拥有权限的节点
     */
    @SneakyThrows
    @PostMapping("querySysPowerUser")
    public Result querySysPowerUser(String mainBodyId, String sysCode, Integer terminalType, String token, HttpServletRequest request) {
        if (StringUtils.isEmpty(mainBodyId)) {
            mainBodyId = null;
        }
        if (StringUtils.isEmpty(sysCode)) {
            return Result.failed("系统代码必填");
        }
        terminalType = terminalType != null ? terminalType : 1;//默认PC端
        if (baseUserService.checkLogin(token, request)) {
            BaseUser baseUser = baseUserService.getUser(request);
            String userId = baseUser.getId();
            if (baseUser.getUserLevel() > 2) {//超级管理员/租户管理员不需要节点权限，所有节点都能看见
                return Result.success(sysNodeService.querySysPowerUser(userId, mainBodyId, sysCode, terminalType));
            } else {
                return Result.success(1);
            }
        } else {
            return Result.failedLogin();
        }
    }

    /**
     * 根据用户查询该用户拥有权限的节点
     */
    @SneakyThrows
    @PostMapping("queryAllByUserApp")
    public Result queryAllByUserApp(String userId, String mainBodyId, String sysCode, String paraNodeCode, Integer nodeType, Integer terminalType, String token, HttpServletRequest request) {
        if (StringUtils.isEmpty(userId)) {
            return Result.failed("用户ID必填");
        }
        if (StringUtils.isEmpty(mainBodyId)) {
            return Result.failed("主体ID必填");
        }
        if (StringUtils.isEmpty(sysCode)) {
            return Result.failed("系统代码必填");
        }
        terminalType = terminalType != null ? terminalType : 1;//默认PC端
        if (baseUserService.checkLogin(token, request)) {
            return Result.success(sysNodeService.queryAllByUserApp(userId, mainBodyId, sysCode, paraNodeCode, nodeType, terminalType));
        } else {
            return Result.failedLogin();
        }
    }

    /**
     * 查询所有节点（分层级）
     * --传入角色ID，将该角色以有权限的节点标记为已选中状态
     */
    @SneakyThrows
    @PostMapping("queryAllForList")
    public Result queryAllForList(String roleId, String sysCode, Integer terminalType, String token, HttpServletRequest request) {
        if (baseUserService.checkLogin(token, request)) {
            if (StringUtils.isEmpty(roleId)) {
                roleId = null;
            }
            if (StringUtils.isEmpty(sysCode)) {
                return Result.failed("系统代码必填");
            }
            terminalType = terminalType != null ? terminalType : 1;//默认PC端
            return Result.success(sysNodeService.queryAllForList(roleId, sysCode, terminalType));
        } else {
            return Result.failedLogin();
        }
    }


    /**
     * 查询多条数据page
     *
     * @param pageNo   查询起始位置
     * @param pageSize 查询条数
     * @return 对象列表
     */
    @SneakyThrows
    @PostMapping("queryAllByLimit")
    public Result queryAllByLimit(Integer pageNo, Integer pageSize, String sysCode, Integer applicationType, String nodeName, String nodeShowName, String paraNodeCode, Integer nodeType, Integer button, Integer useState, String token, HttpServletRequest request) {
        if (baseUserService.checkLogin(token, request)) {
            pageNo = pageNo != null ? pageNo : StringFormatUtil.PAGE_NO_DEFAULT;
            pageSize = pageSize != null ? pageSize : StringFormatUtil.PAGE_SIZE_DEFAULT;
            return Result.success(sysNodeService.queryAllByLimit(pageNo, pageSize, sysCode, applicationType, nodeName, nodeShowName, paraNodeCode, nodeType, button, useState, request));
        } else {
            return Result.failedLogin();
        }
    }

    /**
     * 变更可用状态
     *
     * @return 对象列表
     */
    @SneakyThrows
    @PostMapping("changeUseState")
    public Result changeUseState(String id, Integer useState, String token, HttpServletRequest request) {
        if (baseUserService.checkLogin(token, request)) {
            if (StringUtils.isEmpty(id)) {
                return Result.failed("ID必填");
            }
            if (useState == null) {
                return Result.failed("可用状态必填");
            }
            return Result.success(sysNodeService.changeUseState(id, useState, request));
        } else {
            return Result.failedLogin();
        }
    }

    /**
     * 筛选目标应用下面的菜单
     */
    @NeedLogin
    @PostMapping("queryBySysCode")
    public Result queryBySysCode(SysNodeVo vo) {
        if (StringUtils.isEmpty(vo.getSysCode())) {
            return Result.failed("系统代码不能为空");
        }
        try {
            return Result.success(sysNodeService.queryBySysCode(vo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }

    /**
     * 复制节点信息
     */
    @NeedLogin
    @PostMapping("copySysNode")
    public Result copySysNode(SysNodeVo vo, @RequestHeader(value = "loginID") String loginID) {
        if (StringUtils.isEmpty(vo.getAliasNodeName())) {
            return Result.failed("别名节点名称不能为空");
        }
        try {
            vo.setLoginID(loginID);
            return sysNodeService.copySysNode(vo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }

}