package com.mz.controller.base;

import cn.hutool.core.util.ObjectUtil;
import com.mz.framework.annotation.NeedLogin;
import com.mz.model.base.SysDataDictClassify;
import com.mz.service.base.SysDataDictClassifyService;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.mz.common.util.Result;
import com.aliyuncs.utils.StringUtils;
import com.mz.common.util.ResponseCode;
import com.mz.common.ConstantsUtil;

/**
 * 系统数据字典分类(SysDataDictClassify)表控制层
 *
 * @author makejava
 * @since 2024-04-08 18:08:54
 */
@RestController
@RequestMapping("sysDataDictClassify")
@Slf4j
public class SysDataDictClassifyController {
    /**
     * 服务对象
     */
    @Autowired
    private SysDataDictClassifyService sysDataDictClassifyService;

     /**
     * @return 对象列表
     * */
    @NeedLogin
    @PostMapping("insert")
    public Result insert(SysDataDictClassify pojo, @RequestHeader(value = "loginID") String loginID) {
        if (StringUtils.isEmpty(loginID)) {
            return Result.failed("loginID不能为空");
        }
        if (ObjectUtil.isEmpty(pojo.getDictTypeCode())) {
            return Result.failed("类型代码不能为空");
        }
        if (ObjectUtil.isEmpty(pojo.getDictTypeName())) {
            return Result.failed("类型名称不能为空");
        }
        try {
            return this.sysDataDictClassifyService.insert(pojo,loginID);
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
            return Result.success(this.sysDataDictClassifyService.getById(id));
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
            SysDataDictClassify pojo = new SysDataDictClassify();
            pojo.setId(id);
            pojo.setDelState(ConstantsUtil.IS_DEL);
            return Result.success(this.sysDataDictClassifyService.updateById(pojo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }
}
