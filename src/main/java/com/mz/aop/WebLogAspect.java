package com.mz.aop;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.mz.common.annotation.FieldMeta;
import com.mz.common.util.*;
import com.mz.common.util.reflect.ReflectionUtils;
import com.mz.framework.annotation.OperationLog;
import com.mz.framework.service.common.ContentParser;
import com.mz.framework.service.common.OperaLogService;
import com.mz.framework.util.redis.RedisUtil;
import com.mz.framework.web.util.DeviceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @author yangh
 * @date 2024/05/07 15:19
 * @desc 类功能描述。
 * @link <a href="https://blog.csdn.net/qq_29238615/article/details/134318166">...</a>
 * @link <a href="https://blog.csdn.net/qq_44299529/article/details/132061701">...</a>
 */
@Aspect
@Component
@Slf4j
public class WebLogAspect {
    /**
     * 保存修改之前的数据
     */
    public static ThreadLocal<Map<String, Object>> oldMapThreadLocal = new ThreadLocal<>();
    /**
     * JoinPoint
     */
    public static ThreadLocal<JoinPoint> joinPointThreadLocal = new ThreadLocal<>();

    @Lazy
    @Resource
    private RedisUtil redisUtil;
    @Lazy
    @Resource
    private OperaLogService operaLogService;
    @Resource
    private ApplicationContext applicationContext;

    @Value("${geoLite.geodatebase}")
    private String geodatebase;
    @Value("${geoLite.geodatebase2018}")
    private String geodatebase2018;

    public WebLogAspect() {
    }


    /**
     * 定义切入点，切入点为com.mz.controller下的所有函数
     */
    @Pointcut("@annotation(com.mz.framework.annotation.OperationLog)")
    public void webLog() {
    }

    /**
     * 前置通知：在连接点之前执行的通知
     *
     * @param joinPoint
     * @throws Throwable
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        String methodName = joinPoint.getSignature().getName();
        Map<String, String> headerParamMap = MessageUtil.getHeaderParams(request);
        Map<String, String> reqParamMap = MessageUtil.getReqParams(request);

        //设置ThreadLocal值
        joinPointThreadLocal.set(joinPoint);

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        OperationLog operationLog = signature.getMethod().getAnnotation(OperationLog.class);
        String operatorTypeKey = operationLog.operatorType().getKey();
        if ("insert".equalsIgnoreCase(operatorTypeKey) || "update".equalsIgnoreCase(operatorTypeKey) || "delete".equalsIgnoreCase(operatorTypeKey)) {
            if (reqParamMap.containsKey("id") && ObjectUtil.isNotEmpty(reqParamMap.get("id"))) {
                ContentParser contentParser = (ContentParser) applicationContext.getBean(operationLog.parseclass());
                Object oldObject = contentParser.getResult(joinPoint, operationLog, operationLog.tableId());
                oldMapThreadLocal.set(BeanUtil.beanToMap(oldObject)); // 存储修改前的对象
            }
        }

        // 记录下请求内容
        log.info("method[{}]，request path: {}", methodName, request.getRequestURL().toString());
        log.info("method[{}]，request method: {}.{}", methodName, joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        log.info("method[{}]，header param: {}", methodName, headerParamMap);
        log.info("method[{}]，request param: {}", methodName, reqParamMap);
    }

    /**
     * 后置通知
     *
     * @param joinPoint
     * @param ret
     * @throws Throwable
     */
    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(JoinPoint joinPoint, Object ret) {
//        String methodName = joinPoint.getSignature().getName();
        // 处理完请求，返回内容
//        log.info("method[" + methodName + "] return content: " + ret);

        if (ObjectUtil.isNotEmpty(ret)) {
            saveLog(joinPoint, ret);
        }
    }

    /**
     * 环绕通知：
     * 环绕通知非常强大，可以决定目标method是否执行，什么时候执行，执行时是否需要替换method参数，执行完毕是否需要替换返回值。
     * 环绕通知第一个参数必须是org.aspectj.lang.ProceedingJoinPoint类型
     */
    @Around(value = "webLog()")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String methodName = proceedingJoinPoint.getSignature().getName();
        long startTime = System.currentTimeMillis();
        Object obj = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();
        log.info("method[{}] deal time: {} ms", methodName, endTime - startTime);
        return obj;
    }

    /**
     * 该注解标注的method在所有的Advice执行完成后执行，无论业务模块是否抛出异常，类似于finally的作用；
     *
     * @param joinPoint
     */
    @After(value = "webLog()")
    public void logEnd(JoinPoint joinPoint) {
        // 移除ThreadLocal值
        removeThreadLocal();

        String methodName = joinPoint.getSignature().getName();
        log.info("method[{}]remove thread local content", methodName);
    }

    private void removeThreadLocal() {
        joinPointThreadLocal.remove();
        oldMapThreadLocal.remove();
    }

    /**
     * 异常通知
     *
     * @param ex
     */
    @AfterThrowing(throwing = "ex", value = "webLog()")
    public void logEnd(Exception ex) {
        String errorMsg = ex.getMessage();
        log.error("异常，原因：{}", errorMsg);

        // 保存日志
        saveLog(null, ex);
    }

    /**
     * 添加日志
     *
     * @param joinPoint
     * @param ret
     */
    private void saveLog(JoinPoint joinPoint, Object ret) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            assert attributes != null;
            HttpServletRequest request = attributes.getRequest();

            if (ObjectUtil.isEmpty(joinPoint)) {
                joinPoint = joinPointThreadLocal.get();
            }

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            OperationLog operationLog = signature.getMethod().getAnnotation(OperationLog.class);
            String methodName = joinPoint.getSignature().getName();
//        Object[] args = joinPoint.getArgs();
//        log.info("args： " + args.toString());

            Map<String, String> headerParamMap = MessageUtil.getHeaderParams(request);
            Map<String, String> reqParamMap = MessageUtil.getReqParams(request);

            String userId = null, loginName = null, headerParam, reqParam = null, jsonResult = null, errorMsg = null;
            StringBuilder operaContent;

            String operatorTypeKey = operationLog.operatorType().getKey();
            if ("userLogin".equalsIgnoreCase(methodName) || "login".equalsIgnoreCase(operatorTypeKey)) {
                loginName = reqParamMap.get("loginName");
                operaContent = new StringBuilder(String.format(operationLog.operatorType().getValue(), loginName));
            } else {
                operaContent = new StringBuilder(operationLog.logDetails());
            }

            if (ret instanceof Exception) {
//            errorMsg = ((Exception) ret).getMessage();
                errorMsg = ExceptionUtils.getStackTrace((Throwable) ret);
            } else {
                ContentParser contentParser;
                Object retData = null;
                if (ObjectUtil.isNotEmpty(ret)) {
                    retData = ((Result) ret).getData();
                    jsonResult = JSON.toJSONString(retData);
                }

                if ("userLogin".equalsIgnoreCase(methodName) || "login".equalsIgnoreCase(operatorTypeKey)) {
                    loginName = reqParamMap.get("loginName");
                    operaContent = new StringBuilder(String.format(operationLog.operatorType().getValue(), loginName));
                    if (StringUtil.isNotEmpty(jsonResult)) {
                        if (jsonResult.startsWith("{")) {
                            JSONObject jsonObject = JSON.parseObject(jsonResult);
                            if (jsonObject.containsKey("loginID")) {
                                userId = jsonObject.getString("loginID");
                            }
                            if (jsonObject.containsKey("failMsg")) { // 登录失败不记录日志
//                            operaContent = new StringBuilder(jsonObject.getString("failMsg"));
                                operaContent = new StringBuilder();
                            }
                        } else {
                            operaContent = new StringBuilder(jsonResult);
                        }
                    }
                }

                String logDetails = operationLog.logDetails();
                if ("import".equalsIgnoreCase(operatorTypeKey) || "export".equalsIgnoreCase(operatorTypeKey)) {
                    // 导入"低保户-4528人"
                    operaContent = new StringBuilder(String.format(logDetails, retData));
                }

                if ("insert".equalsIgnoreCase(operatorTypeKey) ||
                        "update".equalsIgnoreCase(operatorTypeKey) ||
                        "delete".equalsIgnoreCase(operatorTypeKey) ||
                        "link".equalsIgnoreCase(operatorTypeKey)) {
                    if (CommonUtil.isPrimitive(retData.getClass())) {
                        try {
                            // 如果返回的是基本类型，则需要重新查询
                            if (reqParamMap.containsKey("id") && ObjectUtil.isNotEmpty(reqParamMap.get("id"))) {
                                contentParser = (ContentParser) applicationContext.getBean(operationLog.parseclass());
                                retData = contentParser.getResult(joinPoint, operationLog, operationLog.tableId());
                            }
                        } catch (Exception e) {
                            log.error("service加载失败:", e);
                        }
                    }

                    String[] logDetailsAry = {};
                    String logDetailsKey = "", fieldName = "";
                    if ("insert".equalsIgnoreCase(operatorTypeKey) || "delete".equalsIgnoreCase(operatorTypeKey)) {
                        if (logDetails.contains("#")) {
                            logDetailsAry = logDetails.split("#");
                            logDetailsKey = logDetailsAry[1];
                        }
                        assert retData != null;
                        Field field = ReflectionUtils.getAccessibleField(retData, logDetailsKey);
                        if (ObjectUtil.isNotEmpty(field)) {
                            assert field != null;
                            FieldMeta fieldMeta = field.getAnnotation(FieldMeta.class);
                            if (ObjectUtil.isNotEmpty(fieldMeta)) {
                                fieldName = fieldMeta.name();
                            }
                        }
                    }
                    if (reqParamMap.containsKey("id") && ObjectUtil.isNotEmpty(reqParamMap.get("id"))) {
                        operaContent = new StringBuilder();
                        if ("insert".equalsIgnoreCase(operatorTypeKey) || "update".equalsIgnoreCase(operatorTypeKey)) {
                            operaContent.append("编辑");
                        }
                        //比较新数据与数据库原数据
                        Map<String, Object> oldMap = oldMapThreadLocal.get();
                        if (ObjectUtil.isNotEmpty(retData) && ObjectUtil.isNotEmpty(oldMap)) {
                            List<Map<String, Object>> mapList = CommonUtil.getObjPropOldNew(oldMap, retData);
                            for (Map<String, Object> dataMap : mapList) {
                                Object filedName = dataMap.get("filedName");
                                Object oldValue = dataMap.get("oldValue");
                                Object newValue = dataMap.get("newValue");
                                // 编辑李四111"姓名"，由"李四111"改为"李四2"；"身份证"，由"420121199912221381"改为"4****************1"；"联系方式"，由"13028462542"改为"130****2542"；
                                if (ObjectUtil.isNotEmpty(newValue) && !oldValue.equals(newValue)) {
                                    if ("useState".equals(filedName)) {
                                        operaContent.append("[").append(filedName).append("]").append("由\"");
                                        if (ObjectUtil.isNotEmpty(oldValue)) {
                                            operaContent.append(2 == (int) oldValue ? "删除" : "可用");
                                        }
                                        operaContent.append("\"改为：\"");
                                        if (ObjectUtil.isNotEmpty(newValue)) {
                                            operaContent.append(2 == (int) newValue ? "删除" : "可用");
                                        }
                                        operaContent.append("\"；");
                                    } else {
                                        operaContent.append("[").append(filedName).append("]").append("由\"").append(oldValue).append("\"改为：\"").append(newValue).append("\"；");
                                    }
                                }
                            }
                        }

                        if ("delete".equalsIgnoreCase(operatorTypeKey) && oldMap.containsKey(logDetailsKey)) {
                            operaContent = new StringBuilder("删除" + logDetailsAry[0] + "-" + fieldName + "：" + "\"" + oldMap.get(logDetailsKey) + "\"");
                        }

                        if ("link".equalsIgnoreCase(operatorTypeKey) && oldMap.containsKey(logDetailsKey)) {
                            operaContent = new StringBuilder("关联" + logDetailsAry[0] + "-" + fieldName + "：" + "\"" + oldMap.get(logDetailsKey) + "\"");
                        }
                    } else {
                        Map<String, Object> newMap = BeanUtil.beanToMap(retData);
                        if ("insert".equalsIgnoreCase(operatorTypeKey) && newMap.containsKey(logDetailsKey)) {
                            operaContent = new StringBuilder("新增" + logDetailsAry[0] + "-" + fieldName + "：" + "\"" + newMap.get(logDetailsKey) + "\"");
                        }
                    }
                }
            }

            if (operationLog.isSaveHeaderData() && !("userLogin".equalsIgnoreCase(methodName) || "login".equalsIgnoreCase(operatorTypeKey))) {
                headerParam = headerParamMap.toString();
            } else {
                headerParam = null;
            }

            if (operationLog.isSaveReqData()) {
                reqParam = reqParamMap.toString();
            }

            if (!operationLog.isSaveRespData()) {
                jsonResult = null;
            }

            String operaContentStr = operaContent.toString();
            if (StringUtil.isNotEmpty(operaContentStr)) {
                String requestURI = request.getRequestURI();
                String _contextPath = "";
                if (null != request.getServletContext()) {
                    _contextPath = request.getServletContext().getContextPath();
                }
                if (StringUtils.isNotEmpty(_contextPath)) {
                    requestURI = requestURI.replaceFirst(_contextPath, "");
                }

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("loginName", loginName);
                jsonObject.put("userId", userId);
                jsonObject.put("logType", operationLog.logType().getKey());
                jsonObject.put("reqPath", requestURI);
                jsonObject.put("reqMethod", joinPoint.getSignature().getDeclaringTypeName() + "." + methodName);
                jsonObject.put("headerParam", headerParam);
                jsonObject.put("reqParam", reqParam);
                jsonObject.put("jsonResult", jsonResult);
                jsonObject.put("errorMsg", errorMsg);
                jsonObject.put("moduleName", operationLog.moduleName());
                jsonObject.put("operaContent", operaContentStr);

                String ip = WebUtil.getIpAddr(request);
                String ipAddress = "本地IP";
                if (StringUtil.isNotEmpty(ip) && !("127.0.0.1".equalsIgnoreCase(ip) || "2.0.0.1".equalsIgnoreCase(ip) || "0:0:0:0:0:0:0:1".equalsIgnoreCase(ip))) {
                    AddressVO addressVO = AddressUtils.getAddressGeoIp(ip, geodatebase2018);
                    if (ObjectUtil.isEmpty(addressVO)) {
                        addressVO = AddressUtils.getAddressGeoIp(ip, geodatebase);
                    }
                    if (ObjectUtil.isNotEmpty(addressVO)) {
                        assert addressVO != null;
                        ipAddress = addressVO.getNation();
                        String province = addressVO.getProvince();
                        if (StringUtil.isNotEmpty(province)) {
                            ipAddress += province;
                        }
                        String city = addressVO.getCity();
                        if (StringUtil.isNotEmpty(city)) {
                            ipAddress += city;
                        }
                    }
                }
                jsonObject.put("ip", ip);
                jsonObject.put("ipAddress", ipAddress);

                String operaEqpm = DeviceUtil.getDeviceType(request);
                jsonObject.put("operaEqpm", operaEqpm);

                if (ObjectUtil.isNotEmpty(jsonObject)) {

                    String sysCode = headerParamMap.getOrDefault("sysCode", "");
                    if (StringUtils.isEmpty(sysCode)) {
                        sysCode = headerParamMap.getOrDefault("syscode", "");
                    }
                    if ("undefined".equalsIgnoreCase(sysCode)) {
                        sysCode = "";
                    }

                    String nodeCode = headerParamMap.getOrDefault("nodeCode", "");
                    if (StringUtils.isEmpty(nodeCode)) {
                        nodeCode = headerParamMap.getOrDefault("nodecode", "");
                    }
                    if ("undefined".equalsIgnoreCase(nodeCode)) {
                        nodeCode = "";
                    }

                    log.info("sysCode: {}", sysCode);
                    log.info("nodeCode: {}", nodeCode);

                    JSONObject baseUserJson = this.operaLogService.getUser(request, userId);
                    log.info("baseUserJson: {}", JSON.toJSONString(baseUserJson));
                    this.operaLogService.insertLog(jsonObject, baseUserJson, sysCode, nodeCode);
                }
            }
        } catch (Exception ex) {
            log.error("<====== AOP保存日志异常：{} ======>", ex);
        }
    }

}