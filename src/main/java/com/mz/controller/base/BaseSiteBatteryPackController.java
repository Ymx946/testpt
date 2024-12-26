package com.mz.controller.base;

import com.mz.model.base.BaseSiteBatteryPack;
import com.mz.model.base.vo.BaseSiteBatteryPackVO;
import com.mz.service.base.BaseSiteBatteryPackService;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.mz.framework.annotation.NeedLogin;
import com.mz.common.util.Result;
import com.aliyuncs.utils.StringUtils;
import com.mz.common.util.ResponseCode;
import com.mz.common.ConstantsUtil;

/**
 * 站点电池组信息表(BaseSiteBatteryPack)表控制层
 *
 * @author makejava
 * @since 2024-12-25 11:18:33
 */
@RestController
@RequestMapping("baseSiteBatteryPack")
@Slf4j
public class BaseSiteBatteryPackController {
    /**
     * 服务对象
     */
    @Autowired
    private BaseSiteBatteryPackService baseSiteBatteryPackService;

     /**
     * 新增/编辑
     * */
    @NeedLogin
    @PostMapping("insert")
    public Result insert(BaseSiteBatteryPack pojo, @RequestHeader(value = "loginID") String loginID) {
        if (StringUtils.isEmpty(loginID)) {
            return Result.failed("loginID不能为空");
        }
        try {
            return Result.success(this.baseSiteBatteryPackService.insert(pojo,loginID));
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
            return Result.success(this.baseSiteBatteryPackService.getById(id));
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
    public Result queryAllByLimit(BaseSiteBatteryPackVO vo, @RequestHeader(value = "loginID") String loginID) {
        if (StringUtils.isEmpty(loginID)) {
            return Result.failed("loginID不能为空");
        }
        try {
            return Result.success(this.baseSiteBatteryPackService.queryAllByLimit(vo));
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
            BaseSiteBatteryPack pojo = new BaseSiteBatteryPack();
            pojo.setId(id);
            pojo.setState(state);
            return Result.success(this.baseSiteBatteryPackService.updateById(pojo));
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
            BaseSiteBatteryPack pojo = new BaseSiteBatteryPack();
            pojo.setId(id);
            pojo.setDelState(ConstantsUtil.IS_DEL);
            return Result.success( this.baseSiteBatteryPackService.updateById(pojo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }
}
