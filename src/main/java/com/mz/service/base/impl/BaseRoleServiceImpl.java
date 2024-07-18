package com.mz.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.mz.common.context.PageInfo;
import com.mz.common.util.IdWorker;
import com.mz.common.util.Result;
import com.mz.mapper.localhost.BaseRoleMapper;
import com.mz.mapper.localhost.BaseUserRoleMapper;
import com.mz.model.base.BaseRole;
import com.mz.model.base.BaseUser;
import com.mz.model.base.BaseUserRole;
import com.mz.model.base.vo.BaseRoleVO;
import com.mz.service.base.BaseRoleService;
import com.mz.service.base.BaseUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 角色表(BaseRole)表服务实现类
 *
 * @author makejava
 * @since 2021-03-17 09:08:17
 */
@Service("baseRoleService")
public class BaseRoleServiceImpl extends ServiceImpl<BaseRoleMapper, BaseRole> implements BaseRoleService {
    @Resource
    private BaseRoleMapper baseRoleMapper;
    @Resource
    private BaseUserRoleMapper baseUserRoleMapper;
    /**
     * 用户服务对象
     */
    @Resource
    private BaseUserService baseUserService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BaseRole queryById(String id, HttpServletRequest request) {
        BaseRole baseRole = baseRoleMapper.queryById(id);
        BaseUser baseUser = baseUserService.getUser(request);
        int flag = 1;//是否有权限
        if (baseUser.getUserLevel() >= 3) {//用户为主体管理员时需要验证主体越权情况
            BaseUserRole baseUserRole = baseUserRoleMapper.queryByMainAndUser(baseUser.getId(), baseRole.getMainBodyId());
            if (baseUserRole == null) {
                flag = 2;
            }
        }
        if (flag == 1) {//无越权情况
            return baseRole;
        } else {
            return null;
        }
    }

    /**
     * 主体和级别查询
     *
     * @return 实例对象
     */
    @Override
    public BaseRole queryByLevel(String tenantId, String mainBodyId, Integer rolerLevel) {
        BaseRole baseRole = baseRoleMapper.queryByLevel(tenantId, mainBodyId, rolerLevel);
        return baseRole;
    }

    /**
     * 查询应用没有权限的角色
     *
     * @return 实例对象
     */
    @Override
    public List<BaseRole> queryNoListForSys(String tenantId, String mainBodyId, String sysCode) {
        List<BaseRole> list = baseRoleMapper.queryNoListForSys(tenantId, mainBodyId, sysCode);
        return list;
    }

    /**
     * 查询应用有权限的角色
     *
     * @return 实例对象
     */
    @Override
    public List<BaseRole> queryListForSys(String sysCode, Integer roleLevel) {
        List<BaseRole> list = baseRoleMapper.queryListForSys(sysCode, roleLevel);
        return list;
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public Result queryAllByLimit(int offset, int limit, String deptId, String mainBodyId, String roleName, HttpServletRequest request) {
        BaseUser baseUser = baseUserService.getUser(request);
        int flag = 1;
        if (baseUser.getUserLevel() >= 3) {//主体管理员/普通员工用户需要验证主体越权情况
            //查找用户在这个企业的角色，没有角色则无权操作
            BaseUserRole baseUserRole = baseUserRoleMapper.queryByMainAndUser(baseUser.getId(), mainBodyId);
            if (baseUserRole == null) {
                flag = 2;
            }

        }
        if (flag == 1) {
            PageHelper.startPage(offset, limit);
//            PageHelper.clearPage();
            List<BaseRole> list = baseRoleMapper.queryAllByLimit(offset, limit, deptId, mainBodyId, baseUser.getTenantId(), roleName);
            return Result.success(new PageInfo<BaseRole>(list));
        } else {
            return Result.success(new PageInfo<BaseRole>());
        }
    }

    /**
     * 查询多条数据
     *
     * @param deptId     查询起始位置
     * @param mainBodyId 查询条数
     * @return 对象列表
     */
    @Override
    public Result queryAll(String deptId, String mainBodyId, HttpServletRequest request) {
        BaseUser baseUser = baseUserService.getUser(request);
        int flag = 1;//是否有权限
        if (baseUser.getUserLevel() >= 3) {
            BaseUserRole baseUserRole = baseUserRoleMapper.queryByMainAndUser(baseUser.getId(), mainBodyId);
            if (baseUserRole != null) {
                flag = 1;
            } else {
                flag = 2;
            }
        }
        if (flag == 1) {
            BaseRole baseRole = new BaseRole();
            baseRole.setMainBodyId(mainBodyId);
            baseRole.setDeptId(deptId);
            baseRole.setUseState(1);
            return Result.success(this.baseRoleMapper.queryAll(baseRole));
        } else {
            return Result.success(null);
        }
    }

    /**
     * 查询多条数据
     *
     * @return 对象列表
     */
    @Override
    public List<BaseRole> queryAll(BaseRole baseRole) {
        return this.baseRoleMapper.queryAll(baseRole);
    }

    /**
     * 根据主体类型和系统角色ID查询对应角色（有角色的更新关联，没有的新增觉得）
     *
     * @return 对象列表
     */
    @Override
    public List<BaseRole> querySysRoleByMainType(BaseRoleVO vo) {
        return this.baseRoleMapper.querySysRoleByMainType(vo);
    }

    /**
     * 根据部门ID和主体ID查询角色列表
     * --根据传入用户ID标记已选中状态
     *
     * @param deptId     查询起始位置
     * @param mainBodyId 查询条数
     * @param userId     用户ID
     * @return 对象列表
     */
    @Override
    public Result queryAllForUser(String deptId, String mainBodyId, String userId, HttpServletRequest request) {
        BaseUser baseUser = baseUserService.getUser(request);

        int flag = 1;//是否有权限查看
        if (baseUser.getUserLevel() >= 3) {//用户为主体管理员时需要验证主体越权情况
            BaseUserRole baseUserRole = baseUserRoleMapper.queryByMainAndUser(baseUser.getId(), mainBodyId);
            if (baseUserRole == null) {
                flag = 2;
            }
        }
        if (flag == 1) {//无越权情况，则返回结果
            List<BaseRoleVO> baseRoleVOList = this.baseRoleMapper.queryAllForUser(deptId, mainBodyId, baseUser.getTenantId(), userId);
            for (BaseRoleVO baseRoleVO : baseRoleVOList) {
                if (baseRoleVO.getUserId() != null) {
                    baseRoleVO.setUserHasRole(1);
                } else {
                    baseRoleVO.setUserHasRole(2);
                }
            }
            return Result.success(baseRoleVOList);
        } else {//有越权，则返回空结果
            return Result.success(null);
        }
    }

    /**
     * 新增数据
     *
     * @param baseRole 实例对象
     * @return 实例对象
     */
    @Override
    public Result insert(BaseRole baseRole, HttpServletRequest request) {
        BaseUser baseUser = baseUserService.getUser(request);
        int flag = 1;//是否有权限查看
        if (baseUser.getUserLevel() >= 3) {//用户为主体管理员时需要验证主体越权情况
            BaseUserRole baseUserRole = baseUserRoleMapper.queryByMainAndUser(baseUser.getId(), baseRole.getMainBodyId());
            if (baseUserRole == null) {
                flag = 2;
            }
        }
        if (flag == 1) {
            BaseRole find = new BaseRole();
            find.setId(baseRole.getId());
            find.setRoleName(baseRole.getRoleName());
            find.setMainBodyId(baseRole.getMainBodyId());
            List<BaseRole> list = baseRoleMapper.queryAll(find);
            if (list != null && list.size() > 0) {
                Result.failed("名称重复");
            }
            if (baseRole.getId() != null) {
                BaseRole oldrole = baseRoleMapper.queryById(baseRole.getId());
                baseRole.setRolerLevel(oldrole.getRolerLevel());//角色等级不变动
                this.baseRoleMapper.update(baseRole);
                return Result.success(baseRoleMapper.queryById(baseRole.getId()));
            } else {
                baseRole.setTenantId(baseUser.getTenantId());
                IdWorker idWorker = new IdWorker(0L, 0L);
                long newId = idWorker.nextId();
                baseRole.setId(String.valueOf(newId));
                this.baseRoleMapper.insert(baseRole);
                return Result.success(baseRole);
            }
        } else {
            return Result.failed("越权操作");
        }
    }

    /**
     * 修改数据
     *
     * @param baseRole 实例对象
     * @return 实例对象
     */
    @Override
    public BaseRole update(BaseRole baseRole) {
        this.baseRoleMapper.update(baseRole);
        return this.baseRoleMapper.queryById(baseRole.getId());
    }

    /**
     * 批量新增数据
     *
     * @return 影响行数
     */
    @Override
    public int batchInsert(List<BaseRole> baseRoleList) {
        return this.baseRoleMapper.batchInsert(baseRoleList);
    }

    /**
     * 变更可用状态
     *
     * @return 实例对象
     */
    @Override
    public int changeUseState(String id, Integer useState, HttpServletRequest request) {
        BaseUser baseUser = baseUserService.getUser(request);
        int a = this.baseRoleMapper.changeUseState(id, useState);
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
        return this.baseRoleMapper.deleteById(id) > 0;
    }
}