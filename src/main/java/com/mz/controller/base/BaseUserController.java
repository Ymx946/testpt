package com.mz.controller.base;

import cn.hutool.core.util.ObjectUtil;
import com.mz.common.ConstantsUtil;
import com.mz.common.util.Result;
import com.mz.framework.annotation.NeedLogin;
import com.mz.service.base.BaseUserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户表(BaseUser)表控制层
 *
 * @author makejava
 * @since 2021-03-17 09:08:18
 */
@Slf4j
@RestController
@RequestMapping(value = {"datacenter/baseUser", "baseUser"})
public class BaseUserController {

    /**
     * 用户服务对象
     */
    @Resource
    private BaseUserService baseUserService;


    /**
     * 修改密码
     */
    @SneakyThrows
    @PostMapping("updatePwd")
    public Result updatePwd(String oldPwd, String newPwd, String token, HttpServletRequest request) {
        if (StringUtils.isEmpty(oldPwd)) {
            return Result.failed("旧密码必填");
        }
        if (StringUtils.isEmpty(newPwd)) {
            return Result.failed("新密码必填");
        }
        if (newPwd.length() < 8) {
            return Result.failed("新密码必须大于8位");
        }
//        if (!RegexUtils.isComplexityMatches3(newPwd, 8, 20)) {
//            return Result.failed("该账号密码过于简单，建议修改密码");
//        }

        if (baseUserService.checkLogin(token, request)) {
            return baseUserService.updatePwd(oldPwd, newPwd, request);
        } else {
            return Result.failedLogin();
        }
    }

    /**
     * 用户登录
     */
    @SneakyThrows
    @PostMapping("userLogin")
    public Result userLogin(String loginName, String password, String systemCode, HttpServletRequest request) {
        if (StringUtils.isEmpty(loginName)) {
            return Result.failed("登录名必填");
        }
        if (StringUtils.isEmpty(password)) {
            return Result.failed("密码必填");
        }

        /*if(!RegexUtils.isComplexityMatches3(newPwd, 8, 20)) {
            return Result.failed("该账号密码过于简单，建议修改密码");
        }*/
        return this.baseUserService.userLogin(loginName, password, systemCode, null, 0, request);
    }

    /**
     * 检查密码是否超过90天
     */
    @NeedLogin
    @PostMapping("checkPwd")
    public Result checkPwd(@RequestHeader(value = "loginID") String loginID) {
        if (org.springframework.util.StringUtils.isEmpty(loginID)) {
            return Result.failed("loginID不能为空");
        }
        return this.baseUserService.checkPwd(loginID);
    }

    /**
     * 注意: 超级管理员、主体管理员、租户管理员可解锁才可以解锁
     * 锁定或解锁账户
     */
    @NeedLogin
    @PostMapping("lockAccount")
    public Result lockAccount(Integer lockType, String userId, @RequestHeader(value = "loginID") String loginId) {
        if (StringUtils.isEmpty(loginId)) {
            return Result.failed("loginId不能为空");
        }
        if (StringUtils.isEmpty(userId)) {
            return Result.failed("锁定或解锁账户Id不能为空");
        }

        if (ObjectUtil.isEmpty(lockType)) {
            return Result.failed("锁定或解锁的类型不能为空");
        }

        int lockInt = lockType.intValue();
        if (lockInt == ConstantsUtil.USER_ACCOUNT_UNLOCK) {
            return this.baseUserService.lockAccount(lockType, userId, loginId);
        }
        if (lockInt == ConstantsUtil.USER_ACCOUNT_LOCKED) {
            return this.baseUserService.lockAccount(lockType, userId, loginId);
        }
        return Result.failed();
    }


}