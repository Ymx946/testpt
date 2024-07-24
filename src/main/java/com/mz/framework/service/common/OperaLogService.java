package com.mz.framework.service.common;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

public interface OperaLogService {

    JSONObject getUser(HttpServletRequest request,
                       @RequestParam(value = "userId", required = false) String userId);

    void saveOperaLog(@RequestParam(value = "baseUserObj", required = false) JSONObject baseUserObj,
                      @RequestParam(value = "logType", required = false, defaultValue = "2") Integer logType,
                      @RequestParam(value = "moduleName", required = false, defaultValue = "数字管理后台") String moduleName,
                      @RequestParam(value = "operaContent", required = false) String operaContent,
                      @RequestParam(value = "sysCode", required = false) String sysCode,
                      @RequestParam(value = "nodeCode", required = false) String nodeCode,
                      HttpServletRequest request);

    /**
     * 新增数据(业务接口)
     *
     * @return 实例对象
     */
    void insertLog(@RequestParam(value = "jsonObject", required = false) JSONObject jsonObject,
                   @RequestParam(value = "baseUserObj", required = false) JSONObject baseUserObj,
                   @RequestParam(value = "sysCode", required = false) String sysCode,
                   @RequestParam(value = "nodeCode", required = false) String nodeCode);
}
