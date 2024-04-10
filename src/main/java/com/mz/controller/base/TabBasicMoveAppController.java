package com.mz.controller.base;

import com.aliyuncs.utils.StringUtils;
import com.mz.common.ConstantsUtil;
import com.mz.common.util.ResponseCode;
import com.mz.common.util.Result;
import com.mz.framework.annotation.NeedLogin;
import com.mz.model.base.TabBasicMoveApp;
import com.mz.model.base.vo.TabBasicMoveAppVO;
import com.mz.service.base.TabBasicMoveAppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 基础移动应用表(TabBasicMoveApp)表控制层
 *
 * @author makejava
 * @since 2022-12-29 09:23:01
 */
@Slf4j
@RestController
@RequestMapping(value = {"server/tabBasicMoveApp", "tabBasicMoveApp"})
public class TabBasicMoveAppController {
    /**
     * 服务对象
     */
    @Autowired
    private TabBasicMoveAppService tabBasicMoveAppService;

    /**
     * @return 对象列表
     */
    @NeedLogin
    @PostMapping("insert")
    public Result insert(TabBasicMoveApp pojo, @RequestHeader(value = "loginID") String loginID) {
        if (StringUtils.isEmpty(loginID)) {
            return Result.failed("loginID不能为空");
        }
        try {
            return Result.success(this.tabBasicMoveAppService.insert(pojo, loginID));
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
    public Result queryAllByLimit(TabBasicMoveAppVO vo, @RequestHeader(value = "loginID") String loginID) {
        try {
            if (org.springframework.util.StringUtils.isEmpty(loginID)) {
                return Result.failed("loginID不能为空");
            }
            vo.setLoginID(loginID);
            return Result.success(tabBasicMoveAppService.queryAllByLimit(vo));
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
            return Result.success(this.tabBasicMoveAppService.getById(id));
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
            TabBasicMoveApp pojo = new TabBasicMoveApp();
            pojo.setId(id);
            pojo.setState(state);
            return Result.success(this.tabBasicMoveAppService.updateById(pojo));
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
            TabBasicMoveApp pojo = new TabBasicMoveApp();
            pojo.setId(id);
            pojo.setDelState(ConstantsUtil.IS_DEL);
            return Result.success(this.tabBasicMoveAppService.updateById(pojo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }
}
