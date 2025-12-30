package com.tiv.im.study.realtime.communicate.websocket;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * channel管理器
 */
public class ChannelManager {

    private static final ConcurrentHashMap<String, Channel> userChannelMap = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<Channel, String> channelUserMap = new ConcurrentHashMap<>();

    public static void addChannel(String userId, Channel channel) {
        userChannelMap.put(userId, channel);
        channelUserMap.put(channel, userId);
    }

    public static void removeChannel(String userId, Channel channel) {
        userChannelMap.remove(userId);
        channelUserMap.remove(channel);
    }

    public static Channel getChannelByUserId(String userId) {
        return userChannelMap.get(userId);
    }

    public static String getUserIdByChannel(Channel channel) {
        return channelUserMap.get(channel);
    }

}
