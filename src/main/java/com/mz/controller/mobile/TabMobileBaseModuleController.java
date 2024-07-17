package com.mz.controller.mobile;

import cn.hutool.core.util.ObjectUtil;
import com.aliyuncs.utils.StringUtils;
import com.mz.common.ConstantsUtil;
import com.mz.common.util.ResponseCode;
import com.mz.common.util.Result;
import com.mz.framework.annotation.NeedLogin;
import com.mz.model.mobile.TabMobileBaseModule;
import com.mz.model.mobile.vo.TabMobileBaseModuleVO;
import com.mz.service.mobile.TabMobileBaseModuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 移动组件表(TabMobileBaseModule)表控制层
 *
 * @author makejava
 * @since 2022-11-21 17:00:56
 */
@RestController
@RequestMapping(value = {"server/tabMobileBaseModule", "tabMobileBaseModule"})
@Slf4j
public class TabMobileBaseModuleController {
    /**
     * 服务对象
     */
    @Autowired
    private TabMobileBaseModuleService tabMobileBaseModuleService;

    /**
     * @param pojo
     * @param loginID
     * @param styleJson [{id:样式ID,styleName:样式名称}]
     * @return
     */
    @NeedLogin
    @PostMapping("insert")
    public Result insert(TabMobileBaseModule pojo, @RequestHeader(value = "loginID") String loginID, String styleJson) {
        if (StringUtils.isEmpty(loginID)) {
            return Result.failed("loginID不能为空");
        }
        if(ObjectUtil.isEmpty(styleJson)){
            return Result.failed("列表样式不能为空");
        }
        try {
            return Result.success(this.tabMobileBaseModuleService.insert(pojo, loginID, styleJson));
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
            return Result.success(this.tabMobileBaseModuleService.queryById(id));
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
    @PostMapping("changeState")
    public Result changeState(Long id, Integer state) {
        if (id == null) {
            return Result.failed("id不能为空");
        }
        if (state == null) {
            return Result.failed("状态不能为空");
        }
        try {
            TabMobileBaseModule pojo = new TabMobileBaseModule();
            pojo.setId(id);//
            pojo.setState(state);
            return Result.success(this.tabMobileBaseModuleService.updateById(pojo));
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
    public Result queryAllByLimit(TabMobileBaseModuleVO vo, @RequestHeader(value = "loginID") String loginID) {
        try {
            if (org.springframework.util.StringUtils.isEmpty(loginID)) {
                return Result.failed("loginID不能为空");
            }
            vo.setLoginID(loginID);
            return Result.success(tabMobileBaseModuleService.queryAllByLimit(vo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }

    @NeedLogin
    @GetMapping("queryModule")
    public Result queryModule(TabMobileBaseModule vo) {
        return Result.success(this.tabMobileBaseModuleService.queryModule(vo));
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
            TabMobileBaseModule pojo = new TabMobileBaseModule();
            pojo.setId(id);
            pojo.setDelState(ConstantsUtil.IS_DEL);
            return Result.success(this.tabMobileBaseModuleService.updateById(pojo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }
}
