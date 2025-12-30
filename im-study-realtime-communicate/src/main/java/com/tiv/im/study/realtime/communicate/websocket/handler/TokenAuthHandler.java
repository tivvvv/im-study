package com.tiv.im.study.realtime.communicate.websocket.handler;

import com.tiv.im.study.realtime.communicate.utils.NettyUtil;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * Token认证处理器
 */
@Slf4j
@Sharable
public class TokenAuthHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String userId = Optional.ofNullable(request.headers().get("userId")).map(CharSequence::toString).orElse("");
            String token = Optional.ofNullable(request.headers().get("token")).map(CharSequence::toString).orElse("");

            NettyUtil.setAttr(ctx.channel(), NettyUtil.USER_ID, userId);
            NettyUtil.setAttr(ctx.channel(), NettyUtil.TOKEN, token);

            ctx.pipeline().remove(this);
            ctx.fireChannelRead(request);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

}
