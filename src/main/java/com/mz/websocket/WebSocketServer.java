package com.mz.websocket;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mz.common.ConstantsUtil;
import com.mz.common.util.UUIDGenerator;
import com.mz.framework.websocket.MyEndpointConfigure;
import com.mz.framework.websocket.entity.EnumMessageType;
import com.mz.framework.websocket.entity.Message;
import com.mz.model.system.model.SystemDataServiceNodeModel;
import com.mz.model.system.vo.SystemDataServiceNodeVO;
import com.mz.service.system.SystemDataServiceNodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * websocket 异步通信
 */
@Slf4j
@Component
@ServerEndpoint(value = "/websocket/{userId}", configurator = MyEndpointConfigure.class)
public class WebSocketServer {
    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static final AtomicInteger onlineCount = new AtomicInteger(0);
    /**
     * 用于记录连接的客户端 (sid,session)
     */
    public static Map<String, Session> clients = new ConcurrentHashMap<>();
    /**
     * 基于userId关联sid(用于解决同一用户id存在多个web端连接的问题)
     */
    public static Map<String, Set<String>> conn = new ConcurrentHashMap<>();
    /**
     * 根据系统编码存储的用户连接信息
     */
    public static Map<String, Map<String, Set<String>>> sysCodeMap = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 接收userId
     */
    private String userId;
    /**
     * sid
     */
    private String sid;

    @Resource
    private SystemDataServiceNodeService systemDataServiceNodeService;

    public static synchronized int getOnlineCount() {
        return onlineCount.get() > 0 ? onlineCount.get() : 0;
    }

    public static synchronized void addOnlineCount() {
        onlineCount.incrementAndGet(); // 在线数加1
    }

    /**
     * 在线数减1
     */
    public static synchronized void subOnlineCount() {
        onlineCount.decrementAndGet();
    }

    // 给所有当前连接的用户发送消息
    public void sendMessageAll(String sysCode, Object message) throws IOException {
        if (StringUtils.isEmpty(sysCode)) {
            log.info("系统编码【" + sysCode + "】不能为空");
            return;
        }

        if (!sysCodeMap.containsKey(sysCode)) {
            log.info("系统编码【" + sysCode + "】不存在");
            return;
        }

        Map<String, Set<String>> connSetMap = sysCodeMap.get(sysCode);
        log.info("connSetMap：" + connSetMap + "，clients：" + clients);

        for (Set<String> clientSet : connSetMap.values()) {
            Iterator<String> iterator = clientSet.iterator();
            while (iterator.hasNext()) {
                String sid = iterator.next();
                if (StringUtils.isNotEmpty(sid)) {
                    Session session = clients.get(sid);
                    if (session != null) {
                        log.info("sessionId：" + session.getId());
                        synchronized (session) {
                            if (session.isOpen()) {
                                try {
                                    if (message instanceof String) {
                                        session.getBasicRemote().sendText((String) message);
                                    } else if (message instanceof ByteBuffer) {
                                        session.getBasicRemote().sendBinary((ByteBuffer) message);
                                    }
                                } catch (Exception e) {
                                    log.error("[WebSocket] 给用户:" + sid + "，发送消息异常，原因：" + e);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 根据用户ID发送给某一个用户
     */
    public void sendMessageByUserId(String userId, Object message) throws IOException {
        if (userId != null) {
//            log.info("[WebSocket] 给用户[" + userId + "]发送消息，报文:" + message);
            Set<String> clientSet = conn.get(userId);
            if (ObjectUtil.isNotEmpty(clientSet)) {
                Iterator<String> iterator = clientSet.iterator();
                while (iterator.hasNext()) {
                    String sid = iterator.next();
                    if (StringUtils.isNotEmpty(sid)) {
                        Session session = clients.get(sid);
                        if (session != null) {
                            log.info("sessionId：" + session.getId());
                            if (session.isOpen()) {
                                try {
                                    if (message instanceof String) {
                                        session.getBasicRemote().sendText((String) message);// 同步发送
                                    } else if (message instanceof ByteBuffer) {
                                        session.getBasicRemote().sendBinary((ByteBuffer) message);
                                    }
                                } catch (Exception e) {
                                    log.error("[WebSocket] 给用户:" + userId + "，发送消息异常，原因：" + e);
                                    onOpen(session, userId);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) throws IOException {
        if (StringUtils.isEmpty(userId)) {
            log.error("userId不能为空");
            return;
        }
        if (userId.equalsIgnoreCase("undefined")) {
            log.error("userId参数不正确");
            return;
        }

        if (userId.indexOf("@") < 0) {
            log.error("userId参数格式不正确，必须包含系统编码，通过@分割");
            return;
        }

        this.userId = userId;
        this.session = session;

        String sysCode = userId.split("@")[0];
        this.sid = UUIDGenerator.generate();
        clients.put(this.sid, session);

        Set<String> clientSet = conn.get(userId);
        if (CollectionUtil.isEmpty(clientSet)) {
            clientSet = new HashSet<>();
            conn = new ConcurrentHashMap<>();
            conn.put(userId, clientSet);
        }
        clientSet.add(this.sid);

        Map<String, Set<String>> sysCodeSetMap = sysCodeMap.get(sysCode);
        if (CollectionUtil.isEmpty(sysCodeSetMap)) {
            sysCodeSetMap = new HashMap<>();
            sysCodeMap.put(sysCode, sysCodeSetMap);
        }
        sysCodeSetMap.putAll(conn);

        // 在线数加1
        addOnlineCount();

//        log.info("[WebSocket] 用户连接:" + userId + "，当前在线人数为:" + getOnlineCount() + "，sessionId====：" + session.getId() + "，sysCodeMap====：" + sysCodeMap);

        if (session.isOpen()) {
            session.getBasicRemote().sendText(JSON.toJSONString(new Message()
                    .setType(EnumMessageType.WEBSOCKET_CONNECT_SUCCESS.getCode())
                    .setMsg(EnumMessageType.WEBSOCKET_CONNECT_SUCCESS.getName())
                    .setContent(EnumMessageType.WEBSOCKET_CONNECT_SUCCESS.getName())
                    .setDate(new Date())));
        }

        // websocket链接成功之后，就立刻推送服务节点的状态
        SystemDataServiceNodeModel systemDataServiceNodeModel = systemDataServiceNodeService.getSystemDataServiceNodeCurMsg(new SystemDataServiceNodeVO()
                .setRemarks(sysCode)
                .setState(ConstantsUtil.STATE_UN_NORMAL));
        if (ObjectUtil.isNotEmpty(systemDataServiceNodeModel)) {
            int existsState = systemDataServiceNodeModel.getState().intValue();
            String msg = systemDataServiceNodeModel.getMsg();
            String content = systemDataServiceNodeModel.getContent();
            String type = "";
            if (ConstantsUtil.STATE_NORMAL == existsState) {
                type = EnumMessageType.SYSTEM_UPGRADE_SUCCESS.getCode();
                msg = StringUtils.isNotEmpty(msg) ? msg : EnumMessageType.SYSTEM_UPGRADE_SUCCESS.getName();
                content = StringUtils.isNotEmpty(content) ? content : msg;
            }

            if (ConstantsUtil.STATE_UN_NORMAL == existsState) {
                type = EnumMessageType.SYSTEM_UPGRADE_ONGOING.getCode();
                msg = StringUtils.isNotEmpty(msg) ? msg : EnumMessageType.SYSTEM_UPGRADE_ONGOING.getName();
                content = StringUtils.isNotEmpty(content) ? content : msg;
            }

            if (session.isOpen()) {
                session.getBasicRemote().sendText(JSON.toJSONString(new Message()
                        .setType(type)
                        .setMsg(msg)
                        .setContent(content)
                        .setDate(new Date())));
            }
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("userId") String userId) throws IOException {
        if (message.indexOf("ping") <= -1) {
            log.info("[WebSocket] 收到来自客户端：" + userId + "的消息：" + message);
        }
        // 可以群发消息
        // 消息保存到数据库、redis
        if (StringUtils.isEmpty(message)) {
            return;
        }

        if (session.isOpen()) {
            String type = "", msg = "", content = "";
            if ("ping".equalsIgnoreCase(message)) {
                type = EnumMessageType.WEBSOCKET_RESPONSE_PONG.getCode();
                msg = EnumMessageType.WEBSOCKET_RESPONSE_PONG.getName();
                content = msg;
            } else {
                // 解析发送的报文
                type = EnumMessageType.SYSTEM_UPGRADE_SUCCESS.getCode();
                msg = message;
                content = msg;
            }

            session.getBasicRemote().sendText(JSON.toJSONString(new Message()
                    .setType(type)
                    .setMsg(msg)
                    .setContent(content)
                    .setDate(new Date())));
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("userId") String userId) {
        log.info("[WebSocket] 用户[" + userId + ": " + this.sid + "]断开连接");
        clients.remove(this.sid);

        // 在线数减1
        subOnlineCount();

        log.info("[WebSocket] 用户[" + userId + ": " + this.sid + "]退出，当前在线人数为:" + getOnlineCount());
    }

    /**
     * 用户异常调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error, @PathParam("userId") String userId) throws IOException {
//        this.onClose();
        log.error("[WebSocket] 用户[" + userId + "]错误，原因:" + error);
//        error.printStackTrace();
//        onOpen(session, userId);
    }


}
