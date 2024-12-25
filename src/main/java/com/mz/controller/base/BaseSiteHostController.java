package com.mz.controller.base;

import cn.hutool.core.util.ObjectUtil;
import com.mz.framework.util.redis.RedisUtil;
import com.mz.model.base.BaseSiteHost;
import com.mz.model.base.vo.BaseSiteHostVO;
import com.mz.service.base.BaseSiteHostService;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.mz.framework.annotation.NeedLogin;
import com.mz.common.util.Result;
import com.aliyuncs.utils.StringUtils;
import com.mz.common.util.ResponseCode;
import com.mz.common.ConstantsUtil;

/**
 * 站点主机信息表(BaseSiteHost)表控制层
 *
 * @author makejava
 * @since 2024-12-25 08:59:06
 */
@RestController
@RequestMapping("baseSiteHost")
@Slf4j
public class BaseSiteHostController {
    /**
     * 服务对象
     */
    @Autowired
    private BaseSiteHostService baseSiteHostService;
    @Autowired
    private RedisUtil redisUtil;

     /**
     * @return 对象列表
     * */
    @NeedLogin
    @PostMapping("insert")
    public Result insert(BaseSiteHost pojo, @RequestHeader(value = "loginID") String loginID) {
        if (StringUtils.isEmpty(loginID)) {
            return Result.failed("loginID不能为空");
        }
        if (ObjectUtil.isEmpty(pojo.getDeviceTypeCode())) {
            return Result.failed("设备类型不能为空");
        }
        if (ObjectUtil.isEmpty(pojo.getSiteId())) {
            return Result.failed("所属站点不能为空");
        }
        if (ObjectUtil.isEmpty(pojo.getIpAddress())) {
            return Result.failed("IP地址不能为空");
        }
        if (ObjectUtil.isEmpty(pojo.getIpGateway())) {
            return Result.failed("IP网关不能为空");
        }
        if (ObjectUtil.isEmpty(pojo.getIpSubnetMask())) {
            return Result.failed("IP子网掩码不能为空");
        }
        if (ObjectUtil.isEmpty(pojo.getIpPort())) {
            return Result.failed("IP端口不能为空");
        }
        try {
            return Result.success(this.baseSiteHostService.insert(pojo,loginID));
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
    public Result queryById(Long id ) {
        if (null == id) {
            return Result.failed("ID必填");
        }
        try {
            return Result.success(this.baseSiteHostService.getById(id));
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
    public Result queryAllByLimit(BaseSiteHostVO vo, @RequestHeader(value = "loginID") String loginID) {
        if (StringUtils.isEmpty(loginID)) {
            return Result.failed("loginID不能为空");
        }
        try {
            return Result.success(this.baseSiteHostService.queryAllByLimit(vo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }
    /**
     * 变更状态
     * @return 对象列表
     * */
    @NeedLogin
    @PostMapping("changeState")
    public Result changeState(Long id,Integer state) {
        if (id==null) {
            return Result.failed("id不能为空");
        }
        if (state==null) {
            return Result.failed("状态不能为空");
        }
        try {
            BaseSiteHost pojo = new BaseSiteHost();
            pojo.setId(id);
            pojo.setState(state);
            return Result.success(this.baseSiteHostService.updateById(pojo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }
    /**
     * 删除
     * @return 对象列表
     * */
    @NeedLogin
    @PostMapping("delPojo")
    public Result delPojo(Long id) {
        if (id==null) {
            return Result.failed("id不能为空");
        }
        try {
            BaseSiteHost pojo = new BaseSiteHost();
            pojo.setId(id);
            pojo.setDelState(ConstantsUtil.IS_DEL);
            return Result.success( this.baseSiteHostService.updateById(pojo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }
}
