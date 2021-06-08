package com.swayingleaves.websocketdemo.entity;

import lombok.Data;

/**
 * @author zhenglin
 * @date 2021/6/8
 */
@Data
public class Msg {
    private String type;
    private String msg;
}
