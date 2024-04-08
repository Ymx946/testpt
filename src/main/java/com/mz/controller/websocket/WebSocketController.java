package com.mz.controller.websocket;

import com.github.pagehelper.util.StringUtil;
import com.mz.common.util.Result;
import com.mz.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping(value = {"datacenter/websocketq", "websocketq"}, method = {RequestMethod.POST, RequestMethod.GET})
public class WebSocketController {
    @Resource
    private WebSocketServer webSocketServer;

    @ResponseBody
    @RequestMapping("/sendToUser")
    public Result send(@RequestParam(value = "userId") String userId, @RequestParam(value = "msg") String msg) {
        if (StringUtil.isEmpty(msg)) {
            return Result.failed("消息不能为空");
        }
        try {
            webSocketServer.sendMessageAll("", msg);
        } catch (IOException e) {
            log.error("websocket发送信息失败，原因：", e);
            return Result.failed("websocket发送信息失败");
        }
        return Result.success();
    }

}