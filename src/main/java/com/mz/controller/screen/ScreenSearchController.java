package com.mz.controller.screen;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.utils.StringUtils;
import com.mz.common.ConstantsCacheUtil;
import com.mz.common.util.ResponseCode;
import com.mz.common.util.Result;
import com.mz.framework.annotation.NeedLogin;
import com.mz.framework.util.redis.RedisUtil;
import com.mz.model.base.BaseUser;
import com.mz.model.screen.vo.TabScreenSearchVO;
import com.mz.service.screen.ScreenSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 分模块大屏查询
 *
 * @author makejava
 * @since 2022-10-17 15:18:14
 */
@Slf4j
@RestController
@RequestMapping(value = {"server/screenSearch", "screenSearch"})
public class ScreenSearchController {
    @Autowired
    private ScreenSearchService screenSearchService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分模块查询
     * 按传入的模块返回对应的数据
     *
     * @param modularNameS 模块名称串
     * @return 对象列表
     */
    @NeedLogin
    @GetMapping("queryByModular")
    public Result queryByModular(String modularNameS, String typeName, TabScreenSearchVO vo, @RequestHeader(value = "loginID") String loginID) {
        if (StringUtils.isEmpty(loginID)) {
            return Result.failed("loginID不能为空");
        }
        try {
            vo.setLoginID(loginID);
            return Result.success(screenSearchService.queryByModular(modularNameS, typeName, vo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }

}
