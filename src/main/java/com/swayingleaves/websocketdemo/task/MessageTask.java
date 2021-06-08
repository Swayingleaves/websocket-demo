package com.swayingleaves.websocketdemo.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.swayingleaves.websocketdemo.con.MsgType;
import com.swayingleaves.websocketdemo.entity.Msg;
import com.swayingleaves.websocketdemo.user.WebSocketUserUtil;
import com.swayingleaves.websocketdemo.websocket.WebSocketBus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 模拟消息系统推送消息
 *
 * @author zhenglin
 * @date 2021/6/8
 */
@Component
@Slf4j
public class MessageTask {

    @Scheduled(cron = "0/2 * * * * ?")
    public void send() {
        for (Map.Entry<String, WebSocketBus> entry : WebSocketUserUtil.total.entrySet()) {
            final WebSocketBus user = entry.getValue();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Msg sendMsg = new Msg();
            sendMsg.setMsg(dateFormat.format(new Date()));
            sendMsg.setType(MsgType.RE);
            final String msgStr = JSONObject.toJSONString(sendMsg);
            user.getSession().getAsyncRemote().sendText(msgStr);
            log.info("发送消息to:{} data:{}", entry.getKey(), msgStr);
        }
    }
}
