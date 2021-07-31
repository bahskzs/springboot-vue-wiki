package com.yqy.wiki.service;

import com.yqy.wiki.websocket.WebSocketServer;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author bahsk
 * @createTime 2021-07-31 21:25
 * @description
 */
@Service
public class WsService {

    @Resource
    private WebSocketServer webSocketServer;

    @Async
    public void sendInfo(String message, String logId) {
        MDC.put("LOG_ID", logId);
        webSocketServer.sendInfo(message);
    }


}
