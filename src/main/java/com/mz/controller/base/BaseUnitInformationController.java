package com.mz.controller.base;

import com.mz.model.base.BaseUnitInformation;
import com.mz.model.base.vo.BaseUnitInformationVO;
import com.mz.service.base.BaseUnitInformationService;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.mz.framework.annotation.NeedLogin;
import com.mz.common.util.Result;
import com.aliyuncs.utils.StringUtils;
import com.mz.common.util.ResponseCode;
import com.mz.common.ConstantsUtil;

/**
 * 单位信息表(BaseUnitInformation)表控制层
 *
 * @author makejava
 * @since 2024-12-23 10:17:52
 */
@RestController
@RequestMapping("baseUnitInformation")
@Slf4j
public class BaseUnitInformationController {
    /**
     * 服务对象
     */
    @Autowired
    private BaseUnitInformationService baseUnitInformationService;

     /**
     * @return 对象列表
     * */
    @NeedLogin
    @PostMapping("insert")
    public Result insert(BaseUnitInformation pojo, @RequestHeader(value = "loginID") String loginID) {
        if (StringUtils.isEmpty(loginID)) {
            return Result.failed("loginID不能为空");
        }
        if (StringUtils.isEmpty(pojo.getUnitName())) {
            return Result.failed("单位名称不能为空");
        }
        try {
            return this.baseUnitInformationService.insert(pojo,loginID);
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
            return Result.success(this.baseUnitInformationService.getById(id));
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
    public Result queryAllByLimit(BaseUnitInformationVO vo, @RequestHeader(value = "loginID") String loginID) {
        if (StringUtils.isEmpty(loginID)) {
            return Result.failed("loginID不能为空");
        }
        try {
            return Result.success(this.baseUnitInformationService.queryAllByLimit(vo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }

    /**
     * 单位选择列表
     */
    @NeedLogin
    @GetMapping("queryTreeList")
    public Result queryTreeList(BaseUnitInformationVO vo, @RequestHeader(value = "loginID") String loginID) {
        if (StringUtils.isEmpty(loginID)) {
            return Result.failed("loginID不能为空");
        }
        try {
            return Result.success(this.baseUnitInformationService.queryTreeList(vo));
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
            BaseUnitInformation pojo = new BaseUnitInformation();
            pojo.setId(id);
            pojo.setState(state);
            return Result.success(this.baseUnitInformationService.updateById(pojo));
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
            BaseUnitInformation pojo = new BaseUnitInformation();
            pojo.setId(id);
            pojo.setDelState(ConstantsUtil.IS_DEL);
            return Result.success( this.baseUnitInformationService.updateById(pojo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }
}
