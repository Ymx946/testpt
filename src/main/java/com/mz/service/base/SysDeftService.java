package com.mz.service.base;


import com.mz.common.context.PageInfo;
import com.mz.common.util.Result;
import com.mz.model.base.SysDeft;
import com.mz.model.base.vo.SysDeftSelectVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 系统定义表(SysDeft)表服务接口
 *
 * @author makejava
 * @since 2021-03-17 11:00:04
 */
public interface SysDeftService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysDeft queryById(String id);
    /**
     * 通过代码查询单条数据
     *
     * @param sysCode 代码
     * @return 实例对象
     */
    SysDeft queryByCode(String sysCode);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    PageInfo<SysDeft> queryAllByLimit(int offset, int limit, String sysName, Integer sysType,Integer belongType, HttpServletRequest request);

    /**
     * 查询多条数据
     *
     * @param sysDeft 实例对象
     * @return 对象列表
     */
    List<SysDeft> queryAll(SysDeft sysDeft);
    /**
     * 通过租户ID查询有权限的系统列表
     *
     * @param tenantId 租户ID
     * @return 对象列表
     */
     Result queryAllForTenant(String tenantId,Integer applicationType, HttpServletRequest request);

    /**
     * 查询租户有权限的应用（包含分类选中状态）
     *
     * @param tenantId 租户ID
     * @param classifyId 分类ID
     * @return 对象列表
     */
    List<SysDeftSelectVO> queryAllWithClassify(String tenantId, String classifyId);
    /**
     * 新增数据
     *
     * @param sysDeft 实例对象
     * @return 实例对象
     */
    Result insert(SysDeft sysDeft, HttpServletRequest request) throws Exception;

    /**
     * 修改数据
     *
     * @param sysDeft 实例对象
     * @return 实例对象
     */
    SysDeft update(SysDeft sysDeft);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(String id);

}