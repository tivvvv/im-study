package com.tiv.im.study.realtime.communicate.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息类型枚举
 */
@Getter
@AllArgsConstructor
public enum MessageTypeEnum {

    /**
     * 响应
     */
    ACK(1),

    /**
     * 心跳
     */
    HEART_BEAT(2),

    /**
     * 登出
     */
    LOG_OUT(3),

    /**
     * 非法
     */
    ILLEGAL(100);

    private final Integer code;

    public static MessageTypeEnum of(Integer type) {
        for (MessageTypeEnum value : values()) {
            if (value.code.equals(type)) {
                return value;
            }
        }
        return ILLEGAL;
    }

}
