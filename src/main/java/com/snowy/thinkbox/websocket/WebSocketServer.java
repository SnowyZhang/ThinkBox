package com.snowy.thinkbox.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/ws/{token}")
public class WebSocketServer {

    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    // 存储所有 WebSocket 连接
    private static HashMap<String,Session> webSocketMap = new HashMap<>();

    private String token;    // 用于标识客户端的 token（可以是用户 ID 或者其他标识符）

    /**
     * 建立连接时调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        this.token = token;
        webSocketMap.put(token,session); // 将当前连接添加到 WebSocket 集合中
        log.info("New connection established with token: {}", token);
    }

    /**
     * 接收客户端消息时调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("Received message from token {}: {}", token, message);
    }

    /**
     * 连接关闭时调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        webSocketMap.remove(this.token); // 从连接集合中移除当前连接
        log.info("Connection closed for token: {}, session :{}", this.token,session.getId());
    }

    /**
     * 发生错误时调用的方法
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("Error occurred for token {}: {}", token, error.getMessage());
        error.printStackTrace();
    }

    /**
     * 向客户端发送消息
     */
    public void sendMessage(Session session, String message) {
        try {
            if (session != null && session.isOpen()) {
                session.getBasicRemote().sendText(message); // 发送消息给客户端
                log.info("Sent message to token {}: {}", token, message);
            }
        } catch (IOException e) {
            log.error("Error sending message to token {}: {}", token, e.getMessage());
        }
    }

    /**
     * 向所有连接的客户端广播消息
     */
    public void broadcastMessage(String message) throws IOException {
        for(String token : webSocketMap.keySet()) {
            Session session = webSocketMap.get(token);
            try {
                if(session!=null&&session.isOpen()){
                    session.getBasicRemote().sendText(message); // 发送消息给客户端
                    log.info("Broadcast message to token {}: {}", token, message);
                }
            }catch (IOException e) {
                log.error("Error broadcasting message to token {}: {}", token, message);
            }
        }
    }
}
