package com.mz.controller.base;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aliyuncs.utils.StringUtils;
import com.mz.common.util.ResponseCode;
import com.mz.common.util.Result;
import com.mz.common.util.StringFormatUtil;
import com.mz.framework.annotation.NeedLogin;
import com.mz.model.base.BaseSiteInformation;
import com.mz.model.base.SysDataDict;
import com.mz.model.base.vo.BaseSiteInformationVO;
import com.mz.service.base.BaseUserService;
import com.mz.service.base.SysDataDictService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 系统数据字典(SysDataDict)表控制层
 *
 * @author makejava
 * @since 2021-03-17 10:58:51
 */
@Slf4j
@RestController
@RequestMapping(value = {"datacenter/sysDataDict", "sysDataDict"})
public class SysDataDictController {
    /**
     * 用户服务对象
     */
    @Resource
    private BaseUserService baseUserService;
    /**
     * 服务对象
     */
    @Resource
    private SysDataDictService sysDataDictService;


//    /**
//     * 新增数据
//     */
//    @SneakyThrows
//    @PostMapping("insert")
//    public Result insert(String id, String dictTypeCode, String dictTypeName, String dictCode, String dictName, String areaCode, String remarks, Integer sort, String dictPic, String token, HttpServletRequest request) {
//        if (baseUserService.checkLogin(token, request)) {
//            if (ObjectUtil.isEmpty(dictTypeCode)) {
//                return Result.failed("类型代码不能为空");
//            }
//            if (ObjectUtil.isEmpty(dictTypeName)) {
//                return Result.failed("类型名称不能为空");
//            }
//            SysDataDict sysDataDict = new SysDataDict();
//            if (!StringUtils.isEmpty(id)) {
//                sysDataDict.setId(id);
//            } else {
//                sysDataDict.setUseState(1);
//                sysDataDict.setId(null);
//            }
//            sysDataDict.setDictTypeCode(dictTypeCode);
//            sysDataDict.setDictTypeName(dictTypeName);
//            sysDataDict.setDictCode(dictCode);
//            sysDataDict.setDictName(dictName);
//            sysDataDict.setAreaCode(areaCode);
//            sysDataDict.setRemarks(remarks);
//            sysDataDict.setSort(sort);
//            sysDataDict.setDictPic(dictPic);
//            return sysDataDictService.insert(sysDataDict, request);
//        } else {
//            return Result.failedLogin();
//        }
//    }

    @NeedLogin
    @PostMapping("insert")
    public Result insert(SysDataDict pojo, @RequestHeader(value = "loginID") String loginID,HttpServletRequest request) {
        if (com.aliyuncs.utils.StringUtils.isEmpty(loginID)) {
            return Result.failed("loginID不能为空");
        }
        if (ObjectUtil.isEmpty(pojo.getDictTypeCode())) {
            return Result.failed("类型代码不能为空");
        }
        if (ObjectUtil.isEmpty(pojo.getDictTypeName())) {
            return Result.failed("类型名称不能为空");
        }
        try {
            return this.sysDataDictService.insert(pojo,request);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @SneakyThrows
    @PostMapping("queryById")
    public Result queryById(String id, String token, HttpServletRequest request) {
        if (baseUserService.checkLogin(token, request)) {
            return Result.success(sysDataDictService.queryById(id));
        } else {
            return Result.failedLogin();
        }
    }

    /**
     * 根据类型和上级代码查询
     *
     * @param dictTypeCode 类型代码
     * @return 对象列表
     */
    @SneakyThrows
    @PostMapping("querySubByMajor")
    public Result querySubByMajor(String dictTypeCode, String dictCode, String token, HttpServletRequest request) {
        if (baseUserService.checkLogin(token, request)) {
            return Result.success(sysDataDictService.querySubByMajor(dictTypeCode, dictCode));
        } else {
            return Result.failedLogin();
        }
    }

    /**
     * 根据类型和上级代码查询(公共)
     *
     * @param dictTypeCode 类型代码
     * @return 对象列表
     */
    @SneakyThrows
    @PostMapping("querySubByMajorPub")
    public Result querySubByMajorPub(String dictTypeCode, String dictCode) {
        return Result.success(sysDataDictService.querySubByMajor(dictTypeCode, dictCode));
    }

    /**
     * 通过代码查询单条数据
     *
     * @param dictTypeCode 类型代码
     * @param dictCode     字典代码
     * @return 实例对象
     */
    @SneakyThrows
    @PostMapping("queryByCode")
    public Result queryByCode(String dictTypeCode, String dictCode, String token, HttpServletRequest request) {
        if (baseUserService.checkLogin(token, request)) {
            return Result.success(sysDataDictService.queryByCode(dictTypeCode, dictCode));
        } else {
            return Result.failedLogin();
        }
    }

    /**
     * 通过代码查询单条数据
     *
     * @param dictTypeCode 类型代码
     * @param dictCode     字典代码
     * @return 实例对象
     */
    @SneakyThrows
    @PostMapping("queryByCodeOpen")
    public Result queryByCodeOpen(String dictTypeCode, String dictCode) {
        return Result.success(sysDataDictService.queryByCode(dictTypeCode, dictCode));
    }

    /**
     * 通过代码查询单条数据
     *
     * @param dictTypeCode 类型代码
     * @return 实例对象
     */
    @SneakyThrows
    @PostMapping("queryAllOpen")
    public Result queryAllOpen(String dictTypeCode) {
        return Result.success(sysDataDictService.queryAll(null, dictTypeCode, null, null));
    }

    /**
     * 通过代码查询单条数据
     *
     * @param dictTypeCode 类型代码
     * @return 实例对象
     */
    @SneakyThrows
    @PostMapping("queryAllOpen2")
    public List<SysDataDict> queryAllOpen2(String dictTypeCode) {
        List<SysDataDict> list = sysDataDictService.queryAll(null, dictTypeCode, null, null);
        return list;
    }

    /**
     * 根据类型查询多条数据
     *
     * @param dictTypeCode 类型代码
     * @param dictTypeName 类型名称（完全匹配，需要任意匹配需要另传参数）
     * @return 对象列表
     */
//    @SneakyThrows
//    @PostMapping("queryAll")
//    public Result queryAll(String areaCode, String dictTypeCode, String dictTypeName, String dictName, String token, HttpServletRequest request) {
//        if (baseUserService.checkLogin(token, request)) {
//            return Result.success();
//        } else {
//            return Result.failedLogin();
//        }
//    }

    /**
     * 根据类型查询多条数据
     *
     * @param dictTypeCode 类型代码
     * @param dictTypeName 类型名称（完全匹配，需要任意匹配需要另传参数）
     * @return 对象列表
     */
    @NeedLogin
    @GetMapping("queryAll")
    public Result queryAll(String areaCode, String dictTypeCode, String dictTypeName, String dictName) {
        try {
            return Result.success(sysDataDictService.queryAll(areaCode, dictTypeCode, dictTypeName, dictName));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }

    /**
     * 根据类型查询多条数据(公开)
     *
     * @param dictTypeCode 类型代码
     * @param dictTypeName 类型名称（完全匹配，需要任意匹配需要另传参数）
     * @return 对象列表
     */
    @SneakyThrows
    @PostMapping("queryAllForOpen")
    public Result queryAllForOpen(String areaCode, String dictTypeCode, String dictTypeName, String dictName) {
        return Result.success(sysDataDictService.queryAll(areaCode, dictTypeCode, dictTypeName, dictName));
    }

    /**
     * 查询数据字典大类带小类
     *
     * @return 对象列表
     */
    @SneakyThrows
    @PostMapping("queryAllWithSub")
    public Result queryAllWithSub(String dictTypeCodeMajor, String dictTypeCodeSub, String token, HttpServletRequest request) {
        if (baseUserService.checkLogin(token, request)) {
            return Result.success(sysDataDictService.queryAllWithSub(dictTypeCodeMajor, dictTypeCodeSub));
        } else {
            return Result.failedLogin();
        }
    }

    /**
     * 查询数据字典大类带小类
     *
     * @return 对象列表
     */
    @PostMapping("queryAllWithSubPub")
    public Result queryAllWithSubPub(String dictTypeCodeMajor, String dictTypeCodeSub) {
        return Result.success(sysDataDictService.queryAllWithSub(dictTypeCodeMajor, dictTypeCodeSub));
    }

    /**
     * 查询多条数据page
     *
     * @param pageNo   查询起始位置
     * @param pageSize 查询条数
     * @return 对象列表
     */
    @SneakyThrows
    @PostMapping("queryAllByLimit")
    public Result queryAllByLimit(Integer pageNo, Integer pageSize, String areaCode, String dictTypeCode, String dictTypeName, String dictName, Integer useState, String startTime, String endTime, String token, HttpServletRequest request) {
        if (baseUserService.checkLogin(token, request)) {
            pageNo = pageNo != null ? pageNo : StringFormatUtil.PAGE_NO_DEFAULT;
            pageSize = pageSize != null ? pageSize : StringFormatUtil.PAGE_SIZE_DEFAULT;
            return Result.success(sysDataDictService.queryAllByLimit(pageNo, pageSize, areaCode, dictTypeCode, dictTypeName, dictName, useState, startTime, endTime));
        } else {
            return Result.failedLogin();
        }
    }

    /**
     * 变更可用状态
     *
     * @return 对象列表
     */
    @NeedLogin
    @PostMapping("updateState")
    public Result updateState(String id, Integer useState) {
        if (ObjectUtil.isEmpty(id)) {
            return Result.failed("ID不能为空");
        }
        if (ObjectUtil.isEmpty(useState)) {
            return Result.failed("状态不能为空");
        }
        try {
            SysDataDict sysDataDict = new SysDataDict();
            sysDataDict.setId(id);
            sysDataDict.setUseState(useState);
            sysDataDict.setModifyTime(DateUtil.now());
            this.sysDataDictService.update(sysDataDict);
            return Result.success();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }
}