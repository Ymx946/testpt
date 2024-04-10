package com.mz.mapper.localhost;

import com.mz.model.base.BaseRole;
import com.mz.model.base.vo.BaseRoleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色表(BaseRole)表数据库访问层
 *
 * @author makejava
 * @since 2021-03-17 09:08:17
 */
public interface BaseRoleMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    BaseRole queryById(String id);

    /**
     * 根据主体类型和系统角色ID查询对应角色（有角色的更新关联，没有的新增觉得）
     *
     * @return 实例对象
     */
    List<BaseRole> querySysRoleByMainType(BaseRoleVO vo);

    /**
     * 通过ID查询单条数据
     *
     * @return 实例对象
     */
    BaseRole queryByLevel(@Param("tenantId") String tenantId, @Param("mainBodyId") String mainBodyId, @Param("rolerLevel") Integer rolerLevel);

    /**
     * 查询应用没有权限的角色
     *
     * @return 实例对象
     */
    List<BaseRole> queryNoListForSys(@Param("tenantId") String tenantId, @Param("mainBodyId") String mainBodyId, @Param("sysCode") String sysCode);

    /**
     * 查询应用没有权限的角色
     *
     * @return 实例对象
     */
    List<BaseRole> queryListForSys(@Param("sysCode") String sysCode, @Param("roleLevel") Integer roleLevel);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<BaseRole> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit, @Param("deptId") String deptId, @Param("mainBodyId") String mainBodyId, @Param("tenantId") String tenantId, @Param("roleName") String roleName);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param baseRole 实例对象
     * @return 对象列表
     */
    List<BaseRole> queryAll(BaseRole baseRole);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param deptId     部门ID
     * @param mainBodyId 主体ID
     * @param userId     用户ID
     * @return 对象列表
     */
    List<BaseRoleVO> queryAllForUser(@Param("deptId") String deptId, @Param("mainBodyId") String mainBodyId, @Param("tenantId") String tenantId, @Param("userId") String userId);

    /**
     * 新增数据
     *
     * @param baseRole 实例对象
     * @return 影响行数
     */
    int insert(BaseRole baseRole);

    /**
     * 批量新增数据
     *
     * @return 影响行数
     */
    int batchInsert(List<BaseRole> baseRoleList);

    /**
     * 修改数据
     *
     * @param baseRole 实例对象
     * @return 影响行数
     */
    int update(BaseRole baseRole);

    /**
     * 变更可用状态
     *
     * @return 影响行数
     */
    int changeUseState(@Param("id") String id, @Param("useState") Integer useState);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

}