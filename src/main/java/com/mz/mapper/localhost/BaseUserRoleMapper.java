package com.mz.mapper.localhost;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mz.model.base.BaseUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色表(BaseUserRole)表数据库访问层
 *
 * @author makejava
 * @since 2021-03-17 09:08:18
 */
public interface BaseUserRoleMapper extends BaseMapper<BaseUserRole> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    BaseUserRole queryById(String id);

    /**
     * 通过用户ID和主体ID查询单条数据（验证用户所属角色是否有主体越权）
     * --用户可能有多个主体的不同角色
     * --用户同一主体也可能有多个角色--查询结果只返回第一个
     *
     * @param userId     主键
     * @param mainBodyId 主键
     * @return 实例对象
     */
    BaseUserRole queryByMainAndUser(@Param("userId") String userId, @Param("mainBodyId") String mainBodyId);

    /**
     * 通过角色ID和用户ID查询单条数据（验证用户和角色不重复）
     *
     * @param roleId 主键
     * @param userId 主键
     * @return 实例对象
     */
    BaseUserRole queryByOne(@Param("dutyId") String dutyId, @Param("roleId") String roleId, @Param("userId") String userId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<BaseUserRole> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param baseUserRole 实例对象
     * @return 对象列表
     */
    List<BaseUserRole> queryAll(BaseUserRole baseUserRole);

    /**
     * 根据用户主体查询角色关联信息
     *
     * @param mainBodyId 主体ID
     * @return 对象列表
     */
    List<BaseUserRole> queryUserRole(@Param("mainBodyId") String mainBodyId, @Param("userId") String userId);

    /**
     * 新增数据
     *
     * @param baseUserRole 实例对象
     * @return 影响行数
     */
    int insert(BaseUserRole baseUserRole);

    /**
     * 新增数据
     *
     * @param powers 数据集
     * @return 影响行数
     */
    int batchInsert(List<BaseUserRole> powers);

    /**
     * 修改数据
     *
     * @param baseUserRole 实例对象
     * @return 影响行数
     */
    int update(BaseUserRole baseUserRole);

    /**
     * 根据主体ID变更角色的当前角色状态
     *
     * @param mainBodyId 实例对象
     * @return 影响行数
     */
    int updateByMainBody(@Param("currentRole") Integer currentRole, @Param("userId") String userId, @Param("mainBodyId") String mainBodyId);

    /**
     * 根据职务ID修改角色ID
     *
     * @return 影响行数
     */
    int updateByDuty(@Param("roleId") String roleId, @Param("dutyId") String dutyId);

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
    int deleteByRoleId(String roleId);

    /**
     * 通过用户ID删除数据
     *
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteByUserId(@Param("userId") String userId, @Param("mainBodyId") String mainBodyId, @Param("deptId") String deptId);

    /**
     * 通过用户ID和职务ID删除数据
     *
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteByUserIdWithDuty(@Param("userId") String userId, @Param("mainBodyId") String mainBodyId, @Param("deptId") String deptId);

}