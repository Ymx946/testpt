//package com.mz.framework.websocket;
//
//
//import com.alibaba.fastjson2.JSON;
//import com.alibaba.fastjson2.JSONObject;
//import com.mz.common.util.StringUtils;
//import com.mz.framework.websocket.entity.EnumMessageType;
//import com.mz.framework.websocket.entity.Message;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import javax.websocket.*;
//import javax.websocket.server.PathParam;
//import javax.websocket.server.ServerEndpoint;
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.util.Date;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.CopyOnWriteArraySet;
//
///**
// * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
// * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
// */
//@Slf4j
//@Component
//@ServerEndpoint(value = "/websocket/{userId}", configurator = MyEndpointConfigure.class)
//public class WebSocketServerBak {
//    private static final ConcurrentHashMap<String, CopyOnWriteArraySet<WebSocketServerBak>> webSocketMap = new ConcurrentHashMap<String, CopyOnWriteArraySet<WebSocketServerBak>>();
//
//    private static final ConcurrentHashMap<String, Integer> count = new ConcurrentHashMap<String, Integer>();
//
//    private String userId;
//
//
//    /*
//     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
//     */
//    private Session session;
//
//    /**
//     * 发送自定义消息
//     */
//    public static String sendInfo(@PathParam("userId") String userId, String message) throws IOException {
//        // 只推送给指定的userId, 为null则全部推送
//        log.info("[WebSocket]发送消息到:" + userId + "，报文:" + message);
//        synchronized (webSocketMap) {
//            webSocketMap.remove("undefined");
//            if (StringUtils.isNotBlank(userId)) {
//                if (StringUtils.isNotBlank(userId) && webSocketMap.containsKey(userId)) {
//                    webSocketMap.get(userId).forEach(webSocketServer -> {
//                        Session webSocketServerSession = webSocketServer.session;
//                        if (webSocketServerSession.isOpen()) {
//                            try {
//                                webSocketServer.sendMessage(message);
//                            } catch (IOException e) {
//                                log.error("[WebSocket] 用户:" + userId + ", 发送消息异常，原因：", e);
//                            }
//                        }
//                    });
//                } else {
//                    log.warn("[WebSocket] 用户" + userId + ",不在线！");
//                    return "offline";
//                }
//            } else {
//                log.info("webSocketMap====：" + webSocketMap);
//                for (Map.Entry<String, CopyOnWriteArraySet<WebSocketServerBak>> copyOnWriteArraySetEntry : webSocketMap.entrySet()) {
//                    String clientId = copyOnWriteArraySetEntry.getKey();
//                    log.info("clientId====：" + clientId);
//
//                    copyOnWriteArraySetEntry.getValue().forEach(webSocketServer -> {
//                        Session webSocketServerSession = webSocketServer.session;
//                        if (webSocketServerSession.isOpen()) {
//                            try {
//                                webSocketServer.sendMessage(message);
//                            } catch (IOException e) {
//                                log.error("[WebSocket] 用户:" + userId + ", 发送消息异常，原因：", e);
//                            }
//                        }
//                    });
//                }
//            }
//        }
//        return "success";
//    }
//
//    /**
//     * 连接建立成功调用的方法
//     *
//     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
//     */
//    @OnOpen
//    public void onOpen(Session session, @PathParam("userId") final String userId) {
//        if (userId.equalsIgnoreCase("undefined")) {
//            return;
//        }
//
//        webSocketMap.remove("undefined");
//        this.session = session;
//        this.userId = userId;
//        if (!exitUser(userId)) {
//            initUserInfo(userId);
//        } else {
//            CopyOnWriteArraySet<WebSocketServerBak> webSocketTestSet = getUserSocketSet(userId);
//            webSocketTestSet.add(this);
//            userCountIncrease(userId);
//        }
//        log.info("[WebSocket] 用户:" + userId + "新连接加入，当前在线人数为:" + getCurrUserCount(userId) + "，sessionId====：" + session.getId() + "，webSocketMap====：" + webSocketMap);
//
//        try {
//            this.sendMessage(JSON.toJSONString(new Message()
//                    .setType(EnumMessageType.WEBSOCKET_CONNECT_SUCCESS.getCode())
//                    .setMsg(EnumMessageType.WEBSOCKET_CONNECT_SUCCESS.getName())
//                    .setDate(new Date())));
//        } catch (IOException e) {
//            log.error("[WebSocket] 用户:" + userId + ",网络异常!!!!!!");
//        }
//    }
//
//    /**
//     * 连接关闭调用的方法
//     */
//    @OnClose
//    public void onClose() {
//        CopyOnWriteArraySet<WebSocketServerBak> webSocketServer = webSocketMap.get(userId);
//        //从set中删除
//        webSocketServer.remove(this);
//        //在线数减1
//        userCountDecrement(userId);
//        log.info("[WebSocket] 用户：" + userId + "退出， 当前在线人数为:" + getCurrUserCount(userId));
//    }
//
//    /**
//     * 收到客户端消息后调用的方法
//     *
//     * @param message 客户端发送过来的消息
//     * @param session 可选的参数
//     */
//    @OnMessage
//    public void onMessage(String message, Session session) {
//        // 可以群发消息
//        // 消息保存到数据库、redis
//        CopyOnWriteArraySet<WebSocketServerBak> webSocketSet = webSocketMap.get(userId);
//        System.out.println("来自客户端" + userId + "的消息:" + message);
//        log.info("[WebSocket] 收到来自客户端：" + userId + "的消息：" + message);
//        synchronized (webSocketMap) {
//            webSocketMap.remove("undefined");
//            if (StringUtils.isNotBlank(message) && !"ping".equals(message)) {
//                try {
//                    // 解析发送的报文
//                    String pattern = "^\\{.*\\}$";
//                    if (message.matches(pattern)) {
//                        JSONObject jsonObject = JSON.parseObject(message);
//                        // 追加发送人(防止串改)
//                        jsonObject.put("fromUserId", this.userId);
//                        if (jsonObject.containsKey("toUserId")) {
//                            String toUserId = jsonObject.getString("toUserId");
//                            if (session.isOpen() && StringUtils.isNotBlank(toUserId) && !toUserId.equalsIgnoreCase(userId)) {
//                                CopyOnWriteArraySet<WebSocketServerBak> toWebSocketSet = webSocketMap.get(toUserId);
//                                if (toWebSocketSet != null) {
//                                    for (WebSocketServerBak item : toWebSocketSet) {
//                                        try {
//                                            item.sendMessage(jsonObject.toJSONString());
//                                        } catch (IOException e) {
//                                            log.error(e.getMessage(), e);
//                                            continue;
//                                        }
//                                    }
//                                }
//                            } else {
//                                log.error("[WebSocket] 请求的userId:" + toUserId + "不在该服务器上");
//                                // 否则不在这个服务器上，发送到mysql或者redis
//                            }
//                        }
//                    } else {
//                        for (WebSocketServerBak item : webSocketSet) {
//                            try {
//                                item.sendMessage(JSON.toJSONString(new Message()
//                                        .setType(EnumMessageType.SYSTEM_UPGRADE_SUCCESS.getCode())
//                                        .setMsg(message)
//                                        .setDate(new Date())));
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                                continue;
//                            }
//                        }
//                    }
//                } catch (Exception e) {
//                    log.error(e.getMessage(), e);
//                }
//            } else {
//                //群发消息
//                for (WebSocketServerBak item : webSocketSet) {
//                    try {
//                        item.sendMessage(JSON.toJSONString(new Message()
//                                .setType(EnumMessageType.WEBSOCKET_RESPONSE_PONG.getCode())
//                                .setMsg(EnumMessageType.WEBSOCKET_RESPONSE_PONG.getName())
//                                .setDate(new Date())));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        continue;
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * 发生错误时调用
//     *
//     * @param session
//     * @param error
//     */
//    @OnError
//    public void onError(Session session, Throwable error) {
//        log.error("[WebSocket] 用户错误:" + this.userId + ",原因:" + error);
//    }
//
//    /**
//     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
//     *
//     * @param message
//     * @throws IOException
//     */
//    public synchronized void sendMessage(Object message) throws IOException {
////        this.session.getBasicRemote().sendText(message);
//        //this.session.getAsyncRemote().sendText(message);
//
//        synchronized (this.session) {
//            if (this.session.isOpen()) {
//                if (message instanceof String) {
//    /*					String msg = ((String) message).substring(0, 24);
//                        log.info("====sendMessage====" + msg);*/
//                    this.session.getBasicRemote().sendText((String) message);// 同步发送
//                    //                this.session.getAsyncRemote().sendText((String) message); // 异步发送
//                } else if (message instanceof ByteBuffer) {
//                    this.session.getBasicRemote().sendBinary((ByteBuffer) message);
//                    //                this.session.getAsyncRemote().sendBinary((ByteBuffer) message);
//                }
//            }
//        }
//    }
//
//
//    public boolean exitUser(String userId) {
//        return webSocketMap.containsKey(userId);
//    }
//
//    public CopyOnWriteArraySet<WebSocketServerBak> getUserSocketSet(String userId) {
//        return webSocketMap.get(userId);
//    }
//
//    public void userCountIncrease(String userId) {
//        if (count.containsKey(userId)) {
//            count.put(userId, count.get(userId) + 1);
//        }
//    }
//
//
//    public void userCountDecrement(String userId) {
//        if (count.containsKey(userId)) {
//            count.put(userId, count.get(userId) - 1);
//        }
//    }
//
//    public void removeUserConunt(String userId) {
//        count.remove(userId);
//    }
//
//    public Integer getCurrUserCount(String userId) {
//        return count.get(userId);
//    }
//
//    private void initUserInfo(String userId) {
//        CopyOnWriteArraySet<WebSocketServerBak> webSocketTestSet = new CopyOnWriteArraySet<WebSocketServerBak>();
//        webSocketTestSet.add(this);
//        webSocketMap.put(userId, webSocketTestSet);
//        count.put(userId, 1);
//    }
//}
