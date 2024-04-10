package com.mz.service.base;

import com.mz.common.util.Result;
import com.mz.model.base.BaseRole;
import com.mz.model.base.vo.BaseRoleVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 角色表(BaseRole)表服务接口
 *
 * @author makejava
 * @since 2021-03-17 09:08:17
 */
public interface BaseRoleService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    BaseRole queryById(String id, HttpServletRequest request);
    /**
     * 主体和级别查询(单个)
     *
     * @return 实例对象
     */
    BaseRole queryByLevel(String tenantId,String mainBodyId,Integer rolerLevel);
    /**
     * 查询应用没有权限的角色
     *
     * @return 实例对象
     */
    List<BaseRole> queryNoListForSys(String tenantId, String mainBodyId, String sysCode);
    /**
     * 查询应用有权限的角色
     *
     * @return 实例对象
     */
    List<BaseRole> queryListForSys(String sysCode,Integer roleLevel);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    Result queryAllByLimit(int offset, int limit,String deptId, String mainBodyId,String roleName, HttpServletRequest request);

    /**
     * 查询多条数据
     *
     * @param deptId 查询起始位置
     * @param mainBodyId 查询条数
     * @return 对象列表
     */
     Result queryAll(String deptId, String mainBodyId, HttpServletRequest request);
    /**
     * 查询多条数据
     *
     * @return 对象列表
     */
    List<BaseRole>  queryAll(BaseRole baseRole );
    /**
     * 根据主体类型和系统角色ID查询对应角色（有角色的更新关联，没有的新增觉得）
     *
     * @return 实例对象
     */
    List<BaseRole> querySysRoleByMainType(BaseRoleVO vo);
    /**
     * 根据部门ID和主体ID查询角色列表
     *--根据传入用户ID，该用户已有的角色标记为已选中状态
     * @param deptId 查询起始位置
     * @param mainBodyId 查询条数
     * @param userId 用户
     * @return 对象列表
     */
     Result queryAllForUser(String deptId, String mainBodyId, String userId,  HttpServletRequest request);
    /**
     * 新增数据
     *
     * @param baseRole 实例对象
     * @return 实例对象
     */
    Result insert(BaseRole baseRole, HttpServletRequest request);
    /**
     * 批量新增数据
     *
     * @return 影响行数
     */
    int batchInsert(List<BaseRole> baseRoleList);
    /**
     * 变更可用状态
     *
     * @return 实例对象
     */
    int changeUseState(String id,Integer useState, HttpServletRequest request);
    /**
     * 修改数据
     *
     * @param baseRole 实例对象
     * @return 实例对象
     */
    BaseRole update(BaseRole baseRole);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(String id);

}