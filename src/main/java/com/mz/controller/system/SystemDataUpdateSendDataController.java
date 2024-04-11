package com.mz.controller.system;

import com.mz.framework.annotation.NeedLogin;
import com.mz.model.system.SystemDataUpdateSendData;
import com.mz.service.system.SystemDataUpdateSendDataService;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.mz.common.util.Result;
import com.aliyuncs.utils.StringUtils;
import com.mz.common.util.ResponseCode;
import com.mz.common.ConstantsUtil;

/**
 * 版本更新记录下发数据表(SystemDataUpdateSendData)表控制层
 *
 * @author makejava
 * @since 2024-04-11 17:08:12
 */
@RestController
@RequestMapping("systemDataUpdateSendData")
@Slf4j
public class SystemDataUpdateSendDataController {
    /**
     * 服务对象
     */
    @Autowired
    private SystemDataUpdateSendDataService systemDataUpdateSendDataService;

     /**
     * @return 对象列表
     * */
    @NeedLogin
    @PostMapping("insert")
    public Result insert(SystemDataUpdateSendData pojo, @RequestHeader(value = "loginID") String loginID) {
        if (StringUtils.isEmpty(loginID)) {
            return Result.failed("loginID不能为空");
        }
        try {
            return Result.success(this.systemDataUpdateSendDataService.insert(pojo,loginID));
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
            return Result.success(this.systemDataUpdateSendDataService.getById(id));
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
            SystemDataUpdateSendData pojo = new SystemDataUpdateSendData();
            pojo.setId(id);
            pojo.setState(state);
            return Result.success(this.systemDataUpdateSendDataService.updateById(pojo));
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
            SystemDataUpdateSendData pojo = new SystemDataUpdateSendData();
            pojo.setId(id);
            pojo.setDelState(ConstantsUtil.IS_DEL);
            return Result.success(this.systemDataUpdateSendDataService.updateById(pojo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }
}
