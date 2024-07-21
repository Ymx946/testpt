package com.mz.service.base.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.model.BaseMainBody;
import com.mz.common.util.AddressUtils;
import com.mz.common.util.AddressVO;
import com.mz.common.util.IdWorker;
import com.mz.common.util.WebUtil;
import com.mz.framework.service.common.OperaLogService;
import com.mz.framework.util.redis.RedisUtil;
import com.mz.framework.web.util.DeviceUtil;
import com.mz.mapper.localhost.SysNodeMapper;
import com.mz.mapper.localhost.SysOperaLogMapper;
import com.mz.model.base.BaseUser;
import com.mz.model.base.SysNode;
import com.mz.model.base.SysOperaLog;
import com.mz.model.base.SysOperaStatistics;
import com.mz.model.base.vo.SysOperaStatisticsVO;
import com.mz.service.base.BaseUserService;
import com.mz.service.base.SysOperaStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service("operaLogService")
public class OperaLogServiceImpl implements OperaLogService {
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private SysOperaLogMapper sysOperaLogMapper;
    @Resource
    private BaseUserService baseUserService;
    @Resource
    private SysOperaStatisticsService sysOperaStatisticsService;
    @Resource
    private SysNodeMapper sysNodeMapper;
    @Value("${geoLite.geodatebase}")
    private String geodatebase;
    @Value("${geoLite.geodatebase2018}")
    private String geodatebase2018;

    @Override
    public JSONObject getUser(HttpServletRequest request, String loginId) {
        JSONObject baseUserJson = null;
        if (StringUtils.isEmpty(loginId)) {
            loginId = request.getHeader("loginID");// 请求头有登录ID，则根据ID去库中获取用户信息
        }
        if (!StringUtils.isEmpty(loginId)) {
            String baseUserStr = redisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginId);// 获取PC的baseUser4
            if (StringUtils.isEmpty(baseUserStr)) {// 没有则获取APP的baseUser
                baseUserStr = redisUtil.get(ConstantsCacheUtil.LOGIN_USER_INFO_APP + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginId);
            }
            if (!StringUtils.isEmpty(baseUserStr)) {
                baseUserJson = JSONObject.parseObject(baseUserStr);
            }
            if (baseUserJson == null) {
                BaseUser baseUser = this.baseUserService.queryById(loginId);
                baseUserJson = (JSONObject) JSON.toJSON(baseUser);
                redisUtil.setEx(ConstantsCacheUtil.LOGIN_USER_INFO + ConstantsCacheUtil.REDIS_DEFAULT_DELIMITER + loginId, JSON.toJSONString(baseUserJson), 300, TimeUnit.MINUTES);
            }
        }
        return baseUserJson;
    }

    @Async
    @Override
    public void saveOperaLog(JSONObject baseUserObj, @DefaultValue("2") Integer logType, @DefaultValue("数字管理后台") String moduleName, String operaContent, String sysCode, String nodeCode, HttpServletRequest request) {
        BaseUser baseUser = baseUserObj.to(BaseUser.class);
        if (ObjectUtil.isNotEmpty(baseUser)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("loginName", baseUser.getLoginName());
            jsonObject.put("userId", baseUser.getId());
            jsonObject.put("logType", logType);
            jsonObject.put("reqPath", request.getRequestURI());
            jsonObject.put("moduleName", moduleName);
            jsonObject.put("operaContent", operaContent);

            try {
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

                this.insertLog(jsonObject, baseUserObj, sysCode, nodeCode);
            } catch (Exception ex) {
                log.error("AOP保存日志异常：{}", ex.getMessage());
            }
        }
    }

    @Override
    public void insertLog(JSONObject jsonObject, JSONObject baseUserObj, String sysCode, String nodeCode) {
        SysOperaLog sysOperaLog = jsonObject.to(SysOperaLog.class);
        if (ObjectUtil.isNotEmpty(sysOperaLog)) {
            long newId = new IdWorker(0L, 0L).nextId();
            sysOperaLog.setId(String.valueOf(newId));

            if (ObjectUtil.isNotEmpty(baseUserObj)) {
                BaseUser baseUser = baseUserObj.to(BaseUser.class);
                if (ObjectUtil.isNotEmpty(baseUser)) {
                    String tenantId = baseUser.getTenantId();
                    if (StringUtil.isNotEmpty(tenantId)) {
                        sysOperaLog.setTenantId(tenantId);
                    }
                    sysOperaLog.setUserId(baseUser.getId());
                    sysOperaLog.setLoginName(baseUser.getLoginName());
                    sysOperaLog.setRealName(baseUser.getRealName());
                }
            }

            if (StringUtil.isNotEmpty(sysCode) && StringUtil.isNotEmpty(nodeCode)) {
                SysNode sysNode = sysNodeMapper.queryByCode(sysCode, nodeCode);
                if (ObjectUtil.isNotEmpty(sysNode)) {
                    String nodeLevelName = sysNode.getNodeLevelName();
                    if (StringUtil.isNotEmpty(nodeLevelName)) {
                        sysOperaLog.setModuleName(nodeLevelName);
                    }
                }
            }
            Integer logType = sysOperaLog.getLogType();
            if (ObjectUtil.isEmpty(sysOperaLog)) {
                logType = SysOperaLog.LOG_TYPE_BUS;
            }
            sysOperaLog.setLogType(logType);
            sysOperaLog.setOperaTime(DateUtil.now());

            // 保存日志统计信息
            SysOperaStatistics sysOperaStatistics = this.saveSysOperaStatistics(sysOperaLog);
            sysOperaLog.setOperaStatisticsId(sysOperaStatistics.getId());

            // 保存操作日志信息
            this.sysOperaLogMapper.insert(sysOperaLog);
        }
    }

    /**
     * 保存日志统计信息
     *
     * @param sysOperaLog
     */
    private SysOperaStatistics saveSysOperaStatistics(SysOperaLog sysOperaLog) {
        SysOperaStatistics sysOperaStatistics = new SysOperaStatistics();
        String tenantId = sysOperaLog.getTenantId();
        if (StringUtil.isNotEmpty(tenantId)) {
            sysOperaStatistics.setTenantId(Long.parseLong(tenantId));
        }
        sysOperaStatistics.setAreaCode(sysOperaLog.getAreaCode());
        sysOperaStatistics.setAreaName(sysOperaLog.getAreaName());
        String mainBodyId = sysOperaLog.getMainBodyId();
        if (StringUtil.isNotEmpty(mainBodyId)) {
            sysOperaStatistics.setMainBodyId(Long.parseLong(mainBodyId));
        }
        sysOperaStatistics.setMainBodyName(sysOperaLog.getMainBodyName());
        String userId = sysOperaLog.getUserId();
        if (StringUtil.isNotEmpty(userId)) {
            sysOperaStatistics.setUserId(Long.parseLong(userId));
        }

        Integer loginNum = 0, busOperaNum = 0;

        SysOperaStatisticsVO sysOperaStatisticsVO = new SysOperaStatisticsVO();
        sysOperaStatisticsVO.setTenantId(sysOperaStatistics.getTenantId());
        sysOperaStatisticsVO.setAreaCode(sysOperaStatistics.getAreaCode());
        sysOperaStatisticsVO.setUserId(sysOperaStatistics.getUserId());
        sysOperaStatisticsVO.setMainBodyId(sysOperaStatistics.getMainBodyId());
        List<SysOperaStatistics> sysOperaStatisticsList = this.sysOperaStatisticsService.queryAll(sysOperaStatisticsVO);
        if (CollectionUtil.isNotEmpty(sysOperaStatisticsList)) {
            SysOperaStatistics oldSysOperaStatistics = sysOperaStatisticsList.stream().findFirst().get();
            sysOperaStatistics.setId(oldSysOperaStatistics.getId());
            loginNum = oldSysOperaStatistics.getLoginNum();
            busOperaNum = oldSysOperaStatistics.getBusOperaNum();
        }

        Integer logType = sysOperaLog.getLogType();
        if (SysOperaLog.LOG_TYPE_LOGIN == logType) {
            sysOperaStatistics.setLoginNum(++loginNum);
        } else {
            sysOperaStatistics.setBusOperaNum(++busOperaNum);
        }

        sysOperaStatistics.setLoginName(sysOperaLog.getLoginName());
        sysOperaStatistics.setRealName(sysOperaLog.getRealName());
        sysOperaStatistics.setIp(sysOperaLog.getIp());
        sysOperaStatistics.setIpAddress(sysOperaLog.getIpAddress());
        sysOperaStatistics.setOperaEqpm(sysOperaLog.getOperaEqpm());
        sysOperaStatistics.setOperaTime(DateUtil.now());
        this.sysOperaStatisticsService.insert(sysOperaStatistics);
        return sysOperaStatistics;
    }
}
