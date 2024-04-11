package com.mz.service.base.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.mz.common.context.PageInfo;
import com.mz.common.util.IdWorker;
import com.mz.common.util.Result;
import com.mz.common.util.StringUtils;
import com.mz.mapper.localhost.SysDeftMapper;
import com.mz.model.base.BaseSoftwareGroupClassifySys;
import com.mz.model.base.BaseUser;
import com.mz.model.base.SysArea;
import com.mz.model.base.SysDeft;
import com.mz.model.base.vo.BaseSoftwareGroupClassifySysVO;
import com.mz.model.base.vo.SysDeftSelectVO;
import com.mz.model.base.vo.SysDeftVO;
import com.mz.service.base.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 系统定义表(SysDeft)表服务实现类
 *
 * @author makejava
 * @since 2021-03-17 11:00:04
 */
@Service("sysDeftService")
public class SysDeftServiceImpl implements SysDeftService {
    @Resource
    private SysDeftMapper sysDeftMapper;
    @Resource
    private SysNodeService sysNodeService;
    @Autowired
    private SysAreaService sysAreaService;
    @Autowired
    private BaseSoftwareGroupClassifySysService baseSoftwareGroupClassifySysService;
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
    public SysDeft queryById(String id) {
        return this.sysDeftMapper.queryById(id);
    }

    /**
     * 通过代码查询单条数据
     *
     * @param sysCode 代码
     * @return 实例对象
     */
    @Override
    public SysDeft queryByCode(String sysCode) {
        return this.sysDeftMapper.queryByCode(sysCode);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public PageInfo<SysDeft> queryAllByLimit(int offset, int limit, String sysName, Integer sysType, Integer belongType, String startTime,  String endTime) {
        PageHelper.startPage(offset, limit);
        List<SysDeft> list = sysDeftMapper.queryAllByLimit(sysName, sysType, belongType,startTime,endTime);
        return new PageInfo<SysDeft>(list);
    }

    /**
     * 查询多条数据
     *
     * @param sysDeft 查询起始位置
     * @param sysDeft 查询条数
     * @return 对象列表
     */
    @Override
    public List<SysDeft> queryAll(SysDeft sysDeft) {
        return this.sysDeftMapper.queryAll(sysDeft);
    }

    /**
     * 通过租户ID查询有权限的系统列表
     * @return 对象列表
     */
    @Override
    public Result queryAllForTenant(String sysName) {
        List<SysDeftVO> sysDeftVOS = sysDeftMapper.queryAllBySys(sysName);
        return Result.success(sysDeftVOS);
    }

    /**
     * 查询租户有权限的应用（包含分类选中状态）
     *
     * @param tenantId   租户ID
     * @param classifyId 分类ID
     * @return 对象列表
     */
    @Override
    public List<SysDeftSelectVO> queryAllWithClassify(String tenantId, String classifyId) {
        return this.sysDeftMapper.queryAllWithClassify(tenantId, classifyId);
    }

    /**
     * 新增数据
     *
     * @param sysDeft 实例对象
     * @return 实例对象
     */
    @Override
    public Result insert(SysDeft sysDeft, HttpServletRequest request) throws Exception {
        BaseUser baseUser = baseUserService.getUser(request);
        sysDeft.setModifyTime(DateUtil.now());
        sysDeft.setModifyUser(baseUser.getRealName());
        if (sysDeft.getId() != null) {
            SysDeft findSysDeft = new SysDeft();
            findSysDeft.setSysName(sysDeft.getSysName());
            findSysDeft.setId(sysDeft.getId());//<!--这里查询不等于传入的ID，作为修改时验证名称重复问题，其他情况查询不存在传入ID的情况-->
            List<SysDeft> nameDeftList = sysDeftMapper.queryAll(findSysDeft);
            if (nameDeftList != null && nameDeftList.size() > 0) {
                return Result.failed("系统名称重复");
            } else {
                findSysDeft = new SysDeft();
                findSysDeft.setSysCode(sysDeft.getSysCode());
                findSysDeft.setId(sysDeft.getId());//<!--这里查询不等于传入的ID，作为修改时验证名称重复问题，其他情况查询不存在传入ID的情况-->
                List<SysDeft> codeDeftList = sysDeftMapper.queryAll(findSysDeft);
                if (codeDeftList != null && codeDeftList.size() > 0) {
                    return Result.failed("系统代码重复");
                } else {
                    this.sysDeftMapper.update(sysDeft);
                    return Result.success(sysDeftMapper.queryById(sysDeft.getId()));
                }
            }
        } else {
            SysDeft findSysDeft = new SysDeft();
            findSysDeft.setSysName(sysDeft.getSysName());
            List<SysDeft> nameDeftList = sysDeftMapper.queryAll(findSysDeft);
            if (nameDeftList != null && nameDeftList.size() > 0) {
                return Result.failed("系统名称重复");
            } else {
                findSysDeft = new SysDeft();
                findSysDeft.setSysCode(sysDeft.getSysCode());
                List<SysDeft> codeDeftList = sysDeftMapper.queryAll(findSysDeft);
                if (codeDeftList != null && codeDeftList.size() > 0) {
                    return Result.failed("系统代码重复");
                } else {
                    IdWorker idWorker = new IdWorker(0L, 0L);
                    long newId = idWorker.nextId();
                    sysDeft.setId(String.valueOf(newId));
                    sysDeft.setCreatUser(baseUser.getRealName());
                    sysDeft.setCreatTime(DateUtil.now());
                    this.sysDeftMapper.insert(sysDeft);
                    if (sysDeft.getSysManage().intValue() == 1) {//系统设置的系统需要初始化节点管理页面的节点
                        sysNodeService.initNode(sysDeft.getSysCode());
                    }
                    return Result.success(sysDeft);
                }

            }
        }
    }

    /**
     * 修改数据
     *
     * @param sysDeft 实例对象
     * @return 实例对象
     */
    @Override
    public SysDeft update(SysDeft sysDeft) {
        this.sysDeftMapper.update(sysDeft);
        return this.queryById(sysDeft.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String id) {
        return this.sysDeftMapper.deleteById(id) > 0;
    }
}