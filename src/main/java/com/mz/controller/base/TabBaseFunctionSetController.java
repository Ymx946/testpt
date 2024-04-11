package com.mz.controller.base;

import cn.hutool.core.util.ObjectUtil;
import com.aliyuncs.utils.StringUtils;
import com.mz.common.ConstantsUtil;
import com.mz.common.util.ResponseCode;
import com.mz.common.util.Result;
import com.mz.framework.annotation.NeedLogin;
import com.mz.model.base.TabBaseFunctionSet;
import com.mz.model.base.vo.TabBaseFunctionSetVO;
import com.mz.service.base.TabBaseFunctionSetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 功能设置基础表(TabBaseFunctionSet)表控制层
 *
 * @author makejava
 * @since 2022-12-12 11:32:52
 */
@RestController
@RequestMapping(value = {"server/tabBaseFunctionSet", "tabBaseFunctionSet"})
@Slf4j
public class TabBaseFunctionSetController {
    /**
     * 服务对象
     */
    @Autowired
    private TabBaseFunctionSetService tabBaseFunctionSetService;

    /**
     * @return 对象列表
     */
    @NeedLogin
    @PostMapping("insert")
    public Result insert(TabBaseFunctionSet pojo, @RequestHeader(value = "loginID") String loginID) {
        if (StringUtils.isEmpty(loginID)) {
            return Result.failed("loginID不能为空");
        }
        if (ObjectUtil.isEmpty(pojo.getAccreditType())) {
            return Result.failed("授权类型不能为空");
        }
        try {
            return this.tabBaseFunctionSetService.insert(pojo, loginID);
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
            return Result.success(this.tabBaseFunctionSetService.getById(id));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }

    /**
     * 变更状态
     *
     * @return 对象列表
     */
    @NeedLogin
    @PostMapping("changeState")
    public Result changeState(Long id, Integer state) {
        if (id == null) {
            return Result.failed("id不能为空");
        }
        if (state == null) {
            return Result.failed("状态不能为空");
        }
        try {
            TabBaseFunctionSet pojo = new TabBaseFunctionSet();
            pojo.setId(id);
            pojo.setState(state);
            return Result.success(this.tabBaseFunctionSetService.updateById(pojo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }

    /**
     * 列表展示
     */
    @NeedLogin
    @GetMapping("queryAllByLimit")
    public Result queryAllByLimit(TabBaseFunctionSetVO vo, @RequestHeader(value = "loginID") String loginID) {
        try {
            if (ObjectUtil.isEmpty(loginID)) {
                return Result.failed("loginID不能为空");
            }
            if (ObjectUtil.isEmpty(vo.getAccreditType())) {
                return Result.failed("授权类型不能为空");
            }
            vo.setLoginID(loginID);
            return Result.success(tabBaseFunctionSetService.queryAllByLimit(vo));
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
            TabBaseFunctionSet pojo = new TabBaseFunctionSet();
            pojo.setId(id);
            pojo.setDelState(ConstantsUtil.IS_DEL);
            return Result.success(this.tabBaseFunctionSetService.updateById(pojo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }
}
