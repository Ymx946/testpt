package com.mz.mapper.localhost;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mz.model.base.SysNode;
import com.mz.model.base.vo.SysNodeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 功能节点表(SysNode)表数据库访问层
 *
 * @author makejava
 * @since 2021-03-17 11:00:22
 */
public interface SysNodeMapper extends BaseMapper<SysNode> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysNode queryById(String id);

    /**
     * 通过ID查询单条数据
     *
     * @param nodeCode 节点编号
     * @return 实例对象
     */
    SysNode queryByCode(@Param("nodeCode") String nodeCode,
                        @Param("sysCode") String sysCode);


    /**
     * 查询应用节点
     *
     * @return 对象列表
     */
    List<SysNode> queryAllBySys(@Param("sysCode") String sysCode);


    /**
     * 查询权限节点
     *
     * @param userId       用户ID
     * @param paraNodeCode 上级节点代码
     * @return 对象列表
     */
    List<SysNode> queryAllByUser(@Param("userId") String userId,
                                 @Param("mainBodyId") String mainBodyId,
                                 @Param("sysCode") String sysCode,
                                 @Param("paraNodeCode") String paraNodeCode,
                                 @Param("terminalType") Integer terminalType);

    /**
     * 用户是否又权限
     *
     * @param userId 用户ID
     * @return 对象列表
     */
    int querySysPowerUser(@Param("userId") String userId,
                          @Param("mainBodyId") String mainBodyId,
                          @Param("sysCode") String sysCode,
                          @Param("terminalType") Integer terminalType);

    /**
     * 查询权限节点
     *
     * @param userId       用户ID
     * @param paraNodeCode 上级节点代码
     * @return 对象列表
     */
    List<SysNode> queryAllByUserApp(@Param("userId") String userId,
                                    @Param("mainBodyId") String mainBodyId,
                                    @Param("sysCode") String sysCode,
                                    @Param("paraNodeCode") String paraNodeCode,
                                    @Param("terminalType") Integer terminalType);


    /**
     * 查询权限节点
     *
     * @param userId       用户ID
     * @param paraNodeCode 上级节点代码
     * @return 对象列表
     */
    List<SysNode> queryNodeByUserAndType(@Param("userId") String userId,
                                         @Param("mainBodyId") String mainBodyId,
                                         @Param("sysCode") String sysCode,
                                         @Param("paraNodeCode") String paraNodeCode,
                                         @Param("nodeType") Integer nodeType,
                                         @Param("terminalType") Integer terminalType);

    /**
     * 查询所有节点（分层列表）
     *
     * @param paraNodeCode 上级节点代码
     * @return 对象列表
     */
    List<SysNode> queryAllForList(@Param("sysCode") String sysCode,
                                  @Param("paraNodeCode") String paraNodeCode,
                                  @Param("terminalType") Integer terminalType);

    /**
     * 查询所有节点（分层列表）
     *
     * @return 对象列表
     */
    List<SysNode> queryAllClassifyNode(@Param("sysCode") String sysCode,
                                       @Param("terminalType") Integer terminalType);

    /**
     * 查询顶层节点
     *
     * @return 对象列表
     */
    List<SysNode> queryTopAll(@Param("sysCode") String sysCode);

    /**
     * 查询指定行数据
     *
     * @return 对象列表
     */
    List<SysNode> queryAllByLimit(@Param("sysCode") String sysCode,
                                  @Param("nodeName") String nodeName,
                                  @Param("paraNodeCode") String paraNodeCode,
                                  @Param("nodeType") Integer nodeType,
                                  @Param("button") Integer button,
                                  @Param("useState") Integer useState,
                                  @Param("sysCodeArr") String[] sysCodeArr);

    List<SysNode> queryAllOrderByTime(SysNodeVo vo);
    /**
     * 通过实体作为筛选条件查询
     *
     * @param sysNode 实例对象
     * @return 对象列表
     */
    List<SysNode> queryAll(SysNode sysNode);

    /**
     * 新增数据
     *
     * @param sysNode 实例对象
     * @return 影响行数
     */
    int insert(SysNode sysNode);

    /**
     * 修改数据
     *
     * @param sysNode 实例对象
     * @return 影响行数
     */
    int update(SysNode sysNode);

    /**
     * 修改数据
     *
     * @param sysNode 实例对象
     * @return 影响行数
     */
    int updateForModify(SysNode sysNode);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

    /**
     * 根据系统代码查询权限节点
     *
     * @param userId 用户ID
     * @return 对象列表
     */
    List<SysNode> queryAllNodeForRole(@Param("userId") String userId,
                                      @Param("mainBodyId") String mainBodyId,
                                      @Param("classifyId") Long classifyId,
                                      @Param("terminalType") Integer terminalType,@Param("classifyType") Integer classifyType);

    /**
     * 根据系统代码查询节点
     *
     * @param userId 用户ID
     * @return 对象列表
     */
    List<SysNode> queryAllNodeBySysCode(@Param("userId") String userId,
                                        @Param("mainBodyId") String mainBodyId,
                                        @Param("classifyId") Long classifyId,
                                        @Param("terminalType") Integer terminalType,@Param("classifyType") Integer classifyType);
}