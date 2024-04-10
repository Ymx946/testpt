package com.mz.controller.base;

import com.aliyuncs.utils.StringUtils;
import com.mz.common.ConstantsUtil;
import com.mz.common.util.ResponseCode;
import com.mz.common.util.Result;
import com.mz.common.util.wxaes.HttpKit;
import com.mz.framework.annotation.NeedLogin;
import com.mz.model.base.TabActivitiFlowInfo;
import com.mz.model.base.vo.TabActivitiFlowInfoVO;
import com.mz.service.base.TabActivitiFlowInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * 流程控制表(ActFlowInfo)表控制层
 *
 * @author makejava
 * @since 2023-02-06 18:27:50
 */
@RestController
@RequestMapping(value = {"server/actFlowInfo", "actFlowInfo"})
@Slf4j
public class TabActivitiFlowInfoController {
    /**
     * 服务对象
     */
    @Autowired
    private TabActivitiFlowInfoService tabActivitiFlowInfoService;
    @Value("${spring.profiles.active}")
    private String profile;

    /**
     * @return 对象列表
     */
    @NeedLogin
    @PostMapping("insert")
    public Result insert(TabActivitiFlowInfo pojo, @RequestHeader(value = "loginID") String loginID) {
        if (StringUtils.isEmpty(loginID)) {
            return Result.failed("loginID不能为空");
        }
        try {
            return Result.success(this.tabActivitiFlowInfoService.insert(pojo, loginID));
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
            return Result.success(this.tabActivitiFlowInfoService.getById(id));
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
    public Result queryAllByLimit(TabActivitiFlowInfoVO vo, @RequestHeader(value = "loginID") String loginID) {
        try {
            if (org.springframework.util.StringUtils.isEmpty(loginID)) {
                return Result.failed("loginID不能为空");
            }
            vo.setLoginID(loginID);
            return Result.success(tabActivitiFlowInfoService.queryAllByLimit(vo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }

    /**
     * 列表展示
     */
    @NeedLogin
    @GetMapping("queryAll")
    public Result queryAll(TabActivitiFlowInfoVO vo, @RequestHeader(value = "loginID") String loginID) {
        try {
            if (org.springframework.util.StringUtils.isEmpty(loginID)) {
                return Result.failed("loginID不能为空");
            }
            vo.setLoginID(loginID);
            return Result.success(tabActivitiFlowInfoService.queryAll(vo));
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
            TabActivitiFlowInfo pojo = new TabActivitiFlowInfo();
            pojo.setId(id);//
            pojo.setState(state);
            this.tabActivitiFlowInfoService.updateById(pojo);
            return Result.success();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }

    /**
     * 部署流程
     *
     * @param
     * @return 0-部署失败  1- 部署成功  2- 已经部署过
     */
    @NeedLogin
    @PostMapping("deployment")
    public Result deployment(String id) {
        TabActivitiFlowInfo flowInfo = tabActivitiFlowInfoService.getById(id);
        if (flowInfo.getState().intValue() == 1) {
            flowInfo.setState(0);
            tabActivitiFlowInfoService.updateById(flowInfo);
//            if ("test".equalsIgnoreCase(profile)) {//本地测试无法获取授权码.返回固定值
//                result = "{ \"code\": 200,\"data\": {\"accessToken\": \"cf7b880d03cd7d2c17286a0ac7a6f720c3a4190a4e4341e7e4d9d94566f042a0e18f5d5fea2815fbc9a277db17dca6e7dd1544bfd7505c50\", \"expire\": 3600},\"message\": \"成功\",\"throwable\": null, \"type\": \"success\"}";
//            } else {
//                result = HttpKit.postHead(url + "/open/api/auth/getToken", jsonObject.toString());//获取token
//            }
//            actFlowCommService.saveNewDeploy(flowInfo);
        }
        if (flowInfo.getState().intValue() == 0) {
            return Result.success(2);
        }
        return Result.success(1);
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
            TabActivitiFlowInfo pojo = new TabActivitiFlowInfo();
            pojo.setId(id);
            pojo.setDelState(ConstantsUtil.IS_DEL);
            return Result.success( this.tabActivitiFlowInfoService.updateById(pojo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }
}
