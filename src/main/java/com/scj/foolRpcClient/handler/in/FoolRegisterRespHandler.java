package com.scj.foolRpcClient.handler.in;

import com.scj.foolRpcClient.entity.FoolProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author suchangjie.NANKE
 * @Title: FoolRegisterRespHandler
 * @date 2023/8/24 23:01
 * @description 注册中心响应处理器
 */
public class FoolRegisterRespHandler extends SimpleChannelInboundHandler<FoolProtocol<Object>> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext
            , FoolProtocol<Object> foolProtocol) throws Exception {
        byte remoteType = foolProtocol.getRemoteType();
        // TODO
    }
}
