package com.scj.foolRpcClient.handler;

import com.scj.foolRpcBase.constant.Constant;
import com.scj.foolRpcBase.entity.FoolCommonResp;
import com.scj.foolRpcBase.entity.FoolProtocol;
import com.scj.foolRpcBase.exception.ExceptionEnum;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author suchangjie.NANKE
 * @Title: FoolRegisterHandler
 * @date 2023/8/24 23:01
 * @description 注册中心响应处理器
 */

@Slf4j
public class FoolRegisterHandler extends SimpleChannelInboundHandler<FoolProtocol<Object>> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx
            , FoolProtocol<Object> foolProtocol) {
        byte remoteType = foolProtocol.getRemoteType();
        Object obj = foolProtocol.getData();
        if (remoteType == Constant.REGISTER_RESP_REG_CLASS) {
            // 注册bean
            FoolCommonResp data = (FoolCommonResp)obj;
            if (!data.getCode().equals(ExceptionEnum.SUCCESS.getErrorCode())){
                log.error("注册异常, code:{} message:{}", data.getCode(), data.getMessage());
            }
        }
        ctx.fireChannelRead(foolProtocol);
    }
}
