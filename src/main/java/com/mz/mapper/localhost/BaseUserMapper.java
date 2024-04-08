package com.mz.mapper.localhost;

import com.mz.model.base.BaseUser;
import com.mz.model.base.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表(BaseUser)表数据库访问层
 *
 * @author makejava
 * @since 2021-03-17 09:08:18
 */
public interface BaseUserMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    BaseUser queryById(String id);

    /**
     * 通过登录名查询单条数据
     *
     * @param loginName 登录名
     * @return 实例对象
     */
    BaseUser queryByName(String loginName);

    /**
     * 通过登录名查询单条数据
     *
     * @return 实例对象
     */
    BaseUser queryByAreaCodeAndType(@Param("areaCode") String areaCode, @Param("userType") Integer userType);

    /**
     * 通过手机号查询单条数据（手机号查询的则是用户名或者联系电话匹配的）
     *
     * @param mobilePhone 手机号
     * @return 实例对象
     */
    BaseUser queryByPhoneNo(@Param("mobilePhone") String mobilePhone, @Param("tenantId") String tenantId);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param baseUser 实例对象
     * @return 对象列表
     */
    List<BaseUser> queryAll(BaseUser baseUser);

    /**
     * 新增数据
     *
     * @param baseUser 实例对象
     * @return 影响行数
     */
    int insert(BaseUser baseUser);

    /**
     * 修改数据
     *
     * @param baseUser 实例对象
     * @return 影响行数
     */
    int update(BaseUser baseUser);

    /**
     * 变更可用状态
     *
     * @return 影响行数
     */
    int changeUserState(@Param("id") String id, @Param("useState") Integer useState);

    /**
     * 修改数据
     *
     * @return 影响行数
     */
    int changeLevle(@Param("id") String id, @Param("userLevel") Integer userLevel);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

    /**
     * 查询镇级以上的用户
     *
     * @return 影响行数
     */
    String queryIdentityForUser(@Param("userId") String userId);

    /**
     * 根据用户名字查询用户级别最大的用户ID
     */
    String queryUserIdByRealName(BaseUser baseUser);
}