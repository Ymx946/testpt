package com.mz.controller.base;

import com.mz.common.util.Result;
import com.mz.common.util.StringFormatUtil;
import com.mz.model.base.BaseUser;
import com.mz.model.base.SysDeft;
import com.mz.service.base.BaseUserService;
import com.mz.service.base.SysDeftService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 系统定义表(SysDeft)表控制层
 *
 * @author makejava
 * @since 2021-03-17 11:00:04
 */
@RestController
@RequestMapping(value = {"datacenter/sysDeft", "sysDeft"})
@Slf4j
public class SysDeftController {
    /**
     * 用户服务对象
     */
    @Resource
    private BaseUserService baseUserService;
    /**
     * 服务对象
     */
    @Resource
    private SysDeftService sysDeftService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("queryById")
    public Result queryById(String id, String token, HttpServletRequest request) {
        if (baseUserService.checkLogin(token, request)) {
            if (StringUtils.isEmpty(id)) {
                return Result.failed("ID必填");
            }
            return Result.success(this.sysDeftService.queryById(id));
        } else {
            return Result.failedLogin();
        }
    }

    /**
     * 通过主键查询单条数据
     *
     * @return 单条数据
     */
    @PostMapping("queryAll")
    public Result queryAll(String sysCode, String sysName, Integer sysType, String remarks, String token, HttpServletRequest request) {
        if (baseUserService.checkLogin(token, request)) {
            SysDeft sysDeft = new SysDeft();
            sysDeft.setSysCode(sysCode);//（完全匹配，需要任意匹配需要另传参数）
            sysDeft.setSysName(sysName);//（完全匹配，需要任意匹配需要另传参数）
            sysDeft.setSysType(sysType);
            sysDeft.setRemarks(remarks);
            return Result.success(this.sysDeftService.queryAll(sysDeft));
        } else {
            return Result.failedLogin();
        }
    }

    /**
     * 通过租户ID查询有权限的系统列表
     *
     * @return 单条数据
     */
    @PostMapping("queryAllByTenant")
    public Result queryAllByTenant(String token, String sysName, HttpServletRequest request) {
        if (baseUserService.checkLogin(token, request)) {
            return this.sysDeftService.queryAllForTenant(sysName);
        } else {
            return Result.failedLogin();
        }
    }

    /**
     * 查询租户有权限的应用（包含分类选中状态）
     *
     * @param classifyId 分类ID
     * @return 对象列表
     */
    @PostMapping("queryAllWithClassify")
    public Result queryAllWithClassify(String classifyId, String token, HttpServletRequest request) {
        if (baseUserService.checkLogin(token, request)) {
            BaseUser baseUser = baseUserService.getUser(request);
            return Result.success(this.sysDeftService.queryAllWithClassify(baseUser.getTenantId(), classifyId));
        } else {
            return Result.failedLogin();
        }
    }


    /**
     * 新增数据
     *
     * @param sysManage 1系统设置2业务系统
     */
    @SneakyThrows
    @PostMapping("insert")
    public Result insert(String id, String sysCode, String sysName, Integer sysType, String remarks, Integer sysManage, String sysLogo,
                         String sysIntroduce, String releaseScope, String scopeName, Integer belongType, String supplierName, Integer applicationType, String useType,
                         String sysAddr, String areaLevel,
                         String token, HttpServletRequest request) {
        if (baseUserService.checkLogin(token, request)) {
            sysManage = sysManage != null ? sysManage : 2;//默认业务系统
            belongType = belongType != null ? belongType : 1;//默认自建
            applicationType = applicationType != null ? applicationType : 1;//默认应用
            if (StringUtils.isEmpty(useType)) {
                useType = "1,2";//默认值
            }
            SysDeft sysDeft = new SysDeft();
            if (!StringUtils.isEmpty(id)) {
                sysDeft.setId(id);
            } else {
                sysDeft.setId(null);
            }
            sysDeft.setSysCode(sysCode);
            sysDeft.setSysLogo(sysLogo);
            sysDeft.setSysName(sysName);
            sysDeft.setSysType(sysType);
            sysDeft.setRemarks(remarks);
            sysDeft.setSysManage(sysManage);
            sysDeft.setSysIntroduce(sysIntroduce);
            sysDeft.setReleaseScope(releaseScope);
            sysDeft.setScopeName(scopeName);
            sysDeft.setBelongType(belongType);
            sysDeft.setSupplierName(supplierName);
            sysDeft.setApplicationType(applicationType);
            sysDeft.setUseType(useType);
            sysDeft.setSysAddr(sysAddr);
            sysDeft.setAreaLevel(areaLevel);
            return sysDeftService.insert(sysDeft, request);
        } else {
            return Result.failedLogin();
        }
    }

    /**
     * 通过查询条件查询数据列表
     *
     * @param pageNo   页码
     * @param pageSize 每页条数
     * @return 数据列表
     */
    @SneakyThrows
    @PostMapping("queryAllByLimit")
    public Result queryAllByLimit(Integer pageNo, Integer pageSize, String sysName, Integer sysType, Integer belongType, String token, HttpServletRequest request) {
        if (baseUserService.checkLogin(token, request)) {
            pageNo = pageNo != null ? pageNo : StringFormatUtil.PAGE_NO_DEFAULT;
            pageSize = pageSize != null ? pageSize : StringFormatUtil.PAGE_SIZE_DEFAULT;
            return Result.success(this.sysDeftService.queryAllByLimit(pageNo, pageSize, sysName, sysType, belongType, null, null));
        } else {
            return Result.failedLogin();
        }
    }
}