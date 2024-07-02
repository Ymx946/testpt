package com.mz.framework.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.model.BaseUser;
import com.mz.common.util.*;
import com.mz.common.util.cache.LocalCache;
import com.mz.framework.annotation.NeedLogin;
import com.mz.framework.annotation.NeedLoginName;
import com.mz.framework.util.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 请求拦截器
 *
 * @Description:
 * @author: yangh
 * @date: 2019年9月19日 下午3:12:07
 */
@Slf4j
@Component
public class AuthInterceptor implements AsyncHandlerInterceptor {
    private static final String LOCK_IP_URL_KEY = "lock_ip_";
    private static final String IP_URL_REQ_TIME = "ip_url_times_";
    private static final long LIMIT_TIMES = 10;
    private static final int IP_LOCK_TIME = 60;
    private final List<String> EXCLOUD_URI = Arrays.asList(
            "index.html", "doc.html", "swagger-resources", "swagger-resources/configuration",
            "v3/api-docs", "v2/api-docs", "webjars", "error",
            "getappletopenid", "getweixinsign", "api-docs", "openMember/queryByCode",
            "tabProcessRemindRecord/queryNum", "druid",
            "verifyCode/verifyCodeImage", "verifyCode/captchaCodeImage", "verifyCode/checkVerifyCode",
            "websocket");
    @Value("${spring.profiles.active}")
    private String profile;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 预处理回调方法，实现处理器的预处理（如检查登陆）， <br />
     * 第三个参数为响应的处理器，自定义Controller <br />
     * 返回值： true表示继续流程（如调用下一个拦截器或处理器）；<br />
     * false表示流程中断（如登录检查失败），不会继续调用其他的拦截器或处理器，此时我们需要通过response来产生响应；
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        // 用户访问的资源地址
        String requestPath = ResourceUtil.getRequestPath(request);
        String ip = WebUtil.getIpAddr(request);
        String requestType = request.getMethod();
        String serverName = request.getServerName();
        String referer = request.getHeader("Referer");
        if ("dev".equalsIgnoreCase(profile) || "test".equalsIgnoreCase(profile) || "bstest".equalsIgnoreCase(profile)) {
            log.info("======ip:" + ip + ", requestType:" + requestType + ", requestPath:" + requestPath + ", serverName:" + serverName + ", referer:" + referer);
        }
        if (StringUtils.isNoneBlank(requestPath) && isExcludeUrl(requestPath)) {
            return true;
        }

        StringBuilder params = getParams(request);
        // 防止重复提交
        boolean isSave = ResourceUtil.isSavePath(requestPath);
        if (isSave && preventDuplicateSubmit(response, requestPath, ip, params)) {
            return false;
        }

        // 判断对象是否是映射到一个方法，如果不是则直接通过
        if (!(object instanceof HandlerMethod)) {
            // instanceof运算符是用来在运行时指出对象是否是特定类的一个实例
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        // 检查方法是否有NeedLogin注解，无则跳过认证
        if (method.isAnnotationPresent(NeedLogin.class)) {
            NeedLogin needLogin = method.getAnnotation(NeedLogin.class);
            if (needLogin.required()) {
                String token = request.getHeader("token");
                if (StringUtils.isBlank(token)) {
                    String msg = ResponseCode.FAILURE_EXPIRE_TOKEN.getMsg();
                    CommonUtil.printJson(response, ResponseCode.FAILURE_EXPIRE_TOKEN.getCode(), "操作失败, " + msg, msg);
                    return false;
                } else {
                    String msg = "";
                    int retCode = checkLoginByRedis(token, request);
                    if (retCode == ResponseCode.FAILURE_EXPIRE_TOKEN.getCode()) {
                        msg = ResponseCode.FAILURE_EXPIRE_TOKEN.getMsg();
                    }
                    if (retCode == ResponseCode.FAILURE_USER_KICKOUT.getCode()) {
                        msg = ResponseCode.FAILURE_USER_KICKOUT.getMsg();
                    }

                    if (StringUtils.isNoneBlank(msg)) {
                        CommonUtil.printJson(response, retCode, "操作失败, " + msg, msg);
                        return false;
                    }
                }
//                return extracted(request, response);
            }
        }
        // 检查方法是否有NeedLoginName注解，无则跳过认证
        if (method.isAnnotationPresent(NeedLoginName.class)) {
            NeedLoginName needLogin = method.getAnnotation(NeedLoginName.class);
            if (needLogin.required()) {
                String token = request.getHeader("token");
                if (StringUtils.isBlank(token) || !checkLoginByName(token, request)) {
                    String msg = ResponseCode.FAILURE_EXPIRE_TOKEN.getMsg();
                    CommonUtil.printJson(response, ResponseCode.FAILURE_EXPIRE_TOKEN.getCode(), "操作失败, " + msg, msg);
                    return false;
                }
//                return extracted(request, response);
            }
        }
        return true;
    }

    /**
     * @param ip
     * @return
     * @Description: 判断ip是否被禁用
     */
    private Boolean ipIsLock(String ip) {
        return redisUtil.hasKey(LOCK_IP_URL_KEY + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + ip);
    }

    /**
     * @Description: 记录请求次数
     */
    private Boolean addRequestTime(String ip, String uri) {
        String key = IP_URL_REQ_TIME + ip + "/" + uri;
        if (redisUtil.hasKey(key)) {
            long time = redisUtil.incrBy(key, 1);
            if (time >= LIMIT_TIMES) {
                redisUtil.getLock(LOCK_IP_URL_KEY + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + ip, ip, IP_LOCK_TIME);
                return false;
            }
        } else {
            redisUtil.getLock(key, (long) 1, 1);
        }
        return true;
    }

    @NotNull
    private StringBuilder getParams(HttpServletRequest request) {
        StringBuilder params = new StringBuilder();
        Map<String, String> requestmap = MessageUtil.getReqParams(request);
        for (String param : requestmap.keySet()) {
            if ("dev".equalsIgnoreCase(profile) || "test".equalsIgnoreCase(profile) || "bstest".equalsIgnoreCase(profile)) {
                log.info("======key:" + param + ", value:" + requestmap.get(param));
            }
            String value = requestmap.get(param);
            if (StringUtils.isNoneBlank(value)) {
                params.append(param);
                params.append("=");
                params.append(value);
                params.append("&");
            }
        }
        return params;
    }

    /**
     * 防止表单重复提交
     *
     * @param response
     * @param requestPath
     * @param ip
     * @param params
     * @return
     * @throws IOException
     */
    private boolean preventDuplicateSubmit(HttpServletResponse response, String requestPath, String ip, StringBuilder params) throws Exception {
        String requestKey = requestPath + ip;
        String accessKey = ConstantsCacheUtil.ACCESS_KEY + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + requestKey.hashCode() + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + params.toString().hashCode();
        // 缓存1秒钟
        if (null == LocalCache.getKey(accessKey)) {
            LocalCache.setKey(accessKey, "1");
        } else {
            String msg = ResponseCode.REQUEST_DOT_ALLOWED.getMsg();
            CommonUtil.printJson(response, ResponseCode.REQUEST_DOT_ALLOWED.getCode(), "操作失败, " + msg, msg);
            return true;
        }
        return false;
    }

    /**
     * 验证登录Redis
     *
     * @param token 主键
     * @return 是否成功
     */
    public int checkLoginByRedis(String token, HttpServletRequest request) {
        if (StringUtils.isAnyBlank(token)) {
            return ResponseCode.FAILURE_EXPIRE_TOKEN.getCode();
        }

        // 请求头有登录ID，则根据ID去库中获取用户信息
        String loginID = request.getHeader("loginID");
        // 登录类型 PC, APP, APPLET
        String loginType = request.getHeader("loginType");
        if (StringUtils.isAnyBlank(loginID)) {
            return ResponseCode.FAILURE_EXPIRE_TOKEN.getCode();
        }

        String baseUserStr = "";
        if (LoginType.PC.getCode().equals(loginType)) {
            String redisToken = redisUtil.get(ConstantsCacheUtil.LOGIN_TOKEN + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginID);
            if (StringUtils.isAnyBlank(redisToken)) {
                return ResponseCode.FAILURE_EXPIRE_TOKEN.getCode();
            }
//            log.info("=====token: " + token + "，=====redisToken: " + redisToken);
            if (!token.equalsIgnoreCase(redisToken)) {
                return ResponseCode.FAILURE_USER_KICKOUT.getCode();
            }
            // 获取PC的baseUser
            baseUserStr = redisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginID);
        }

        if (LoginType.APP.getCode().equals(loginType)) {
            String redisAppToken = redisUtil.get(ConstantsCacheUtil.LOGIN_TOKEN_APP + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginID);
            if (StringUtils.isAnyBlank(redisAppToken)) {
                return ResponseCode.FAILURE_EXPIRE_TOKEN.getCode();
            }
//            log.info("=====token: " + token + "，=====redisAppToken: " + redisAppToken);
            if (!token.equalsIgnoreCase(redisAppToken)) {
                return ResponseCode.FAILURE_USER_KICKOUT.getCode();
            }

            baseUserStr = redisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO_APP + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginID);
        }

        if (LoginType.APPLET.getCode().equals(loginType)) {

        }

        if (StringUtils.isNoneBlank(baseUserStr)) {
            JSONObject baseUserJson = JSONObject.parseObject(baseUserStr);
            BaseUser sessionBaseUser = JSONObject.toJavaObject(baseUserJson, BaseUser.class);
            redisUtil.setEx(ConstantsCacheUtil.LOGIN_TOKEN + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginID, token, 300, TimeUnit.MINUTES);
            redisUtil.setEx(ConstantsCacheUtil.LOGIN_USER_INFO + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginID, JSON.toJSONString(sessionBaseUser), 300, TimeUnit.MINUTES);
            return ResponseCode.SUCCESS.getCode();
        }
        return ResponseCode.SERVER_ERROR.getCode();
    }

    /**
     * 验证登录Redis
     *
     * @param token 主键
     * @return 是否成功
     */
    public boolean checkLoginByName(String token, HttpServletRequest request) {
        if (StringUtils.isAnyBlank(token)) {
            return false;
        }

        String userKey = request.getHeader("userKey");//请求头有登录ID，则根据ID去库中获取用户信息
        if (StringUtils.isAnyBlank(userKey)) {
            return false;
        }

        String baseUserStr = redisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + userKey);//获取用户信息
        if (StringUtils.isNoneBlank(baseUserStr)) {
            JSONObject baseUserJson = JSONObject.parseObject(baseUserStr);
            BaseUser sessionBaseUser = JSONObject.toJavaObject(baseUserJson, BaseUser.class);
            redisUtil.setEx(ConstantsCacheUtil.LOGIN_TOKEN + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + userKey, token, 300, TimeUnit.MINUTES);
            redisUtil.setEx(ConstantsCacheUtil.LOGIN_USER_INFO + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + userKey, JSON.toJSONString(sessionBaseUser), 300, TimeUnit.MINUTES);
            return true;
        }
        return false;
    }

    /**
     * 排除不需要拦截的urli
     *
     * @param uri
     * @return
     */
    private boolean isExcludeUrl(String uri) {
        boolean flag = false;
        for (String s : EXCLOUD_URI) {
            if (uri.contains(s)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (null != ex) {
            log.error("发生异常:" + ex.getMessage());
        }
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info(Thread.currentThread().getName() + " 进入afterConcurrentHandlingStarted方法");
    }

}
