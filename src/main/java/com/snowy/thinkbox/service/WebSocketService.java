package com.snowy.thinkbox.service;

import com.snowy.thinkbox.websocket.WebSocketServer;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WebSocketService {
    @Resource
    WebSocketServer webSocketServer;

    public void sendMessage(String message) {
        try {
            webSocketServer.broadcastMessage(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
