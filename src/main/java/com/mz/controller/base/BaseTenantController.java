package com.mz.controller.base;

import cn.hutool.core.util.ObjectUtil;
import com.mz.common.ConstantsCacheUtil;
import com.mz.framework.util.redis.RedisUtil;
import com.mz.model.base.BaseTenant;
import com.mz.model.base.vo.BaseTenantVO;
import com.mz.service.base.BaseTenantService;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.mz.framework.annotation.NeedLogin;
import com.mz.common.util.Result;
import com.aliyuncs.utils.StringUtils;
import com.mz.common.util.ResponseCode;
import com.mz.model.base.BaseUser;
import com.alibaba.fastjson.JSONObject;
import com.mz.common.ConstantsUtil;

/**
 * 租户表(BaseTenant)表控制层
 *
 * @author makejava
 * @since 2024-07-18 13:48:04
 */
@RestController
@RequestMapping("baseTenant")
@Slf4j
public class BaseTenantController {
    /**
     * 服务对象
     */
    @Autowired
    private BaseTenantService baseTenantService;

    /**
     * @return 对象列表
     */
    @NeedLogin
    @PostMapping("insert")
    public Result insert(BaseTenant pojo, @RequestHeader(value = "loginID") String loginID) {
        if (StringUtils.isEmpty(loginID)) {
            return Result.failed("loginID不能为空");
        }
        if (StringUtils.isEmpty(pojo.getAreaCode())) {
            return Result.failed("区域不能为空");
        }
        if (ObjectUtil.isEmpty(pojo.getTenantName())) {
            return Result.failed("租户名称不能为空");
        }
        if (ObjectUtil.isEmpty(pojo.getPeriodStart())) {
            return Result.failed("有限期开始时间不能为空");
        }
        if (ObjectUtil.isEmpty(pojo.getPeriodEnd())) {
            return Result.failed("有限期结束时间不能为空");
        }
        if (ObjectUtil.isEmpty(pojo.getPlatformName())) {
            return Result.failed("平台名称不能为空");
        }
        if (ObjectUtil.isEmpty(pojo.getIsOpenWatermark())) {
            return Result.failed("是否开启水印不能为空");
        }
        if (ObjectUtil.isEmpty(pojo.getPasswordType())) {
            return Result.failed("密码类型不能为空");
        }
        if (pojo.getPasswordType().intValue() == 1 && ObjectUtil.isEmpty(pojo.getPasswordDefault())) {
            return Result.failed("初始密码值不能为空");
        }
        if (ObjectUtil.isEmpty(pojo.getAccreditMainNum())) {
            return Result.failed("授权主体数不能为空");
        }
        try {
            return Result.success(this.baseTenantService.insert(pojo, loginID));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }

    /**
     * 根据主键查询
     *
     * @return 对象列表
     */
    @NeedLogin
    @GetMapping("queryById")
    public Result queryById(Long id) {
        if (null == id) {
            return Result.failed("ID必填");
        }
        try {
            return Result.success(this.baseTenantService.getById(id));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }

    /**
     * 分页列表
     */
    @NeedLogin
    @GetMapping("queryAllByLimit")
    public Result queryAllByLimit(BaseTenantVO vo) {
        try {
            if (ObjectUtil.isEmpty(vo.getServiceNodeId())) {
                return Result.failed("服务节点ID不能为空");
            }
            return Result.success(this.baseTenantService.queryAllByLimit(vo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }

    /**
     * 删除
     *
     * @return 对象列表
     */
    @NeedLogin
    @PostMapping("delPojo")
    public Result delPojo(Long id) {
        if (id == null) {
            return Result.failed("id不能为空");
        }
        try {
            BaseTenant pojo = new BaseTenant();
            pojo.setId(id);
            pojo.setUseState(2);
            return Result.success(this.baseTenantService.updateById(pojo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }
}
