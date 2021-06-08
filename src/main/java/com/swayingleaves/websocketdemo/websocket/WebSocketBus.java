package com.swayingleaves.websocketdemo.websocket;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.swayingleaves.websocketdemo.con.MsgType;
import com.swayingleaves.websocketdemo.entity.Msg;
import com.swayingleaves.websocketdemo.user.WebSocketUserUtil;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yeeq
 * @date 2021/4/9
 */
@Component
@Slf4j
@ServerEndpoint(value = "/match/{userId}")
public class WebSocketBus {

    private Session session;

    private String userId;

    public Session getSession() {
        return session;
    }

    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) {

        log.info("ChatWebsocket open 有新连接加入 userId: {}", userId);

        this.userId = userId;
        this.session = session;

        WebSocketUserUtil.addUser(userId,this);
        log.info("ChatWebsocket open 连接建立完成 userId: {}", userId);
    }

    @OnError
    public void onError(Session session, Throwable error) {

        log.error("ChatWebsocket onError 发生了错误 userId: {}, errorMessage: {}", userId, error.getMessage());
        WebSocketUserUtil.removeUser(userId);
        log.info("ChatWebsocket onError 连接断开完成 userId: {}", userId);
    }

    @OnClose
    public void onClose()
    {
        log.info("ChatWebsocket onClose 连接断开 userId: {}", userId);

        WebSocketUserUtil.removeUser(userId);
        log.info("ChatWebsocket onClose 连接断开完成 userId: {}", userId);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("ChatWebsocket onMessage userId: {}, 来自客户端的消息 message: {}", userId, message);

        final JSONObject object = JSONObject.parseObject(message);
        final String sender = object.getString("sender");
        final String msg = object.getString("msg");

        final WebSocketBus user = WebSocketUserUtil.getUser(sender);
        if (user == null){
            log.info("用户不存在或未上线");
            return;
        }
        Msg sendMsg = new Msg();
        sendMsg.setMsg(msg);
        sendMsg.setType(MsgType.U_MSG);
        user.session.getAsyncRemote().sendText(JSONObject.toJSONString(sendMsg));
        log.info("ChatWebsocket onMessage userId: {} 消息接收结束", userId);
    }


}
