package com.mz.mapper.localhost;

import com.mz.model.base.SysDeft;
import com.mz.model.base.vo.SysDeftSelectVO;
import com.mz.model.base.vo.SysDeftVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统定义表(SysDeft)表数据库访问层
 *
 * @author makejava
 * @since 2021-03-17 11:00:04
 */
public interface SysDeftMapper {

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
     * 查询指定行数据
     *
     * @return 对象列表
     */
    List<SysDeft> queryAllByLimit(@Param("sysName") String sysName, @Param("sysType") Integer sysType, @Param("belongType") Integer belongType);


    /**
     * 通过实体作为筛选条件查询
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
    List<SysDeft> queryAllForTenant(@Param("tenantId") String tenantId, @Param("applicationType") Integer applicationType,@Param("sysName")String sysName);

    /**
     * 通过租户ID查询有权限的系统列表
     * @return 对象列表
     */
    List<SysDeftVO> queryAllBySys(String sysName);

    /**
     * 查询租户有权限的应用（包含分类选中状态）
     *
     * @param tenantId 租户ID
     * @return 对象列表
     */
    List<SysDeftSelectVO> queryAllWithClassify(@Param("tenantId") String tenantId, @Param("classifyId") String classifyId);

    /**
     * 查询租户有权限的应用--判断主体是否开通
     *
     * @param tenantId 租户ID
     * @return 对象列表
     */
    List<SysDeftSelectVO> queryAllWithMainBody(@Param("tenantId") String tenantId, @Param("classifyId") String classifyId, @Param("mainBodyId") String mainBodyId, @Param("openState") Integer openState, @Param("areaCode") String areaCode, @Param("useType") String useType, @Param("userId") String userId, @Param("userLevel") Integer userLevel, @Param("areaLevel") String areaLevel, @Param("limit") Integer limit);

    /**
     * 通过Id数组作为筛选条件查询
     *
     * @param sysIdStringArr Id数组
     * @return 对象列表
     */
    List<SysDeft> queryAllByIds(String[] sysIdStringArr);

    /**
     * 新增数据
     *
     * @param sysDeft 实例对象
     * @return 影响行数
     */
    int insert(SysDeft sysDeft);

    /**
     * 修改数据
     *
     * @param sysDeft 实例对象
     * @return 影响行数
     */
    int update(SysDeft sysDeft);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

}