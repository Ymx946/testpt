package com.mz.controller.base;

import com.aliyuncs.utils.StringUtils;
import com.mz.common.ConstantsUtil;
import com.mz.common.util.ResponseCode;
import com.mz.common.util.Result;
import com.mz.framework.annotation.NeedLogin;
import com.mz.model.base.TabBasicColumn;
import com.mz.model.base.vo.TabBasicColumnVO;
import com.mz.service.base.TabBasicColumnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 基础栏目表(TabBasicColumn)表控制层
 *
 * @author makejava
 * @since 2022-12-28 09:53:01
 */
@Slf4j
@RestController
@RequestMapping(value = {"server/tabBasicColumn", "tabBasicColumn"})
public class TabBasicColumnController {
    /**
     * 服务对象
     */
    @Autowired
    private TabBasicColumnService tabBasicColumnService;

    /**
     * @return 对象列表
     */
    @NeedLogin
    @PostMapping("insert")
    public Result insert(TabBasicColumn pojo, @RequestHeader(value = "loginID") String loginID) {
        if (StringUtils.isEmpty(loginID)) {
            return Result.failed("loginID不能为空");
        }
        if (StringUtils.isEmpty(pojo.getColumnName())) {
            return Result.failed("栏目名称必填");
        }
        if (pojo.getColumnType() == null || pojo.getColumnType() == 0) {
            return Result.failed("栏目类型必填");
        }
        if (pojo.getPageType() == null || pojo.getPageType() == 0) {
            return Result.failed("页面类型必填");
        }
        try {
            return this.tabBasicColumnService.insert(pojo, loginID);
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
    public Result queryAllByLimit(TabBasicColumnVO vo, @RequestHeader(value = "loginID") String loginID) {
        try {
            if (org.springframework.util.StringUtils.isEmpty(loginID)) {
                return Result.failed("loginID不能为空");
            }
            vo.setLoginID(loginID);
            return Result.success(tabBasicColumnService.queryAllByLimit(vo));
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
            return Result.success(this.tabBasicColumnService.getById(id));
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
            TabBasicColumn pojo = new TabBasicColumn();
            pojo.setId(id);
            pojo.setDelState(ConstantsUtil.IS_DEL);
            return Result.success(this.tabBasicColumnService.updateById(pojo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }
}
