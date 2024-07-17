package com.mz.mapper.localhost;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mz.model.base.BaseRoleNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色节点表(BaseRoleNode)表数据库访问层
 *
 * @author makejava
 * @since 2021-03-17 09:08:17
 */
public interface BaseRoleNodeMapper extends BaseMapper<BaseRoleNode> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    BaseRoleNode queryById(String id);

    /**
     * 查询指定行数据
     *
     * @return 对象列表
     */
    List<BaseRoleNode> queryBySysCode(@Param("roleId") String roleId, @Param("sysCode") String sysCode);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<BaseRoleNode> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param baseRoleNode 实例对象
     * @return 对象列表
     */
    List<BaseRoleNode> queryAll(BaseRoleNode baseRoleNode);

    /**
     * 查询角色已有节点权限的应用ID
     *
     * @return 对象列表
     */
    List<String> querySysIdAll(Long roleId, Long mainBodyId, Long groupId);

    /**
     * 查询角色已有节点权限的应用ID
     *
     * @return 对象列表
     */
    List<String> querySysIdAllByUser(Long userId, Long mainBodyId, Long groupId);

    /**
     * 新增数据
     *
     * @param baseRoleNode 实例对象
     * @return 影响行数
     */
    int insert(BaseRoleNode baseRoleNode);


    /**
     * 新增数据
     *
     * @param powers 数据集
     * @return 影响行数
     */
    int batchInsert(List<BaseRoleNode> powers);

    /**
     * 修改数据
     *
     * @param baseRoleNode 实例对象
     * @return 影响行数
     */
    int update(BaseRoleNode baseRoleNode);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

    /**
     * 通过角色ID删除数据
     *
     * @param roleId 角色ID
     * @return 影响行数
     */
    int deleteByRoleId(@Param("roleId") String roleId, @Param("sysCode") String sysCode);

}