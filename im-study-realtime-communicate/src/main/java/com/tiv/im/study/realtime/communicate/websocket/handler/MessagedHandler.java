package com.tiv.im.study.realtime.communicate.websocket.handler;

import cn.hutool.json.JSONUtil;
import com.tiv.im.study.realtime.communicate.constants.MessageTypeEnum;
import com.tiv.im.study.realtime.communicate.model.Message;
import com.tiv.im.study.realtime.communicate.utils.JwtUtil;
import com.tiv.im.study.realtime.communicate.utils.NettyUtil;
import com.tiv.im.study.realtime.communicate.websocket.ChannelManager;
import io.jsonwebtoken.Claims;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息处理器
 */
@Slf4j
@Sharable
public class MessagedHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        log.info("接收到消息: {}", msg.text());
        Message message = JSONUtil.toBean(msg.text(), Message.class);
        MessageTypeEnum messageTypeEnum = MessageTypeEnum.of(message.getType());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 处理心跳
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            switch (event.state()) {
                case READER_IDLE:
                    log.info("userEventTriggered--读空闲超时,关闭连接:{},用户id:{}", ctx.channel().remoteAddress(), ChannelManager.getUserIdByChannel(ctx.channel()));
                    offline(ctx);
                    break;
                case WRITER_IDLE:
                    log.info("userEventTriggered--写空闲超时");
                    break;
                case ALL_IDLE:
                    log.info("userEventTriggered--读写空闲超时");
                    break;
            }
        }

        // 处理握手
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            // 验证token
            String userId = NettyUtil.getAttr(ctx.channel(), NettyUtil.USER_ID);
            String token = NettyUtil.getAttr(ctx.channel(), NettyUtil.TOKEN);
            if (!validateToken(userId, token)) {
                log.warn("userEventTriggered--token验证失败:{},用户id:{}", ctx.channel().remoteAddress(), userId);
                ctx.close();
                return;
            }

            // 单点登录
            Channel oldChannel = ChannelManager.getChannelByUserId(userId);
            // 关闭旧的连接
            if (oldChannel != null) {
                ChannelManager.removeChannel(userId, oldChannel);
                oldChannel.close();
            }

            // 添加新的连接
            ChannelManager.addChannel(userId, ctx.channel());
            log.info("userEventTriggered--用户上线:{},用户id:{}", ctx.channel().remoteAddress(), userId);
        }
    }

    /**
     * 下线
     *
     * @param ctx
     */
    private void offline(ChannelHandlerContext ctx) {
        String userId = ChannelManager.getUserIdByChannel(ctx.channel());
        try {
            ChannelManager.removeChannel(userId, ctx.channel());
            log.info("offline--下线:{},用户id:{}", ctx.channel().remoteAddress(), userId);
        } catch (Exception e) {
            log.error("offline--下线异常:{}", e.getMessage());
        } finally {
            // 关闭channel
            if (ctx.channel() != null) {
                ctx.channel().close();
            }
        }
    }

    /**
     * 验证token
     *
     * @param userId
     * @param token
     * @return
     */
    private boolean validateToken(String userId, String token) {
        Claims claims = JwtUtil.parse(token);
        String parseUserId = claims.getSubject();
        return parseUserId != null && parseUserId.equals(userId);
    }

}
