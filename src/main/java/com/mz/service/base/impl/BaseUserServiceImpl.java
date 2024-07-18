package com.mz.service.base.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.ConstantsUtil;
import com.mz.common.util.*;
import com.mz.framework.util.redis.RedisUtil;
import com.mz.mapper.localhost.BaseUserMapper;
import com.mz.model.base.BaseUser;
import com.mz.model.base.vo.UserLoginVO;
import com.mz.service.base.BaseUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户表(BaseUser)表服务实现类
 *
 * @author makejava
 * @since 2021-03-17 09:08:18
 */
@Slf4j
@Service("baseUserService")
public class BaseUserServiceImpl extends ServiceImpl<BaseUserMapper, BaseUser> implements BaseUserService {
    @Resource
    private BaseUserMapper baseUserMapper;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BaseUser queryById(String id) {
        return this.baseUserMapper.queryById(id);
    }

    @Override
    public Result lockAccount(Integer lockType, String userId, String loginId) {
        BaseUser baseUser = JSONObject.toJavaObject(JSONObject.parseObject(redisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginId)), BaseUser.class);
        Integer userLevel = baseUser.getUserLevel();
        if (!(userLevel == 1 || userLevel == 2 || userLevel == 3)) {
            return Result.success("非超级管理员、主体管理员、租户管理员身份不能进行账户锁定或解锁操作");
        }

        int lockInt = lockType.intValue();
        try {
            if (lockInt == ConstantsUtil.USER_ACCOUNT_UNLOCK) {
                if ("LOCK".equalsIgnoreCase(redisUtil.get(ConstantsCacheUtil.LOGIN_USER_LOCK + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + userId))) {
                    List<String> delKeyList = new ArrayList<>();
                    delKeyList.add(ConstantsCacheUtil.LOGIN_USER_FAILCOUNT + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + userId);
                    delKeyList.add(ConstantsCacheUtil.LOGIN_USER_LOCK + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + userId);
                    redisUtil.delete(delKeyList);
                }
                return Result.success("该账户解锁成功");
            }

            if (lockInt == ConstantsUtil.USER_ACCOUNT_LOCKED) {
                if (!"LOCK".equalsIgnoreCase(redisUtil.get(ConstantsCacheUtil.LOGIN_USER_LOCK + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + userId))) {
                    redisUtil.setEx(ConstantsCacheUtil.LOGIN_USER_LOCK + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + userId, "LOCK", ConstantsCacheUtil.LOGIN_USER_LOCK_MINUTES, TimeUnit.MINUTES);
                }
                return Result.success("该账户锁定成功");
            }
            return Result.failed();
        } catch (Exception e) {
            String msg = "";
            if (lockInt == ConstantsUtil.USER_ACCOUNT_UNLOCK) {
                msg = "该账户解锁失败";
            }
            if (lockInt == ConstantsUtil.USER_ACCOUNT_LOCKED) {
                msg = "该账户锁定失败";
            }
            log.error(msg + "，原因：" + e);
            return Result.failed(msg);
        }
    }

    @Override
    public Result checkPwd(String loginId) {
        BaseUser baseUser = JSONObject.toJavaObject(JSONObject.parseObject(redisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginId)), BaseUser.class);
        String pwdModifyTime = baseUser.getPwdModifyTime();

        // 90天未设置密码登入后台弹框展示，“该账号长期未更换密码，建议修改密码，保障账号的安全性~”
        if (ObjectUtil.isNotEmpty(pwdModifyTime)) {
            if (DateUtils.daysBetween(DateUtil.dateFormat(pwdModifyTime, DateUtil.DATE_FORMAT_YMDHMS), new Date()) > ConstantsCacheUtil.PWD_EXPIRE_DAYS) {
                ResponseCode failureUserExpire = ResponseCode.FAILURE_USER_EXPIRE;
                return Result.instance(failureUserExpire.getCode(), failureUserExpire.getMsg());
            }
        }
        return Result.success();
    }

    /**
     * 验证登录(拼接用户ID从数据库中获取用户信息)
     *
     * @param token 主键
     * @return 是否成功
     */
    @Override
    public boolean checkLogin(String token, HttpServletRequest request) {
        if (StringUtils.isEmpty(token)) {
            return false;
        }

        String loginIDStr = request.getHeader("loginID");// 请求头有登录ID，则根据ID去库中获取用户信息
        if (StringUtils.isEmpty(loginIDStr)) {
            return false;
        }

        String redisToken = redisUtil.get(ConstantsCacheUtil.LOGIN_TOKEN + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginIDStr);
        if ((token.equalsIgnoreCase(redisToken))) {// 先比较PC/大屏的TOKEN，验证不成功则再验证APP的TOKEN
            log.info("pc验证成功");
            BaseUser sessionBaseUser = getUserRedis(request);
            if (sessionBaseUser == null) {
                sessionBaseUser = queryById(loginIDStr);
            }
            return true;
        } else {
            String redisAppToken = redisUtil.get(ConstantsCacheUtil.LOGIN_TOKEN_APP + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginIDStr);
            if (token.equalsIgnoreCase(redisAppToken)) {
                log.info("app验证成功");
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 登出
     *
     * @return 是否成功
     */
    public Result userLoginOut(HttpServletRequest request) {
        String loginIDStr = request.getHeader("loginID");// 请求头有登录ID，则根据ID去库中获取用户信息
        List<String> redisKeyList = new ArrayList<>();
        redisKeyList.add(ConstantsCacheUtil.LOGIN_TOKEN + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginIDStr);
        redisKeyList.add(ConstantsCacheUtil.LOGIN_USER_INFO + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginIDStr);
        redisUtil.delete(redisKeyList);
        return Result.success();
    }

    /**
     * 获取用户
     */
    public BaseUser getUserRedis(HttpServletRequest request) {
        BaseUser sessionbaseUser = null;
        String loginIDStr = request.getHeader("loginID");// 请求头有登录ID，则根据ID去库中获取用户信息
        if (!StringUtils.isEmpty(loginIDStr)) {
            String baseUserStr = redisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginIDStr);// 获取PC的baseUser4
            if (StringUtils.isEmpty(baseUserStr)) {// 没有则获取APP的baseUser
                baseUserStr = redisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO_APP + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginIDStr);
            }
            if (!StringUtils.isEmpty(baseUserStr)) {
                JSONObject baseUserJson = JSONObject.parseObject(baseUserStr);
                sessionbaseUser = JSONObject.toJavaObject(baseUserJson, BaseUser.class);
            }
            if (sessionbaseUser == null) {
                sessionbaseUser = queryById(loginIDStr);
                redisUtil.setEx(ConstantsCacheUtil.LOGIN_USER_INFO + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginIDStr, JSON.toJSONString(sessionbaseUser, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty), 300, TimeUnit.MINUTES);
            }
        }
        return sessionbaseUser;
    }

    /**
     * 修改密码
     *
     * @param newPwd 新密码
     * @return 实例对象
     */
    @Override
    public Result updatePwd(String oldPwd, String newPwd, HttpServletRequest request) {
        BaseUser sessionbaseUser = getUser(request);
        BaseUser oldBaseUser = baseUserMapper.queryById(sessionbaseUser.getId());
        // 明码密码前三位+盐+明码密码3位以后---转MD5--最终密码
        String oldPwdMD5 = PasswordUtil.md5(oldPwd.substring(0, 3) + oldBaseUser.getPwdSalt() + oldPwd.substring(3));
        if (oldPwdMD5.equals(oldBaseUser.getPassword())) {
            String newPwdMD5 = PasswordUtil.md5(newPwd.substring(0, 3) + oldBaseUser.getPwdSalt() + newPwd.substring(3));
            oldBaseUser.setPassword(newPwdMD5);
            oldBaseUser.setRawPwdMd5(newPwd);
//            oldBaseUser.setRawPwd(newPwd);
            oldBaseUser.setPwdModifyTime(DateUtil.getNowStringTime());
            baseUserMapper.update(oldBaseUser);
            return Result.success();

        } else {
            return Result.failed("旧密码验证错误");
        }
    }

    /**
     * 获取用户
     */
    @Override
    public BaseUser getUser(HttpServletRequest request) {
        // session
//        BaseUser sessionBaseUser = SessionUtil.getUser(request);
//        String loginIDStr =  request.getHeader("loginID");//请求头有登录ID，则根据ID去库中获取用户信息
//        if(!StringUtils.isEmpty(loginIDStr)) {
//            sessionbaseUser = this.baseUserMapper.queryById(loginIDStr);
//        }
        // Redis

        BaseUser sessionBaseUser = getUserRedis(request);
        return sessionBaseUser;
    }

    /**
     * 用户登录
     *
     * @param loginName 登录名
     * @param password  密码
     * @return 实例对象 其中登录结果：1登录成功2用户不存在/用户不可用3密码错误
     */
    public Result userLogin(String loginName, String password, String systemCode, String sysCodes, Integer appLogin, HttpServletRequest request) {
        UserLoginVO userLoginVO = new UserLoginVO();
        BaseUser baseUser = this.baseUserMapper.queryByName(loginName);
        if (baseUser != null && baseUser.getUseState() == 1) {
            String oldepwd = baseUser.getPassword();
            String pwdSalt = baseUser.getPwdSalt();
            String pwdModifyTime = baseUser.getPwdModifyTime();
//            //明码密码前三位+盐+明码密码3位以后---转MD5--传入的最终密码
//            if(password.length()!=32){//如果不是32位，则说明密码前端未加密
//                password = PasswordUtil.md5(password);
//            }

            String userId = baseUser.getId();
            if ("LOCK".equals(redisUtil.get(ConstantsCacheUtil.LOGIN_USER_LOCK + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + userId))) {
                ResponseCode failureUserLocked = ResponseCode.FAILURE_USER_LOCKED;
                Long expireTime = redisUtil.getExpire(ConstantsCacheUtil.LOGIN_USER_LOCK + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + userId);
                return Result.instance(failureUserLocked.getCode(), String.format(failureUserLocked.getMsg(), expireTime / 60));
            }

            String inpwd = PasswordUtil.md5(password.substring(0, 3) + pwdSalt + password.substring(3));
            if (!oldepwd.equals(inpwd)) {
                redisUtil.incrBy(ConstantsCacheUtil.LOGIN_USER_FAILCOUNT + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + userId, 1);
                // 计数大于5时，设置用户被锁定30分钟
                int loginFailCount = 0;
                if (redisUtil.hasKey(ConstantsCacheUtil.LOGIN_USER_FAILCOUNT + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + userId)) {
                    loginFailCount = Integer.parseInt(redisUtil.get(ConstantsCacheUtil.LOGIN_USER_FAILCOUNT + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + userId));
                }
                if (loginFailCount >= ConstantsCacheUtil.LOGIN_FAIL_NUM) {
                    redisUtil.setEx(ConstantsCacheUtil.LOGIN_USER_LOCK + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + userId, "LOCK", ConstantsCacheUtil.LOGIN_USER_LOCK_MINUTES, TimeUnit.MINUTES);
                    redisUtil.expire(ConstantsCacheUtil.LOGIN_USER_FAILCOUNT + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + userId, ConstantsCacheUtil.LOGIN_USER_LOCK_MINUTES, TimeUnit.MINUTES);

                    ResponseCode failureUserLocked = ResponseCode.FAILURE_USER_LOCKED;
                    return Result.instance(failureUserLocked.getCode(), String.format(failureUserLocked.getMsg(), ConstantsCacheUtil.LOGIN_USER_LOCK_MINUTES));
                }

                userLoginVO.setLoginResult(3);
                ResponseCode failureUserFailcount = ResponseCode.FAILURE_USER_FAILCOUNT;
                // 剩余登录次数
                int leftNum = ConstantsCacheUtil.LOGIN_FAIL_NUM - loginFailCount;
                return Result.instance(failureUserFailcount.getCode(), String.format(failureUserFailcount.getMsg(), leftNum));
//                ResponseCode errorpassword = ResponseCode.ERRORPASSWORD;
//                return Result.instance(errorpassword.getCode(), errorpassword.getMsg());
            }

            // 90天未设置密码登入后台弹框展示，“该账号长期未更换密码，建议修改密码，保障账号的安全性~”
            if (ObjectUtil.isNotEmpty(pwdModifyTime)) {
                if (DateUtils.daysBetween(DateUtil.dateFormat(pwdModifyTime, DateUtil.DATE_FORMAT_YMDHMS), new Date()) > ConstantsCacheUtil.PWD_EXPIRE_DAYS) {
                    userLoginVO.setPwdExpired(ConstantsCacheUtil.pwd_EXPIRED_YES);
                    userLoginVO.setPwdExpiredContent(ResponseCode.FAILURE_USER_EXPIRE.getMsg());
                }
            }

            if (!StringUtils.isEmpty(sysCodes)) {
                String[] sysCodeArr = sysCodes.split(",");
                String[] newSysCodeArr = null;
                if (sysCodeArr != null && sysCodeArr.length > 0) {
                    newSysCodeArr = new String[sysCodeArr.length];
                    int i = 0;
                    for (String sysCode : sysCodeArr) {
                        newSysCodeArr[i] = sysCode.trim();
                        i++;
                    }
                }
            }
            String loginTime = DateUtil.getNowStringTime();
            if (appLogin != null && appLogin == 1) {// app登录
                baseUser.setAppLoginTime(loginTime);
            } else {
                baseUser.setLoginTime(loginTime);
            }
            baseUser.setLoginState(1);
            baseUserMapper.update(baseUser);
            String loginMsg = userId + "mzsz" + loginTime;
            String token = PasswordUtil.md5(loginMsg);
//                request.getSession().setAttribute(ConstantsCacheUtil.LOGIN_USER_INFO,baseUser);
            userLoginVO.setLoginResult(1);// 登录结果：1登录成功
            userLoginVO.setLoginID(userId);
            userLoginVO.setToken(token);
            userLoginVO.setLoginName(baseUser.getLoginName());
            userLoginVO.setManageArea(baseUser.getManageArea());
            userLoginVO.setManageAreaName(baseUser.getManageAreaName());
            userLoginVO.setLoginState(baseUser.getLoginState());
            userLoginVO.setUserLevel(baseUser.getUserLevel());
            userLoginVO.setUserType(baseUser.getUserType());
            userLoginVO.setAreaCode(baseUser.getAreaCode());
            userLoginVO.setTenantId(baseUser.getTenantId());
            userLoginVO.setRealName(baseUser.getRealName());
            if (userLoginVO.getUserLevel().intValue() >= 3 && userLoginVO.getMainBodyId() == null) {// 主体管理员及以下用户登录时，找不到可用主体，不允许登录
                return Result.instance(0, "用户暂无权限");
            }
            baseUser.setAreaCode(userLoginVO.getAreaCode());// 存入Redis的用户信息代码以主体代码为准
            if (appLogin != null && appLogin == 1) {// app登录(默认一直有效)
                redisUtil.setEx(ConstantsCacheUtil.LOGIN_TOKEN_APP + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + baseUser.getId(), token, 365, TimeUnit.DAYS);
                redisUtil.setEx(ConstantsCacheUtil.LOGIN_USER_INFO_APP + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + baseUser.getId(), JSON.toJSONString(baseUser), 365, TimeUnit.DAYS);
            } else {
                redisUtil.setEx(ConstantsCacheUtil.LOGIN_TOKEN + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + userId, token, 300, TimeUnit.MINUTES);
                redisUtil.setEx(ConstantsCacheUtil.LOGIN_USER_INFO + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + userId, JSON.toJSONString(baseUser, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty), 300, TimeUnit.MINUTES);
            }

            List<String> delKeyList = new ArrayList<>();
            delKeyList.add(ConstantsCacheUtil.LOGIN_USER_FAILCOUNT + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + userId);
            delKeyList.add(ConstantsCacheUtil.LOGIN_USER_LOCK + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + userId);
            redisUtil.delete(delKeyList);
            return Result.success(userLoginVO);

        } else {
            userLoginVO.setLoginResult(2);// 登录结果：2用户不存在/用户不可用
            ResponseCode userLoginNotexits = ResponseCode.USER_LOGIN_NOTEXITS;
            return Result.instance(99998, userLoginNotexits.getMsg());
        }
    }

}