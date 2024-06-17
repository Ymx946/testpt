package com.mz.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mz.common.context.PageInfo;
import com.mz.common.util.Result;
import com.mz.model.base.SysNode;
import com.mz.model.base.model.SysNodeSelectModel;
import com.mz.model.base.vo.SysNodeVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 功能节点表(SysNode)表服务接口
 *
 * @author makejava
 * @since 2021-03-17 11:00:22
 */
public interface SysNodeService extends IService<SysNode> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysNode queryById(String id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    PageInfo<SysNode> queryAllByLimit(int offset, int limit, String sysCode,Integer applicationType,String nodeName, String nodeShowName,String paraNodeCode, Integer nodeType,Integer button, Integer useState, HttpServletRequest request);

    /**
     * 查询应用节点
     *
     * @param sysCode 系统代码
     * @return 对象列表
     */
     List<SysNode> queryAllBySys( String sysCode);
    /**
     * 查询权限节点
     *
     * @param userId 用户ID
     * @param sysCode 系统代码
     * @return 对象列表
     */
     List<SysNodeVo> queryAllByUser(String userId,String mainBodyId, String sysCode, String paraNodeCode,Integer nodeType,Integer terminalType);

    /**
     * 查询用户是否有权限
     *
     * @param userId 用户ID
     * @param sysCode 系统代码
     * @return 对象列表
     */
    int querySysPowerUser(String userId,String mainBodyId,String sysCode,Integer terminalType);
    /**
     * 查询权限节点
     *
     * @param userId 用户ID
     * @param sysCode 系统代码
     * @return 对象列表
     */
     List<SysNode> queryAllByUserApp(String userId,String mainBodyId, String sysCode, String paraNodeCode,Integer nodeType,Integer terminalType);

     List<SysNodeVo> queryAllForList(String roleId,String sysCode,Integer terminalType);
    /**
     * 新增数据
     *
     * @param sysNode 实例对象
     * @return 实例对象
     */
    Result insert(SysNode sysNode,  HttpServletRequest request) throws Exception;
    /**
     * 初始化节点管理页面
     *
     * @return 实例对象
     */
    Result initNode(String sysCode) ;

    /**
     * 修改数据
     *
     * @param sysNode 实例对象
     * @return 实例对象
     */
    SysNode update(SysNode sysNode);

    /**
     * 变更可用状态
     *
     * @return 实例对象
     */
    int changeUseState(String id,Integer useState, HttpServletRequest request);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(String id);
    /**
     * 根据系统code查询列表
     */
    List<SysNode> queryBySysCode(SysNodeVo vo);
    /**
     * 复制节点信息
     */
     Result copySysNode(SysNodeVo vo);
    PageInfo<SysNode> queryAllOrderByTime(SysNodeVo vo);

}