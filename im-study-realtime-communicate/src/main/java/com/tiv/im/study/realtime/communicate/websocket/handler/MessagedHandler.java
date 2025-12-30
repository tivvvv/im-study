package com.tiv.im.study.realtime.communicate.websocket.handler;

import cn.hutool.json.JSONUtil;
import com.tiv.im.study.realtime.communicate.constants.MessageTypeEnum;
import com.tiv.im.study.realtime.communicate.model.Message;
import com.tiv.im.study.realtime.communicate.websocket.ChannelManager;
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
            // 协议升级
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

}
