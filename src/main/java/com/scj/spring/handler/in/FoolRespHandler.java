package com.scj.spring.handler.in;

import com.scj.spring.constant.LocalCache;
import com.scj.spring.entity.FoolProtocol;
import com.scj.spring.entity.FoolResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;

/**
 * @author suchangjie.NANKE
 * @Title: FoolRespHandler
 * @date 2023/8/19 21:10
 * @description 响应处理器
 */

@ChannelHandler.Sharable
public class FoolRespHandler extends SimpleChannelInboundHandler<FoolProtocol<FoolResponse>> {

    /**
     * 设置返回值
     * @param ctx 上下文
     * @param resp 响应
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                FoolProtocol<FoolResponse> resp) {
        Promise<FoolResponse> promise = LocalCache.getPromise(resp);
        promise.setSuccess(resp.getData());
        ctx.fireChannelRead(resp);
    }
}
