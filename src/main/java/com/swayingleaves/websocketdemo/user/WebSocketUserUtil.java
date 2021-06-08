package com.swayingleaves.websocketdemo.user;

import com.swayingleaves.websocketdemo.websocket.WebSocketBus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhenglin
 * @date 2021/6/8
 */
public class WebSocketUserUtil {
    public static Map<String, WebSocketBus> total = new ConcurrentHashMap<>();

    public static void addUser(String userId, WebSocketBus bus) {
        total.put(userId, bus);
    }

    public static void removeUser(String userId) {
        total.remove(userId);
    }

    public static WebSocketBus getUser(String userId) {
        return total.get(userId);
    }
}
