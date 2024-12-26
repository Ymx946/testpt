package com.mz.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mz.common.context.PageInfo;
import com.mz.common.model.BaseMainBody;
import com.mz.common.util.Result;
import com.mz.model.base.BaseSiteHost;
import com.mz.model.base.BaseUser;
import com.mz.model.base.vo.BaseSiteHostVO;
import com.mz.model.base.vo.BaseUserNewVO;
import com.mz.model.base.vo.BaseUserVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户表(BaseUser)表服务接口
 *
 * @author makejava
 * @since 2021-03-17 09:08:18
 */
public interface BaseUserService extends IService<BaseUser> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    BaseUser queryById(String id);

    Result lockAccount(Integer lockType, String userId, String loginId);

    Result checkPwd(String loginId);

    /**
     * 验证登录
     *
     * @param token 主键
     * @return 是否成功
     */
    boolean checkLogin(String token, HttpServletRequest request);

    /**
     * 登出
     *
     * @return 是否成功
     */
    Result userLoginOut(HttpServletRequest request);

    /**
     * 修改密码
     *
     * @param newPwd 新密码
     * @return 实例对象
     */
    Result updatePwd(String oldPwd, String newPwd, HttpServletRequest request);

    /**
     * 用户登录
     *
     * @param loginName 登录名
     * @param password  密码
     * @return 1登录成功2用户不存在3密码错误4登录异常
     */
    Result userLogin(String loginName, String password, String systemCode, String sysCodes, Integer appLogin, HttpServletRequest request);

    BaseUser getUser(HttpServletRequest request);

    /**
     * 新增数据
     *
     * @param baseUser 实例对象
     * @return 实例对象
     */
    Result insert(BaseUser baseUser, HttpServletRequest request);

    /**
     * 重置(重置为默认密码 gzsz@2024)
     *
     * @return 实例对象
     */
    Result reSetPwd(String id, String password, HttpServletRequest request);

    /**
     * 分页列表
     *
     */
    PageInfo<BaseUser> queryAllByLimit(BaseUserNewVO vo);
}