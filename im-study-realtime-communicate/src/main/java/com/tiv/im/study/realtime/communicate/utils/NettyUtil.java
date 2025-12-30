package com.tiv.im.study.realtime.communicate.utils;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Sharable
public class NettyUtil {

    public static AttributeKey<String> TOKEN = AttributeKey.valueOf("token");

    public static AttributeKey<String> USER_ID = AttributeKey.valueOf("userId");

    public static AttributeKey<WebSocketServerHandshaker> HANDSHAKE = AttributeKey.valueOf(WebSocketServerHandshaker.class, "HANDSHAKE");

    public static <T> void setAttr(Channel channel, AttributeKey<T> attributeKey, T data) {
        channel.attr(attributeKey).set(data);
    }

    public static <T> T getAttr(Channel channel, AttributeKey<T> key) {
        return channel.attr(key).get();
    }

}
