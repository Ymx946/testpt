package com.mz.controller.base;

import cn.hutool.core.util.ObjectUtil;
import com.mz.model.base.BaseSiteInformation;
import com.mz.model.base.vo.BaseSiteInformationVO;
import com.mz.service.base.BaseSiteInformationService;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.mz.framework.annotation.NeedLogin;
import com.mz.common.util.Result;
import com.aliyuncs.utils.StringUtils;
import com.mz.common.util.ResponseCode;
import com.mz.common.ConstantsUtil;

/**
 * 站点信息表(BaseSiteInformation)表控制层
 *
 * @author makejava
 * @since 2024-12-23 14:06:59
 */
@RestController
@RequestMapping("baseSiteInformation")
@Slf4j
public class BaseSiteInformationController {
    /**
     * 服务对象
     */
    @Autowired
    private BaseSiteInformationService baseSiteInformationService;

     /**
     * @return 对象列表
     * */
    @NeedLogin
    @PostMapping("insert")
    public Result insert(BaseSiteInformation pojo, @RequestHeader(value = "loginID") String loginID) {
        if (StringUtils.isEmpty(loginID)) {
            return Result.failed("loginID不能为空");
        }
        if (StringUtils.isEmpty(pojo.getAreaCode())) {
            return Result.failed("位置不能为空");
        }
        if (ObjectUtil.isEmpty(pojo.getSiteCode())) {
            return Result.failed("站点编码不能为空");
        }
        if (ObjectUtil.isEmpty(pojo.getSiteName())) {
            return Result.failed("站点名称不能为空");
        }
        if (ObjectUtil.isEmpty(pojo.getVoltageLevelCode())) {
            return Result.failed("电压等级不能为空");
        }
        if (ObjectUtil.isEmpty(pojo.getLocalLat()) || ObjectUtil.isEmpty(pojo.getLocalLng())) {
            return Result.failed("经纬度不能为空");
        }
        if (ObjectUtil.isEmpty(pojo.getAddress())) {
            return Result.failed("详细地址不能为空");
        }
        if (ObjectUtil.isEmpty(pojo.getUnitId())) {
            return Result.failed("所属单位不能为空");
        }
        if (ObjectUtil.isEmpty(pojo.getOpsId())) {
            return Result.failed("运维单位不能为空");
        }
        try {
            return Result.success(this.baseSiteInformationService.insert(pojo,loginID));
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
            return Result.success(this.baseSiteInformationService.getById(id));
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
    public Result queryAllByLimit(BaseSiteInformationVO vo, @RequestHeader(value = "loginID") String loginID) {
        if (StringUtils.isEmpty(loginID)) {
            return Result.failed("loginID不能为空");
        }
        try {
            return Result.success(this.baseSiteInformationService.queryAllByLimit(vo));
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
            BaseSiteInformation pojo = new BaseSiteInformation();
            pojo.setId(id);
            pojo.setState(state);
            return Result.success(this.baseSiteInformationService.updateById(pojo));
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
            BaseSiteInformation pojo = new BaseSiteInformation();
            pojo.setId(id);
            pojo.setDelState(ConstantsUtil.IS_DEL);
            return Result.success( this.baseSiteInformationService.updateById(pojo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }
}
