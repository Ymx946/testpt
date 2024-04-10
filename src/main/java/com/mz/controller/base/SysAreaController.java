package com.mz.controller.base;

import cn.hutool.core.util.ObjectUtil;
import com.aliyuncs.utils.StringUtils;
import com.mz.common.util.ResponseCode;
import com.mz.common.util.Result;
import com.mz.framework.annotation.NeedLogin;
import com.mz.model.base.SysArea;
import com.mz.model.base.vo.SysAreaLimitVO;
import com.mz.service.base.SysAreaService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 区域表(SysArea)表控制层
 *
 * @author makejava
 * @since 2021-03-17 10:58:50
 */
@RestController
@RequestMapping(value = {"datacenter/sysArea", "sysArea"})
@Slf4j
public class SysAreaController {
    /**
     * 服务对象
     */
    @Resource
    private SysAreaService sysAreaService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public SysArea selectOne(String id) {
        return this.sysAreaService.queryById(id);
    }


    /**
     * 根据上级查询
     *
     * @param paraCode 上级代码
     * @param len      代码长度 省2位，市4位，区县6位，乡镇9位，村12位
     * @return 实例对象
     */
    @SneakyThrows
    @PostMapping("queryAreaList")
    public Result queryAreaList(String paraCode, Integer len) {
        if (len == null) {
            if (paraCode.length() == 2) {//
                len = 4;
            } else if (paraCode.length() == 4) {
                len = 6;
            } else if (paraCode.length() == 6) {
                len = 9;
            } else if (paraCode.length() == 9) {
                len = 12;
            }
        }
        return Result.success(sysAreaService.queryAreaList(paraCode, len));
    }

    /**
     * 根据上级查询（有村的下级）
     *
     * @param paraCode 上级代码
     * @param len      代码长度 省2位，市4位，区县6位，乡镇9位，村12位
     * @return 实例对象
     */
    @SneakyThrows
    @PostMapping("queryAreaListForVillage")
    public Result queryAreaListForVillage(String paraCode, Integer len) {
        return Result.success(sysAreaService.queryAreaListForVillage(paraCode, len));
    }


    /**
     * 查询区域(所有层级--有村的上级)
     *
     * @return 实例对象
     */
    @SneakyThrows
    @PostMapping("queryAllByCodeForVillage")
    public Result queryAllByCodeForVillage(String areaCode) {
        return Result.success(sysAreaService.queryAllByCodeForVillage(areaCode));
    }

    /**
     * 查询区域(所有层级)
     *
     * @return 实例对象
     */
    @SneakyThrows
    @PostMapping("queryAreaListAndSon")
    public Result queryAreaListAndSon(String areaCode, Integer len) {
        len = len != null ? len : 12;
        return Result.success(sysAreaService.queryAllByCode(areaCode, len));
    }


    /**
     * 查询区域分层机（新接口优化，前端不修改，则接口直接调用一样新接口）
     *
     * @return 实例对象
     */
    @SneakyThrows
    @PostMapping("queryListSelfAndSon")
    public Result queryListSelfAndSon(String areaCode, Integer len) {
        len = len != null ? len : 12;
        return Result.success(sysAreaService.queryAllByCode(areaCode, len));
    }

    /**
     * 根据区域代码获取全区域名称
     *
     * @return 实例对象
     */
    @SneakyThrows
    @PostMapping("getFullAreaNameByAreaCode")
    public Result getFullAreaNameByAreaCode(String areaCode) {
        return Result.success(sysAreaService.getFullAreaNameByAreaCode(areaCode));
    }

    /**
     * 根据名称查询代码
     *
     * @return 实例对象
     */
    @SneakyThrows
    @PostMapping("queryByParaAndName")
    public Result queryByParaAndName(String paraCode, String areaName, Integer areaLevel) {
        return Result.success(sysAreaService.queryByParaAndName(paraCode, areaName, areaLevel));
    }

    /**
     * 根据名称查询代码
     *
     * @return 实例对象
     */
    @SneakyThrows
    @GetMapping("queryByCode")
    public Result queryByCode(String areaCode) {
        SysArea sysArea = sysAreaService.queryByCode(areaCode);
        return Result.success(sysArea);
    }

    /**
     * 根据名称查询代码
     *
     * @return 实例对象
     */
    @RequestMapping(value = "queryByAreaCode", method = {RequestMethod.POST, RequestMethod.GET})
    public Result queryByAreaCode(String areaCode) {
        return Result.success(sysAreaService.queryByCode(areaCode));
    }

    /**
     * 查询区域(所有层级)
     *
     * @return 实例对象
     */
    @SneakyThrows
    @NeedLogin
    @PostMapping("queryAreaListAndSonLogin")
    public Result queryAreaListAndSonLogin(String areaCode, Integer len) {
        len = len != null ? len : 12;
        return Result.success(sysAreaService.queryAllByCode(areaCode, len));
    }

    /**
     * @return 对象列表
     */
    @NeedLogin
    @PostMapping("insert")
    public Result insert(SysArea pojo, @RequestHeader(value = "loginID") String loginID) {
        if (StringUtils.isEmpty(loginID)) {
            return Result.failed("loginID不能为空");
        }
        if (StringUtils.isEmpty(pojo.getAreaCode())) {
            return Result.failed("区域代码不能为空");
        }
        if (StringUtils.isEmpty(pojo.getAreaName())) {
            return Result.failed("区域名称不能为空");
        }
        if (StringUtils.isEmpty(pojo.getCityClassifyCode())) {
            return Result.failed("城市分类代码不能为空");
        }
        if (ObjectUtil.isEmpty(pojo.getSort())) {
            return Result.failed("排序不能为空");
        }
        try {
            return this.sysAreaService.insert(pojo, loginID);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }

    /**
     * 列表查询
     *
     * @return 对象列表
     */
    @NeedLogin
    @GetMapping("queryAllByLimit")
    public Result queryAllByLimit(SysAreaLimitVO vo, @RequestHeader(value = "loginID") String loginID) {
        try {
            if (StringUtils.isEmpty(loginID)) {
                return Result.failed("loginID不能为空");
            }
            return Result.success(this.sysAreaService.queryAllByLimitNew(vo));
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
            return Result.success(this.sysAreaService.getById(id));
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
            SysArea pojo = new SysArea();
            pojo.setId(String.valueOf(id));
            pojo.setState(state);
            return Result.success(this.sysAreaService.updateById(pojo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }

}