package com.mz.controller.system;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.aliyuncs.utils.StringUtils;
import com.github.pagehelper.util.StringUtil;
import com.mz.common.ConstantsUtil;
import com.mz.common.util.ResponseCode;
import com.mz.common.util.Result;
import com.mz.framework.annotation.NeedLogin;
import com.mz.framework.websocket.entity.EnumMessageType;
import com.mz.framework.websocket.entity.Message;
import com.mz.model.system.SystemDataServiceNode;
import com.mz.model.system.SystemDataUpdateRecord;
import com.mz.model.system.vo.SystemDataServiceNodeVO;
import com.mz.model.system.vo.SystemDataUpdateRecordVO;
import com.mz.service.system.SystemDataServiceNodeService;
import com.mz.service.system.SystemDataUpdateRecordService;
import com.mz.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 服务节点(SystemDataServiceNode)表控制层
 *
 * @author makejava
 * @since 2023-12-29 10:01:11
 */
@Slf4j
@RestController
@RequestMapping(value = {"datacenter/systemDataServiceNode", "systemDataServiceNode"})
public class SystemDataServiceNodeController {
    @Autowired
    private SystemDataServiceNodeService systemDataServiceNodeService;
    @Autowired
    private SystemDataUpdateRecordService systemDataUpdateRecordService;
    @Resource
    private WebSocketServer webSocketServer;

    /**
     * @return 对象列表
     */
    @NeedLogin
    @Transactional
    @PostMapping("insert")
    public Result insert(SystemDataServiceNode pojo, @RequestHeader(value = "loginID") String loginID) {
        if (StringUtils.isEmpty(loginID)) {
            return Result.failed("loginID不能为空");
        }
        if (StringUtils.isEmpty(pojo.getNodeName())) {
            return Result.failed("名称不能为空");
        }
        if (StringUtils.isEmpty(pojo.getNodeUrl())) {
            return Result.failed("路径不能为空");
        }
        try {
            this.systemDataServiceNodeService.insert(pojo, loginID);
            return Result.success();
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
            return Result.success(this.systemDataServiceNodeService.getById(id));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }

    /**
     * 列表page
     *
     * @return 对象列表
     */
    @NeedLogin
    @GetMapping("queryAllByLimit")
    public Result queryAllByLimit(SystemDataServiceNodeVO vo) {
        try {
            return Result.success(this.systemDataServiceNodeService.queryAllByLimit(vo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }

    /**
     * 列表page
     *
     * @return 对象列表
     */
    @NeedLogin
    @GetMapping("queryAll")
    public Result queryAll(SystemDataServiceNodeVO vo) {
        try {
            return Result.success(this.systemDataServiceNodeService.queryAll(vo));
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
    @Transactional
    @PostMapping("changeState")
    public Result changeState(Long id, Integer state, @RequestHeader(value = "loginID") String loginID) {
        if (id == null) {
            return Result.failed("id不能为空");
        }
        if (state == null) {
            return Result.failed("状态不能为空");
        }
        try {
            SystemDataServiceNode pojo = new SystemDataServiceNode();
            pojo.setId(id);
            pojo.setState(state);
            this.systemDataServiceNodeService.updateById(pojo);

            // 发布websocket 通知
            String type = "", msg = "", content = "";
            SystemDataServiceNode systemDataServiceNode = this.systemDataServiceNodeService.getById(id);

            SystemDataUpdateRecordVO systemDataUpdateRecordVO = new SystemDataUpdateRecordVO();
            systemDataUpdateRecordVO.setNodeId(id);
            List<SystemDataUpdateRecord> systemDataUpdateRecordList = this.systemDataUpdateRecordService.queryAll(systemDataUpdateRecordVO);
            if (CollectionUtil.isNotEmpty(systemDataUpdateRecordList)) {
                SystemDataUpdateRecord systemDataUpdateRecord = systemDataUpdateRecordList.stream().findFirst().get();
                msg = systemDataUpdateRecord.getUpdateTime();
                content = systemDataUpdateRecord.getUpdateContent();
            }

            Message message = new Message().setDate(new Date());
            int existsState = systemDataServiceNode.getState().intValue();
            if (ConstantsUtil.STATE_NORMAL == existsState) {
                type = EnumMessageType.SYSTEM_UPGRADE_SUCCESS.getCode();
                msg = StringUtil.isNotEmpty(msg) ? msg : EnumMessageType.SYSTEM_UPGRADE_SUCCESS.getName();
                content = StringUtil.isNotEmpty(content) ? content : msg;
            }
            if (ConstantsUtil.STATE_UN_NORMAL == existsState) {
                type = EnumMessageType.SYSTEM_UPGRADE_ONGOING.getCode();
                msg = StringUtil.isNotEmpty(msg) ? msg : EnumMessageType.SYSTEM_UPGRADE_ONGOING.getName();
                content = StringUtil.isNotEmpty(content) ? content : msg;
            }

            message.setType(type)
                    .setMsg(msg)
                    .setContent(content);
            msg = JSON.toJSONString(message);
            log.info("msg：" + msg);
            String remarks = systemDataServiceNode.getRemarks();
            log.info("remarks：" + remarks);
            webSocketServer.sendMessageAll(remarks, msg);
            return Result.success();
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
    @Transactional
    @PostMapping("delPojo")
    public Result delPojo(Long id) {
        if (id == null) {
            return Result.failed("id不能为空");
        }
        try {
            SystemDataUpdateRecordVO recordVO = new SystemDataUpdateRecordVO();
            recordVO.setNodeId(id);
            List<SystemDataUpdateRecord> recordList = systemDataUpdateRecordService.queryAll(recordVO);
            if (CollectionUtil.isNotEmpty(recordList)) {
                return Result.failed("有版本记录，不能删除");
            }
            SystemDataServiceNode pojo = new SystemDataServiceNode();
            pojo.setId(id);
            pojo.setDelState(ConstantsUtil.IS_DEL);
            this.systemDataServiceNodeService.updateById(pojo);
            return Result.success();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg());
        }
    }
}
